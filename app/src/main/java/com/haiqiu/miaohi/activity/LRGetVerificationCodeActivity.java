package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.response.VerificationCodeResponse;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.MobileUtils;
import com.haiqiu.miaohi.view.CommonNavigation;

/**
 * 获取验证码界面
 * Created by LiuXiang on 2016/8/29.
 */
public class LRGetVerificationCodeActivity extends CommonLRActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private CommonNavigation navigation;
    private TextView tv_phone_number;
    private EditText act_register_verify_number;
    private TextView act_register_btn_verify;
    private TextView tv_error_msg;
    private TextView tv_login;
    //    private TextView tv_agreement;
    private TextView tv_phone_number_pre;
    private String phone_number;
    private TimeCount mTimeCount;
    private int fromAccountBoundType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_verificationcode);
        //获取前一页面数据
        phone_number = getIntent().getStringExtra("phone_number");
        fromAccountBoundType = getIntent().getIntExtra("isFromAccountBound", 0);
        //初始化控件
        initView();
        //进入此页自动获取验证码
        clickGetVerifyCode();
    }

    /**
     * 初始化
     */
    private void initView() {
        navigation = (CommonNavigation) findViewById(R.id.navigation);
        navigation.setNavigationBackgroundColor(getResources().getColor(R.color.white));
        navigation.setTitleTextColor(getResources().getColor(R.color.color_1d));
        navigation.setLeftIcon(R.drawable.to_left_arrow_thin);
        tv_phone_number = (TextView) findViewById(R.id.tv_phone_number);
        act_register_verify_number = (EditText) findViewById(R.id.act_register_verify_number);
        act_register_btn_verify = (TextView) findViewById(R.id.act_register_btn_verify);
        tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
        tv_login = (TextView) findViewById(R.id.tv_login);
        if (getIntent().getBooleanExtra("is_bound_phone", false))
            tv_login.setText("下一步(2/3)");
//        tv_agreement = (TextView) findViewById(R.id.tv_agreement);
        tv_phone_number_pre = (TextView) findViewById(R.id.tv_phone_number_pre);
        mTimeCount = new TimeCount(60000, 1000);
        addEvent();
    }

    /**
     * 为控件添加事件
     */
    private void addEvent() {
        navigation.hideBottomLine();
//        tv_agreement.setOnClickListener(this);
        act_register_btn_verify.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        act_register_verify_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                MHLogUtil.i(TAG, "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MHLogUtil.i(TAG, "onTextChanged");
                if (null != s && s.length() >= 1) {
                    tv_login.setBackgroundResource(R.drawable.selector_button_black);
                } else {
                    tv_login.setBackgroundColor(getResources().getColor(R.color.color_df));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                MHLogUtil.i(TAG, "afterTextChanged");
            }
        });
    }

    private void submit() {
        showLoading("正在验证...", true, false);
        String number = act_register_verify_number.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            showToastAtBottom("验证码不能为空");
            return;
        }

        checkVerificationCode(number);
    }

    /**
     * 获取验证码
     */
    private void clickGetVerifyCode() {
        act_register_btn_verify.setEnabled(false);

        //此判断用于防止前一界面手机后未正常传递
        if (MHStringUtils.isEmpty(phone_number)) {
            showToastAtBottom("发生错误,请返回重新输入手机号");
            return;
        } else {
            if (!MobileUtils.isMobileNO(phone_number)) {
                showToastAtBottom("发生错误,请返回重新输入手机号");
                return;
            }
        }

        showLoading("发送短信中...", true, false);
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("mobile_code", "+86");
        requestParams.addParams("mobile_number", phone_number);
        MHHttpClient.getInstance().post(VerificationCodeResponse.class, context, ConstantsValue.Url.SMS_CODE, requestParams, new MHHttpHandler<VerificationCodeResponse>() {
            @Override
            public void onSuccess(VerificationCodeResponse response) {
                act_register_btn_verify.setEnabled(true);
                mTimeCount.start();
                tv_phone_number_pre.setVisibility(View.VISIBLE);
                tv_phone_number.setText(MHStringUtils.getFormatPhoneNumber(phone_number));
                showToastAtBottom("验证码发送成功");
            }

            @Override
            public void onFailure(String content) {
                act_register_btn_verify.setEnabled(true);
            }

            @Override
            public void onStatusIsError(String message) {
                act_register_btn_verify.setEnabled(true);
                super.onStatusIsError(message);
            }
        });
    }

    /**
     * 验证验证码是否正确
     */
    private void checkVerificationCode(final String number) {
        showLoading("正在验证...", true, false);
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("verify_type", "20");//10:验证手机号  20 验证短信
        requestParams.addParams("mobile_code", "+86");
        requestParams.addParams("mobile_number", phone_number);
        requestParams.addParams("verify_number", number);
        MHHttpClient.getInstance().post(BaseResponse.class, context, ConstantsValue.Url.VERIFY_REGISTER_INFO, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                tv_error_msg.setVisibility(View.INVISIBLE);
                //跳转至设置密码界面
                Intent intent = new Intent(LRGetVerificationCodeActivity.this, LRSetPasswordActivity.class);
                intent.putExtra("verification_code", number);
                intent.putExtra("phone_number", phone_number);
                intent.putExtra("is_bound_phone", getIntent().getBooleanExtra("is_bound_phone", false));
                intent.putExtra("isFromAccountBound", fromAccountBoundType);
                intent.putExtra("detailed_type", getIntent().getStringExtra("detailed_type"));
                startActivity(intent);
            }

            @Override
            public void onFailure(String content) {
                tv_error_msg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStatusIsError(String message) {
                tv_error_msg.setVisibility(View.VISIBLE);
                super.onStatusIsError(message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_register_btn_verify:      //获取验证码按钮
                clickGetVerifyCode();
                break;
            case R.id.tv_login:                     //下一步按钮
                submit();
                break;
            case R.id.tv_agreement:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("uri", context.getResources().getString(R.string.text_miaohi_user_protocol_url));
                intent.putExtra("title", context.getResources().getString(R.string.text_miaohi_user_protocol));
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        onHideSoftInput(event);
        return super.onTouchEvent(event);
    }

    class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    以毫秒为单位的总时长
         * @param countDownInterval 以毫秒为单位的时间间隔
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {                            //计时完毕时触发
            act_register_btn_verify.setText("重新获取");
            act_register_btn_verify.setTextColor(getResources().getColor(R.color.color_1d));
            if (phone_number.length() == 11
                    && MobileUtils.isMobileNO(phone_number)
                    && phone_number.length() >= 6) {
                act_register_btn_verify.setEnabled(true);
//                act_register_btn_verify.setBackgroundResource(R.drawable.corner_blue_shape);
            } else {
                act_register_btn_verify.setEnabled(false);
            }

        }

        @Override
        public void onTick(long millisUntilFinished) {      //计时过程显示
            act_register_btn_verify.setEnabled(false);
            act_register_btn_verify.setTextColor(getResources().getColor(R.color.color_c4));
            act_register_btn_verify.setText(millisUntilFinished / 1000 + "秒后重新发送");
        }
    }
}
