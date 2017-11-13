package com.haiqiu.miaohi.bean;

/**
 * 发现页--轮播图
 * Created by miaohi on 2016/5/4.
 */
public class FoundActivityResultArrayObj{
    private String banner_id;
    private int banner_type;
    private String activity_name;
    private String banner_uri;
    private String banner_state;
    private String banner_note1;
    private String banner_note2;
    private String player_count;
    private String video_count;
    private int duration_second;
    private String face_count;

    @Override
    public String toString() {
        return "FoundActivityResultArrayObj{" +
                "banner_id='" + banner_id + '\'' +
                ", activity_name='" + activity_name + '\'' +
                ", banner_uri='" + banner_uri + '\'' +
                ", banner_state='" + banner_state + '\'' +
                ", banner_note1='" + banner_note1 + '\'' +
                ", banner_note2='" + banner_note2 + '\'' +
                ", player_count='" + player_count + '\'' +
                ", video_count='" + video_count + '\'' +
                '}';
    }

    public int getDuration_second() {
        return duration_second;
    }

    public String getFace_count() {
        return face_count;
    }

    public String getBanner_id() {
        return banner_id;
    }

    public int getBanner_type() {
        return banner_type;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public String getBanner_uri() {
        return banner_uri;
    }

    public String getBanner_state() {
        return banner_state;
    }

    public String getBanner_note1() {
        return banner_note1;
    }

    public String getBanner_note2() {
        return banner_note2;
    }

    public String getPlayer_count() {
        return player_count;
    }

    public String getVideo_count() {
        return video_count;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public void setBanner_uri(String banner_uri) {
        this.banner_uri = banner_uri;
    }

    public void setBanner_state(String banner_state) {
        this.banner_state = banner_state;
    }

    public void setBanner_note1(String banner_note1) {
        this.banner_note1 = banner_note1;
    }

    public void setBanner_note2(String banner_note2) {
        this.banner_note2 = banner_note2;
    }

    public void setPlayer_count(String player_count) {
        this.player_count = player_count;
    }

    public void setVideo_count(String video_count) {
        this.video_count = video_count;
    }
}
