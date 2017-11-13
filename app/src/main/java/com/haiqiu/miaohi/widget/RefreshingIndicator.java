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
 * Created by zhandalin on 2016-12-27 20:45.
 * 说明:准备加载指示器
 */
public class RefreshingIndicator extends BaseIndicatorController {

    private float[] scaleFloats = new float[2];
    private float size = 4;//Indicator的大小,dp
    private float space = 2;//Indicator的间距,dp
    private float totalWidth;//指示器的总宽度


    @Override
    public void setTarget(View target) {
        super.setTarget(target);
        size = dp2px(context, size);
        space = dp2px(context, space);
        totalWidth = (size) * (scaleFloats.length + 1) + space * scaleFloats.length;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        //前两个的动画
        if (scaleFloats[0] * 180 != 180) {
            canvas.save();
            canvas.drawCircle((getWidth() - totalWidth) / 2 + size / 2 + 2 * (size + space), getHeight() / 2, size / 2, paint);

            canvas.rotate(scaleFloats[0] * 180, getWidth() / 2 - size / 2 - space / 2, getHeight() / 2);
            for (int i = 0; i < 2; i++) {
                canvas.drawCircle((getWidth() - totalWidth) / 2 + size / 2 + i * (size + space), getHeight() / 2, size / 2, paint);
            }
            canvas.restore();
        } else {
            //后两个的动画
            canvas.save();
            canvas.drawCircle((getWidth() - totalWidth) / 2 + size / 2, getHeight() / 2, size / 2, paint);
            canvas.rotate(scaleFloats[1] * 180, getWidth() / 2 + size / 2 + space / 2, getHeight() / 2);
            for (int i = 0; i < 2; i++) {
                canvas.drawCircle((getWidth() - totalWidth) / 2 + size / 2 + (i + 1) * (size + space), getHeight() / 2, size / 2, paint);
            }
            canvas.restore();
        }
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();

        for (int i = 0; i < scaleFloats.length; i++) {
            ValueAnimator animator = ObjectAnimator.ofFloat(0, 1, 1);
            animator.setDuration(1400);
            if (i != 0)
                animator.setStartDelay(700);

            animator.setRepeatCount(-1);

            final int finalI = i;
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    scaleFloats[finalI] = animatedValue;
                    postInvalidate();
                }
            });
            animator.start();
            animators.add(animator);
        }
        return animators;
    }

    private float dp2px(Context context, float dpValue) {
        return context.getResources().getDisplayMetrics().density * dpValue;
    }

}
