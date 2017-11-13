package com.haiqiu.miaohi.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhandalin on 2016-12-27 21:41.
 * 说明:准备加载指示器
 */
public class RefreshReadingIndicator extends BaseIndicatorController {

    private float scaleFloats;
    private float size = 4;//Indicator的大小,dp
    private float space = 2;//Indicator的间距,dp
    private float totalWidth;//指示器的总宽度


    @Override
    public void setTarget(View target) {
        super.setTarget(target);
        size = dp2px(context, size);
        space = dp2px(context, space);
        totalWidth = size * 3 + space * 2;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        paint.setAlpha(255);
        canvas.drawCircle((getWidth() - totalWidth) / 2 + size / 2, getHeight() / 2, size / 2, paint);

        if (scaleFloats == 0) {//这个是表示第二个是显示的
            canvas.drawCircle((getWidth() - totalWidth) / 2 + size / 2 + (size + space), getHeight() / 2, size / 2, paint);
            paint.setAlpha(0);
            canvas.drawCircle((getWidth() - totalWidth) / 2 + size / 2 + 2 * (size + space), getHeight() / 2, size / 2, paint);
        } else if (scaleFloats == 1) {//这个是表示第二,三个是显示的
            canvas.drawCircle((getWidth() - totalWidth) / 2 + size / 2 + (size + space), getHeight() / 2, size / 2, paint);
            paint.setAlpha(255);
            canvas.drawCircle((getWidth() - totalWidth) / 2 + size / 2 + 2 * (size + space), getHeight() / 2, size / 2, paint);
        } else {
            paint.setAlpha(0);
            canvas.drawCircle((getWidth() - totalWidth) / 2 + size / 2 + (size + space), getHeight() / 2, size / 2, paint);
            canvas.drawCircle((getWidth() - totalWidth) / 2 + size / 2 + 2 * (size + space), getHeight() / 2, size / 2, paint);
        }
        canvas.restore();
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();

        ValueAnimator animator = ObjectAnimator.ofFloat(-1,0,0,1);
        animator.setDuration(600);

        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.REVERSE);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scaleFloats = (float) animation.getAnimatedValue();
                if (scaleFloats < 0.5 && scaleFloats >= 0) {
                    scaleFloats = 0;
                } else if (scaleFloats >= 0.5) {
                    scaleFloats = 1;
                } else {
                    scaleFloats = -1;
                }

                postInvalidate();
            }
        });
        animator.start();
        animators.add(animator);
        return animators;
    }

    private float dp2px(Context context, float dpValue) {
        return context.getResources().getDisplayMetrics().density * dpValue;
    }

}
