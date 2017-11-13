package com.haiqiu.miaohi.bean;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hackest on 16/7/4.
 */
public class ChannelItem implements Parcelable {

    private String kind_tag;
    private String toolbar_title;
    private String position_index;

    private String toolbar_command;
    private String icon_state;
    private String icon_uri;

    private boolean fix_mark;

    private boolean isDefaultSelect;


    public String getKind_tag() {
        return kind_tag;
    }


    public String getToolbar_title() {
        return toolbar_title;
    }


    public String getPosition_index() {
        return position_index;
    }

    public void setPosition_index(String position_index) {
        this.position_index = position_index;
    }

    public String getToolbar_command() {
        return toolbar_command;
    }


    public String getIcon_state() {
        return icon_state;
    }


    public String getIcon_uri() {
        return icon_uri;
    }


    public boolean isDefaultSelect() {
        return isDefaultSelect;
    }

    public void setDefaultSelect(boolean defaultSelect) {
        isDefaultSelect = defaultSelect;
    }

    public boolean isFix_mark() {
        return fix_mark;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.kind_tag);
        dest.writeString(this.toolbar_title);
        dest.writeString(this.position_index);
        dest.writeString(this.toolbar_command);
        dest.writeString(this.icon_state);
        dest.writeString(this.icon_uri);
        dest.writeByte(this.fix_mark ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDefaultSelect ? (byte) 1 : (byte) 0);
    }

    public ChannelItem() {
    }

    protected ChannelItem(Parcel in) {
        this.kind_tag = in.readString();
        this.toolbar_title = in.readString();
        this.position_index = in.readString();
        this.toolbar_command = in.readString();
        this.icon_state = in.readString();
        this.icon_uri = in.readString();
        this.fix_mark = in.readByte() != 0;
        this.isDefaultSelect = in.readByte() != 0;
    }

    public static final Creator<ChannelItem> CREATOR = new Creator<ChannelItem>() {
        @Override
        public ChannelItem createFromParcel(Parcel source) {
            return new ChannelItem(source);
        }

        @Override
        public ChannelItem[] newArray(int size) {
            return new ChannelItem[size];
        }
    };
}
