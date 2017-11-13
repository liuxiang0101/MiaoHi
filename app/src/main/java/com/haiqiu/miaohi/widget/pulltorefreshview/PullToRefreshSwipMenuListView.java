package com.haiqiu.miaohi.widget.pulltorefreshview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.widget.swipemenulistview.SwipeMenuListView;
import com.haiqiu.miaohi.widget.pulltorefreshview.ILoadingLayout.State;

/**
 * 这个类实现了ListView下拉刷新，上加载更多和滑到底部自动加载
 *
 * @author Li Hong
 * @since 2013-8-15
 */
public class PullToRefreshSwipMenuListView extends PullToRefreshBase<SwipeMenuListView> implements OnScrollListener {

    /**
     * ListView
     */
    private SwipeMenuListView mListView;
    /**
     * 用于滑到底部自动加载的Footer
     */
    private LoadingLayout mLoadMoreFooterLayout;
    /**
     * 滚动的监听器
     */
    private OnScrollListener mScrollListener;
    private boolean lastItemVisible;

    /**
     * 构造方法
     *
     * @param context context
     */
    public PullToRefreshSwipMenuListView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public PullToRefreshSwipMenuListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public PullToRefreshSwipMenuListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setPullLoadEnabled(false);
    }

    @Override
    protected SwipeMenuListView createRefreshableView(Context context, AttributeSet attrs) {
        SwipeMenuListView listView = new SwipeMenuListView(context);
        listView.setOverScrollMode(OVER_SCROLL_NEVER);
        listView.setDivider(context.getResources().getDrawable(R.color.list_divider));
        listView.setDividerHeight(1);
        mListView = listView;
        listView.setOnScrollListener(this);

        return listView;
    }


    /**
     * 设置滑动的监听器
     *
     * @param l 监听器
     */
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    protected boolean isReadyForPullUp() {
        return isLastItemVisible();
    }

    @Override
    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    @Override
    protected void startLoading() {
        super.startLoading();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(State.REFRESHING);
        }
    }


    @Override
    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        super.setScrollLoadEnabled(scrollLoadEnabled);

        if (scrollLoadEnabled) {
            // 设置Footer
            if (null == mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
            }

            if (null == mLoadMoreFooterLayout.getParent()) {
                mListView.addFooterView(mLoadMoreFooterLayout, null, false);
            }
            mLoadMoreFooterLayout.show(true);
        } else {
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.show(false);
            }
        }
    }

    @Override
    public LoadingLayout getFooterLoadingLayout() {
        if (isScrollLoadEnabled()) {
            return mLoadMoreFooterLayout;
        }

        return super.getFooterLoadingLayout();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isScrollLoadEnabled() && hasMoreData()) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
                if (isReadyForPullUp()) {
                    startLoading();
                }
            }
        }

        if (null != mScrollListener) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (null != mScrollListener) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        if (null != mRefreshListener && autoLoadMoreIsEnable) {
            int tempPosition = firstVisibleItem + visibleItemCount + autoLoadMoreBackwardPosition;
            if (tempPosition == totalItemCount && tempPosition != lastPosition) {//保证只是执行一次
                lastPosition = tempPosition;
                isAutoLoading = true;
                mLoadMoreState = ILoadingLayout.State.REFRESHING;
                mRefreshListener.onLoadMore();
            }
        }
        lastItemVisible = isLastItemVisible();
        if (isAutoLoading && lastItemVisible && hasMoreData) {
            smoothScrollToFooter(mFooterHeight, getSmoothScrollDuration(), 0);
        }
        if (mLoadMoreState != State.REFRESHING && mPullDownState != State.REFRESHING
                && hasMoreData && lastItemVisible) {
            //数据量太小就不把尾部滚动上去了,并且不处理自动刷新了
            if (getItemCount() >= MIN_PAGE_SIZE) {
                if (null != mRefreshListener) {
                    smoothScrollToFooter(mFooterHeight, getSmoothScrollDuration(), 0);
                    mLoadMoreState = ILoadingLayout.State.REFRESHING;
                    LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
                    if (null != footerLoadingLayout) {
                        footerLoadingLayout.onStateChanged(mLoadMoreState, ILoadingLayout.State.RESET);
                        footerLoadingLayout.setState(mLoadMoreState);
                    }
                    mRefreshListener.onLoadMore();
                }
            }
        }
    }


    /**
     * 表示是否还有更多数据
     *
     * @return true表示还有更多数据
     */
    private boolean hasMoreData() {
        if ((null != mLoadMoreFooterLayout) && (mLoadMoreFooterLayout.getState() == State.NO_MORE_DATA)) {
            return false;
        }

        return true;
    }

    /**
     * 判断第一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isFirstItemVisible() {
        final Adapter adapter = mListView.getAdapter();

        if (null == adapter || adapter.getCount() == 0) {
            return true;
        }

        int mostTop = (mListView.getChildCount() > 0) ? mListView.getChildAt(0).getTop() : 0;
        if (mostTop >= 0) {
            return true;
        }

        return false;
    }

    /**
     * 判断最后一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isLastItemVisible() {
        final Adapter adapter = mListView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = mListView.getLastVisiblePosition();

        /**
         * This check should really just be: lastVisiblePosition == lastItemPosition, but ListView
         * internally uses a FooterView which messes the positions up. For me we'll just subtract
         * one to account for it and rely on the inner condition which checks getBottom().
         */
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - mListView.getFirstVisiblePosition();
            final int childCount = mListView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = mListView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= mListView.getBottom();
            }
        }

        return false;
    }

    @Override
    protected void scrollContentUp(final int dy) {
        if (null == mListView) return;
        post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mListView.scrollListBy(dy);
                }
            }
        });
    }

    @Override
    protected int getItemCount() {
        if (null != mListView && null != mListView.getAdapter())
            return mListView.getAdapter().getCount();
        return 0;
    }
}
