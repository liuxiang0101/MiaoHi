package com.haiqiu.miaohi.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;

/**
 * Created by zhandalin on 2016-11-29 20:40.
 * 说明:视频比例展示View
 */
public class RatioView extends LinearLayout {

    private ImageView innerRatioView;
    private TextView textView;
    private Point ratioPoint;

    public RatioView(Context context) {
        this(context, null);
    }

    public RatioView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioView);
        int ratio_x = typedArray.getInt(R.styleable.RatioView_ratio_x, 1);
        int ratio_y = typedArray.getInt(R.styleable.RatioView_ratio_y, 1);
        int size = typedArray.getDimensionPixelSize(R.styleable.RatioView_ratio_size, DensityUtil.dip2px(context, 19));
        int textSize = typedArray.getDimensionPixelSize(R.styleable.RatioView_ratio_text_size, DensityUtil.dip2px(context, 9));

        innerRatioView = new ImageView(context);
        addView(innerRatioView, new LayoutParams(size, size));
        textView = new TextView(context);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setText("1:1");
        textView.setGravity(Gravity.CENTER_HORIZONTAL);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, textSize);
        layoutParams.topMargin = DensityUtil.dip2px(context, 3);
        addView(textView, layoutParams);

        ratioPoint = new Point(ratio_x, ratio_y);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setRatio(ratioPoint);
        MHLogUtil.d("RatioView", "onMeasure");
    }

    public void setRatio(Point point) {
        if (null == point) return;
        ratioPoint = point;

//        ViewGroup.LayoutParams layoutParams = innerRatioView.getLayoutParams();
//        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        float result = 1.0f * point.x / point.y;
        if (result == 1 / 1.0f) {//1:1
            innerRatioView.setImageResource(R.drawable.selector_paishe_1_1);
//            layoutParams.height = layoutParams.width = width;
        } else if (result == 4 / 3.0f) {//4:3
            innerRatioView.setImageResource(R.drawable.selector_paishe_4_3);
//            layoutParams.width = width;
//            layoutParams.height = 3 * width / 4;
        } else if (result == 3 / 4.0f) {//3:4
            innerRatioView.setImageResource(R.drawable.selector_paishe_3_4);
//            layoutParams.width = 3 * width / 4;
//            layoutParams.height = width;
        } else if (result == 16 / 9.0f) {//16:9
            innerRatioView.setImageResource(R.drawable.selector_paishe_16_9);
//            layoutParams.width = width;
//            layoutParams.height = 9 * width / 16;
        }
//        innerRatioView.setLayoutParams(layoutParams);
        textView.setText(point.x + ":" + point.y);
    }

    public Point getRatioPoint() {
        return ratioPoint;
    }
}
