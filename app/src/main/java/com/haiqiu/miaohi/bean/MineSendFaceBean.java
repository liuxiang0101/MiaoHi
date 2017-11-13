package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.ConstantsValue;

import java.io.Serializable;

/**
 * Created by ningl on 2016/5/27.
 */
public class MineSendFaceBean implements Serializable{

    /**
     * video_uri : http://o7fa02tzz.bkt.clouddn.com/3E0CFD7E7E31418FA2813A8C5A8D9F31_2016_05_26_18_57_28_15Movie
     * send_face_time : 2016-05-26 20:35:37.0
     * face_icon_uri : http://o7favme76.bkt.clouddn.com/face_big_2016_05_20_03_37_10_957
     * face_id : FACE-d3c4ffef-24cb-4bac-869b-ee4e9f1937ff
     * face_name : 喜欢
     * face_icon_role : big
     * video_state : 10
     * face_icon_state : 10
     * video_cover_state : 10
     * video_id : VIDE-a484a489-2330-11e6-ae3b-5cb9018949c8
     * video_cover_uri : http://o7fawhrna.bkt.clouddn.com/3E0CFD7E7E31418FA2813A8C5A8D9F31_2016_05_26_18_57_26_61vedioCoverImage
     */

    private String video_uri;
    private String send_face_time;
    private String face_icon_uri;
    private String face_id;
    private String face_name;
    private String face_icon_role;
    private String video_state;
    private String face_icon_state;
    private String video_cover_state;
    private String video_id;
    private String video_cover_uri;

    public String getVideo_uri() {
        return video_uri;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
    }

    public String getSend_face_time() {
        return send_face_time;
    }


    public String getFace_icon_uri() {
        return face_icon_uri;
    }


    public String getVideo_state() {
        return video_state;
    }

    public void setVideo_state(String video_state) {
        this.video_state = video_state;
    }



    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getVideo_cover_uri() {
        if (null != video_cover_uri && video_cover_uri.contains("?")) {
            return video_cover_uri + ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM_FRAME;
        }
        return video_cover_uri+ ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM;
    }

}
