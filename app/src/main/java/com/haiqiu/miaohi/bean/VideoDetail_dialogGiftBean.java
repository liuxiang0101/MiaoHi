package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.ConstantsValue;

/**
 * 礼物对话框实体类
 * Created by ningl on 2016/6/23.
 */
public class VideoDetail_dialogGiftBean {


    /**
     * sender_attention_state : true
     * sender_portrait_state : 10
     * sender_note : 用户说明测试
     * gift_icon_state : 10
     * send_gift_time : 2016-06-23 22:28:25.0
     * sender_type : 20000
     * gift_id : GIFT-c66d091a-047b-11e6-aa2d-002522d24f4b
     * sender_portrait_uri : http://7xt99c.com1.z0.glb.clouddn.com/iconImage9C46439D85D54A2C8A9750D211C3F7E3_2016_05_30_04_12_28_91
     * sender_fans_count : 3
     * sender_name : 好咯
     * gift_type : 20
     * gift_state : 10
     * sender_id : USER-db269cfe-263d-11e6-83a3-44a8424640fa
     * video_cover_state : 10
     * gift_icon_role : default
     * video_cover_uri : http://7xt99g.com1.z0.glb.clouddn.com/img_379a78ffe134ace11f0b9888985a3182_2016_05_30_20_34_54_401
     * video_uri : http://7xt99j.com1.z0.glb.clouddn.com/video_379a78ffe134ace11f0b9888985a3182_2016_05_30_20_34_54_403
     * video_hls_state : 8
     * gift_name : 球靴
     * video_state : 10
     * gift_icon_uri : http://o71qpn3yk.bkt.clouddn.com/gift_normal_shoes_default.png
     * video_id : VIDE-a6ded530-2662-11e6-83a3-44a8424640fa
     */

    private boolean sender_attention_state;
    private String sender_portrait_state;
    private String sender_note;
    private String gift_icon_state;
    private String send_gift_time;
    private String sender_type;
    private String gift_id;
    private String sender_portrait_uri;
    private String sender_fans_count;
    private String sender_name;
    private String gift_type;
    private String gift_state;
    private String sender_id;
    private String video_cover_state;
    private String gift_icon_role;
    private String video_cover_uri;
    private String video_uri;
    private String video_hls_state;
    private String gift_name;
    private String video_state;
    private String gift_icon_uri;
    private String video_id;

    public boolean isSender_attention_state() {
        return sender_attention_state;
    }

    public void setSender_attention_state(boolean sender_attention_state) {
        this.sender_attention_state = sender_attention_state;
    }

    public String getSender_portrait_state() {
        return sender_portrait_state;
    }

    public void setSender_portrait_state(String sender_portrait_state) {
        this.sender_portrait_state = sender_portrait_state;
    }

    public String getSender_note() {
        return sender_note;
    }

    public void setSender_note(String sender_note) {
        this.sender_note = sender_note;
    }

    public String getGift_icon_state() {
        return gift_icon_state;
    }

    public void setGift_icon_state(String gift_icon_state) {
        this.gift_icon_state = gift_icon_state;
    }

    public String getSend_gift_time() {
        return send_gift_time;
    }

    public void setSend_gift_time(String send_gift_time) {
        this.send_gift_time = send_gift_time;
    }

    public String getSender_type() {
        return sender_type;
    }

    public void setSender_type(String sender_type) {
        this.sender_type = sender_type;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getSender_portrait_uri() {
        return sender_portrait_uri+ ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public void setSender_portrait_uri(String sender_portrait_uri) {
        this.sender_portrait_uri = sender_portrait_uri;
    }

    public String getSender_fans_count() {
        return sender_fans_count;
    }

    public void setSender_fans_count(String sender_fans_count) {
        this.sender_fans_count = sender_fans_count;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getGift_type() {
        return gift_type;
    }

    public void setGift_type(String gift_type) {
        this.gift_type = gift_type;
    }

    public String getGift_state() {
        return gift_state;
    }

    public void setGift_state(String gift_state) {
        this.gift_state = gift_state;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getVideo_cover_state() {
        return video_cover_state;
    }

    public void setVideo_cover_state(String video_cover_state) {
        this.video_cover_state = video_cover_state;
    }

    public String getGift_icon_role() {
        return gift_icon_role;
    }

    public void setGift_icon_role(String gift_icon_role) {
        this.gift_icon_role = gift_icon_role;
    }

    public String getVideo_cover_uri() {
        if (null != video_cover_uri && video_cover_uri.contains("?")) {
            return video_cover_uri + ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM_FRAME;
        }
        return video_cover_uri+ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM;
    }

    public void setVideo_cover_uri(String video_cover_uri) {
        this.video_cover_uri = video_cover_uri;
    }

    public String getVideo_uri() {
        return video_uri;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
    }

    public String getVideo_hls_state() {
        return video_hls_state;
    }

    public void setVideo_hls_state(String video_hls_state) {
        this.video_hls_state = video_hls_state;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public String getVideo_state() {
        return video_state;
    }

    public void setVideo_state(String video_state) {
        this.video_state = video_state;
    }

    public String getGift_icon_uri() {
        return gift_icon_uri;
    }

    public void setGift_icon_uri(String gift_icon_uri) {
        this.gift_icon_uri = gift_icon_uri;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}
