package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.PageResultBean;

import java.util.List;

/**
 * Created by hackest on 16/7/27.
 */
public class CharactorItemResponse extends BaseResponse {


    private DataBean data;

    /**
     * server_vername : 1.6
     * server_vercode : 160
     * command : topquestionvip
     * status : 0
     */


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {
        /**
         * portrait_state : 10
         * answer_total : 46
         * vip_note : 测试认证
         * user_type : 20000
         * user_id : USER-03c97163-3464-11e6-83a3-44a8424640fa
         * user_name : 狼牙棒
         * user_note : 用户说明测试
         * question_total : 46
         * play_total : 0
         * user_state : 16
         * portrait_uri : http://icon.dev.miaohi.com/iconImageE4A9FFE9D67F11230113EB01F637E41F_2016_07_09_03_58_45_10
         */

        private List<PageResultBean> page_result;

        public List<PageResultBean> getPage_result() {
            return page_result;
        }

        public void setPage_result(List<PageResultBean> page_result) {
            this.page_result = page_result;
        }

    }


}
