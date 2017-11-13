package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.CommonUtil;

/**
 * 用户信息
 * Created by ningl on 2016/5/25.
 */
public class UserInfoBean implements Parcelable {

    /**
     * portrait_state : 10
     * face_sent_count : 1
     * achievement_video_count : 0
     * user_name : ぃ    维她命 ヅ
     * attention_count : 0
     * user_gender : 1
     * gift_received_count : 0
     * user_state : 10
     * user_birthday : 2000-02-01
     * fans_count : 0
     * user_type : 10
     * user_area : 北京 | 北京市内 | 东城区
     * unread_comment_count
     * unread_praise_count
     * unread_invitation_count
     * unread_attention_count
     * unread_atme_count
     * unread_ system_count
     * portrait_uri : http://q.qlogo.cn/qqapp/1105377386/76F7914C7CE46A31E29F5352E6ECBC08/100
     */
    private String user_id;//用户id
    private int attention_state;//关注状态

    private String portrait_state;
    private String face_sent_count;
    private long achievement_video_count;
    private String user_name;

    private long attention_count;
    private int user_gender;
    private String gift_received_count;
    private String user_state;
    private long user_birthday;
    private String user_birthdayStr;
    private long fans_count;
    private int user_type;
    private String user_area;
    private String portrait_uri;
    private String user_note;
    private float unread_comment_count;
    private float unread_praise_count;
    private float unread_invitation_count;
    private float unread_attention_count;
    private float unread_atme_count;
    private float unread_system_count;

    private String fans_new_count;
    private String gift_received_new_count;
    private String praise_sent_count;
    private String user_authentic;
    private boolean price_allow_modify;
    private int answer_droit;
    private long question_price;
    private int question_all_total;
    private long answer_total;
    private long play_total;
    private String background_uri;

    public String getBgurl() {
        return background_uri;
    }

    public void setBgurl(String bgurl) {
        this.background_uri = bgurl;
    }

    public String getUser_birthdayStr() {
        return user_birthdayStr;
    }

    public void setUser_birthdayStr(String user_birthdayStr) {
        this.user_birthdayStr = user_birthdayStr;
    }

    public boolean isPrice_allow_modify() {
        return price_allow_modify;
    }

    public void setPortrait_state(String portrait_state) {
        this.portrait_state = portrait_state;
    }

    public void setFace_sent_count(String face_sent_count) {
        this.face_sent_count = face_sent_count;
    }

