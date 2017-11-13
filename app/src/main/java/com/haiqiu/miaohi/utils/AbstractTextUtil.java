package com.haiqiu.miaohi.utils;

import com.haiqiu.miaohi.bean.TextInfo;

import java.util.List;

/**
 * Created by ningl on 16/10/17.
 */
public abstract class AbstractTextUtil {

    public abstract void onClickSpan(TextInfo textInfo);

    public void getTextInfos(List<TextInfo> textInfos){}
}
