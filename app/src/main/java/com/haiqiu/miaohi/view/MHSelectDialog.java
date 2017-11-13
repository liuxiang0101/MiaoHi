package com.haiqiu.miaohi.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.utils.ScreenUtils;

/**
 * 选择对话框
 * Created by ningl on 2016/5/30.
 */
public class MHSelectDialog extends Dialog implements View.OnClickListener {

    private TextView tv_message;
    private TextView tv_cancle;
    private TextView tv_ok;
    private RightCallBack rightCallBack;
    private LeftCallBack leftCallBack;

    public MHSelectDialog(Activity context) {
        super(context, R.style.MiaoHiDialog);
        setContentView(R.layout.dialog_loginout);
        tv_message = (TextView) this.findViewById(R.id.tv_message);
        tv_cancle = (TextView) this.findViewById(R.id.tv_cancle);
        tv_ok = (TextView) this.findViewById(R.id.tv_ok);
        Window win = getWindow();
        WindowManager wm = context.getWindowManager();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = 3 * (ScreenUtils.getScreenSize(context).x / 4);
        win.setAttributes(lp);
        setCancelable(false);
        tv_cancle.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
    }

    /**
     * 设置提示信息
     *
     * @param message
     */
    public MHSelectDialog setMessage(String message) {
        tv_message.setText(message);
        return this;
    }

    public MHSelectDialog setMessageGravity(int gravity) {
        tv_message.setGravity(gravity);
        return this;
    }

    /**
     * 设置左侧按钮文本
     *
     * @param left
     */
    public MHSelectDialog setLeftBtnText(String left) {
        tv_cancle.setText(left);
        return this;
    }

    /**
     * 设置左侧文本颜色
     *
     * @param color
     */
    public MHSelectDialog setLeftBtnTextColor(int color) {
        tv_cancle.setTextColor(color);
        return this;
    }

    /**
     * 设置右侧按钮文本
     *
     * @param Right
     */
    public MHSelectDialog setRightBtnText(String Right) {
        tv_ok.setText(Right);
        return this;
    }

    /**
     * 设置右侧文本颜色
     *
     * @param color
     */
    public MHSelectDialog setRightBtnTextColor(int color) {
        tv_cancle.setTextColor(color);
        return this;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle:
                if (leftCallBack == null) dismiss();
                else leftCallBack.onSelectLeft(MHSelectDialog.this);
                break;
            case R.id.tv_ok:
                if (rightCallBack == null) dismiss();
                else rightCallBack.onSelectRight(MHSelectDialog.this);
                break;
        }
    }

    /**
     * 左侧按钮添加监听
     *
     * @param leftListener
     */
    public MHSelectDialog setLeftListener(LeftCallBack leftListener) {
        this.leftCallBack = leftListener;
        return this;
    }

    /**
     * 右侧按钮添加监听
     *
     * @param rightCallBack
     */
    public MHSelectDialog setRightCallBack(RightCallBack rightCallBack) {
        this.rightCallBack = rightCallBack;
        return this;
    }

    public interface LeftCallBack {
        public void onSelectLeft(MHSelectDialog dialog);
    }

    public interface RightCallBack {
        public void onSelectRight(MHSelectDialog dialog);
    }

}
