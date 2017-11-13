package com.haiqiu.miaohi.response;

/**
 * Created by hackest on 2016/9/5.
 */
public class VideoSendGiftResponse extends BaseResponse {


    /**
     * user_balance : 9300
     * video_hls_state : 10
     * video_hls_uri : http://video.dev.miaohi.com/wm_video683WmGHxBZnQiXWHszWJwefj6weHS5tF2016_8_25_20_40_23Movie.m3u8
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private long user_balance;
        private String video_hls_state;
        private String video_hls_uri;

        public long getUser_balance() {
            return user_balance;
        }

        public void setUser_balance(long user_balance) {
            this.user_balance = user_balance;
        }

        public String getVideo_hls_state() {
            return video_hls_state;
        }

        public void setVideo_hls_state(String video_hls_state) {
            this.video_hls_state = video_hls_state;
        }

        public String getVideo_hls_uri() {
            return video_hls_uri;
        }

        public void setVideo_hls_uri(String video_hls_uri) {
            this.video_hls_uri = video_hls_uri;
        }
    }
}
