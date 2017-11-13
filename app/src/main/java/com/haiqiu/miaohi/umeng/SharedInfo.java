package com.haiqiu.miaohi.umeng;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 分享的信息
 * Created by ningl on 16/12/12.
 */
public class SharedInfo {

    private String imgPath;
    private SHARE_MEDIA share_media;

    public SHARE_MEDIA getShare_media() {
        return share_media;
    }

    public void setShare_media(SHARE_MEDIA share_media) {
        this.share_media = share_media;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
