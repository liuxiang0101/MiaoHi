package com.haiqiu.miaohi.widget.pulltorefreshview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by zhandalin on 2016-11-09 13:38.
 * 说明:PullToRefreshLinearLayout
 */
public class PullToRefreshLinearLayout extends PullToRefreshBase<LinearLayout> {
    private ReadyForPullDownListener readyForPullDownListener;

    public PullToRefreshLinearLayout(Context context) {
        super(context);
    }

    public PullToRefreshLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected LinearLayout createRefreshableView(Context context, AttributeSet attrs) {
        LinearLayout linearLayout = new LinearLayout(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setOrientation(VERTICAL);
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }

    @Override
    protected boolean isReadyForPullDown() {
        if (null != readyForPullDownListener) {
            return readyForPullDownListener.isReadyForPullDown();
        }
        return false;
    }

    @Override
    protected boolean isReadyForPullUp() {
        return false;
    }


    public interface ReadyForPullDownListener {
        /**
         * 是否可以下拉刷新
         */
        boolean isReadyForPullDown();
    }

    public void setReadyForPullDownListener(ReadyForPullDownListener readyForPullDownListener) {
        this.readyForPullDownListener = readyForPullDownListener;
    }

    @Override
    protected void scrollContentUp(int dy) {
        
    }

    @Override
    protected int getItemCount() {
        return 0;
    }
}
