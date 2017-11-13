package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ningl on 16/12/6.
 */
public class Comment implements Parcelable{
//    comment_id”:”xxxx”,
//            “comment_text”:”xxxx”,
//            “comment_user_name”:”xxxx”,
//            “comment_user_id”:”xxx”,
    private String comment_text;
    private String comment_user_name;
    private String comment_id;
    private String comment_user_id;
    private List<Notify_user_result> notify_user_result;

    public Comment() {

    }

    protected Comment(Parcel in) {
        comment_text = in.readString();
        comment_user_name = in.readString();
        comment_id = in.readString();
        comment_user_id = in.readString();
        notify_user_result = in.createTypedArrayList(Notify_user_result.CREATOR);
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getComment_user_name() {
        return comment_user_name;
    }

    public void setComment_user_name(String comment_user_name) {
        this.comment_user_name = comment_user_name;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_user_id() {
        return comment_user_id;
    }

    public void setComment_user_id(String comment_user_id) {
        this.comment_user_id = comment_user_id;
    }

    public List<Notify_user_result> getNotify_user_result() {
        return notify_user_result;
    }

    public void setNotify_user_result(List<Notify_user_result> notify_user_result) {
        this.notify_user_result = notify_user_result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comment_text);
        dest.writeString(comment_user_name);
        dest.writeString(comment_id);
        dest.writeString(comment_user_id);
        dest.writeTypedList(notify_user_result);
    }
}
