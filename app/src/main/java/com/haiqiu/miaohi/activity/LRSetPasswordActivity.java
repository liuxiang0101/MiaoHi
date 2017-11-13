package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.utils.InputVerificationUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.SysMethod;
import com.haiqiu.miaohi.view.CommonNavigation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 设置密码界面
 * Created by LiuXiang on 2016/8/30.
 */
public class LRSetPasswordActivity extends CommonLRActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private CommonNavigation navigation;
    private TextView tv_page_prompt_msg;
    private TextView tv_psd_prompt_msg;
    private EditText et_user_psd;
    private TextView tv_psd_error_prompt;
    private TextView tv_login;
    private String verification_code;
    private String phone_number;
    private int fromAccountBoundType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        verification_code = getIntent().getStringExtra("verification_code");
        phone_number = getIntent().getStringExtra("phone_number");
        fromAccountBoundType = getIntent().getIntExtra("isFromAccountBound", 0);
        MHLogUtil.e("手机号:" + phone_number, "\t验证码:" + verification_code);
        initView();
    }

    private void initView() {
        navigation = (CommonNavigation) findViewById(R.id.navigation);
        navigation.setNavigationBackgroundColor(getResources().getColor(R.color.white));
        navigation.setTitleTextColor(getResources().getColor(R.color.color_1d));
        navigation.setLeftIcon(R.drawable.to_left_arrow_thin);
        tv_page_prompt_msg = (TextView) findViewById(R.id.tv_page_prompt_msg);
        tv_psd_prompt_msg = (TextView) findViewById(R.id.tv_psd_prompt_msg);
        et_user_psd = (EditText) findViewById(R.id.et_user_psd);
        tv_psd_error_prompt = (TextView) findViewById(R.id.tv_psd_error_prompt);
        tv_login = (TextView) findViewById(R.id.tv_login);

        addEvent();
    }

    private void addEvent() {
        navigation.hideBottomLine();
        tv_login.setOnClickListener(this);
        String detailed_type = getIntent().getStringExtra("detailed_type");
        if ("phone_register".equals(detailed_type)) {           //手机号注册

        } else if ("follow_bound".equals(detailed_type)) {      //第三方登录首次登录
            tv_login.setText("下一步(3/3)");
        } else if ("account_bound".equals(detailed_type)) {     //设置界面进行绑定
            tv_login.setText("完成");
        }
        et_user_psd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                MHLogUtil.i(TAG, "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MHLogUtil.i(TAG, "onTextChanged");
                if (null != s && s.length() >= 6) {
                    tv_login.setBackgroundResource(R.drawable.selector_button_black);
                } else {
                    tv_login.setBackgroundColor(getResources().getColor(R.color.color_df));
                }
                String str = String.valueOf(s);
                if (!InputVerificationUtil.pwdIsNumAndLetter(context, str) && str.length() != 0) {
                    et_user_psd.setText(str.substring(0, str.length() - 1));
                    try {
                        et_user_psd.setSelection(str.length() - 1);
                    } catch (Exception e) {
                        MHLogUtil.e(TAG,e);
                    }
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                MHLogUtil.i(TAG, "afterTextChanged");
                if (null != s && s.length() > 20) {
                    showToastAtBottom("密码长度不能大于20位");
                    et_user_psd.setText(et_user_psd.getText().toString().substring(0, 20));
                    et_user_psd.setSelection(et_user_psd.getText().toString().length());
                }
            }
        });
    }

    private void submit() {
        // validate
        String psd = et_user_psd.getText().toString().trim();
        if (TextUtils.isEmpty(psd)) {
            showToastAtBottom("输入的密码为空");
            return;
        }
        if (psd.length() < 6) {
            SysMethod.toastShowCenter(this, "密码长度不能小于6位", Toast.LENGTH_SHORT);
            return;
        }

        if (psd.length() > 20) {
            SysMethod.toastShowCenter(this, "密码长度不能大于20位", Toast.LENGTH_SHORT);
            return;
        }

        if (!pwdIsNumAndLetter(psd)) {
            tv_psd_error_prompt.setVisibility(View.VISIBLE);
            return;
        }
        tv_psd_error_prompt.setVisibility(View.INVISIBLE);

        if (fromAccountBoundType != 0) {
            boundPhone(psd);
        } else {
//            registerNewUser(psd);
            Intent intent = new Intent(this, LRPerfectInformationActivity.class);
            intent.putExtra("verification_code", verification_code);
            intent.putExtra("phone_number", phone_number);
            intent.putExtra("psd", psd);
            startActivity(intent);
        }
    }

    /**
     * 绑定手机号
     */
    private void boundPhone(String psd) {
        showLoading("正在绑定号码...", true, false);
        final MHRequestParams requestParams = new MHRequestParams();
        if (fromAccountBoundType == 1) {
            requestParams.addParams("bind_type", String.valueOf(10));//bind_type -- 10:绑定账号/20:更换绑定账号
        } else {
            requestParams.addParams("bind_type", String.valueOf(fromAccountBoundType));
        }
        requestParams.addParams("account_type", "1");//1:手机号 2：qq  3: 微信  4: 微博
        requestParams.addParams("mobile_code", "+86");
        requestParams.addParams("mobile_number", phone_number);
        requestParams.addParams("password", com.haiqiu.miaohi.utils.MHStringUtils.getPassword(psd));
        requestParams.addParams("verify_number", verification_code);
        MHHttpClient.getInstance().post(BaseResponse.class, context, ConstantsValue.Url.BIND_ACCOUNT, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (fromAccountBoundType == 1) {        //第三方登录首次登录绑定账号
                    startActivity(new Intent(LRSetPasswordActivity.this, RecommendSportsActivity.class));
                    Intent intent = new Intent("common_lr");
                    context.sendBroadcast(intent);
                } else {                                //在设置中选择绑定账号
                    startActivity(new Intent(LRSetPasswordActivity.this, AccountBindActivity.class)
                            .putExtra("phoneNum", phone_number));
                }
            }

            @Override
            public void onFailure(String content) {
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
            }
        });
    }

