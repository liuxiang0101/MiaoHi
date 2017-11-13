package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.QAItem;

/**
 * Created by ningl on 16/12/7.
 */
public class QAResponse extends BaseResponse {

    private QAItem data;

    public QAItem getData() {
        return data;
    }

    public void setData(QAItem data) {
        this.data = data;
    }
}
