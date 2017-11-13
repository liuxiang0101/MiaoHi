package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;

/**
 * Created by zhandalin on 2016-07-22 10:45.
 * 说明:
 */
public class AskAndAnswerPageResult {
    private String question_id;
    private String question_text;
    private String question_time;
    private int question_private;
    private String question_user_id;
    private int question_user_type;
    private String question_user_name;
    private String question_user_state;
    private String answer_user_id;
    private int answer_user_type;
    private String answer_user_name;
    private String answer_user_state;
    private String answer_portrait_uri;
    private String answer_portrait_state;
    private String video_id;
    private String video_time;
    private String cover_uri;
    private String cover_state;
    private long play_total;
    private long video_price;
    private int observe_state;
    private String user_authentic;
    private String answer_authentic;
    private String answer_user_authentic;
    private int question_type;
    private String video_time_text;


    private String doWithVideoPrice;
    private String doWithLookCount;
    private String doWithLookCountTime;

    public String getVideo_time_text() {
        return video_time_text;
    }

    public void setVideo_time_text(String video_time_text) {
        this.video_time_text = video_time_text;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public String getQuestion_time() {
        return question_time;
    }

    public boolean isQuestion_private() {
        return question_private == 1;
    }

    public String getQuestion_user_id() {
        return question_user_id;
    }

    public int getQuestion_user_type() {
        return question_user_type;
    }

    public String getQuestion_user_name() {
        return question_user_name;
    }

    public String getQuestion_user_state() {
        return question_user_state;
    }

    public String getAnswer_user_id() {
        return answer_user_id;
    }

    public int getAnswer_user_type() {
        return answer_user_type;
    }

    public String getAnswer_user_name() {
        return answer_user_name;
    }

    public String getAnswer_portrait_uri() {
        return answer_portrait_uri+ ConstantsValue.Other.USER_HEAD_PARAM;
    }

    public String getVideo_id() {
        return video_id;
    }


    public String getCover_uri() {
        if (null != cover_uri && cover_uri.contains("?")) {
            return cover_uri + ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM_FRAME;
        }
        return cover_uri+ConstantsValue.Other.VIDEO_MIN_PREVIEW_PICTURE_PARAM;
    }

    public String getUser_authentic() {
        return user_authentic;
    }

    public long getPlay_total() {
        return play_total;
    }

    public long getVideo_price() {
        return video_price;
    }

    public boolean getObserve_state() {//是否已经支付过
        return observe_state == 1;
    }


    public String getDoWithLookCount() {
        if (null == doWithLookCount)
            doWithLookCount = CommonUtil.formatCount(play_total);
        return doWithLookCount;
    }

    public String getAnswer_user_authentic() {
        return answer_user_authentic;
    }


    public int getQuestion_type() {
        return question_type;
    }

}
