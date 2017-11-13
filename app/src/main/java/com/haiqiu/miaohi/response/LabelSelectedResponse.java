package com.haiqiu.miaohi.response;

/**
 * Created by zhandalin on 2017-01-10 21:30.
 * 说明:判断用户是否选择了标签
 */
public class LabelSelectedResponse extends BaseResponse {
    public LabelSelected data;

    public class LabelSelected {
        public int label_selected;
    }
}
