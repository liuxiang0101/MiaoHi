package com.haiqiu.miaohi.bean;

import java.util.List;

/**
 * Created by hackest on 16/7/5.
 */
public class ToolbarVo {

    List<ChannelItem> toolbar_result;
    int default_index;//默认选中这个

    public int getDefault_index() {
        return default_index;
    }

    public void setDefault_index(int default_index) {
        this.default_index = default_index;
    }

    public List<ChannelItem> getToolbar_result() {
        return toolbar_result;
    }

    public void setToolbar_result(List<ChannelItem> toolbar_result) {
        this.toolbar_result = toolbar_result;
    }
}
