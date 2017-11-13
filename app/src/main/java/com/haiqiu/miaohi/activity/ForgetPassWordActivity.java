package com.haiqiu.miaohi.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.LoginBean;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.LoginResponse;
import com.haiqiu.miaohi.response.VerificationCodeResponse;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.InputVerificationUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MobileUtils;
import com.haiqiu.miaohi.utils.SysMethod;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.view.CommonNavigation;

/**
 * 忘记密码
 * Created by Guoy on 2016/4/21.
 */
public class ForgetPassWordActivity extends BaseActivity {
    private TextView act_register_phone_code;
    private EditText act_register_phone_number;
    private EditText act_register_password;
    private EditText act_register_verify_number;
    private TextView act_register_btn_verify;
    private TextView act_register_btn_next;
    private CommonNavigation commonnavigation;
    private boolean isSendedVerificationCode = false;
    private TimeCount mTimeCount = new TimeCount(60000, 1000);
    private int jumpType;
    private final int DIALOG_FORGET_PSD_BACK = 201;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_phone_forgetpassword);
        jumpType = getIntent().getIntExtra("jumpType", 0);
        initView();
        init();
    }

    private void initView() {
        act_register_phone_code = (TextView) findViewById(R.id.act_register_phone_code);
        act_register_phone_number = (EditText) findViewById(R.id.act_register_phone_number);
        act_register_password = (EditText) findViewById(R.id.act_register_password);
        act_register_verify_number = (EditText) findViewById(R.id.act_register_verify_number);
        act_register_btn_verify = (TextView) findViewById(R.id.act_register_btn_verify);
        act_register_btn_next = (TextView) findViewById(R.id.act_register_btn_next);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) act_register_btn_next.getLayoutParams();
        layoutParams.topMargin = DensityUtil.dip2px(context, 50);
        commonnavigation = (CommonNavigation) findViewById(R.id.commonnavigation);
        commonnavigation.setTitle("忘记密码");
        commonnavigation.setNavigationBackgroundColor(getResources().getColor(R.color.white));
        commonnavigation.setTitleTextColor(getResources().getColor(R.color.color_1d));
        commonnavigation.setLeftIcon(R.drawable.to_left_arrow_thin);
        act_register_btn_verify.setOnClickListener(l);
        act_register_btn_next.setOnClickListener(l);
    }

    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.act_register_btn_verify:
                    clickGetVerifyCode();
                    break;
                case R.id.act_register_btn_next:
                    clickLogin();
                    break;
            }
        }
    };

    class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    以毫秒为单位的总时长
         * @param countDownInterval 以毫秒为单位的时间间隔
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {    //计时完毕时触发
            act_register_btn_verify.setText("重发验证码");
            if (act_register_phone_number.getText().length() == 11
                    && MobileUtils.isMobileNO(act_register_phone_number.getText().toString())
                    && act_register_password.getText().length() >= 6) {
                act_register_btn_verify.setEnabled(true);
                act_register_btn_verify.setBackgroundResource(R.drawable.corner_blue_shape);
            } else {
                act_register_btn_verify.setEnabled(false);
            }

        }

        @Override
        public void onTick(long millisUntilFinished) {    //计时过程显示
            act_register_btn_verify.setEnabled(false);
            act_register_btn_verify.setBackgroundResource(R.drawable.corner_gray_shape);
            act_register_btn_verify.setText(millisUntilFinished / 1000 + "秒后重新发送");
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {//点击屏幕关闭系统键盘
//        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        return super.onTouchEvent(event);
//    }

    private void init() {
        //输入手机号监听
        act_register_phone_number.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (act_register_phone_number.getText().length() < 11 && act_register_phone_number.getText().length() != 0) {
                        AlertDialog.Builder builders = new AlertDialog.Builder(ForgetPassWordActivity.this);
                        builders.setTitle("您输入的手机号少于11位");
                        builders.setPositiveButton("确定",
                                null);
                        builders.show();
                    }
                }
            }
        });
        act_register_password.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (act_register_password.getText().length() < 6 && act_register_password.getText().length() != 0) {
                        AlertDialog.Builder builders = new AlertDialog.Builder(ForgetPassWordActivity.this);
                        builders.setTitle("您输入的密码少于6位");
                        builders.setPositiveButton("确定",
                                null);
                        builders.show();
                    }
                }
            }
        });
        act_register_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (act_register_phone_number.getText().length() == 11 && act_register_password.getText().toString().trim().length() >= 6) {
                    act_register_btn_verify.setBackgroundResource(R.drawable.corner_blue_shape);
                    act_register_btn_verify.setEnabled(true);
                } else {
                    act_register_btn_verify.setBackgroundResource(R.drawable.corner_gray_shape);
                    act_register_btn_verify.setEnabled(false);
                }
                if (act_register_phone_number.getText().length() == 11) {
                    act_register_password.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        act_register_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = String.valueOf(s);
                if (!InputVerificationUtil.pwdIsNumAndLetter(context, str) && str.length() != 0) {
                    act_register_password.setText(str.substring(0, str.length() - 1));
                    try {
                        act_register_password.setSelection(str.length() - 1);
                    } catch (Exception e) {
                        MHLogUtil.e(TAG,e);
                    }
                    return;
                }
                if (act_register_phone_number.getText().length() == 11 && act_register_password.getText().length() >= 6) {
                    act_register_btn_verify.setBackgroundResource(R.drawable.corner_blue_shape);
                    act_register_btn_verify.setEnabled(true);
                } else {
                    act_register_btn_verify.setBackgroundResource(R.drawable.corner_gray_shape);
                    act_register_btn_verify.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (null != s && s.length() > 20) {
                    showToastAtBottom("密码长度不能大于20位");
                    act_register_password.setText(act_register_password.getText().toString().substring(0, 20));
                    act_register_password.setSelection(act_register_password.getText().toString().length());
                }
            }
        });
    }

    /**
     * 获取验证码
     */
    private void clickGetVerifyCode() {
        String areaCode = act_register_phone_code.getText().toString();
        String phoneNumber = act_register_phone_number.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            SysMethod.toastShowCenter(ForgetPassWordActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT);
            return;
        }
        if (phoneNumber.length() < 11) {
            SysMethod.toastShowCenter(ForgetPassWordActivity.this, "手机号码格式不对", Toast.LENGTH_SHORT);
        }
        if (!MobileUtils.isMobileNO(phoneNumber)) {
            SysMethod.toastShowCenter(ForgetPassWordActivity.this, "手机号码输入错误", Toast.LENGTH_SHORT);
            return;
        }
        showLoading("正在加载...");
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("mobile_code", areaCode);
        requestParams.addParams("mobile_number", phoneNumber);
        MHHttpClient.getInstance().post(VerificationCodeResponse.class, context, ConstantsValue.Url.SMS_CODE, requestParams, new MHHttpHandler<VerificationCodeResponse>() {
            @Override
            public void onSuccess(VerificationCodeResponse response) {
                isSendedVerificationCode = true;
                mTimeCount.start();
                showToastAtBottom("验证码发送成功");
                hiddenLoadingView();
            }

            @Override
            public void onFailure(String content) {
//                showToastAtBottom(content);
                hiddenLoadingView();
            }
        });
    }

    /**
     * 提交
     */
    private void clickLogin() {
        String areaCode = act_register_phone_code.getText().toString();
        String phoneNumber = act_register_phone_number.getText().toString().trim();
        String password = act_register_password.getText().toString().trim();
        String verify = act_register_verify_number.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            SysMethod.toastShowCenter(this, "手机号码不能为空", Toast.LENGTH_SHORT);
            return;
        }

        if (phoneNumber.length() < 11) {
            SysMethod.toastShowCenter(this, "手机号码格式不对", Toast.LENGTH_SHORT);
        }

        if (!MobileUtils.isMobileNO(phoneNumber)) {
            SysMethod.toastShowCenter(this, "手机号码输入错误", Toast.LENGTH_SHORT);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            SysMethod.toastShowCenter(this, "密码不能为空", Toast.LENGTH_SHORT);
            return;
        }

        if (password.length() < 6) {
            SysMethod.toastShowCenter(this, "密码长度不能小于6位", Toast.LENGTH_SHORT);
            return;
        }

        if (password.length() > 20) {
            SysMethod.toastShowCenter(this, "密码长度不能大于20位", Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(verify)) {
            SysMethod.toastShowCenter(this, "验证码不能为空", Toast.LENGTH_SHORT);
            return;
        }
        if (!isSendedVerificationCode) {
            SysMethod.toastShowCenter(this, "验证码未获取", Toast.LENGTH_SHORT);
            return;
        }
        showLoading();
        final MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("mobile_code", areaCode);
        requestParams.addParams("mobile_number", phoneNumber);
        requestParams.addParams("password", com.haiqiu.miaohi.utils.MHStringUtils.getPassword(password));
        requestParams.addParams("verify_number", verify);
        MHHttpClient.getInstance().post(LoginResponse.class, context, ConstantsValue.Url.FORGET_PASSWORD, requestParams, new MHHttpHandler<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse response) {
                LoginBean loginBean = response.getData();
                UserInfoUtil.saveUserInfo(ForgetPassWordActivity.this, loginBean);
                hiddenLoadingView();
                if (TextUtils.equals("register", response.getBase().getCommand())) {
                    Intent intent = new Intent(ForgetPassWordActivity.this, MineDataActivity.class);
                    setResult(200);
                } else {
                    Intent intent=new Intent();
                    intent.putExtra("jumpType",jumpType);
                    setResult(DIALOG_FORGET_PSD_BACK,intent);
                }
                finish();
                startActivity(new Intent(ForgetPassWordActivity.this, MainActivity.class));
                context.sendBroadcast(new Intent("common_lr"));
            }

            @Override
            public void onFailure(String content) {
                hiddenLoadingView();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        onHideSoftInput(event);
        return super.onTouchEvent(event);
    }
}
