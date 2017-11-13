package com.haiqiu.miaohi.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.R;

/**
 * Created by LiuXiang on 2016/10/28.
 */
public class CustomPrivilegeDialog extends Dialog {
    private String info;
    private int image;

    //定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener {
        void back(String name);
    }


    private OnCustomDialogListener customDialogListener;

    public CustomPrivilegeDialog(Context context, String info, int image, OnCustomDialogListener customDialogListener) {
        super(context, R.style.Dialog_loading);
        this.customDialogListener = customDialogListener;
        this.info = info;
        this.image = image;
        init();
    }

    protected void init() {
        setContentView(R.layout.privilege_show_dialog);
        //设置标题
//        setTitle(name);
        TextView clickBtn = (TextView) findViewById(R.id.tv_cancle);
        TextView tv_privilege_info = (TextView) findViewById(R.id.tv_privilege_info);
        ImageView imageView = (ImageView) findViewById(R.id.iv_privilege);
        ImageView iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
        imageView.setImageResource(image);
        tv_privilege_info.setText(info);
        clickBtn.setOnClickListener(clickListener);
        iv_cancel.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CustomPrivilegeDialog.this.dismiss();
        }
    };

}
