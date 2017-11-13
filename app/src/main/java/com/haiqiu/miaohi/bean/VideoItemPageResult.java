package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhandalin on 2016-05-28 15:49.
 * 说明:关注的视频列表bean类
 */
public class VideoItemPageResult implements Parcelable {

    private String video_id;//视频id
    private boolean praise_state;//视频点赞状态
    private long praise_count;
    private String user_id;//用户id
    private int attention_state;//关注状态

    private String portrait_state;
    private String cover_state;
    private int user_type;
    private int video_state;
    private int hls_state;
    private String auto_id;
    private long video_price;
    private String video_uri;
    private String hls_uri;

    private long duration_second;
    private String cover_uri;
    private String upload_time;
    private String worked_time;
    private String video_note;
    private String user_name;
    private String portrait_uri;

    private boolean isPraising;

    private long comments_count;
    private long gift_count;

    private List<Invite_resultBean> invite_result;

    private String doWithCommentCount;
    private String doWithGiftCount;
    private String doWithPraiseCount;

    private String doWithInviteName;

    /**
     * element_type : 元素类型 1为视频  3为映答卡片
     */
    private int element_type;
    /**
     * answer_total : 46
     * vip_note : 测试认证
     * user_note : 用户说明测试
     * question_total : 46
     * play_total : 0
     * user_state : 16
     */

    private String answer_total;
    private String vip_note;
    private String user_note;
    private String question_total;
    private int play_total;
    private String user_state;

    private int observe_state;
    /**
     * video_time : 2016-07-28 11:05:08.0
     * answer_user_name : xxx
     * answer_portrait_state : 10
     * question_private : 0
     * question_id : QUST-095b9d85-53dc-11e6-8f77-44a8424640fa
     * answer_portrait_uri : http://icon.dev.miaohi.com/myIcon_20160727202935
     * answer_user_type : 20988
     * question_text : 哦OK了咯OK了提问啦
     * answer_user_id : USER-8571b2c4-265e-11e6-83a3-44a8424640fa
     */

    private String video_time;
    private String answer_user_name;
    private String answer_portrait_state;
    private String question_private;
    private String question_id;
    private String answer_portrait_uri;
    private int answer_user_type;
    private String question_text;
    private String answer_user_id;
    /**
     * answer_authentic : 测试认证
     * answer_note : 用户说明测试
     */

    private String answer_authentic;
    private String answer_note;

    /**
     * 分享#xxxx#
     */
    private String shared_tag_text;

    private String activity_id;
    private String activity_name;
    private long question_price;
    private String question_user_id;
    private int question_user_type;
    private String question_user_name;
    private int question_user_state;
    private String question_portrait_uri;
    private int question_portrait_state;
    private int observe_attention_count;
    private int scaleState;
    private int closeState;
    private int controlState;//0普通状态,1重播和点赞状态,2重播状态
    private List<ObserveAttentionResult> observe_attention_result = new ArrayList<>();
    private String upload_time_text;
    private String video_time_text;
    private List<Notify_user_result> notify_user_result;


    public List<Notify_user_result> getNotify_user_result() {
        return notify_user_result;
    }

    public void setNotify_user_result(List<Notify_user_result> notify_user_result) {
        this.notify_user_result = notify_user_result;
    }

    public String getVideo_time_text() {
        return video_time_text;
    }

    public void setVideo_time_text(String video_time_text) {
        this.video_time_text = video_time_text;
    }

    public String getUpload_time_text() {
        return upload_time_text;
    }

    public void setUpload_time_text(String upload_time_text) {
        this.upload_time_text = upload_time_text;
    }

    public int getControlState() {
        return controlState;
    }

    public void setControlState(int controlState) {
        this.controlState = controlState;
    }

    public int getCloseState() {
        return closeState;
    }

    public void setCloseState(int closeState) {
        this.closeState = closeState;
    }

    public int getScaleState() {
        return scaleState;
    }

