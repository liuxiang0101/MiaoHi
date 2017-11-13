package com.haiqiu.miaohi.bean;

/**
 * 账户绑定所需数据
 * Created by ningl on 16/9/1.
 */
public class AccountBindInfo {


    /**
     * account_type : xxxxxxxx
     * mobile_code : xxxxx
     * mobile_number : xxxxx
     * password : xxxxx
     * verify_number : xxxxx
     * login_name_id : xxxx
     * weixin_union_id : xxxx
     * weixin_open_id : xxxx
     * nick_name : xxxx
     */

    private int account_type;
    private String mobile_code;
    private String mobile_number;
    private String password;
    private String verify_number;
    private String login_name_id;
    private String weixin_union_id;
    private String weixin_open_id;
    private String nick_name;


    public void setAccount_type(int account_type) {
        this.account_type = account_type;
    }



    public String getLogin_name_id() {
        return login_name_id;
    }

    public void setLogin_name_id(String login_name_id) {
        this.login_name_id = login_name_id;
    }

    public String getWeixin_union_id() {
        return weixin_union_id;
    }

    public void setWeixin_union_id(String weixin_union_id) {
        this.weixin_union_id = weixin_union_id;
    }

    public String getWeixin_open_id() {
        return weixin_open_id;
    }

    public void setWeixin_open_id(String weixin_open_id) {
        this.weixin_open_id = weixin_open_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }
}
