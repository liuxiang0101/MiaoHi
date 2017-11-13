package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ningl on 16/10/14.
 */
public class Notify_user_result implements Parcelable {


    /**
     * notify_user_name : 蓝色
     * notify_user_id : USER-0040f9a0-3e79-11e6-ae3b-5cb9018949c8
     */

    private String notify_user_name;
    private String notify_user_id;


    public String getNotify_user_name() {
        return notify_user_name;
    }

    public void setNotify_user_name(String notify_user_name) {
        this.notify_user_name = notify_user_name;
    }

    public String getNotify_user_id() {
        return notify_user_id;
    }

    public void setNotify_user_id(String notify_user_id) {
        this.notify_user_id = notify_user_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.notify_user_name);
        dest.writeString(this.notify_user_id);
    }

    public Notify_user_result() {
    }

    protected Notify_user_result(Parcel in) {
        this.notify_user_name = in.readString();
        this.notify_user_id = in.readString();
    }

    public static final Creator<Notify_user_result> CREATOR = new Creator<Notify_user_result>() {
        @Override
        public Notify_user_result createFromParcel(Parcel source) {
            return new Notify_user_result(source);
        }

        @Override
        public Notify_user_result[] newArray(int size) {
            return new Notify_user_result[size];
        }
    };
}
