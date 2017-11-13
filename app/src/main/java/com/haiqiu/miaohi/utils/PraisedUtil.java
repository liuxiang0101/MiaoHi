package com.haiqiu.miaohi.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;

/**
 * 点赞工具类
 * Created by hackest on 16/7/28.
 */
public class PraisedUtil {
    private static final String TAG="PraisedUtil";
    public static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
            return true;
        }
    });
    public static PopupWindow popupWindow;

    //点赞动画
    public static void showPop(View prise_view, final Context context, boolean isPraised, int size) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
        handler.removeMessages(1);

        View view = LayoutInflater.from(context).inflate(R.layout.heart_anim_layout, null);
        popupWindow = new PopupWindow(view, size, size);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        final ImageView imageView = (ImageView) view.findViewById(R.id.whisper_prise_animation);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = size;
        layoutParams.width = size;
        imageView.setLayoutParams(layoutParams);

        imageView.setSelected(isPraised);

        int[] location = new int[2];
        prise_view.getLocationOnScreen(location);
        try {
            if (!(((BaseActivity) context).isDestroyed())) {
                popupWindow.showAtLocation(prise_view, Gravity.NO_GRAVITY, location[0] + prise_view.getWidth() / 2 - size / 2, location[1] + prise_view.getHeight() / 2 - size / 2);
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.red_hart_disappear);
                imageView.startAnimation(animation);
                handler.sendEmptyMessageDelayed(1, 700);
            } else {
                handler.removeMessages(1);
            }
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }

    }

    //点赞动画
    public static void showPop(View prise_view, final Context context, boolean isPraised) {
        showPop(prise_view, context, isPraised, DensityUtil.dip2px(context, 44) * 2);
    }


    public static void showPopForReCommend(ImageView iv_prise_icon, final Context context, boolean isPraised) {
        View view = LayoutInflater.from(context).inflate(R.layout.heart_anim_layout, null);
        popupWindow = new PopupWindow(view, 70, 300);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        final ImageView imageView = (ImageView) view.findViewById(R.id.whisper_prise_animation);

        imageView.setSelected(isPraised);

        int[] location = new int[2];
        iv_prise_icon.getLocationOnScreen(location);

        try {
            if (!(((Activity) context).isFinishing())) {
                popupWindow.showAtLocation(iv_prise_icon, Gravity.NO_GRAVITY, (location[0] + iv_prise_icon.getWidth() / 2) - popupWindow.getWidth() / 2 + DensityUtil.px2dip(context, 50), location[1] - popupWindow.getHeight());
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.red_hart_disappear);
                imageView.startAnimation(animation);
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    imageView.setVisibility(View.GONE);
                    if (!(((Activity) context).isFinishing()) && popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            }, 800);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }


    }

}
