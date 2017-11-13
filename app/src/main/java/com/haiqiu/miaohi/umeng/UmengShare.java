package com.haiqiu.miaohi.umeng;

import android.app.Activity;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ToastUtils;
import com.haiqiu.miaohi.widget.ShareLayout;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;

/**
 * umeng分享类
 * Created by ningl on 2016/5/16.
 */
public class UmengShare {

    private static ShareLayout.ShareRsultListener shareRsultListener;

    /**
     * 分享到微信
     */
//    public static void sharedWX(Activity activity, String title, String url, int drawable){
//        new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN)
//                .setCallback(new ShareListener(activity))
//                .withText(title)
//                .withTargetUrl(url)
//                .withMedia(new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), drawable)))
//                .share();
//    }
    public static void sharedWX(Activity activity, String title, String content, String url, String imgUrl, ShareLayout.ShareRsultListener shareRsultListener) {
        if (MHStringUtils.isEmpty(imgUrl)) imgUrl = ConstantsValue.Shared.ANDROID_DEFAULT_SHARE_IMAGE;
        new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(new ShareListener(activity, shareRsultListener))
                .withText(content)
                .withTitle(title)
                .withTargetUrl(url)
                .withMedia(new UMImage(activity, imgUrl))
                .share();
    }

    public static void sharedWXCirle(Activity activity, String title, String url, String imgUrl, ShareLayout.ShareRsultListener shareRsultListener) {
        if (MHStringUtils.isEmpty(imgUrl)) imgUrl = ConstantsValue.Shared.ANDROID_DEFAULT_SHARE_IMAGE;
        new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(new ShareListener(activity, shareRsultListener))
                .withTargetUrl(url)
                .withTitle(title)
                .withText(title)
//                .withFollow(title)
                .withMedia(new UMImage(activity, imgUrl))
                .share();
    }

    /**
     * 分享到QQ
     */
    public static void sharedQQ(Activity activity, String title, String content, String url, String imgUrl, ShareLayout.ShareRsultListener shareRsultListener) {
        if (MHStringUtils.isEmpty(imgUrl)) imgUrl = ConstantsValue.Shared.ANDROID_DEFAULT_SHARE_IMAGE;
        new ShareAction(activity).setPlatform(SHARE_MEDIA.QQ)
                .setCallback(new ShareListener(activity, shareRsultListener))
                .withText(content)
                .withTargetUrl(url)
                .withTitle(title)
                .withMedia(new UMImage(activity, imgUrl))
                .share();
    }

    /**
     * 分享到QQ空间
     */
    public static void sharedQZone(Activity activity, String title, String describe, String url, String imgUrl, ShareLayout.ShareRsultListener shareRsultListener) {
        if (MHStringUtils.isEmpty(imgUrl)) imgUrl = ConstantsValue.Shared.ANDROID_DEFAULT_SHARE_IMAGE;
        new ShareAction(activity).setPlatform(SHARE_MEDIA.QZONE)
                .setCallback(new ShareListener(activity, shareRsultListener))
                .withText(describe)
                .withTitle(title)
                .withTargetUrl(url)
                .withMedia(new UMImage(activity, imgUrl))
                .share();
    }

    public static void sharedSina(Activity activity, String content, String url, String imgUrl, ShareLayout.ShareRsultListener shareRsultListener) {
        if (MHStringUtils.isEmpty(imgUrl)) imgUrl = ConstantsValue.Shared.ANDROID_DEFAULT_SHARE_IMAGE;
        new ShareAction(activity).setPlatform(SHARE_MEDIA.SINA)
                .setCallback(new ShareListener(activity, shareRsultListener))
                .withText(content)
                .withTargetUrl(url)
                .withMedia(new UMImage(activity, imgUrl))
                .share();
    }

    public static class ShareListener implements UMShareListener {

        private Activity activity;
        private ShareLayout.ShareRsultListener shareRsultListener;

