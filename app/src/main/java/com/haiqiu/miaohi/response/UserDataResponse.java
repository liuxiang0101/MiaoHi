package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.UserData;

import java.util.List;

/**
 * Created by zhandalin on 2016-12-19 15:18.
 * 说明:
 */
public class UserDataResponse extends BaseResponse {
    public UserDataInfo data;

    public class UserDataInfo {
        public int all_attention_count;
        public int all_fans_count;
        public String server_synch_time;
        public List<UserData> page_result;
    }

}
