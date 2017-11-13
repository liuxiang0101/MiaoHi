package com.haiqiu.miaohi.bean;

/**
 * Created by ningl on 16/12/17.
 */
public class UploadPicture {


    /**
     * photo_id : 67
     * “photo_state” : ”xxxxxx”
     */

    private String photo_id;
    private int photo_state;
    private String share_link_address;

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public int getPhoto_state() {
        return photo_state;
    }

    public void setPhoto_state(int photo_state) {
        this.photo_state = photo_state;
    }

    public String getShare_link_address() {
        return share_link_address;
    }

    public void setShare_link_address(String share_link_address) {
        this.share_link_address = share_link_address;
    }
}
