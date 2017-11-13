package com.haiqiu.miaohi.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.LoginBean;
import com.haiqiu.miaohi.bean.UserInfoBean;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.receiver.RefreshUserInfo;
import com.haiqiu.miaohi.response.CommonUserInfoResponse;
import com.haiqiu.miaohi.response.LoginResponse;
import com.haiqiu.miaohi.response.SaveUserInfoResponse;
import com.haiqiu.miaohi.rong.RongReceiveMessageListener;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.utils.crop.Crop;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.view.MHActionSheetDialog;
import com.haiqiu.miaohi.view.MyCircleView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.tendcloud.tenddata.TCAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.rong.imlib.RongIMClient;

/**
 * 完善个人资料
 * Created by LiuXiang on 2016/8/29.
 */
public class LRPerfectInformationActivity extends CommonLRActivity implements View.OnClickListener {
    private final String TAG = "LRPerfectInformationActivity";
    private static final int PHOTO_WITH_CAMERA = 37;    // 拍摄照片
    private ImageView iv_photo_mark;
    private TextView tv_login;
    private TextView tv_man;
    private TextView tv_woman;
    private RelativeLayout rl;
    private LinearLayout linearlayout;
    private EditText et_nickname;
    private MyCircleView iv_head_img;
    private CommonNavigation navigation;
    private String filePath;
    private String portraitUri;                         //头像url
    private String userName;                            //用户名
    private Uri imageUri;
    private boolean hasSelection = false;
    private boolean isUndefine;
    private boolean isBoundOrMain = false;              //点击下一步时跳转主界面还是绑定账号
    private UserInfoBean userInfoBean;                  //用户信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_information);
        initView();
        isFormThirdPart();
        addEvent();
        String s = et_nickname.getText().toString().trim();
        if (null != s && s.length() >= 1
                && !(MHStringUtils.isEmpty(userInfoBean.getMineDataPortrait_uri()) && MHStringUtils.isEmpty(filePath))
                && userInfoBean.getUser_gender() != 0) {
            tv_login.setBackgroundResource(R.drawable.selector_button_black);
        }
    }

    /**
     * 控件初始化
     */
    private void initView() {
        userInfoBean = new UserInfoBean();
        navigation = (CommonNavigation) findViewById(R.id.navigation);
        navigation.setNavigationBackgroundColor(getResources().getColor(R.color.white));
        navigation.setTitleTextColor(getResources().getColor(R.color.color_1d));
        navigation.setLeftIcon(R.drawable.to_left_arrow_thin);
        iv_head_img = (MyCircleView) findViewById(R.id.iv_head_img);
        rl = (RelativeLayout) findViewById(R.id.rl);
        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        iv_photo_mark = (ImageView) findViewById(R.id.iv_photo_mark);
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_man = (TextView) findViewById(R.id.tv_man);
        tv_woman = (TextView) findViewById(R.id.tv_woman);
        setResult(30);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tv_login.getLayoutParams();
        layoutParams.topMargin = ScreenUtils.getScreenHeight(context) - et_nickname.getBottom() - DensityUtil.dip2px(context, 50);
        tv_login.setLayoutParams(layoutParams);
    }

    /**
     * 是否第三方注册
     */
    private void isFormThirdPart() {
        if (getIntent().getExtras() != null) {
            String undefine = getIntent().getExtras().getString("undefine", "");
            if (TextUtils.equals("undefine", undefine)) {
                TCAgent.onEvent(context, "第三方完善信息页" + ConstantsValue.android);
                portraitUri = getIntent().getExtras().getString("icon");
                userName = getIntent().getExtras().getString("userName");
                userInfoBean.setPortrait_uri(portraitUri);
                userInfoBean.setUser_name(userName);
                setUserInfo(userInfoBean);
                isUndefine = true;
                isBoundOrMain = true;
            } else {
                TCAgent.onEvent(context, "手机号注册成功" + ConstantsValue.android);
                navigation.hideRightLayout();
                setUserInfo(userInfoBean);
            }
        } else {
            showToastAtBottom("信息缺失,请重新尝试");
        }
    }

    /**
     * 为控件添加事件
     */
    private void addEvent() {
        navigation.hideBottomLine();
        tv_login.setOnClickListener(this);
        tv_man.setOnClickListener(this);
        tv_woman.setOnClickListener(this);
        iv_head_img.setOnClickListener(this);
        et_nickname.setOnClickListener(this);
        et_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                MHLogUtil.i(TAG, "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MHLogUtil.i(TAG, "onTextChanged");
                if (null != s && s.length() >= 1
                        && !(MHStringUtils.isEmpty(userInfoBean.getMineDataPortrait_uri())
                        && MHStringUtils.isEmpty(filePath))
                        && userInfoBean.getUser_gender() != 0) {
                    tv_login.setBackgroundResource(R.drawable.selector_button_black);
                } else {
                    tv_login.setBackgroundColor(getResources().getColor(R.color.color_df));
                }
                int byteNum = 0;
                try {
                    byteNum = s.toString().trim().getBytes("GBK").length;
                } catch (UnsupportedEncodingException e) {
                    MHLogUtil.e(TAG,e);
                }
                if (null != s && byteNum > 20) {
                    showToastAtCenter("昵称超出限制啦~");
                    et_nickname.setText(s.toString().substring(0, s.toString().length() - 1));
                    try {
                        et_nickname.setSelection(s.toString().length() - 1);
                    } catch (Exception e) {
                        MHLogUtil.e(TAG,e);
                    }
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                MHLogUtil.i(TAG, "afterTextChanged");
//                if (s.length() > 0) {
//                    et_nickname.setSelection(s.length());
//                }
//                int byteNum = 0;
//                try {
//                    byteNum = s.toString().trim().getBytes("GBK").length;
//                } catch (UnsupportedEncodingException e) {
//                    MHLogUtil.e(TAG,e);
//                }
//                if (null != s && byteNum > 20) {
//                    showToastAtCenter("昵称最大只能是10个字哦");
//                    return;
//                }
            }
        });
    }
    //截取20字节的字符串
    private String subStr(String str, int subSLength)
            throws UnsupportedEncodingException{
        if (str == null)
            return "";
        else{
            //截取字节数
            int tempSubLength = subSLength;
            //截取的子串
            String subStr = str.substring(0, str.length()<subSLength ? str.length() : subSLength);
            //截取子串的字节长度
            int subStrByetsL = subStr.getBytes("GBK").length;
            //int subStrByetsL = subStr.getBytes().length;//截取子串的字节长度
            // 说明截取的字符串中包含有汉字
            while (subStrByetsL > tempSubLength){
                int subSLengthTemp = --subSLength;
                subStr = str.substring(0, subSLengthTemp>str.length() ? str.length() : subSLengthTemp);
                subStrByetsL = subStr.getBytes("GBK").length;
                //subStrByetsL = subStr.getBytes().length;
            }
            return subStr;
        }
    }
    /**
     * 验证上传参数
     */
    private boolean checkParams(boolean isWarning) {
        if (userInfoBean.getUser_gender() == 0) {
            if (isWarning) showToastAtBottom("请选择性别");
            return false;
        }
        if (MHStringUtils.isEmpty(userInfoBean.getMineDataPortrait_uri()) && MHStringUtils.isEmpty(filePath)) {
            if (isWarning) showToastAtBottom("请选择头像");
            return false;
        }
        if (MHStringUtils.isEmpty(et_nickname.getText().toString().trim())) {
            if (isWarning) showToastAtBottom("昵称不能为空");
            return false;
        } else {
            userInfoBean.setUser_name(et_nickname.getText().toString().trim());
        }
        return true;
    }

    /**
     * 上传用户头像
     */
    private void saveAllInfo() {
        showLoading("正在保存...", true, false);
        //七牛上传
        UploadManager uploadManager = new UploadManager();
        String token = SpUtils.getString(ConstantsValue.Sp.TOKEN_QINIU_UPLOAD_ICON);
        if (MHStringUtils.isEmpty(filePath)) {
            saveUserInfo();
        } else {
            String key = getHeaderName();
            userInfoBean.setPortrait_uri("http://" + SpUtils.get(ConstantsValue.Sp.QINIU_WEB_ICON_BASE, "") + "/" + key);
            uploadManager.put(filePath, key, token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {
                    if (info.isOK()) {
                        saveUserInfo();
                    } else {
                        showToastAtBottom("信息保存失败，请重试");
                        hiddenLoadingView();
                    }
                }
            }, null);
        }
    }

    /**
     * 保存用户信息(服务器后端保存)
     */
    private void saveUserInfo() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("portrait_uri", userInfoBean.getMineDataPortrait_uri());
        requestParams.addParams("user_name", Base64Util.getBase64Str(userInfoBean.getUser_name()));
        requestParams.addParams("user_gender", userInfoBean.getUser_gender() + "");
        MHHttpClient.getInstance().post(SaveUserInfoResponse.class, context, ConstantsValue.Url.SET_USERINFO, requestParams, new MHHttpHandler<SaveUserInfoResponse>() {
            @Override
            public void onSuccess(SaveUserInfoResponse response) {
                EventBus.getDefault().post(new RefreshUserInfo());
                showToastAtBottom("保存成功");
                SpUtils.put(ConstantsValue.Sp.USER_NAME, userInfoBean.getUser_name());
                hiddenLoadingView();
                finish();

                //跳转至主界面
                Intent intent = null;
                if (isBoundOrMain) {
                    TCAgent.onEvent(context, "第三方注册成功" + ConstantsValue.android);
                    intent = new Intent(LRPerfectInformationActivity.this, LRBoundPhoneActivity.class);
                    intent.putExtra("isFromAccountBound", 1);
                } else {
                    intent = new Intent(LRPerfectInformationActivity.this, RecommendSportsActivity.class);
                    context.sendBroadcast(new Intent("common_lr"));
                }
                if (intent != null)
                    startActivity(intent);
            }

            @Override
            public void onFailure(String content) {
                hiddenLoadingView();
                showToastAtCenter("信息保存失败");
                hiddenLoadingView();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                showToastAtCenter("信息保存失败");
                hiddenLoadingView();
            }
        });
    }

    /**
     * 设置用户数据
     */
    private void setUserInfo(UserInfoBean userInfo) {
        String user_name = userInfo.getUser_name();
        String portrait_uri = userInfo.getMineDataPortrait_uri();
        if (!MHStringUtils.isEmpty(user_name)) {
            try {
                et_nickname.setText(subStr(user_name,20));
            } catch (UnsupportedEncodingException e) {
                MHLogUtil.e(TAG,e);
            }
            et_nickname.setSelection(et_nickname.getText().toString().length());
        }
        if (!MHStringUtils.isEmpty(portrait_uri)) {
            ImageLoader.getInstance().displayImage(portrait_uri, iv_head_img, DisplayOptionsUtils.getHeaderDefaultImageOptions());
            iv_photo_mark.setVisibility(View.GONE);
        }
    }

    /**
     * 点击登录
     */
    private void submit() {
        //获取输入的昵称
        String nickname = et_nickname.getText().toString().trim();
        if (MHStringUtils.isEmpty(nickname)) {
            showToastAtBottom("昵称不能为空");
            return;
        }
        if (!checkParams(true)) {
            return;
        }
        showLoading("正在注册...", true, false);
        if (getIntent().getExtras().getString("undefine", "").equals("")) {
            //手机注册生成账户
//            saveAllInfo();
            registerPhoneNewUser();
        } else {
            //后台注册第三方用户
            registerNewUser();
        }
    }

    private void registerNewUser() {
        MHRequestParams requestParams = new MHRequestParams();

        String theThirdId = getIntent().getStringExtra("theThirdId");
        int theThirdPartType = getIntent().getIntExtra("theThirdPartType", 0);

        if (theThirdPartType == 3) {
            requestParams.addParams("weixin_open_id", getIntent().getStringExtra("wx_open_id"));
            requestParams.addParams("weixin_union_id", theThirdId);
        }
        requestParams.addParams("login_name_id", theThirdId);
        requestParams.addParams("user_name", Base64Util.getBase64Str(MHStringUtils.isEmpty(userName) ? " " : userName));
        requestParams.addParams("portrait_uri", MHStringUtils.isEmpty(portraitUri) ? ConstantsValue.Shared.ANDROID_DEFAULT_SHARE_IMAGE : portraitUri);
        requestParams.addParams("user_gender", "1");
        requestParams.addParams("nick_name", Base64Util.getBase64Str(MHStringUtils.isEmpty(userName) ? " " : userName));
        requestParams.addParams("login_type", String.valueOf(theThirdPartType));
        MHHttpClient.getInstance().post(LoginResponse.class, context, ConstantsValue.Url.LOGINEX, requestParams, new MHHttpHandler<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse response) {
                afterLoginSuccess(response);
//                sendBroadcast(new Intent(ConstantsValue.lOGIN_SUCCESS));
                //上传用户信息至服务器
                sendBroadcast(new Intent(ConstantsValue.IntentFilterAction.LOGIN_SUCCESS_ACTION));
                saveAllInfo();
            }

            @Override
            public void onFailure(String content) {
                failLogic();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                showToastAtBottom(message);
                failLogic();
            }
        });
    }

    /**
     * 手机号注册新用户
     */
    private void registerPhoneNewUser() {
        final MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("mobile_code", "+86");
        requestParams.addParams("mobile_number", getIntent().getExtras().getString("phone_number", ""));
        requestParams.addParams("password", MHStringUtils.getPassword(getIntent().getExtras().getString("psd", "")));
        requestParams.addParams("verify_number", getIntent().getExtras().getString("verification_code", ""));
        requestParams.addParams("portrait_uri", ConstantsValue.Shared.ANDROID_DEFAULT_SHARE_IMAGE);//设置手机注册头像为七牛上的默认图,新设置的图会在注册账号成功后自动上传并设置
        requestParams.addParams("user_name", Base64Util.getBase64Str(MHStringUtils.isEmpty(userName) ? " " : userName));
        requestParams.addParams("user_gender", "1");
        MHHttpClient.getInstance().post(LoginResponse.class, context, ConstantsValue.Url.REGISTER, requestParams, new MHHttpHandler<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse response) {
                afterLoginSuccess(response);
                sendBroadcast(new Intent(ConstantsValue.IntentFilterAction.LOGIN_SUCCESS_ACTION));
                //上传用户信息至服务器
                saveAllInfo();
            }

            @Override
            public void onFailure(String content) {
                failLogic();
            }

            @Override
            public void onStatusIsError(String message) {
                showToastAtBottom(message);
                failLogic();
                super.onStatusIsError(message);
            }
        });
    }

    public void afterLoginSuccess(final LoginResponse response) {
        LoginBean loginBean = response.getData();
        UserInfoUtil.saveUserInfo(this, loginBean);
        ConnectionRong();
    }

    private void ConnectionRong() {
        //用融云token连接融云服务器
        RongIMClient.setOnReceiveMessageListener(new RongReceiveMessageListener(this));
        RongIMClient.connect(SpUtils.get(ConstantsValue.Sp.RONG_TOKEN, "") + "", new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                MHLogUtil.e(TAG, "rong_token失效");
                if (!isLogin()) {
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
                        finish();
                    }

                    @Override
                    public void onFailure(String content) {
                        finish();
                    }

                    @Override
                    public void onStatusIsError(String message) {
                        finish();
                        super.onStatusIsError(message);
                    }
                });
            }

            @Override
            public void onSuccess(String s) {
                MHLogUtil.e(TAG, "连接融云服务器成功--userId:" + s);
                showToastAtBottom("登录成功");
                Intent intent = new Intent("receivedMsg");
                context.sendBroadcast(intent);
                finish();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                MHLogUtil.e(TAG, "获取rong_token失败--errorCode:" + errorCode.getMessage());
                finish();
            }
        });
