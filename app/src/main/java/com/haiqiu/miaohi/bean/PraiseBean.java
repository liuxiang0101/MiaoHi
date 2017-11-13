package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ningl on 16/12/21.
 */

public class PraiseBean implements Parcelable {

    private String user_id;
    private String portrait_url;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPortrait_url() {
        return portrait_url;
    }

    public void setPortrait_url(String portrait_url) {
        this.portrait_url = portrait_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeString(this.portrait_url);
    }

    public PraiseBean() {
    }

    protected PraiseBean(Parcel in) {
        this.user_id = in.readString();
        this.portrait_url = in.readString();
    }

    public static final Parcelable.Creator<PraiseBean> CREATOR = new Parcelable.Creator<PraiseBean>() {
        @Override
        public PraiseBean createFromParcel(Parcel source) {
            return new PraiseBean(source);
        }

        @Override
        public PraiseBean[] newArray(int size) {
            return new PraiseBean[size];
        }
    };
}
