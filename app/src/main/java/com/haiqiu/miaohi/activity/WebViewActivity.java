package com.haiqiu.miaohi.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.Attention;
import com.haiqiu.miaohi.bean.ShareVideoAndImgInfo;
import com.haiqiu.miaohi.bean.VideoAndImg;
import com.haiqiu.miaohi.umeng.IUMShareResultListener;
import com.haiqiu.miaohi.umeng.ShareImg;
import com.haiqiu.miaohi.umeng.ShareSubject;
import com.haiqiu.miaohi.umeng.UmengShare;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.NoDoubleClickUtils;
import com.haiqiu.miaohi.utils.SetClickStateUtil;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.TimeFormatUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.utils.shareImg.SharePersonalHomeImgView;
import com.haiqiu.miaohi.utils.shareImg.ShareVideoAndImgView2;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.view.ShareDialog;
import com.haiqiu.miaohi.widget.ShareLayout;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;

/**
 * Created by LiuXiang on 2016/9/29.
 * 可处理有输入框的界面
 * 此界面导航栏在底部，带分享功能，使用时请区分使用
 */
public class WebViewActivity extends BaseActivity {
    private WebView wv;
    private String uri;                         //处理过后的uri
    private String originalUri;                 //进入页面的原始uri
    private boolean happenError;
    private CommonNavigation navigation;
    private ShareVideoAndImgView2 sviv_attention;
    private ImageView iv_videoandimgback;
    private ImageView iv_videoandimgshare;
    private String title;
    private String activity_note;
    private String activity_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        sviv_attention = (ShareVideoAndImgView2) findViewById(R.id.sviv_attention);
        originalUri = getIntent().getStringExtra("uri");
        title = getIntent().getStringExtra("title");
        activity_note = getIntent().getStringExtra("activity_note");
        activity_picture = getIntent().getStringExtra("activity_picture");
        if (!MHStringUtils.isEmpty(originalUri))
            if (originalUri.contains("?"))
                uri = originalUri + "&app=miaohi";
            else
                uri = originalUri + "?app=miaohi";
        if (isLogin())
            uri = uri + "&miaohi_token=" + SpUtils.getString(ConstantsValue.Sp.TOKEN_MIAOHI);
        initView();
        initSetting(wv.getSettings());
        initTitle(title);
        initData(uri);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        wv = (WebView) findViewById(R.id.wv);
        navigation = (CommonNavigation) findViewById(R.id.navigation);
        iv_videoandimgback = (ImageView) findViewById(R.id.iv_videoandimgback);
        iv_videoandimgshare = (ImageView) findViewById(R.id.iv_videoandimgshare);
        SetClickStateUtil.getInstance().setStateListener(iv_videoandimgback);
        SetClickStateUtil.getInstance().setStateListener(iv_videoandimgshare);
        iv_videoandimgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wv.canGoBack()) {
                    wv.goBack(); // goBack()表示返回WebView的上一页面
                } else {
                    finish();
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                }
            }
        });
        iv_videoandimgshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareSubject();
            }
        });
    }

    /**
     * 初始化webview设置
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initSetting(WebSettings webSettings) {
        wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setTextZoom(100);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        String cacheDirPath = context.getCacheDir() + "/WebCache";
        //设置数据库缓存路径
        webSettings.setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        webSettings.setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //对于不同尺寸手机的显示处理
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity >= 320) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity >= 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity >= 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
    }

    private void initTitle(String title) {
        navigation.setTitle(title);
        navigation.hideLeftArrow();
    }

    /**
     * 初始化数据
     */
    private void initData(String uri) {
        happenError = false;
        showMHLoading(true, false);
        wv.loadUrl(uri);
        wv.requestFocus();
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                happenError = true;
                wv.setVisibility(View.INVISIBLE);
                showErrorView();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hiddenLoadingView();
                if (!happenError) {
                    wv.setVisibility(View.VISIBLE);
                    hideErrorView();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    if (url.startsWith("miaohi://action")) {
                        Uri uri = Uri.parse(url);
                        skipPageByUri(uri, url);
                        return true;
                    }
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

    }

    @Override
    protected void reTry() {
        super.reTry();
        initData(uri);
    }

    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
            wv.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        wv.reload();
        super.onPause();
    }

    /**
     * 点击链接跳转到相关页面
     */
    private String isComment;

    private void skipPageByUri(Uri uri, String url) {
        if (uri != null) {
            String type = uri.getQueryParameter("type");
            final String id = uri.getQueryParameter("id");
            if (type.equals("1")) {
                isComment = uri.getQueryParameter("comment");
                isComment = isComment.equals("undefined") ? "false" : "true";
            }
            int mType = 0;
            try {
                mType = Integer.parseInt(type);
            } catch (Exception e) {
                MHLogUtil.e(TAG,e);
            }
//            if (id != null) {
            final Intent mIntent = new Intent();
            switch (mType) {
                case 1://视频详情
                    new android.os.Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<VideoAndImg> data = new ArrayList<>();
                            VideoAndImg obj = new VideoAndImg();
                            obj.setElement_type(1);
                            obj.setContent_type(1);
                            obj.setPhoto_id(id);
                            obj.setVideo_id(id);
                            data.add(obj);
                            Intent intent = new Intent(context, VideoAndImgActivity.class);
                            intent.putParcelableArrayListExtra("data", data)
                                    .putExtra("currentIndex", 0)
                                    .putExtra("userId", id)
                                    .putExtra("pageIndex", 0)
                                    .putExtra("isFromCommentList", Boolean.parseBoolean(isComment))
                                    .putExtra("command", ConstantsValue.Url.GETALLUSERPHONTSANDVIDEOS);
                            context.startActivity(intent);
                        }
                    });
                    break;
                case 2: //应答详情
                    mIntent.setClass(context, InterlocutionDetailsActivity.class)
                            .putExtra("question_id", id);
                    context.startActivity(mIntent);
                    break;
                case 3: //专题详情
                    mIntent.setClass(context, ActiveDetailActivity.class)
                            .putExtra("activityId", id);
                    context.startActivity(mIntent);
                    break;
                case 4: //个人中心
                    if (!isLogin(false)) {
                    } else {
                        skipPersonalCenter(context, id);
                    }
                    break;
                case 5://图片详情
                    ArrayList<VideoAndImg> data = new ArrayList<>();
                    VideoAndImg obj = new VideoAndImg();
                    obj.setElement_type(2);
                    obj.setContent_type(2);
                    obj.setPhoto_id(id);
                    obj.setVideo_id(id);
                    data.add(obj);
                    Intent intent = new Intent(context, VideoAndImgActivity.class);
                    intent.putParcelableArrayListExtra("data", data)
                            .putExtra("currentIndex", 0)
                            .putExtra("userId", id)
                            .putExtra("pageIndex", 0)
                            .putExtra("isJumpComment", Boolean.parseBoolean(isComment))
                            .putExtra("command", ConstantsValue.Url.GETALLUSERPHONTSANDVIDEOS);
                    context.startActivity(intent);
                    break;
                case 6://映答主页
                    break;
                case 7://分享
                    /**
                     * upload_user_name
                     * width
                     * height
                     * content_type
                     * video_note
                     * video_cover_uri
                     * share_link_address
                     video_id
                     photo_id
                     * photo_note
                     * photo_uri
                     * upload_time_text
                     */
                    uri = Uri.parse(url.replace("/#", ""));
                    if (!isLogin(false)) return;
                    Attention attention = new Attention();
                    if (uri.getQueryParameter("content_type").equals("1")) { //类型是1--分享视频
                        attention.setContent_type(1);
                        attention.setElement_type(1);
                        attention.setVideo_cover_uri(uri.getQueryParameter("video_cover_uri"));
                        attention.setVideo_note(uri.getQueryParameter("video_note"));
                        attention.setVideo_id(uri.getQueryParameter("video_id"));
                    } else {                                                //类型是2--分享图片
                        attention.setContent_type(2);
                        attention.setElement_type(2);
                        attention.setPhoto_uri(uri.getQueryParameter("photo_uri"));
                        attention.setPhoto_note(uri.getQueryParameter("photo_note"));
                        attention.setPhoto_id(uri.getQueryParameter("photo_id"));
                    }
                    attention.setUpload_time(Long.parseLong(uri.getQueryParameter("upload_time_text")));
                    attention.setHeight(Integer.parseInt(uri.getQueryParameter("height")));
                    attention.setWidth(Integer.parseInt(uri.getQueryParameter("width")));
                    attention.setUser_name(uri.getQueryParameter("upload_user_name"));

                    attention.setShare_link_address(uri.getQueryParameter("share_link_address").replaceFirst("share", "share/#"));
                    shareVideoAndImg(this, attention);
                    break;
            }

        }
    }

    /**
     * 跳转到个人中心
     *
     * @param context
     * @param userId
     */
    private void skipPersonalCenter(Context context, String userId) {
        Intent intent = new Intent(context, PersonalHomeActivity.class);
        intent.putExtra("userId", userId);
        if (TextUtils.equals(UserInfoUtil.getUserId(context), userId)) {
            intent.putExtra("isSelf", true);
        } else {
            intent.putExtra("isSelf", false);
        }
        intent.putExtra("activityType", 0);
        context.startActivity(intent);
    }

    /**
     * 分享视频
     */
    public void shareVideoAndImg(final Activity activity, final Attention attention) {
        final ShareDialog shareDialog = new ShareDialog((BaseActivity)activity);
        shareDialog.setData();
        shareDialog.setShareLable(ShareDialog.IMG);
        shareDialog.setShareLink(originalUri);
        shareDialog.setOnShareImgPath(new ShareLayout.OnShareImgPath() {
            @Override
            public void getimgPath(final SHARE_MEDIA platform) {
                shareDialog.dismiss();
                shareVideoAndIMGToPlatform(activity, platform, attention);
            }
        });

        ShareImg shareImg = null;
        if (attention.getElement_type() == 1) {
            if (MHStringUtils.isEmpty(attention.getVideo_id())) return;
            shareImg = new ShareImg(((BaseActivity) context), attention.getVideo_id(), "", "", "", "");
            if (UserInfoUtil.isMyself(context, attention.getUser_id())) {
                shareImg.setShowDelete(true);
                shareImg.setDeleteBtnType(ShareLayout.VIDEO_DELETE);
            } else {
                shareImg.setShowDelete(true);
                shareImg.setDeleteBtnType(ShareLayout.VIDEO_REPORT);
            }
        } else {
            if (MHStringUtils.isEmpty(attention.getPhoto_id())) return;
            shareImg = new ShareImg(((BaseActivity) context), attention.getPhoto_id(), "", "", "", "");
            if (UserInfoUtil.isMyself(context, attention.getUser_id())) {
                shareImg.setShowDelete(true);
                shareImg.setDeleteBtnType(ShareLayout.IMG_DELETE);
            } else {
                shareImg.setShowDelete(true);
                shareImg.setDeleteBtnType(ShareLayout.IMG_REPORT);
            }
        }
        shareDialog.setShareInfo(shareImg);
    }

    private void shareVideoAndIMGToPlatform(final Activity activity, final SHARE_MEDIA platform, final Attention attention) {
        ((BaseActivity) context).showLoading();
        ShareVideoAndImgInfo videoAndImgInfo = new ShareVideoAndImgInfo();
        videoAndImgInfo.setHeight(attention.getHeight());
        videoAndImgInfo.setWidth(attention.getWidth());
        videoAndImgInfo.setJoinTime("");
        videoAndImgInfo.setJoinTime(TimeFormatUtils.formatYMD(attention.getUpload_time()));
        videoAndImgInfo.setName(attention.getUser_name());
        videoAndImgInfo.setType(attention.getElement_type());
        videoAndImgInfo.setHeaderUrl(attention.getPortrait_uri());
        if (MHStringUtils.isEmpty(attention.getShare_link_address()))
            videoAndImgInfo.setQaCode_str("http://www.baidu.com");
        else videoAndImgInfo.setQaCode_str(attention.getShare_link_address());
        if (attention.getElement_type() == 1) {
            //视频
            videoAndImgInfo.setImgUrl(attention.getVideo_cover_uri());
            videoAndImgInfo.setNote(attention.getVideo_note());
            sviv_attention.setOnLoadFinishListener(new SharePersonalHomeImgView.OnLoadFinish() {
                @Override
                public void onFinish(Object path) {
                    UmengShare.sharedIMG(activity, platform, path, attention.getShare_link_address(), attention.getVideo_note(), new IUMShareResultListener((BaseActivity) context));
                }
            });
        } else {
            //图片
            videoAndImgInfo.setImgUrl(attention.getPhoto_uri());
            videoAndImgInfo.setNote(attention.getPhoto_note());
            sviv_attention.setOnLoadFinishListener(new SharePersonalHomeImgView.OnLoadFinish() {
                @Override
                public void onFinish(Object path) {
                    UmengShare.sharedIMG(activity, platform, path, attention.getShare_link_address(), attention.getPhoto_note(), new IUMShareResultListener((BaseActivity) context));
                }
            });
        }
        sviv_attention.genderImage(videoAndImgInfo, platform);
    }

    /**
     * 分享此专题
     */
    private void shareSubject() {
        String share_icon;
        String share_note;
        if (MHStringUtils.isEmpty(activity_picture))
            share_icon = ConstantsValue.Shared.ANDROID_DEFAULT_SHARE_IMAGE1;
        else
            share_icon = activity_picture;

        if (MHStringUtils.isEmpty(activity_note))
            share_note = title;
        else
            share_note = activity_note;
        if (isLogin(false))
            if (!NoDoubleClickUtils.isDoubleClick()) {
                final ShareDialog shareDialog = new ShareDialog(this);
                shareDialog.setData();
                ShareSubject umShare = new ShareSubject(this,
                        "shareId",
                        share_icon,
                        "分享人",
                        "subject",
                        share_note,
                        title);
                umShare.setShare_link_address(originalUri);
                shareDialog.setShareInfo(umShare);
                //隐藏举报
//                shareDialog.hidenReport();
//                shareDialog.hidenDelete();
//                shareDialog.setOnDeleteListener(this);
            }
    }

    @Override
    protected void onRestart() {
        initData(uri);
        super.onRestart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }

    @Override
    protected void onDestroy() {
        wv.destroy();
        super.onDestroy();
    }
}
