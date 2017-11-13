package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.Notify_user_result;
import com.haiqiu.miaohi.bean.TextInfo;
import com.haiqiu.miaohi.bean.UserInfo;
import com.haiqiu.miaohi.bean.VideoUploadInfo;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.receiver.ActivityEvent;
import com.haiqiu.miaohi.response.UserInfoResponse1;
import com.haiqiu.miaohi.utils.AbstractTextUtil;
import com.haiqiu.miaohi.utils.BitmapUtil;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DraftUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.TextUtil;
import com.haiqiu.miaohi.utils.ToastUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.utils.upload.UploadService;
import com.haiqiu.miaohi.view.NoteEditText;
import com.haiqiu.miaohi.widget.CommonDialog;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhandalin on 2016-12-05 14:17.
 * 说明:图片发布页面
 */
public class PicturePublishActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_preview;
    private NoteEditText et_add_describe;
    private TextView tv_add_describe_count;
    private View tv_publish;

    private boolean hasSaved;
    private VideoUploadInfo videoUploadInfo;
    private TextUtil edit_textUtil;
    private List<TextInfo> textInfos;
    private List<Notify_user_result> notify_user_results = new ArrayList<>();
    private ArrayList<String> userIdByat;
    private ArrayList<String> namesByat;
    private int lastHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_publish);
        videoUploadInfo = getIntent().getParcelableExtra("videoUploadInfo");
        edit_textUtil = TextUtil.getInstance();

        if (null == videoUploadInfo)
            videoUploadInfo = new VideoUploadInfo();
        initView();

        initData();
    }

    private void initView() {
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);

        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                int height = scrollView.getHeight();
                if (lastHeight == height) return;
                int keyboardHeight = Math.abs(lastHeight - height);
                MHLogUtil.d(TAG, "keyboardHeight=" + keyboardHeight);
                //109是根据布局算出来的
                scrollView.smoothScrollTo(0, keyboardHeight - DensityUtil.dip2px(context, 109));
                lastHeight = height;
            }
        });
        iv_preview = (ImageView) findViewById(R.id.iv_preview);
        View rl_image_container = findViewById(R.id.rl_image_container);
        //适配图片发布预览
        if (videoUploadInfo.getPictureWidth() > 0 && videoUploadInfo.getPictureHeight() > 0) {
            Point screenSize = ScreenUtils.getScreenSize(context);
            MHLogUtil.d(TAG, "getPaddingLeft=" + rl_image_container.getPaddingLeft());

            LinearLayout.LayoutParams parentParams = (LinearLayout.LayoutParams) rl_image_container.getLayoutParams();

            int targetWidth = screenSize.x - parentParams.leftMargin - parentParams.rightMargin;
            int targetHeight = targetWidth * videoUploadInfo.getPictureHeight() / videoUploadInfo.getPictureWidth();


            int enableHeight = screenSize.y - getResources().getDimensionPixelSize(R.dimen.navigation_height)
                    - parentParams.topMargin - parentParams.bottomMargin
                    - getResources().getDimensionPixelSize(R.dimen.publish_bottom_layout_height);
            if (targetHeight > enableHeight) {//这时候要以高度做为标准来计算
                targetHeight = enableHeight;
                targetWidth = targetHeight * videoUploadInfo.getPictureWidth() / videoUploadInfo.getPictureHeight();
            }
            parentParams.width = targetWidth;
            parentParams.height = targetHeight;
            rl_image_container.setLayoutParams(parentParams);

            iv_preview.setImageBitmap(BitmapUtil.loadBitmap(videoUploadInfo.getPicturePath(), videoUploadInfo.getPictureWidth(), videoUploadInfo.getPictureHeight()));
            rl_image_container.setBackgroundColor(Color.WHITE);
        }

        et_add_describe = (NoteEditText) findViewById(R.id.et_add_describe);
        tv_add_describe_count = (TextView) findViewById(R.id.tv_add_describe_count);

        findViewById(R.id.tv_at_friend).setOnClickListener(this);
        View tv_close = findViewById(R.id.tv_close);
        tv_close.setOnClickListener(this);

        findViewById(R.id.tv_save).setOnClickListener(this);
        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.ll_content).setOnClickListener(this);

        tv_publish = findViewById(R.id.tv_publish);
        tv_publish.setOnClickListener(this);

        if (VideoUploadInfo.FROM_DRAFTS == videoUploadInfo.getFromInfo()) {
            tv_close.setVisibility(View.GONE);
        }

        et_add_describe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() <= 0) {
                    tv_publish.setSelected(true);
                } else {
                    tv_publish.setSelected(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                edit_textUtil.handlerStringBlack(s.toString(), notify_user_results, new AbstractTextUtil() {
                    @Override
                    public void onClickSpan(final TextInfo textInfo) {
//                et_videodetail_input.setSelection(textInfo.getEnd());
                    }

                    @Override
                    public void getTextInfos(List<TextInfo> textInfos) {
                        super.getTextInfos(textInfos);
                        PicturePublishActivity.this.textInfos = textInfos;
                    }
                });
                int decribeCount = 200 - s.length();
                if (decribeCount >= 0) {
                    tv_add_describe_count.setTextColor(context.getResources().getColor(R.color.color_666));
                } else {
                    tv_add_describe_count.setTextColor(context.getResources().getColor(R.color.red_bg));
                }
                tv_add_describe_count.setText(decribeCount + "");
            }
        });

    }


    private void initData() {

        userIdByat = videoUploadInfo.getNotifyUserId();
        namesByat = videoUploadInfo.getNotifyUserName();

        if (null == userIdByat)
            userIdByat = new ArrayList<>();
        if (null == namesByat)
            namesByat = new ArrayList<>();

        for (int i = 0; i < namesByat.size(); i++) {
            Notify_user_result notify_user_result = new Notify_user_result();
            notify_user_result.setNotify_user_id(userIdByat.get(i));
            notify_user_result.setNotify_user_name(namesByat.get(i).trim().replace("@", ""));
            notify_user_results.add(notify_user_result);
        }

        et_add_describe.setText(videoUploadInfo.getVideoNote());
        String oldStr = videoUploadInfo.getVideoNote();

        et_add_describe.setText(edit_textUtil.handlerStringBlack(oldStr, notify_user_results, new AbstractTextUtil() {
            @Override
            public void onClickSpan(final TextInfo textInfo) {
//                        et_videodetail_input.setSelection(textInfo.getEnd());
            }

            @Override
            public void getTextInfos(List<TextInfo> textInfos) {
                PicturePublishActivity.this.textInfos = textInfos;
            }
        }));
        int length = et_add_describe.getText().toString().length();
        et_add_describe.setSelection(length);
        if (length > 0) {
            tv_publish.setSelected(false);
        }
        checkDeleteAtFriend();
    }

    /**
     * 监听软键盘删除@好友,并删除
     */
    public void checkDeleteAtFriend() {
        et_add_describe.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
//                    String inputStr = et_add_describe.getText().toString();
                    int selectorIndex = et_add_describe.getSelectionStart();
                    if (textInfos != null)
                        for (int i = 0; i < textInfos.size(); i++) {
                            TextInfo textInfo = textInfos.get(i);
                            if (selectorIndex > textInfo.getStart() && selectorIndex <= textInfo.getEnd()) {
                                textInfos.remove(i);
                                notify_user_results.remove(i);
                                Editable editable = et_add_describe.getText();
                                editable.delete(textInfo.getStart(), textInfo.getEnd());
                            }
                        }
                }
                return false;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
                finish();
                break;
            case R.id.iv_close:
                toBack();
                break;
            case R.id.tv_save:
                if (!isLogin(false)) {
                    return;
                }

                savePictureToDraft(null);
                TCAgent.onEvent(context, "存草稿箱" + ConstantsValue.android);
                break;

            case R.id.tv_at_friend:
                if (!isLogin(false))
                    return;
                Intent intent = new Intent(context, UserListActivity.class);                    //@好友界面
                intent.putExtra("type", UserListActivity.USER_LIST_TYPE_AT_FRIENDS);
                startActivityForResult(intent, UserListActivity.AT_FRIENDS_REQUEST_CODE);
                TCAgent.onEvent(context, "@好友" + ConstantsValue.android);
                break;
            case R.id.tv_publish:
                tv_publish.setEnabled(false);
                if (!publish()) {
                    tv_publish.setEnabled(true);
                }
                break;
            case R.id.ll_content:
                hiddenKeyboard();
                break;
        }
    }

    private boolean publish() {
//        if (null == videoUploadInfo.getQuestionId() && StringUtils.isEmpty(et_add_describe.getText().toString().trim())) {
//            showToastAtCenter("图片描述为空");
//            return false;
//        }
        TCAgent.onEvent(context, "发布" + ConstantsValue.android);
        if (!isLogin(false))
            return false;
        if (!CommonUtil.isNetworkAvailable(context)) {
            videoUploadInfo.setUploadFail(true);
            if (videoUploadInfo == null) return false;

            savePictureToDraft("好像断网了,图片已存入草稿箱");
            return false;
        }
        videoUploadInfo.setUploadFail(false);
        Intent it = new Intent(context, UploadService.class);

        String trim = et_add_describe.getText().toString().trim();
        if (trim.length() > 200) {
            showToastAtCenter("描述内容超出限制");
            return false;
//                            trim = trim.substring(0, 200);
        }
//        if (trim.length() <= 0) {
//            showToastAtCenter("请输入图片描述");
//            return false;
//        }
        videoUploadInfo.setVideoNote(trim);
        getUserInfo(it);
        return true;
    }

    public void doAction() {
        if (videoUploadInfo.getFromInfo() == VideoUploadInfo.FROM_DRAFTS) {
            //来自草稿箱
            Intent intent = new Intent(context, MineDraftsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            EventBus.getDefault().post(new ActivityEvent());
            context.startActivity(intent);
        } else if (videoUploadInfo.getFromInfo() == VideoUploadInfo.FROM_ACTIVITY) {
            Intent intent = new Intent(context, ActiveDetailActivity.class);
            intent.putExtra("activityId", videoUploadInfo.getActivity_id());
            intent.putExtra("activity_name", videoUploadInfo.getActivity_name());
            intent.putExtra("needRefresh", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            EventBus.getDefault().post(new ActivityEvent());
            context.startActivity(intent);
        } else if (videoUploadInfo.getFromInfo() == VideoUploadInfo.FROM_ASK_AND_ANSWER) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("pageIndex", 1);
            context.startActivity(intent);
        } else if (videoUploadInfo.getFromInfo() == VideoUploadInfo.FROM_COMMON) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("pageIndex", 1);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("pageIndex", 1);
            context.startActivity(intent);
        }
        finish();
    }

    private void toBack() {
        if (hasSaved) {
            toMainActivity();
            return;
        }
        final CommonDialog commonDialog = new CommonDialog(context);
        commonDialog.setLeftButtonMsg("放弃");
        commonDialog.setRightButtonMsg("再想想");
        commonDialog.setContentMsg("确认放弃发布此条状态吗？");
        commonDialog.setOnLeftButtonOnClickListener(new CommonDialog.LeftButtonOnClickListener() {
            @Override
            public void onLeftButtonOnClick() {
                toMainActivity();
            }
        });
        commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
            @Override
            public void onRightButtonOnClick() {
//                savePictureToDraft(null);
//                toMainActivity();
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }

    private void toMainActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }


    /**
     * 保存到草稿箱
     */
    public void savePictureToDraft(String message) {
        videoUploadInfo.setSaveTime(System.currentTimeMillis());
        String trim = et_add_describe.getText().toString().trim();
        if (trim.length() > 200) {
            trim = trim.substring(0, 200);
        }
        videoUploadInfo.setVideoNote(trim);
        videoUploadInfo.setSaveUserId(UserInfoUtil.getUserId(context));
        videoUploadInfo.setMediaType(VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE);

        if (DraftUtil.saveDraft(videoUploadInfo)) {
            if (null != message)
                showToastAtCenter(message);
            else
                showToastAtCenter("存入草稿箱成功");
            hasSaved = true;
        } else {
            showToastAtCenter("存入草稿箱失败");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
        if (null == data)
            return;
        switch (requestCode) {
            case UserListActivity.AT_FRIENDS_REQUEST_CODE:
                namesByat = data.getStringArrayListExtra("nameList");
                userIdByat = data.getStringArrayListExtra("userIds");
                if (null == namesByat)
                    namesByat = new ArrayList<>();
                if (null == userIdByat)
                    userIdByat = new ArrayList<>();
                if (namesByat == null) return;
                if (notify_user_results == null)
                    notify_user_results = new ArrayList<>();


                for (int i = 0; i < namesByat.size(); i++) {
                    Notify_user_result notify_user_result = new Notify_user_result();
                    notify_user_result.setNotify_user_id(userIdByat.get(i));
                    notify_user_result.setNotify_user_name(namesByat.get(i).trim().replace("@", ""));
                    notify_user_results.add(notify_user_result);
                    videoUploadInfo.addNotifyUserName(namesByat.get(i).trim());
                    videoUploadInfo.addNotifyUserId(userIdByat.get(i));
                }
                String oldStr = et_add_describe.getText().toString();
                for (int i = 0; i < namesByat.size(); i++) {
                    if (i == namesByat.size() - 1) {
                        oldStr += " " + namesByat.get(i).trim();
                    } else {
                        oldStr += " " + namesByat.get(i).trim() + " ";
                    }
                }
                et_add_describe.setText(edit_textUtil.handlerStringBlack(oldStr, notify_user_results, new AbstractTextUtil() {
                    @Override
                    public void onClickSpan(final TextInfo textInfo) {
//                        et_videodetail_input.setSelection(textInfo.getEnd());
                    }

                    @Override
                    public void getTextInfos(List<TextInfo> textInfos) {
                        PicturePublishActivity.this.textInfos = textInfos;
                    }
                }));
                et_add_describe.setSelection(et_add_describe.getText().length());
                break;

        }
    }

    /**
     * 获取人信息
     */
    public void getUserInfo(final Intent it) {
        MHRequestParams params = new MHRequestParams();
        params.addParams("user_id", UserInfoUtil.getUserId(context));
        //TODO UserInfoResponse1 改名
        MHHttpClient.getInstance().post(UserInfoResponse1.class, context, ConstantsValue.Url.GET_USERINFO, params, new MHHttpHandler<UserInfoResponse1>() {
            @Override
            public void onSuccess(UserInfoResponse1 response) {
                UserInfo userInfo = response.getData();
                videoUploadInfo.setUserInfo(userInfo);
                videoUploadInfo.setNotify_user_results(getNotifyUserResult(videoUploadInfo));
                it.putExtra("uploadTask", videoUploadInfo);
                startService(it);
                doAction();
            }

            @Override
            public void onFailure(String content) {
                ToastUtils.showToastAtCenter(context, "获取用户失败");
                tv_publish.setEnabled(true);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                ToastUtils.showToastAtCenter(context, "获取用户失败");
                tv_publish.setEnabled(true);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            toBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 获取@好友集合
     */
    public ArrayList<Notify_user_result> getNotifyUserResult(VideoUploadInfo uploadInfo) {
        ArrayList<Notify_user_result> notify_user_results = new ArrayList<>();
        if (uploadInfo == null) return notify_user_results;
        List<String> atUserName = uploadInfo.getNotifyUserName();
        List<String> atUserId = uploadInfo.getNotifyUserId();
        if (atUserName == null) return notify_user_results;
        if (atUserId == null) return notify_user_results;
        for (int i = 0; i < atUserId.size(); i++) {
            Notify_user_result notify_user_result = new Notify_user_result();
            notify_user_result.setNotify_user_name(atUserName.get(i).replace("@", ""));
            notify_user_result.setNotify_user_id(atUserId.get(i));
            notify_user_results.add(notify_user_result);
        }
        return notify_user_results;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MHLogUtil.d(TAG, "onDestroy");
    }


}
