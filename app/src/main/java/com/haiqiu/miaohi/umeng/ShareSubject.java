package com.haiqiu.miaohi.umeng;

import android.app.Activity;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by ningl on 2016/8/11.
 */
public class ShareSubject extends AbsUMShare {
    private String subjectName;
    private String activeDescribe;
    private String share_link_address;

    public ShareSubject(Activity activity, String shareId, String imgUrl, String shareUserName, String userId, String describe, String subjectName) {
        super(activity, shareId, imgUrl, shareUserName, userId, describe);
        this.subjectName = subjectName;
        this.activeDescribe = describe;
    }

    @Override
    public String getDescribe(SHARE_MEDIA platform) {
        String content = "";
        switch (platform) {
            case SINA:
//                content = "秒嗨上有一个有趣的专题——"+subjectName+",来这看看吧——";
                if (activeDescribe.length() > 120)
                    activeDescribe = activeDescribe.substring(0, 120) + "...";

                content = activeDescribe + " 更多内容在秒嗨 ";
                TCAgent.onEvent(activity, subjectName + "-" + "微博点击次数-" + ConstantsValue.android);
                break;

            case WEIXIN:
                content = describe;
                TCAgent.onEvent(activity, subjectName + "-" + "微信点击次数-" + ConstantsValue.android);
                break;
            case QQ:
                content = describe;
                TCAgent.onEvent(activity, subjectName + "-" + "QQ点击次数-" + ConstantsValue.android);
                break;
            case QZONE:
                content = describe;
                TCAgent.onEvent(activity, subjectName + "-" + "QQ空间点击次数-" + ConstantsValue.android);
                break;

            case WEIXIN_CIRCLE:
                content = subjectName + "。" + ConstantsValue.Shared.SHARED_TITLE;
                TCAgent.onEvent(activity, subjectName + "-" + "微信朋友圈点击次数-" + ConstantsValue.android);
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
                title = subjectName;
                break;

            case WEIXIN:
            case QQ:
                title = subjectName;
                break;

        }
        return title;
    }

    public void setShare_link_address(String share_link_address) {
        this.share_link_address = share_link_address;
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
    public boolean isMySelf() {
        return super.isMySelf();
    }

    @Override
    public String getHost() {
        if (MHStringUtils.isEmpty(share_link_address))
            return ConstantsValue.Shared.SHARED_SUBJECTDETAIL_BASEURL;
        else
            return share_link_address;
    }

    @Override
    public void setCopyLinkStatistics() {
        TCAgent.onEvent(activity, subjectName + "-" + "复制链接点击次数-" + ConstantsValue.android);
    }
}
