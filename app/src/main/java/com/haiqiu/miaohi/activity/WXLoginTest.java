package com.haiqiu.miaohi.activity;

import android.os.Bundle;
import android.view.View;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.UmengLoginInfo;
import com.haiqiu.miaohi.okhttp.MHHttpBaseHandler;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.umeng.IUMLoginCallback;
import com.haiqiu.miaohi.umeng.UmengLogin;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by zhandalin on 2016-07-21 17:33.
 * 说明:
 */
public class WXLoginTest extends BaseActivity {
    private static final String TAG = "--WXLoginTest";
    private static final String WX_OPENID = "o7XDdwg7P-v_5sOQpj_t2O_ebGkI";//唯一的
    private static final String refresh_token = "U-fgDAdazuWweoCGs8siMiEauWzFDv-8aIayo7J3aKhsNznaArku_HbFFeJ6Asc-Q-SO-C5dE5aHvOIMUYgmFtbttbxRlGWkauhWchEb28E";//唯一的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_login_test);
    }


    public void start(View view) {
        UMShareAPI umShareAPI = UMShareAPI.get(this);
        UmengLogin.loginWX(this, umShareAPI, new IUMLoginCallback() {

            @Override
            public void callBackInfo(UmengLoginInfo loginInfo) {
                String wx_open_id = loginInfo.getMap().get("openid");
            }
        });
    }

    public void refresh_token() {
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=wx0c912fd1ce10f3b3&grant_type=refresh_token&refresh_token=" + refresh_token;
        MHHttpClient.getInstance().requestService(url, new MHHttpBaseHandler() {
            @Override
            public void onSuccess(String content) {
                MHLogUtil.d(TAG, "refresh_token--content=" + content);
                try {
                    if (content.contains("errcode")) return;//失败

                    JSONObject jsonObject = new JSONObject(content);
                    String access_token = jsonObject.getString("access_token");
                    getOpenId(access_token);

                } catch (Exception e) {
                    MHLogUtil.e(TAG,e);
                }
            }

            @Override
            public void onFailure(String content) {
                MHLogUtil.d(TAG, "refresh_token--content=" + content);
            }
        });
    }

    public void getOpenId(String access_token) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + access_token;
        MHHttpClient.getInstance().requestService(url, new MHHttpBaseHandler() {
            @Override
            public void onSuccess(String content) {
                MHLogUtil.d(TAG, "content=" + content);
            }

            @Override
            public void onFailure(String content) {
                MHLogUtil.d(TAG, "content=" + content);
            }
        });
    }

}
