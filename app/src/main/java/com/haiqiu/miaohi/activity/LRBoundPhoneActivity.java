package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.FormatPhoneNumberTextWatcher;
import com.haiqiu.miaohi.utils.MobileUtils;
import com.haiqiu.miaohi.view.CommonNavigation;

/**
 * 绑定手机
 * Created by LiuXiang on 2016/8/29.
 */
public class LRBoundPhoneActivity extends CommonLRActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private TextView tv_msg;
    private TextView tv_next;
    private TextView tv_page_prompt_msg;
    private TextView tv_button_prompt_msg;
    private EditText et_phone_number;
    private CommonNavigation navigation;

    private int fromAccountBoundType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bound_phone);
        fromAccountBoundType = getIntent().getIntExtra("isFromAccountBound", 0);
        initView();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_page_prompt_msg = (TextView) findViewById(R.id.tv_page_prompt_msg);
        tv_button_prompt_msg = (TextView) findViewById(R.id.tv_button_prompt_msg);

        navigation = (CommonNavigation) findViewById(R.id.navigation);
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);

        addEvent();

        if (fromAccountBoundType >= 10) {//从设置绑定界面进入
            navigation.setRightText("");
            TextView tv = new TextView(context);
            tv.setText("取消");
            tv.setTextColor(getResources().getColor(R.color.color_1d));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            tv.setPadding(DensityUtil.dip2px(context, 17), 0, 0, 0);
            navigation.setLeftLayoutView(tv);
            tv_page_prompt_msg.setText("请输入你的手机号");
            tv_msg.setVisibility(View.GONE);
            navigation.setOnLeftLayoutClickListener(new CommonNavigation.OnLeftLayoutClick() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            navigation.hideLeftLayout();
        }
    }

    /**
     * 添加事件
     */
    private void addEvent() {
        navigation.hideBottomLine();
        //点击右上角“跳过”
        navigation.setRightTextColor(getResources().getColor(R.color.color_1d));
        navigation.setOnRightLayoutClickListener(new CommonNavigation.OnRightLayoutClick() {
            @Override
            public void onClick(View v) {
                finishTheActivity();
            }
        });
        navigation.setNavigationBackgroundColor(getResources().getColor(R.color.white));
        navigation.setTitleTextColor(getResources().getColor(R.color.color_1d));
        navigation.setLeftIcon(R.drawable.to_left_arrow_thin);
        tv_next.setOnClickListener(this);
        FormatPhoneNumberTextWatcher textWatcher = new FormatPhoneNumberTextWatcher(et_phone_number, tv_next, new ImageView(context), context);
        et_phone_number.addTextChangedListener(textWatcher);
    }

    /**
     * 点击"下一步"，执行操作
     */
    private void submit() {
        // 获取输入的号码
        String number = et_phone_number.getText().toString().trim();
        number = number.replaceAll(" ", "");
        if (TextUtils.isEmpty(number)) {
            showToastAtBottom("手机号不能为空");
            return;
        }
        if (number.length() < 11) {
            showToastAtBottom("手机号码格式不对");
            return;
        }
        if (!MobileUtils.isMobileNO(number)) {
//            showToastAtBottom("手机号码输入错误");
            tv_button_prompt_msg.setText("手机号码输入错误");
            return;
        }

        // 手机号是否已注册或绑定
        checkPhoneIsRegister(number);
    }

    /**
     * 验证该手机号是否已注册
     */
    private void checkPhoneIsRegister(final String phone_number) {
        showLoading("正在验证...", true, false);
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("verify_type", "10");//10:验证手机号  20 验证短信
        requestParams.addParams("mobile_code", "+86");
        requestParams.addParams("mobile_number", phone_number);
//        requestParams.addParams("verify_number", "+86");
        MHHttpClient.getInstance().post(BaseResponse.class, ConstantsValue.Url.VERIFY_REGISTER_INFO, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                hiddenLoadingView();
                tv_button_prompt_msg.setVisibility(View.INVISIBLE);
                //跳转至验证码界面
                Intent intent = new Intent(LRBoundPhoneActivity.this, LRGetVerificationCodeActivity.class);
                intent.putExtra("phone_number", phone_number);
                intent.putExtra("detailed_type", fromAccountBoundType > 1 ? "account_bound" : "follow_bound");
                intent.putExtra("is_bound_phone", true);
                intent.putExtra("isFromAccountBound", fromAccountBoundType);
                startActivity(intent);
            }

            @Override
            public void onFailure(String content) {
                hiddenLoadingView();
                tv_button_prompt_msg.setText("网络异常，请稍后重试");
            }

            @Override
            public void onStatusIsError(String message) {
                hiddenLoadingView();
                tv_button_prompt_msg.setText(message);
                super.onStatusIsError(message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next:
                submit();
                break;
        }
    }

    /**
     * 结束此界面
     */
    private void finishTheActivity() {
        if (fromAccountBoundType == 1) {
            startActivity(new Intent(LRBoundPhoneActivity.this, RecommendSportsActivity.class));
            Intent intent = new Intent("common_lr");
            context.sendBroadcast(intent);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        onHideSoftInput(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fromAccountBoundType == 1) {
                finishTheActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
