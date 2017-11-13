package com.haiqiu.miaohi.bean;

/**
 * Created by zhandalin on 2016-07-25 15:29.
 * 说明:
 */
public class PayInfoData {

    /**
     * deposit_id：秒嗨后台生成的充值单号<微信，支付宝，iap>使用
     *
     * wx_prepay_id：微信服务器返回给客户端调用支付使用，可选字段  <微信>使用,
     * wx_sign:签名内容，可选字段 <微信>使用
     * wx_nonce_str:随机数（32位）可选字段	<微信>使用
     * wx_time_stamp:时间戳，可选字段 <微信>使用，
     * wx_app_id:微信开放平台审核通过的应用appid 可选字段 ，<微信>使用
     * wx_partner_id: 微信支付分配的商户号 可选字段，<微信>使用
     * wx_package_info:扩展字段 可选字段，<微信>使用
     */
    private String deposit_id;

    private String wx_prepay_id;
    private String wx_sign;
    private String wx_nonce_str;
    private String wx_time_stamp;
    private String wx_app_id;
    private String wx_partner_id;
    private String wx_package_info;

    private String alipay_info;

    public String getDeposit_id() {
        return deposit_id;
    }

    public void setDeposit_id(String deposit_id) {
        this.deposit_id = deposit_id;
    }

    public String getWx_prepay_id() {
        return wx_prepay_id;
    }

    public void setWx_prepay_id(String wx_prepay_id) {
        this.wx_prepay_id = wx_prepay_id;
    }

    public String getWx_sign() {
        return wx_sign;
    }

    public void setWx_sign(String wx_sign) {
        this.wx_sign = wx_sign;
    }

    public String getWx_nonce_str() {
        return wx_nonce_str;
    }

    public void setWx_nonce_str(String wx_nonce_str) {
        this.wx_nonce_str = wx_nonce_str;
    }

    public String getWx_time_stamp() {
        return wx_time_stamp;
    }

    public void setWx_time_stamp(String wx_time_stamp) {
        this.wx_time_stamp = wx_time_stamp;
    }

    public String getWx_app_id() {
        return wx_app_id;
    }

    public void setWx_app_id(String wx_app_id) {
        this.wx_app_id = wx_app_id;
    }

    public String getWx_partner_id() {
        return wx_partner_id;
    }

    public void setWx_partner_id(String wx_partner_id) {
        this.wx_partner_id = wx_partner_id;
    }

    public String getWx_package_info() {
        return wx_package_info;
    }

    public void setWx_package_info(String wx_package_info) {
        this.wx_package_info = wx_package_info;
    }

    public String getAlipay_info() {
        return alipay_info;
    }

    public void setAlipay_info(String alipay_info) {
        this.alipay_info = alipay_info;
    }
}
