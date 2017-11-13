package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.utils.CommonUtil;

/**
 * Created by miaohi on 2016/6/2.
 */
public class HomeActivityPageResult {
    private int player_count;
    private int video_count;
    private int icon_state;
    private int recommend;
    private String activity_id;
    private String activity_note;
    private String icon_uri;

    private String doWithPlayerCount;
    private String doWithVideoCount;

    public int getPlayer_count() {
        return player_count;
    }

    public String getDoWithPlayerCount() {
        if (null == doWithPlayerCount) doWithPlayerCount = CommonUtil.formatCount(player_count);
        return doWithPlayerCount;
    }


    public int getVideo_count() {
        return video_count;
    }

    public String getDoWithVideoCount() {
        if (null == doWithVideoCount) doWithVideoCount = CommonUtil.formatCount(video_count);
        return doWithVideoCount;
    }

    public int getIcon_state() {
        return icon_state;
    }

    public int getRecommend() {
        return recommend;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public String getActivity_note() {
        return activity_note;
    }

    public String getIcon_uri() {
        return icon_uri;
    }
}
