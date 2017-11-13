package com.haiqiu.miaohi.bean;

/**
 * Created by zhandalin on 2016-05-20 15:46.
 * 说明:
 */
public class ServerResponseBaseInfo {
    private String server_vername;
    private String server_vercode;
    private String command;
    private int status;
    private String errtext;
    private String exception;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrtext() {
        return errtext;
    }

    public void setErrtext(String errtext) {
        this.errtext = errtext;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
