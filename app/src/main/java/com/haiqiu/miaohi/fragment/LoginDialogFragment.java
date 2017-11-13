package com.haiqiu.miaohi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.ForgetPassWordActivity;
import com.haiqiu.miaohi.activity.LRInputPhoneNumberActivity;
import com.haiqiu.miaohi.activity.LRPerfectInformationActivity;
import com.haiqiu.miaohi.activity.MainActivity;
import com.haiqiu.miaohi.activity.RecommendSportsActivity;
import com.haiqiu.miaohi.activity.VideoRecorderActivity;
import com.haiqiu.miaohi.activity.WebActivity;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.LoginBean;
import com.haiqiu.miaohi.bean.UmengLoginInfo;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.CommonUserInfoResponse;
import com.haiqiu.miaohi.response.LoginResponse;
import com.haiqiu.miaohi.rong.RongReceiveMessageListener;
import com.haiqiu.miaohi.umeng.IUMLoginCallback;
import com.haiqiu.miaohi.umeng.UmengLogin;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.InputVerificationUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.MobileUtils;
import com.haiqiu.miaohi.utils.SetClickStateUtil;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.SysMethod;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.umeng.socialize.UMShareAPI;

import io.rong.imlib.RongIMClient;

/**
 * Created by LiuXiang on 2016/12/14.
 * 登录用的dialog弹框
 */
