package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;

import java.util.List;

/**
 * Created by ningl on 16/12/15.
 */
public class Attention extends VideoAndImg implements Parcelable{


    /**
     * answer_user_name : 小智嫂子是从什么时候
     * cover_uri : http://image.dev.miaohi.com/64C3A41F18B3014CD42FA8D59C54E137_2016_09_28_11_25_42_62vedioCoverImage
     * videoDuration : 09.28/11:25
     * answer_portrait_state : 10
     * observe_count : 4
     * observe_state : 1
     * element_type : 3
     * question_id : QUST-6f0bc9ba-8523-11e6-b0c9-44a8424640fa
     * answer_user_type : 20
     * answer_portrait_uri : http://icon.dev.miaohi.com//iconImage_beSm3c2KM5icNb85HXKzcdPSDxAdZhpm_2016_11_10_19_56_14
     * question_text : ……你是一直以为的以为不管时间
     * cover_state : 10
     * video_url : http://video.dev.miaohi.com/videozDstTm7sPrEdCPbsjRRhKHhNCRSadatn2016_10_21_11_12_25Movie
     * shared_tag_text : #秒嗨狂送500W#
     * answer_user_id : USER-6aa3f04a-74af-11e6-a51b-44a8424640fa
     * video_id : ANVI-42a1caea-852b-11e6-b0c9-44a8424640fa
     */

    private String answer_user_name;
    private String cover_uri;
    private String video_time_text;
    private int answer_portrait_state;
    private int observe_count;
    private int observe_state;
    private String question_id;
    private String answer_user_portrait;
    private int answer_user_type;
    private String question_text;
    private int cover_state;
    private String video_url;
    private String shared_tag_text;
    private String answer_user_id;
    private String video_note;
    /**
     * comment_count : 10
     * portrait_state : 10
     * user_name : xxx
     * video_cover_uri : http://image.dev.miaohi.com/img_379a78ffe134ace11f0b9888985a3182_2016_12_12_15_04_46_040
     * video_uri : http://video.dev.miaohi.com/video_379a78ffe134ace11f0b9888985a3182_2016_12_12_15_04_45_990_EXWM.mp4
     * user_type : 20
     * user_id : USER-370f34ff-846b-11e6-b0c9-44a8424640fa
     * element_type: 1
     */

    private int comments_count;
    private int portrait_state;
    private String user_name;
    private String video_cover_uri;
    private String video_uri;
    private int user_type;
    private List<Notify_user_result> notify_user_result;
    private List<Comment> comment_list;
    /**
     * photo_id : xxxxx
     * photo_thumb_url : xxxxx
     * photo_url : xxxx
     * photo_note : xxxx
     * activity_id : xxxx
     * activity_name : xxxx
     * upload_time_text : xxxx
     * portrait_uri : xxxxx
     * praise_state : xxxx
     * praise_count : xxxx
     * attention_state : xxx
     * element_type:2
     */

    private String photo_thumb_url;
    private String photo_uri;
    private String photo_note;
    private String activity_id;
    private String activity_name;
    private String activity_uri;

    private String upload_time_text;
    private String portrait_uri;
    private String user_note;
    private String username;
    private String portait_state;
    private int video_state;

    private boolean praising;
    private double width;
    private double height;
    private String recommend_text;
    private boolean isShowItemMarsk;
    private boolean is_question_owner;
    private boolean temporary_free;
    private String observe_price_text;
    private boolean showQaBtn = true;
    private boolean PlayNow;//是否立即播放
    private String share_link_address;
    private boolean isUploading;
    private String videoSrcPath;
    private String photoSrcPath;
    private long observe_price;
    private int uploadState;
    private double progress;
    private String user_authentic;

    private long duration_second;
    private long play_total;
    private long answer_time;
    private long upload_time;
    private long time_remain;
    private int answer_user_gender;

    public int getAnswer_user_gender() {
        return answer_user_gender;
    }

    public void setAnswer_user_gender(int answer_user_gender) {
        this.answer_user_gender = answer_user_gender;
    }

    public long getTime_remain() {
        return time_remain;
    }

    public void setTime_remain(long time_remain) {
        this.time_remain = time_remain;
    }

    public long getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(long upload_time) {
        this.upload_time = upload_time;
    }

