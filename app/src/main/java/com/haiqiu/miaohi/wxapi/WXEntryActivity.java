
package com.haiqiu.miaohi.wxapi;

import com.haiqiu.miaohi.activity.LoginActivity;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity {
    private static final String TAG = "--WXEntryActivity";

    @Override
    public void onReq(BaseReq req) {
        super.onReq(req);
    }

    @Override
    public void onResp(BaseResp resp) {
        try {
            super.onResp(resp);
        } catch (Exception e) {
            LoginActivity.wxBackError = true;
            MHLogUtil.e(TAG,e);
        }
        finish();
//        if (resp instanceof SendAuth.Resp) {
//            SendAuth.Resp result = (SendAuth.Resp) resp;
//            final String code = result.code;
//            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx0c912fd1ce10f3b3&secret=869d7c1f643808e4f6d8f15cb93b7084&code=" + code + "&grant_type=authorization_code";
//            MHHttpClient.getInstance().requestService(url, new MHHttpBaseHandler() {
//                @Override
//                public void onSuccess(String content) {
//                    MHLogUtil.d(TAG, "content=" + content);
//                    try {
//                        JSONObject jsonObject = new JSONObject(content);
//                        String access_token = jsonObject.getString("access_token");
//                        String refresh_token = jsonObject.getString("refresh_token");
//
//                        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + access_token;
//                        MHHttpClient.getInstance().requestService(url, new MHHttpBaseHandler() {
//                            @Override
//                            public void onSuccess(String content) {
//                                MHLogUtil.d(TAG, "content=" + content);
//                            }
//
//                            @Override
//                            public void onFailure(String content) {
//                                MHLogUtil.d(TAG, "content=" + content);
//                            }
//                        });
//                    } catch (Exception e) {
//                        MHLogUtil.e(TAG,e);
//                    }
//
//                }
//                @Override
//                public void onFailure(String content) {
//                    MHLogUtil.d(TAG, "content=" + content);
//                }
//            });
//        }
    }
}
