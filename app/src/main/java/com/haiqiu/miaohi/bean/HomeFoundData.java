package com.haiqiu.miaohi.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhandalin on 2016-05-23 20:46.
 * 说明:
 */
public class HomeFoundData {
    private List<FoundActivityResultArrayObj> banner_result;
    private ArrayList<HomeFoundPageResult> page_result;
    private TopVipObj topvip_result;
    private TopVipObj tophot_result;
    private TopVipObj topattract_result;
    private TopVipObj topamaze_result;
    private TopVipObj toplike_result;

    public ArrayList<HomeFoundPageResult> getPage_result() {
        return page_result;
    }

    public void setPage_result(ArrayList<HomeFoundPageResult> page_result) {
        this.page_result = page_result;
    }

    public List<FoundActivityResultArrayObj> getBanner_result() {
        return banner_result;
    }

    public void setBanner_result(List<FoundActivityResultArrayObj> banner_result) {
        this.banner_result = banner_result;
    }

    public TopVipObj getTopvip_result() {
        return topvip_result;
    }

    public void setTopvip_result(TopVipObj topvip_result) {
        this.topvip_result = topvip_result;
    }

    public TopVipObj getTophot_result() {
        return tophot_result;
    }

    public void setTophot_result(TopVipObj tophot_result) {
        this.tophot_result = tophot_result;
    }

    public TopVipObj getTopattract_result() {
        return topattract_result;
    }

    public void setTopattract_result(TopVipObj topattract_result) {
        this.topattract_result = topattract_result;
    }

    public TopVipObj getTopamaze_result() {
        return topamaze_result;
    }

    public void setTopamaze_result(TopVipObj topamaze_result) {
        this.topamaze_result = topamaze_result;
    }

    public TopVipObj getToplike_result() {
        return toplike_result;
    }

    public void setToplike_result(TopVipObj toplike_result) {
        this.toplike_result = toplike_result;
    }
}
