package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.haiqiu.miaohi.utils.CommonUtil;

/**
 * 视频详情和图片详情
 * Created by ningl on 16/12/17.
 */
public class VideoAndImg implements Parcelable {

    private String photo_id;
    private int type;
    private int element_type;
    private int content_type;

    protected String video_id;//视频id
    protected boolean praise_state;//视频点赞状态
    protected long praise_count;
    protected String user_id;//用户id
    protected int attention_state;//关注状态
    protected boolean isDelete;//是否已经被删除

    public void setAttention_state(int attention_state) {
        this.attention_state = attention_state;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public int getType() {
        if (element_type != 0) return element_type;
        if (content_type != 0) return content_type;
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getElement_type() {
        return getContent_type();
    }

    public void setElement_type(int element_type) {
        setContent_type(element_type);
    }

    public int getContent_type() {
        return content_type;
    }

    public void setContent_type(int content_type) {
        this.content_type = content_type;
    }

    public VideoAndImg() {
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

    public long getPraiseSrcCount() {
        return praise_count;
    }

    public void setPraise_count(long praise_count) {
        this.praise_count = praise_count;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getAttention_state() {
        return attention_state;
    }

    public void setAttention_state(boolean attention_state) {
        this.attention_state = attention_state ? 1 : 0;
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
        dest.writeString(this.photo_id);
        dest.writeInt(this.type);
        dest.writeInt(this.element_type);
        dest.writeInt(this.content_type);
        dest.writeString(this.video_id);
        dest.writeByte(this.praise_state ? (byte) 1 : (byte) 0);
        dest.writeLong(this.praise_count);
        dest.writeString(this.user_id);
        dest.writeInt(this.attention_state);
        dest.writeByte(this.isDelete ? (byte) 1 : (byte) 0);
    }

    protected VideoAndImg(Parcel in) {
        this.photo_id = in.readString();
        this.type = in.readInt();
        this.element_type = in.readInt();
        this.content_type = in.readInt();
        this.video_id = in.readString();
        this.praise_state = in.readByte() != 0;
        this.praise_count = in.readLong();
        this.user_id = in.readString();
        this.attention_state = in.readInt();
        this.isDelete = in.readByte() != 0;
    }

    public static final Creator<VideoAndImg> CREATOR = new Creator<VideoAndImg>() {
        @Override
        public VideoAndImg createFromParcel(Parcel source) {
            return new VideoAndImg(source);
        }

        @Override
        public VideoAndImg[] newArray(int size) {
            return new VideoAndImg[size];
        }
    };
}
