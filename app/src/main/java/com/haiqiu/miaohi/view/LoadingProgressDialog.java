package com.haiqiu.miaohi.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.haiqiu.miaohi.R;


/**
 * Created by bookzhan on 2015/8/6.
 * 最后修改者: bookzhan  version 1.0
 * 说明:加载的对话框,带动画的
 */
public class LoadingProgressDialog extends Dialog {
    private AnimationDrawable animationDrawable;

    public LoadingProgressDialog(Context context) {
        this(context, R.style.loading_dialog);
    }

    public LoadingProgressDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.anim_loading);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        setContentView(imageView);
    }

    @Override
    public void show() {
        super.show();
        animationDrawable.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        animationDrawable.stop();
    }

}
