package com.haiqiu.miaohi.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haiqiu.miaohi.R;


/**
 * 自定义Toast
 * Created by zhandalin on 2015/6/18.
 * 都没维护这个类了,建议使用BaseActivity里面的方法
 */
@Deprecated
public class ToastUtils {

    @Deprecated
    public static void showToastAtCenter(Context context, String content) {
        hiddenKeyboard(context);
        Toast mToast = new Toast(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView textView = new TextView(context);
        textView.setTextColor(Color.WHITE);
        int paddingLeftAndRight = DensityUtil.dip2px(context, 14);
        int paddingTopAndBottom = DensityUtil.dip2px(context, 7);
        textView.setPadding(paddingLeftAndRight, paddingTopAndBottom, paddingLeftAndRight, paddingTopAndBottom);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textView.setGravity(Gravity.CENTER);
        textView.setId(android.R.id.message);
        textView.setText(content);
        textView.setMinimumWidth(DensityUtil.dip2px(context, 200));
        textView.setBackgroundColor(context.getResources().getColor(R.color.transparent_70));

        linearLayout.addView(textView);
        linearLayout.setAlpha(0.8f);
        mToast.setView(linearLayout);
        mToast.setMargin(0, 0);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    @Deprecated
    public static void showToastAtBottom(Context context, String message) {
        hiddenKeyboard(context);
        Toast mToast = new Toast(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView textView = new TextView(context);
        textView.setTextColor(Color.WHITE);
        int paddingLeftAndRight = DensityUtil.dip2px(context, 14);
        int paddingTopAndBottom = DensityUtil.dip2px(context, 7);
        textView.setPadding(paddingLeftAndRight, paddingTopAndBottom, paddingLeftAndRight, paddingTopAndBottom);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textView.setGravity(Gravity.CENTER);
        textView.setId(android.R.id.message);
        textView.setText(message);
        textView.setMinimumWidth(DensityUtil.dip2px(context, 200));
        textView.setBackgroundColor(context.getResources().getColor(R.color.transparent_70));
        linearLayout.addView(textView);
        linearLayout.setAlpha(0.8f);
        mToast.setView(linearLayout);
        mToast.setMargin(0, 0);
        mToast.setGravity(Gravity.BOTTOM, 0, DensityUtil.dip2px(context, 70));
        mToast.show();
    }

    /**
     * 正方形的toast
     *
     * @param iconResourceId 要显示的图片
     * @param message        显示的内容
     */
    @Deprecated
    public static void showRectangleToast(Context context, int iconResourceId, String message) {

        Toast rectangleToast = new Toast(context);
        LinearLayout container = new LinearLayout(context);
        container.setGravity(Gravity.CENTER);

        LinearLayout linearLayout = new LinearLayout(context);
        int size = DensityUtil.dip2px(context, 114);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(size, size));
        linearLayout.setBackgroundResource(R.drawable.shape_corner_rectangle);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(iconResourceId);

        TextView textView = new TextView(context);
        textView.setTextColor(Color.WHITE);
        textView.setPadding(0, DensityUtil.dip2px(context, 14), 0, 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        textView.setGravity(Gravity.CENTER);
        textView.setId(android.R.id.message);
        textView.setText(message);

        linearLayout.addView(imageView);
        linearLayout.addView(textView);
        container.addView(linearLayout);
        rectangleToast.setView(container);

        rectangleToast.setGravity(Gravity.CENTER, 0, 0);
        rectangleToast.show();
    }

    public static void hiddenKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}



