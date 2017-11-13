package com.haiqiu.miaohi.utils;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.MHApplication;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.BaseResponse;

import org.json.JSONObject;

/**
 * Created by zhandalin on 2017-01-15 18:45.
 * 说明:用户行为统计
 */
public class BehaviourStatistic {

    /**
     * @param jsonObject json串对象
     */
    public static void uploadBehaviourInfo(JSONObject jsonObject) {
        if (null == jsonObject) return;
        try {
            jsonObject.put("time", System.currentTimeMillis() + "");
            jsonObject.put("mac", MHApplication.mac);
            jsonObject.put("imei", MHApplication.imei);
            jsonObject.put("miaohi_token", MHApplication.mMiaohiToken);
            jsonObject.put("version_code", MHApplication.versionCode);
            jsonObject.put("version_name", MHApplication.versionName);
            jsonObject.put("device", "android");
        } catch (Exception e) {

            MHLogUtil.e("uploadBehaviourInfo", e);
        }
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("behaviour_info", jsonObject.toString());
        MHHttpClient.getInstance().post(BaseResponse.class, ConstantsValue.Url.BE_HAVIOUR_STATISTICS, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
            }

            @Override
            public void onFailure(String content) {

            }
        });
    }
}
