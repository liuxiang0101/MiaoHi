package com.haiqiu.miaohi.bean;

/**
 * 视频详情收到的礼物
 * Created by ningl on 16/9/5.
 */
public class VideoDetailReceiveGiftBean {

    /**
     * sender_id : xxxxx
     * sender_type : xxxxx
     * sender_name : xxxx
     * sender_portrait_uri : xxxx
     * sender_portrait_state : xxxx
     * sender_gender : xxxx
     * sender_note : xxxx
     * sender_fans_count : xxxx
     * sender_attention_state : xxxx
     * sender_question_auth : xxxx
     * gift_id : xxxxx
     * gift_type : xxxx
     * gift_name : xxxxx
     * gift_icon_uri : xxxxxx
     * gift_icon_state : xxxxx
     * gift_icon_role : xxxxx
     * gift_state : xxxx
     * send_gitf_time : xxxx
     */

    private String sender_id;
    private int sender_type;
    private String sender_name;
    private String sender_portrait_uri;
    private String sender_portrait_state;
    private int sender_gender;
    private String sender_note;
    private String sender_fans_count;
    private String sender_attention_state;
    private boolean sender_question_auth;
    private String gift_id;
    private String gift_type;
    private String gift_name;
    private String gift_icon_uri;
    private String gift_icon_state;
    private String gift_icon_role;
    private String gift_state;
    private long send_gift_time;
    private String sender_birthday;
    private String send_gift_time_text;

    public String getSend_gift_time_text() {
        return send_gift_time_text;
    }

    public void setSend_gift_time_text(String send_gitf_time_text) {
        this.send_gift_time_text = send_gitf_time_text;
    }

    public void setSender_question_auth(boolean sender_question_auth) {
        this.sender_question_auth = sender_question_auth;
    }

    public boolean isSender_question_auth() {
        return sender_question_auth;
    }

    public String getSender_birthday() {
        return sender_birthday;
    }

    public void setSender_birthday(String sender_birthday) {
        this.sender_birthday = sender_birthday;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public int getSender_type() {
        return sender_type;
    }

    public void setSender_type(int sender_type) {
        this.sender_type = sender_type;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_portrait_uri() {
        return sender_portrait_uri;
    }

    public void setSender_portrait_uri(String sender_portrait_uri) {
        this.sender_portrait_uri = sender_portrait_uri;
    }

    public String getSender_portrait_state() {
        return sender_portrait_state;
    }

    public void setSender_portrait_state(String sender_portrait_state) {
        this.sender_portrait_state = sender_portrait_state;
    }

    public int getSender_gender() {
        return sender_gender;
    }

    public void setSender_gender(int sender_gender) {
        this.sender_gender = sender_gender;
    }

    public String getSender_note() {
        return sender_note;
    }

    public void setSender_note(String sender_note) {
        this.sender_note = sender_note;
    }

    public String getSender_fans_count() {
        return sender_fans_count;
    }

    public void setSender_fans_count(String sender_fans_count) {
        this.sender_fans_count = sender_fans_count;
    }

    public String getSender_attention_state() {
        return sender_attention_state;
    }

    public void setSender_attention_state(String sender_attention_state) {
        this.sender_attention_state = sender_attention_state;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getGift_type() {
        return gift_type;
    }

    public void setGift_type(String gift_type) {
        this.gift_type = gift_type;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public String getGift_icon_uri() {
        return gift_icon_uri;
    }

    public void setGift_icon_uri(String gift_icon_uri) {
        this.gift_icon_uri = gift_icon_uri;
    }

    public String getGift_icon_state() {
        return gift_icon_state;
    }

    public void setGift_icon_state(String gift_icon_state) {
        this.gift_icon_state = gift_icon_state;
    }

    public String getGift_icon_role() {
        return gift_icon_role;
    }

    public void setGift_icon_role(String gift_icon_role) {
        this.gift_icon_role = gift_icon_role;
    }

    public String getGift_state() {
        return gift_state;
    }

    public void setGift_state(String gift_state) {
        this.gift_state = gift_state;
    }

    public long getSend_gitf_time() {
        return send_gift_time;
    }

    public void setSend_gitf_time(long send_gitf_time) {
        this.send_gift_time = send_gitf_time;
    }
}
