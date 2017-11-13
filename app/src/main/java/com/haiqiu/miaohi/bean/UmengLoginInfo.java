package com.haiqiu.miaohi.bean;

import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * 友盟登录返回信息
 * Created by ningl on 16/10/12.
 */
public class UmengLoginInfo {

    private SHARE_MEDIA share_media;
    private Map<String, String> map;
    private String uId;
    private String threeId;
    private String nickname;
    private String headerUrl;

    public SHARE_MEDIA getShare_media() {
        return share_media;
    }

    public void setShare_media(SHARE_MEDIA share_media) {
        this.share_media = share_media;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getThreeId() {
        return threeId;
    }

    public void setThreeId(String threeId) {
        this.threeId = threeId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }
}
