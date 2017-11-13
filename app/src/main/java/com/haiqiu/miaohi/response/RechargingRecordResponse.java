package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.RechargingRecordItem;

import java.util.List;

/**
 * Created by hackest on 16/8/18.
 */
public class RechargingRecordResponse extends BaseResponse {


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private List<RechargingRecordItem> page_result;

        public List<RechargingRecordItem> getPage_result() {
            return page_result;
        }

        public void setPage_result(List<RechargingRecordItem> page_result) {
            this.page_result = page_result;
        }

    }
}