    public void setScaleState(int scaleState) {
        this.scaleState = scaleState;
    }


    public List<ObserveAttentionResult> getObserve_attention_result() {
        return observe_attention_result;
    }

    public void setObserve_attention_result(List<ObserveAttentionResult> observe_attention_result) {
        this.observe_attention_result = observe_attention_result;
    }

    public void setQuestion_price(long question_price) {
        this.question_price = question_price;
    }

    public int getObserve_attention_count() {
        return observe_attention_count;
    }

    public void setObserve_attention_count(int observe_attention_count) {
        this.observe_attention_count = observe_attention_count;
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

    public long getQuestion_price() {
        return question_price;
    }

    public String getQuestion_user_id() {
        return question_user_id;
    }

    public void setQuestion_user_id(String question_user_id) {
        this.question_user_id = question_user_id;
    }

    public int getQuestion_user_type() {
        return question_user_type;
    }

    public void setQuestion_user_type(int question_user_type) {
        this.question_user_type = question_user_type;
    }

    public String getQuestion_user_name() {
        return question_user_name;
    }

    public void setQuestion_user_name(String question_user_name) {
        this.question_user_name = question_user_name;
    }

    public int getQuestion_user_state() {
        return question_user_state;
    }

    public void setQuestion_user_state(int question_user_state) {
        this.question_user_state = question_user_state;
    }

    public String getQuestion_portrait_uri() {
        return question_portrait_uri;
    }

    public void setQuestion_portrait_uri(String question_portrait_uri) {
        this.question_portrait_uri = question_portrait_uri;
    }

    public int getQuestion_portrait_state() {
        return question_portrait_state;
    }

    public void setQuestion_portrait_state(int question_portrait_state) {
        this.question_portrait_state = question_portrait_state;
    }

    public String getShared_tag_text() {
        return shared_tag_text;
    }

    public void setShared_tag_text(String shared_tag_text) {
        this.shared_tag_text = shared_tag_text;
    }

    public String getPortrait_state() {
        return portrait_state;
    }

    public String getCover_state() {
        return cover_state;
    }

    public int getUser_type() {
        return user_type;
    }

    public int getVideo_state() {
        return video_state;
    }

    public int getHls_state() {
        return hls_state;
    }

    public String getAuto_id() {
        return auto_id;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getVideo_uri() {
        return video_uri;
    }

    public String getHls_uri() {
        return hls_uri;
    }

    public long getDuration_second() {
        return duration_second;
    }

    public String getCover_uri() {
        if (null != cover_uri && cover_uri.contains("?")) {
            return cover_uri + ConstantsValue.Other.VIDEO_MAX_PREVIEW_PICTURE_PARAM_FRAME;
        }
        return cover_uri + ConstantsValue.Other.VIDEO_MAX_PREVIEW_PICTURE_PARAM;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public String getWorked_time() {
        if (null == worked_time) {
            worked_time = MHStringUtils.getTimeCH(getUpload_time());
        }
        return worked_time;
    }

    public String getVideo_note() {
        return video_note;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPortrait_uri() {
        return portrait_uri + ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public long getGift_count() {
        return gift_count;
    }


    public void setPraise_count(long praise_count) {
        if (praise_count < 0)
            praise_count = 0;
        this.praise_count = praise_count;
        doWithPraiseCount = CommonUtil.formatCount(praise_count);
    }

    public boolean isPraise_state() {
        return praise_state;
    }

    public void setPraise_state(boolean praise_state) {
        this.praise_state = praise_state;
    }

    public String getDoWithCommentCount() {
        if (null == doWithCommentCount)
            doWithCommentCount = CommonUtil.formatCount(comments_count);
        return doWithCommentCount;
    }

    public String getDoWithGiftCount() {
        if (null == doWithGiftCount)
            doWithGiftCount = CommonUtil.formatCount(gift_count);
        return doWithGiftCount;
    }

    public String getDoWithPraiseCount() {
        if (null == doWithPraiseCount)
            doWithPraiseCount = CommonUtil.formatCount(praise_count);
        return doWithPraiseCount;
    }

    public List<Invite_resultBean> getInvite_result() {
        return invite_result;
    }



    public String getDoWithInviteName() {
        if (null == doWithInviteName) {
            if (null != invite_result && invite_result.size() > 0) {
                doWithInviteName = "";
                for (Invite_resultBean invite : invite_result) {
                    if (!MHStringUtils.isEmpty(invite.getSender_name()))
                        doWithInviteName += "  @" + invite.getSender_name();
                }
            }
        }
        return doWithInviteName;
    }

    public long getComments_count() {
        return comments_count;
    }

    public void setComments_count(long comments_count) {
        this.comments_count = comments_count;
    }

    public int getElement_type() {
        return element_type;
    }

    public void setElement_type(int element_type) {
        this.element_type = element_type;
    }

    public String getAnswer_total() {
        return answer_total;
    }

    public void setAnswer_total(String answer_total) {
        this.answer_total = answer_total;
    }

    public String getVip_note() {
        return vip_note;
    }

    public void setVip_note(String vip_note) {
        this.vip_note = vip_note;
    }

    public String getUser_note() {
        return user_note;
    }

    public void setUser_note(String user_note) {
        this.user_note = user_note;
    }

    public String getQuestion_total() {
        return question_total;
    }

    public void setQuestion_total(String question_total) {
        this.question_total = question_total;
    }

    public int getPlay_total() {
        return play_total;
    }

    public void setPlay_total(int play_total) {
        this.play_total = play_total;
    }

    public String getUser_state() {
        return user_state;
    }

    public void setUser_state(String user_state) {
        this.user_state = user_state;
    }

    public boolean getObserve_state() {//是否已经支付过
        return observe_state == 1;
    }

    public void setObserve_state(int observe_state) {
        this.observe_state = observe_state;
    }

    public VideoItemPageResult() {
    }

    public String getVideo_time() {
        return video_time;
    }

    public void setVideo_time(String video_time) {
        this.video_time = video_time;
    }

    public String getAnswer_user_name() {
        return answer_user_name;
    }

    public void setAnswer_user_name(String answer_user_name) {
        this.answer_user_name = answer_user_name;
    }

    public String getAnswer_portrait_state() {
        return answer_portrait_state;
    }

    public void setAnswer_portrait_state(String answer_portrait_state) {
        this.answer_portrait_state = answer_portrait_state;
    }

    public String getQuestion_private() {
        return question_private;
    }

    public void setQuestion_private(String question_private) {
        this.question_private = question_private;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAnswer_portrait_uri() {
        return answer_portrait_uri + ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public void setAnswer_portrait_uri(String answer_portrait_uri) {
        this.answer_portrait_uri = answer_portrait_uri;
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

    public String getAnswer_user_id() {
        return answer_user_id;
    }

    public void setAnswer_user_id(String answer_user_id) {
        this.answer_user_id = answer_user_id;
    }

    public String getAnswer_authentic() {
        return answer_authentic;
    }

    public void setAnswer_authentic(String answer_authentic) {
        this.answer_authentic = answer_authentic;
    }

    public String getAnswer_note() {
        return answer_note;
    }

    public void setAnswer_note(String answer_note) {
        this.answer_note = answer_note;
    }

    public long getVideo_price() {
        return video_price;
    }

    public void setVideo_price(long video_price) {
        this.video_price = video_price;
    }

    public void setPortrait_state(String portrait_state) {
        this.portrait_state = portrait_state;
    }

    public void setCover_state(String cover_state) {
        this.cover_state = cover_state;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public void setVideo_state(int video_state) {
        this.video_state = video_state;
    }

    public void setHls_state(int hls_state) {
        this.hls_state = hls_state;
    }

    public void setAuto_id(String auto_id) {
        this.auto_id = auto_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
    }

    public void setHls_uri(String hls_uri) {
        this.hls_uri = hls_uri;
    }

    public void setDuration_second(long duration_second) {
        this.duration_second = duration_second;
    }

    public void setCover_uri(String cover_uri) {
        this.cover_uri = cover_uri;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public void setWorked_time(String worked_time) {
        this.worked_time = worked_time;
    }

    public void setVideo_note(String video_note) {
        this.video_note = video_note;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setPortrait_uri(String portrait_uri) {
        this.portrait_uri = portrait_uri;
    }

    public void setComment_count(long comment_count) {
        this.comments_count = comment_count;
        doWithCommentCount = CommonUtil.formatCount(comment_count);
    }

    public void setGift_count(long gift_count) {
        doWithGiftCount = CommonUtil.formatCount(gift_count);
        this.gift_count = gift_count;
    }

    public void setInvite_result(List<Invite_resultBean> invite_result) {
        this.invite_result = invite_result;
    }

    public void setDoWithCommentCount(String doWithCommentCount) {
        this.doWithCommentCount = doWithCommentCount;
    }

    public void setDoWithGiftCount(String doWithGiftCount) {
        this.doWithGiftCount = doWithGiftCount;
    }

    public void setDoWithPraiseCount(String doWithPraiseCount) {
        this.doWithPraiseCount = doWithPraiseCount;
    }


    public void setDoWithInviteName(String doWithInviteName) {
        this.doWithInviteName = doWithInviteName;
    }

    public void setAnswer_user_type(int answer_user_type) {
        this.answer_user_type = answer_user_type;
    }

    public boolean isPraising() {
        return isPraising;
    }

    public void setPraising(boolean praising) {
        isPraising = praising;
    }

    public long getPraise_count() {
        return praise_count;
    }

    public int getAttention_state() {
        return attention_state;
    }

    public void setAttention_state(int attention_state) {
        this.attention_state = attention_state;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.video_id);
        dest.writeByte(this.praise_state ? (byte) 1 : (byte) 0);
        dest.writeLong(this.praise_count);
        dest.writeString(this.user_id);
        dest.writeInt(this.attention_state);
        dest.writeString(this.portrait_state);
        dest.writeString(this.cover_state);
        dest.writeInt(this.user_type);
        dest.writeInt(this.video_state);
        dest.writeInt(this.hls_state);
        dest.writeString(this.auto_id);
        dest.writeLong(this.video_price);
        dest.writeString(this.video_uri);
        dest.writeString(this.hls_uri);
        dest.writeLong(this.duration_second);
        dest.writeString(this.cover_uri);
        dest.writeString(this.upload_time);
        dest.writeString(this.worked_time);
        dest.writeString(this.video_note);
        dest.writeString(this.user_name);
        dest.writeString(this.portrait_uri);
        dest.writeByte(this.isPraising ? (byte) 1 : (byte) 0);
        dest.writeLong(this.comments_count);
        dest.writeLong(this.gift_count);
        dest.writeTypedList(this.invite_result);
        dest.writeString(this.doWithCommentCount);
        dest.writeString(this.doWithGiftCount);
        dest.writeString(this.doWithPraiseCount);
        dest.writeString(this.doWithInviteName);
        dest.writeInt(this.element_type);
        dest.writeString(this.answer_total);
        dest.writeString(this.vip_note);
        dest.writeString(this.user_note);
        dest.writeString(this.question_total);
        dest.writeInt(this.play_total);
        dest.writeString(this.user_state);
        dest.writeInt(this.observe_state);
        dest.writeString(this.video_time);
        dest.writeString(this.answer_user_name);
        dest.writeString(this.answer_portrait_state);
        dest.writeString(this.question_private);
        dest.writeString(this.question_id);
        dest.writeString(this.answer_portrait_uri);
        dest.writeInt(this.answer_user_type);
        dest.writeString(this.question_text);
        dest.writeString(this.answer_user_id);
        dest.writeString(this.answer_authentic);
        dest.writeString(this.answer_note);
        dest.writeString(this.shared_tag_text);
        dest.writeString(this.activity_id);
        dest.writeString(this.activity_name);
        dest.writeLong(this.question_price);
        dest.writeString(this.question_user_id);
        dest.writeInt(this.question_user_type);
        dest.writeString(this.question_user_name);
        dest.writeInt(this.question_user_state);
        dest.writeString(this.question_portrait_uri);
        dest.writeInt(this.question_portrait_state);
        dest.writeInt(this.observe_attention_count);
        dest.writeInt(this.scaleState);
        dest.writeInt(this.closeState);
        dest.writeInt(this.controlState);
        dest.writeTypedList(this.observe_attention_result);
        dest.writeString(this.upload_time_text);
        dest.writeString(this.video_time_text);
        dest.writeTypedList(this.notify_user_result);
    }

    protected VideoItemPageResult(Parcel in) {
        this.video_id = in.readString();
        this.praise_state = in.readByte() != 0;
        this.praise_count = in.readLong();
        this.user_id = in.readString();
        this.attention_state = in.readInt();
        this.portrait_state = in.readString();
        this.cover_state = in.readString();
        this.user_type = in.readInt();
        this.video_state = in.readInt();
        this.hls_state = in.readInt();
        this.auto_id = in.readString();
        this.video_price = in.readLong();
        this.video_uri = in.readString();
        this.hls_uri = in.readString();
        this.duration_second = in.readLong();
        this.cover_uri = in.readString();
        this.upload_time = in.readString();
        this.worked_time = in.readString();
        this.video_note = in.readString();
        this.user_name = in.readString();
        this.portrait_uri = in.readString();
        this.isPraising = in.readByte() != 0;
        this.comments_count = in.readLong();
        this.gift_count = in.readLong();
        this.invite_result = in.createTypedArrayList(Invite_resultBean.CREATOR);
        this.doWithCommentCount = in.readString();
        this.doWithGiftCount = in.readString();
        this.doWithPraiseCount = in.readString();
        this.doWithInviteName = in.readString();
        this.element_type = in.readInt();
        this.answer_total = in.readString();
        this.vip_note = in.readString();
        this.user_note = in.readString();
        this.question_total = in.readString();
        this.play_total = in.readInt();
        this.user_state = in.readString();
        this.observe_state = in.readInt();
        this.video_time = in.readString();
        this.answer_user_name = in.readString();
        this.answer_portrait_state = in.readString();
        this.question_private = in.readString();
        this.question_id = in.readString();
        this.answer_portrait_uri = in.readString();
        this.answer_user_type = in.readInt();
        this.question_text = in.readString();
        this.answer_user_id = in.readString();
        this.answer_authentic = in.readString();
        this.answer_note = in.readString();
        this.shared_tag_text = in.readString();
        this.activity_id = in.readString();
        this.activity_name = in.readString();
        this.question_price = in.readLong();
        this.question_user_id = in.readString();
        this.question_user_type = in.readInt();
        this.question_user_name = in.readString();
        this.question_user_state = in.readInt();
        this.question_portrait_uri = in.readString();
        this.question_portrait_state = in.readInt();
        this.observe_attention_count = in.readInt();
        this.scaleState = in.readInt();
        this.closeState = in.readInt();
        this.controlState = in.readInt();
        this.observe_attention_result = in.createTypedArrayList(ObserveAttentionResult.CREATOR);
        this.upload_time_text = in.readString();
        this.video_time_text = in.readString();
        this.notify_user_result = in.createTypedArrayList(Notify_user_result.CREATOR);
    }

    public static final Creator<VideoItemPageResult> CREATOR = new Creator<VideoItemPageResult>() {
        @Override
        public VideoItemPageResult createFromParcel(Parcel source) {
            return new VideoItemPageResult(source);
        }

        @Override
        public VideoItemPageResult[] newArray(int size) {
            return new VideoItemPageResult[size];
        }
    };
}
