package com.haiqiu.miaohi.umeng;

import android.app.Activity;

import com.haiqiu.miaohi.ConstantsValue;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 分享问答
 * Created by ning on 2016/8/11.
 */
public class ShareQA extends AbsUMShare {

    public ShareQA(Activity activity, String shareId, String imgUrl, String shareUserName, String userId, String describe) {
        super(activity, shareId, imgUrl, shareUserName, userId, describe);
    }

    @Override
    public String getDescribe(SHARE_MEDIA platform) {
        String content = "";
        switch (platform){
            case SINA:
//                content = "有人问"+shareUserName+","+describe+" 他会怎么回答呢?快来围观——";
                if(describe.length()>100)describe=describe.substring(0,100)+"...";
                content = describe+" 想看"+shareUserName+"的回答，速来秒嗨围观!";
                break;

            case WEIXIN:
            case QQ:
            case QZONE:
                content = "来自"+shareUserName+"的秒嗨映答,"+"体坛的大咖都在这!";
                break;

            case WEIXIN_CIRCLE:
                content = shareUserName+"正在秒嗨映答:"+describe;
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
        return ConstantsValue.Shared.SHARED_QADETAIL_BASEURL;
    }
}
