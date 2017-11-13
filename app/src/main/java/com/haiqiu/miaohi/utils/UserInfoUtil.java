package com.haiqiu.miaohi.utils;

import android.content.Context;
import android.text.TextUtils;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.MHApplication;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.LoginBean;
import com.haiqiu.miaohi.db.SQLiteHelper;
import com.haiqiu.miaohi.db.UserInfoManager;

/**
 * 用户信息处理
 * Created by ningl on 2016/5/25.
 */
public class UserInfoUtil {

    /**
     * 从sharedPreference取用户信息
     */
    public static String getUserInfoFromSp(Context context, String key) {
        return (String) SpUtils.get(key, "");
    }

    /**
     * 保存登陆用户信息
     *
     * @param loginBean 用户信息
     */
    public static void saveUserInfo(Context context, LoginBean loginBean) {
        if (null == loginBean)
            return;
        // 秒嗨token
        MHApplication.mMiaohiToken = loginBean.getMiaohi_token();
        SpUtils.put(ConstantsValue.Sp.TOKEN_MIAOHI, loginBean.getMiaohi_token());

        //秒嗨用户类型
        SpUtils.put(ConstantsValue.Sp.USER_TYPE, loginBean.getUser_type());
        //用户id
        SpUtils.put(ConstantsValue.Sp.USER_ID, loginBean.getUser_id());

        //用户昵称
        SpUtils.put(ConstantsValue.Sp.USER_NAME, loginBean.getUser_name());

        //用户头像
        SpUtils.put(ConstantsValue.Sp.PORTRAIT_URI, loginBean.getPortrait_uri());

        //用户是否开通映答
        SpUtils.put(ConstantsValue.Sp.ANSER_DROIT, loginBean.isAnswer_auth() ? "1" : "0");

        //用户是否选择过运动标签
        SpUtils.put(ConstantsValue.Sp.USER_LABEL_SELECTED, loginBean.isLabel_selected());

        // 七牛上传头像图片token
        SpUtils.put(ConstantsValue.Sp.TOKEN_QINIU_UPLOAD_ICON, loginBean.getQiniu_upload_icon_token());

        // 七牛上传普通头像token
        SpUtils.put(ConstantsValue.Sp.TOKEN_QINIU_UPLOAD_IMAGE, loginBean.getQiniu_upload_image_token());
        // 七牛上传视频token
        SpUtils.put(ConstantsValue.Sp.TOKEN_QINIU_UPLOAD_VIDEO, loginBean.getQiniu_upload_video_token());

        // 融云token
        SpUtils.put(ConstantsValue.Sp.RONG_TOKEN, loginBean.getRong_token());

        //七牛用户图片bucket
        SpUtils.put(ConstantsValue.Sp.QINIU_WEB_ICON_BASE, loginBean.getQiniu_web_icon_base());

        //七牛视频缩略图bucket
        SpUtils.put(ConstantsValue.Sp.QINIU_WEB_IMAGE_BASE, loginBean.getQiniu_web_image_base());

        //七牛视频bucket
        SpUtils.put(ConstantsValue.Sp.QINIU_WEB_VIDEO_BASE, loginBean.getQiniu_web_video_base());

        //切换数据库
        SQLiteHelper.switchDB();

        //同步用户数据
        UserInfoManager.syncData(context.getApplicationContext());

        //切换草稿箱
        DraftUtil.init(context.getApplicationContext());

        //清除状态
        MHStateSyncUtil.clearState();
        MHContentSyncUtil.clearContent();
    }

    /**
     * 保存登陆用户信息
     *
     * @param loginBean 用户信息
     */
    public static void saveUploadToken(Context context, LoginBean loginBean) {
        if (null == loginBean)
            return;

        //七牛用户图片bucket
        SpUtils.put(ConstantsValue.Sp.QINIU_WEB_ICON_BASE, loginBean.getQiniu_web_icon_base());

        //七牛视频缩略图bucket
        SpUtils.put(ConstantsValue.Sp.QINIU_WEB_IMAGE_BASE, loginBean.getQiniu_web_image_base());

        //七牛视频bucket
        SpUtils.put(ConstantsValue.Sp.QINIU_WEB_VIDEO_BASE, loginBean.getQiniu_web_video_base());

        // 七牛上传头像图片token
        SpUtils.put(ConstantsValue.Sp.TOKEN_QINIU_UPLOAD_ICON, loginBean.getQiniu_upload_icon_token());

        // 七牛上传普通头像token
        SpUtils.put(ConstantsValue.Sp.TOKEN_QINIU_UPLOAD_IMAGE, loginBean.getQiniu_upload_image_token());

        // 七牛上传视频token
        SpUtils.put(ConstantsValue.Sp.TOKEN_QINIU_UPLOAD_VIDEO, loginBean.getQiniu_upload_video_token());

    }


