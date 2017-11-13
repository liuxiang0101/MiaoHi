package com.haiqiu.miaohi.umeng;

import com.haiqiu.miaohi.bean.UmengLoginInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * umeng三方登陆回调接口
 * Created by ningl on 2016/5/16.
 */
public interface IUMLoginCallback {

    public void callBackInfo(UmengLoginInfo loginInfo);

}
