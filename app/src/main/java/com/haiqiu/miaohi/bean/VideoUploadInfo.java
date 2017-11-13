package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 草稿箱
 */
public class VideoUploadInfo implements Parcelable {
    public final static String UPDATE_QUESTION_STATE_ACTION = "update_question_state_action";

    public final static int FROM_COMMON = 0;//来自一般拍摄流程
    public final static int FROM_ASK_AND_ANSWER = 1;//来自映答
    public final static int FROM_DRAFTS = 2;//来自草稿箱
    public final static int FROM_ACTIVITY = 3;//来自活动

    public final static int VIDEO_TYPE_LOCAL = 10;//本地视频
    public final static int VIDEO_TYPE_RECORDER = 0;//录制的视频


    public final static int VIDEO_UPLOAD_SUCCESS = 10;
    public final static int VIDEO_DRAFTS_DELETE = 11;
    public final static int VIDEO_UPLOAD_PRE = 12;

    /*上传状态*/
    public static final int UPLOAD_NORMAL = 0;     //开始上传
    public static final int UPLOAD_PRE = 1;     //开始上传
    public static final int UPLOAD_SUCCESS = 2; //上传成功
    public static final int UPLOAD_FAILE = 3;   //上传失败
    public static final int UPLOAD_PROGRESS = 4;//上传中刷新进度
    public static final int UPLOAD_FINISH = 5;  //上传完成


    //所有文件的父目录
    private String filesParent;

    private String videoPreviewImagePath;
    private String videoPath;
    private String videoSrcPath;//视频的原始地址
    private long videoDuration;//视频时长
    private int videoWidth;
    private int videoHeight;

    private String name;
    private long saveTime;//存入的时间
    private String questionId;//问题id
    private int fromInfo;//1,表示来自草稿箱,2表示来自映答
    private String questionStartTime;//问题起始时间
    private long questionEndTime;//问题结束时间
    private String question_text;

    //草稿类型 0:普通录制手动存入草稿箱
    //1：普通录制外因失败存入草稿箱
    //2：映答录制手动存入草稿箱
    //3：映答录制外因失败存入草稿箱

    private String activity_id;
    private String activity_name;
    private String videoId;
    private String videoUrl;
    private String pictureUrl;
    private String uploadUserName;
    private String serviceImageUrl;
    private String videoNote;
    private int videoType;
    private boolean isUploadFail;

    public static final int DRAFTS_UPLOAD_FAIL = 0;//草稿箱上传失败
    public static final int DRAFTS_UNUPLOAD = 1;//草稿箱未上传
    //0普通录制手动存入草稿箱
    //1普通录制外因失败存入草稿箱
    //2映答录制手动存入草稿箱
    //3/映答录制外因失败存入草稿箱

    private ArrayList<String> notifyUserId;
    private ArrayList<String> notifyUserName;

    private int upLoadType;//1是私有视频


    private double prosess;//上传进度
    private int uploadState;//上传状态

    private String saveUserId;

    private int videoSrcWidth;

    private MediaType mediaType = MediaType.MEDIA_TYPE_VIDEO;//上传的类型,图片/视频 详见:MEDIA_TYPE_

    private String picturePath;
    private String pictureSrcPath;//图片原始地址
    private int pictureWidth;
    private int pictureHeight;

    private String extraFilterParam;//附加的滤镜参数

    private transient ArrayList<VideoRecorderObject> videoList = new ArrayList<>();//分段视频的信息

    private UserInfo userInfo;

    private List<Notify_user_result> notify_user_results;
    private String localPicturePath;
    private String localCoverPath;
    private String coverUrl;
    private String photoId;
    private String share_link_address;
    private boolean isUploading;

    public boolean isUploading() {
        return isUploading;
    }

    public void setUploading(boolean uploading) {
        isUploading = uploading;
    }

    public String getShare_link_address() {
        return share_link_address;
    }

