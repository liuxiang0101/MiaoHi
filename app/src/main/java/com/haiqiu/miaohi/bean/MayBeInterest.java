package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 关注页可能感兴趣的人
 * Created by ningl on 16/12/16.
 */
public class MayBeInterest implements Parcelable {


    /**
     * user_id : xxx
     * username : xxx
     * portrait_uri : xxx
     * portait_state : xxx
     * user_note : xxx
     * user_type : xxx
     * attention_state : xxx
     */
    private String user_id;//用户id
    private int attention_state;//关注状态

    private String user_name;
    private String portrait_uri;
    private boolean portait_state;
    private String user_note;
    private int user_type;
    private boolean findMore;

    public boolean isFindMore() {
        return findMore;
    }

    public void setFindMore(boolean findMore) {
        this.findMore = findMore;
    }

    public String getUsername() {
        return user_name;
    }

    public void setUsername(String username) {
        this.user_name = username;
    }

    public String getPortrait_uri() {
        return portrait_uri;
    }

    public void setPortrait_uri(String portrait_uri) {
        this.portrait_uri = portrait_uri;
    }

    public boolean isPortait_state() {
        return portait_state;
    }

    public void setPortait_state(boolean portait_state) {
        this.portait_state = portait_state;
    }

    public String getUser_note() {
        return user_note;
    }

    public void setUser_note(String user_note) {
        this.user_note = user_note;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
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

    public void setAttention_state(int attention_state) {
        this.attention_state = attention_state;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeInt(this.attention_state);
        dest.writeString(this.user_name);
        dest.writeString(this.portrait_uri);
        dest.writeByte(this.portait_state ? (byte) 1 : (byte) 0);
        dest.writeString(this.user_note);
        dest.writeInt(this.user_type);
        dest.writeByte(this.findMore ? (byte) 1 : (byte) 0);
    }

    public MayBeInterest() {
    }

    protected MayBeInterest(Parcel in) {
        this.user_id = in.readString();
        this.attention_state = in.readInt();
        this.user_name = in.readString();
        this.portrait_uri = in.readString();
        this.portait_state = in.readByte() != 0;
        this.user_note = in.readString();
        this.user_type = in.readInt();
        this.findMore = in.readByte() != 0;
    }

    public static final Creator<MayBeInterest> CREATOR = new Creator<MayBeInterest>() {
        @Override
        public MayBeInterest createFromParcel(Parcel source) {
            return new MayBeInterest(source);
        }

        @Override
        public MayBeInterest[] newArray(int size) {
            return new MayBeInterest[size];
        }
    };

    public boolean isAttention_state() {
        return attention_state==1;
    }

    public void setAttention_state(boolean b) {
        attention_state=b?1:0;
    }
}
