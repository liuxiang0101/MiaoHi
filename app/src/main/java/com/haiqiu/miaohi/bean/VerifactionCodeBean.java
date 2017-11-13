package com.haiqiu.miaohi.bean;

import java.io.Serializable;

/**
 * 发送验证码返回
 * Created by ningl on 2016/5/25.
 */
public class VerifactionCodeBean implements Serializable{


    /**
     * server_vername : 1.0.1
     * server_vercode : 1
     * command : smscode
     * status : 0
     */

    private String server_vername;
    private String server_vercode;
    private String command;
    private String status;

    public String getServer_vername() {
        return server_vername;
    }

    public void setServer_vername(String server_vername) {
        this.server_vername = server_vername;
    }

    public String getServer_vercode() {
        return server_vercode;
    }

    public void setServer_vercode(String server_vercode) {
        this.server_vercode = server_vercode;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