        public ShareListener(Activity activity, ShareLayout.ShareRsultListener shareRsultListener) {
            this.activity = activity;
            this.shareRsultListener = shareRsultListener;
        }
        public ShareListener(Activity activity) {
            this.activity = activity;
        }



        @Override
        public void onResult(SHARE_MEDIA share_media) {
            ToastUtils.showToastAtCenter(activity, "分享成功");
            if (shareRsultListener != null) shareRsultListener.success();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            ToastUtils.showToastAtCenter(activity, "分享失败");
            TCAgent.onEvent(activity,"分享失败"+ConstantsValue.android);
            if (shareRsultListener != null) shareRsultListener.fail();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            ToastUtils.showToastAtCenter(activity, "分享取消");
        }
    }

    /**
     * 分享
     * @param umShare 分享数据
     * @param share_media 分享平台
     */
    public static void share(Activity activity, AbsUMShare umShare, SHARE_MEDIA share_media, ShareLayout.ShareRsultListener shareRsultListener){
        switch (share_media) {
            case SINA://新浪微博分享
                UmengShare.sharedSina(activity, umShare.getDescribe(SHARE_MEDIA.SINA), umShare.getShareLink(), umShare.imgUrl,shareRsultListener);
                break;
            case QQ://qq分享
                UmengShare.sharedQQ(activity, umShare.getTitle(SHARE_MEDIA.QQ), umShare.getDescribe(SHARE_MEDIA.QQ), umShare.getShareLink(), umShare.imgUrl, shareRsultListener);
                break;
            case QZONE://QQ空间分享
                UmengShare.sharedQZone(activity, umShare.getTitle(SHARE_MEDIA.QZONE), umShare.getDescribe(SHARE_MEDIA.QZONE), umShare.getShareLink(), umShare.imgUrl, shareRsultListener);
                break;
            case WEIXIN://微信分享
                UmengShare.sharedWX(activity, umShare.getTitle(SHARE_MEDIA.WEIXIN), umShare.getDescribe(SHARE_MEDIA.WEIXIN), umShare.getShareLink(), umShare.imgUrl, shareRsultListener);
                break;
            case WEIXIN_CIRCLE://朋友圈分享
                UmengShare.sharedWXCirle(activity, umShare.getDescribe(SHARE_MEDIA.WEIXIN_CIRCLE), umShare.getShareLink(), umShare.imgUrl, shareRsultListener);
                break;
//            case SHARE_COPYLINK://复制链接
//                ClipboardManager cmb = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
//                cmb.setText(umShare.getShareLink());
//                ToastUtils.showToastAtCenter(context, "已经复制到剪切板");
//                if (shareRsultListener != null) shareRsultListener.reportSuccess();
//                break;
//            case SHARE_REPORTVIDEO://举报视频
//                reportVideo();
//                break;
        }
    }

    /**
     * 只分享图片
     * @param activity
     * @param share_media
     * @param imgPath
     */
    public static void sharedIMG(final Activity activity, SHARE_MEDIA share_media, final Object imgPath, String link, String text, UMShareListener umShareListener){
        UMImage image = null;
        if(imgPath instanceof String){
            image = new UMImage(activity, new File((String)imgPath));
        } else if(imgPath instanceof Integer){
            image = new UMImage(activity, (int)imgPath);
        } else {
            new Throwable("no found the type");
        }
        final ShareAction action = new ShareAction(activity);
        action.setCallback(umShareListener);
        action.setPlatform(share_media);
        switch (share_media){
            case WEIXIN:
            case WEIXIN_CIRCLE:
                image.compressStyle = UMImage.CompressStyle.SCALE;
                action.withMedia(image)
                        .share();
                break;
            case QZONE:
                action.withMedia(image)
                        .withTitle("秒嗨·敢想敢放肆")
                        .withText(text)
                        .withTargetUrl(link)
                        .share();
                break;
            case SINA:
                image.setThumb(image);
                action.withMedia(image)
                        .withText(text)
                        .withTargetUrl(link)
                        .share();
                break;
            case QQ:
                action.withMedia(image)
                .share();
                break;
        }
    }

}
