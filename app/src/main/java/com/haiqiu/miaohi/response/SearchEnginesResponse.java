package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.SearchEnginesData;

/**
 * Created by LiuXiang on 2016/12/16.
 */
public class SearchEnginesResponse extends BaseResponse{
    SearchEnginesData data;

    public SearchEnginesData getData() {
        return data;
    }
}
