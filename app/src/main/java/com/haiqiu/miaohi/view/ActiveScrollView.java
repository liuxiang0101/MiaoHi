package com.haiqiu.miaohi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by ningl on 2016/5/23.
 */
public class ActiveScrollView extends ScrollView{

    private ScrollViewListener scrollViewListener;

    public ActiveScrollView(Context context) {
        super(context);
    }

    public ActiveScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActiveScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(this.scrollViewListener != null){
            scrollViewListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setOnScrollViewListener(ScrollViewListener scrollViewListener){
        this.scrollViewListener = scrollViewListener;
    }
}
