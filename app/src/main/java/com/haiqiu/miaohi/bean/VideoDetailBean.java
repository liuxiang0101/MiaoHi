package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.CommonUtil;

import java.util.List;

/**
 * 视频详情实体类
 * Created by ningl on 2016/6/22.
 */
public class VideoDetailBean implements Parcelable {


    //    "play_total" = 0;
    //    "praise_count" = 0;
    //    "praise_state" = false;
    //    "upload_time" = "2016-06-22 11:19:40.0";
    //    "upload_user_id" = "USER-db269cfe-263d-11e6-83a3-44a8424640fa";
    //    "upload_user_name" = "\U597d\U54af";
    //    "upload_user_portrait_state" = 10;
    //    "upload_user_portrait_uri" = "http://7xt99c.com1.z0.glb.clouddn.com/iconImage9C46439D85D54A2C8A9750D211C3F7E3_2016_05_30_04_12_28_91";
    //    "upload_user_type" = 20000;
    //    "video_id" = "VIDE-2845b98a-3828-11e6-83a3-44a8424640fa";
    //    "video_note" = "\U4f60\U7684\U65f6\U5019\U624d\U80af\U53bb";
    //    "video_state" = 10;
    //    "video_uri" = "http://video.dev.miaohi.com/9C46439D85D54A2C8A9750D211C3F7E3_2016_06_22_11_21_55_62Movie";
    //    "attention_state" = false;
    //    "cover_state" = 10;
    //    "cover_uri" = "http://image.dev.miaohi.com/9C46439D85D54A2C8A9750D211C3F7E3_2016_06_22_11_21_55_11vedioCoverImage";
    //    "duration_second" = 5;
    //    "hls_state" = 8;
    protected String video_id;//视频id
    protected boolean praise_state;//视频点赞状态
    protected long praise_count;
    public String user_id;//用户id
    public int attention_state;//关注状态

    private String play_total;
    private long upload_time;
    private String upload_user_id;
    private String upload_user_name;
    private String upload_user_portrait_state;
    private String upload_user_portrait_uri;
    private String upload_user_type;
    private String video_note;
    private int video_state;
    private String video_uri;
    private String cover_state;
    private String cover_uri;
    private String duration_second;
    private int hls_state;
    private String gift_count;
    private String hls_uri;
    private String shared_tag_text;
    private String vip_note;
    private String activity_name;
    private String upload_time_text;
    private String activity_id;
    private double video_width;
    private double video_height;
    private int answer_auth;
    private List<Notify_user_result> notify_user_result;
    private List<PraiseBean> praise_user_list;
    private int upload_user_gender;
    private String activity_uri;
    private String share_link_address;
    private String user_authentic;

    public String getUser_authentic() {
        return user_authentic;
    }

    public void setUser_authentic(String user_authentic) {
        this.user_authentic = user_authentic;
    }

    public String getShare_link_address() {
        return share_link_address;
    }

    public void setShare_link_address(String share_link_address) {
        this.share_link_address = share_link_address;
    }

    public String getActivity_uri() {
        return activity_uri;
    }

