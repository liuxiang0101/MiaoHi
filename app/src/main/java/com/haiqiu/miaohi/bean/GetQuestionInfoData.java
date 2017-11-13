package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.MHStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuXiang on 2016/7/21.
 * 应答详情界面数据bean类
 */


public class GetQuestionInfoData implements Parcelable {
    private String question_id;
    private String video_time_text;
    private int observe_state;
    private long question_price;
    private String question_text;
    private String question_time;
    private int question_private;
    private String question_user_id;
    private int question_user_type;
    private String question_user_name;
    private String answer_user_id;
    private int answer_user_type;
    private String answer_user_name;
    private String answer_portrait_uri;
    private String video_id;
    private String video_time;
    private String cover_uri;
    private long play_total;
    private long video_price;
    private int duration_second;
    private String hls_uri;
    private String video_uri;
    private String answer_authentic;
    private String answer_note;
    private String question_portrait_uri;
    private int video_state;
    private int answer_user_state;
    private int question_user_state;
    private int answer_portrait_state;
    private int cover_state;
    private long answer_total;
    private int answer_gender;
    private long answer_observed_total;
    private long question_all_total;
    private int hls_uri_state;
    private boolean temporary_free;//现在是否为显示免费true,false
    private long time_remain;//显示免费剩余毫秒数
    private boolean is_question_owner;//是否为映答所有者（提问的,回答的,围观的）true,false
    private String observe_price_text;//围观按键显示文字
    private GetQAInfoRecommendObject recommend_question;
    private List<GetQAObserveUserObject> observe_user_result;
    private String activity_id;
    private String activity_name;
    private long observe_price;
    private double video_width;
    private double video_height;
    private String share_link_address;
    private long answer_time;
    private String Activity_uri;
    private long observe_count;
    private String answer_time_text;


    public long getSrcObservPrice(){
        return observe_price;
    }

    public String getAnswer_time_text() {
        return answer_time_text;
    }

    public void setAnswer_time_text(String answer_time_text) {
        this.answer_time_text = answer_time_text;
    }

    public long getObserve_count() {
        return observe_count;
    }

    public void setObserve_count(long observe_count) {
        this.observe_count = observe_count;
    }

    public String getActivity_uri() {
        return Activity_uri;
    }

    public void setActivity_uri(String activity_uri) {
        Activity_uri = activity_uri;
    }

    public long getAnswer_time() {
        return answer_time;
    }

