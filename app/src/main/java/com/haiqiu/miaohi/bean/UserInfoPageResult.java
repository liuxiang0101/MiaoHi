package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by miaohi on 2016/6/2.
 */
public class UserInfoPageResult implements Parcelable {
    private String user_id;//用户id
    private int attention_state;//关注状态

    private int portrait_state;
    private int user_type;
    private String user_name;
    private String nick_name;
    private String attention_time;
    private String portrait_uri;

    public int getPortrait_state() {
        return portrait_state;
    }

    public int getUser_type() {
        return user_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        if (null == user_name) user_name = nick_name;
        return user_name;
    }

    public void setPortrait_state(int portrait_state) {
        this.portrait_state = portrait_state;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getAttention_time() {
        return attention_time;
    }

    public void setAttention_time(String attention_time) {
        this.attention_time = attention_time;
    }

    public String getPortrait_uri() {
        return portrait_uri;
    }

    public void setPortrait_uri(String portrait_uri) {
        this.portrait_uri = portrait_uri;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeInt(this.attention_state);
        dest.writeInt(this.portrait_state);
        dest.writeInt(this.user_type);
        dest.writeString(this.user_name);
        dest.writeString(this.nick_name);
        dest.writeString(this.attention_time);
        dest.writeString(this.portrait_uri);
    }

    public UserInfoPageResult() {
    }

    protected UserInfoPageResult(Parcel in) {
        this.user_id = in.readString();
        this.attention_state = in.readInt();
        this.portrait_state = in.readInt();
        this.user_type = in.readInt();
        this.user_name = in.readString();
        this.nick_name = in.readString();
        this.attention_time = in.readString();
        this.portrait_uri = in.readString();
    }

    public static final Parcelable.Creator<UserInfoPageResult> CREATOR = new Parcelable.Creator<UserInfoPageResult>() {
        @Override
        public UserInfoPageResult createFromParcel(Parcel source) {
            return new UserInfoPageResult(source);
        }

        @Override
        public UserInfoPageResult[] newArray(int size) {
            return new UserInfoPageResult[size];
        }
    };
}
