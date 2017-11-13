package com.haiqiu.miaohi.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;

/**
 * 设置条目
 * Created by ningl on 16/12/6.
 */
public class ItemSettingView extends LinearLayout {

    private Context context;
    private TextView tv_itemsetting_text;
    private TextView tv_itemsetting_right;
    private ImageView iv_itemsetting_arrow;

    public ItemSettingView(Context context) {
        this(context, null);
    }

    public ItemSettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        initAttribute(attrs);
    }

    private void init() {
        View view = View.inflate(context, R.layout.itemsettingview, null);
        tv_itemsetting_text = (TextView) view.findViewById(R.id.tv_itemsetting_text);
        tv_itemsetting_right = (TextView) view.findViewById(R.id.tv_itemsetting_right);
        iv_itemsetting_arrow = (ImageView) view.findViewById(R.id.iv_itemsetting_arrow);
        addView(view);
    }

    private void initAttribute(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.itemsettingview);
        String text = typedArray.getString(R.styleable.itemsettingview_text);
        boolean showArrow = typedArray.getBoolean(R.styleable.itemsettingview_showarrow, true);
        String rightText = typedArray.getString(R.styleable.itemsettingview_righttext);
        tv_itemsetting_text.setText(text);
        tv_itemsetting_right.setText(rightText);
        iv_itemsetting_arrow.setVisibility(showArrow?VISIBLE:GONE);
        typedArray.recycle();
    }

    public void setRightText(String rightText){
        tv_itemsetting_right.setText(rightText);
    }
}
