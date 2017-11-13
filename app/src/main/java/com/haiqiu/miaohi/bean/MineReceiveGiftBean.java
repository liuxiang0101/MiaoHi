package com.haiqiu.miaohi.bean;

import java.io.Serializable;

/**
 * Created by ningl on 2016/5/27.
 */
public class MineReceiveGiftBean implements Serializable {


    /**
     * sender_portrait_state : 10
     * gift_icon_state : 10
     * send_gift_time : 2016-08-25 18:16:24.0
     * sender_type : 10
     * gift_id : GIFT-92e1e4c9-e782-4855-b674-1d62ebb97ce2
     * sender_portrait_uri : http://icon.miaohi.com/myIcon_20160715144815
     * sender_name : 鎏麽
     * upload_user_name : 鎏麽
     * sender_id : USER-d5d5d647-4a57-11e6-ae3b-5cb9018949c8
     * video_cover_state : 10
     * gift_icon_role : default
     * video_cover_uri : http://image.miaohi.com/img_e698b866f5fc54dced4ac52bfa8c8530_2016_08_19_15_29_07_012
     * video_uri : http://video.miaohi.com/video_e698b866f5fc54dced4ac52bfa8c8530_2016_08_19_15_29_07_020_EXWM
     * video_hls_state : 8
     * gift_name : 么么哒
     * video_state : 10
     * gift_icon_uri : http://res.miaohi.com/gift_default_2016_8_15_15_31_59_784
     * video_id : VIDE-a2513749-65de-11e6-aeeb-70106fac13fa
     */

    private String sender_portrait_state;
    private String gift_icon_state;
    private String send_gift_time;
    private String sender_type;
    private String gift_id;
    private String sender_portrait_uri;
    private String sender_name;
    private String upload_user_name;
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
    private String send_gift_time_text;

    public String getSend_gitf_time_text() {
        return send_gift_time_text;
    }



    public String getSender_type() {
        return sender_type;
    }

    public String getSender_portrait_uri() {
        return sender_portrait_uri;
    }


    public String getSender_name() {
        return sender_name;
    }


    public String getSender_id() {
        return sender_id;
    }


    public String getVideo_cover_uri() {
        return video_cover_uri;
    }


    public String getVideo_uri() {
        return video_uri;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
    }


    public String getGift_name() {
        return gift_name;
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


    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}
