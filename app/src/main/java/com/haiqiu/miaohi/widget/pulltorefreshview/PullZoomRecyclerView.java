package com.haiqiu.miaohi.widget.pulltorefreshview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;

/**
 * 下拉头部放大view
 * Created by ningl on 16/11/28.
 */
public class PullZoomRecyclerView extends RecyclerView {

    /*头部View 的容器*/
    private FrameLayout mHeaderContainer;
    /*头部View 的图片*/
    private ImageView mHeaderImg;
    /*屏幕的高度*/
    private int mScreenHeight;
    /*屏幕的宽度*/
    private int mScreenWidth;

    private int mHeaderHeight;

    /*无效的点*/
    private static final int INVALID_POINTER = -1;
    /*滑动动画执行的时间*/
    private static final int MIN_SETTLE_DURATION = 200; // ms
    /*定义了一个时间插值器，根据ViewPage控件来定义的*/
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    /*记录上一次手指触摸的点*/
    private float mLastMotionX;
    private float mLastMotionY;

    /*当前活动的点Id,有效的点的Id*/
    protected int mActivePointerId = INVALID_POINTER;
    /*开始滑动的标志距离*/
    private int mTouchSlop;

    private float mScale;
    private float mLastScale;

    private final float mMaxScale = 2.0f;

    private boolean isNeedCancelParent;

    private RecyclerView.OnScrollListener mScrollListener;

    private final float REFRESH_SCALE = 1.50F;

    private OnRefreshListener mRefreshListener;

    private View rootView;
    private LinearLayout ll_minebottom;
    private FrameLayout fl;
    private View titleView;
    private int statusBarHeight;
    private OnPullZoomCallback onPullZoomCallback;
    private OnScrolledCallback onScrolledCallbackListener;
    private boolean isEnptyView = false;
    private int lastY;
    private View headerView;


    public PullZoomRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public PullZoomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullZoomRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        mScreenWidth = ScreenUtils.getScreenWidth(context);
        mHeaderHeight = (int) ((9 * 1.0f / 16) * mScreenWidth);

