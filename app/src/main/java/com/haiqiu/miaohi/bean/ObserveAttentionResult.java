package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ningl on 16/9/21.
 */
public class ObserveAttentionResult implements Parcelable{


    /**
     * observe_user_id : xxxxx
     * observe_user_type : xxxxx
     * observe_user_name : xxxxx
     * observe_portrait_uri : xxxxx
     * observe_portrait_state : xxxx
     * observe_video_time : xxxxx
     */

    private String observe_user_id;
    private int observe_user_type;
    private String observe_user_name;
    private String observe_portrait_uri;
    private int observe_portrait_state;
    private String observe_video_time;
    private String observe_video_time_text;

    public String getObserve_video_time_text() {
        return observe_video_time_text;
    }

    public void setObserve_video_time_text(String observe_video_time_text) {
        this.observe_video_time_text = observe_video_time_text;
    }

    public String getObserve_user_id() {
        return observe_user_id;
    }

    public void setObserve_user_id(String observe_user_id) {
        this.observe_user_id = observe_user_id;
    }

    public int getObserve_user_type() {
        return observe_user_type;
    }

    public void setObserve_user_type(int observe_user_type) {
        this.observe_user_type = observe_user_type;
    }

    public String getObserve_user_name() {
        return observe_user_name;
    }

    public void setObserve_user_name(String observe_user_name) {
        this.observe_user_name = observe_user_name;
    }

    public String getObserve_portrait_uri() {
        return observe_portrait_uri;
    }

    public void setObserve_portrait_uri(String observe_portrait_uri) {
        this.observe_portrait_uri = observe_portrait_uri;
    }

    public int getObserve_portrait_state() {
        return observe_portrait_state;
    }

    public void setObserve_portrait_state(int observe_portrait_state) {
        this.observe_portrait_state = observe_portrait_state;
    }

    public String getObserve_video_time() {
        return observe_video_time;
    }

    public void setObserve_video_time(String observe_video_time) {
        this.observe_video_time = observe_video_time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.observe_user_id);
        dest.writeInt(this.observe_user_type);
        dest.writeString(this.observe_user_name);
        dest.writeString(this.observe_portrait_uri);
        dest.writeInt(this.observe_portrait_state);
        dest.writeString(this.observe_video_time);
        dest.writeString(this.observe_video_time_text);
    }

    public ObserveAttentionResult() {
    }

    protected ObserveAttentionResult(Parcel in) {
        this.observe_user_id = in.readString();
        this.observe_user_type = in.readInt();
        this.observe_user_name = in.readString();
        this.observe_portrait_uri = in.readString();
        this.observe_portrait_state = in.readInt();
        this.observe_video_time = in.readString();
        this.observe_video_time_text = in.readString();
    }

    public static final Creator<ObserveAttentionResult> CREATOR = new Creator<ObserveAttentionResult>() {
        @Override
        public ObserveAttentionResult createFromParcel(Parcel source) {
            return new ObserveAttentionResult(source);
        }

        @Override
        public ObserveAttentionResult[] newArray(int size) {
            return new ObserveAttentionResult[size];
        }
    };
}
