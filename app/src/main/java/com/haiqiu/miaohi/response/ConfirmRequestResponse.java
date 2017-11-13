package com.haiqiu.miaohi.response;

/**
 * Created by hackest on 16/8/17.
 */
public class ConfirmRequestResponse extends BaseResponse {


    /**
     * state : 20
     * title : 余额不足
     * content : 当前余额不足，充值后才能继续提问，是否充值？
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int state;
        private String title;
        private String content;
        private long price;

        public long getPrice() {
            return price;
        }

        public void setPrice(long price) {
            this.price = price;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
