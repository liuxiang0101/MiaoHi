package com.haiqiu.miaohi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ningl on 2016/5/27.
 */
public class MineSendFaceBeans implements Serializable {

    private List<MineSendFaceBean> page_result;

    public List<MineSendFaceBean> getPage_result() {
        return page_result;
    }

    public void setPage_result(List<MineSendFaceBean> page_result) {
        this.page_result = page_result;
    }
}
