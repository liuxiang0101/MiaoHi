package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.Attention;
import com.haiqiu.miaohi.bean.MayBeInterest;

import java.util.List;

/**
 * Created by ningl on 16/12/15.
 */
public class AttentionData {

    private String update_content;
    private int attention_user_list_index;
    private List<Attention> page_result;
    private List<MayBeInterest> attention_user_list;

    public String getUpdate_content() {
        return update_content;
    }

    public void setUpdate_content(String update_content) {
        this.update_content = update_content;
    }

    public int getAttention_user_list_index() {
        return attention_user_list_index;
    }

    public void setAttention_user_list_index(int attention_user_list_index) {
        this.attention_user_list_index = attention_user_list_index;
    }

    public List<Attention> getPage_result() {
        return page_result;
    }

    public void setPage_result(List<Attention> page_result) {
        this.page_result = page_result;
    }

    public List<MayBeInterest> getAttention_user_list() {
        return attention_user_list;
    }

    public void setAttention_user_list(List<MayBeInterest> attention_user_list) {
        this.attention_user_list = attention_user_list;
    }
}
