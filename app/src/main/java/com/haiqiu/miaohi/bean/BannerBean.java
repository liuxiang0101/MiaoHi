package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Banner数据
 * Created by ningl on 16/10/28.
 */
public class BannerBean implements Parcelable{
    private String banner_id;
    private String banner_uri;
    private int banner_state;
    private int banner_width;
    private int banner_height;
    private String recommend;
    private String target_name;
    private String target_content;
    private String target_content_extra;

    protected BannerBean(Parcel in) {
        banner_id = in.readString();
        banner_uri = in.readString();
        banner_state = in.readInt();
        banner_width = in.readInt();
        banner_height = in.readInt();
        recommend = in.readString();
        target_name = in.readString();
        target_content = in.readString();
        target_content_extra = in.readString();
    }

    public static final Creator<BannerBean> CREATOR = new Creator<BannerBean>() {
        @Override
        public BannerBean createFromParcel(Parcel in) {
            return new BannerBean(in);
        }

        @Override
        public BannerBean[] newArray(int size) {
            return new BannerBean[size];
        }
    };

    public String getTarget_content_extra() {
        return target_content_extra;
    }

    public void setTarget_content_extra(String target_content_extra) {
        this.target_content_extra = target_content_extra;
    }

    public String getBanner_uri() {
        return banner_uri;
    }


    public int getBanner_width() {
        return banner_width;
    }


    public int getBanner_height() {
        return banner_height;
    }


    public String getTarget_name() {
        return target_name;
    }


    public String getTarget_content() {
        return target_content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(banner_id);
        dest.writeString(banner_uri);
        dest.writeInt(banner_state);
        dest.writeInt(banner_width);
        dest.writeInt(banner_height);
        dest.writeString(recommend);
        dest.writeString(target_name);
        dest.writeString(target_content);
        dest.writeString(target_content_extra);
    }
}
