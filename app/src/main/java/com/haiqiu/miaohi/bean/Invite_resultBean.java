package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 邀请实体类
 * Created by ningl on 2016/6/22.
 */
public class Invite_resultBean implements Parcelable {


    /**
     * activity_id : xxxx
     * activity_name : xxxx
     * sender_id : xxxx
     * sender_name : xxxx
     * receiver_id : xxxx
     * receiver_name : xxxx
     */

    private String activity_id;
    private String activity_name;
    private String sender_id;
    private String sender_name;
    private String recever_id;
    private String receiver_name;

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

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getRecever_id() {
        return recever_id;
    }

    public void setRecever_id(String recever_id) {
        this.recever_id = recever_id;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {

        this.receiver_name = receiver_name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.activity_id);
        dest.writeString(this.activity_name);
        dest.writeString(this.sender_id);
        dest.writeString(this.sender_name);
        dest.writeString(this.recever_id);
        dest.writeString(this.receiver_name);
    }

    public Invite_resultBean() {
    }

    protected Invite_resultBean(Parcel in) {
        this.activity_id = in.readString();
        this.activity_name = in.readString();
        this.sender_id = in.readString();
        this.sender_name = in.readString();
        this.recever_id = in.readString();
        this.receiver_name = in.readString();
    }

    public static final Creator<Invite_resultBean> CREATOR = new Creator<Invite_resultBean>() {
        @Override
        public Invite_resultBean createFromParcel(Parcel source) {
            return new Invite_resultBean(source);
        }

        @Override
        public Invite_resultBean[] newArray(int size) {
            return new Invite_resultBean[size];
        }
    };
}
