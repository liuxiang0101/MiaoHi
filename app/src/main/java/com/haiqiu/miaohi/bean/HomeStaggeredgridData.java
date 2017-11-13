package com.haiqiu.miaohi.bean;

import java.util.List;

/**
 * Created by ningl on 2016/6/17.
 */
public class HomeStaggeredgridData {

    private String activity_works_id;
    private String activity_sponsor_icon;
    private String activity_sponsor_id;
    private String activity_sponsor_nickname;
    private String activity_banner;
    private String activity_sponsor_gender;
    private String activity_banner_h5_uri;
    private String activity_banner_h5_uri_note;
    private int activity_publish_state;


    private List<HomeStaggereedgridResult> page_result;
    private List<BannerBean> banner_result;
    /**
     * activity_works_type : 2
     * activity_banner_state : 0
     * activity_player_count : 6
     * page_index : 0
     * activity_detail_uri_state : 10
     * activity_detail_uri : http://image.test.miaohi.com/demo_video_cover_shangbuqi
     * activity_detail_note : 星光大道比赛32
     * activity_video_count : 9
     * page_size : 10
     */

    private String activity_works_type;
    private String activity_banner_state;
    private String activity_player_count;
    private String page_index;
    private String activity_detail_uri_state;
    private String activity_detail_uri;
    private String activity_detail_note;
    private String activity_video_count;
    private String page_size;

    public String getActivity_banner_h5_uri() {
        return activity_banner_h5_uri;
    }

    public List<BannerBean> getBanner_result() {
        return banner_result;
    }

    public void setBanner_result(List<BannerBean> banner_result) {
        this.banner_result = banner_result;
    }

    /**
     * activity_works_id : VIDE-613c39ab-0b56-11e6-9003-44a8424640fc
     * activity_detail_note : 星光大道比赛32星光大道比赛32星光大道比赛32星光大道比赛32星光大道比赛32
     * activity_sponsor_icon : http://7xt99c.com2.z0.glb.qiniucdn.com/portrait_demo_woman_3.png
     * activity_sponsor_id : USER-vipspeci-062a-11e6-9003-44a8424640fd
     * activity_sponsor_nickname : specialvip1
     */



    public List<HomeStaggereedgridResult> getPage_result() {
        return page_result;
    }

    public String getActivity_banner_h5_uri_note() {
        return activity_banner_h5_uri_note;
    }

    public void setPage_result(List<HomeStaggereedgridResult> page_result) {
        this.page_result = page_result;
    }

    public String getActivity_works_type() {
        return activity_works_type;
    }

    public void setActivity_works_type(String activity_works_type) {
        this.activity_works_type = activity_works_type;
    }

    public String getActivity_banner_state() {
        return activity_banner_state;
    }

    public void setActivity_banner_state(String activity_banner_state) {
        this.activity_banner_state = activity_banner_state;
    }

    public String getActivity_player_count() {
        return activity_player_count;
    }

    public void setActivity_player_count(String activity_player_count) {
        this.activity_player_count = activity_player_count;
    }

    public String getPage_index() {
        return page_index;
    }

    public void setPage_index(String page_index) {
        this.page_index = page_index;
    }

    public String getActivity_detail_uri_state() {
        return activity_detail_uri_state;
    }

    public void setActivity_detail_uri_state(String activity_detail_uri_state) {
        this.activity_detail_uri_state = activity_detail_uri_state;
    }

    public String getActivity_detail_uri() {
        return activity_detail_uri;
    }

    public void setActivity_detail_uri(String activity_detail_uri) {
        this.activity_detail_uri = activity_detail_uri;
    }


    public String getActivity_video_count() {
        return activity_video_count;
    }

    public void setActivity_video_count(String activity_video_count) {
        this.activity_video_count = activity_video_count;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }


    public String getActivity_works_id() {
        return activity_works_id;
    }

    public void setActivity_works_id(String activity_works_id) {
        this.activity_works_id = activity_works_id;
    }

    public String getActivity_detail_note() {
        return activity_detail_note;
    }

    public void setActivity_detail_note(String activity_detail_note) {
        this.activity_detail_note = activity_detail_note;
    }

    public String getActivity_sponsor_icon() {
        return activity_sponsor_icon;
    }

    public void setActivity_sponsor_icon(String activity_sponsor_icon) {
        this.activity_sponsor_icon = activity_sponsor_icon;
    }

    public String getActivity_sponsor_id() {
        return activity_sponsor_id;
    }

    public void setActivity_sponsor_id(String activity_sponsor_id) {
        this.activity_sponsor_id = activity_sponsor_id;
    }

    public String getActivity_sponsor_nickname() {
        return activity_sponsor_nickname;
    }

    public void setActivity_sponsor_nickname(String activity_sponsor_nickname) {
        this.activity_sponsor_nickname = activity_sponsor_nickname;
    }

    public String getActivity_banner() {
        return activity_banner;
    }

    public void setActivity_banner(String activity_banner) {
        this.activity_banner = activity_banner;
    }

    public String getActivity_sponsor_gender() {
        return activity_sponsor_gender;
    }

    public void setActivity_sponsor_gender(String activity_sponsor_gender) {
        this.activity_sponsor_gender = activity_sponsor_gender;
    }

    public int getActivity_publish_state() {
        return activity_publish_state;
    }
}
