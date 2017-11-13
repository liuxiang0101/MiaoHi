package com.haiqiu.miaohi.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by zhandalin on 2016-11-28 12:02.
 * 说明:
 */
public class VideoRecorderObject implements Parcelable {
    private long duration;//视频时长

    private String videoTsPath;//TS保存地址

    private String finalTsPath;

    private String videoMp4Path;//MP4保存地址

    private FilterInfo filterInfo;
    private DecalInfo decalInfo;

    private View decalContentView;//贴纸View
    private transient Bitmap decalBitmap;//贴纸最终的bitmap


    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getVideoTsPath() {
        return videoTsPath;
    }

    public void setVideoTsPath(String videoTsPath) {
        this.videoTsPath = videoTsPath;
    }

    public String getVideoMp4Path() {
        return videoMp4Path;
    }

    public void setVideoMp4Path(String videoMp4Path) {
        this.videoMp4Path = videoMp4Path;
    }

    public FilterInfo getFilterInfo() {
        return filterInfo;
    }

    public void setFilterInfo(FilterInfo filterInfo) {
        this.filterInfo = filterInfo;
    }

    public DecalInfo getDecalInfo() {
        return decalInfo;
    }

    public void setDecalInfo(DecalInfo decalInfo) {
        this.decalInfo = decalInfo;
    }

    public String getFinalTsPath() {
        return finalTsPath;
    }

    public void setFinalTsPath(String finalTsPath) {
        this.finalTsPath = finalTsPath;
    }

    public View getDecalContentView() {
        return decalContentView;
    }

    public void setDecalContentView(View decalContentView) {
        this.decalContentView = decalContentView;
    }

    public Bitmap getDecalBitmap() {
        return decalBitmap;
    }

    public void setDecalBitmap(Bitmap decalBitmap) {
        this.decalBitmap = decalBitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.duration);
        dest.writeString(this.videoTsPath);
        dest.writeString(this.videoMp4Path);
    }

    public VideoRecorderObject() {
    }

    protected VideoRecorderObject(Parcel in) {
        this.duration = in.readLong();
        this.videoTsPath = in.readString();
        this.videoMp4Path = in.readString();
    }

    public static final Creator<VideoRecorderObject> CREATOR = new Creator<VideoRecorderObject>() {
        @Override
        public VideoRecorderObject createFromParcel(Parcel source) {
            return new VideoRecorderObject(source);
        }

        @Override
        public VideoRecorderObject[] newArray(int size) {
            return new VideoRecorderObject[size];
        }
    };
}
