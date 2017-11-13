package com.haiqiu.miaohi.widget.pulltorefreshview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.widget.AVLoadingIndicatorView;


/**
 * 这个类封装了下拉刷新的布局
 *
 * @author Li Hong
 * @since 2013-7-30
 */
public class FooterLoadingLayout extends LoadingLayout {
    /**
     * 进度条
     */
//    private ProgressBar mProgressBar;
    private AVLoadingIndicatorView mProgressBar;
    /**
     * 显示的文本
     */
    private TextView mHintView;
    private View container;

    /**
     * 构造方法
     *
     * @param context context
     */
    public FooterLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public FooterLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context context
     */
    private void init(Context context) {
        mProgressBar = (AVLoadingIndicatorView) findViewById(R.id.pull_to_load_footer_progressbar);
        mHintView = (TextView) findViewById(R.id.pull_to_load_footer_hint_textview);

        setState(State.RESET);
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        container = LayoutInflater.from(context).inflate(R.layout.pull_to_load_footer, null);
        return container;
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
    }

    @Override
    public int getContentSize() {
        if (null != container) {
            int height = container.getHeight();
            if (height > 0)
                return height;
        }
        return getResources().getDimensionPixelSize(R.dimen.pull_to_load_footer_height);
    }

    @Override
    protected void onStateChanged(State curState, State oldState) {
        if (State.RESET == curState) {
            mProgressBar.setVisibility(View.GONE);
        }
        if (State.NO_MORE_DATA == curState) {
            mProgressBar.setVisibility(View.GONE);
        }
        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
        mHintView.setText(R.string.pull_to_refresh_header_hint_loading);
    }

    @Override
    protected void onPullToRefresh() {
        mHintView.setText(R.string.pull_to_refresh_header_hint_normal2);
    }

    @Override
    protected void onReleaseToRefresh() {
        mHintView.setText(R.string.pull_to_refresh_header_hint_ready);
    }

    @Override
    protected void onRefreshing() {
        mProgressBar.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pull_to_refresh_header_hint_loading);
    }

    @Override
    protected void onNoMoreData() {
        mHintView.setText(R.string.pushmsg_center_no_more_msg);
    }

    @Override
    protected void hasMonrData() {
//        mHintView.setText(R.string.pushmsg_center_no_more_msg);
    }
}
