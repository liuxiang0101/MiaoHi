package com.haiqiu.miaohi.widget.pulltorefreshview;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.widget.pulltorefreshview.ILoadingLayout.State;
import com.haiqiu.miaohi.widget.staggeredgridview.StaggeredGridView;

/**
 * 这个实现了下拉刷新和上拉加载更多的功能
 *
 * @param <T>
 * @author Li Hong
 * @since 2013-7-29
 * <p/>
 * 最后修改者:zhandalin
 * 修改内容:
 * 1,加了自动加载更多功能,默认是自动触发加载更多
 * 2,空白viw,错误view,以及加载中的loadingView
 * 3,优化了刷新与加载体验
 * 可以说现在改版的是最原生支持,并最好使的了
 */
public abstract class PullToRefreshBase<T extends View> extends LinearLayout implements IPullToRefresh<T> {
    private final static String TAG = "PullToRefreshBase";

    int autoLoadMoreBackwardPosition = 3;
    boolean autoLoadMoreIsEnable = true;
    int lastPosition;

    private Context context;
    /**
     * 是否正在自动加载更多,如果正在自动加载更多就不触发手动加载更多
     */
    boolean isAutoLoading;
    private boolean isRetring;//是否正在重试
    private View errorView;//错误View
    private View blankView;//空View
    private View loadingView;
    protected boolean hasMoreData = true;

    /**
     * 这个数只是用来本页面判断数据的,不会影响外部的页面大小判断
     */
    protected static final int MIN_PAGE_SIZE = 10;


    /**
     * 定义了下拉刷新和上拉加载更多的接口。
     *
     * @author Li Hong
     * @since 2013-7-29
     */
    public interface OnRefreshListener<V extends View> {

        /**
         * 下拉刷新
         *
         * @param refreshView 刷新的View
         */
        void onPullDownToRefresh(final PullToRefreshBase<V> refreshView);

        /**
         * 手动加载更多,自动加载更多,错误页面点击重试的回调方法
         */
        void onLoadMore();
    }

    /**
     * 回滚的时间
     */
    private static final int SCROLL_DURATION = 400;
    /**
     * 阻尼系数
     */
    private static final float OFFSET_RADIO = 2.5f;
    /**
     * 上一次移动的点
     */
    private float mLastMotionY = -1;
    private float mLastMotionX = -1;
    /**
     * 下拉刷新和加载更多的监听器
     */
    OnRefreshListener<T> mRefreshListener;
    /**
     * 下拉刷新的布局
     */
    private LoadingLayout mHeaderLayout;
    /**
     * 上拉加载更多的布局
     */
    private LoadingLayout mFooterLayout;
    /**
     * HeaderView的高度
     */
    private int mHeaderHeight;
    /**
     * FooterView的高度
     */
    protected int mFooterHeight;
    /**
     * 下拉刷新是否可用
     */
    private boolean mPullRefreshEnabled = true;
    /**
     * 上拉加载是否可用
     */
    private boolean mPullLoadEnabled = true;
    /**
     * 判断滑动到底部加载是否可用
     */
    private boolean mScrollLoadEnabled = false;
    /**
     * 是否截断touch事件
     */
    private boolean mInterceptEventEnable = true;
    /**
     * 表示是否消费了touch事件，如果是，则不调用父类的onTouchEvent方法
     */
    private boolean mIsHandledTouchEvent = false;
    /**
     * 移动点的保护范围值
     */
    private int mTouchSlop;
    /**
     * 下拉的状态
     */
    protected State mPullDownState = State.NONE;
    /**
     * 上拉的状态
     */
    protected State mLoadMoreState = State.NONE;
    /**
     * 可以下拉刷新的View
     */
    T mRefreshableView;
    /**
     * 平滑滚动的Runnable
     */
    private SmoothScrollRunnable mSmoothScrollRunnable;
    /**
     * Footer平滑滚动的Runnable
     */
    private SmoothScrollRunnable mSmoothScrollRunnableFooter;
    /**
     * 可刷新View的包装布局
     */
    private FrameLayout mRefreshableViewWrapper;

