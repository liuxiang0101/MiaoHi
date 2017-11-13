package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.MHStringUtils;

import java.util.List;

/**
 * Created by ningl on 16/12/6.
 */
public class UserWork extends VideoAndImg implements Parcelable {
    private String video_cover_uri;
    private String video_uri;
    private String video_note;
    private int comments_count;
    private int video_state;
    private List<Notify_user_result> notify_user_result;
    private List<Comment> comment_list;
    private String photo_thumb_uri;
    private String photo_uri;
    private String photo_note;
    private int photo_state;
    private boolean isPraising;
    private double width;
    private double height;
    private String share_link_address;
    private String upload_time_text;
    private long duration_second;
    private long play_total;
    private String activity_name;
    private String activity_uri;
    private long upload_time;
    private boolean isLoading;


    public UserWork(){

    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public long getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(long upload_time) {
        this.upload_time = upload_time;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getActivity_uri() {
        return activity_uri;
    }

    public void setActivity_uri(String activity_uri) {
        this.activity_uri = activity_uri;
    }

    public long getDuration_second() {
        return duration_second;
    }

    public void setDuration_second(long duration_second) {
        this.duration_second = duration_second;
    }

    public long getPlay_total() {
        return play_total;
    }

    public void setPlay_total(long play_total) {
        this.play_total = play_total;
    }

    public String getUpload_time_text() {
        return upload_time_text;
    }

    public void setUpload_time_text(String upload_time_text) {
        this.upload_time_text = upload_time_text;
    }

    public String getShare_link_address() {
        if(MHStringUtils.isEmpty(share_link_address)){
            return ConstantsValue.Shared.APPDOWNLOAD;
        }
        return share_link_address;
    }

    public void setShare_link_address(String share_link_address) {
        this.share_link_address = share_link_address;
    }

    public String getPhoto_thumb_uri() {
        return photo_thumb_uri;
    }

    public void setPhoto_thumb_uri(String photo_thumb_uri) {
        this.photo_thumb_uri = photo_thumb_uri;
    }

    public String getPhoto_uri() {
        return photo_uri;
    }

    public void setPhoto_uri(String photo_uri) {
        this.photo_uri = photo_uri;
    }

    public String getVideo_cover_uri() {
        return video_cover_uri;
    }

    public void setVideo_cover_uri(String video_cover_uri) {
        this.video_cover_uri = video_cover_uri;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isPraising() {
        return isPraising;
    }

    public void setPraising(boolean praising) {
        isPraising = praising;
    }

    public void setPraise_count(int praise_count) {
        this.praise_count = praise_count;
    }

    public int getVideo_state() {
        return video_state;
    }

    public void setVideo_state(int video_state) {
        this.video_state = video_state;
    }

    public String getVideo_uri() {
        return video_uri;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
    }

    public String getVideo_note() {
        return video_note;
    }

    public void setVideo_note(String video_note) {
        this.video_note = video_note;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public List<Notify_user_result> getNotify_user_result() {
        return notify_user_result;
    }

    public void setNotify_user_result(List<Notify_user_result> notify_user_result) {
        this.notify_user_result = notify_user_result;
    }

    public List<Comment> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<Comment> comment_list) {
        this.comment_list = comment_list;
    }

    public String getPhoto_thumb_url() {
        return photo_thumb_uri;
    }

    public void setPhoto_thumb_url(String photo_thumb_url) {
        this.photo_thumb_uri = photo_thumb_url;
    }

    public String getPhoto_note() {
        return photo_note;
    }

    public void setPhoto_note(String photo_note) {
        this.photo_note = photo_note;
    }

    public int getPhoto_state() {
        return photo_state;
    }

    public void setPhoto_state(int photo_state) {
        this.photo_state = photo_state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.video_cover_uri);
        dest.writeString(this.video_uri);
        dest.writeString(this.video_note);
        dest.writeInt(this.comments_count);
        dest.writeInt(this.video_state);
        dest.writeTypedList(this.notify_user_result);
        dest.writeTypedList(this.comment_list);
        dest.writeString(this.photo_thumb_uri);
        dest.writeString(this.photo_uri);
        dest.writeString(this.photo_note);
        dest.writeInt(this.photo_state);
        dest.writeByte(this.isPraising ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.width);
        dest.writeDouble(this.height);
        dest.writeString(this.share_link_address);
        dest.writeString(this.upload_time_text);
        dest.writeLong(this.duration_second);
        dest.writeLong(this.play_total);
        dest.writeString(this.activity_name);
        dest.writeString(this.activity_uri);
        dest.writeLong(this.upload_time);
        dest.writeByte(this.isLoading ? (byte) 1 : (byte) 0);
    }

    protected UserWork(Parcel in) {
        super(in);
        this.video_cover_uri = in.readString();
        this.video_uri = in.readString();
        this.video_note = in.readString();
        this.comments_count = in.readInt();
        this.video_state = in.readInt();
        this.notify_user_result = in.createTypedArrayList(Notify_user_result.CREATOR);
        this.comment_list = in.createTypedArrayList(Comment.CREATOR);
        this.photo_thumb_uri = in.readString();
        this.photo_uri = in.readString();
        this.photo_note = in.readString();
        this.photo_state = in.readInt();
        this.isPraising = in.readByte() != 0;
        this.width = in.readDouble();
        this.height = in.readDouble();
        this.share_link_address = in.readString();
        this.upload_time_text = in.readString();
        this.duration_second = in.readLong();
        this.play_total = in.readLong();
        this.activity_name = in.readString();
        this.activity_uri = in.readString();
        this.upload_time = in.readLong();
        this.isLoading = in.readByte() != 0;
    }

    public static final Creator<UserWork> CREATOR = new Creator<UserWork>() {
        @Override
        public UserWork createFromParcel(Parcel source) {
            return new UserWork(source);
        }

        @Override
        public UserWork[] newArray(int size) {
            return new UserWork[size];
        }
    };
}
