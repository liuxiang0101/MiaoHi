package com.haiqiu.miaohi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ReplacementSpan;

import com.haiqiu.miaohi.utils.DensityUtil;

import java.lang.ref.WeakReference;

/**
 * Created by zhandalin on 2016-08-19 10:45.
 * 说明:
 */
public class MyImageSpan extends ReplacementSpan {
    private static final String TAG = "DynamicDrawableSpan";

    public static final int ALIGN_CENTER = 0;

    public static final int ALIGN_BOTTOM = 1;

    /**
     * A constant indicating that the bottom of this span should be aligned
     * with the baseline of the surrounding text.
     */
    public static final int ALIGN_BASELINE = 2;
    private int offsetY;


    private Context context;
    private Drawable drawable;
    protected final int mVerticalAlignment;

    public MyImageSpan() {
        mVerticalAlignment = ALIGN_CENTER;
    }


    public MyImageSpan(Context context, Drawable drawable, int verticalAlignment) {
        this.context = context;
        this.drawable = drawable;
        mVerticalAlignment = verticalAlignment;
        offsetY = DensityUtil.dip2px(context, 2);
    }

    public int getVerticalAlignment() {
        return mVerticalAlignment;
    }

    private WeakReference<Drawable> mDrawableRef;

    @Override
    public int getSize(Paint paint, CharSequence text,
                       int start, int end,
                       Paint.FontMetricsInt fm) {
        Drawable d = getCachedDrawable();
        Rect rect = d.getBounds();

        if (fm != null) {
            fm.ascent = -rect.bottom;
            fm.descent = 0;

            fm.top = fm.ascent;
            fm.bottom = 0;
        }

        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text,
                     int start, int end, float x,
                     int top, int y, int bottom, Paint paint) {
        Drawable b = getCachedDrawable();
        canvas.save();

        int transY = bottom - b.getBounds().bottom;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        } else if (mVerticalAlignment == ALIGN_CENTER) {
            transY = offsetY;
        }
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }

    private Drawable getCachedDrawable() {
        WeakReference<Drawable> wr = mDrawableRef;
        Drawable d = null;

        if (wr != null)
            d = wr.get();

        if (d == null) {
            d = drawable;
            mDrawableRef = new WeakReference<Drawable>(d);
        }

        return d;
    }

}