    public void setActivity_uri(String activity_uri) {
        this.activity_uri = activity_uri;
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

    public double getVideo_width() {
        return video_width;
    }

    public void setVideo_width(double video_width) {
        this.video_width = video_width;
    }

    public double getVideo_height() {
        return video_height;
    }

    public void setVideo_height(double video_height) {
        this.video_height = video_height;
    }

    public String getUser_id() {
        return upload_user_id;
    }

    public void setUser_id(String user_id) {
        upload_user_id = user_id;
    }

    public List<Notify_user_result> getNotify_user_result() {
        return notify_user_result;
    }

    public void setNotify_user_result(List<Notify_user_result> notify_user_result) {
        this.notify_user_result = notify_user_result;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getUpload_time_text() {
        return upload_time_text;
    }

    public void setUpload_time_text(String upload_time_text) {
        this.upload_time_text = upload_time_text;
    }

    public String getVip_note() {
        return vip_note;
    }

    public void setVip_note(String vip_note) {
        this.vip_note = vip_note;
    }

    public void setVideo_state(int video_state) {
        this.video_state = video_state;
    }

    public String getShared_tag_text() {
        return shared_tag_text;
    }

    public void setShared_tag_text(String shared_tag_text) {
        this.shared_tag_text = shared_tag_text;
    }

    public String getGift_count() {
        return gift_count;
    }

    public void setGift_count(String gift_count) {
        this.gift_count = gift_count;
    }

    private List<Invite_resultBean> invite_result;

    public String getPlay_total() {
        return play_total;
    }

    public void setPlay_total(String play_total) {
        this.play_total = play_total;
    }

    public long getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(long upload_time) {
        this.upload_time = upload_time;
    }

    public String getUpload_user_id() {
        return upload_user_id;
    }

    public void setUpload_user_id(String upload_user_id) {
        this.upload_user_id = upload_user_id;
    }

    public String getUpload_user_name() {
        return upload_user_name;
    }

    public void setUpload_user_name(String upload_user_name) {
        this.upload_user_name = upload_user_name;
    }

    public String getUpload_user_portrait_state() {
        return upload_user_portrait_state;
    }

    public void setUpload_user_portrait_state(String upload_user_portrait_state) {
        this.upload_user_portrait_state = upload_user_portrait_state;
    }

    public String getUpload_user_portrait_uri() {
        return upload_user_portrait_uri + ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public void setUpload_user_portrait_uri(String upload_user_portrait_uri) {
        this.upload_user_portrait_uri = upload_user_portrait_uri;
    }

    public String getUpload_user_type() {
        return upload_user_type;
    }

    public void setUpload_user_type(String upload_user_type) {
        this.upload_user_type = upload_user_type;
    }

    public String getVideo_note() {
        return video_note;
    }

    public void setVideo_note(String video_note) {
        this.video_note = video_note;
    }

    public int getVideo_state() {
        return video_state;
    }

    public String getVideo_uri() {
        return video_uri;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
    }


    public String getCover_state() {
        return cover_state;
    }

    public void setCover_state(String cover_state) {
        this.cover_state = cover_state;
    }

    public String getCover_uri() {
        if (null != cover_uri && cover_uri.contains("?")) {
            return cover_uri + ConstantsValue.Other.VIDEO_MAX_PREVIEW_PICTURE_PARAM_FRAME;
        }
        return cover_uri + ConstantsValue.Other.VIDEO_MAX_PREVIEW_PICTURE_PARAM;
    }

    public void setCover_uri(String cover_uri) {
        this.cover_uri = cover_uri;
    }

    public String getDuration_second() {
        return duration_second;
    }

    public void setDuration_second(String duration_second) {
        this.duration_second = duration_second;
    }

    public int getHls_state() {
        return hls_state;
    }

    public void setHls_state(int hls_state) {
        this.hls_state = hls_state;
    }

    public List<Invite_resultBean> getInvite_result() {
        return invite_result;
    }

    public void setInvite_result(List<Invite_resultBean> invite_result) {
        this.invite_result = invite_result;
    }

    public String getHls_uri() {
        return hls_uri;
    }

    public void setHls_uri(String hls_uri) {
        this.hls_uri = hls_uri;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public VideoDetailBean() {
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

    public void setAttention_state(boolean attention_state) {
        this.attention_state = attention_state ? 1 : 0;
    }

    public boolean isAttention_state() {

        return attention_state == 1;
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
        dest.writeString(this.play_total);
        dest.writeLong(this.upload_time);
        dest.writeString(this.upload_user_id);
        dest.writeString(this.upload_user_name);
        dest.writeString(this.upload_user_portrait_state);
        dest.writeString(this.upload_user_portrait_uri);
        dest.writeString(this.upload_user_type);
        dest.writeString(this.video_note);
        dest.writeInt(this.video_state);
        dest.writeString(this.video_uri);
        dest.writeString(this.cover_state);
        dest.writeString(this.cover_uri);
        dest.writeString(this.duration_second);
        dest.writeInt(this.hls_state);
        dest.writeString(this.gift_count);
        dest.writeString(this.hls_uri);
        dest.writeString(this.shared_tag_text);
        dest.writeString(this.vip_note);
        dest.writeString(this.activity_name);
        dest.writeString(this.upload_time_text);
        dest.writeString(this.activity_id);
        dest.writeDouble(this.video_width);
        dest.writeDouble(this.video_height);
        dest.writeInt(this.answer_auth);
        dest.writeTypedList(this.notify_user_result);
        dest.writeTypedList(this.praise_user_list);
        dest.writeInt(this.upload_user_gender);
        dest.writeString(this.activity_uri);
        dest.writeString(this.share_link_address);
        dest.writeString(this.user_authentic);
        dest.writeTypedList(this.invite_result);
    }

    protected VideoDetailBean(Parcel in) {
        this.video_id = in.readString();
        this.praise_state = in.readByte() != 0;
        this.praise_count = in.readLong();
        this.user_id = in.readString();
        this.attention_state = in.readInt();
        this.play_total = in.readString();
        this.upload_time = in.readLong();
        this.upload_user_id = in.readString();
        this.upload_user_name = in.readString();
        this.upload_user_portrait_state = in.readString();
        this.upload_user_portrait_uri = in.readString();
        this.upload_user_type = in.readString();
        this.video_note = in.readString();
        this.video_state = in.readInt();
        this.video_uri = in.readString();
        this.cover_state = in.readString();
        this.cover_uri = in.readString();
        this.duration_second = in.readString();
        this.hls_state = in.readInt();
        this.gift_count = in.readString();
        this.hls_uri = in.readString();
        this.shared_tag_text = in.readString();
        this.vip_note = in.readString();
        this.activity_name = in.readString();
        this.upload_time_text = in.readString();
        this.activity_id = in.readString();
        this.video_width = in.readDouble();
        this.video_height = in.readDouble();
        this.answer_auth = in.readInt();
        this.notify_user_result = in.createTypedArrayList(Notify_user_result.CREATOR);
        this.praise_user_list = in.createTypedArrayList(PraiseBean.CREATOR);
        this.upload_user_gender = in.readInt();
        this.activity_uri = in.readString();
        this.share_link_address = in.readString();
        this.user_authentic = in.readString();
        this.invite_result = in.createTypedArrayList(Invite_resultBean.CREATOR);
    }

    public static final Creator<VideoDetailBean> CREATOR = new Creator<VideoDetailBean>() {
        @Override
        public VideoDetailBean createFromParcel(Parcel source) {
            return new VideoDetailBean(source);
        }

        @Override
        public VideoDetailBean[] newArray(int size) {
            return new VideoDetailBean[size];
        }
    };


}
