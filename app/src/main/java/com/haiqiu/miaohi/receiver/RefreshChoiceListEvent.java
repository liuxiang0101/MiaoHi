package com.haiqiu.miaohi.receiver;

/**
 * 精选榜围观状态刷新事件
 * Created by hackest on 16/8/1.
 */
public class RefreshChoiceListEvent {
    private String question_id;
    private int position;


    public RefreshChoiceListEvent(String question_id, int position) {
        this.question_id = question_id;
        this.position = position;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
