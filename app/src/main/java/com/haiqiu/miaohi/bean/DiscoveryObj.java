package com.haiqiu.miaohi.bean;

import java.util.List;

/**
 * Created by LiuXiang on 2016/12/12.
 */
public class DiscoveryObj {
    private List<DiscoveryBannerObj> banner_list;
    private List<DiscoveryUserObj> interest_user_list;
    private List<DiscoveryObjectObj> object_list;
    private List<DiscoveryLabelObj> kind_tag_list;

    public List<DiscoveryBannerObj> getBanner_list() {
        return banner_list;
    }

    public List<DiscoveryUserObj> getInterest_user_list() {
        return interest_user_list;
    }

    public List<DiscoveryObjectObj> getObject_list() {
        return object_list;
    }

    public List<DiscoveryLabelObj> getLabel_list() {
        return kind_tag_list;
    }
}
