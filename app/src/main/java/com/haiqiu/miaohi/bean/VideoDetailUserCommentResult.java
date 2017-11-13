package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 视频详情评论返回结果实体类
 * Created by ningl on 2016/6/22.
 */
public class VideoDetailUserCommentResult implements Parcelable {

    private String comments_count;
    private String vip_comment_count;
    private List<VideoDetailUserCommentBean> page_result;

    public String getComments_count() {
        return comments_count;
    }

    public void setComments_count(String comments_count) {
        this.comments_count = comments_count;
    }

    public List<VideoDetailUserCommentBean> getPage_result() {
        return page_result;
    }

    public void setPage_result(List<VideoDetailUserCommentBean> page_result) {
        this.page_result = page_result;
    }

    public String getVip_comment_count() {
        return vip_comment_count;
    }

    public void setVip_comment_count(String vip_comment_count) {
        this.vip_comment_count = vip_comment_count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.comments_count);
        dest.writeString(this.vip_comment_count);
        dest.writeTypedList(this.page_result);
    }

    public VideoDetailUserCommentResult() {
    }

    protected VideoDetailUserCommentResult(Parcel in) {
        this.comments_count = in.readString();
        this.vip_comment_count = in.readString();
        this.page_result = in.createTypedArrayList(VideoDetailUserCommentBean.CREATOR);
    }

    public static final Parcelable.Creator<VideoDetailUserCommentResult> CREATOR = new Parcelable.Creator<VideoDetailUserCommentResult>() {
        @Override
        public VideoDetailUserCommentResult createFromParcel(Parcel source) {
            return new VideoDetailUserCommentResult(source);
        }

        @Override
        public VideoDetailUserCommentResult[] newArray(int size) {
            return new VideoDetailUserCommentResult[size];
        }
    };
}
