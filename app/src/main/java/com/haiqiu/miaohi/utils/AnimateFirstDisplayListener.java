package com.haiqiu.miaohi.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * ImageLoader加载动画
 * Created by ningl on 17/1/12.
 */

public class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

    static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

    private final ImageView imageView;

    public AnimateFirstDisplayListener(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        if (loadedImage != null) {
            AlphaAnimation fadeImage = new AlphaAnimation(0.3f, 1f);
            fadeImage.setDuration(600);
            fadeImage.setInterpolator(new DecelerateInterpolator());
            imageView.startAnimation(fadeImage);
        }
    }
}
