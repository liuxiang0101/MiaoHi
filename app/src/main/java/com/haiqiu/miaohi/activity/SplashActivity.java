package com.haiqiu.miaohi.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.BannerSubjectBean;
import com.haiqiu.miaohi.okhttp.MHHttpBaseHandler;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.CommonUserInfoResponse;
import com.haiqiu.miaohi.response.GetReplaceImageResponse;
import com.haiqiu.miaohi.rong.RongReceiveMessageListener;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tendcloud.tenddata.TCAgent;

import java.io.File;
import java.io.InputStream;

import io.rong.imlib.RongIMClient;

/**
 * 闪屏页面
 */
public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";
    private ImageView iv_ad;
    private ImageView iv_splash;
    private ImageView iv_engish;
    private TextView tv_countdown_number;
//    private TextView tv_version;
    private LinearLayout ll_countdown_click_skip;
    private Handler handler;
    private ImageLoader imageLoader;
    private int requestTimes = 0;
    private int theTimeLeft = 1;
    private boolean isLeft = false;
    private boolean isFinish = false;
    private boolean isFinishAnim = false;
    private boolean isClickSkip = false;
    private boolean isOneSecondLoadComplete = false;
    private int advert_type;
    private String advert_info;
    private String advert_info_extra;
    private Gson gson = new Gson();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
//        generatedLinkME();
        //加载补丁包，每次启动时都要检查更新，位置慎动
//        initPatch();
//        setUmengDialog();

        //检查是否存在未分组的草稿如果存在则就行分组
//        DraftHandlerUtil.getInstance().saveOldDraftToNewDraft(context);
//        SpUtils.put(ConstantsValue.Sp.FIRST_USE_APP, System.currentTimeMillis() + "");
        //app上一版本的版本号
        String last_version_code = SpUtils.getString(ConstantsValue.Sp.LAST_VERSION_CODE);
        MHLogUtil.d(TAG, "last_version_code=" + last_version_code);
        //是否新版本发布,若是,进入引导页
