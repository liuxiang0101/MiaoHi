package com.haiqiu.miaohi.response;

import com.haiqiu.miaohi.bean.DecalInfo;

import java.util.List;

/**
 * Created by zhandalin on 2017-03-06 10:36.
 * 说明:
 */
public class StickerResponse extends BaseResponse {
    public StickerData data;

    public class StickerData {
        public List<DecalInfo> page_result;
    }
}