        super.addOnScrollListener(new InternalScrollerListener());
    }

    public void setEnptyView(boolean enptyView) {
        isEnptyView = enptyView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        getHeaderView();
        if (mHeaderContainer == null || mHeaderImg == null) return super.onTouchEvent(ev);
        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                MHLogUtil.i("ACTION_DOWN");
                /*计算 x，y 的距离*/
                int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                if (mActivePointerId == INVALID_POINTER)
                    break;
                mLastMotionX = MotionEventCompat.getX(ev, index);
//                mLastMotionY = MotionEventCompat.getY(ev, index);
                mLastMotionY = ev.getY();
                // 结束动画
                abortAnimation();
                mLastScale = (this.mHeaderContainer.getBottom() / this.mHeaderHeight);
                isNeedCancelParent = true;
                break;
            case MotionEvent.ACTION_MOVE:
                int indexMove = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, indexMove);

                if (mActivePointerId == INVALID_POINTER) {
                    /*这里相当于松手*/
                    finishPull();
                    isNeedCancelParent = true;
                } else {
//                    MHLogUtil.i("aaaaaaa", "mHeaderContainer.getBottom()=" + mHeaderContainer.getBottom() + " " + "mHeaderHeight=" + mHeaderHeight);
                    boolean isTop;
                    int firstVisiblePosition = getChildPosition(getChildAt(0));
                    if (firstVisiblePosition == 0) {
                        isTop = getChildAt(0).getTop() == getPaddingTop();
                    } else {
                        isTop = false;
                    }
                    final float y = /*MotionEventCompat.getY(ev, indexMove);*/ ev.getY();
                    float dy = y - mLastMotionY;
//                    MHLogUtil.i("PullZoomRecyclerView", dy+"");
                    //FIXME 边界控制 非最佳方式
                    if (isTop&&(dy>0&&dy<100)&&mHeaderContainer.getBottom() >= mHeaderHeight) {
                        ViewGroup.LayoutParams params = this.mHeaderContainer
                                .getLayoutParams();
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mHeaderImg.getLayoutParams();
                        float f = ((y - this.mLastMotionY + this.mHeaderContainer
                                .getBottom()) / this.mHeaderHeight - this.mLastScale)
                                / 2.0F + this.mLastScale;

//                        MHLogUtil.i("mLastScale", this.mLastScale + "");
//                        MHLogUtil.i("fffff", mLastScale + "=" + this.mLastScale + "   " + "f=" + "" + f + "");
                        /*这里设置紧凑度*/
                        dy = dy * 0.5f * (mHeaderHeight * 1.0f / params.height);
                        mLastScale = (dy + params.height) * 1.0f / mHeaderHeight;
                        mScale = clamp(mLastScale, 1.0f, mMaxScale);

                        params.height = (int) (mHeaderHeight * mScale);
                        lp.height = params.height;
                        mHeaderContainer.setLayoutParams(params);
                        mHeaderImg.setLayoutParams(lp);
                        mLastMotionY = y;
                        if (isNeedCancelParent) {
                            isNeedCancelParent = false;
                            MotionEvent motionEvent = MotionEvent.obtain(ev);
                            motionEvent.setAction(MotionEvent.ACTION_CANCEL);
                            super.onTouchEvent(motionEvent);
                        }
                        return true;
                    }
                    mLastMotionY = MotionEventCompat.getY(ev, indexMove);

                }

                break;
            case MotionEvent.ACTION_UP:

                finishPull();

                break;
            case MotionEvent.ACTION_POINTER_UP:

                int pointUpIndex = MotionEventCompat.getActionIndex(ev);
                int pointId = MotionEventCompat.getPointerId(ev, pointUpIndex);
                if (pointId == mActivePointerId) {
                    /*松手执行结束拖拽操作*/
                    finishPull();
                }

                break;

        }

        return super.onTouchEvent(ev);
    }

    @Override
    public void setOnScrollListener(RecyclerView.OnScrollListener l) {
        mScrollListener = l;
    }

    private void abortAnimation() {

    }

    private void finishPull() {
        mActivePointerId = INVALID_POINTER;
        if (mHeaderContainer.getBottom() > mHeaderHeight) {
            Log.v("zgy", "===super====onTouchEvent========");
            if (mScale > REFRESH_SCALE) {
                if (mRefreshListener != null) {
                    mRefreshListener.onRefresh();
                }
            }
            pullBackAnimation();
        }
    }

    private void pullBackAnimation() {
        ValueAnimator pullBack = ValueAnimator.ofFloat(mScale, 1.0f);
        pullBack.setInterpolator(sInterpolator);
        pullBack.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) mHeaderContainer.getLayoutParams();
                params.height = (int) (mHeaderHeight * value);
                mHeaderContainer.setLayoutParams(params);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mHeaderImg.getLayoutParams();
                lp.height = (int) (mHeaderHeight * value);
                mHeaderImg.setLayoutParams(lp);
            }
        });
        pullBack.setDuration((long) (MIN_SETTLE_DURATION * mScale));
        pullBack.start();

    }


    /**
     * 通过事件和点的 id 来获取点的索引
     *
     * @param ev
     * @param id
     * @return
     */
    private int getPointerIndex(MotionEvent ev, int id) {
        int activePointerIndex = MotionEventCompat.findPointerIndex(ev, id);
        if (activePointerIndex == -1)
            mActivePointerId = INVALID_POINTER;
        return activePointerIndex;
    }


    public void setOnRefreshListener(OnRefreshListener l) {
        mRefreshListener = l;
    }

    public ImageView getHeaderImageView() {
        return this.mHeaderImg;
    }


    private float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    private class InternalScrollerListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            getHeaderView();
            if (mScrollListener != null) {
                mScrollListener.onScrollStateChanged(recyclerView, newState);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            getHeaderView();
            if (null != onScrolledCallbackListener)
                onScrolledCallbackListener.onScrolled(recyclerView, dx, dy);
            if (onPullZoomCallback != null) onPullZoomCallback.callback(marginStatusBarPercent());
//            MHLogUtil.i("zoomonTopPercent", marginStatusBarPercent() + "");
            if (mHeaderContainer == null || mHeaderImg == null) return;
            float diff = mHeaderHeight - mHeaderContainer.getBottom();
            if ((diff > 0.0F) && (diff < mHeaderHeight)) {
                int i = (int) (0.3D * diff);
                mHeaderImg.scrollTo(0, -i);
            } else if (mHeaderImg.getScrollY() != 0) {
                mHeaderImg.scrollTo(0, 0);
            }
            if (mScrollListener != null) {
                mScrollListener.onScrolled(recyclerView, dx, dy);
            }
        }
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public void computeRefresh() {
        if (mActivePointerId != INVALID_POINTER) {

        }
    }

    public void setHeaderImage(int img) {
        if (mHeaderImg != null) mHeaderImg.setImageResource(img);
    }

    /**
     * 获取头部布局
     *
     * @return
     */
    public View getHeaderView() {
        if(headerView != null) return headerView;
        if(getLayoutManager().getItemCount()!=0){
            headerView = getLayoutManager().findViewByPosition(0);
            mHeaderImg = (ImageView) headerView.findViewById(R.id.iv_headerbg);
            mHeaderContainer = (FrameLayout) headerView.findViewById(R.id.fl_mineheadercontainer);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mHeaderContainer.getLayoutParams();
            lp.height = mHeaderHeight;
            mHeaderContainer.setLayoutParams(lp);
        }
        return headerView;
    }

    public ImageView getBgImgeView() {
        return mHeaderImg;
    }

    public void getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
    }

    public float marginStatusBarPercent() {
        float offset = getHeaderView().getTop();
//        MHLogUtil.i("距离顶部距离", getHeaderView().getTop()+"  "+(mHeaderHeight - offset)/mHeaderHeight);
        return Math.abs(offset/(float)mHeaderHeight);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isEnptyView)
            if (canScrollVertically(1)) {
                requestDisallowInterceptTouchEvent(true);
            }
        return super.dispatchTouchEvent(ev);
    }

    public interface OnPullZoomCallback {
        int[] getLocation();

        void callback(float percent);
    }

    public void setOnPullZoomListener(OnPullZoomCallback onPullZoomCallback) {
        this.onPullZoomCallback = onPullZoomCallback;
    }

    public interface OnScrolledCallback {
        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

    public void setOnScrolledCallbackListener(OnScrolledCallback onScrolledCallbackListener) {
        this.onScrolledCallbackListener = onScrolledCallbackListener;
    }

}
