package com.haiqiu.miaohi.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.alipay.sdk.app.PayTask;
import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.PayInfoData;
import com.haiqiu.miaohi.bean.PayInfoRequestParam;
import com.haiqiu.miaohi.bean.PayResult;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.PayResponse;
import com.haiqiu.miaohi.response.PayResultResponse;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.MiaoHiKeyUtils;
import com.haiqiu.miaohi.widget.CommonDialog;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Map;


/**
 * Created by zhandalin on 2015-03-17 10:12.
 * 说明:支付的基本类,要注意的是,子类的启动模式必须是singleTask, 并且android:exported="true",如:
 * <p/>
 * <activity
 * android:name=".activity.OrderConfirmActivity"
 * android:exported="true"
 * android:launchMode="singleTask" />
 * <p/>
 * 直接调用{@link #toPay(PayInfoRequestParam)}即可完成支付流程
 */
public class BasePayActivity extends BaseActivity implements IWXAPIEventHandler {
    public static final int PAY_STATUS_USER_CANCEL = -1; //用户取消
    public static final int PAY_STATUS_FAILED = 0; //支付失败
    public static final int PAY_STATUS_SUCCESS = 1; //支付成功
    public static final int PAY_STATUS_PENDING = 2; //支付结果等待确认
    public static final String PAY_RESULT_NOTIFY_ACTION = "pay_result_notify_action";


    private static final int ALIPAY_PAY_FLAG = 1;

