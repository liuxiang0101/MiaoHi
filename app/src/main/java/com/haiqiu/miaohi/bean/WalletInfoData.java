package com.haiqiu.miaohi.bean;

import com.haiqiu.miaohi.response.BaseResponse;

/**
 * Created by hackest on 16/8/16.
 */
public class WalletInfoData extends BaseResponse {


    /**
     * notice_note : 1元=1嗨币
     * income_total : 0
     * balance : 0
     * month_total : 0
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
        private long income_total;
        private long balance;
        private long month_total;


        public String getNotice_note() {
            return notice_note;
        }

        public void setNotice_note(String notice_note) {
            this.notice_note = notice_note;
        }

        public long getIncome_total() {
            return income_total;
        }

        public void setIncome_total(long income_total) {
            this.income_total = income_total;
        }

        public long getBalance() {
            return balance;
        }

        public void setBalance(long balance) {
            this.balance = balance;
        }

        public long getMonth_total() {
            return month_total;
        }

        public void setMonth_total(long month_total) {
            this.month_total = month_total;
        }
    }
}
