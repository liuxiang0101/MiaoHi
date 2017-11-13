package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LiuXiang on 2016/9/22.
 */
public class GetQAInfoRecommendObject implements Parcelable {

    /**
     * question_text : 在这条件和
     * observe_price_text : 点击播放
     * answer_user_name : 小智嫂子是
     * answer_authentic : 小智嫂子是
     * observe_count : 3
     * answer_time_text : 09.28/11:28
     * answer_user_state : 10
     * question_id : QUST-64519690-8523-11e6-b0c9-44a8424640fa
     * is_question_owner : true
     * answer_user_id : USER-6aa3f04a-74af-11e6-a51b-44a8424640fa
     * answer_user_type : 20
     * answer_user_portrait : http://icon.dev.miaohi.com//iconImage_beSm3c2KM5icNb85HXKzcdPSDxAdZhpm_2016_11_10_19_56_14
     */

    private String question_text;
    private String observe_price_text;
    private String answer_user_name;
    private String answer_authentic;
    private int observe_count;
    private String answer_time_text;
    private int answer_user_state;
    private String question_id;
    private boolean is_question_owner;
    private String answer_user_id;
    private int answer_user_type;
    private String answer_user_portrait;
    private boolean temporary_free;
    private long time_remain;//显示免费剩余毫秒数
    private long observe_price;

    public long getObserve_price() {
        return observe_price;
    }

    public void setObserve_price(long observe_price) {
        this.observe_price = observe_price;
    }

    public long getTime_remain() {
        return time_remain;
    }

    public void setTime_remain(long time_remain) {
        this.time_remain = time_remain;
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

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getObserve_price_text() {
        return observe_price_text;
    }

    public void setObserve_price_text(String observe_price_text) {
        this.observe_price_text = observe_price_text;
    }

    public String getAnswer_user_name() {
        return answer_user_name;
    }

    public void setAnswer_user_name(String answer_user_name) {
        this.answer_user_name = answer_user_name;
    }

    public String getAnswer_authentic() {
        return answer_authentic;
    }

    public void setAnswer_authentic(String answer_authentic) {
        this.answer_authentic = answer_authentic;
    }

    public int getObserve_count() {
        return observe_count;
    }

    public void setObserve_count(int observe_count) {
        this.observe_count = observe_count;
    }

    public String getAnswer_time_text() {
        return answer_time_text;
    }

    public void setAnswer_time_text(String answer_time_text) {
        this.answer_time_text = answer_time_text;
    }

    public int getAnswer_user_state() {
        return answer_user_state;
    }

    public void setAnswer_user_state(int answer_user_state) {
        this.answer_user_state = answer_user_state;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAnswer_user_id() {
        return answer_user_id;
    }

    public void setAnswer_user_id(String answer_user_id) {
        this.answer_user_id = answer_user_id;
    }

    public int getAnswer_user_type() {
        return answer_user_type;
    }

    public void setAnswer_user_type(int answer_user_type) {
        this.answer_user_type = answer_user_type;
    }

    public String getAnswer_user_portrait() {
        return answer_user_portrait;
    }

    public void setAnswer_user_portrait(String answer_user_portrait) {
        this.answer_user_portrait = answer_user_portrait;
    }

    public GetQAInfoRecommendObject() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question_text);
        dest.writeString(this.observe_price_text);
        dest.writeString(this.answer_user_name);
        dest.writeString(this.answer_authentic);
        dest.writeInt(this.observe_count);
        dest.writeString(this.answer_time_text);
        dest.writeInt(this.answer_user_state);
        dest.writeString(this.question_id);
        dest.writeByte(this.is_question_owner ? (byte) 1 : (byte) 0);
        dest.writeString(this.answer_user_id);
        dest.writeInt(this.answer_user_type);
        dest.writeString(this.answer_user_portrait);
        dest.writeByte(this.temporary_free ? (byte) 1 : (byte) 0);
        dest.writeLong(this.time_remain);
    }

    protected GetQAInfoRecommendObject(Parcel in) {
        this.question_text = in.readString();
        this.observe_price_text = in.readString();
        this.answer_user_name = in.readString();
        this.answer_authentic = in.readString();
        this.observe_count = in.readInt();
        this.answer_time_text = in.readString();
        this.answer_user_state = in.readInt();
        this.question_id = in.readString();
        this.is_question_owner = in.readByte() != 0;
        this.answer_user_id = in.readString();
        this.answer_user_type = in.readInt();
        this.answer_user_portrait = in.readString();
        this.temporary_free = in.readByte() != 0;
        this.time_remain = in.readLong();
    }

    public static final Creator<GetQAInfoRecommendObject> CREATOR = new Creator<GetQAInfoRecommendObject>() {
        @Override
        public GetQAInfoRecommendObject createFromParcel(Parcel source) {
            return new GetQAInfoRecommendObject(source);
        }

        @Override
        public GetQAInfoRecommendObject[] newArray(int size) {
            return new GetQAInfoRecommendObject[size];
        }
    };
}