    /**
     * 构造方法
     *
     * @param context context
     */
    public PullToRefreshBase(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public PullToRefreshBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 构造方法
     *
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public PullToRefreshBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context context
     */
    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        setOrientation(LinearLayout.VERTICAL);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mHeaderLayout = createHeaderLoadingLayout(context, attrs);
        mFooterLayout = createFooterLoadingLayout(context, attrs);
        mRefreshableView = createRefreshableView(context, attrs);

        if (null == mRefreshableView) {
            throw new NullPointerException("Refreshable view can not be null.");
        }

        addRefreshableView(context, mRefreshableView);
        addHeaderAndFooter(context);

        // 得到Header的高度，这个高度需要用这种方式得到，在onLayout方法里面得到的高度始终是0
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                refreshLoadingViewsSize();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    /**
     * 初始化padding，我们根据header和footer的高度来设置top padding和bottom padding
     */
    private void refreshLoadingViewsSize() {
        // 得到header和footer的内容高度，它将会作为拖动刷新的一个临界值，如果拖动距离大于这个高度
        // 然后再松开手，就会触发刷新操作
        int headerHeight = (null != mHeaderLayout) ? mHeaderLayout.getContentSize() : 0;
        int footerHeight = (null != mFooterLayout) ? mFooterLayout.getContentSize() : 0;

        if (headerHeight < 0) {
            headerHeight = 0;
        }

        if (footerHeight < 0) {
            footerHeight = 0;
        }

        mHeaderHeight = headerHeight;
        mFooterHeight = footerHeight;

        // 这里得到Header和Footer的高度，设置的padding的top和bottom就应该是header和footer的高度
        // 因为header和footer是完全看不见的
//        headerHeight = (null != mHeaderLayout) ? mHeaderLayout.getMeasuredHeight() : 0;
//        footerHeight = (null != mFooterLayout) ? mFooterLayout.getMeasuredHeight() : 0;
        if (0 == footerHeight) {
            footerHeight = mFooterHeight;
        }

        int pLeft = getPaddingLeft();
        int pTop = getPaddingTop();
        int pRight = getPaddingRight();
        int pBottom = getPaddingBottom();

        pTop = -headerHeight;
        pBottom = -footerHeight;

        setPadding(pLeft, pTop, pRight, pBottom);
    }

    @Override
    protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // We need to update the header/footer when our size changes
        refreshLoadingViewsSize();

        // 设置刷新View的大小
        refreshRefreshableViewSize(w, h);

        /**
         * As we're currently in a Layout Pass, we need to schedule another one
         * to layout any changes we've made here
         */
        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    @Override
    public void setOrientation(int orientation) {
        if (LinearLayout.VERTICAL != orientation) {
            throw new IllegalArgumentException("This class only supports VERTICAL orientation.");
        }

        // Only support vertical orientation
        super.setOrientation(orientation);
    }

    /**
     * @param event
     * @return
     */
    @Override
    public final boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isInterceptTouchEventEnabled()) {
            return false;
        }

        if (!isPullLoadEnabled() && !isPullRefreshEnabled()) {
            return false;
        }

