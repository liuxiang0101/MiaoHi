package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.haiqiu.miaohi.ConstantsValue;

import java.util.List;

/**
 * 视频详情评论实体类
 * Created by ningl on 2016/6/22.
 */
public class VideoDetailUserCommentBean implements Parcelable {


    /**
     * root_id : xxxx
     * root_type : xxxx
     * root_uri : xxxxx
     * root_uri_state : xxxx
     * root_cover_uri : xxxxxx
     * root_cover_uri_state : xxxxx
     * root_hls_uri : xxxx
     * root_hls_state : xx
     * root_user_id : xxxx
     * root_user_type : xxxx
     * root_user_name : xxxx
     * root_user_portrait_uri : xxxx
     * root_user_portrait_state : xxxx
     * commented_id : xxxx
     * commented_type : x
     * commented_user_id : xxXXxx
     * commented_user_type : xxxx
     * commented_user_name : xxxxxx
     * commented_user_portrait_uri : xxxxx
     * commented_user_portrait_state : xxxx
     * comment_id : xxxxx
     * comment_time : xxxxxxx
     * comment_state : xxxxxxx
     * comment_text : xxxxxx
     * praise_count : xxxx
     * comment_user_id : xxxxx
     * comment_user_type : xxxxx
     * comment_user_name : xxxxx
     * comment_user_portrait_uri : xxxxxx
     * comment_user_portrait_state : x
     * click_state : xxxxx
     */

    private String root_id;
    private String root_type;
    private String root_uri;
    private String root_uri_state;
    private String root_cover_uri;
    private String root_cover_uri_state;
    private String root_hls_uri;
    private String root_hls_state;
    private String root_user_id;
    private String root_user_type;
    private String root_user_name;
    private String root_user_portrait_uri;
    private String root_user_portrait_state;
    private String commented_id;
    private String commented_type;
    private String commented_user_id;
    private String commented_user_type;
    private String commented_user_name;
    private String commented_user_portrait_uri;
    private String commented_user_portrait_state;
    private String comment_id;
    private String comment_time;
    private String comment_state;
    private String comment_text;
    private String praise_count;
    private String comment_user_id;
    private String comment_user_type;
    private String comment_user_name;
    private String comment_user_portrait_uri;
    private String comment_user_portrait_state;
    private String click_state;
    private String comment_time_text;
    private List<Notify_user_result> notify_user_result;
    private int size = -1;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Notify_user_result> getNotify_user_result() {
        return notify_user_result;
    }

    public void setNotify_user_result(List<Notify_user_result> notify_user_result) {
        this.notify_user_result = notify_user_result;
    }

    public String getComment_time_text() {
        return comment_time_text;
    }

    public void setComment_time_text(String comment_time_text) {
        this.comment_time_text = comment_time_text;
    }

    public String getRoot_id() {
        return root_id;
    }

    public void setRoot_id(String root_id) {
        this.root_id = root_id;
    }

    public String getRoot_type() {
        return root_type;
    }

    public void setRoot_type(String root_type) {
        this.root_type = root_type;
    }

    public String getRoot_uri() {
        return root_uri;
    }

    public void setRoot_uri(String root_uri) {
        this.root_uri = root_uri;
    }

    public String getRoot_uri_state() {
        return root_uri_state;
    }

    public void setRoot_uri_state(String root_uri_state) {
        this.root_uri_state = root_uri_state;
    }

    public String getRoot_cover_uri() {
        if (null != root_cover_uri && root_cover_uri.contains("?")) {
            return root_cover_uri + ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM_FRAME;
        }
        return root_cover_uri+ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM;
    }

    public void setRoot_cover_uri(String root_cover_uri) {
        this.root_cover_uri = root_cover_uri;
    }

    public String getRoot_cover_uri_state() {
        return root_cover_uri_state;
    }

    public void setRoot_cover_uri_state(String root_cover_uri_state) {
        this.root_cover_uri_state = root_cover_uri_state;
    }

    public String getRoot_hls_uri() {
        return root_hls_uri;
    }

    public void setRoot_hls_uri(String root_hls_uri) {
        this.root_hls_uri = root_hls_uri;
    }

    public String getRoot_hls_state() {
        return root_hls_state;
    }

    public void setRoot_hls_state(String root_hls_state) {
        this.root_hls_state = root_hls_state;
    }

    public String getRoot_user_id() {
        return root_user_id;
    }

    public void setRoot_user_id(String root_user_id) {
        this.root_user_id = root_user_id;
    }

    public String getRoot_user_type() {
        return root_user_type;
    }

    public void setRoot_user_type(String root_user_type) {
        this.root_user_type = root_user_type;
    }

    public String getRoot_user_name() {
        return root_user_name;
    }

    public void setRoot_user_name(String root_user_name) {
        this.root_user_name = root_user_name;
    }

