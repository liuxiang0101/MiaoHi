package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.VerifyRegisterInfoResponse;
import com.haiqiu.miaohi.utils.FormatPhoneNumberTextWatcher;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.MobileUtils;
import com.haiqiu.miaohi.utils.SysMethod;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.tendcloud.tenddata.TCAgent;

/**
 * 输入手机号--进行注册
 * Created by LiuXiang on 2016/8/29.
 */
public class LRInputPhoneNumberActivity extends CommonLRActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private TextView tv_next;
    //    private TextView tv_agreement;
    private TextView tv_page_prompt_msg;
    private TextView tv_button_prompt_msg;
    private EditText et_phone_number;
    private ImageView iv_et_clear;
    //    private LinearLayout ll_agreement;
    private CommonNavigation navigation;
    //    private RelativeLayout rl_select_country;
    private String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_phonenumber);
        TCAgent.onEvent(context, "手机号注册页" + ConstantsValue.android);
        initView();
    }

    /**
     * 控件初始化
     */
    private void initView() {
        navigation = (CommonNavigation) findViewById(R.id.navigation);
        navigation.setNavigationBackgroundColor(getResources().getColor(R.color.white));
        navigation.setTitleTextColor(getResources().getColor(R.color.color_1d));
        navigation.setLeftIcon(R.drawable.to_left_arrow_thin);
        tv_page_prompt_msg = (TextView) findViewById(R.id.tv_page_prompt_msg);
        iv_et_clear = (ImageView) findViewById(R.id.iv_et_clear);
//        rl_select_country = (RelativeLayout) findViewById(R.id.rl_select_country);
//        ll_agreement = (LinearLayout) findViewById(R.id.ll_agreement);
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        tv_next = (TextView) findViewById(R.id.tv_next);
//        tv_agreement = (TextView) findViewById(R.id.tv_agreement);
        tv_button_prompt_msg = (TextView) findViewById(R.id.tv_button_prompt_msg);

        addEvent();
    }

    /**
     * 为控件添加事件
     */
    private void addEvent() {
        navigation.hideBottomLine();
//        rl_select_country.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        iv_et_clear.setOnClickListener(this);
//        tv_agreement.setOnClickListener(this);
        FormatPhoneNumberTextWatcher textWatcher = new FormatPhoneNumberTextWatcher(et_phone_number, tv_next, iv_et_clear, context);
        et_phone_number.addTextChangedListener(textWatcher);
    }

    /**
     * 点击下一步执行操作
     */
    private void submit() {
        // 获取输入框输入的手机号码
        phone_number = et_phone_number.getText().toString().trim();
        phone_number = phone_number.replaceAll(" ", "");

        if (MHStringUtils.isEmpty(phone_number)) {
            SysMethod.toastShowCenter(LRInputPhoneNumberActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT);
            return;
        }
        MHLogUtil.e(TAG, phone_number);
        if (phone_number.length() < 11) {
            SysMethod.toastShowCenter(LRInputPhoneNumberActivity.this, "手机号码格式不对", Toast.LENGTH_SHORT);
            return;
        }
        if (!MobileUtils.isMobileNO(phone_number)) {
            tv_button_prompt_msg.setVisibility(View.VISIBLE);
            tv_button_prompt_msg.setText("手机号码输入错误");
            return;
        }
        checkPhoneIsRegister();
//        Intent intent = new Intent(LRInputPhoneNumberActivity.this, LRGetVerificationCodeActivity.class);
//        intent.putExtra("phone_number", phone_number);
//        intent.putExtra("detailed_type", "phone_register");
//        startActivity(intent);
    }

    /**
     * 验证该手机号是否已注册
     */
    private void checkPhoneIsRegister() {
        showLoading("正在验证...", true, false);
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("verify_type", "10");//10:验证手机号  20 验证短信
        requestParams.addParams("mobile_code", "+86");
        requestParams.addParams("mobile_number", phone_number);
//        requestParams.addParams("verify_number", "");
        MHHttpClient.getInstance().post(VerifyRegisterInfoResponse.class, ConstantsValue.Url.VERIFY_REGISTER_INFO, requestParams, new MHHttpHandler<VerifyRegisterInfoResponse>() {
            @Override
            public void onSuccess(VerifyRegisterInfoResponse response) {
                hiddenLoadingView();
                tv_button_prompt_msg.setVisibility(View.INVISIBLE);
                //跳转至验证码界面
                Intent intent = new Intent(LRInputPhoneNumberActivity.this, LRGetVerificationCodeActivity.class);
                intent.putExtra("phone_number", phone_number);
                intent.putExtra("detailed_type", "phone_register");
                startActivity(intent);
            }

            @Override
            public void onFailure(String content) {
                hiddenLoadingView();
                tv_button_prompt_msg.setText("网络异常，请稍后重试");
                tv_button_prompt_msg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStatusIsError(String message) {
                hiddenLoadingView();
                tv_button_prompt_msg.setText(message);
                tv_button_prompt_msg.setVisibility(View.VISIBLE);
                super.onStatusIsError(message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_et_clear:
                et_phone_number.setText("");
                break;
            case R.id.tv_next:                  //点击“下一步”按钮
                submit();
                break;
            case R.id.tv_agreement:             //秒嗨协议
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
}
