package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.ToolbarVo;

/**
 * 5.66获取首页工具栏布局(command:gethomepagetoolbar)
 * Created by hackest on 16/7/5.
 */
public class ToolbarResponse extends BaseResponse {

    private ToolbarVo data;


    public ToolbarVo getData() {
        return data;
    }

    public void setData(ToolbarVo data) {
        this.data = data;
    }

}