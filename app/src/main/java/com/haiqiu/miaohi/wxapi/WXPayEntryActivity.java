package com.haiqiu.miaohi.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.base.BasePayActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by zhandalin on 2016-08-04 12:31.
 * 说明:微信支付回调类,不再使用
 */
@Deprecated
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(context, null);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null != api)
            api.handleIntent(intent, this);
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp instanceof PayResp) {//由于PersonalPageActivity要在本页面来回跳转,所以不能再由PersonalPageActivity处理
            int mPayStatus = 0;
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK: //成功
                    mPayStatus = BasePayActivity.PAY_STATUS_SUCCESS;
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL://取消
                    mPayStatus = BasePayActivity.PAY_STATUS_USER_CANCEL;
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED://失败
                    mPayStatus = BasePayActivity.PAY_STATUS_FAILED;
                    break;
                default:
                    break;
            }
            Intent intent = new Intent(BasePayActivity.PAY_RESULT_NOTIFY_ACTION);
            intent.putExtra("PayStatus", mPayStatus);
            sendBroadcast(intent);
        }
        finish();
    }
}