    public void setShare_link_address(String share_link_address) {
        this.share_link_address = share_link_address;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getLocalPicturePath() {
        return localPicturePath;
    }

    public void setLocalPicturePath(String localPicturePath) {
        this.localPicturePath = localPicturePath;
    }

    public String getLocalCoverPath() {
        return localCoverPath;
    }

    public void setLocalCoverPath(String localCoverPath) {
        this.localCoverPath = localCoverPath;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }


    public List<Notify_user_result> getNotify_user_results() {
        return notify_user_results;
    }

    public void setNotify_user_results(List<Notify_user_result> notify_user_results) {
        this.notify_user_results = notify_user_results;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * 注意不能把get与set方法删除了,然后重新生成,有的有特殊处理
     */
    public String getSaveUserId() {
        return saveUserId;
    }

    public void setSaveUserId(String saveUserId) {
        this.saveUserId = saveUserId;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }


    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionStartTime() {
        return questionStartTime;
    }

    public void setQuestionStartTime(String questionStartTime) {
        this.questionStartTime = questionStartTime;
    }

    public long getQuestionEndTime() {
        return questionEndTime;
    }

    public void setQuestionEndTime(long questionEndTime) {
        this.questionEndTime = questionEndTime;
    }


    public String getVideoPreviewImagePath() {
        return videoPreviewImagePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public String getName() {
        return name;
    }

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    public void setVideoPreviewImagePath(String videoPreviewImagePath) {
        this.videoPreviewImagePath = videoPreviewImagePath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getFromInfo() {
        return fromInfo;
    }

    public void setFromInfo(int fromInfo) {
        this.fromInfo = fromInfo;
    }


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

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUploadUserName() {
        return uploadUserName;
    }

    public void setUploadUserName(String uploadUserName) {
        this.uploadUserName = uploadUserName;
    }

    public String getServiceImageUrl() {
        return serviceImageUrl;
    }

    public void setServiceImageUrl(String serviceImageUrl) {
        this.serviceImageUrl = serviceImageUrl;
    }

    public String getFilesParent() {
        return filesParent;
    }

    public void setFilesParent(String filesParent) {
        this.filesParent = filesParent;
    }

    public String getVideoNote() {
        return videoNote;
    }

    public void setVideoNote(String videoNote) {
        this.videoNote = videoNote;
    }

    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }


    public ArrayList<String> getNotifyUserId() {
        if (null == notifyUserId) notifyUserId = new ArrayList<>();
        return notifyUserId;
    }

    public void addNotifyUserId(String notifyUserId) {
        if (null == notifyUserId) return;

        ArrayList<String> notifySrc = getNotifyUserId();
        //去重逻辑
//        boolean hasUser = false;
//        for (String userId : notifySrc) {
//            if (notifyUserId.equals(userId)) {
//                hasUser = true;
//                break;
//            }
//        }
//        if (!hasUser)
        notifySrc.add(notifyUserId);

    }

    public ArrayList<String> getNotifyUserName() {
        if (null == notifyUserName) notifyUserName = new ArrayList<>();
        return notifyUserName;
    }

    public void addNotifyUserName(String notifyUserName) {
        if (null == notifyUserName) return;
        ArrayList<String> notifyNameSrc = getNotifyUserName();
        //去重逻辑
//        boolean hasUser = false;
//        for (String userId : notifyNameSrc) {
//            if (notifyUserName.equals(userId)) {
//                hasUser = true;
//                break;
//            }
//        }
//        if (!hasUser)
        notifyNameSrc.add(notifyUserName);

    }

    public int getUpLoadType() {
        return upLoadType;
    }

    public void setUpLoadType(int upLoadType) {
        this.upLoadType = upLoadType;
    }


    public long getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(long videoDuration) {
        this.videoDuration = videoDuration;
    }

    public double getProsess() {
        return prosess;
    }

    public void setProsess(double prosess) {
        this.prosess = prosess;
    }

    public int getUploadState() {
        return uploadState;
    }

    public void setUploadState(int uploadState) {
        this.uploadState = uploadState;
    }

    public String getVideoSrcPath() {
        return videoSrcPath;
    }

    public void setVideoSrcPath(String videoSrcPath) {
        this.videoSrcPath = videoSrcPath;
    }

    public int getVideoSrcWidth() {
        return videoSrcWidth;
    }

    public void setVideoSrcWidth(int videoSrcWidth) {
        this.videoSrcWidth = videoSrcWidth;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getPictureWidth() {
        return pictureWidth;
    }

    public void setPictureWidth(int pictureWidth) {
        this.pictureWidth = pictureWidth;
    }

    public int getPictureHeight() {
        return pictureHeight;
    }

    public void setPictureHeight(int pictureHeight) {
        this.pictureHeight = pictureHeight;
    }

    public ArrayList<VideoRecorderObject> getVideoList() {
        return videoList;
    }

    public String getExtraFilterParam() {
        if (null == extraFilterParam) extraFilterParam = "";
        return extraFilterParam;
    }

    public void setExtraFilterParam(String extraFilterParam) {
        this.extraFilterParam = extraFilterParam;
    }

    public String getPictureSrcPath() {
        return pictureSrcPath;
    }

    public void setPictureSrcPath(String pictureSrcPath) {
        this.pictureSrcPath = pictureSrcPath;
    }

    public boolean isUploadFail() {
        return isUploadFail;
    }

    public void setUploadFail(boolean uploadFail) {
        isUploadFail = uploadFail;
    }


    public enum MediaType {
        MEDIA_TYPE_VIDEO, MEDIA_TYPE_PICTURE
    }


    //重新序列化的时候一定要videoList手动加进去, 切记!!!!!
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.filesParent);
        dest.writeString(this.videoPreviewImagePath);
        dest.writeString(this.videoPath);
        dest.writeString(this.videoSrcPath);
        dest.writeLong(this.videoDuration);
        dest.writeInt(this.videoWidth);
        dest.writeInt(this.videoHeight);
        dest.writeString(this.name);
        dest.writeLong(this.saveTime);
        dest.writeString(this.questionId);
        dest.writeInt(this.fromInfo);
        dest.writeString(this.questionStartTime);
        dest.writeLong(this.questionEndTime);
        dest.writeString(this.question_text);
        dest.writeString(this.activity_id);
        dest.writeString(this.activity_name);
        dest.writeString(this.videoId);
        dest.writeString(this.videoUrl);
        dest.writeString(this.pictureUrl);
        dest.writeString(this.uploadUserName);
        dest.writeString(this.serviceImageUrl);
        dest.writeString(this.videoNote);
        dest.writeInt(this.videoType);
        dest.writeByte(this.isUploadFail ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.notifyUserId);
        dest.writeStringList(this.notifyUserName);
        dest.writeInt(this.upLoadType);
        dest.writeDouble(this.prosess);
        dest.writeInt(this.uploadState);
        dest.writeString(this.saveUserId);
        dest.writeInt(this.videoSrcWidth);
        dest.writeInt(this.mediaType == null ? -1 : this.mediaType.ordinal());
        dest.writeString(this.picturePath);
        dest.writeString(this.pictureSrcPath);
        dest.writeInt(this.pictureWidth);
        dest.writeInt(this.pictureHeight);
        dest.writeString(this.extraFilterParam);
        dest.writeParcelable(this.userInfo, flags);
        dest.writeTypedList(this.notify_user_results);
        dest.writeString(this.localPicturePath);
        dest.writeString(this.localCoverPath);
        dest.writeString(this.coverUrl);
        dest.writeString(this.photoId);
        dest.writeString(this.share_link_address);
        dest.writeByte(this.isUploading ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.videoList);
    }

    public VideoUploadInfo() {
    }

    protected VideoUploadInfo(Parcel in) {
        this.filesParent = in.readString();
        this.videoPreviewImagePath = in.readString();
        this.videoPath = in.readString();
        this.videoSrcPath = in.readString();
        this.videoDuration = in.readLong();
        this.videoWidth = in.readInt();
        this.videoHeight = in.readInt();
        this.name = in.readString();
        this.saveTime = in.readLong();
        this.questionId = in.readString();
        this.fromInfo = in.readInt();
        this.questionStartTime = in.readString();
        this.questionEndTime = in.readLong();
        this.question_text = in.readString();
        this.activity_id = in.readString();
        this.activity_name = in.readString();
        this.videoId = in.readString();
        this.videoUrl = in.readString();
        this.pictureUrl = in.readString();
        this.uploadUserName = in.readString();
        this.serviceImageUrl = in.readString();
        this.videoNote = in.readString();
        this.videoType = in.readInt();
        this.isUploadFail = in.readByte() != 0;
        this.notifyUserId = in.createStringArrayList();
        this.notifyUserName = in.createStringArrayList();
        this.upLoadType = in.readInt();
        this.prosess = in.readDouble();
        this.uploadState = in.readInt();
        this.saveUserId = in.readString();
        this.videoSrcWidth = in.readInt();
        int tmpMediaType = in.readInt();
        this.mediaType = tmpMediaType == -1 ? null : MediaType.values()[tmpMediaType];
        this.picturePath = in.readString();
        this.pictureSrcPath = in.readString();
        this.pictureWidth = in.readInt();
        this.pictureHeight = in.readInt();
        this.extraFilterParam = in.readString();
        this.userInfo = in.readParcelable(UserInfo.class.getClassLoader());
        this.notify_user_results = in.createTypedArrayList(Notify_user_result.CREATOR);
        this.localPicturePath = in.readString();
        this.localCoverPath = in.readString();
        this.coverUrl = in.readString();
        this.photoId = in.readString();
        this.share_link_address = in.readString();
        this.isUploading = in.readByte() != 0;
        this.videoList = in.createTypedArrayList(VideoRecorderObject.CREATOR);
    }

    public static final Creator<VideoUploadInfo> CREATOR = new Creator<VideoUploadInfo>() {
        @Override
        public VideoUploadInfo createFromParcel(Parcel source) {
            return new VideoUploadInfo(source);
        }

        @Override
        public VideoUploadInfo[] newArray(int size) {
            return new VideoUploadInfo[size];
        }
    };
}
