package com.haiqiu.miaohi.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.haiqiu.miaohi.utils.CommonUtil;

import java.util.List;

/**
 * 图片详情
 * Created by ningl on 16/12/17.
 */
public class ImgDetail implements Parcelable{

    /**
     * photo_id : xxx
     * photo_uri : xxxxxx
     * photo_state : xxxxx
     * photo_height : xxxx
     * photo_width : xxxx
     * upload_user_id : xxxx
     * upload_user_type : xxxxx
     * upload_user_name : xxxx
     * upload_user_portrait_uri : xxxxx
     * upload_user_portrait_state : xxxx
     * praise_count : 4
     * attention_state : xxx
     * upload_time : xxXXxx
     * upload_time_text : xxXXxx
     * photo_note : xxXXxx
     * play_total : xxxxx
     * praise_state : xxxx
     * vip_note : xxxx
     * activity_id : xxxx
     * activity_name : xxxx
     */

    private String video_id;//视频id
    private boolean praise_state;//视频点赞状态
    private long praise_count;
    private String user_id;//用户id
    private int attention_state;//关注状态

    private String photo_id;
    private String photo_uri;
    private int photo_state;
    private double photo_height;
    private double photo_width;
    private String upload_user_id;
    private int upload_user_type;
    private String upload_user_name;
    private String upload_user_portrait_uri;
    private int upload_user_portrait_state;
    private long upload_time;
    private String upload_time_text;
    private String photo_note;
    private int play_total;
    private String vip_note;
    private String activity_id;
    private String activity_name;
    private int answer_auth;
    private List<PraiseBean> praise_user_list;
    private int upload_user_gender;
    private String share_link_address;
    private String activity_uri;
    private String user_authentic;

    public String getUser_authentic() {
        return user_authentic;
    }

    public void setUser_authentic(String user_authentic) {
        this.user_authentic = user_authentic;
    }

    public String getActivity_uri() {
        return activity_uri;
    }

    public void setActivity_uri(String activity_uri) {
        this.activity_uri = activity_uri;
    }

    public String getShare_link_address() {
        return share_link_address;
    }

    public void setShare_link_address(String share_link_address) {
        this.share_link_address = share_link_address;
    }

    public int getUpload_user_gender() {
        return upload_user_gender;
    }

    public void setUpload_user_gender(int upload_user_gender) {
        this.upload_user_gender = upload_user_gender;
    }

    public int getAnswer_auth() {
        return answer_auth;
    }

    public void setAnswer_auth(int answer_auth) {
        this.answer_auth = answer_auth;
    }

    public List<PraiseBean> getPraise_user_list() {
        return praise_user_list;
    }

    public void setPraise_user_list(List<PraiseBean> praise_user_list) {
        this.praise_user_list = praise_user_list;
    }


    private List<Notify_user_result> notify_user_result;

    public List<Notify_user_result> getNotify_user_result() {
        return notify_user_result;
    }

