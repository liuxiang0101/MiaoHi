package com.haiqiu.miaohi;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.growingio.android.sdk.collection.Configuration;
import com.growingio.android.sdk.collection.GrowingIO;
import com.haiqiu.miaohi.db.UserInfoManager;
import com.haiqiu.miaohi.ffmpeg.FFmpegUtil;
import com.haiqiu.miaohi.rong.CustomTheMoreTxtMessage;
import com.haiqiu.miaohi.rong.CustomTxtMessage;
import com.haiqiu.miaohi.service.AssertService;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.DraftUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MessageDigestUtils;
import com.haiqiu.miaohi.utils.MiaoHiKeyUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.SysMethod;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.yixia.camera.VCamera;
import com.yixia.camera.util.DeviceUtils;

import java.io.File;

import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.RongIMClient;


public class MHApplication extends MultiDexApplication {
    private static final String TAG = "MHApplication";
    public static String mMiaohiToken;
    public static String versionCode;
    public static String versionName;
    public static String mac;
    public static String imei;
    public static String securityKey;
    public static String securityVector;
    //播放器是否是静音
    public static boolean audioIsSilence;
    public static Context applicationContext;

    /**
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();
        MHLogUtil.d(TAG, "onCreate");
        applicationContext = this;
        SpUtils.init(this);
        MiaoHiKeyUtils.init(this);
        //缓存起来防止每次网络请求都去拿
        versionCode = SysMethod.getVersionCode(this);
        versionName = SysMethod.getVersionName(this);

//        versionCode = "12040";
//        versionName = "1.7.3";

        mac = SysMethod.getMacAddress(this);
        imei = SysMethod.getImei(this);
        MHApplication.mMiaohiToken = SpUtils.getString(ConstantsValue.Sp.TOKEN_MIAOHI);

        String sha = MessageDigestUtils.getStringSHA256(mac).toLowerCase();
        securityKey = sha.substring(0, 16);
        securityVector = sha.substring(sha.length() - 16);
        initImageLoader();
        //初始化umeng平台初始化配置
        umengInit();
        talkingDataInit();
        //        stethoInit();

        //融云初始化必须在这里否则会出问题
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
//            <!-- imlib config begin 测试key:p5tvi9dstuxm4  n19jmcy59upl9 打包的正式key -->
            if (ConstantsValue.SERVER_URL.contains("app") || ConstantsValue.SERVER_URL.contains("apr")) {
                RongIMClient.init(this, MiaoHiKeyUtils.getRongIMKey());
            } else {
                RongIMClient.init(this, "p5tvi9dstuxm4");
            }
            try {
                RongIMClient.registerMessageType(CustomTxtMessage.class);
                RongIMClient.registerMessageType(CustomTheMoreTxtMessage.class);
            } catch (AnnotationNotFoundException e) {

                MHLogUtil.e(TAG, e);
            }
        }


        String processName = CommonUtil.getProcessName();
        if (!TextUtils.isEmpty(processName) && processName.equals(this.getPackageName())) {//判断进程名，保证只有主进程运行
            //主进程初始化逻辑
            FFmpegUtil.init(getApplicationContext());
            initDir();

            MediaScannerConnection.scanFile(this, new String[]{
                            Environment.getExternalStorageDirectory().getAbsolutePath(),
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath(),
                            Environment.getDownloadCacheDirectory().getAbsolutePath()
                    },
                    new String[]{"Video"}, null);
            setUmengDialog();
            boolean hasCopy = SpUtils.getBoolean(ConstantsValue.Sp.COPY_FILE_FLAG, false);
            String string = SpUtils.getString(ConstantsValue.Sp.LAST_VERSION_CODE);
            if (!hasCopy || !versionCode.equals(string))//每个版本更新后都要重新copy
                startService(new Intent(getApplicationContext(), AssertService.class));

            UserInfoManager.syncData(getApplicationContext());
            DraftUtil.init(getApplicationContext());

            //ConstantsValue.BUSINESS   一定要先初始化
            //正式环境GrowingIO
            if (ConstantsValue.SERVER_URL.contains("app")) {
                GrowingIO.startWithConfiguration(this, new Configuration()
                        .setProjectId("baf8dd0bfa32e428")
                        .setURLScheme("growing.6ba2f6c268653524")
                        .useID()
                        .trackAllFragments()
                        .setChannel(ConstantsValue.BUSINESS));
            } else {//测试环境GrowingIO
                GrowingIO.startWithConfiguration(this, new Configuration()
                        .setProjectId("80f565aaff1c665f")
                        .setURLScheme("growing.aa894fcef4836543")
                        .useID()
                        .trackAllFragments()
                        .setChannel(ConstantsValue.BUSINESS));
            }
        }
        //refWatcher = LeakCanary.install(this);
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(new WeakMemoryCache())
                .memoryCacheExtraOptions(720, 720)
                .diskCacheFileCount(1000)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(getDisplayOptions())
                .diskCache(new UnlimitedDiskCache(new File(getCacheDir().getAbsolutePath() + "/ImageCache")))
                // .writeDebugLogs() // Remove for release apps
                .build();
        ImageLoader.getInstance().init(config);
        L.writeLogs(false);

    }

    private DisplayImageOptions getDisplayOptions() {
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.color.color_f1) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.color.color_f1)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.color.color_f1) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                // .delayBeforeLoading(int delayInMillis)//int
                // delayInMillis为你设置的下载前的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                .build();// 构建完成
        return options;
    }

    public void umengInit() {
        PlatformConfig.setWeixin(MiaoHiKeyUtils.getWeiXinAppid(), MiaoHiKeyUtils.getWeiXinAppsecret());
        //        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setQQZone(MiaoHiKeyUtils.getQQAppid(), MiaoHiKeyUtils.getQQAppkey());
        PlatformConfig.setSinaWeibo(MiaoHiKeyUtils.getSinaAppkey(), MiaoHiKeyUtils.getSinaAppkey());
    }

    /**
     * TalkingData初始化,并初始化渠道号
     */
    public void talkingDataInit() {
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
//            String td_app_id = appInfo.metaData.getString("TD_APP_ID");
            ConstantsValue.BUSINESS = appInfo.metaData.getString("TD_CHANNEL_ID");
            MHLogUtil.d(TAG, "td_channel_id=" + ConstantsValue.BUSINESS);

            TCAgent.LOG_ON = ConstantsValue.isDeveloperMode(null);
            TCAgent.init(this, MiaoHiKeyUtils.getTDAppid(), ConstantsValue.BUSINESS);
            TCAgent.setReportUncaughtExceptions(true);
        } catch (Exception e) {

            MHLogUtil.e(TAG, e);
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImageLoader.getInstance().clearMemoryCache();
    }

    private void initDir() {
        if ("x86".equalsIgnoreCase(Build.CPU_ABI)) {
            return;
        }
        // 设置拍摄视频缓存路径
        File dcim = new File(ConstantsValue.Video.VIDEO_TEMP_PATH);
        if (!dcim.exists())
            dcim.mkdirs();
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                VCamera.setVideoCachePath(dcim.getAbsolutePath());
            } else {
                VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/", "/sdcard-ext/"));
            }
        } else {
            VCamera.setVideoCachePath(dcim.getAbsolutePath() + "/temp");
        }
        // 开启log输出,ffmpeg输出到logcat
        VCamera.setDebugMode(ConstantsValue.isDeveloperMode(null));
        // 初始化拍摄SDK，必须
        VCamera.initialize(getApplicationContext());

//        //解压assert里面的文件
//        startService(new Intent(getApplicationContext(), AssertService.class));
    }

    /**
     * 将umeng默认的dialog替换为loadingdialog
     */
    private void setUmengDialog() {
        Config.dialogSwitch = false;
    }

//    public static RefWatcher getRefWatcher(Context context) {
//        MHApplication application = (MHApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }
//
//    private RefWatcher refWatcher;

}