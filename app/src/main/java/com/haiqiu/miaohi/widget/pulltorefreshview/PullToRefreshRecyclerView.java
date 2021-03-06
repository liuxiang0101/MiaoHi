package com.haiqiu.miaohi.widget.pulltorefreshview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by ningl on 2016/6/15.
 */
public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {
    RecyclerView.LayoutManager layoutManager;
    private int visibleItemCount;
    private int totalItemCount;
    private int firstVisibleItem;
    private RecyclerView recyclerView;
    private boolean lastItemVisible;

    public PullToRefreshRecyclerView(Context context) {
        super(context);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        recyclerView = new RecyclerView(context, attrs);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                layoutManager = recyclerView.getLayoutManager();
                if (null != layoutManager) {
                    LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                    visibleItemCount = manager.getChildCount();
                    totalItemCount = manager.getItemCount();
                    firstVisibleItem = manager.findFirstVisibleItemPosition();

                    if (null != mRefreshListener && autoLoadMoreIsEnable) {
                        int tempPosition = firstVisibleItem + totalItemCount + autoLoadMoreBackwardPosition;
                        if (layoutManager instanceof GridLayoutManager) {//GridLayoutManager 需要校正
                            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                            tempPosition = firstVisibleItem + (autoLoadMoreBackwardPosition + gridLayoutManager.getSpanCount()) * gridLayoutManager.getSpanCount();
                        }
                        if (tempPosition == totalItemCount && tempPosition != lastPosition) {//保证只是执行一次
                            lastPosition = tempPosition;
                            isAutoLoading = true;
                            mLoadMoreState = ILoadingLayout.State.REFRESHING;
                            mRefreshListener.onLoadMore();
                        }
                    }
                }
                lastItemVisible = isReadyForPullUp();
                if (isAutoLoading && lastItemVisible && hasMoreData) {
                    smoothScrollToFooter(mFooterHeight, getSmoothScrollDuration(), 0);
                }
                if (mLoadMoreState != ILoadingLayout.State.REFRESHING && mPullDownState != ILoadingLayout.State.REFRESHING
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
        });
        return recyclerView;
    }

    @Override
    protected boolean isReadyForPullDown() {
        if (mRefreshableView.getChildCount() <= 0)
            return true;
        RecyclerView.LayoutManager layoutManager = mRefreshableView.getLayoutManager();
        if (null == layoutManager) return true;

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
        if (firstVisiblePosition == 0)
            return mRefreshableView.getChildAt(0).getTop() == mRefreshableView.getPaddingTop();
        else
            return false;
    }

    @Override
    protected boolean isReadyForPullUp() {
        RecyclerView.Adapter adapter = mRefreshableView.getAdapter();
        if (null == adapter) return false;

        RecyclerView.LayoutManager layoutManager = mRefreshableView.getLayoutManager();
        if (null == layoutManager) return true;

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();

        final int lastItemPosition = adapter.getItemCount() - 1;
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - linearLayoutManager.findFirstVisibleItemPosition();
            final int childCount = adapter.getItemCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = mRefreshableView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= mRefreshableView.getBottom();
            }
        }
        return false;
    }

    @Override
    protected void scrollContentUp(final int dy) {
        post(new Runnable() {
            @Override
            public void run() {
                if (null != recyclerView)
                    recyclerView.scrollBy(0, dy);
            }
        });
    }
    @Override
    protected int getItemCount() {
        if (null != recyclerView && null != recyclerView.getAdapter())
            return recyclerView.getAdapter().getItemCount();
        return 0;
    }
}
