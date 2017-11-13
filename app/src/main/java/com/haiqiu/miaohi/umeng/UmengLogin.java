package com.haiqiu.miaohi.umeng;

import android.app.Activity;
import android.widget.Toast;

import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.UmengLoginInfo;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.umeng.socialize.Config;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

import java.util.Map;

/**
 * umeng登陆工具类
 * Created by ningl on 2016/5/16.
 */
public class UmengLogin {


    /**
     * 微信登陆
     *
     * @param activity
     * @param iUMLoginCallback
     */
    public static void loginWX(final Activity activity, final UMShareAPI umShareAPI, final IUMLoginCallback iUMLoginCallback) {
        final SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
        umShareAPI.doOauthVerify(activity, platform, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                getUserInfo(activity, umShareAPI, platform, iUMLoginCallback);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Toast.makeText(activity, "微信授权失败", Toast.LENGTH_SHORT).show();
                ((BaseActivity) activity).hiddenLoadingView();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Toast.makeText(activity, "取消授权", Toast.LENGTH_SHORT).show();
                ((BaseActivity) activity).hiddenLoadingView();
            }
        });
    }

    /**
     * QQ登陆
     *
     * @param activity
     * @param umShareAPI
     * @param iumLoginCallback
     */
    public static void loginQQ(final Activity activity, final UMShareAPI umShareAPI, final IUMLoginCallback iumLoginCallback) {
        final SHARE_MEDIA platform = SHARE_MEDIA.QQ;
        umShareAPI.doOauthVerify(activity, platform, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//                Toast.makeText(activity, "授权成功", Toast.LENGTH_SHORT).show();
//                iumLoginCallback.callbackInfo(platform, map, map.get("uid"), map.get("unionid"), map.get("nickname"),map.get("headimgurl"));
                getUserInfo(activity, umShareAPI, platform, iumLoginCallback);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Toast.makeText(activity, "QQ授权失败", Toast.LENGTH_SHORT).show();
                ((BaseActivity) activity).hiddenLoadingView();
            }

            @Override

            public void onCancel(SHARE_MEDIA share_media, int i) {
                Toast.makeText(activity, "取消授权", Toast.LENGTH_SHORT).show();
                ((BaseActivity) activity).hiddenLoadingView();
            }
        });
    }

    /**
     * sina登陆
     *
     * @param activity
     * @param umShareAPI
     * @param iumLoginCallback
     */
    public static void loginSina(final Activity activity, final UMShareAPI umShareAPI, final IUMLoginCallback iumLoginCallback) {
        final SHARE_MEDIA platform = SHARE_MEDIA.SINA;
        umShareAPI.doOauthVerify(activity, platform, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//                Toast.makeText(activity, "授权成功", Toast.LENGTH_SHORT).show();
//                iumLoginCallback.callbackInfo(platform, map, map.get("uid"), map.get("unionid"), map.get("nickname"),map.get("headimgurl"));
                getUserInfo(activity, umShareAPI, platform, iumLoginCallback);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Toast.makeText(activity, "微博授权失败", Toast.LENGTH_SHORT).show();
                ((BaseActivity) activity).hiddenLoadingView();
            }

            @Override

            public void onCancel(SHARE_MEDIA share_media, int i) {
                Toast.makeText(activity, "取消授权", Toast.LENGTH_SHORT).show();
                ((BaseActivity) activity).hiddenLoadingView();
            }
        });
    }


    /**
     * 获取平台用户信息
     *
     * @param activity
     * @param umShareAPI
     * @param platform
     */
    public static void getUserInfo(final Activity activity, final UMShareAPI umShareAPI, final SHARE_MEDIA platform, final IUMLoginCallback iUMLoginCallback) {

        umShareAPI.getPlatformInfo(activity, platform, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Map<String, String> iMap = map;
                umShareAPI.deleteOauth(activity, platform, null);
                if(null == map||null == iUMLoginCallback||null == umShareAPI||null == platform) {
                    Toast.makeText(activity, "获取平台用户信息失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                UmengLoginInfo loginInfo = new UmengLoginInfo();
                loginInfo.setShare_media(share_media);
                loginInfo.setMap(map);
                if (platform == SHARE_MEDIA.WEIXIN) {
                    loginInfo.setuId(map.get("uid"));
                    loginInfo.setThreeId(map.get("uid"));
                    loginInfo.setNickname(map.get("name"));
                    loginInfo.setHeaderUrl(map.get("iconurl"));
//                    iUMLoginCallback.callbackInfo(share_media, map, map.get("uid"), map.get("unionid"), map.get("nickname"), map.get("headimgurl"));
                    iUMLoginCallback.callBackInfo(loginInfo);
                } else if (platform == SHARE_MEDIA.QQ) {
                    loginInfo.setuId(map.get("uid"));
                    loginInfo.setThreeId(map.get("uid"));
                    loginInfo.setNickname(map.get("name"));
                    loginInfo.setHeaderUrl(map.get("iconurl"));
//                    iUMLoginCallback.callbackInfo(share_media, map, map.get("uid"), map.get("openid"), map.get("screen_name"), map.get("profile_image_url"));
                    iUMLoginCallback.callBackInfo(loginInfo);
                } else if (platform == SHARE_MEDIA.SINA) {
//                    String result = map.get("result");
                    try {
//                        JSONObject obj = new JSONObject(result);
                        loginInfo.setuId(map.get("uid"));
                        loginInfo.setThreeId(map.get("uid"));
                        loginInfo.setNickname(map.get("name"));
                        loginInfo.setHeaderUrl(map.get("iconurl"));
//                        iUMLoginCallback.callbackInfo(share_media, map, obj.getString("id"), obj.getString("id"), obj.getString("name"), obj.getString("profile_image_url"));
                        iUMLoginCallback.callBackInfo(loginInfo);
                    } catch (Exception e) {
                        MHLogUtil.e("getUserInfo",e);
                    }
                }

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                umShareAPI.deleteOauth(activity, platform, null);
                ((BaseActivity) activity).hiddenLoadingView();
                Toast.makeText(activity, "获取平台用户信息失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                umShareAPI.deleteOauth(activity, platform, null);
                ((BaseActivity) activity).hiddenLoadingView();
                Toast.makeText(activity, "取消操作", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 判断微信是否安装
     *
     * @param activity
     * @return
     */
    public static boolean isWeixinAvilible(Activity activity) {
//        final PackageManager packageManager = activity.getPackageManager();// 获取packagemanager
//        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
//        if (pinfo != null) {
//            for (int i = 0; i < pinfo.size(); i++) {
//                String pn = pinfo.get(i).packageName;
//                if (pn.equals("com.tencent.mm")) {
//                    return true;
//                }
//            }
//        }
//        return false;
        //关闭umeng toast
        Log.LOG = false;
        Config.IsToastTip = false;
        UMShareAPI umShareAPI = UMShareAPI.get(activity);
        return umShareAPI.isInstall(activity, SHARE_MEDIA.WEIXIN);
    }

    /**
     * 判断qq是否可用
     *
     * @param activity
     * @return
     */
    public static boolean isQQClientAvailable(Activity activity) {
//        final PackageManager packageManager = activity.getPackageManager();
//        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
//        if (pinfo != null) {
//            for (int i = 0; i < pinfo.size(); i++) {
//                String pn = pinfo.get(i).packageName;
//                if (pn.equals("com.tencent.mobileqq")) {
//                    return true;
//                }
//            }
//        }
//        return false;
        UMShareAPI umShareAPI = UMShareAPI.get(activity);
        return umShareAPI.isInstall(activity, SHARE_MEDIA.QQ);
    }


}
