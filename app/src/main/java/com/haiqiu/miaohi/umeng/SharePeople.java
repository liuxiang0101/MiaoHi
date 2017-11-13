package com.haiqiu.miaohi.umeng;

import android.app.Activity;

import com.haiqiu.miaohi.ConstantsValue;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 分享个人主页
 * Created by ningl on 2016/8/11.
 */
public class SharePeople extends AbsUMShare {

    public SharePeople(Activity activity, String shareId, String imgUrl, String shareUserName, String userId, String describe) {
        super(activity, shareId, imgUrl, shareUserName, userId, describe);
    }

    @Override
    public String getDescribe(SHARE_MEDIA platform) {
        String content = "";
        switch (platform){
            case SINA:
                content = "我在秒嗨上发现了一个有意思的人——"+shareUserName+"。他的主页非常精彩，点此围观~";
                break;

            case WEIXIN:
            case QQ:
            case QZONE:
                content = "快来围观吧~"+ConstantsValue.Shared.SHARED_TITLE;
                break;

            case WEIXIN_CIRCLE:
                content = "快来围观"+shareUserName+"的秒嗨主页。";
                break;
        }
        return content;
    }

    @Override
    public String getTitle(SHARE_MEDIA platform) {
        String title = "";
        switch (platform){
            case WEIXIN_CIRCLE:
            case QZONE:
                title = "我在秒嗨上发现了一个有意思的人:"+shareUserName;
                break;

            case WEIXIN:
            case QQ:
                title = "我在秒嗨上发现了一个有意思的人:"+shareUserName;
                break;

        }
        return title;
    }

    @Override
    public boolean isHasDetete() {
        return super.isHasDetete();
    }

    @Override
    public boolean isHasReport() {
        return super.isHasReport();
    }

    @Override
    public boolean isHasCopyLink() {
        return super.isHasCopyLink();
    }

    @Override
    public boolean isHasCancleBtn() {
        return super.isHasCancleBtn();
    }

    @Override
    public String getHost() {
        return ConstantsValue.Shared.SHARED_PERSONALPAGE_BASEURL;
    }

}
