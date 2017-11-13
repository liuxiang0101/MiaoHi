package com.haiqiu.miaohi.umeng;

import android.app.Activity;
import android.text.TextUtils;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 分享视频
 * Created by ningl on 2016/8/11.
 */
public class ShareVideo extends AbsUMShare {

    private String mContent;

    public ShareVideo(Activity activity, String shareId, String getImgUrl, String shareUserName, String userId, String describe, String mContent) {
        super(activity, shareId, getImgUrl, shareUserName, userId, describe);
        if (MHStringUtils.isEmpty(mContent)) {
            this.mContent = "";
        } else {
            this.mContent = mContent;
        }
    }

    @Override
    public String getDescribe(SHARE_MEDIA platform) {
        String content = "";
        switch (platform) {
            case SINA:
                String string = mContent + describe;
                if (string.length() > 100) string = string.substring(0, 100) + "...";
                content = string + " 来自" + shareUserName + "的秒嗨视频。" + ConstantsValue.Shared.SHARED_TITLE;
                break;

            case WEIXIN:
            case QQ:
            case QZONE:
                content = "来自" + shareUserName + "的秒嗨。" + ConstantsValue.Shared.SHARED_TITLE;
                break;

            case WEIXIN_CIRCLE:
//                content = "来自"+shareUserName+"的秒嗨视频:"+describe;
                content = describe + "  来自" + shareUserName + "的秒嗨视频";
                break;
        }
        return content;
    }

    @Override
    public String getTitle(SHARE_MEDIA platform) {
        String title = "";
        switch (platform) {
            case WEIXIN_CIRCLE:
            case QZONE:
                title = describe;
                break;

            case WEIXIN:
            case QQ:
                title = describe;
                break;

        }
        return title;
    }

    @Override
    public boolean isHasDetete() {
        if (isMySelf()) return true;
        return false;
    }

    @Override
    public boolean isHasReport() {
        return true;
    }

    @Override
    public boolean isHasCopyLink() {
        return true;
    }

    @Override
    public boolean isHasCancleBtn() {
        return true;
    }

    @Override
    public boolean isMySelf() {
        return TextUtils.equals(userId, UserInfoUtil.getUserId(activity));
    }

    @Override
    public String getHost() {
        return ConstantsValue.Shared.SHARED_VIDEO_BASEURL;
    }

}
