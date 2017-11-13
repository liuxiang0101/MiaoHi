package com.haiqiu.miaohi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.utils.DensityUtil;

/**
 *
 * Created by ningl on 16/12/1.
 */
public class QAButton extends RelativeLayout {

    private int bg_color;
    private String text;
    private TextView tv_qabutton_text;
    private ImageView iv_timelimitfree;
    private int with, height;
    private boolean isSetIV;

    public QAButton(Context context) {
        this(context, null);
    }

    public QAButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QAButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initAttribute(attrs);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.qabutton, this);
        tv_qabutton_text = (TextView) findViewById(R.id.tv_qabutton_text);
        iv_timelimitfree = (ImageView) findViewById(R.id.iv_timelimitfree);
    }

    private void initAttribute(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.qabutton);
        bg_color = typedArray.getInteger(R.styleable.qabutton_bgcolor, Color.parseColor("#00a2ff"));
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        with = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        int iv_size = height - DensityUtil.dip2px(getContext(), 3*2);
        TextView tv = tv_qabutton_text;
        int marginleft = 0;
        for (int i = 0; i < getChildCount(); i++) {
            marginleft += ((MarginLayoutParams)(getChildAt(i).getLayoutParams())).leftMargin;
        }
        if(tv == null) return;
        String text = tv_qabutton_text.getText().toString();
        Paint p = new Paint();
        Typeface typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        p.setTypeface(typeface);
        p.setTextSize(tv.getTextSize());
        with = iv_size + marginleft + getTextWidth(p, text) + DensityUtil.dip2px(getContext(), 21);
        setMeasuredDimension(with, height);
        setBg(bg_color);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setBg(int color){
        int iv_size = height - DensityUtil.dip2px(getContext(), 3*2);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(iv_size);
        drawable.setColor(color);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if(!isSetIV){
            isSetIV = true;
            lp.width = lp.height = iv_size;
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            lp.setMargins(DensityUtil.dip2px(getContext(), 3), 0 , 0, 0);
            iv_timelimitfree.setLayoutParams(lp);
        }
    }

    public int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    public void setText(String text){
        tv_qabutton_text.setText(text);
    }

    public void changeBg(int bg){
        setBg(bg);
    }
}
