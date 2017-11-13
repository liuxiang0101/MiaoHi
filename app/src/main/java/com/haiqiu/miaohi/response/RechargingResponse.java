package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.RechargingItem;

import java.util.List;

/**
 * Created by hackest on 16/8/16.
 */
public class RechargingResponse extends BaseResponse {


    /**
     * notice_note : 1元=1嗨币
     * page_result : [{"hi_coin":"600","goods_price":"600","goods_id":"PROD_6_5b3e7fb9_4f0f_4fa8_a0f7_838746040888"},{"hi_coin":"2500","goods_price":"2500","goods_id":"PROD_25_1fa5ccdb_85bc_46bc_996e_9fb44b6c4e29"},{"hi_coin":"6800","goods_price":"6800","goods_id":"PROD_68_61c3b02a_9d95_4ff3_973c_483dc556872c"},{"hi_coin":"10800","goods_price":"10800","goods_id":"PROD_108_5333262b_09f4_4399_b807_1571d043e52d"},{"hi_coin":"16800","goods_price":"16800","goods_id":"PROD_168_cf3e3343_2cce_478e_950c_776b5b26429d"},{"hi_coin":"29800","goods_price":"29800","goods_id":"PROD_298_dd1554a5_f111_43e1_b166_c621dc745079"}]
     * balance : 0
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String notice_note;
        private long balance;
        /**
         * hi_coin : 600
         * goods_price : 600
         * goods_id : PROD_6_5b3e7fb9_4f0f_4fa8_a0f7_838746040888
         */

        private List<RechargingItem> page_result;

        public String getNotice_note() {
            return notice_note;
        }

        public void setNotice_note(String notice_note) {
            this.notice_note = notice_note;
        }

        public long getBalance() {
            return balance;
        }

        public void setBalance(long balance) {
            this.balance = balance;
        }

        public List<RechargingItem> getPage_result() {
            return page_result;
        }

        public void setPage_result(List<RechargingItem> page_result) {
            this.page_result = page_result;
        }
    }
}
