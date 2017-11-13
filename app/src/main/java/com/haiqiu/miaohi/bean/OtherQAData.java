package com.haiqiu.miaohi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.MHStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ningl on 16/12/9.
 */
public class OtherQAData implements Parcelable {
    /**
     * user_name : xxxx
     * user_portrait : xxxx
     * user_vip_note : xxxx
     * user_vip_remarks : xxxx
     * user_gender : xxxx
     * user_type : xxxx
     * attention_state : xxxxx
     * income_amount : xxxxx
     * answered_amount : xxxx
     * observed_amount : xxxx
     * question_cost : xxxx
     */

    private String user_name;
    private String user_portrait;
    private String vip_note;
    private String user_note;
    private String user_vip_remarks;
    private int user_gender;
    private int user_type;
    private int attention_state;
    private long income_amount;
    private int answered_amount;
    private int observed_amount;
    private long question_cost;
    private List<OthterQAInfo> page_result;
    private String share_link_address;

    public String getShare_link_address() {
        if(MHStringUtils.isEmpty(share_link_address)){
            return ConstantsValue.Shared.APPDOWNLOAD;
        }
        return share_link_address;
    }

    public void setShare_link_address(String share_link_address) {
        this.share_link_address = share_link_address;
    }

    public void setAttention_state(int attention_state) {
        this.attention_state = attention_state;
    }

    public List<OthterQAInfo> getPage_result() {
        return page_result;
    }

    public void setPage_result(List<OthterQAInfo> page_result) {
        this.page_result = page_result;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_portrait() {
        return user_portrait;
    }

    public void setUser_portrait(String user_portrait) {
        this.user_portrait = user_portrait;
    }

    public String getVip_note() {
        return vip_note;
    }

    public String getUser_note() {
        return user_note;
    }

    public String getUser_vip_remarks() {
        return user_vip_remarks;
    }

    public void setUser_vip_remarks(String user_vip_remarks) {
        this.user_vip_remarks = user_vip_remarks;
    }

    public int getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(int user_gender) {
        this.user_gender = user_gender;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public boolean getAttention_state() {
        return attention_state == 1;
    }

    public void setAttention_state(boolean attention_state) {
        this.attention_state = attention_state ? 1 : 0;
    }

    public long getIncome_amount() {
        return income_amount;
    }

    public void setIncome_amount(long income_amount) {
        this.income_amount = income_amount;
    }

    public int getAnswered_amount() {
        return answered_amount;
    }

    public void setAnswered_amount(int answered_amount) {
        this.answered_amount = answered_amount;
    }

    public int getObserved_amount() {
        return observed_amount;
    }

    public void setObserved_amount(int observed_amount) {
        this.observed_amount = observed_amount;
    }

    public long getQuestion_cost() {
        return question_cost;
    }

    public void setQuestion_cost(long question_cost) {
        this.question_cost = question_cost;
    }


    public OtherQAData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_name);
        dest.writeString(this.user_portrait);
        dest.writeString(this.vip_note);
        dest.writeString(this.user_note);
        dest.writeString(this.user_vip_remarks);
        dest.writeInt(this.user_gender);
        dest.writeInt(this.user_type);
        dest.writeInt(this.attention_state);
        dest.writeLong(this.income_amount);
        dest.writeInt(this.answered_amount);
        dest.writeInt(this.observed_amount);
        dest.writeLong(this.question_cost);
        dest.writeList(this.page_result);
        dest.writeString(this.share_link_address);
    }

    protected OtherQAData(Parcel in) {
        this.user_name = in.readString();
        this.user_portrait = in.readString();
        this.vip_note = in.readString();
        this.user_note = in.readString();
        this.user_vip_remarks = in.readString();
        this.user_gender = in.readInt();
        this.user_type = in.readInt();
        this.attention_state = in.readInt();
        this.income_amount = in.readLong();
        this.answered_amount = in.readInt();
        this.observed_amount = in.readInt();
        this.question_cost = in.readLong();
        this.page_result = new ArrayList<OthterQAInfo>();
        in.readList(this.page_result, OthterQAInfo.class.getClassLoader());
        this.share_link_address = in.readString();
    }

    public static final Creator<OtherQAData> CREATOR = new Creator<OtherQAData>() {
        @Override
        public OtherQAData createFromParcel(Parcel source) {
            return new OtherQAData(source);
        }

        @Override
        public OtherQAData[] newArray(int size) {
            return new OtherQAData[size];
        }
    };
}
