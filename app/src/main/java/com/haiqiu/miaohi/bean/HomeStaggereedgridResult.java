package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.CommonUtil;

/**
 * 瀑布流数据
 * Created by hackest on 2016-08-12 19:50:46.
 */
public class HomeStaggereedgridResult implements Parcelable {


    /**
     * portrait_state : 10
     * cover_uri : http://image.test.miaohi.com/demo_video_cover_lgtv4k
     * user_name : vip1
     * hot_total : 36
     * hls_uri : http://video.dev.miaohi.com/sUWPWh5odxh9vtorJ2tsEue__hQ=/lumwgstyhThLgeZykMFTqxL6XZ6V
     * invite_state : false
     * duration_second : 213
     * gift_count : 0
     * video_uri : http://video.dev.miaohi.com/sUWPWh5odxh9vtorJ2tsEue__hQ=/lumwgstyhThLgeZykMFTqxL6XZ6V
     * upload_time : 2016-05-16 14:28:35.0
     * cover_state : 10
     * user_type : 20
     * user_id : USER-vipcd9a2-062a-11e6-9003-44a8424640fc
     * video_note : LG电视4K演示片
     * praise_count : 0
     * video_state : 12
     * hls_state : 10
     * portrait_uri : http://7xt99c.com2.z0.glb.qiniucdn.com/portrait_demo_woman_2.png
     * video_id : VIDE-713c39ab-0b56-11e6-9003-44a8424640fb
     */

    private String video_id;//视频id
    private boolean praise_state;//视频点赞状态
    private long praise_count;
    private String user_id;//用户id
    private int attention_state;//关注状态

    private String cover_uri;
    private String user_name;
    private String hot_total;
    private String hls_uri;
    private String invite_state;
    private String duration_second;
    private String gift_count;
    private String video_uri;
    private String upload_time;
    private String cover_state;
    private String user_type;
    private String video_note;
    private String video_state;
    private String hls_state;
    private String portrait_uri;
    private String auto_id;


    private String element_type;
    private String question_id;
    private String question_text;
    private String background_color;

    private String activity_id;
    private String activity_note;

    private int player_count;
    private int video_count;


    public String getCover_uri() {
        if (null != cover_uri && cover_uri.contains("?")) {
            return cover_uri + ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM_FRAME;
        }
        return cover_uri + ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM;
    }