//        SpUtils.put("isLoginoutApp", false);
        SpUtils.put(ConstantsValue.Sp.IS_LOGINOUT_APP, false);
    }

    private void rongMethod(String token) {
        RongIMClient.setOnReceiveMessageListener(new RongReceiveMessageListener(this));
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

    /**
     * 获取头像名称
     */
    private String getHeaderName() {
        return "myIcon_" + formatDate();
    }

    /**
     * 格式化当前时间
     */
    private String formatDate() {
        // 获取"yyyy-MM-dd-HH:mm:ss"格式的当前时间
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = format.format(new Date(System.currentTimeMillis()));
        return time;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:             //点击下一步
                submit();
                break;
            case R.id.tv_man:               //性别男
                selectSex(0);
                break;
            case R.id.tv_woman:             //性别女
                selectSex(1);
                break;
            case R.id.iv_head_img:          //设置头像
                new MHActionSheetDialog(context, new MHActionSheetDialog.IMHActionSheetDialogListener() {
                    @Override
                    public void onSelect(String select, int index) {
                        if (index == 0) {
                            doTakePhoto();
                        } else {
                            Crop.pickImage(LRPerfectInformationActivity.this);
                        }
                    }
                }, "拍照上传", "相册选择");
                break;
            case R.id.et_nickname:          //设置昵称
                if (!hasSelection) {
                    et_nickname.setSelection(et_nickname.getText().toString().length());
                    hasSelection = true;
                }
                break;
        }
    }

    private void selectSex(int i) {
        tv_man.setTextColor(getResources().getColor(R.color.selector_black_blue));
        tv_man.setBackgroundResource(R.drawable.line_black_18_radius);
        tv_woman.setTextColor(getResources().getColor(R.color.selector_black_red));
        tv_woman.setBackgroundResource(R.drawable.line_black_18_radius);
        if (i == 0) {
            tv_man.setTextColor(getResources().getColor(R.color.fontblue));
            tv_man.setBackgroundResource(R.drawable.line_blue_18_radius);
            userInfoBean.setUser_gender(1);
        } else {
            tv_woman.setTextColor(getResources().getColor(R.color.common_red));
            tv_woman.setBackgroundResource(R.drawable.line_red_18_radius);
            userInfoBean.setUser_gender(2);
        }
        if (null != et_nickname.getText().toString().trim() && et_nickname.getText().toString().trim().length() >= 1
                && !(MHStringUtils.isEmpty(userInfoBean.getMineDataPortrait_uri()) && MHStringUtils.isEmpty(filePath))
                && userInfoBean.getUser_gender() != 0) {
            tv_login.setBackgroundResource(R.drawable.selector_button_black);
        }
    }

    /**
     * 注册新用户失败时的逻辑
     */
    private void failLogic() {
//        showLoading("正在跳转...");
//        tv_login.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hiddenLoadingView();
//                startActivity(new Intent(LRPerfectInformationActivity.this, MainActivity.class));
//                context.sendBroadcast(new Intent("common_lr"));
//            }
//        }, 1500);
    }

    /**
     * 拍照获取相片
     */
    private void doTakePhoto() {
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
        // 下面这句指定调用相机拍照后的照片存储的路径
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, PHOTO_WITH_CAMERA);// CAMERA_OK是用作判断返回结果的标识
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0)
            return;
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        } else if (requestCode == PHOTO_WITH_CAMERA) {
            beginCrop(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        onHideSoftInput(event);
        return super.onTouchEvent(event);
    }

    /**
     * 开始剪裁
     */
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            iv_head_img.setImageDrawable(null);
            iv_head_img.setImageURI(Crop.getOutput(result));
            iv_photo_mark.setVisibility(View.GONE);
            filePath = Crop.getOutput(result).getPath();
            userInfoBean.setPortrait_uri("http://" + SpUtils.get(ConstantsValue.Sp.QINIU_WEB_ICON_BASE, "") + "/" + getHeaderName());
            if (et_nickname.getText().toString().trim().length() >= 1
                    && userInfoBean.getUser_gender() != 0) {
                tv_login.setBackgroundResource(R.drawable.selector_button_black);
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isClickEt(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            view.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            //此处根据输入框左上位置和宽高获得右下位置
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            //获取当前获得当前焦点所在View
            View view = getCurrentFocus();
            if (isClickEt(view, event)) {

                //如果不是edittext，则隐藏键盘

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    //隐藏键盘
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(event);
        }
        /**
         * 看源码可知superDispatchTouchEvent  是个抽象方法，用于自定义的Window
         * 此处目的是为了继续将事件由dispatchTouchEvent(MotionEvent event)传递到onTouchEvent(MotionEvent event)
         * 必不可少，否则所有组件都不能触发 onTouchEvent(MotionEvent event)
         */
        if (getWindow().superDispatchTouchEvent(event)) {
            return true;
        }
        return onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
