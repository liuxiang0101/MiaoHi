package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.GiftResultBean;

import java.util.List;

/**
 * Created by hackest on 2016/8/31.
 */
public class GiftResponse extends BaseResponse {


    /**
     * user_balance : 0
     * gift_result : [{"hi_coin":0,"icon_role":"default","icon_state":"10","gift_id":"GIFT-49572a53-3070-4174-83c2-4a3d0de0c650","remain_count":"-1","gift_name":"1","gift_type":"10","icon_uri":"http://res.dev.miaohi.com/gift_default_2016_8_18_15_22_37_668"},{"hi_coin":0,"icon_role":"default","icon_state":"10","gift_id":"GIFT-5c29a390-a64c-4828-9c0f-3e3f1eb1f633","remain_count":"-1","gift_name":"2","gift_type":"20","icon_uri":"http://res.dev.miaohi.com/gift_default_2016_8_18_15_22_57_639"},{"hi_coin":0,"icon_role":"default","icon_state":"10","gift_id":"GIFT-67930138-f55b-49e1-9d81-236ea256be14","remain_count":"-1","gift_name":"阿凡提之怒","gift_type":"10","icon_uri":"http://res.dev.miaohi.com/gift_default_2016_8_23_13_52_15_769"}]
     * gift_count : 3
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
        private int gift_count;
        /**
         * hi_coin : 0
         * icon_role : default
         * icon_state : 10
         * gift_id : GIFT-49572a53-3070-4174-83c2-4a3d0de0c650
         * remain_count : -1
         * gift_name : 1
         * gift_type : 10
         * icon_uri : http://res.dev.miaohi.com/gift_default_2016_8_18_15_22_37_668
         */

        private List<GiftResultBean> gift_result;


        public long getUser_balance() {
            return user_balance;
        }

        public void setUser_balance(long user_balance) {
            this.user_balance = user_balance;
        }

        public int getGift_count() {
            return gift_count;
        }

        public void setGift_count(int gift_count) {
            this.gift_count = gift_count;
        }

        public List<GiftResultBean> getGift_result() {
            return gift_result;
        }

        public void setGift_result(List<GiftResultBean> gift_result) {
            this.gift_result = gift_result;
        }
    }
}