//    /**
//     * 注册新用户
//     *
//     * @param psd
//     */
//    private void registerNewUser(String psd) {
//        showLoading("正在注册...", true, false);
//        final MHRequestParams requestParams = new MHRequestParams();
//        requestParams.addParams("mobile_code", "+86");
//        requestParams.addParams("mobile_number", phone_number);
//        requestParams.addParams("password", MHStringUtils.getPassword(psd));
//        requestParams.addParams("verify_number", verification_code);
//        MHHttpClient.getInstance().post(LoginResponse.class, context, ConstantsValue.Url.REGISTER, requestParams, new MHHttpHandler<LoginResponse>() {
//            @Override
//            public void onSuccess(LoginResponse response) {
//                tv_login.setClickable(true);
//                LoginBean loginBean = response.getData();
//                UserInfoUtil.saveUserInfo(LRSetPasswordActivity.this, loginBean);
////                if (TextUtils.equals("register", response.getBase().getCommand())) {
////                    setResult(200);
////                } else {
////                    setResult(50);
////                }
//                Intent intent = new Intent(LRSetPasswordActivity.this, LRPerfectInformationActivity.class);
//                intent.putExtra("undefine", "");
//                startActivity(intent);
//                finish();
//                ConnectionRong();
//            }
//
//            @Override
//            public void onFailure(String content) {
//                tv_login.setClickable(true);
//            }
//
//            @Override
//            public void onStatusIsError(String message) {
//                tv_login.setClickable(true);
//                super.onStatusIsError(message);
//            }
//        });
//    }
//
//    private void ConnectionRong() {
//        //用融云token连接融云服务器
//        RongIMClient.setOnReceiveMessageListener(new RongReceiveMessageListener(this));
//        RongIMClient.connect(SpUtils.get(ConstantsValue.Sp.RONG_TOKEN, "") + "", new RongIMClient.ConnectCallback() {
//            @Override
//            public void onTokenIncorrect() {
//                MHLogUtil.e(TAG, "rong_token失效");
//                if (!isLogin()) {
//                    return;
//                }
//                //请求新的rong_token
//                MHRequestParams requestParams = new MHRequestParams();
//                MHHttpClient.getInstance().post(CommonUserInfoResponse.class, ConstantsValue.Url.GET_RONG_VALID_TOKEN, requestParams, new MHHttpHandler<CommonUserInfoResponse>() {
//                    @Override
//                    public void onSuccess(CommonUserInfoResponse response) {
//                        String rong_token = response.getData().getRong_token();
//                        SpUtils.put(ConstantsValue.Sp.RONG_TOKEN, rong_token);
//                        rongMethod(rong_token);
//                        finish();
//                    }
//
//                    @Override
//                    public void onFailure(String content) {
//                        finish();
//                    }
//
//                    @Override
//                    public void onStatusIsError(String message) {
//                        finish();
//                        super.onStatusIsError(message);
//                    }
//                });
//            }
//
//            @Override
//            public void onSuccess(String s) {
//                MHLogUtil.i(TAG, "连接融云服务器成功--userId:" + s);
//                Intent intent = new Intent("receivedMsg");
//                context.sendBroadcast(intent);
//                finish();
//            }
//
//            @Override
//            public void onVideoError(RongIMClient.ErrorCode errorCode) {
//                MHLogUtil.e(TAG, "获取rong_token失败--errorCode:" + errorCode.getMessage());
//                finish();
//            }
//        });
////        SpUtils.put("isLoginoutApp", false);
//        SpUtils.put(ConstantsValue.Sp.IS_LOGINOUT_APP, false);
//    }
//
//    /**
//     * 连接融云服务器
//     */
//    private void rongMethod(String token) {
//        RongIMClient.setOnReceiveMessageListener(new RongReceiveMessageListener(this));
//        RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
//            @Override
//            public void onTokenIncorrect() {
//                MHLogUtil.e(TAG, "rong_token失效,重新请求");
//            }
//
//            @Override
//            public void onSuccess(String s) {
//                MHLogUtil.i(TAG, "获取rong_token成功--userId:" + s);
//            }
//
//            @Override
//            public void onVideoError(RongIMClient.ErrorCode errorCode) {
//                MHLogUtil.e(TAG, "获取rong_token失败--errorCode:" + errorCode.getMessage());
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                submit();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        onHideSoftInput(event);
        return super.onTouchEvent(event);
    }

    /**
     * 判断字符串中是否只存在数字和字母
     *
     * @param s
     * @return
     */
    private boolean pwdIsNumAndLetter(String s) {
        Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher m = p.matcher(s);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }
}