    /**
     * 退出登录的时候调用这个方法
     * 不能直接调用:SpUtils.clear();
     */
    public static void logout() {
        MHApplication.mMiaohiToken = null;
        SpUtils.remove(ConstantsValue.Sp.TOKEN_MIAOHI);
        SpUtils.remove(ConstantsValue.Sp.USER_TYPE);
        SpUtils.remove(ConstantsValue.Sp.USER_NAME);
        SpUtils.remove(ConstantsValue.Sp.USER_ID);
        SpUtils.remove(ConstantsValue.Sp.TOKEN_QINIU_UPLOAD_ICON);
        SpUtils.remove(ConstantsValue.Sp.TOKEN_QINIU_UPLOAD_IMAGE);
        SpUtils.remove(ConstantsValue.Sp.TOKEN_QINIU_UPLOAD_VIDEO);
        SpUtils.remove(ConstantsValue.Sp.RONG_TOKEN);
        SpUtils.remove(ConstantsValue.Sp.QINIU_WEB_ICON_BASE);
        SpUtils.remove(ConstantsValue.Sp.QINIU_WEB_IMAGE_BASE);
        SpUtils.remove(ConstantsValue.Sp.QINIU_WEB_VIDEO_BASE);
    }

    /**
     * 获取登陆用户id
     */
    public static String getUserId(Context context) {
        return getUserInfoFromSp(context, ConstantsValue.Sp.USER_ID);
    }

    /**
     * 获取登陆用户id
     */
    public static String getUserName(Context context) {
        return getUserInfoFromSp(context, ConstantsValue.Sp.USER_NAME);
    }

    /**
     * 获取用户类型
     *
     * @param context
     * @return
     */
    public static String getUserType(Context context) {
        return getUserInfoFromSp(context, ConstantsValue.Sp.USER_TYPE);
    }

    /**
     * 获取用户类型
     *
     * @param context
     * @return
     */
    public static int getUserType1(Context context) {
        String type = getUserInfoFromSp(context, ConstantsValue.Sp.USER_TYPE);
        if (MHStringUtils.isEmpty(type)) {
            return 0;
        } else {
            return Integer.valueOf(type);
        }
    }

    /**
     * 获取用户映答权限
     */
    public static String getUserANSER_DROIT(Context context) {
        return getUserInfoFromSp(context, ConstantsValue.Sp.ANSER_DROIT);
    }

    /**
     * 获取用户头像
     *
     * @param context
     * @return
     */
    public static String getUserHeader(Context context) {
        return SpUtils.getString(ConstantsValue.Sp.PORTRAIT_URI);
    }

    /**
     * 是否有应答权限
     *
     * @return
     */
    public static boolean isAnswerDoit(Context context) {
        try {
            String answerDroit = getUserANSER_DROIT(context);
            return TextUtils.equals("1", answerDroit) ? true : false;
        } catch (Exception e) {
            MHLogUtil.e("isAnswerDoit",e);
            return false;
        }
    }

    /**
     * 是否是大咖
     *
     * @param context
     * @return
     */
    public static boolean isVip(Context context) {
        if (MHStringUtils.isEmpty(getUserType(context)))
            return false;
        boolean isVip = Integer.parseInt(getUserType(context)) > 10 ? true : false;
        return isVip;
    }

    /**
     * 是否登录
     */
    public static boolean isLogin() {
        return !MHStringUtils.isEmpty(SpUtils.getString(ConstantsValue.Sp.TOKEN_MIAOHI));
    }


    /**
     * 跳转到登录
     */
    public static void goLogin(Context context) {
//        Intent intent = new Intent(context, LoginActivity.class);
//        context.startActivity(intent);
        ((BaseActivity) context).isLogin(false);
    }

    public static boolean isMyself(Context context, String userid) {
        if (userid == null) return false;
        if (getUserId(context) == null) return false;
        return TextUtils.equals(userid, getUserId(context));
    }


}