public class LoginDialogFragment extends DialogFragment implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    public static boolean wxBackError = false;

    private ImageView iv_close;
    private ImageView iv_login_qq;
    private ImageView iv_login_sina;
    private ImageView iv_login_wechat;
    private EditText et_phone_number;
    private EditText et_phone_password;
    private TextView tv_register_phone;
    private TextView tv_forget_password;
    private Button bt_login;
    private LinearLayout ll;

    private int loginType = 0;          //登录类型
    private final int SELF = 0;         //登录类型--app自带登录
    private final int THIRDPART = 1;    //登录类型--第三方登录
    private int theThirdPartType;       //第三方登录类型———2:QQ;3:微信;4:微博
    private int jumpType = 0;

    private String phoneNumber;
    private String password;
    private String loginParam;

    private UMShareAPI umShareAPI;
    private UmengLoginInfo umengLoginInfo;
    private BaseActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_loading);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //设置dialog默认的自带背景透明
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        getDialog().setCanceledOnTouchOutside(false);
        activity = (BaseActivity) getActivity();
        if (getArguments() != null)
            jumpType = getArguments().getInt("jumpType");
        View view = inflater.inflate(R.layout.dialog_login, container, false);
        initView(view);
        init();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MobileUtils.isMobileNO(et_phone_number.getText().toString().trim())) {
            et_phone_password.requestFocus();
            et_phone_password.setSelection(et_phone_password.length());
        }
        iv_login_wechat.setEnabled(true);
        iv_login_qq.setEnabled(true);
        iv_login_sina.setEnabled(true);
        if (wxBackError) {
            activity.hiddenLoadingView();
            wxBackError = false;
        }
    }

    private void initView(View view) {
        iv_close = (ImageView) view.findViewById(R.id.iv_close);
        et_phone_number = (EditText) view.findViewById(R.id.et_phone_number);
        et_phone_password = (EditText) view.findViewById(R.id.et_phone_password);
        tv_register_phone = (TextView) view.findViewById(R.id.tv_register_phone);
        tv_forget_password = (TextView) view.findViewById(R.id.tv_forget_password);
        bt_login = (Button) view.findViewById(R.id.bt_login);
        iv_login_wechat = (ImageView) view.findViewById(R.id.iv_login_wechat);
        iv_login_sina = (ImageView) view.findViewById(R.id.iv_login_sina);
        iv_login_qq = (ImageView) view.findViewById(R.id.iv_login_qq);
        ll = (LinearLayout) view.findViewById(R.id.ll);
        //判断是否安装第三方客户端
        if (!UmengLogin.isWeixinAvilible(activity)) {
            iv_login_wechat.setVisibility(View.GONE);
        }
        if (!UmengLogin.isQQClientAvailable(activity)) {
            iv_login_qq.setVisibility(View.GONE);
        }

        addEvent();
    }

    private void addEvent() {
        tv_register_phone.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        iv_login_qq.setOnClickListener(this);
        iv_login_sina.setOnClickListener(this);
        iv_login_wechat.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        SetClickStateUtil.getInstance().setStateListener(iv_login_qq);
        SetClickStateUtil.getInstance().setStateListener(iv_login_sina);
        SetClickStateUtil.getInstance().setStateListener(iv_login_wechat);

        String user_name = SpUtils.getString(ConstantsValue.Sp.USER_PHONE_NUMBER);
        et_phone_number.setText(user_name);
        et_phone_number.setSelection(user_name.length());
    }

    private void init() {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        umShareAPI = UMShareAPI.get(activity);
        et_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (MobileUtils.isMobileNO(String.valueOf(s))) {
                    et_phone_password.requestFocus();
                    et_phone_password.setSelection(et_phone_password.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //限制密码只能输入数字和字母
        et_phone_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = String.valueOf(s);
                if (!InputVerificationUtil.pwdIsNumAndLetter(activity, str) && str.length() != 0) {
                    et_phone_password.setText(str.substring(0, str.length() - 1));
                    try {
                        et_phone_password.setSelection(str.length() - 1);
                    } catch (Exception e) {
                        MHLogUtil.e(TAG,e);
                    }
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (null != s && s.length() > 20) {
                    Toast.makeText(activity, "密码长度不能大于20位", Toast.LENGTH_SHORT).show();
                    et_phone_password.setText(et_phone_password.getText().toString().substring(0, 20));
                    et_phone_password.setSelection(et_phone_password.getText().length());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_close:       //点击关闭登录dialog
                dismiss();
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                break;
            case R.id.tv_forget_password:   //忘记密码
                startActivityForResult(new Intent(activity, ForgetPassWordActivity.class)
                        .putExtra("jumpType", jumpType), 200);
                dismiss();
                break;
            case R.id.tv_register_phone:    //手机注册
                intent = new Intent();
                intent.setClass(activity, LRInputPhoneNumberActivity.class);
                startActivityForResult(intent, 200);
                dismiss();
                break;
            case R.id.bt_login:             //登录
                loginType = SELF;
                Login();
                break;
            case R.id.iv_login_wechat:      //微信登录
                if (!UmengLogin.isWeixinAvilible(activity)) {
                    activity.showToastAtBottom("请安装微信客户端");
                    return;
                }
                iv_login_wechat.setEnabled(false);
                activity.showLoading("登录中...", true, false);
                theThirdPartType = 3;
                loginType = THIRDPART;
                UmengLogin.loginWX(activity, umShareAPI, new IUMLoginCallback() {

                    @Override
                    public void callBackInfo(UmengLoginInfo loginInfo) {
                        umengLoginInfo = loginInfo;
                        loginType = THIRDPART;
                        theThirdPartType = 3;
                        login();
                    }
                });
                break;
            case R.id.iv_login_qq:          //qq登录
                if (!UmengLogin.isQQClientAvailable(activity)) {
                    activity.showToastAtBottom("请安装QQ客户端");
                    return;
                }
                iv_login_qq.setEnabled(false);
                activity.showLoading("登录中...", true, false);
                theThirdPartType = 2;
                loginType = THIRDPART;
                UmengLogin.loginQQ(activity, umShareAPI, new IUMLoginCallback() {
                    @Override
                    public void callBackInfo(UmengLoginInfo loginInfo) {
                        umengLoginInfo = loginInfo;
                        loginType = THIRDPART;
                        theThirdPartType = 2;
                        login();
                    }
                });
                break;
            case R.id.iv_login_sina:        //新浪微博登录
                activity.showLoading("登录中...", true, false);
                iv_login_sina.setEnabled(false);
                theThirdPartType = 4;
                loginType = THIRDPART;
                UmengLogin.loginSina(activity, umShareAPI, new IUMLoginCallback() {
                    @Override
                    public void callBackInfo(UmengLoginInfo loginInfo) {
                        umengLoginInfo = loginInfo;
                        loginType = THIRDPART;
                        theThirdPartType = 4;
                        login();
                    }
                });
                break;
            case R.id.tv_agreement:     //用户协议书
                intent = new Intent(activity, WebActivity.class);
                intent.putExtra("uri", activity.getResources().getString(R.string.text_miaohi_user_protocol_url));
                intent.putExtra("title", activity.getResources().getString(R.string.text_miaohi_user_protocol));
                startActivityForResult(intent, 200);
                break;
        }
    }

    /**
     * 正常登录
     */
    private void Login() {
        String areaCode = "+86";
        //获取用户输入框输入的值
        phoneNumber = et_phone_number.getText().toString().trim();
        password = et_phone_password.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber)) {
            SysMethod.toastShowCenter(activity, "手机号码不能为空", Toast.LENGTH_SHORT);
            return;
        }

        if (phoneNumber.length() < 11) {
            SysMethod.toastShowCenter(activity, "手机号码格式不对", Toast.LENGTH_SHORT);
        }
        if (!MobileUtils.isMobileNO(phoneNumber)) {
            SysMethod.toastShowCenter(activity, "手机号码输入错误", Toast.LENGTH_SHORT);
            return;
        }

//        SpUtils.put("USER_NAME", phoneNumber);
        SpUtils.put(ConstantsValue.Sp.USER_PHONE_NUMBER, phoneNumber);
        if (TextUtils.isEmpty(password)) {
            SysMethod.toastShowCenter(activity, "密码不能为空", Toast.LENGTH_SHORT);
            return;
        }

        if (password.length() < 6) {
            SysMethod.toastShowCenter(activity, "密码长度不能小于6位", Toast.LENGTH_SHORT);
            return;
        }

        if (password.length() > 20) {
            SysMethod.toastShowCenter(activity, "密码长度不能大于20位", Toast.LENGTH_SHORT);
            return;
        }
        activity.showLoading("登录中...", true, false);
        login();
    }

    public void login() {
        activity.hiddenKeyboard();
        MHRequestParams requestParams = new MHRequestParams();
        if (loginType == SELF) {
            requestParams.addParams("mobile_code", "+86");
            requestParams.addParams("mobile_number", phoneNumber);
            requestParams.addParams("password", com.haiqiu.miaohi.utils.MHStringUtils.getPassword(password));
            loginParam = ConstantsValue.Url.LOGIN;
        } else if (loginType == THIRDPART) {
            if (MHStringUtils.isEmpty(umengLoginInfo.getThreeId()) || MHStringUtils.isEmpty(umengLoginInfo.getNickname())) {
                activity.showToastAtBottom("获取信息失败,请重新尝试");
                return;
            }
            requestParams.addParams("login_name_id", umengLoginInfo.getThreeId());
            if (theThirdPartType == 3) {
                requestParams.addParams("weixin_open_id", umengLoginInfo.getMap().get("openid"));
                requestParams.addParams("weixin_union_id", umengLoginInfo.getThreeId());
            }
            requestParams.addParams("login_type", theThirdPartType + "");
            requestParams.addParams("nick_name", Base64Util.getBase64Str(umengLoginInfo.getNickname()));
            loginParam = ConstantsValue.Url.VERIFY_LOGINEX;
        }
        MHHttpClient.getInstance().post(LoginResponse.class, loginParam, requestParams, new MHHttpHandler<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse response) {
                afterLoginSuccess(response);
                activity.sendBroadcast(new Intent(ConstantsValue.IntentFilterAction.LOGIN_SUCCESS_ACTION));
            }

            @Override
            public void onFailure(String content) {
                activity.hiddenLoadingView();
                activity.showToastAtBottom("网络异常");
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                activity.hiddenLoadingView();
                activity.showToastAtBottom(message);
            }
        });
    }

    /**
     * 登陆成功后操作
     */
    public void afterLoginSuccess(final LoginResponse response) {
        LoginBean loginBean = response.getData();
        UserInfoUtil.saveUserInfo(activity, loginBean);
        //检查是否存在未分组的草稿如果存在则就行分组
        Intent intent = null;
        switch (loginType) {
            case SELF:
                //记住用户名和密码
//                SpUtils.put("USER_NAME", phoneNumber);
                SpUtils.put(ConstantsValue.Sp.USER_PHONE_NUMBER, phoneNumber);
//                SpUtils.put("is_login_success", true);
                SpUtils.put(ConstantsValue.Sp.IS_LOGIN_SUCCESS, true);
                if (TextUtils.equals("register", response.getBase().getCommand())) {
                    intent = new Intent(activity, LRPerfectInformationActivity.class);
                    intent.putExtra("undefine", "undefine");
                    startActivity(intent);
                    dismiss();
                } else {
                    ConnectionRong();
                }
                break;
            case THIRDPART:
                if (MHStringUtils.isEmpty(response.getData().getMiaohi_token())) {//秒嗨token为空时，完善用户信息，完成注册
                    intent = new Intent(activity, LRPerfectInformationActivity.class);
                    intent.putExtra("icon", umengLoginInfo.getHeaderUrl());
                    intent.putExtra("userName", umengLoginInfo.getNickname());
                    intent.putExtra("wx_open_id", umengLoginInfo.getMap().get("openid"));
                    intent.putExtra("theThirdId", umengLoginInfo.getThreeId());
                    intent.putExtra("theThirdPartType", theThirdPartType);
                    intent.putExtra("isNormal", 1);
                    intent.putExtra(ConstantsValue.Sp.UNDEFINE, ConstantsValue.Sp.UNDEFINE);
                    startActivityForResult(intent, 30);
                    dismiss();
                } else {
                    ConnectionRong();
                }
                break;
        }
    }

    private void ConnectionRong() {
        //用融云token连接融云服务器
        RongIMClient.setOnReceiveMessageListener(new RongReceiveMessageListener(activity));
        RongIMClient.connect(SpUtils.get(ConstantsValue.Sp.RONG_TOKEN, "") + "", new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                MHLogUtil.e(TAG, "rong_token失效");
                if (MHStringUtils.isEmpty(SpUtils.getString(ConstantsValue.Sp.TOKEN_MIAOHI))) {
                    return;
                }
                //请求新的rong_token
                MHRequestParams requestParams = new MHRequestParams();
                MHHttpClient.getInstance().post(CommonUserInfoResponse.class, ConstantsValue.Url.GET_RONG_VALID_TOKEN, requestParams, new MHHttpHandler<CommonUserInfoResponse>() {
                    @Override
                    public void onSuccess(CommonUserInfoResponse response) {
                        String rong_token = response.getData().getRong_token();
                        SpUtils.put(ConstantsValue.Sp.RONG_TOKEN, rong_token);
                        rongMethod(rong_token);
                        afterLoginSwitch();
                    }

                    @Override
                    public void onFailure(String content) {
                        afterLoginSwitch();
                    }

                    @Override
                    public void onStatusIsError(String message) {
                        afterLoginSwitch();
                        super.onStatusIsError(message);
                    }
                });
            }

            @Override
            public void onSuccess(String s) {
                MHLogUtil.i(TAG, "连接融云服务器成功--userId:" + s);
                activity.showToastAtBottom("登录成功");
                Intent intent = new Intent("receivedMsg");
                activity.sendBroadcast(intent);
                afterLoginSwitch();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                MHLogUtil.e(TAG, "获取rong_token失败--errorCode:" + errorCode.getMessage());
                afterLoginSwitch();
            }
        });

        SpUtils.put(ConstantsValue.Sp.IS_LOGINOUT_APP, false);
    }

    private void afterLoginSwitch() {
        dismiss();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (jumpType == 0) return;
        if (jumpType == 5) {
            startActivity(new Intent(activity, VideoRecorderActivity.class));
            return;
        }
        try {
            if (SpUtils.getBoolean(ConstantsValue.Sp.USER_LABEL_SELECTED, false)) {//判断用户是否选择过运动标签
                ((MainActivity) activity).switchFragment(jumpType - 1);
            } else {
                startActivity(new Intent(activity, RecommendSportsActivity.class));
            }
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }

    /**
     * 连接融云服务器
     */
    private void rongMethod(String token) {
        RongIMClient.setOnReceiveMessageListener(new RongReceiveMessageListener(activity));
        RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                MHLogUtil.e(TAG, "rong_token失效,重新请求");
            }

            @Override
            public void onSuccess(String s) {
                MHLogUtil.i(TAG, "获取rong_token成功--userId:" + s);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                MHLogUtil.e(TAG, "获取rong_token失败--errorCode:" + errorCode.getMessage());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
