package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LiuXiang on 2016/12/12.
 */
public class DiscoveryLabelObj implements Parcelable{
    //用户标签
    private String label_id;
    private String label_name;
    private String label_img_uri;
    private String img_uri;
    private int is_select;
    //发现-分类
    private String kind_name;
    private String kind_command;
    private String kind_tag;

    protected DiscoveryLabelObj(Parcel in) {
        label_id = in.readString();
        label_name = in.readString();
        label_img_uri = in.readString();
        img_uri = in.readString();
        is_select = in.readInt();
        kind_name = in.readString();
        kind_command = in.readString();
        kind_tag = in.readString();
    }

    public static final Creator<DiscoveryLabelObj> CREATOR = new Creator<DiscoveryLabelObj>() {
        @Override
        public DiscoveryLabelObj createFromParcel(Parcel in) {
            return new DiscoveryLabelObj(in);
        }

        @Override
        public DiscoveryLabelObj[] newArray(int size) {
            return new DiscoveryLabelObj[size];
        }
    };

    public String getLabel_id() {
        return label_id;
    }

    public String getLabel_name() {
        return label_name;
    }

    public String getLabel_img_uri() {
        return label_img_uri;
    }

    public String getImg_uri() {
        return img_uri;
    }

    public int getIs_select() {
        return is_select;
    }

    public String getKind_name() {
        return kind_name;
    }

    public String getKind_Command() {
        return kind_command;
    }

    public String getKind_tag() {
        return kind_tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label_id);
        dest.writeString(label_name);
        dest.writeString(label_img_uri);
        dest.writeString(img_uri);
        dest.writeInt(is_select);
        dest.writeString(kind_name);
        dest.writeString(kind_command);
        dest.writeString(kind_tag);
    }

    public void setIs_select(int is_select) {
        this.is_select = is_select;
    }
}