    public void setNotify_user_result(List<Notify_user_result> notify_user_result) {
        this.notify_user_result = notify_user_result;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getPhoto_uri() {
        return photo_uri;
    }

    public void setPhoto_uri(String photo_uri) {
        this.photo_uri = photo_uri;
    }

    public int getPhoto_state() {
        return photo_state;
    }

    public void setPhoto_state(int photo_state) {
        this.photo_state = photo_state;
    }

    public double getPhoto_height() {
        return photo_height;
    }

    public void setPhoto_height(double photo_height) {
        this.photo_height = photo_height;
    }

    public double getPhoto_width() {
        return photo_width;
    }

    public void setPhoto_width(double photo_width) {
        this.photo_width = photo_width;
    }

    public String getUpload_user_id() {
        return upload_user_id;
    }

    public void setUpload_user_id(String upload_user_id) {
        this.upload_user_id = upload_user_id;
    }

    public int getUpload_user_type() {
        return upload_user_type;
    }

    public void setUpload_user_type(int upload_user_type) {
        this.upload_user_type = upload_user_type;
    }

    public String getUpload_user_name() {
        return upload_user_name;
    }

    public void setUpload_user_name(String upload_user_name) {
        this.upload_user_name = upload_user_name;
    }

    public String getUpload_user_portrait_uri() {
        return upload_user_portrait_uri;
    }

    public void setUpload_user_portrait_uri(String upload_user_portrait_uri) {
        this.upload_user_portrait_uri = upload_user_portrait_uri;
    }

    public int getUpload_user_portrait_state() {
        return upload_user_portrait_state;
    }

    public void setUpload_user_portrait_state(int upload_user_portrait_state) {
        this.upload_user_portrait_state = upload_user_portrait_state;
    }

    public void setPraise_count(int praise_count) {
        this.praise_count = praise_count;
    }

    public boolean isAttention_state() {
        return attention_state == 1;
    }

    public void setAttention_state(boolean attention_state) {
        this.attention_state = attention_state ? 1 : 0;
    }

    public long getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(long upload_time) {
        this.upload_time = upload_time;
    }

    public String getUpload_time_text() {
        return upload_time_text;
    }

    public void setUpload_time_text(String upload_time_text) {
        this.upload_time_text = upload_time_text;
    }

    public String getPhoto_note() {
        return photo_note;
    }

    public void setPhoto_note(String photo_note) {
        this.photo_note = photo_note;
    }

    public int getPlay_total() {
        return play_total;
    }

    public void setPlay_total(int play_total) {
        this.play_total = play_total;
    }

    public boolean isPraise_state() {
        return praise_state;
    }

    public void setPraise_state(boolean praise_state) {
        this.praise_state = praise_state;
    }

    public String getVip_note() {
        return vip_note;
    }

    public void setVip_note(String vip_note) {
        this.vip_note = vip_note;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public ImgDetail() {
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getPraise_count() {
        return CommonUtil.formatCount(praise_count);
    }

    public void setPraise_count(long praise_count) {
        this.praise_count = praise_count;
    }

    public String getUser_id() {
        return upload_user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getAttention_state() {
        return attention_state;
    }

    public void setAttention_state(int attention_state) {
        this.attention_state = attention_state;
    }

    public long getSrcPraise_count() {
        return praise_count;
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
        dest.writeString(this.photo_id);
        dest.writeString(this.photo_uri);
        dest.writeInt(this.photo_state);
        dest.writeDouble(this.photo_height);
        dest.writeDouble(this.photo_width);
        dest.writeString(this.upload_user_id);
        dest.writeInt(this.upload_user_type);
        dest.writeString(this.upload_user_name);
        dest.writeString(this.upload_user_portrait_uri);
        dest.writeInt(this.upload_user_portrait_state);
        dest.writeLong(this.upload_time);
        dest.writeString(this.upload_time_text);
        dest.writeString(this.photo_note);
        dest.writeInt(this.play_total);
        dest.writeString(this.vip_note);
        dest.writeString(this.activity_id);
        dest.writeString(this.activity_name);
        dest.writeInt(this.answer_auth);
        dest.writeTypedList(this.praise_user_list);
        dest.writeInt(this.upload_user_gender);
        dest.writeString(this.share_link_address);
        dest.writeString(this.activity_uri);
        dest.writeString(this.user_authentic);
        dest.writeTypedList(this.notify_user_result);
    }

    protected ImgDetail(Parcel in) {
        this.video_id = in.readString();
        this.praise_state = in.readByte() != 0;
        this.praise_count = in.readLong();
        this.user_id = in.readString();
        this.attention_state = in.readInt();
        this.photo_id = in.readString();
        this.photo_uri = in.readString();
        this.photo_state = in.readInt();
        this.photo_height = in.readDouble();
        this.photo_width = in.readDouble();
        this.upload_user_id = in.readString();
        this.upload_user_type = in.readInt();
        this.upload_user_name = in.readString();
        this.upload_user_portrait_uri = in.readString();
        this.upload_user_portrait_state = in.readInt();
        this.upload_time = in.readLong();
        this.upload_time_text = in.readString();
        this.photo_note = in.readString();
        this.play_total = in.readInt();
        this.vip_note = in.readString();
        this.activity_id = in.readString();
        this.activity_name = in.readString();
        this.answer_auth = in.readInt();
        this.praise_user_list = in.createTypedArrayList(PraiseBean.CREATOR);
        this.upload_user_gender = in.readInt();
        this.share_link_address = in.readString();
        this.activity_uri = in.readString();
        this.user_authentic = in.readString();
        this.notify_user_result = in.createTypedArrayList(Notify_user_result.CREATOR);
    }

    public static final Creator<ImgDetail> CREATOR = new Creator<ImgDetail>() {
        @Override
        public ImgDetail createFromParcel(Parcel source) {
            return new ImgDetail(source);
        }

        @Override
        public ImgDetail[] newArray(int size) {
            return new ImgDetail[size];
        }
    };

}
