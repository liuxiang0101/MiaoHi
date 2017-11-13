package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.SearchAllData;
import com.haiqiu.miaohi.response.BaseResponse;

/**
 * 搜索好友
 * Created by miaohi on 2016/5/31.
 */
public class SearchAllResponse extends BaseResponse{
    private SearchAllData data;

    public SearchAllData getData() {
        return data;
    }
}