    public void setAchievement_video_count(long achievement_video_count) {
        this.achievement_video_count = achievement_video_count;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_gender(int user_gender) {
        this.user_gender = user_gender;
    }

    public void setGift_received_count(String gift_received_count) {
        this.gift_received_count = gift_received_count;
    }

    public void setUser_state(String user_state) {
        this.user_state = user_state;
    }

    public void setUser_birthday(long user_birthday) {
        this.user_birthday = user_birthday;
    }

    public void setFans_count(long fans_count) {
        this.fans_count = fans_count;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public void setUser_area(String user_area) {
        this.user_area = user_area;
    }

    public void setPortrait_uri(String portrait_uri) {
        this.portrait_uri = portrait_uri;
    }

    public void setUser_note(String user_note) {
        this.user_note = user_note;
    }

    public void setUnread_comment_count(float unread_comment_count) {
        this.unread_comment_count = unread_comment_count;
    }

    public void setUnread_praise_count(float unread_praise_count) {
        this.unread_praise_count = unread_praise_count;
    }

    public void setUnread_invitation_count(float unread_invitation_count) {
        this.unread_invitation_count = unread_invitation_count;
    }

    public void setUnread_attention_count(float unread_attention_count) {
        this.unread_attention_count = unread_attention_count;
    }

    public void setUnread_atme_count(float unread_atme_count) {
        this.unread_atme_count = unread_atme_count;
    }

    public void setUnread_system_count(float unread_system_count) {
        this.unread_system_count = unread_system_count;
    }

    public void setUser_authentic(String user_authentic) {
        this.user_authentic = user_authentic;
    }

    public void setQuestion_price(long question_price) {
        this.question_price = question_price;
    }

    public void setQuestion_all_total(int question_all_total) {
        this.question_all_total = question_all_total;
    }

    public float getUnread_comment_count() {
        return unread_comment_count;
    }

    public float getUnread_praise_count() {
        return unread_praise_count;
    }

    public float getUnread_invitation_count() {
        return unread_invitation_count;
    }

    public float getUnread_attention_count() {
        return unread_attention_count;
    }

    public float getUnread_atme_count() {
        return unread_atme_count;
    }

    public float getUnread_system_count() {
        return unread_system_count;
    }

    public String getUser_name() {
        if (null == user_name) user_name = "";
        return user_name;
    }

    public String getPortrait_state() {
        return portrait_state;
    }

    public String getFace_sent_count() {
        return face_sent_count;
    }

    public String getAchievement_video_count() {
        return CommonUtil.formatCount(achievement_video_count);
    }

    public String getAttention_count() {
        return CommonUtil.formatCount(attention_count);
    }

    public long getAttention_counts() {
        return attention_count;
    }

    public void setAttention_count(long attention_count) {
        this.attention_count = attention_count;
    }

    public int getUser_gender() {
        return user_gender;
    }

    public String getGift_received_count() {
        return gift_received_count;
    }

    public String getUser_state() {
        return user_state;
    }

    public long getUser_birthday() {
        return user_birthday;
    }

    public String getFans_count() {
        return CommonUtil.formatCount(fans_count);
    }


    public int getUser_type() {
        return user_type;
    }

    public String getUser_area() {
        return user_area;
    }

    public String getPortrait_uri() {
        return portrait_uri + ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public boolean isAttention_state() {
        return attention_state == 1;
    }

    public void setAttention_state(boolean attention_state) {
        this.attention_state = attention_state ? 1 : 0;
    }

    public String getUser_note() {
        return user_note;
    }


    public String getFans_new_count() {
        return fans_new_count;
    }

    public void setFans_new_count(String fans_new_count) {
        this.fans_new_count = fans_new_count;
    }

    public String getGift_received_new_count() {
        return gift_received_new_count;
    }

    public void setGift_received_new_count(String gift_received_new_count) {
        this.gift_received_new_count = gift_received_new_count;
    }

    public String getPraise_sent_count() {
        return praise_sent_count;
    }

    public void setPraise_sent_count(String praise_sent_count) {
        this.praise_sent_count = praise_sent_count;
    }

    public String getUser_authentic() {
        return user_authentic;
    }

    public boolean getAnswer_droit() {
        return answer_droit == 1;
    }

    public void setAnswer_droit(int answer_droit) {
        this.answer_droit = answer_droit;
    }

    public long getQuestion_price() {
        return question_price;
    }

    public String getDoWithQuestion_price() {
        return CommonUtil.formatPrice(question_price);
    }

    public String getQuestion_all_total() {
        return CommonUtil.formatCount(question_all_total);
    }

    public long getAnswer_total() {
        return answer_total;
    }

    public long getPlay_total() {
        return play_total;
    }


    public String getUser_id() {
        if (null == user_id) user_id = "";
        return user_id;
    }

    public String getMineDataPortrait_uri() {
        return portrait_uri;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getAttention_state() {
        return attention_state;
    }

    public void setAttention_state(int attention_state) {
        this.attention_state = attention_state;
    }


    public void setPlay_total(long play_total) {
        this.play_total = play_total;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeInt(this.attention_state);
        dest.writeString(this.portrait_state);
        dest.writeString(this.face_sent_count);
        dest.writeLong(this.achievement_video_count);
        dest.writeString(this.user_name);
        dest.writeLong(this.attention_count);
        dest.writeInt(this.user_gender);
        dest.writeString(this.gift_received_count);
        dest.writeString(this.user_state);
        dest.writeLong(this.user_birthday);
        dest.writeString(this.user_birthdayStr);
        dest.writeLong(this.fans_count);
        dest.writeInt(this.user_type);
        dest.writeString(this.user_area);
        dest.writeString(this.portrait_uri);
        dest.writeString(this.user_note);
        dest.writeFloat(this.unread_comment_count);
        dest.writeFloat(this.unread_praise_count);
        dest.writeFloat(this.unread_invitation_count);
        dest.writeFloat(this.unread_attention_count);
        dest.writeFloat(this.unread_atme_count);
        dest.writeFloat(this.unread_system_count);
        dest.writeString(this.fans_new_count);
        dest.writeString(this.gift_received_new_count);
        dest.writeString(this.praise_sent_count);
        dest.writeString(this.user_authentic);
        dest.writeByte(this.price_allow_modify ? (byte) 1 : (byte) 0);
        dest.writeInt(this.answer_droit);
        dest.writeLong(this.question_price);
        dest.writeInt(this.question_all_total);
        dest.writeLong(this.answer_total);
        dest.writeLong(this.play_total);
        dest.writeString(this.background_uri);
    }

    public UserInfoBean() {
    }

    protected UserInfoBean(Parcel in) {
        this.user_id = in.readString();
        this.attention_state = in.readInt();
        this.portrait_state = in.readString();
        this.face_sent_count = in.readString();
        this.achievement_video_count = in.readLong();
        this.user_name = in.readString();
        this.attention_count = in.readLong();
        this.user_gender = in.readInt();
        this.gift_received_count = in.readString();
        this.user_state = in.readString();
        this.user_birthday = in.readLong();
        this.user_birthdayStr = in.readString();
        this.fans_count = in.readLong();
        this.user_type = in.readInt();
        this.user_area = in.readString();
        this.portrait_uri = in.readString();
        this.user_note = in.readString();
        this.unread_comment_count = in.readFloat();
        this.unread_praise_count = in.readFloat();
        this.unread_invitation_count = in.readFloat();
        this.unread_attention_count = in.readFloat();
        this.unread_atme_count = in.readFloat();
        this.unread_system_count = in.readFloat();
        this.fans_new_count = in.readString();
        this.gift_received_new_count = in.readString();
        this.praise_sent_count = in.readString();
        this.user_authentic = in.readString();
        this.price_allow_modify = in.readByte() != 0;
        this.answer_droit = in.readInt();
        this.question_price = in.readLong();
        this.question_all_total = in.readInt();
        this.answer_total = in.readLong();
        this.play_total = in.readLong();
        this.background_uri = in.readString();
    }

    public static final Creator<UserInfoBean> CREATOR = new Creator<UserInfoBean>() {
        @Override
        public UserInfoBean createFromParcel(Parcel source) {
            return new UserInfoBean(source);
        }

        @Override
        public UserInfoBean[] newArray(int size) {
            return new UserInfoBean[size];
        }
    };
}
