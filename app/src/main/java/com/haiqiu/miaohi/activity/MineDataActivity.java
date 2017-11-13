package com.haiqiu.miaohi.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.base.FadeOutFromBottomItem;
import com.haiqiu.miaohi.bean.UserInfoBean;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.receiver.RefreshUserInfo;
import com.haiqiu.miaohi.response.SaveUserInfoResponse;
import com.haiqiu.miaohi.response.UserInfoResponse;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.TimeFormatUtils;
import com.haiqiu.miaohi.utils.ToastUtils;
import com.haiqiu.miaohi.utils.crop.Crop;
import com.haiqiu.miaohi.utils.upload.GetToken;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.view.DialogFadeOutFromBottom;
import com.haiqiu.miaohi.view.MHActionSheetDialog;
import com.haiqiu.miaohi.view.MHSelectDialog;
import com.haiqiu.miaohi.view.MyCircleView;
import com.haiqiu.miaohi.view.SelectPlaceDialog;
import com.haiqiu.miaohi.view.SelectTimeDialog;
import com.haiqiu.miaohi.widget.wheelview.WheelViewSelectCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 个人信息设置
 * Created by ningl on 2016/7/28.
 */
public class MineDataActivity extends BaseActivity implements View.OnClickListener {
    private static final int PHOTO_WITH_CAMERA = 37;        // 拍摄照片
    private static final String TAG = "MineDataActivity";

    private CommonNavigation cn;
    private LinearLayout ll_mine_data_seticon;
    private LinearLayout ll_common_qa_bottom;
    private ImageView iv_minedata_sex;
    private TextView tv_minedata_local;
    private TextView tv_necessary_mark1;
    private TextView tv_necessary_mark2;
    private TextView tv_necessary_mark3;
    private TextView tv_bottom_prompt;
    private EditText et_title;
    private EditText et_be_good_at;
    private EditText et_ask_price;
    private EditText et_minedata_sex;
    private EditText et_minedata_username;
    private EditText et_minedata_brithday;
    private View rightView;
    private MyCircleView iv_minedata_icon;
    private LinearLayout ll_minedata_bg;
    private ImageView iv_minedata_bg;