    private boolean hasInitEnablePay;
    private IWXAPI iwxapi;
    private PayInfoData payInfoData;
    private int payType;
    public boolean hasPayAction;//判断是否有支付行为,微信有bug有一种情况拿不到微信的回调,这个时候我们要手动刷新


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iwxapi = WXAPIFactory.createWXAPI(context, MiaoHiKeyUtils.getWeiXinAppid());//不要挪动位置
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasInitEnablePay)
            initEnablePay();
        hiddenLoadingView();
        MHLogUtil.d(TAG, "onResume");
    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // 这是支付宝的支付逻辑
            switch (msg.what) {
                case ALIPAY_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        //Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        showPayResult(PAY_STATUS_SUCCESS);
                    } else if (TextUtils.equals(resultStatus, "8000")) {//“8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        showPayResult(PAY_STATUS_PENDING);
                    } else if (TextUtils.equals(resultStatus, "6001")) {//6001用户中途取消
                        showPayResult(PAY_STATUS_USER_CANCEL);
                    } else {//订单支付失败
                        showPayResult(PAY_STATUS_FAILED);
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*************************************************
         * 步骤3：处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        if (data.hasExtra("pay_result")) {
            String str = data.getExtras().getString("pay_result");
            if ("success".equalsIgnoreCase(str)) {
                showPayResult(PAY_STATUS_SUCCESS);
            } else if ("fail".equalsIgnoreCase(str)) {
                showPayResult(PAY_STATUS_FAILED);
            } else if ("cancel".equalsIgnoreCase(str)) {
                showPayResult(PAY_STATUS_USER_CANCEL);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(ALIPAY_PAY_FLAG);
    }

    /**
     * 初始化可用的支付方式,用来紧急停掉有问题的支付方式
     */
    public void initEnablePay() {
        hasInitEnablePay = true;
    }


    /**
     * 支持的支付方式回调, -1代表只支持货到付款,0代表都可以,1代表只能在线支付
     *
     * @param payType
     */
    protected void supportPayType(int payType) {

    }

    public void toPay(final PayInfoRequestParam payInfoRequestParam) {
        if (null == payInfoRequestParam || null == payInfoRequestParam.getGoods_id()) {
            showToastAtCenter("支付信息为空，请稍后重试");
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.Dialog_loading);
        View view = View.inflate(context, R.layout.dialog_choose_pay_type_dialog, null);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.ll_pay_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!iwxapi.isWXAppInstalled()) {
                    showToastAtCenter("请先安装微信客户端");
                    return;
                }
                payInfoRequestParam.setDeposit_type(ConstantsValue.PayType.PAY_TYPE_WEIXIN);
                getPayInfo(payInfoRequestParam);
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.ll_pay_zhifubao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payInfoRequestParam.setDeposit_type(ConstantsValue.PayType.PAY_TYPE_ALIPAY);
                getPayInfo(payInfoRequestParam);
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    private void getPayInfo(final PayInfoRequestParam payInfoRequestParam) {

        showLoading("获取支付信息...", true, false);
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("deposit_type", payInfoRequestParam.getDeposit_type() + "");
        requestParams.addParams("goods_id", payInfoRequestParam.getGoods_id());
        requestParams.addParams("deposit_price", payInfoRequestParam.getDeposit_price());

        MHHttpClient.getInstance().post(PayResponse.class, ConstantsValue.Url.CALL_DEPOSITPAY, requestParams, new MHHttpHandler<PayResponse>() {
            @Override
            public void onSuccess(PayResponse response) {
                doPayInfo(response.getData(), payInfoRequestParam.getDeposit_type());
            }

            @Override
            public void onFailure(String content) {
                showToastAtCenter("获取支付信息失败,请稍后再试");
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                showToastAtCenter(message);
            }
        });
    }

    /**
     * 处理支付信息
     */
    private void doPayInfo(PayInfoData payInfoData, int payType) {
        hasPayAction = true;
        this.payInfoData = payInfoData;
        this.payType = payType;
        switch (payType) {
            case ConstantsValue.PayType.PAY_TYPE_WEIXIN:
                if (MHStringUtils.isEmpty(payInfoData.getWx_prepay_id())) {
                    showToastAtCenter("支付信息为空，请稍后重试");
                    MHLogUtil.d(TAG, "prepay_id为null");
                    return;
                }
                PayReq payReq = new PayReq();
                payReq.appId = payInfoData.getWx_app_id();
                payReq.nonceStr = payInfoData.getWx_nonce_str();
                payReq.packageValue = payInfoData.getWx_package_info();
                payReq.partnerId = payInfoData.getWx_partner_id();
                payReq.prepayId = payInfoData.getWx_prepay_id();
                payReq.timeStamp = payInfoData.getWx_time_stamp();
                payReq.sign = payInfoData.getWx_sign();

                PayReq.Options options = new PayReq.Options();
                Bundle bundle = new Bundle();
//                if (context instanceof PersonalPageActivity) {//个人主页的支付不在本类回调
//                    //设置支付回调类
//                    bundle.putString("_wxapi_payoptions_callback_classname", "com.haiqiu.miaohi.wxapi.WXPayEntryActivity");
//                }else {
                bundle.putString("_wxapi_payoptions_callback_classname", context.getClass().getName());
//                }
                options.fromBundle(bundle);
                payReq.options = options;
                //weixinPayRequest.extData = orderNum + "," + orderPrice + "," + payType;
                callWeixinPay(payReq);
                break;
            case ConstantsValue.PayType.PAY_TYPE_ALIPAY:
                if (null != payInfoData.getAlipay_info())
                    callAliPay(payInfoData.getAlipay_info());
                else {
                    showToastAtCenter("没有获取到支付信息");
                }
                break;
            case ConstantsValue.PayType.PAY_TYPE_UNIPAY:
                        /*
                        银联支付需要客户端做的事情有：
                        1.从商户服务器中获得到来自银联的交易流水号TN
                        2.通过交易流水号启动支付控件开始支付
                        3.将支付控件的返回结果展示出来
                         */
//                //获取流水号TN
//                returndata = "{\"data\":" + returndata + "}";
//                UniPayResponse uniPayResponse = new Gson().fromJson(returndata, UniPayResponse.class);
//                String TN = uniPayResponse.getData();
//                if (TN != null && TN.length() > 0) {
//                    //调用支付控件开始支付
//                    callUniPay(TN);
//                } else {
//                isPaying = false;
//                    showToastAtCenter("操作失败，请重试");
//                }
                break;
        }
    }


    private void callUniPay(String TN) {
//        UPPayAssistEx.startPayByJAR(this, PayActivity.class, null, null,
//                TN, "00");
    }

    private void callWeixinPay(PayReq weixinPayRequest) {
        showLoading("启动微信中...", true, false);
        iwxapi.sendReq(weixinPayRequest);
    }

    /**
     * 掉起支付宝支付
     *
     * @param payInfo 封装的信息
     */
    private void callAliPay(final String payInfo) {
        hiddenLoadingView();
        new Thread() {
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(BasePayActivity.this);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(payInfo, true);
                Message msg = new Message();
                msg.what = ALIPAY_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        MHLogUtil.d(TAG, "onNewIntent");
        hiddenLoadingView();
        iwxapi.handleIntent(intent, this);
    }


    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            default:
                break;
        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
//        try {
//            Bundle bundle = new Bundle();
//            resp.toBundle(bundle);
//            String extdata = (String) bundle.get("_wxapi_payresp_extdata");
//            String[] split = extdata.split(",");
//            orderNum = split[0];
//            orderPrice = split[1];
//            payType = split[2];
//        } catch (Exception e) {
//            MHLogUtil.e(TAG,e);
//        }
        if (resp instanceof PayResp) {
            int mPayStatus = 0;
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK: //成功
                    mPayStatus = PAY_STATUS_SUCCESS;
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL://取消
                    mPayStatus = PAY_STATUS_USER_CANCEL;
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED://失败
                    mPayStatus = PAY_STATUS_FAILED;
                    break;
                default:
                    break;
            }
            showPayResult(mPayStatus);
        }
    }

    /**
     * @param payStatus 第三发支付支付都会回调到这个方法中
     */
    protected void showPayResult(int payStatus) {
        MHLogUtil.d(TAG, "showPayResult");
        switch (payStatus) {
            case PAY_STATUS_USER_CANCEL:
                MHLogUtil.d(TAG, "用户手动取消支付");
                showToastAtCenter("支付取消");
                break;
            case PAY_STATUS_FAILED:
                MHLogUtil.d(TAG, "支付失败");
                showToastAtCenter("支付失败");
                confirmPayState(payInfoData, false);
                break;
            case PAY_STATUS_SUCCESS:
                MHLogUtil.d(TAG, "支付成功");
                confirmPayState(payInfoData, true);
                break;
            case PAY_STATUS_PENDING:
                showToastAtCenter("支付状态待确认,请不要重复支付");
                MHLogUtil.d(TAG, "支付状态待确认");
                break;
        }
    }

    /**
     * 确认支付状态
     */
    public void confirmPayState(final PayInfoData payInfoData, final boolean isSuccess) {
        MHLogUtil.d(TAG, "confirmPayState");
        showLoading("获取支付结果中");
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("deposit_type", payType + "");
        requestParams.addParams("deposit_id", payInfoData.getDeposit_id());
        requestParams.addParams("pay_state", isSuccess ? "10" : "20");

        MHHttpClient.getInstance().post(PayResultResponse.class, context, ConstantsValue.Url.CONFIRM_DEPOSIT, requestParams, new MHHttpHandler<PayResultResponse>() {
            @Override
            public void onSuccess(PayResultResponse response) {
                if (null != response && null != response.getData()) {
                    switch (response.getData().getPay_state()) {
                        case 1:
                            showToastAtCenter("充值成功");
                            onPaySuccess(payInfoData);
                            break;
                        case 2:
//                            showToastAtCenter("支付失败");
                            showConfirmDialog(isSuccess);
                            break;
                        case 3:
                            showConfirmDialog(isSuccess);
                            break;
                    }
                } else {
//                    showToastAtCenter("支付结果待确认,请稍后刷新");
                    showConfirmDialog(isSuccess);
                }
            }

            @Override
            public void onFailure(String content) {
                showConfirmDialog(isSuccess);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
            }
        });
    }

    private void showConfirmDialog(final boolean isSuccess) {
        CommonDialog commonDialog = new CommonDialog(context);
        commonDialog.setRightButtonMsg("取消");
        commonDialog.setContentMsg("未获取到支付结果，若已经发生扣款，但未显示充值成功，系统会在10个工作日内退款。");
        commonDialog.setLeftButtonMsg("重新获取");
        commonDialog.setOnLeftButtonOnClickListener(new CommonDialog.LeftButtonOnClickListener() {
            @Override
            public void onLeftButtonOnClick() {
                confirmPayState(payInfoData, isSuccess);
            }
        });
        commonDialog.show();
    }

    /**
     * 支付成功的回调
     */
    protected void onPaySuccess(PayInfoData payInfoData) {
        MHLogUtil.d(TAG, "向服务器确认支付成功---");
    }
}
