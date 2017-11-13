package com.haiqiu.miaohi.bean;

/**
 * Created by zhandalin on 2016-12-19 15:17.
 * 说明:用来个人的关注数据(我关注的,关注我的,联系人)
 */
public class UserData {
    public String user_id;//用户id
    public int attention_state;//关注状态

    public int user_type;
    public String user_name;
    public String user_name_pinyin;//用户的全拼音
    public String user_name_char;//用户名字每个字的首字母
    public String user_name_head_char;//用户名字首字母
    public String portrait_uri;
    public int portrait_state;
    public long attention_time;
    public String attention_time_text;

    public int user_gender;
    public String user_authentic;
    public int answer_auth;

    public boolean isSelected;

    public boolean hasAnswer_auth() {
        return answer_auth == 1;
    }

    public void setAttention_state(boolean attention_state) {
        this.attention_state = attention_state ? 1 : 0;
    }

    public boolean isAttention_state() {
        return attention_state==1;
    }
}
