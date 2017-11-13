package com.haiqiu.miaohi.bean;

import java.util.List;

/**
 * Created by LiuXiang on 2016/12/13.
 */
public class ChoiceQaData {
    List<DiscoveryBannerObj>banner_result;
    List<ChoiceQaPageResult>page_result;

    public List<DiscoveryBannerObj> getBanner_result() {
        return banner_result;
    }

    public List<ChoiceQaPageResult> getPage_result() {
        return page_result;
    }
}
