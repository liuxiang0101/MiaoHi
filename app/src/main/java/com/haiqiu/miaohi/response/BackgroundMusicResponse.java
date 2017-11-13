package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.MusicInfo;

import java.util.List;

/**
 * Created by zhandalin on 2016-12-27 15:47.
 * 说明:
 */
public class BackgroundMusicResponse extends BaseResponse {
    public BackgroundMusicData data;

    public class BackgroundMusicData {
        public List<MusicInfo> page_result;
    }
}