    public long getAnswer_time() {
        return answer_time;
    }

    public void setAnswer_time(long answer_time) {
        this.answer_time = answer_time;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public int getUploadState() {
        return uploadState;
    }

    public void setUploadState(int uploadState) {
        this.uploadState = uploadState;
    }

    public String getPhoto_uri() {
        return photo_uri;
    }

    public void setPhoto_uri(String photo_uri) {
        this.photo_uri = photo_uri;
    }

    public long getObserve_price() {
        return observe_price;
    }

    public void setObserve_price(long observe_price) {
        this.observe_price = observe_price;
    }

    public String getVideoSrcPath() {
        return videoSrcPath;
    }

    public void setVideoSrcPath(String videoSrcPath) {
        this.videoSrcPath = videoSrcPath;
    }

    public String getPhotoSrcPath() {
        return photoSrcPath;
    }

    public void setPhotoSrcPath(String photoSrcPath) {
        this.photoSrcPath = photoSrcPath;
    }

    public boolean isUploading() {
        return isUploading;
    }

    public void setUploading(boolean uploading) {
        isUploading = uploading;
    }

    public String getShare_link_address() {
        if(MHStringUtils.isEmpty(share_link_address)){
            return ConstantsValue.Shared.APPDOWNLOAD;
        }
        return share_link_address;
    }

    public void setShare_link_address(String share_link_address) {
        this.share_link_address = share_link_address;
    }

    public String getAnswer_user_portrait() {
        return answer_user_portrait;
    }

    public void setAnswer_user_portrait(String answer_user_portrait) {
        this.answer_user_portrait = answer_user_portrait;
    }

    public boolean isPlayNow() {
        return PlayNow;
    }

    public void setPlayNow(boolean playNow) {
        PlayNow = playNow;
    }

    public boolean isShowQaBtn() {
        return showQaBtn;
    }

    public void setShowQaBtn(boolean showQaBtn) {
        this.showQaBtn = showQaBtn;
    }

    public String getObserve_price_text() {
        return observe_price_text;
    }

    public void setObserve_price_text(String observe_price_text) {
        this.observe_price_text = observe_price_text;
    }

    public boolean is_question_owner() {
        return is_question_owner;
    }

    public void setIs_question_owner(boolean is_question_owner) {
        this.is_question_owner = is_question_owner;
    }

    public boolean isTemporary_free() {
        return temporary_free;
    }

    public void setTemporary_free(boolean temporary_free) {
        this.temporary_free = temporary_free;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public boolean isShowItemMarsk() {
        return isShowItemMarsk;
    }

    public void setShowItemMarsk(boolean showItemMarsk) {
        isShowItemMarsk = showItemMarsk;
    }

    public String getRecommend_text() {
        return recommend_text;
    }

    public void setRecommend_text(String recommend_text) {
        this.recommend_text = recommend_text;
    }

    public long getDuration_second() {
        return duration_second;
    }

    public void setDuration_second(long duration_second) {
        this.duration_second = duration_second;
    }

    public long getPlay_total() {
        return play_total;
    }

    public void setPlay_total(long play_total) {
        this.play_total = play_total;
    }

    /**
     * 上传相关
     */
//    private VideoUploadInfo uploadinfo;
    private String localPicturePath;
    private String localCoverPath;

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

//    public VideoUploadInfo getUploadinfo() {
//        return uploadinfo;
//    }
//
//    public void setUploadinfo(VideoUploadInfo uploadinfo) {
//        this.uploadinfo = uploadinfo;
//    }

    private List<MayBeInterest> attention_user_list;

    public List<MayBeInterest> getAttention_user_list() {
        return attention_user_list;
    }

    public void setAttention_user_list(List<MayBeInterest> attention_user_list) {
        this.attention_user_list = attention_user_list;
    }

    public int getVideo_state() {
        return video_state;
    }

    public void setVideo_state(int video_state) {
        this.video_state = video_state;
    }

    public String getUser_note() {
        return user_note;
    }

    public void setUser_note(String user_note) {
        this.user_note = user_note;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPortait_state() {
        return portait_state;
    }

    public void setPortait_state(String portait_state) {
        this.portait_state = portait_state;
    }

    public String getUser_authentic() {
        return user_authentic;
    }

    public void setUser_authentic(String user_authentic) {
        this.user_authentic = user_authentic;
    }

    public String getVideo_note() {
        return video_note;
    }

    public void setVideo_note(String video_note) {
        this.video_note = video_note;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isPraising() {
        return praising;
    }

    public void setPraising(boolean praising) {
        this.praising = praising;
    }

    public List<Notify_user_result> getNotify_user_result() {
        return notify_user_result;
    }

    public void setNotify_user_result(List<Notify_user_result> notify_user_result) {
        this.notify_user_result = notify_user_result;
    }

    public List<Comment> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<Comment> comment_list) {
        this.comment_list = comment_list;
    }

    public String getAnswer_user_name() {
        return answer_user_name;
    }

    public void setAnswer_user_name(String answer_user_name) {
        this.answer_user_name = answer_user_name;
    }

    public String getCover_uri() {
        return cover_uri;
    }

    public void setCover_uri(String cover_uri) {
        this.cover_uri = cover_uri;
    }

    public String getVideo_time_text() {
        return video_time_text;
    }

    public void setVideo_time_text(String video_time_text) {
        this.video_time_text = video_time_text;
    }

    public String getObserve_count() {
        return CommonUtil.formatCount(observe_count);
    }

    public int getObserveSrcCount() {
        return observe_count;
    }

    public void setObserve_count(int observe_count) {
        this.observe_count = observe_count;
    }



    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public int getAnswer_user_type() {
        return answer_user_type;
    }


    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }



    public String getVideo_url() {
        return video_url;
    }



    public String getAnswer_user_id() {
        return answer_user_id;
    }


    public int getPortrait_state() {
        return portrait_state;
    }

    public void setPortrait_state(int portrait_state) {
        this.portrait_state = portrait_state;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getVideo_cover_uri() {
        return video_cover_uri;
    }

    public void setVideo_cover_uri(String video_cover_uri) {
        this.video_cover_uri = video_cover_uri;
    }

    public String getVideo_uri() {
        return video_uri;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }


    public String getPhoto_note() {
        return photo_note;
    }

    public void setPhoto_note(String photo_note) {
        this.photo_note = photo_note;
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

    public String getActivity_uri() {
        return activity_uri;
    }

    public void setActivity_uri(String activity_uri) {
        this.activity_uri = activity_uri;
    }

    public String getUpload_time_text() {
        return upload_time_text;
    }


    public String getPortrait_uri() {
        return portrait_uri;
    }

    public void setPortrait_uri(String portrait_uri) {
        this.portrait_uri = portrait_uri;
    }

    public boolean isAttention_state() {
        return attention_state == 1;
    }

    public Attention() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.answer_user_name);
        dest.writeString(this.cover_uri);
        dest.writeString(this.video_time_text);
        dest.writeInt(this.answer_portrait_state);
        dest.writeInt(this.observe_count);
        dest.writeInt(this.observe_state);
        dest.writeString(this.question_id);
        dest.writeString(this.answer_user_portrait);
        dest.writeInt(this.answer_user_type);
        dest.writeString(this.question_text);
        dest.writeInt(this.cover_state);
        dest.writeString(this.video_url);
        dest.writeString(this.shared_tag_text);
        dest.writeString(this.answer_user_id);
        dest.writeString(this.video_note);
        dest.writeInt(this.comments_count);
        dest.writeInt(this.portrait_state);
        dest.writeString(this.user_name);
        dest.writeString(this.video_cover_uri);
        dest.writeString(this.video_uri);
        dest.writeInt(this.user_type);
        dest.writeTypedList(this.notify_user_result);
        dest.writeTypedList(this.comment_list);
        dest.writeString(this.photo_thumb_url);
        dest.writeString(this.photo_uri);
        dest.writeString(this.photo_note);
        dest.writeString(this.activity_id);
        dest.writeString(this.activity_name);
        dest.writeString(this.activity_uri);
        dest.writeString(this.upload_time_text);
        dest.writeString(this.portrait_uri);
        dest.writeString(this.user_note);
        dest.writeString(this.username);
        dest.writeString(this.portait_state);
        dest.writeInt(this.video_state);
        dest.writeByte(this.praising ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.width);
        dest.writeDouble(this.height);
        dest.writeString(this.recommend_text);
        dest.writeByte(this.isShowItemMarsk ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_question_owner ? (byte) 1 : (byte) 0);
        dest.writeByte(this.temporary_free ? (byte) 1 : (byte) 0);
        dest.writeString(this.observe_price_text);
        dest.writeByte(this.showQaBtn ? (byte) 1 : (byte) 0);
        dest.writeByte(this.PlayNow ? (byte) 1 : (byte) 0);
        dest.writeString(this.share_link_address);
        dest.writeByte(this.isUploading ? (byte) 1 : (byte) 0);
        dest.writeString(this.videoSrcPath);
        dest.writeString(this.photoSrcPath);
        dest.writeLong(this.observe_price);
        dest.writeInt(this.uploadState);
        dest.writeDouble(this.progress);
        dest.writeString(this.user_authentic);
        dest.writeLong(this.duration_second);
        dest.writeLong(this.play_total);
        dest.writeLong(this.answer_time);
        dest.writeLong(this.upload_time);
        dest.writeLong(this.time_remain);
        dest.writeInt(this.answer_user_gender);
        dest.writeString(this.localPicturePath);
        dest.writeString(this.localCoverPath);
        dest.writeTypedList(this.attention_user_list);
    }

    protected Attention(Parcel in) {
        super(in);
        this.answer_user_name = in.readString();
        this.cover_uri = in.readString();
        this.video_time_text = in.readString();
        this.answer_portrait_state = in.readInt();
        this.observe_count = in.readInt();
        this.observe_state = in.readInt();
        this.question_id = in.readString();
        this.answer_user_portrait = in.readString();
        this.answer_user_type = in.readInt();
        this.question_text = in.readString();
        this.cover_state = in.readInt();
        this.video_url = in.readString();
        this.shared_tag_text = in.readString();
        this.answer_user_id = in.readString();
        this.video_note = in.readString();
        this.comments_count = in.readInt();
        this.portrait_state = in.readInt();
        this.user_name = in.readString();
        this.video_cover_uri = in.readString();
        this.video_uri = in.readString();
        this.user_type = in.readInt();
        this.notify_user_result = in.createTypedArrayList(Notify_user_result.CREATOR);
        this.comment_list = in.createTypedArrayList(Comment.CREATOR);
        this.photo_thumb_url = in.readString();
        this.photo_uri = in.readString();
        this.photo_note = in.readString();
        this.activity_id = in.readString();
        this.activity_name = in.readString();
        this.activity_uri = in.readString();
        this.upload_time_text = in.readString();
        this.portrait_uri = in.readString();
        this.user_note = in.readString();
        this.username = in.readString();
        this.portait_state = in.readString();
        this.video_state = in.readInt();
        this.praising = in.readByte() != 0;
        this.width = in.readDouble();
        this.height = in.readDouble();
        this.recommend_text = in.readString();
        this.isShowItemMarsk = in.readByte() != 0;
        this.is_question_owner = in.readByte() != 0;
        this.temporary_free = in.readByte() != 0;
        this.observe_price_text = in.readString();
        this.showQaBtn = in.readByte() != 0;
        this.PlayNow = in.readByte() != 0;
        this.share_link_address = in.readString();
        this.isUploading = in.readByte() != 0;
        this.videoSrcPath = in.readString();
        this.photoSrcPath = in.readString();
        this.observe_price = in.readLong();
        this.uploadState = in.readInt();
        this.progress = in.readDouble();
        this.user_authentic = in.readString();
        this.duration_second = in.readLong();
        this.play_total = in.readLong();
        this.answer_time = in.readLong();
        this.upload_time = in.readLong();
        this.time_remain = in.readLong();
        this.answer_user_gender = in.readInt();
        this.localPicturePath = in.readString();
        this.localCoverPath = in.readString();
        this.attention_user_list = in.createTypedArrayList(MayBeInterest.CREATOR);
    }

    public static final Creator<Attention> CREATOR = new Creator<Attention>() {
        @Override
        public Attention createFromParcel(Parcel source) {
            return new Attention(source);
        }

        @Override
        public Attention[] newArray(int size) {
            return new Attention[size];
        }
    };
}