    private UserInfoBean userInfoBean;                      // 用户信息
    private UserInfoBean userInfoBeanTemp;                  // 原始用户信息
    private Uri imageUri;
    private String portraitUri;                             // 头像url
    private String filePath;                                // 要上传的图片本地地址
    private String province, city, area, name;
    private boolean hasSelection;
    private boolean isUndefine;
    private boolean isHeaderChage = false;                  // 用户是否更换头像
    private String prompt = "还没有填完噢(～￣▽￣)～";
    private boolean answer_auth;
    private static final int UPLOADHEADER = 0;
    private static final int UPLOADBG = 1;
    private int uploadType;
    private String bgUrl;
    private String imgUrl;
    private DialogFadeOutFromBottom dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minedata);
        answer_auth = getIntent().getExtras().getBoolean("answer_auth");
        if (!isLogin(false)) {
            finish();
            return;
        }
        initView();
        if (getIntent().getExtras() != null) {
            if (TextUtils.equals("undefine", getIntent().getExtras().getString("undefine"))) {
                cn.hideLeftLayout();
                portraitUri = getIntent().getExtras().getString("icon");
                String name = getIntent().getExtras().getString("userName");
                userInfoBean = new UserInfoBean();
                userInfoBeanTemp = new UserInfoBean();
                userInfoBean.setPortrait_uri(portraitUri);
                userInfoBean.setUser_name(name);

                userInfoBeanTemp.setPortrait_uri(portraitUri);
                userInfoBeanTemp.setUser_name(name);

                setUserInfo(userInfoBean);
                isUndefine = true;
                cn.hideLeftLayout();
            } else {
                userInfoBean = new UserInfoBean();
                setUserInfo(userInfoBean);
                getNetData();
            }
        }
        if (answer_auth)
            ll_common_qa_bottom.setVisibility(View.VISIBLE);
        else
            ll_common_qa_bottom.setVisibility(View.GONE);
    }

    private void initView() {
        cn = (CommonNavigation) findViewById(R.id.cn);
        ll_mine_data_seticon = (LinearLayout) findViewById(R.id.ll_mine_data_seticon);
        ll_common_qa_bottom = (LinearLayout) findViewById(R.id.ll_common_qa_bottom);
        iv_minedata_sex = (ImageView) findViewById(R.id.iv_minedata_sex);
        iv_minedata_icon = (MyCircleView) findViewById(R.id.iv_minedata_icon);
        tv_minedata_local = (TextView) findViewById(R.id.tv_minedata_local);
        tv_necessary_mark1 = (TextView) findViewById(R.id.tv_necessary_mark1);
        tv_necessary_mark2 = (TextView) findViewById(R.id.tv_necessary_mark2);
        tv_necessary_mark3 = (TextView) findViewById(R.id.tv_necessary_mark3);
        tv_bottom_prompt = (TextView) findViewById(R.id.tv_bottom_prompt);
        ll_minedata_bg = (LinearLayout) findViewById(R.id.ll_minedata_bg);
        iv_minedata_bg = (ImageView) findViewById(R.id.iv_minedata_bg);
        ll_minedata_bg.setOnClickListener(this);
        tv_bottom_prompt.setText(getString(R.string.tv_change_qa_price_rule1));
        et_title = (EditText) findViewById(R.id.et_title);
        et_be_good_at = (EditText) findViewById(R.id.et_be_good_at);
        et_ask_price = (EditText) findViewById(R.id.et_ask_price);
        et_ask_price.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        et_ask_price.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                int index = dest.toString().indexOf(".");
                if (dest.toString().contains(".") && index != 0) {
                    int mlength = dest.toString().substring(index).length();
                    if (mlength == 3 && et_ask_price.getSelectionStart() > dest.toString().indexOf(".")) {
                        return "";
                    }
                }
                return null;
            }
        }});
        et_minedata_sex = (EditText) findViewById(R.id.et_minedata_sex);
        et_minedata_username = (EditText) findViewById(R.id.et_minedata_username);
        et_minedata_brithday = (EditText) findViewById(R.id.et_minedata_brithday);
        tv_necessary_mark1.setText("");
        tv_necessary_mark2.setText("");
        tv_necessary_mark3.setText("");
        ll_mine_data_seticon.setOnClickListener(this);
        et_minedata_sex.setOnClickListener(this);
        tv_minedata_local.setOnClickListener(this);
        et_minedata_brithday.setOnClickListener(this);
        et_minedata_username.setCursorVisible(false);
        rightView = cn.getRightLayoutView();
        cn.setOnRightLayoutClickListener(new CommonNavigation.OnRightLayoutClick() {
            @Override
            public void onClick(View v) {
                if (checkParams(true)) {
                    if (checkParamsIsChanged()) {
//                        saveAllInfo();
                        saveUserInfo();
                    } else {
                        finish();
                    }
                }
            }
        });
        cn.setOnLeftLayoutClickListener(new CommonNavigation.OnLeftLayoutClick() {
            @Override
            public void onClick(View v) {
                if (getIntent().getIntExtra("isNormal", 0) == 1) {
                    cn.hideLeftLayout();
                } else {
                    setBack(false);
                }

            }
        });
        et_minedata_username.setOnClickListener(this);
        et_minedata_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                MHLogUtil.i(TAG, "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MHLogUtil.i(TAG, "onTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                MHLogUtil.i(TAG, "afterTextChanged");
//                if (s.toString().toString().length() > 0) {
//                    et_minedata_username.setSelection(s.toString().trim().length());
//                }
                if (null != s && checkStrLength(s) > 20) {
//                    showToastAtCenter("昵称最大只能是10个字哦");
                    et_minedata_username.setText(et_minedata_username.getText().toString().trim().substring(0, s.toString().length()-1));
                }
            }
        });
        et_minedata_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_minedata_username.setCursorVisible(true);
            }
        });
    }

    /**
     * 检测输入昵称长度
     * @return
     */
    public int checkStrLength(Editable s){
        String str =  s.toString();
        str = str.replaceAll("[^\\x00-\\xff]", "**");
        int  length  =  str.length();
        return  length;
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

    /**
     * 开始剪裁
     *
     * @param source
     */
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, final Intent result) {
        if (resultCode == RESULT_OK) {
            //获取token
            GetToken.getToken(context, new GetToken.IGetToken() {
                @Override
                public void getResult(boolean isOk) {
                    if (uploadType == UPLOADHEADER) {//上传头像
                        uploadImg(UPLOADHEADER, result);
                    } else if (uploadType == UPLOADBG) {//上传背景
                        uploadImg(UPLOADBG, result);
                    }
                }
            });
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 请求用户信息
     */
    private void getNetData() {
        showLoading();
        MHHttpClient.getInstance().post(UserInfoResponse.class, context, ConstantsValue.Url.GET_USERINFO, new MHHttpHandler<UserInfoResponse>() {
            @Override
            public void onSuccess(UserInfoResponse response) {
                userInfoBean = response.getData();
                if (userInfoBeanTemp == null) userInfoBeanTemp = new UserInfoBean();
                userInfoBeanTemp.setUser_name(userInfoBean.getUser_name());
                userInfoBeanTemp.setPortrait_uri(userInfoBean.getPortrait_uri());
                userInfoBeanTemp.setUser_gender(userInfoBean.getUser_gender());
                userInfoBeanTemp.setUser_area(userInfoBean.getUser_area());
                userInfoBeanTemp.setUser_birthday(userInfoBean.getUser_birthday());
                userInfoBeanTemp.setUser_birthdayStr(TimeFormatUtils.format24Time1(userInfoBean.getUser_birthday()));
                userInfoBean.setUser_birthdayStr(userInfoBeanTemp.getUser_birthdayStr());
                userInfoBeanTemp.setUser_authentic(userInfoBean.getUser_authentic());
                userInfoBeanTemp.setUser_note(userInfoBean.getUser_note());
                userInfoBeanTemp.setQuestion_price(userInfoBean.getQuestion_price());
                setUserInfo(userInfoBean);
                if (!userInfoBean.isPrice_allow_modify()) {
                    et_ask_price.setEnabled(false);
                    tv_bottom_prompt.setText(getResources().getString(R.string.tv_change_qa_price_rule1));
                } else {
                    et_ask_price.setEnabled(true);
                    tv_bottom_prompt.setText(getResources().getString(R.string.tv_change_qa_price_rule));
                }
                hiddenLoadingView();
            }

            @Override
            public void onFailure(String content) {
                hiddenLoadingView();
            }
        });
    }

    /**
     * 保存所有信息
     */
    private void uploadImg(final int uploadType, final Intent result) {
        showLoading();
        rightView.setEnabled(false);
        UploadManager uploadManager = new UploadManager();
        String token = null;
        String key = getheaderName();
        if (uploadType == UPLOADHEADER) {//上传头像
            token = SpUtils.getString(ConstantsValue.Sp.TOKEN_QINIU_UPLOAD_ICON);
            filePath = Crop.getOutput(result).getPath();
        } else if (uploadType == UPLOADBG) {//上传背景
            token = SpUtils.getString(ConstantsValue.Sp.TOKEN_QINIU_UPLOAD_IMAGE);
            filePath = Crop.getOutput(result).getPath();
        }
//        userInfoBean.setPortrait_uri("http://" + SpUtils.get(ConstantsValue.Sp.QINIU_WEB_ICON_BASE, "") + "/" + key);
        uploadManager.put(filePath, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    if (uploadType == UPLOADHEADER) {
                        iv_minedata_icon.setImageDrawable(null);
                        iv_minedata_icon.setImageURI(Crop.getOutput(result));
                        userInfoBean.setPortrait_uri("http://" + SpUtils.get(ConstantsValue.Sp.QINIU_WEB_ICON_BASE, "") + "/" + key);
                        imgUrl = "http://" + SpUtils.get(ConstantsValue.Sp.QINIU_WEB_ICON_BASE, "") + "/" + key;
                        isHeaderChage = true;
                    } else if (uploadType == UPLOADBG) {
                        iv_minedata_bg.setImageDrawable(null);
                        iv_minedata_bg.setImageURI(Crop.getOutput(result));
//                        userInfoBean.setBgurl("http://" + SpUtils.get(ConstantsValue.Sp.QINIU_WEB_ICON_BASE, "") + "/" + getheaderName());
                        bgUrl = "http://" + SpUtils.get(ConstantsValue.Sp.QINIU_WEB_IMAGE_BASE, "") + "/" + key;
                    }
                    hiddenLoadingView();
                    rightView.setEnabled(true);
                } else {
                    ToastUtils.showToastAtCenter(context, "保存失败");
                    rightView.setEnabled(true);
                    hiddenLoadingView();
                }
            }
        }, null);
    }

    /**
     * 保存用户信息
     */
    private void saveUserInfo() {
        String userLogoPath = ConstantsValue.Video.VIDEO_TEMP_PATH + "/user_logo_" + userInfoBean.getUser_id() + ".png";
        File file = new File(userLogoPath);
        if (file.exists()) file.delete();

        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("portrait_uri", userInfoBean.getMineDataPortrait_uri());
        requestParams.addParams("user_name", Base64Util.getBase64Str(userInfoBean.getUser_name()));
        requestParams.addParams("user_gender", userInfoBean.getUser_gender() + "");
        if (answer_auth) {
            requestParams.addParams("answer_authentic", Base64Util.getBase64Str(userInfoBean.getUser_authentic()));
            requestParams.addParams("answer_note", Base64Util.getBase64Str(userInfoBean.getUser_note()));
            requestParams.addParams("question_price", userInfoBean.getQuestion_price() + "");
        }
        if (!MHStringUtils.isEmpty(userInfoBean.getUser_area()))
            requestParams.addParams("user_area", Base64Util.getBase64Str(userInfoBean.getUser_area()));
        if (!MHStringUtils.isEmpty(userInfoBean.getUser_birthdayStr()))
            requestParams.addParams("user_birthday", userInfoBean.getUser_birthdayStr().replace("/", "-"));
        if (!MHStringUtils.isEmpty(bgUrl)) {
            requestParams.addParams("background_uri", bgUrl);
        }
        MHHttpClient.getInstance().post(SaveUserInfoResponse.class, context, ConstantsValue.Url.SET_USERINFO, requestParams, new MHHttpHandler<SaveUserInfoResponse>() {
            @Override
            public void onSuccess(SaveUserInfoResponse response) {
                EventBus.getDefault().post(new RefreshUserInfo());
                showToastAtBottom("保存成功");
                SpUtils.put(ConstantsValue.Sp.USER_NAME, userInfoBean.getUser_name());
                SpUtils.put(ConstantsValue.Sp.PORTRAIT_URI, userInfoBean.getMineDataPortrait_uri());
                rightView.setEnabled(true);
                hiddenLoadingView();
                finish();
            }

            @Override
            public void onFailure(String content) {
                hiddenLoadingView();
                showToastAtCenter("信息保存失败");
                rightView.setEnabled(true);
                hiddenLoadingView();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                showToastAtCenter("信息保存失败");
                rightView.setEnabled(true);
                hiddenLoadingView();
            }
        });
    }

    /**
     * 验证上传参数
     */
    private boolean checkParams(boolean isWarning) {
        if (MHStringUtils.isEmpty(userInfoBean.getMineDataPortrait_uri()) && MHStringUtils.isEmpty(filePath)) {
            if (isWarning) showToastAtBottom("请添加头像");
            return false;
        }
        if (MHStringUtils.isEmpty(et_minedata_sex.getText().toString())) {
            if (isWarning) showToastAtBottom("性别不能为空");
            return false;
        }
        if (MHStringUtils.isEmpty(et_minedata_username.getText().toString().trim())) {
            if (isWarning) showToastAtBottom("昵称不能为空");
            return false;
        } else {
            userInfoBean.setUser_name(et_minedata_username.getText().toString().trim());
        }
        if (answer_auth) {
            if (MHStringUtils.isEmpty(et_title.getText().toString())) {
                if (isWarning) showToastAtBottom(prompt);
                return false;
            } else {
                userInfoBean.setUser_authentic(et_title.getText().toString());
            }
            if (MHStringUtils.isEmpty(et_be_good_at.getText().toString())) {
                if (isWarning) showToastAtBottom(prompt);
                return false;
            } else {
                userInfoBean.setUser_note(et_be_good_at.getText().toString());
            }
            if (MHStringUtils.isEmpty(et_ask_price.getText().toString())) {
                if (isWarning) showToastAtBottom(prompt);
                return false;
            } else if (Float.parseFloat(et_ask_price.getText().toString()) > 2000) {
                showToastAtBottom("支付金额不能超过2000嗨币噢～");
                return false;
            } else {
                userInfoBean.setQuestion_price((long) (Float.parseFloat(et_ask_price.getText().toString()) * 100));
            }
        }
        if (MHStringUtils.isEmpty(et_minedata_brithday.getText().toString())) {
            userInfoBean.setUser_birthdayStr("");
        } else {
            userInfoBean.setUser_birthdayStr(et_minedata_brithday.getText().toString());
        }

        if (MHStringUtils.isEmpty(tv_minedata_local.getText().toString())) {
            userInfoBean.setUser_area("");
        } else {
            userInfoBean.setUser_area(tv_minedata_local.getText().toString());
        }

        return true;
    }

    /**
     * 查看参数是否被修改
     *
     * @return true:被修改 false:未修改
     */
    private boolean checkParamsIsChanged() {
        if (userInfoBeanTemp.getUser_birthdayStr() == null)
            userInfoBeanTemp.setUser_birthdayStr("");
        if (userInfoBeanTemp.getUser_area() == null) userInfoBeanTemp.setUser_area("");

        if (userInfoBean == null || userInfoBeanTemp == null)
            return true;
        if (isHeaderChage)
            return true;
        if (userInfoBeanTemp.getUser_name() == null || !TextUtils.equals(userInfoBeanTemp.getUser_name(), userInfoBean.getUser_name()))
            return true;
        if (userInfoBeanTemp.getUser_gender() != userInfoBean.getUser_gender())
            return true;
        if (!TextUtils.equals(userInfoBean.getUser_birthdayStr(), userInfoBeanTemp.getUser_birthdayStr()))
            return true;
        if (!TextUtils.equals(userInfoBean.getUser_area(), userInfoBeanTemp.getUser_area()))
            return true;
        if (!TextUtils.equals(userInfoBean.getUser_authentic(), userInfoBeanTemp.getUser_authentic()))
            return true;
        if (!TextUtils.equals(userInfoBean.getUser_note(), userInfoBeanTemp.getUser_note()))
            return true;
        if (imgUrl != null) return true;
        if (bgUrl != null) return true;
        return userInfoBean.getQuestion_price() != userInfoBeanTemp.getQuestion_price();
    }

    /**
     * 获取头像名称
     *
     * @return
     */
    private String getheaderName() {
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

    /**
     * 设置用户数据
     *
     * @return
     */
    private void setUserInfo(UserInfoBean userInfo) {
        if (!MHStringUtils.isEmpty(userInfo.getUser_name())) {          //用户名
            et_minedata_username.setText(userInfo.getUser_name().trim());
            et_minedata_username.setSelection(et_minedata_username.getText().toString().trim().length());
        }
        if (!MHStringUtils.isEmpty(userInfo.getUser_area()))            //用户地址
            tv_minedata_local.setText(userInfo.getUser_area());
        if (!MHStringUtils.isEmpty(userInfo.getUser_birthdayStr()))        //用户生日
            et_minedata_brithday.setText(userInfo.getUser_birthdayStr());
        if (!MHStringUtils.isEmpty(userInfo.getUser_authentic()))       //用户头衔
            et_title.setText(userInfo.getUser_authentic());
        if (!MHStringUtils.isEmpty(userInfo.getUser_note()))            //用户的擅长
            et_be_good_at.setText(userInfo.getUser_note());
        et_ask_price.setText(CommonUtil.formatPrice(userInfo.getQuestion_price()));//向该用户提问所需的价格

        ImageLoader.getInstance().displayImage(userInfo.getMineDataPortrait_uri(), iv_minedata_icon, DisplayOptionsUtils.getHeaderDefaultImageOptions());
        ImageLoader.getInstance().displayImage(userInfo.getBgurl(), iv_minedata_bg);
        if (userInfo.getUser_gender() == 1) {
            iv_minedata_sex.setImageResource(R.drawable.gender_man);
            et_minedata_sex.setText("男");
//            iv_minedata_sex.setVisibility(View.VISIBLE);
        } else if (userInfo.getUser_gender() == 2) {
            iv_minedata_sex.setImageResource(R.drawable.gender_women);
            et_minedata_sex.setText("女");
//            iv_minedata_sex.setVisibility(View.VISIBLE);
        } else {
//            iv_minedata_sex.setVisibility(View.GONE);
        }
        et_title.clearFocus();
        et_minedata_username.clearFocus();
        et_be_good_at.clearFocus();
        et_ask_price.clearFocus();
    }

    /**
     * 选择获取图片方式
     *
     * @param uploadType
     */
    private void selectImg(final int uploadType) {
        dialog = new DialogFadeOutFromBottom(MineDataActivity.this);
        List<FadeOutFromBottomItem> items = new ArrayList<>();
        FadeOutFromBottomItem item1 = new FadeOutFromBottomItem("#00a2ff", "拍照");
        FadeOutFromBottomItem item2 = new FadeOutFromBottomItem("#00a2ff", "相册");
        items.add(item1);
        items.add(item2);
        dialog.setItem(items);
        dialog.show();
        dialog.setOnSelectItemListener(new DialogFadeOutFromBottom.OnSelectItem() {
            @Override
            public void select(int index) {
                MineDataActivity.this.uploadType = uploadType;
                dialog.dismiss();
                if (index == 0) {
                    doTakePhoto();
                } else {
                    Crop.pickImage(MineDataActivity.this);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_mine_data_seticon:     //设置头像
                selectImg(UPLOADHEADER);
                break;

            case R.id.ll_minedata_bg://更换背景
                selectImg(UPLOADBG);
                break;

            case R.id.et_minedata_sex:          //选择性别
                new MHActionSheetDialog(MineDataActivity.this, new MHActionSheetDialog.IMHActionSheetDialogListener() {
                    @Override
                    public void onSelect(String select, int index) {
                        et_minedata_sex.setText(select);
                        if (index == 0) {
                            iv_minedata_sex.setImageResource(R.drawable.gender_man);
                            userInfoBean.setUser_gender(1);
                        } else if (index == 1) {
                            iv_minedata_sex.setImageResource(R.drawable.gender_women);
                            userInfoBean.setUser_gender(2);
                        }
//                        iv_minedata_sex.setVisibility(View.VISIBLE);
                    }
                }, "男", "女");
                break;
            case R.id.tv_minedata_local:        //选择地区
                if (!TextUtils.isEmpty(tv_minedata_local.getText().toString())) {
                    try {
                        String[] address = null;
                        if (tv_minedata_local.getText().toString().contains("\\|")) {
                            address = tv_minedata_local.getText().toString().split("\\|");
                        } else {
                            address = tv_minedata_local.getText().toString().split("\\s+");
                        }
                        if (address != null && address.length > 1) {
                            province = address[0];
                            city = address[1];
                            if (address.length == 2) {
                                area = "";
                            } else {
                                area = address[2];
                            }
                        }
                    } catch (Exception e) {
                        MHLogUtil.e(TAG,e);
                    }
                }
                new SelectPlaceDialog(MineDataActivity.this)
                        .setDefaultAddress("", "", "")
                        .setOnWheelViewSelectListener(new WheelViewSelectCallBack() {
                            @Override
                            public void select(String... params) {
                                String userArea = params[0] + getResources().getString(R.string.space) + params[1] + getResources().getString(R.string.space) + params[2];
                                tv_minedata_local.setText(userArea);
                                userInfoBean.setUser_area(userArea);
                            }
                        }).show();
                break;
            case R.id.et_minedata_brithday: //选择生日
                new SelectTimeDialog(MineDataActivity.this)
                        .setDate(et_minedata_brithday.getText().toString())
                        .setOnWheelViewSelectListener(new WheelViewSelectCallBack() {
                            @Override
                            public void select(String... params) {
                                et_minedata_brithday.setText(params[0] + "/" + params[1] + "/" + params[2]);
                                userInfoBean.setUser_birthdayStr(params[0] + "-" + params[1] + "-" + params[2]);
                            }
                        })
                        .show();
                break;
            case R.id.et_minedata_username:
                if (!hasSelection) {
                    et_minedata_username.setSelection(et_minedata_username.getText().toString().trim().length());
                    hasSelection = true;
                }
                break;
        }
    }

    private void setBack(boolean isWarning) {
        if (checkParams(isWarning)) {
            if (checkParamsIsChanged()) {
                MHSelectDialog dialog = new MHSelectDialog(MineDataActivity.this)
                        .setMessage("是否保存修改信息?")
                        .setLeftBtnText("不保存")
                        .setRightBtnText("保存")
                        .setLeftListener(new MHSelectDialog.LeftCallBack() {
                            @Override
                            public void onSelectLeft(MHSelectDialog dialog) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setRightCallBack(new MHSelectDialog.RightCallBack() {
                            @Override
                            public void onSelectRight(MHSelectDialog dialog) {
//                                saveAllInfo();
                                saveUserInfo();
                                dialog.dismiss();
                            }
                        });
                dialog.show();
            } else {
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                finish();
            }
        } else {
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isUndefine) {
                return true;
            } else {
                setBack(false);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        onHideSoftInput(event);
        return super.onTouchEvent(event);
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
}
