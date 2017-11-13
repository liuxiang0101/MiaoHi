package com.haiqiu.miaohi.bean;

/**
 * Created by ningl on 2016/7/27.
 */
public class DraftsUploadBean {


    /**
     * video_uri : http://video.dev.miaohi.com/video_379a78ffe134ace11f0b9888985a3182_2016_07_27_15_49_32_868
     * cover_uri : http://image.dev.miaohi.com/img_379a78ffe134ace11f0b9888985a3182_2016_07_27_15_49_33_022
     * cover_state : 10
     * video_state : 10
     * duration_second : 0
     * video_id : ANVI-ca24c943-53a4-11e6-8f77-44a8424640fa
     */

    private String video_uri;
    private String cover_uri;
    private String cover_state;
    private String video_state;
    private String duration_second;
    private String video_id;
    private String share_link_address;

    public String getShare_link_address() {
        return share_link_address;
    }

    public void setShare_link_address(String share_link_address) {
        this.share_link_address = share_link_address;
    }

    public String getVideo_uri() {
        return video_uri;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
    }

    public String getCover_uri() {
        return cover_uri;
    }

    public void setCover_uri(String cover_uri) {
        this.cover_uri = cover_uri;
    }

    public String getCover_state() {
        return cover_state;
    }

    public void setCover_state(String cover_state) {
        this.cover_state = cover_state;
    }

    public String getVideo_state() {
        return video_state;
    }

    public void setVideo_state(String video_state) {
        this.video_state = video_state;
    }

    public String getDuration_second() {
        return duration_second;
    }

    public void setDuration_second(String duration_second) {
        this.duration_second = duration_second;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}
