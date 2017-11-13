package com.haiqiu.miaohi.widget.pulltorefreshview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.haiqiu.miaohi.R;


/**
 * 这个类封装了下拉刷新的布局
 *
 * @author Li Hong
 * @since 2013-7-30
 */
public class HeaderLoadingLayout extends LoadingLayout {
    /**
     * Header的容器
     */
    private RelativeLayout mHeaderContainer;
    private View refreshing_view;
    private View refresh_reading_view;


    /**
     * 构造方法
     *
     * @param context context
     */
    public HeaderLoadingLayout(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public HeaderLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mHeaderContainer = (RelativeLayout) findViewById(R.id.pull_to_refresh_header_content);
        refreshing_view = findViewById(R.id.refreshing_view);
        refresh_reading_view = findViewById(R.id.refresh_reading_view);
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {

    }

    @Override
    public int getContentSize() {

        if (null != mHeaderContainer) {
            int height = mHeaderContainer.getHeight();
            if (height > 0)
                return height;
        }
        return getResources().getDimensionPixelSize(R.dimen.pull_to_refresh_header_height);
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        return LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header, null);
    }

    @Override
    protected void onReset() {
        refreshing_view.setVisibility(GONE);
        refresh_reading_view.setVisibility(VISIBLE);
    }

    @Override
    protected void onPullToRefresh() {
        refreshing_view.setVisibility(GONE);
        refresh_reading_view.setVisibility(VISIBLE);
    }

    @Override
    protected void onReleaseToRefresh() {
        refreshing_view.setVisibility(GONE);
        refresh_reading_view.setVisibility(VISIBLE);
    }

    @Override
    protected void onRefreshing() {
        refreshing_view.setVisibility(VISIBLE);
        refresh_reading_view.setVisibility(GONE);
    }


}