    public void setCover_uri(String cover_uri) {
        this.cover_uri = cover_uri;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getHot_total() {
        return hot_total;
    }

    public void setHot_total(String hot_total) {
        this.hot_total = hot_total;
    }

    public String getHls_uri() {
        return hls_uri;
    }

    public void setHls_uri(String hls_uri) {
        this.hls_uri = hls_uri;
    }

    public String getInvite_state() {
        return invite_state;
    }

    public void setInvite_state(String invite_state) {
        this.invite_state = invite_state;
    }

    public String getDuration_second() {
        return duration_second;
    }

    public void setDuration_second(String duration_second) {
        this.duration_second = duration_second;
    }

    public String getGift_count() {
        return gift_count;
    }

    public void setGift_count(String gift_count) {
        this.gift_count = gift_count;
    }

    public String getVideo_uri() {
        return video_uri;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public String getCover_state() {
        return cover_state;
    }

    public void setCover_state(String cover_state) {
        this.cover_state = cover_state;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getVideo_note() {
        return video_note;
    }

    public void setVideo_note(String video_note) {
        this.video_note = video_note;
    }

    public String getVideo_state() {
        return video_state;
    }

    public void setVideo_state(String video_state) {
        this.video_state = video_state;
    }

    public String getHls_state() {
        return hls_state;
    }

    public void setHls_state(String hls_state) {
        this.hls_state = hls_state;
    }

    public String getPortrait_uri() {
        return portrait_uri + ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public void setPortrait_uri(String portrait_uri) {
        this.portrait_uri = portrait_uri;
    }


    public String getAuto_id() {
        return auto_id;
    }

    public void setAuto_id(String auto_id) {
        this.auto_id = auto_id;
    }

    public String getElement_type() {
        return element_type;
    }

    public void setElement_type(String element_type) {
        this.element_type = element_type;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_note() {
        return activity_note;
    }

    public void setActivity_note(String activity_note) {
        this.activity_note = activity_note;
    }

    public int getPlayer_count() {
        return player_count;
    }

    public void setPlayer_count(int player_count) {
        this.player_count = player_count;
    }

    public int getVideo_count() {
        return video_count;
    }

    public void setVideo_count(int video_count) {
        this.video_count = video_count;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public boolean isPraise_state() {
        return praise_state;
    }

    public void setPraise_state(boolean praise_state) {
        this.praise_state = praise_state;
    }

    public String getPraise_count() {
        return CommonUtil.formatCount(praise_count);
    }

    public void setPraise_count(long praise_count) {
        this.praise_count = praise_count;
    }

    public int getAttention_state() {
        return attention_state;
    }

    public void setAttention_state(int attention_state) {
        this.attention_state = attention_state;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.video_id);
        dest.writeByte(this.praise_state ? (byte) 1 : (byte) 0);
        dest.writeLong(this.praise_count);
        dest.writeString(this.user_id);
        dest.writeInt(this.attention_state);
        dest.writeString(this.cover_uri);
        dest.writeString(this.user_name);
        dest.writeString(this.hot_total);
        dest.writeString(this.hls_uri);
        dest.writeString(this.invite_state);
        dest.writeString(this.duration_second);
        dest.writeString(this.gift_count);
        dest.writeString(this.video_uri);
        dest.writeString(this.upload_time);
        dest.writeString(this.cover_state);
        dest.writeString(this.user_type);
        dest.writeString(this.video_note);
        dest.writeString(this.video_state);
        dest.writeString(this.hls_state);
        dest.writeString(this.portrait_uri);
        dest.writeString(this.auto_id);
        dest.writeString(this.element_type);
        dest.writeString(this.question_id);
        dest.writeString(this.question_text);
        dest.writeString(this.background_color);
        dest.writeString(this.activity_id);
        dest.writeString(this.activity_note);
        dest.writeInt(this.player_count);
        dest.writeInt(this.video_count);
    }

    public HomeStaggereedgridResult() {
    }

    protected HomeStaggereedgridResult(Parcel in) {
        this.video_id = in.readString();
        this.praise_state = in.readByte() != 0;
        this.praise_count = in.readLong();
        this.user_id = in.readString();
        this.attention_state = in.readInt();
        this.cover_uri = in.readString();
        this.user_name = in.readString();
        this.hot_total = in.readString();
        this.hls_uri = in.readString();
        this.invite_state = in.readString();
        this.duration_second = in.readString();
        this.gift_count = in.readString();
        this.video_uri = in.readString();
        this.upload_time = in.readString();
        this.cover_state = in.readString();
        this.user_type = in.readString();
        this.video_note = in.readString();
        this.video_state = in.readString();
        this.hls_state = in.readString();
        this.portrait_uri = in.readString();
        this.auto_id = in.readString();
        this.element_type = in.readString();
        this.question_id = in.readString();
        this.question_text = in.readString();
        this.background_color = in.readString();
        this.activity_id = in.readString();
        this.activity_note = in.readString();
        this.player_count = in.readInt();
        this.video_count = in.readInt();
    }

    public static final Creator<HomeStaggereedgridResult> CREATOR = new Creator<HomeStaggereedgridResult>() {
        @Override
        public HomeStaggereedgridResult createFromParcel(Parcel source) {
            return new HomeStaggereedgridResult(source);
        }

        @Override
        public HomeStaggereedgridResult[] newArray(int size) {
            return new HomeStaggereedgridResult[size];
        }
    };
}
