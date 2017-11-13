package com.haiqiu.miaohi.bean;

import java.util.List;

/**
 * Created by LiuXiang on 2016/12/14.
 */
public class HotResponderData {
    List<HotResponderPageResult>page_result;
    List<HotResponderKindResult>kind_result;

    public List<HotResponderPageResult> getPage_result() {
        return page_result;
    }

    public List<HotResponderKindResult> getKind_result() {
        return kind_result;
    }
}