    public String getRoot_user_portrait_uri() {
        return root_user_portrait_uri + ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public void setRoot_user_portrait_uri(String root_user_portrait_uri) {
        this.root_user_portrait_uri = root_user_portrait_uri ;
    }

    public String getRoot_user_portrait_state() {
        return root_user_portrait_state;
    }

    public void setRoot_user_portrait_state(String root_user_portrait_state) {
        this.root_user_portrait_state = root_user_portrait_state;
    }

    public String getCommented_id() {
        return commented_id;
    }

    public void setCommented_id(String commented_id) {
        this.commented_id = commented_id;
    }

    public String getCommented_type() {
        return commented_type;
    }

    public void setCommented_type(String commented_type) {
        this.commented_type = commented_type;
    }

    public String getCommented_user_id() {
        return commented_user_id;
    }

    public void setCommented_user_id(String commented_user_id) {
        this.commented_user_id = commented_user_id;
    }

    public String getCommented_user_type() {
        return commented_user_type;
    }

    public void setCommented_user_type(String commented_user_type) {
        this.commented_user_type = commented_user_type;
    }

    public String getCommented_user_name() {
        return commented_user_name;
    }

    public void setCommented_user_name(String commented_user_name) {
        this.commented_user_name = commented_user_name;
    }

    public String getCommented_user_portrait_uri() {
        return commented_user_portrait_uri + ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public void setCommented_user_portrait_uri(String commented_user_portrait_uri) {
        this.commented_user_portrait_uri = commented_user_portrait_uri ;
    }

    public String getCommented_user_portrait_state() {
        return commented_user_portrait_state;
    }

    public void setCommented_user_portrait_state(String commented_user_portrait_state) {
        this.commented_user_portrait_state = commented_user_portrait_state;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getComment_state() {
        return comment_state;
    }

    public void setComment_state(String comment_state) {
        this.comment_state = comment_state;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getPraise_count() {
        return praise_count;
    }

    public void setPraise_count(String praise_count) {
        this.praise_count = praise_count;
    }

    public String getComment_user_id() {
        return comment_user_id;
    }

    public void setComment_user_id(String comment_user_id) {
        this.comment_user_id = comment_user_id;
    }

    public String getComment_user_type() {
        return comment_user_type;
    }

    public void setComment_user_type(String comment_user_type) {
        this.comment_user_type = comment_user_type;
    }

    public String getComment_user_name() {
        return comment_user_name;
    }

    public void setComment_user_name(String comment_user_name) {
        this.comment_user_name = comment_user_name;
    }

    public String getComment_user_portrait_uri() {
        return comment_user_portrait_uri + ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public void setComment_user_portrait_uri(String comment_user_portrait_uri) {
        this.comment_user_portrait_uri = comment_user_portrait_uri;
    }

    public String getComment_user_portrait_state() {
        return comment_user_portrait_state;
    }

    public void setComment_user_portrait_state(String comment_user_portrait_state) {
        this.comment_user_portrait_state = comment_user_portrait_state;
    }

    public String getClick_state() {
        return click_state;
    }

    public void setClick_state(String click_state) {
        this.click_state = click_state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.root_id);
        dest.writeString(this.root_type);
        dest.writeString(this.root_uri);
        dest.writeString(this.root_uri_state);
        dest.writeString(this.root_cover_uri);
        dest.writeString(this.root_cover_uri_state);
        dest.writeString(this.root_hls_uri);
        dest.writeString(this.root_hls_state);
        dest.writeString(this.root_user_id);
        dest.writeString(this.root_user_type);
        dest.writeString(this.root_user_name);
        dest.writeString(this.root_user_portrait_uri);
        dest.writeString(this.root_user_portrait_state);
        dest.writeString(this.commented_id);
        dest.writeString(this.commented_type);
        dest.writeString(this.commented_user_id);
        dest.writeString(this.commented_user_type);
        dest.writeString(this.commented_user_name);
        dest.writeString(this.commented_user_portrait_uri);
        dest.writeString(this.commented_user_portrait_state);
        dest.writeString(this.comment_id);
        dest.writeString(this.comment_time);
        dest.writeString(this.comment_state);
        dest.writeString(this.comment_text);
        dest.writeString(this.praise_count);
        dest.writeString(this.comment_user_id);
        dest.writeString(this.comment_user_type);
        dest.writeString(this.comment_user_name);
        dest.writeString(this.comment_user_portrait_uri);
        dest.writeString(this.comment_user_portrait_state);
        dest.writeString(this.click_state);
        dest.writeString(this.comment_time_text);
        dest.writeTypedList(this.notify_user_result);
        dest.writeInt(this.size);
    }

    public VideoDetailUserCommentBean() {
    }

    protected VideoDetailUserCommentBean(Parcel in) {
        this.root_id = in.readString();
        this.root_type = in.readString();
        this.root_uri = in.readString();
        this.root_uri_state = in.readString();
        this.root_cover_uri = in.readString();
        this.root_cover_uri_state = in.readString();
        this.root_hls_uri = in.readString();
        this.root_hls_state = in.readString();
        this.root_user_id = in.readString();
        this.root_user_type = in.readString();
        this.root_user_name = in.readString();
        this.root_user_portrait_uri = in.readString();
        this.root_user_portrait_state = in.readString();
        this.commented_id = in.readString();
        this.commented_type = in.readString();
        this.commented_user_id = in.readString();
        this.commented_user_type = in.readString();
        this.commented_user_name = in.readString();
        this.commented_user_portrait_uri = in.readString();
        this.commented_user_portrait_state = in.readString();
        this.comment_id = in.readString();
        this.comment_time = in.readString();
        this.comment_state = in.readString();
        this.comment_text = in.readString();
        this.praise_count = in.readString();
        this.comment_user_id = in.readString();
        this.comment_user_type = in.readString();
        this.comment_user_name = in.readString();
        this.comment_user_portrait_uri = in.readString();
        this.comment_user_portrait_state = in.readString();
        this.click_state = in.readString();
        this.comment_time_text = in.readString();
        this.notify_user_result = in.createTypedArrayList(Notify_user_result.CREATOR);
        this.size = in.readInt();
    }

    public static final Parcelable.Creator<VideoDetailUserCommentBean> CREATOR = new Parcelable.Creator<VideoDetailUserCommentBean>() {
        @Override
        public VideoDetailUserCommentBean createFromParcel(Parcel source) {
            return new VideoDetailUserCommentBean(source);
        }

        @Override
        public VideoDetailUserCommentBean[] newArray(int size) {
            return new VideoDetailUserCommentBean[size];
        }
    };
}