//        if (null == last_version_code || !last_version_code.equals(MHApplication.versionCode)) {
//            afterStartUpLogic(0);
//            return;
//        }

        initView();

        if (isLogin()) {
            //开发版app不会出现三秒广告，如需要，请注释掉
            if (ConstantsValue.isDeveloperMode(null))
                openThreadTask(3, 0);
            loadLocalImageLogic();
        } else {
            startActivity(new Intent(this, VideoPropagandaActivity.class));
            finish();
        }
    }

    /**
     * 跳转界面时执行的逻辑
     */
    private void afterStartUpLogic(int i) {
        //做一些初始化的东西
        isFinishAnim = true;
        Intent intent = new Intent(context, i == 0 ? GuideAct.class : MainActivity.class);
        connectionToRong();
        startActivity(intent);
        finish();// 结束当前页面
    }

    /**
     * 初始化控件
     */
    private void initView() {
        iv_ad = (ImageView) findViewById(R.id.iv_ad);
        iv_engish = (ImageView) findViewById(R.id.iv_engish);
        tv_countdown_number = (TextView) findViewById(R.id.tv_countdown_number);
        iv_splash = (ImageView) findViewById(R.id.iv_splash);
        ll_countdown_click_skip = (LinearLayout) findViewById(R.id.ll_countdown_click_skip);
        ll_countdown_click_skip.setOnClickListener(l);
        iv_splash.setOnClickListener(l);
        iv_ad.setOnClickListener(l);

        handler = new Handler();
        imageLoader = ImageLoader.getInstance();

//        tv_version = (TextView) findViewById(tv_version);
//        if (ConstantsValue.isDeveloperMode(context)) {
//            //如果要改版本号,请在这里更改,正式发版不会生效
//            tv_version.setText("v" + MHApplication.versionName);
//        } else {
//            tv_version.setText("v" + MHApplication.versionName);
//        }
    }

    /**
     * 加载启动页逻辑
     */
    private void loadLocalImageLogic() {
//        iv_splash.setImageBitmap(readBitMap(this, R.drawable.splash));
        iv_splash.setImageResource(R.drawable.splash);
        iv_ad.setVisibility(View.INVISIBLE);
        getAdImage();
        //如果一秒之内加载不出广告图。直接跳过
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isOneSecondLoadComplete) {
                    openThreadTask(3, 1000);
                }
            }
        }, 1000);
    }

    /**
     * 加载广告图片
     */
    private void getAdImage() {
        final MHRequestParams requestParams = new MHRequestParams();
        //      requestParams.addParams("version_name", "");
        MHHttpClient.getInstance().post(GetReplaceImageResponse.class, ConstantsValue.Url.GET_IMAGE_REPLACE, requestParams, new MHHttpHandler<GetReplaceImageResponse>() {
            @Override
            public void onSuccess(GetReplaceImageResponse response) {
                String image_url = "";
                int isShowPic = 0;
                //String h5_url = "";
                if (response.getData().getAdvert_result() != null) {
                    image_url = response.getData().getAdvert_result().getImage_uri_6();
                    isShowPic = response.getData().getAdvert_result().getAdvert_onoff();
                    advert_info = response.getData().getAdvert_result().getAdvert_info();
                    advert_type = response.getData().getAdvert_result().getAdvert_type();
                    advert_info_extra = response.getData().getAdvert_result().getAdvert_info_extra();
                }
                if (MHStringUtils.isEmpty(image_url)) {//广告图为空时，启动页后直接进入
                    openThreadTask(3, 1000);
                } else {//有广告图时，加载显示后进入
                    loadNetImageLogic(image_url, isShowPic);
                }
                //saveImageToLocalLogic(response);
            }

            @Override
            public void onFailure(String content) {
                MHLogUtil.e(TAG, "onFailure--" + content);
            }

            @Override
            public void onStatusIsError(String message) {
                MHLogUtil.e(TAG, "onStatusIsError--" + message);
                super.onStatusIsError(message);
            }
        });
    }

    /**
     * 加载网络广告图片逻辑
     */
    private void loadNetImageLogic(String url, final int isShowAd) {
        ImageLoader.getInstance().displayImage(url, iv_ad, DisplayOptionsUtils.getSilenceDisplayBuilder(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                MHLogUtil.e(TAG, "广告图加载失败" + failReason.toString());
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (isShowAd == 0) {
                    MHLogUtil.e(TAG, "广告图 hide");
                    return;
                }
                isOneSecondLoadComplete = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isFinishAnim) return;
                        iv_ad.setVisibility(View.VISIBLE);
                        TranslateAnimation ta1 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, -1, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
                        TranslateAnimation ta2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                        ta1.setDuration(500);
                        ta2.setDuration(500);
                        iv_engish.startAnimation(ta1);
                        iv_splash.startAnimation(ta1);
//                        tv_version.startAnimation(ta1);
                        iv_ad.startAnimation(ta2);
                        ta2.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                TCAgent.onEvent(context, "3秒广告展示" + ConstantsValue.android);
                                iv_splash.setVisibility(View.INVISIBLE);
                                iv_engish.setVisibility(View.INVISIBLE);
//                                tv_version.setVisibility(View.INVISIBLE);
                                ll_countdown_click_skip.setVisibility(View.VISIBLE);
                                openThreadTask(1, 1000);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }, 1000);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
            }
        });

    }

    /**
     * 开启线程执行操作
     */
    private void openThreadTask(final int time, long delayTime) {
        if (time == 1) MHLogUtil.e(TAG, "3秒倒计时开始");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (time) {
                    case 1:
                        tv_countdown_number.setText("2");
                        if (!isClickSkip && !isLeft && isOneSecondLoadComplete)
                            openThreadTask(time + 1, 1000);
                        break;
                    case 2:
                        tv_countdown_number.setText("1");
                        if (!isClickSkip && !isLeft && isOneSecondLoadComplete)
                            openThreadTask(time + 1, 1000);
                        break;
                    case 3:
                        MHLogUtil.e(TAG, "3秒倒计时结束");
                        if (!isLeft && !isFinish) {
                            afterStartUpLogic(1);
                            isFinish = true;
                        }
                        break;
                }
                theTimeLeft = time;
            }
        }, delayTime);
    }

    /**
     * 图片保存至本地逻辑
     *
     * @param response
     */
    private void saveImageToLocalLogic(GetReplaceImageResponse response) {
        String imageUri = response.getData().getAdvert_result().getImage_uri_4();                       //图片uri
        String imageName = response.getData().getAdvert_result().getImage_name();                       //图片名称
        String saveImageFile = Environment.getExternalStorageDirectory() + "/MiaoHi/ReplaceImage";      //存放图片的文件夹
        final String targetPath = saveImageFile + "/" + imageName;                                      //图片本地绝对路径
        //文件夹不存在或被删除时重新创建
        File destDir = new File(saveImageFile);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        File targetFile = new File(targetPath);//本地文件
        boolean isHaveNewSplashImage = !SpUtils.getString(ConstantsValue.Sp.THE_PATH_SAVE_SPLASH_IMAGE).equals(targetPath);//服务器是否有新的splash图片
        if ((!targetFile.exists()) || isHaveNewSplashImage) {//如果本地文件不存在或有新文件时，重新下载并存储
            if (isHaveNewSplashImage) {
                File oldFile = new File(SpUtils.getString(ConstantsValue.Sp.THE_PATH_SAVE_SPLASH_IMAGE));
                if (oldFile.exists()) {
                    oldFile.delete();
                    MHLogUtil.d(TAG, "原splash图片已删除");
                }
            }
            //服务器下载新的splash闪屏图片
            MHHttpClient.getInstance().downloadAsyn(imageUri, saveImageFile + "/" + imageName, new MHHttpBaseHandler() {
                @Override
                public void onSuccess(String content) {
                    SpUtils.put(ConstantsValue.Sp.THE_PATH_SAVE_SPLASH_IMAGE, targetPath);
                    MHLogUtil.d(TAG, "存储splash图片");
                }

                @Override
                public void onFailure(String content) {
                    MHLogUtil.e(TAG, "存储splash图片失败" + content);
                }
            });
        }
    }

    /**
     * 连接融云服务器
     */
    private void connectionToRong() {
        //如果本地没有存储秒嗨token时，不进行融云的连接
        if (!isLogin()) {
            return;
        }
        //新版本升级后，已登录的用户(1.0版本无rong_token)获取rong_token
        if (SpUtils.getBoolean(ConstantsValue.Sp.FIRST_INTO_NEW_VERSION_APP, true)) {
            requestNewRongToken();
        } else {
            rongMethod(SpUtils.getString(ConstantsValue.Sp.RONG_TOKEN));
        }

    }

    /**
     * 获取新的rong_token(无rong_tokekn或当前rong_token有错误时)
     */
    private void requestNewRongToken() {
        if (!isLogin()) return;
        MHRequestParams requestParams = new MHRequestParams();
        MHHttpClient.getInstance().post(CommonUserInfoResponse.class, ConstantsValue.Url.GET_RONG_VALID_TOKEN, requestParams, new MHHttpHandler<CommonUserInfoResponse>() {
            @Override
            public void onSuccess(CommonUserInfoResponse response) {
                //将获取到的新rong_token存入sp
                String rong_token = response.getData().getRong_token();
                SpUtils.put(ConstantsValue.Sp.RONG_TOKEN, rong_token);

                rongMethod(rong_token);
            }

            @Override
            public void onFailure(String content) {
            }
        });
    }

    /**
     * 使用rong_token连接融云服务器
     *
     * @param token
     */
    private void rongMethod(String token) {
        RongIMClient.setOnReceiveMessageListener(new RongReceiveMessageListener(getApplicationContext()));
        RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                requestTimes++;
                MHLogUtil.e(TAG, "rong_token失效,重新请求--第" + requestTimes + "次");
                if (requestTimes < 2) {
                    requestNewRongToken();
                }
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
     * 初始补丁程序
     */
//    private void initPatch() {
//        if ("qihu360".equals(ConstantsValue.BUSINESS)) {
//            MHLogUtil.d(TAG, "qihu360渠道");
//            return;
//        }
//        String url;
//        if (!ConstantsValue.SERVER_URL.contains("app")) {
//            url = "http://res.test.miaohi.com/" + MHApplication.versionCode + "_info.json";
//        } else {
//            url = "http://res.miaohi.com/" + MHApplication.versionCode + "_info.json";
//        }
//        MHLogUtil.d(TAG, "url=" + url);
//        MHHttpClient.getInstance().requestService(url, new MHHttpBaseHandler() {
//            @Override
//            public void onSuccess(String content) {
//                MHLogUtil.d(TAG, content);
//                try {
//                    PatchManager patchManager = new PatchManager(getApplicationContext());
//                    JSONObject jsonObject = new JSONObject(content);
//                    if (jsonObject.has("remove_all_patch")) {
//                        if (jsonObject.getBoolean("remove_all_patch")) {
//                            MHLogUtil.d(TAG, "remove_all_patch");
//                            patchManager.removeAllPatch();
//                            return;
//                        }
//                    }
//                    if (jsonObject.has("patch_file_url")) {
//                        String patch_file_url = jsonObject.getString("patch_file_url");
//                        final int patch_version_code = jsonObject.getInt("patch_version_code");
////                        int version_code = (int) SpUtils.get("patch_version_code", 0);
//                        int version_code = (int) SpUtils.get(ConstantsValue.Sp.PATCH_VERSION_CODE, 0);
//                        String lastVersion = (String) SpUtils.get(ConstantsValue.Sp.LAST_VERSION_CODE, "");
//                        if (!MHApplication.versionCode.equals(lastVersion)) {
//                            MHLogUtil.d(TAG, MHApplication.versionCode + "当前不是最新版本,直接去拿补丁文件...");
//                            getPatchFile(patch_file_url, patch_version_code, patchManager);
//                        } else if (version_code < patch_version_code) {//表示当前版本有新的补丁文件
//                            MHLogUtil.d(TAG, MHApplication.versionCode + "版本有新的补丁文件...");
//                            getPatchFile(patch_file_url, patch_version_code, patchManager);
//                        } else {
//                            MHLogUtil.d(TAG, MHApplication.versionCode + "无需更新补丁包...");
//                            try {
//                                patchManager.init(MHApplication.versionCode);
//                                patchManager.loadPatch();
//                            } catch (Exception e) {
//                                MHLogUtil.d(TAG, "loadPatch 失败");
//                                MHLogUtil.e(TAG,e);
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    MHLogUtil.e(TAG,e);
//                }
//            }
//
//            @Override
//            public void onFailure(String content) {
//
//            }
//        });
//    }
//
//    private void getPatchFile(String patch_file_url, final int patch_version_code, final PatchManager patchManager) {
////        SpUtils.put("patch_version_code", -1);//如果下载失败保证下次还能下载
//        SpUtils.put(ConstantsValue.Sp.PATCH_VERSION_CODE, -1);//如果下载失败保证下次还能下载
//        final String patchFilePath = getCacheDir() + File.separator + ConstantsValue.Other.PATCH_FILE_NAME;
//        MHHttpClient.getInstance().downloadAsyn(patch_file_url, patchFilePath, new MHHttpBaseHandler() {
//            @Override
//            public void onSuccess(String content) {
//                MHLogUtil.d(TAG, "补丁下载成功...");
////                SpUtils.put("patch_version_code", patch_version_code);
//                SpUtils.put(ConstantsValue.Sp.PATCH_VERSION_CODE, patch_version_code);
//                try {
//                    patchManager.addPatch(patchFilePath);
//                    MHLogUtil.d(TAG, "补丁加载成功...");
//                } catch (Exception e) {
//                    MHLogUtil.d(TAG, "补丁加载失败...");
//                    MHLogUtil.e(TAG,e);
//                }
//            }
//
//            @Override
//            public void onFailure(String content) {
//            }
//        });
//    }

    /**
     * 点击事件监听
     */
    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_countdown_click_skip:  //跳过广告页
                    TCAgent.onEvent(context, "3秒广告跳过" + ConstantsValue.android);
                    openThreadTask(3, 0);
                    isClickSkip = true;
                    break;
                case R.id.iv_ad:                    //点击广告页，跳转相应界面
                    TCAgent.onEvent(context, "3秒广告点击" + ConstantsValue.android);
                    advertLinksJumpLogic();
                    break;
                case R.id.iv_splash:
                    break;
            }
        }
    };

    /**
     * 广告链接跳转逻辑
     */
    private void advertLinksJumpLogic() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isIntoFromPush", true);
        intent.putExtra("isAdLink", true);
        if (advert_type != 0) isLeft = true;
        switch (advert_type) {
            case 1:
                if (MHStringUtils.isEmpty(advert_info_extra)) return;
                intent.putExtra("type", "ad_h5");
                intent.putExtra("html_uri", advert_info);
                BannerSubjectBean bean1 = new BannerSubjectBean();
                if (!MHStringUtils.isEmpty(advert_info_extra)) {
                    bean1 = gson.fromJson(advert_info_extra, BannerSubjectBean.class);
                    intent.putExtra("html_name", bean1.getHtml_name());
                    intent.putExtra("html_note", bean1.getHtml_note());
                    intent.putExtra("html_picture", bean1.getShare_icon());
                } else {
                    intent.putExtra("", bean1.getActivity_name());
                }
                break;
            case 2:                 //广告链接--视频详情
                if (MHStringUtils.isEmpty(advert_info)) return;
                intent.putExtra("objectId", advert_info);
                intent.putExtra("type", "1");
                break;
            case 3:
                if (MHStringUtils.isEmpty(advert_info)) return;
                intent.putExtra("objectId", advert_info);
                intent.putExtra("type", "3");
                break;
            case 4:
                if (MHStringUtils.isEmpty(advert_info)) return;
                intent.putExtra("userId", advert_info);
                intent.putExtra("type", "ad_personal_page");
                break;
            case 5:
                if (MHStringUtils.isEmpty(advert_info) || MHStringUtils.isEmpty(advert_info_extra))
                    return;
                intent.putExtra("type", "2");
                intent.putExtra("activity_uri", advert_info);
                BannerSubjectBean bean = new BannerSubjectBean();
                if (!MHStringUtils.isEmpty(advert_info_extra)) {
                    bean = gson.fromJson(advert_info_extra, BannerSubjectBean.class);
                    intent.putExtra("activity_name", bean.getActivity_name());
                    intent.putExtra("activity_note", bean.getActivity_note());
                    intent.putExtra("activity_picture", bean.getShare_icon());
                } else {
                    intent.putExtra("", bean.getActivity_name());
                }
                break;
            case 6:                 //广告链接--图片详情
                if (MHStringUtils.isEmpty(advert_info))
                    return;
                intent.putExtra("type", "6");
                intent.putExtra("objectId", advert_info);
                break;
        }
        if (advert_type != 0) {//advert_type不为0时，可跳转指定界面，并提前结束此界面
            startActivity(intent);
            finish();
//            overridePendingTransition(R.anim.slide_right_out, R.anim.slide_left_in);
        }
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        isLeft = false;
        openThreadTask(theTimeLeft, 1000);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //FIXME 生成深度链接
//    public void generatedLinkME(){
///**创建深度链接*/
//        //深度链接属性设置
//        final LinkProperties properties = new LinkProperties();
//        //渠道
//        properties.setChannel("");  //微信、微博、QQ
//        //功能
//        properties.setFeature("Share");
//        //标签
//        properties.addTag("LinkedME");
//        properties.addTag("Demo");
//        //阶段
//        properties.setStage("Live");
//        //自定义参数,用于在深度链接跳转后获取该数据
//        properties.addControlParameter("LinkedME", "Demo");
//        properties.addControlParameter("View", "Demo");
//        LMUniversalObject universalObject = new LMUniversalObject();
//        universalObject.setTitle("Demo");
//
//        // Async Link creation example
//        universalObject.generateShortUrl(SplashActivity.this, properties, new LMLinkCreateListener() {
//            @Override
//            public void onLinkCreate(String url, LMError error) {
//                MHLogUtil.i("LinkME", url);
//                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                manager.setPrimaryClip(ClipData.newPlainText("Uri", url));
//            }
//        });
//    }
}
