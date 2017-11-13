package com.haiqiu.miaohi.bean;

/**
 * Created by miaohi on 2016/5/23.
 */
public class InviteResultArrayObj {
    private String activity_id;
    private String activity_name;
    private String sender_id;
    private String sender_name;
    private String receiver_id;
    private String receiver_name;

    public String getActivity_id() {
        return activity_id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public String getSender_id() {
        return sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    @Override
    public String toString() {
        return "InviteResultArrayObj{" +
                "activity_id='" + activity_id + '\'' +
                ", activity_name='" + activity_name + '\'' +
                ", sender_id='" + sender_id + '\'' +
                ", sender_name='" + sender_name + '\'' +
                ", receiver_id='" + receiver_id + '\'' +
                ", receiver_name='" + receiver_name + '\'' +
                '}';
    }
}
