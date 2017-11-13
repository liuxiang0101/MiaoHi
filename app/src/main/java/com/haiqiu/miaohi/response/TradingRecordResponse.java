package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.TradingRecordItem;

import java.util.List;

/**
 * Created by hackest on 16/8/17.
 */
public class TradingRecordResponse extends BaseResponse {


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * transaction_title : 肉丝杨
         * transaction_content : 向Ta提问
         * transaction_time : {"date":18,"hours":11,"seconds":46,"month":7,"nanos":0,"timezoneOffset":-480,"year":116,"minutes":4,"time":1471489486000,"day":4}
         * object_id : QUST-84fb38c1-64f0-11e6-94db-44a8424640fa
         * hi_coin_view : -12.00
         */

        private List<TradingRecordItem> page_result;

        public List<TradingRecordItem> getPage_result() {
            return page_result;
        }

        public void setPage_result(List<TradingRecordItem> page_result) {
            this.page_result = page_result;
        }

    }
}
