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
import android.text.TextWatcher;
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
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.UserInfoBean;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.receiver.OpenQAEvent;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.response.UserInfoResponse;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.crop.Crop;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.view.MHActionSheetDialog;
import com.haiqiu.miaohi.view.MyCircleView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LiuXiang on 2016/10/27.
 * 开通映答填写资料界面
 */
public class OpenQaActivity extends BaseActivity implements View.OnClickListener {
    private static final int PHOTO_WITH_CAMERA = 37;        // 拍摄照片
    private static final String TAG = "BaseActivity";

    private float price = 0;
    private CommonNavigation cn;
    private MyCircleView iv_minedata_icon;
    private ImageView iv_arrow;
    private RelativeLayout rl_mine_data_seticon;
    private LinearLayout ll_common_qa_bottom;
    private EditText et_minedata_username;
    private EditText et_title;
    private EditText et_ask_price;
    private EditText et_be_good_at;
    private TextView tv_title;
    private TextView tv_head;
    private TextView tv_good_at_aspect;
    private TextView tv_next;
    private ImageLoader imageLoader;
    private Uri imageUri;
    private String filePath;
    private String headPortraitUri;
    private boolean isHeaderChage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_qa);

        initView();
        getNetData();
    }

    private void initView() {
        imageLoader = ImageLoader.getInstance();
        cn = (CommonNavigation) findViewById(R.id.cn);
        tv_head = (TextView) findViewById(R.id.tv_head);
        ll_common_qa_bottom = (LinearLayout) findViewById(R.id.ll_common_qa_bottom);
        iv_minedata_icon = (MyCircleView) findViewById(R.id.iv_minedata_icon);
        iv_arrow = (ImageView) findViewById(R.id.iv_arrow);
        rl_mine_data_seticon = (RelativeLayout) findViewById(R.id.rl_mine_data_seticon);
        et_minedata_username = (EditText) findViewById(R.id.et_minedata_username);
        et_title = (EditText) findViewById(R.id.et_title);
        et_be_good_at = (EditText) findViewById(R.id.et_be_good_at);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_good_at_aspect = (TextView) findViewById(R.id.tv_good_at_aspect);
        tv_next = (TextView) findViewById(R.id.tv_next);
        et_ask_price = (EditText) findViewById(R.id.et_ask_price);

        tv_next.setOnClickListener(this);
        rl_mine_data_seticon.setOnClickListener(this);
        ll_common_qa_bottom.setVisibility(View.VISIBLE);
        EditChangedListener editChangedListener = new EditChangedListener();
        et_title.addTextChangedListener(editChangedListener);
        et_minedata_username.addTextChangedListener(editChangedListener);
        et_be_good_at.addTextChangedListener(editChangedListener);
        et_ask_price.addTextChangedListener(editChangedListener);
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
    }

    private void getNetData() {
        showLoading();
        MHHttpClient.getInstance().post(UserInfoResponse.class, context, ConstantsValue.Url.GET_USERINFO, new MHHttpHandler<UserInfoResponse>() {
            @Override
            public void onSuccess(UserInfoResponse response) {
                UserInfoBean userInfoBean = response.getData();
                headPortraitUri = userInfoBean.getPortrait_uri();
                if (headPortraitUri.contains("?"))
                    headPortraitUri = headPortraitUri.substring(0, headPortraitUri.indexOf("?"));
                imageLoader.displayImage(headPortraitUri, iv_minedata_icon);
                et_minedata_username.setText(userInfoBean.getUser_name());
                et_minedata_username.setSelection(userInfoBean.getUser_name().length());

                if (!MHStringUtils.isEmpty(userInfoBean.getUser_authentic()))
                    et_title.setText(userInfoBean.getUser_authentic());
                if (!MHStringUtils.isEmpty(userInfoBean.getUser_note()))
                    et_be_good_at.setText(userInfoBean.getUser_note());
                et_minedata_username.clearFocus();
                et_title.clearFocus();
                et_minedata_username.clearFocus();
                et_be_good_at.clearFocus();
                et_ask_price.clearFocus();
            }

            @Override
            public void onFailure(String content) {

            }
        });
    }

    private void updatePicture() {
        if (isHeaderChage) {
            showLoading("正在上传...");
            UploadManager uploadManager = new UploadManager();
            String token = SpUtils.getString(ConstantsValue.Sp.TOKEN_QINIU_UPLOAD_ICON);
            if (MHStringUtils.isEmpty(filePath)) {
                commitInfo();
            } else {
                String key = getheaderName();
                headPortraitUri = "http://" + SpUtils.get(ConstantsValue.Sp.QINIU_WEB_ICON_BASE, "") + "/" + key;
                uploadManager.put(filePath, key, token, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.isOK()) {
                            commitInfo();
                        } else {
                            showToastAtBottom("保存失败");
                            hiddenLoadingView();
                        }
                    }
                }, null);
            }
        } else {
            commitInfo();
        }
    }

    /**
     * 提交用户填写的信息
     */
    private void commitInfo() {
        showLoading("提交数据...");
        String user_id = SpUtils.getString(ConstantsValue.Sp.USER_ID);
        String nick_name = et_minedata_username.getText().toString().trim();
        String answer_authentic = et_title.getText().toString().trim();
        String answer_note = et_be_good_at.getText().toString().trim();

        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("user_id", user_id);
        requestParams.addParams("user_portrait_uri", headPortraitUri);
        requestParams.addParams("nick_name", Base64Util.getBase64Str(nick_name));
        requestParams.addParams("answer_authentic", Base64Util.getBase64Str(answer_authentic));
        requestParams.addParams("answer_note", Base64Util.getBase64Str(answer_note));
        requestParams.addParams("question_price", (int) price + "");
        MHHttpClient.getInstance().post(BaseResponse.class, context, ConstantsValue.Url.QUESTION_AUTHORIZATION, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new OpenQAEvent());
                SpUtils.put(ConstantsValue.Sp.ANSER_DROIT, "1");
                Intent intent = new Intent(OpenQaActivity.this, PersonalHomeActivity.class);
                intent.putExtra("isSelf", true);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String content) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next:
                if (!isCheckPass()) return;
                updatePicture();
                break;
            case R.id.rl_mine_data_seticon:
                new MHActionSheetDialog(context, new MHActionSheetDialog.IMHActionSheetDialogListener() {
                    @Override
                    public void onSelect(String select, int index) {
                        if (index == 0) {
                            doTakePhoto();
                        } else {
                            Crop.pickImage(OpenQaActivity.this);
                        }
                    }
                }, "拍照上传", "相册选择");
                break;
            default:
                break;
        }
    }

    private boolean isCheckPass() {
        String user_id = SpUtils.getString(ConstantsValue.Sp.USER_ID);
        String nick_name = et_minedata_username.getText().toString().trim();
        String answer_authentic = et_title.getText().toString().trim();
        String answer_note = et_be_good_at.getText().toString().trim();
        if (MHStringUtils.isEmpty(user_id)) {
            showToastAtBottom("发生未知错误,请重新进入!");
            return false;
        }
        if (MHStringUtils.isEmpty(headPortraitUri)) {
            showToastAtBottom("还没有填完噢(～￣▽￣)～");
            return false;
        }
        if (MHStringUtils.isEmpty(nick_name)) {
            showToastAtBottom("还没有填完噢(～￣▽￣)～");
            return false;
        }
        if (MHStringUtils.isEmpty(answer_authentic)) {
            showToastAtBottom("还没有填完噢(～￣▽￣)～");
            return false;
        }
        if (MHStringUtils.isEmpty(answer_note)) {
            showToastAtBottom("还没有填完噢(～￣▽￣)～");
            return false;
        }
        if (!MHStringUtils.isEmpty(et_ask_price.getText().toString().trim())) {
            price = Float.parseFloat(et_ask_price.getText().toString().trim()) * 100;
            if (price > 200000) {
                showToastAtBottom("支付金额不能超过2000嗨币噢～");
                return false;
            }
        } else {
            showToastAtBottom("还没有填完噢(～￣▽￣)～");
            return false;
        }
        return true;
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
     */
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            iv_minedata_icon.setImageDrawable(null);
            iv_minedata_icon.setImageURI(Crop.getOutput(result));
            filePath = Crop.getOutput(result).getPath();
            headPortraitUri = "http://" + SpUtils.get(ConstantsValue.Sp.QINIU_WEB_ICON_BASE, "") + "/" + getheaderName();
            isHeaderChage = true;                       //用户头像修改
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取头像名称
     */
    private String getheaderName() {
        return "myIcon_" + formatDate();
    }

    private String formatDate() {
        // 获取"yyyy-MM-dd-HH:mm:ss"格式的当前时间
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = format.format(new Date(System.currentTimeMillis()));
        return time;
    }

    //编辑框输入监听
    class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            int a = et_title.getText().toString().trim().length();
            int b = et_ask_price.getText().toString().trim().length();
            int c = et_be_good_at.getText().toString().trim().length();
            int d = et_minedata_username.getText().toString().trim().length();
            if (a > 0 && b > 0 && c > 0 && d > 0) {
                tv_next.setBackgroundResource(R.drawable.blue_gray_selector_rectangle);
            } else {
                tv_next.setBackgroundColor(getResources().getColor(R.color.color_df));
            }
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
}
