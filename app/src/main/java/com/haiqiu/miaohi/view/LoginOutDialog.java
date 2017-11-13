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
 * Created by ningl on 2016/5/30.
 */
public class LoginOutDialog extends Dialog implements View.OnClickListener{

    private TextView tv_message;
    private TextView tv_cancle;
    private TextView tv_ok;
    private LoginOutDialogCallBack callBack;

    public LoginOutDialog(Activity context, LoginOutDialogCallBack callBack) {
        super(context, R.style.MiaoHiDialog);
        this.callBack = callBack;
        setContentView(R.layout.dialog_loginout);
        tv_message = (TextView) this.findViewById(R.id.tv_message);
        tv_cancle = (TextView) this.findViewById(R.id.tv_cancle);
        tv_ok = (TextView) this.findViewById(R.id.tv_ok);
        Window win = getWindow();
        WindowManager wm = context.getWindowManager();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = 3*(ScreenUtils.getScreenSize(context).x/4);
        win.setAttributes(lp);
        setCancelable(false);
        tv_cancle.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
    }
public void setMessage(String message){
    tv_message.setText(message);
}
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancle:
                callBack.onCancle(LoginOutDialog.this);
                break;
            case R.id.tv_ok:
                callBack.onOk(LoginOutDialog.this);
                break;
        }
    }

    public interface LoginOutDialogCallBack{
        public void onCancle(Dialog dialog);
        public void onOk(Dialog dialog);
    }

}