    public void setAnswer_time(long answer_time) {
        this.answer_time = answer_time;
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

    public double getVideo_height() {
        return video_height;
    }

    public void setVideo_height(double video_height) {
        this.video_height = video_height;
    }

    public double getVideo_width() {
        return video_width;
    }

    public void setVideo_width(double video_width) {
        this.video_width = video_width;
    }

    public boolean isTemporary_free() {
        return temporary_free;
    }

    public boolean is_question_owner() {
        return is_question_owner;
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

    public String getObserve_price() {
        return observe_price+"";
    }

    public void setObserve_price(long observe_price) {
        this.observe_price = observe_price;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public void setVideo_time_text(String video_time_text) {
        this.video_time_text = video_time_text;
    }

    public void setQuestion_price(long question_price) {
        this.question_price = question_price;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public void setQuestion_time(String question_time) {
        this.question_time = question_time;
    }

    public void setQuestion_private(int question_private) {
        this.question_private = question_private;
    }

    public void setQuestion_user_id(String question_user_id) {
        this.question_user_id = question_user_id;
    }

    public void setQuestion_user_type(int question_user_type) {
        this.question_user_type = question_user_type;
    }

    public void setQuestion_user_name(String question_user_name) {
        this.question_user_name = question_user_name;
    }

    public void setAnswer_user_id(String answer_user_id) {
        this.answer_user_id = answer_user_id;
    }

    public void setAnswer_user_type(int answer_user_type) {
        this.answer_user_type = answer_user_type;
    }

    public void setAnswer_user_name(String answer_user_name) {
        this.answer_user_name = answer_user_name;
    }

    public void setAnswer_portrait_uri(String answer_portrait_uri) {
        this.answer_portrait_uri = answer_portrait_uri;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public void setVideo_time(String video_time) {
        this.video_time = video_time;
    }

    public void setCover_uri(String cover_uri) {
        this.cover_uri = cover_uri;
    }

    public void setDuration_second(int duration_second) {
        this.duration_second = duration_second;
    }

    public void setHls_uri(String hls_uri) {
        this.hls_uri = hls_uri;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
    }

    public void setAnswer_authentic(String answer_authentic) {
        this.answer_authentic = answer_authentic;
    }

    public void setAnswer_note(String answer_note) {
        this.answer_note = answer_note;
    }

    public void setQuestion_portrait_uri(String question_portrait_uri) {
        this.question_portrait_uri = question_portrait_uri;
    }

    public void setVideo_state(int video_state) {
        this.video_state = video_state;
    }

    public void setAnswer_user_state(int answer_user_state) {
        this.answer_user_state = answer_user_state;
    }

    public void setQuestion_user_state(int question_user_state) {
        this.question_user_state = question_user_state;
    }

    public void setAnswer_portrait_state(int answer_portrait_state) {
        this.answer_portrait_state = answer_portrait_state;
    }

    public void setCover_state(int cover_state) {
        this.cover_state = cover_state;
    }

    public void setAnswer_total(long answer_total) {
        this.answer_total = answer_total;
    }

    public void setAnswer_gender(int answer_gender) {
        this.answer_gender = answer_gender;
    }

    public void setAnswer_observed_total(long answer_observed_total) {
        this.answer_observed_total = answer_observed_total;
    }

    public void setQuestion_all_total(long question_all_total) {
        this.question_all_total = question_all_total;
    }

    public boolean getTemporary_free() {
        return temporary_free;
    }

    public void setTemporary_free(boolean temporary_free) {
        this.temporary_free = temporary_free;
    }

    public long getTime_remain() {
        return time_remain;
    }

    public void setTime_remain(long time_remain) {
        this.time_remain = time_remain;
    }

    public boolean getIs_question_owner() {
        return is_question_owner;
    }

    public void setIs_question_owner(boolean is_question_owner) {
        this.is_question_owner = is_question_owner;
    }

    public String getObserve_price_text() {
        return observe_price_text;
    }

    public void setObserve_price_text(String observe_price_text) {
        this.observe_price_text = observe_price_text;
    }

    public void setRecommend_question(GetQAInfoRecommendObject recommend_question) {
        this.recommend_question = recommend_question;
    }

    public void setObserve_user_result(List<GetQAObserveUserObject> observe_user_result) {
        this.observe_user_result = observe_user_result;
    }

    public GetQAInfoRecommendObject getRecommend_question() {
        return recommend_question;
    }

    public List<GetQAObserveUserObject> getObserve_user_result() {
        return observe_user_result;
    }

    public String getVideo_time_text() {
        return video_time_text;
    }

    public int getObserve_state() {
        return observe_state;
    }

    public long getQuestion_price() {
        return question_price;
    }

    public String getAnswer_authentic() {
        return answer_authentic;
    }

    public String getAnswer_note() {
        return answer_note;
    }

    public String getQuestion_portrait_uri() {
        return question_portrait_uri + ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public long getQuestion_all_total() {
        return question_all_total;
    }

    public long getAnswer_total() {
        return answer_total;
    }

    public int getAnswer_gender() {
        return answer_gender;
    }

    public long getAnswer_observed_total() {
        return answer_observed_total;
    }

    public int getVideo_state() {
        return video_state;
    }

    public int getDuration_second() {
        return duration_second;
    }

    public String getHls_uri() {
        return hls_uri;
    }

    public String getVideo_uri() {
        return video_uri;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public String getQuestion_time() {
        return question_time;
    }

    public int getQuestion_private() {
        return question_private;
    }

    public String getQuestion_user_id() {
        return question_user_id;
    }

    public int getQuestion_user_type() {
        return question_user_type;
    }

    public String getQuestion_user_name() {
        return question_user_name;
    }

    public int getQuestion_user_state() {
        return question_user_state;
    }

    public String getAnswer_user_id() {
        return answer_user_id;
    }

    public int getAnswer_user_type() {
        return answer_user_type;
    }

    public String getAnswer_user_name() {
        return answer_user_name;
    }

    public int getAnswer_user_state() {
        return answer_user_state;
    }

    public String getAnswer_portrait_uri() {
        return answer_portrait_uri + ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public int getAnswer_portrait_state() {
        return answer_portrait_state;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getVideo_time() {
        return video_time;
    }

    public String getCover_uri() {
        if (null != cover_uri && cover_uri.contains("?")) {
            return cover_uri + ConstantsValue.Other.VIDEO_MAX_PREVIEW_PICTURE_PARAM_FRAME;
        }
        return cover_uri + ConstantsValue.Other.VIDEO_MAX_PREVIEW_PICTURE_PARAM;
    }

    public int getCover_state() {
        return cover_state;
    }

    public long getPlay_total() {
        return play_total;
    }

    public void setPlay_total(long play_total) {
        this.play_total = play_total;
    }

    public long getVideo_price() {
        return video_price;
    }

    public void setVideo_price(long video_price) {
        this.video_price = video_price;
    }

    public void setObserve_state(int observe_state) {
        this.observe_state = observe_state;
    }

    public int getHls_uri_state() {
        return hls_uri_state;
    }

    public void setHls_uri_state(int hls_uri_state) {
        this.hls_uri_state = hls_uri_state;
    }

    public GetQuestionInfoData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question_id);
        dest.writeString(this.video_time_text);
        dest.writeInt(this.observe_state);
        dest.writeLong(this.question_price);
        dest.writeString(this.question_text);
        dest.writeString(this.question_time);
        dest.writeInt(this.question_private);
        dest.writeString(this.question_user_id);
        dest.writeInt(this.question_user_type);
        dest.writeString(this.question_user_name);
        dest.writeString(this.answer_user_id);
        dest.writeInt(this.answer_user_type);
        dest.writeString(this.answer_user_name);
        dest.writeString(this.answer_portrait_uri);
        dest.writeString(this.video_id);
        dest.writeString(this.video_time);
        dest.writeString(this.cover_uri);
        dest.writeLong(this.play_total);
        dest.writeLong(this.video_price);
        dest.writeInt(this.duration_second);
        dest.writeString(this.hls_uri);
        dest.writeString(this.video_uri);
        dest.writeString(this.answer_authentic);
        dest.writeString(this.answer_note);
        dest.writeString(this.question_portrait_uri);
        dest.writeInt(this.video_state);
        dest.writeInt(this.answer_user_state);
        dest.writeInt(this.question_user_state);
        dest.writeInt(this.answer_portrait_state);
        dest.writeInt(this.cover_state);
        dest.writeLong(this.answer_total);
        dest.writeInt(this.answer_gender);
        dest.writeLong(this.answer_observed_total);
        dest.writeLong(this.question_all_total);
        dest.writeInt(this.hls_uri_state);
        dest.writeByte(this.temporary_free ? (byte) 1 : (byte) 0);
        dest.writeLong(this.time_remain);
        dest.writeByte(this.is_question_owner ? (byte) 1 : (byte) 0);
        dest.writeString(this.observe_price_text);
        dest.writeParcelable(this.recommend_question, flags);
        dest.writeList(this.observe_user_result);
        dest.writeString(this.activity_id);
        dest.writeString(this.activity_name);
        dest.writeLong(this.observe_price);
        dest.writeDouble(this.video_width);
        dest.writeDouble(this.video_height);
        dest.writeString(this.share_link_address);
        dest.writeLong(this.answer_time);
        dest.writeString(this.Activity_uri);
        dest.writeLong(this.observe_count);
        dest.writeString(this.answer_time_text);
    }

    protected GetQuestionInfoData(Parcel in) {
        this.question_id = in.readString();
        this.video_time_text = in.readString();
        this.observe_state = in.readInt();
        this.question_price = in.readLong();
        this.question_text = in.readString();
        this.question_time = in.readString();
        this.question_private = in.readInt();
        this.question_user_id = in.readString();
        this.question_user_type = in.readInt();
        this.question_user_name = in.readString();
        this.answer_user_id = in.readString();
        this.answer_user_type = in.readInt();
        this.answer_user_name = in.readString();
        this.answer_portrait_uri = in.readString();
        this.video_id = in.readString();
        this.video_time = in.readString();
        this.cover_uri = in.readString();
        this.play_total = in.readLong();
        this.video_price = in.readLong();
        this.duration_second = in.readInt();
        this.hls_uri = in.readString();
        this.video_uri = in.readString();
        this.answer_authentic = in.readString();
        this.answer_note = in.readString();
        this.question_portrait_uri = in.readString();
        this.video_state = in.readInt();
        this.answer_user_state = in.readInt();
        this.question_user_state = in.readInt();
        this.answer_portrait_state = in.readInt();
        this.cover_state = in.readInt();
        this.answer_total = in.readLong();
        this.answer_gender = in.readInt();
        this.answer_observed_total = in.readLong();
        this.question_all_total = in.readLong();
        this.hls_uri_state = in.readInt();
        this.temporary_free = in.readByte() != 0;
        this.time_remain = in.readLong();
        this.is_question_owner = in.readByte() != 0;
        this.observe_price_text = in.readString();
        this.recommend_question = in.readParcelable(GetQAInfoRecommendObject.class.getClassLoader());
        this.observe_user_result = new ArrayList<GetQAObserveUserObject>();
        in.readList(this.observe_user_result, GetQAObserveUserObject.class.getClassLoader());
        this.activity_id = in.readString();
        this.activity_name = in.readString();
        this.observe_price = in.readLong();
        this.video_width = in.readDouble();
        this.video_height = in.readDouble();
        this.share_link_address = in.readString();
        this.answer_time = in.readLong();
        this.Activity_uri = in.readString();
        this.observe_count = in.readLong();
        this.answer_time_text = in.readString();
    }

    public static final Creator<GetQuestionInfoData> CREATOR = new Creator<GetQuestionInfoData>() {
        @Override
        public GetQuestionInfoData createFromParcel(Parcel source) {
            return new GetQuestionInfoData(source);
        }

        @Override
        public GetQuestionInfoData[] newArray(int size) {
            return new GetQuestionInfoData[size];
        }
    };
}
