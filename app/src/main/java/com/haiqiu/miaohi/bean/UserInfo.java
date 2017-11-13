package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;

/**
 * 用户信息
 * Created by ningl on 16/12/5.
 */
public class UserInfo implements Parcelable {


    /**
     * user_name : xxxxx
     * user_type : xx
     * user_state : x
     * user_gender : x
     * user_area : xxxxx
     * user_birthday : 1985-04-09
     * answer_auth : true
     * user_authentic : xxx
     * portrait_uri : xxxxx
     * content_count : xxxx
     * attention_count : xxxxx
     * fans_count : xxxxx
     * attention_state : xxxx
     * background_uri : xxxxx
     */

    protected String video_id;//视频id
    protected boolean praise_state;//视频点赞状态
    protected long praise_count;
    public String user_id;//用户id
    public int attention_state;//关注状态

    private String user_name;
    private int user_type;
    private int user_state;
    private int user_gender;
    private String user_area;
    private String user_birthday;
    private int answer_auth;
    private String user_authentic;
    private String portrait_uri;
    private int content_count;
    private int attention_count;
    private int fans_count;
    private String background_uri;
    private String UserId;
    private String share_link_address;
    private long register_time;

    public long getRegister_time() {
        return register_time;
    }

    public void setRegister_time(long register_time) {
        this.register_time = register_time;
    }

    public int getAnswer_auth() {
        return answer_auth;
    }

    public void setAnswer_auth(int answer_auth) {
        this.answer_auth = answer_auth;
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

    public boolean isAnswer_auth() {
        return answer_auth == 1;
    }

    public void setAnswer_auth(boolean answer_auth) {
        this.answer_auth = answer_auth ? 1 : 0;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getUser_state() {
        return user_state;
    }

    public void setUser_state(int user_state) {
        this.user_state = user_state;
    }

    public int getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(int user_gender) {
        this.user_gender = user_gender;
    }

    public String getUser_area() {
        return user_area;
    }

    public void setUser_area(String user_area) {
        this.user_area = user_area;
    }

    public String getUser_birthday() {
        return user_birthday;
    }

    public void setUser_birthday(String user_birthday) {
        this.user_birthday = user_birthday;
    }

    public String getUser_authentic() {
        return user_authentic;
    }

    public void setUser_authentic(String user_authentic) {
        this.user_authentic = user_authentic;
    }

    public String getPortrait_uri() {
        return portrait_uri;
    }

    public void setPortrait_uri(String portrait_uri) {
        this.portrait_uri = portrait_uri;
    }

    public int getContent_count() {
        return content_count;
    }

    public void setContent_count(int content_count) {
        this.content_count = content_count;
    }

    public int getAttention_count() {
        return attention_count;
    }

    public void setAttention_count(int attention_count) {
        this.attention_count = attention_count;
    }

    public int getFans_count() {
        return fans_count;
    }

    public void setFans_count(int fans_count) {
        this.fans_count = fans_count;
    }

    public boolean getAttention_state() {
        return attention_state == 1;
    }

    public void setAttention_state(boolean attention_state) {
        this.attention_state = attention_state ? 1 : 0;
    }

    public String getBackground_uri() {
        return background_uri;
    }

    public void setBackground_uri(String background_uri) {
        this.background_uri = background_uri;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public boolean isPraise_state() {
        return praise_state;
    }

    public void setPraise_state(boolean praise_state) {
        this.praise_state = praise_state;
    }

    public String getPraise_count() {
        return CommonUtil.formatCount(praise_count);
    }

    public void setPraise_count(long praise_count) {
        this.praise_count = praise_count;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setAttention_state(int attention_state) {
        this.attention_state = attention_state;
    }
    public boolean isAttention_state() {
        return attention_state == 1;
    }

    public UserInfo() {
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
        dest.writeString(this.user_name);
        dest.writeInt(this.user_type);
        dest.writeInt(this.user_state);
        dest.writeInt(this.user_gender);
        dest.writeString(this.user_area);
        dest.writeString(this.user_birthday);
        dest.writeInt(this.answer_auth);
        dest.writeString(this.user_authentic);
        dest.writeString(this.portrait_uri);
        dest.writeInt(this.content_count);
        dest.writeInt(this.attention_count);
        dest.writeInt(this.fans_count);
        dest.writeString(this.background_uri);
        dest.writeString(this.UserId);
        dest.writeString(this.share_link_address);
        dest.writeLong(this.register_time);
    }

    protected UserInfo(Parcel in) {
        this.video_id = in.readString();
        this.praise_state = in.readByte() != 0;
        this.praise_count = in.readLong();
        this.user_id = in.readString();
        this.attention_state = in.readInt();
        this.user_name = in.readString();
        this.user_type = in.readInt();
        this.user_state = in.readInt();
        this.user_gender = in.readInt();
        this.user_area = in.readString();
        this.user_birthday = in.readString();
        this.answer_auth = in.readInt();
        this.user_authentic = in.readString();
        this.portrait_uri = in.readString();
        this.content_count = in.readInt();
        this.attention_count = in.readInt();
        this.fans_count = in.readInt();
        this.background_uri = in.readString();
        this.UserId = in.readString();
        this.share_link_address = in.readString();
        this.register_time = in.readLong();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };


}