        final int action = event.getAction();
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsHandledTouchEvent = false;
            return false;
        }

        if (action != MotionEvent.ACTION_DOWN && mIsHandledTouchEvent) {
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = event.getY();
                mLastMotionX = event.getX();
                mIsHandledTouchEvent = false;
                break;

            case MotionEvent.ACTION_MOVE:
                final float deltaY = event.getY() - mLastMotionY;
                final float deltaX = event.getX() - mLastMotionX;

                final float absDiffY = Math.abs(deltaY);
                final float absDiffX = Math.abs(deltaX);
                if (absDiffX > absDiffY) break;

                // 这里有三个条件：
                // 1，位移差大于mTouchSlop，这是为了防止快速拖动引发刷新
                // 2，isPullRefreshing()，如果当前正在下拉刷新的话，是允许向上滑动，并把刷新的HeaderView挤上去
                // 3，isPullLoading()，理由与第2条相同
                if (absDiffY > mTouchSlop || isPullRefreshing() || isPullLoading()) {
                    mLastMotionY = event.getY();
                    // 第一个显示出来，Header已经显示或拉下
                    if (isPullRefreshEnabled() && isReadyForPullDown()) {
                        // 1，Math.abs(getScrollY()) > 0：表示当前滑动的偏移量的绝对值大于0，表示当前HeaderView滑出来了或完全
                        // 不可见，存在这样一种case，当正在刷新时并且RefreshableView已经滑到顶部，向上滑动，那么我们期望的结果是
                        // 依然能向上滑动，直到HeaderView完全不可见
                        // 2，deltaY > 0.5f：表示下拉的值大于0.5f
                        mIsHandledTouchEvent = (Math.abs(getScrollYValue()) > 0 || deltaY > 0.5f);
                        // 如果截断事件，我们则仍然把这个事件交给刷新View去处理，典型的情况是让ListView/GridView将按下
                        // Child的Selector隐藏
                        if (mIsHandledTouchEvent) {
                            mRefreshableView.onTouchEvent(event);
                        }
                    } else if (isPullLoadEnabled() && isReadyForPullUp()) {
                        // 原理如上
                        mIsHandledTouchEvent = (Math.abs(getScrollYValue()) > 0 || deltaY < -0.5f);
                    }
                }
                break;

            default:
                break;
        }
        return mIsHandledTouchEvent;
    }

    @Override
    public final boolean onTouchEvent(MotionEvent ev) {
        boolean handled = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = ev.getY();
                mIsHandledTouchEvent = false;
                if (null != loadingView && MotionEvent.ACTION_DOWN == ev.getAction()) {
                    mRefreshableViewWrapper.removeView(loadingView);
                    loadingView = null;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getY() - mLastMotionY;
                mLastMotionY = ev.getY();
                if (isPullRefreshEnabled() && isReadyForPullDown()) {
                    pullHeaderLayout(deltaY / OFFSET_RADIO);
                    handled = true;
                } else if (isPullLoadEnabled() && isReadyForPullUp()) {
                    pullFooterLayout(deltaY / OFFSET_RADIO);
                    handled = true;
                } else {
                    mIsHandledTouchEvent = false;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mIsHandledTouchEvent) {
                    mIsHandledTouchEvent = false;
                    // 当第一个显示出来时
                    if (isReadyForPullDown()) {
                        // 调用刷新
                        if (mPullRefreshEnabled && (mPullDownState == State.RELEASE_TO_REFRESH)) {
                            startRefreshing();
                            handled = true;
                        }
                        resetHeaderLayout(0);
                    }
                    if (isReadyForPullUp()) {
                        // 加载更多
                        if (getFooterLoadingLayout().getState() == State.HAS_NORE_DATA)
                            mLoadMoreState = State.RELEASE_TO_REFRESH;
                        if (isPullLoadEnabled() && (mLoadMoreState == State.RELEASE_TO_REFRESH)) {
                            startLoading();
                            handled = true;
                        }
                        resetFooterLayout();
                    }
                }
                //容错处理
                resetFooterLayout();
//                MHLogUtil.d(TAG, "getScrollY=" + getScrollY());
                break;

            default:
                break;
        }
        return handled;
    }

    @Override
    public void setPullRefreshEnabled(boolean pullRefreshEnabled) {
        mPullRefreshEnabled = pullRefreshEnabled;
    }

    @Override
    public void setPullLoadEnabled(boolean pullLoadEnabled) {
        mPullLoadEnabled = pullLoadEnabled;
    }

    @Override
    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        mScrollLoadEnabled = scrollLoadEnabled;
    }

    @Override
    public boolean isPullRefreshEnabled() {
        return mPullRefreshEnabled && (null != mHeaderLayout);
    }

    @Override
    public boolean isPullLoadEnabled() {
        return mPullLoadEnabled && (null != mFooterLayout);
    }

    @Override
    public boolean isScrollLoadEnabled() {
        return mScrollLoadEnabled;
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener<T> refreshListener) {
        mRefreshListener = refreshListener;
    }

    @Override
    public void onLoadComplete(boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
        if (isPullRefreshing()) {
            mPullDownState = State.RESET;
            onStateChanged(State.RESET, true);
            resetHeaderLayout(900);
            setInterceptTouchEventEnabled(false);

            // 回滚动有一个时间，我们在回滚完成后再设置状态为normal
            // 在将LoadingLayout的状态设置为normal之前，我们应该禁止
            // 截断Touch事件，因为设里有一个post状态，如果有post的Runnable
            // 未被执行时，用户再一次发起下拉刷新，如果正在刷新时，这个Runnable
            // 再次被执行到，那么就会把正在刷新的状态改为正常状态，这就不符合期望
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    setInterceptTouchEventEnabled(true);
                    mHeaderLayout.setState(State.RESET);
                }
            }, 900);
        } else {
            mPullDownState = State.RESET;
            onStateChanged(State.RESET, true);
        }

        if (null != loadingView) {
            mRefreshableViewWrapper.removeView(loadingView);
            loadingView = null;
        }

        isAutoLoading = false;
        if (isPullLoading()) {
            final LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
            if (!hasMoreData) {
                if (null != footerLoadingLayout) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            resetFooterLayoutByComplete();
                        }
                    }, getSmoothScrollDuration() * 2);
                    footerLoadingLayout.onStateChanged(ILoadingLayout.State.NO_MORE_DATA, mLoadMoreState);
                    footerLoadingLayout.setState(ILoadingLayout.State.NO_MORE_DATA);
                }
                setPullUpState(State.NO_MORE_DATA);
            } else if (null != footerLoadingLayout) {
                mLoadMoreState = State.RESET;
                footerLoadingLayout.onStateChanged(State.RESET, mLoadMoreState);
                footerLoadingLayout.setState(State.RESET);
                resetFooterLayoutByComplete();
            }
        } else {
            final LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
            if (!hasMoreData) {
                if (null != footerLoadingLayout) {
                    footerLoadingLayout.onStateChanged(ILoadingLayout.State.NO_MORE_DATA, mLoadMoreState);
                    footerLoadingLayout.setState(ILoadingLayout.State.NO_MORE_DATA);
                }
                setPullUpState(State.NO_MORE_DATA);
            } else if (null != footerLoadingLayout) {
                mLoadMoreState = State.RESET;
                footerLoadingLayout.onStateChanged(State.RESET, mLoadMoreState);
                footerLoadingLayout.setState(State.RESET);
            }
        }
        //这是由于我们特殊的业务逻辑导致的,如果第一页加载的小于我们请求的数,那么就还需要再去请求一次验证是否还有更多的数据....
        if (hasMoreData && getItemCount() < MIN_PAGE_SIZE && null != mRefreshListener && null == errorView && null == blankView) {
            //把当前方法执行完,放在消息队列后面
            post(new Runnable() {
                @Override
                public void run() {
                    mLoadMoreState = ILoadingLayout.State.REFRESHING;
                    LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
                    if (null != footerLoadingLayout) {
                        footerLoadingLayout.onStateChanged(mLoadMoreState, ILoadingLayout.State.RESET);
                        footerLoadingLayout.setState(mLoadMoreState);
                    }
                    mRefreshListener.onLoadMore();
                }
            });
        }
    }

    @Override
    public T getRefreshableView() {
        return mRefreshableView;
    }

    @Override
    public LoadingLayout getHeaderLoadingLayout() {
        return mHeaderLayout;
    }

    @Override
    public LoadingLayout getFooterLoadingLayout() {
        return mFooterLayout;
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        if (null != mHeaderLayout) {
            mHeaderLayout.setLastUpdatedLabel(label);
        }

        if (null != mFooterLayout) {
            mFooterLayout.setLastUpdatedLabel(label);
        }
    }

    /**
     * 开始刷新，通常用于调用者主动刷新，典型的情况是进入界面，开始主动刷新，这个刷新并不是由用户拉动引起的
     *
     * @param smoothScroll 表示是否有平滑滚动，true表示平滑滚动，false表示无平滑滚动
     */

    public void doPullRefreshing(final boolean smoothScroll) {
        try {
            T refreshableView = getRefreshableView();
            if (refreshableView instanceof StaggeredGridView) {
                StaggeredGridView staggeredGridView = (StaggeredGridView) refreshableView;
                ListAdapter adapter = staggeredGridView.getAdapter();
                if (null != adapter && adapter instanceof BaseAdapter) {
                    ((BaseAdapter) adapter).notifyDataSetChanged();
                }
                staggeredGridView.resetToTop();
            } else if (refreshableView instanceof AbsListView) {
                ((AbsListView) refreshableView).setSelection(0);
            } else if (refreshableView instanceof RecyclerView) {
                ((RecyclerView) refreshableView).scrollToPosition(0);
            }
            doPullRefreshing(smoothScroll, SCROLL_DURATION);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }

    }

    public void doPullRefreshing(final boolean smoothScroll, int time) {
        int newScrollValue = -mHeaderHeight;
        int duration = smoothScroll ? SCROLL_DURATION : 0;
        smoothScrollTo(newScrollValue, duration, 0);
        mHeaderLayout.setState(State.PULL_TO_REFRESH);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                startRefreshing();
            }
        }, time);
    }

    /**
     * 创建可以刷新的View
     *
     * @param context context
     * @param attrs   属性
     * @return View
     */
    protected abstract T createRefreshableView(Context context, AttributeSet attrs);

    /**
     * 判断刷新的View是否滑动到顶部
     *
     * @return true表示已经滑动到顶部，否则false
     */
    protected abstract boolean isReadyForPullDown();

    /**
     * 判断刷新的View是否滑动到底
     *
     * @return true表示已经滑动到底部，否则false
     */
    protected abstract boolean isReadyForPullUp();

    /**
     * @param dy 列表类容需要滚动dy像素
     */
    protected abstract void scrollContentUp(int dy);

    protected abstract int getItemCount();

    /**
     * 创建Header的布局
     *
     * @param context context
     * @param attrs   属性
     * @return LoadingLayout对象
     */
    protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
        return new HeaderLoadingLayout(context);
    }

    /**
     * 创建Footer的布局
     *
     * @param context context
     * @param attrs   属性
     * @return LoadingLayout对象
     */
    protected LoadingLayout createFooterLoadingLayout(Context context, AttributeSet attrs) {
        return new FooterLoadingLayout(context);
    }

    /**
     * 得到平滑滚动的时间，派生类可以重写这个方法来控件滚动时间
     *
     * @return 返回值时间为毫秒
     */
    protected long getSmoothScrollDuration() {
        return SCROLL_DURATION;
    }

    /**
     * 计算刷新View的大小
     *
     * @param width  当前容器的宽度
     * @param height 当前容器的宽度
     */
    protected void refreshRefreshableViewSize(int width, int height) {
        if (null != mRefreshableViewWrapper) {
            LayoutParams lp = (LayoutParams) mRefreshableViewWrapper.getLayoutParams();
            if (lp.height != height) {
                lp.height = height;
                mRefreshableViewWrapper.requestLayout();
            }
        }
    }

    /**
     * 将刷新View添加到当前容器中
     *
     * @param context         context
     * @param refreshableView 可以刷新的View
     */
    protected void addRefreshableView(Context context, T refreshableView) {
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        // 创建一个包装容器
        mRefreshableViewWrapper = new FrameLayout(context);
        mRefreshableViewWrapper.addView(refreshableView, width, height);

        // 这里把Refresh view的高度设置为一个很小的值，它的高度最终会在onSizeChanged()方法中设置为MATCH_PARENT
        // 这样做的原因是，如果此是它的height是MATCH_PARENT，那么footer得到的高度就是0，所以，我们先设置高度很小
        // 我们就可以得到header和footer的正常高度，当onSizeChanged后，Refresh view的高度又会变为正常。
        height = 10;
        addView(mRefreshableViewWrapper, new LayoutParams(width, height));
    }

    /**
     * 添加Header和Footer
     *
     * @param context context
     */
    protected void addHeaderAndFooter(Context context) {
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        final LoadingLayout headerLayout = mHeaderLayout;
        final LoadingLayout footerLayout = mFooterLayout;

        if (null != headerLayout) {
            if (this == headerLayout.getParent()) {
                removeView(headerLayout);
            }

            addView(headerLayout, 0, params);
        }

        if (null != footerLayout) {
            if (this == footerLayout.getParent()) {
                removeView(footerLayout);
            }

            addView(footerLayout, -1, params);
        }
    }

    /**
     * 拉动Header Layout时调用
     *
     * @param delta 移动的距离
     */
    protected void pullHeaderLayout(float delta) {
        // 向上滑动，并且当前scrollY为0时，不滑动
        int oldScrollY = getScrollYValue();
        if (delta < 0 && (oldScrollY - delta) >= 0) {
            setScrollTo(0, 0);
            return;
        }

        // 向下滑动布局
        setScrollBy(0, -(int) delta);

        if (null != mHeaderLayout && 0 != mHeaderHeight) {
            float scale = Math.abs(getScrollYValue()) / (float) mHeaderHeight;
            mHeaderLayout.onPull(scale);
        }

        // 未处于刷新状态，更新箭头
        int scrollY = Math.abs(getScrollYValue());
        if (isPullRefreshEnabled() && !isPullRefreshing()) {
            if (scrollY > mHeaderHeight) {
                mPullDownState = State.RELEASE_TO_REFRESH;
            } else {
                mPullDownState = State.PULL_TO_REFRESH;
            }

            mHeaderLayout.setState(mPullDownState);
            onStateChanged(mPullDownState, true);
        }
    }

    /**
     * 拉Footer时调用
     *
     * @param delta 移动的距离
     */
    protected void pullFooterLayout(float delta) {
        int oldScrollY = getScrollYValue();
        if (delta > 0 && (oldScrollY - delta) <= 0) {
            setScrollTo(0, 0);
            return;
        }

        setScrollBy(0, -(int) delta);

        if (null != mFooterLayout && 0 != mFooterHeight) {
            float scale = Math.abs(getScrollYValue()) / (float) mFooterHeight;
            mFooterLayout.onPull(scale);
        }

        int scrollY = Math.abs(getScrollYValue());
        if (isPullLoadEnabled() && !isPullLoading() && mLoadMoreState != State.NO_MORE_DATA) {
            if (scrollY > mFooterHeight) {
                mLoadMoreState = State.RELEASE_TO_REFRESH;
            } else {
                mLoadMoreState = State.PULL_TO_REFRESH;
            }

            mFooterLayout.setState(mLoadMoreState);
            onStateChanged(mLoadMoreState, false);
        }
    }

    /**
     * 得置header
     */
    public void resetHeaderLayout(long delayMillis) {
        final int scrollY = Math.abs(getScrollYValue());
        final boolean refreshing = isPullRefreshing();

        if (refreshing && scrollY <= mHeaderHeight) {
            smoothScrollTo(0, getSmoothScrollDuration(), delayMillis);
            return;
        }
        if (refreshing) {
            smoothScrollTo(-mHeaderHeight, getSmoothScrollDuration(), delayMillis);
        } else {
            smoothScrollTo(0, getSmoothScrollDuration(), delayMillis);
        }
    }

    /**
     * 重置footer
     */
    public void resetFooterLayout() {
        if (getScrollY() <= 0) return;

        MHLogUtil.d(TAG, "resetFooterLayout");
        int scrollY = Math.abs(getScrollYValue());
        boolean isPullLoading = isPullLoading();

        if (isPullLoading && scrollY <= mFooterHeight) {
            smoothScrollToFooter(0, getSmoothScrollDuration(), 0);
            return;
        }

        if (isPullLoading) {
            smoothScrollToFooter(mFooterHeight, getSmoothScrollDuration(), 0);
        } else {
            smoothScrollToFooter(0, getSmoothScrollDuration(), 0);
        }
    }

    /**
     * 重置footer
     */
    public void resetFooterLayoutByComplete() {
        if (getScrollY() <= 0) return;
        //做一个兼容处理
        if (mRefreshableView instanceof AbsListView && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            resetFooterLayout();
            return;
        }
        if (!hasMoreData) {
            resetFooterLayout();
            return;
        }
        smoothScrollToFooter(0, 0, 0);
        scrollContentUp(getScrollY());
    }


    /**
     * 判断是否正在下拉刷新
     *
     * @return true正在刷新，否则false
     */
    protected boolean isPullRefreshing() {
        return (mPullDownState == State.REFRESHING);
    }

    /**
     * 是否正的上拉加载更多
     *
     * @return true正在加载更多，否则false
     */
    protected boolean isPullLoading() {
        return (mLoadMoreState == State.REFRESHING);
    }

    /**
     * 开始刷新，当下拉松开后被调用
     */
    protected void startRefreshing() {
        // 如果正在刷新
        if (isPullRefreshing()) {
            return;
        }

        mPullDownState = State.REFRESHING;
        LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
        if (null != footerLoadingLayout) {
            footerLoadingLayout.onStateChanged(State.NONE, mLoadMoreState);
            mLoadMoreState = State.NONE;
        }
        onStateChanged(State.REFRESHING, true);

        if (null != mHeaderLayout) {
            mHeaderLayout.setState(State.REFRESHING);
        }

        if (null != mRefreshListener && !isRetring) {//正在重试就不需要下拉加载的回调了
            // 因为滚动回原始位置的时间是200，我们需要等回滚完后才执行刷新回调
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshListener.onPullDownToRefresh(PullToRefreshBase.this);
                }
            }, getSmoothScrollDuration());
        }
    }

    /**
     * 开始加载更多，上拉松开后调用
     */
    protected void startLoading() {
        // 如果正在加载
        if (isPullLoading() || mLoadMoreState == State.NO_MORE_DATA) {
            return;
        }

        mLoadMoreState = State.REFRESHING;
        onStateChanged(State.REFRESHING, false);

        if (null != mFooterLayout) {
            mFooterLayout.setState(State.REFRESHING);
        }

        if (null != mRefreshListener) {
            // 因为滚动回原始位置的时间是200，我们需要等回滚完后才执行加载回调
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isAutoLoading && mLoadMoreState != State.REFRESHING)
                        mRefreshListener.onLoadMore();
                }
            }, getSmoothScrollDuration());
        }
    }

    /**
     * 当状态发生变化时调用
     *
     * @param state      状态
     * @param isPullDown 是否向下
     */
    protected void onStateChanged(State state, boolean isPullDown) {

    }

    /**
     * 设置滚动位置
     *
     * @param x 滚动到的x位置
     * @param y 滚动到的y位置
     */
    private void setScrollTo(int x, int y) {
        scrollTo(x, y);
    }

    /**
     * 设置滚动的偏移
     *
     * @param x 滚动x位置
     * @param y 滚动y位置
     */
    private void setScrollBy(int x, int y) {
        scrollBy(x, y);
    }

    /**
     * 得到当前Y的滚动值
     *
     * @return 滚动值
     */
    private int getScrollYValue() {
        return getScrollY();
    }

    /**
     * 平滑滚动
     *
     * @param newScrollValue 滚动的值
     */
    private void smoothScrollTo(int newScrollValue) {
        smoothScrollTo(newScrollValue, getSmoothScrollDuration(), 0);
    }

    /**
     * 平滑滚动
     *
     * @param newScrollValue 滚动的值
     * @param duration       滚动时候
     * @param delayMillis    延迟时间，0代表不延迟
     */
    private void smoothScrollTo(int newScrollValue, long duration, long delayMillis) {
        if (null != mSmoothScrollRunnable) {
            mSmoothScrollRunnable.stop();
        }

        int oldScrollValue = this.getScrollYValue();
        boolean post = (oldScrollValue != newScrollValue);
        if (post) {
            mSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration);
        }
        if (post) {
            if (delayMillis > 0) {
                postDelayed(mSmoothScrollRunnable, delayMillis);
            } else {
                post(mSmoothScrollRunnable);
            }
        }
    }

    /**
     * 平滑滚动
     *
     * @param newScrollValue 滚动的值
     * @param duration       滚动时候
     * @param delayMillis    延迟时间，0代表不延迟
     */
    public void smoothScrollToFooter(int newScrollValue, long duration, long delayMillis) {
        if (null != mSmoothScrollRunnableFooter) {
            mSmoothScrollRunnableFooter.stop();
        }

        int oldScrollValue = this.getScrollYValue();
        boolean post = (oldScrollValue != newScrollValue);
        if (post) {
            mSmoothScrollRunnableFooter = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration);
        }
        if (post) {
            if (delayMillis > 0) {
                postDelayed(mSmoothScrollRunnableFooter, delayMillis);
            } else {
                post(mSmoothScrollRunnableFooter);
            }
        }
    }

    /**
     * 设置是否截断touch事件
     *
     * @param enabled true截断，false不截断
     */
    private void setInterceptTouchEventEnabled(boolean enabled) {
        mInterceptEventEnable = enabled;
    }

    /**
     * 标志是否截断touch事件
     *
     * @return true截断，false不截断
     */
    private boolean isInterceptTouchEventEnabled() {
        return mInterceptEventEnable;
    }

    /**
     * 实现了平滑滚动的Runnable
     *
     * @author Li Hong
     * @since 2013-8-22
     */
    final class SmoothScrollRunnable implements Runnable {
        /**
         * 动画效果
         */
        private final Interpolator mInterpolator;
        /**
         * 结束Y
         */
        private final int mScrollToY;
        /**
         * 开始Y
         */
        private final int mScrollFromY;
        /**
         * 滑动时间
         */
        private final long mDuration;
        /**
         * 是否继续运行
         */
        private boolean mContinueRunning = true;
        /**
         * 开始时刻
         */
        private long mStartTime = -1;
        /**
         * 当前Y
         */
        private int mCurrentY = -1;

        /**
         * 构造方法
         *
         * @param fromY    开始Y
         * @param toY      结束Y
         * @param duration 动画时间
         */
        public SmoothScrollRunnable(int fromY, int toY, long duration) {
            mScrollFromY = fromY;
            mScrollToY = toY;
            mDuration = duration;
            mInterpolator = new DecelerateInterpolator();
        }

        @Override
        public void run() {
            /**
             * If the duration is 0, we scroll the view to target y directly.
             */
            if (mDuration <= 0) {
                setScrollTo(0, mScrollToY);
                return;
            }

            /**
             * Only set mStartTime if this is the first time we're starting,
             * else actually calculate the Y delta
             */
            if (mStartTime == -1) {
                mStartTime = System.currentTimeMillis();
            } else {

                /**
                 * We do do all calculations in long to reduce software float
                 * calculations. We use 1000 as it gives us good accuracy and
                 * small rounding errors
                 */
                final long oneSecond = 1000;    // SUPPRESS CHECKSTYLE
                long normalizedTime = (oneSecond * (System.currentTimeMillis() - mStartTime)) / mDuration;
                normalizedTime = Math.max(Math.min(normalizedTime, oneSecond), 0);

                final int deltaY = Math.round((mScrollFromY - mScrollToY)
                        * mInterpolator.getInterpolation(normalizedTime / (float) oneSecond));
                mCurrentY = mScrollFromY - deltaY;

                setScrollTo(0, mCurrentY);
            }

            // If we're not at the target Y, keep going...
            if (mContinueRunning && mScrollToY != mCurrentY) {
                PullToRefreshBase.this.postDelayed(this, 16);// SUPPRESS CHECKSTYLE
            }
        }

        /**
         * 停止滑动
         */
        public void stop() {
            mContinueRunning = false;
            removeCallbacks(this);
        }
    }

    public void setPullUpState(State mState) {
        mLoadMoreState = mState;
    }


    public boolean isAutoLoadMoreIsEnable() {
        return autoLoadMoreIsEnable;
    }

    public void setAutoLoadMoreIsEnable(boolean autoLoadMoreIsEnable) {
        this.autoLoadMoreIsEnable = autoLoadMoreIsEnable;
    }

    /**
     * @param autoLoadMoreBackwardPosition 倒数第几个开始自动加载,默认是倒数第三个
     */
    public void setAutoLoadMoreBackwardPosition(int autoLoadMoreBackwardPosition) {
        this.autoLoadMoreBackwardPosition = autoLoadMoreBackwardPosition;
    }

    /**
     * 一般在pageIndex=0才错误页面显示,有内容了就不再展示错误页面,显示错误View之后就会移除空view
     */
    public void showErrorView() {
        showErrorView(0, context.getResources().getString(R.string.network_error_message));
    }

    /**
     * 一般在pageIndex=0才错误页面显示,有内容了就不再展示错误页面,显示错误View之后就会移除空view
     * <p/>
     * 不带错误icon
     *
     * @param tipMessage 错误提示信息
     */
    public void showErrorView(String tipMessage) {
        showErrorView(0, tipMessage);
    }

    /**
     * 一般在pageIndex=0才错误页面显示,有内容了就不再展示错误页面,显示错误View之后就会移除空view
     * <p/>
     * 不带错误icon
     *
     * @param imageResourceId 错误页面显示的图片
     * @param tipMessage      错误提示信息
     */
    public void showErrorView(int imageResourceId, String tipMessage) {
        isRetring = false;
        if (null != errorView) return;
        hideAllTipView();
        errorView = createErrorView();
    }

    /**
     * @param errorView 要展示的错误view
     */
    public void showErrorView(View errorView) {
        isRetring = false;
        if (null != this.errorView) return;
        hideAllTipView();
        this.errorView = errorView;
        mRefreshableViewWrapper.addView(errorView, 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        errorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRetring && mPullDownState != State.REFRESHING && null != mRefreshListener) {
                    showMHLoading();
                    mRefreshListener.onLoadMore();
                }
            }
        });
    }

    /**
     * 隐藏错误view
     */
    private void hideErrorView() {
        isRetring = false;
        if (null == errorView) return;
        mRefreshableViewWrapper.removeView(errorView);
        errorView = null;
    }


    /**
     * 隐藏所有提示的view 包括加载中的动画view,空view,错误view
     */
    public void hideAllTipView() {
        isRetring = false;
        hideErrorView();
        hideBlankView();
        if (null != loadingView) {
            mRefreshableViewWrapper.removeView(loadingView);
            loadingView = null;
        }
    }


    /**
     * 一般在pageIndex=0才显示空页面,显示空View之后就会移除错误view
     * 会有默认的图片以及文字
     */
    public void showBlankView() {
        showBlankView(0, context.getResources().getString(R.string.empty_page_message));
    }

    /**
     * 一般在pageIndex=0才显示空页面,显示空View之后就会移除错误view
     *
     * @param tipMessage 空页面的提示信息,不显示默认图片
     */
    public void showBlankView(String tipMessage) {
        showBlankView(0, tipMessage);
    }

    /**
     * 一般在pageIndex=0才显示空页面,显示空View之后就会移除错误view
     * <p/>
     * 自定义空页面的提示信息,以及默认图片
     *
     * @param imageResourceId 空页面显示的图标
     * @param tipMessage      空页面的提示信息
     */
    public void showBlankView(int imageResourceId, String tipMessage) {
        isRetring = false;
        if (null != blankView) return;
        hideAllTipView();
        blankView = createTipView(imageResourceId, tipMessage);
    }

    /**
     * @param blankView 要呈现的空view
     */
    public void showBlankView(View blankView) {
        isRetring = false;
        if (null != this.blankView) return;
        hideAllTipView();
        this.blankView = blankView;
        mRefreshableViewWrapper.addView(blankView, 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    /**
     * 隐藏空白view
     */
    private void hideBlankView() {
        isRetring = false;
        if (null == blankView) return;
        mRefreshableViewWrapper.removeView(blankView);
        blankView = null;
    }

    private View createTipView(int drawable_id, String tipMessage) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setClickable(true);
        linearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(context);
        if (0 != drawable_id) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(drawable_id);
            linearLayout.addView(imageView);
            linearLayout.setGravity(Gravity.CENTER);
        } else {
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = DensityUtil.dip2px(context, 160);
            textView.setLayoutParams(params);
            linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textView.setTextColor(context.getResources().getColor(R.color.color_999));
        textView.setPadding(0, DensityUtil.dip2px(context, 14), 0, 0);
        textView.setGravity(Gravity.CENTER);
        textView.setText(tipMessage);
        linearLayout.addView(textView);

        mRefreshableViewWrapper.addView(linearLayout, 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return linearLayout;
    }

    private View createErrorView() {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setClickable(true);
        linearLayout.setBackgroundColor(context.getResources().getColor(R.color.common_bg));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);


        TextView textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        textView.setTextColor(context.getResources().getColor(R.color.color_333));
        textView.setGravity(Gravity.CENTER);
        textView.setText("网络抽风中...");

        TextView textView_2 = new TextView(context);
        textView_2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textView_2.setTextColor(context.getResources().getColor(R.color.gray_text_1));
        int margin = DensityUtil.dip2px(context, 14);
        textView_2.setPadding(0, margin, 0, margin);
        textView_2.setGravity(Gravity.CENTER);
        textView_2.setText("每天总有那么一会儿不舒服");

        TextView textView_3 = new TextView(context);
        textView_3.setText(context.getResources().getString(R.string.network_error_message));
        textView_3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textView_3.setGravity(Gravity.CENTER);
        textView_3.setTextColor(context.getResources().getColor(R.color.color_666));
        textView_3.setBackgroundResource(R.drawable.selector_corners_stroke);
        textView_3.setLayoutParams(new ViewGroup.LayoutParams(DensityUtil.dip2px(context, 150), DensityUtil.dip2px(context, 35)));
        textView_3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRetring && mPullDownState != State.REFRESHING && null != mRefreshListener) {
                    showMHLoading();
                    mRefreshListener.onLoadMore();
                }
            }
        });

        linearLayout.addView(textView);
        linearLayout.addView(textView_2);
        linearLayout.addView(textView_3);

        mRefreshableViewWrapper.addView(linearLayout, 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return linearLayout;
    }

    /**
     * 如果是在Fragment里面请这么调用,如果是在Activity里面请直接调用showMHLoading
     */
    public void showMHLoading() {
        if (null != loadingView) return;

//        LinearLayout linearLayout = new LinearLayout(context);
//        LoadingImageView loadingImageView = new LoadingImageView(context);
//
//        if (mPullDownState == State.REFRESHING) {
//            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.topMargin = (ScreenUtils.getScreenSize(context).y - context.getResources().getDimensionPixelSize(R.dimen.navigation_height)) / 2 - DensityUtil.dip2px(context, 110) / 2 - mHeaderHeight;
//
//            linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
//            linearLayout.addView(loadingImageView, layoutParams);
//        } else {
//            linearLayout.setGravity(Gravity.CENTER);
//            linearLayout.addView(loadingImageView);
//        }
//        loadingView = linearLayout;
//        mRefreshableViewWrapper.addView(linearLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        View view = View.inflate(context, R.layout.loading_layout, null);
        int size = context.getResources().getDimensionPixelSize(R.dimen.progress_loading_size);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size, size);
        if (mPullDownState == State.REFRESHING) {
            layoutParams.topMargin = (ScreenUtils.getScreenSize(context).y - context.getResources().getDimensionPixelSize(R.dimen.navigation_height)) / 2 - DensityUtil.dip2px(context, 110) / 2 - mHeaderHeight;
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        } else {
            layoutParams.gravity = Gravity.CENTER;
        }
        loadingView = view;
        mRefreshableViewWrapper.addView(view, layoutParams);
    }

}
