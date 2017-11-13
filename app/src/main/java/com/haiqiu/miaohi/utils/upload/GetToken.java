package com.haiqiu.miaohi.utils.upload;

import android.content.Context;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.response.LoginResponse;
import com.haiqiu.miaohi.utils.UserInfoUtil;

/**
 * 获取token
 * Created by ningl on 17/1/9.
 */

public class GetToken {

    public static void getToken(final Context context, final IGetToken iGetToken) {
        MHHttpClient.getInstance().post(LoginResponse.class, ConstantsValue.Url.GETTHIRDPARTYTOKEN, new MHHttpHandler<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse response) {
                UserInfoUtil.saveUploadToken(context, response.getData());
                if (iGetToken != null) iGetToken.getResult(true);
            }

            @Override
            public void onFailure(String content) {
                if (iGetToken != null) iGetToken.getResult(false);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                if (iGetToken != null) iGetToken.getResult(false);
            }
        });
    }

    public interface IGetToken {
        void getResult(boolean isOk);
    }

}
