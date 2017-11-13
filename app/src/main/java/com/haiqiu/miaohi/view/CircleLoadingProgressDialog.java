package com.haiqiu.miaohi.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.haiqiu.miaohi.R;


/**
 * Created by bookzhan on 2015/8/6.
 * 最后修改者: bookzhan  version 1.0
 * 说明:圆圈的加载的对话框
 */
public class CircleLoadingProgressDialog extends Dialog {
    private TextView tvMsg;

    public CircleLoadingProgressDialog(Context context) {
        this(context, R.style.loading_dialog);
    }

    public CircleLoadingProgressDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init() {
        setContentView(R.layout.loading_layout);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
    }

    /**
     * 默认是"加载中..."
     *
     * @param message
     */
    public void setMessage(String message) {
        tvMsg.setText(message);
    }
}


