package com.haiqiu.miaohi.okhttp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.MHApplication;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SecurityMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zhandalin on 2015-10-28 16:04.
 * 说明:基于OkHttpClient与自身的逻辑进行了简单封装
 */
public class MHHttpClient {
    private OkHttpClient okHttpClient;

    private static MHHttpClient mhHttpClient;
    private static final String TAG = "MHHttpClient";
    private final Handler uiHandler;

    private MHHttpClient() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS).build();
        uiHandler = new Handler(Looper.getMainLooper());
    }

    public static MHHttpClient getInstance() {
        if (null == mhHttpClient) {
            synchronized (MHHttpClient.class) {
                if (null == mhHttpClient) {
                    mhHttpClient = new MHHttpClient();
                }
            }
        }
        return mhHttpClient;
    }


    /**
     * post请求,错误的时候会弹Toast,status为0的时候会弹errtext所包含的信息
     * 会在子线程中解析json
     */
    public <T extends BaseResponse> void post(Class<T> object, Context context, String url, MHRequestParams params, MHHttpHandler<T> responseHandler) {
        sendPostRequest(object, (BaseActivity) context, handleRequestParams(params, url), responseHandler);
    }


    /**
     * 不带参数的post请求,错误的时候不弹Toast,status为0的时候会弹errtext所包含的信息
     * 会在子线程中解析json
     */
    public <T extends BaseResponse> void post(Class<T> object, String url, MHHttpHandler<T> responseHandler) {
        sendPostRequest(object, null, handleRequestParams(new MHRequestParams(), url), responseHandler);
    }

    /**
     * 不带参数的post请求,错误的时候会弹Toast,status为0的时候会弹errtext所包含的信息
     * 会在子线程中解析json
     */
    public <T extends BaseResponse> void post(Class<T> object, Context context, String url, MHHttpHandler<T> responseHandler) {
        sendPostRequest(object, (BaseActivity) context, handleRequestParams(new MHRequestParams(), url), responseHandler);
    }

    /**
     * 带参数的post请求,错误的时候会弹Toast,status为0的时候会弹message所包含的信息
     * 会在子线程中解析json
     */
    public <T extends BaseResponse> void post(Class<T> object, String url, MHRequestParams params, MHHttpHandler<T> responseHandler) {
        sendPostRequest(object, null, handleRequestParams(params, url), responseHandler);
    }

    /**
     * 不弹任何信息,返回的数据根元素是data
     */
    @Deprecated
    public void post(String url, MHRequestParams params, MHHttpBaseHandler responseHandler) {
        sendPostRequest(null, null, handleRequestParams(params, url), responseHandler);
    }

    /**
     * 直接返回网络请求的数据不做任何预处理,如果预处理的话会把有些转义字符处理掉
     * 有相应需求直接调用这个方法
     */
    public void post4NoParseJson(final String url, MHRequestParams params, MHHttpBaseHandler responseHandler) {
        sendPostRequest(handleRequestParams(params, url), responseHandler);
    }

    /**
     * 异步下载文件
     *
     * @param targetFile 全路径
     */
    public void downloadAsyn(final String url, final String targetFile, final MHHttpBaseHandler responseHandler) {
        try {
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            final Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callFail(e.toString(), responseHandler, null);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len;
                    FileOutputStream fos = null;
                    try {
                        is = response.body().byteStream();
                        File file = new File(targetFile);
                        if (!file.exists()){
                            file.getParentFile().mkdirs();
                            file.createNewFile();
                        }
                        fos = new FileOutputStream(file);
                        float totalLen = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            totalLen += len;
                            if (responseHandler instanceof MHHttpProgressHandler) {
                                final float finalTotalLen = totalLen;
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            ((MHHttpProgressHandler) responseHandler).onProgress(finalTotalLen / response.body().contentLength());
                                        } catch (Exception e) {
                                            MHLogUtil.e(TAG,e);
                                        }
                                    }
                                });
                            }
                        }
                        fos.flush();
                        callSuccess(null, targetFile + "下载成功", responseHandler, null);
                    } catch (Exception e) {
                        callFail(e.toString(), responseHandler, null);
                    } finally {
                        try {
                            if (is != null)
                                is.close();
                        } catch (Exception e) {
                            MHLogUtil.e(TAG,e);
                        }
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (Exception e) {
                            MHLogUtil.e(TAG,e);
                        }
                        try {
                            response.close();
                        } catch (Exception e) {
                            MHLogUtil.e(TAG,e);
                        }
                    }
                }
            });
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }

    /**
     * 异步上传文件,不弹消息
     *
     * @param targetFile 全路径名字
     */
    public void uploadAsyn4NoTost(final String url, MHRequestParams params, final String targetFile, final MHHttpBaseHandler responseHandler) {

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();

        multipartBuilder.setType(MultipartBody.FORM);

        for (Map.Entry<String, String> temp : params.getParamMap().entrySet()) {
            multipartBuilder.addFormDataPart(temp.getKey(), null, RequestBody.create(null, temp.getValue()));
        }

        File file = new File(targetFile);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        multipartBuilder.addFormDataPart("file", file.getName(), fileBody);

        Request request = new Request.Builder()
                .url(ConstantsValue.SERVER_URL)
                .post(multipartBuilder.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callFail(e.toString(), responseHandler, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    callSuccess(null, response.body().string(), responseHandler, null);
                } catch (Exception e) {
                    MHLogUtil.e(TAG,e);
                    MHLogUtil.d(TAG, e.getMessage());
                }finally {
                    try {
                        response.close();
                    } catch (Exception e) {
                        MHLogUtil.e(TAG,e);
                    }
                }
            }
        });
    }

    /**
     * 异步上传文件
     *
     * @param destFile 全路径名字
     */
    public void uploadAsyn(Context context, final String url, MHRequestParams params, final String destFile, final MHHttpBaseHandler responseHandler) {
        final BaseActivity baseActivity = (BaseActivity) context;

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        multipartBuilder.setType(MultipartBody.FORM);

        for (Map.Entry<String, String> temp : params.getParamMap().entrySet()) {
            multipartBuilder.addFormDataPart(temp.getKey(), null, RequestBody.create(null, temp.getValue()));
        }

        if (!MHStringUtils.isEmpty(destFile)) {
            File file = new File(destFile);
            if (file.isFile()) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                multipartBuilder.addFormDataPart("file", file.getName(), fileBody);
            }
        }

        Request request = new Request.Builder()
                .url(ConstantsValue.SERVER_URL)
                .post(multipartBuilder.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callFail(e.toString(), responseHandler, baseActivity);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    callSuccess(null, response.body().string(), responseHandler, baseActivity);
                } catch (Exception e) {
                    MHLogUtil.e(TAG,e);
                    callFail(e.toString(), responseHandler, baseActivity);
                    MHLogUtil.d(TAG, e.getMessage());
                }finally {
                    try {
                        response.close();
                    } catch (Exception e) {
                        MHLogUtil.e(TAG,e);
                    }
                }
            }
        });
    }

    //    /**
    //     * 异步上传文件
    //     *
    //     * @param destFiles 图片地址数组
    //     */
    //    public void multiUploadAsyn(Context context, final String url, MHRequestParams params, final List<Return_change_image> destFiles, final MHHttpBaseHandler responseHandler) {
    //        final BaseActivity context = (BaseActivity) context;
    //
    //        MultipartBuilder multipartBuilder = new MultipartBuilder();
    //        multipartBuilder.type(MultipartBuilder.FORM);
    //
    //        for (Map.Entry<String, String> temp : params.getParamMap().entrySet()) {
    //            multipartBuilder.addFormDataPart(temp.getKey(), null, RequestBody.create(null, temp.getValue()));
    //        }
    //
    //        if (destFiles != null && destFiles.size() > 0) {
    //            for (int i = 0; i < destFiles.size(); i++) {
    //                File file = new File(destFiles.get(i).getUrl());
    //                if (file.isFile()) {
    //                    RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
    //                    multipartBuilder.addFormDataPart("file" + i + 1, file.getName(), fileBody);
    //                }
    //            }
    //        }
    //
    //        Request request = new Request.Builder()
    //                .url(Constants.SERVER_URL)
    //                .post(multipartBuilder.build())
    //                .build();
    //        okHttpClient.newCall(request).enqueue(new Callback() {
    //            @Override
    //            public void onFailure(Request request, IOException e) {
    //                callFail(e.toString(), responseHandler, context, false);
    //            }
    //
    //            @Override
    //            public void onResponse(Response response) throws IOException {
    //                callSuccess(null, response.body().string(), responseHandler, context);
    //                response.close();
    //            }
    //        });
    //    }


    /**
     * 不建议使用,如果要使用的话,访问我们的接口会失败
     * <p/>
     * 直接请求服务器,不会做任何预处理,返回结果也不会做任何处理
     *
     * @param url 请求的绝对地址
     */
    public void requestService(final String url, final MHHttpBaseHandler responseHandler) {
        try {
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            final Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callFail(e.toString(), responseHandler, null);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        callSuccess(null, response.body().string(), responseHandler, null);
                    } catch (Exception e) {
                        MHLogUtil.e(TAG,e);
                        callFail(e.toString(), responseHandler, null);
                    } finally {
                        try {
                            response.close();
                        } catch (Exception e) {
                            MHLogUtil.e(TAG,e);
                        }
                    }
                }
            });
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }


    /**
     * 获取一个请求,一些基础信息会在这里处理,包括加密逻辑
     *
     * @param params  info 里面的参数
     * @param command 接口地址
     */
    private Request handleRequestParams(MHRequestParams params, String command) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject baseJson = new JSONObject();
            JSONObject infoJson = new JSONObject();

            baseJson.put("client_vername", MHApplication.versionName);
            baseJson.put("client_vercode", MHApplication.versionCode);
            if (!MHStringUtils.isEmpty(MHApplication.mMiaohiToken))
                baseJson.put("miaohi_token", MHApplication.mMiaohiToken);

            baseJson.put("business", ConstantsValue.CHANNEL_MAP.get(ConstantsValue.BUSINESS));
            baseJson.put("command", command);
            baseJson.put("time", System.currentTimeMillis() + "");
            jsonObject.put("base", baseJson);

            TreeMap<String, String> paramMap = params.getParamMap();
            if (paramMap.size() > 0) {
                if (null != paramMap.get("info")) {
                    jsonObject.put("info", new JSONArray(paramMap.get("info")));
                } else {
                    for (Map.Entry<String, String> keyAndValue : paramMap.entrySet()) {
                        String value = keyAndValue.getValue();
                        if (value.startsWith("["))
                            infoJson.put(keyAndValue.getKey(), new JSONArray(value));
                        else
                            infoJson.put(keyAndValue.getKey(), value);
                    }
                    jsonObject.put("info", infoJson);
                }
            }
            return getRequest(jsonObject.toString());
        } catch (Exception e) {
            MHLogUtil.e(TAG, e.toString());
        }
        return getRequest(null);
    }

    /**
     * @param data 加密以后的
     * @return 最终的访问地址Url
     */
    private String getRequestUrl(String data, String time) {
        String url = null;
        if (ConstantsValue.isDeveloperMode(null)) {
            try {
                if (null != data) {
                    url = ConstantsValue.SERVER_URL + "?data=" + data + "&mac=" + MHApplication.mac + "&imei=" + MHApplication.imei + "&time=" + time;
                    MHLogUtil.d(TAG, "url=" + url);
                }
            } catch (Exception e) {
                MHLogUtil.e(TAG,e);
            }

        }
        return url;
    }

    /**
     * 这个会预处理json
     */
    private <T extends BaseResponse> void sendPostRequest(final Class<T> object, final BaseActivity baseActivity, Request request, final MHHttpBaseHandler responseHandler) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callFail(e.toString(), responseHandler, baseActivity);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    parseJson(object, response.body().string(), responseHandler, baseActivity);
                } catch (Exception e) {
                    MHLogUtil.d(TAG, e.toString());
                    callFail(e.toString(), responseHandler, baseActivity);
                }finally {
                    try {
                        response.close();
                    } catch (Exception e) {
                        MHLogUtil.e(TAG,e);
                    }
                }
            }
        });
    }


    /**
     * 这个方法不会预处理json,直接返回服务器结果
     */
    private void sendPostRequest(Request request, final MHHttpBaseHandler responseHandler) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callFail(e.toString(), responseHandler, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    callSuccess(null, decodeResponseData(response.body().string()), responseHandler, null);
                } catch (final Exception e) {
                    MHLogUtil.d(TAG, e.toString());
                    callFail(e.toString(), responseHandler, null);
                }finally {
                    try {
                        response.close();
                    } catch (Exception e) {
                        MHLogUtil.e(TAG,e);
                    }
                }
            }
        });
    }


    /**
     * 初步的解析出json,来判定是否应该显示来自服务端的消息
     *
     * @throws JSONException
     */
    private <T extends BaseResponse> void parseJson(Class<T> object, String content, final MHHttpBaseHandler responseHandler, BaseActivity baseActivity) throws Exception {
        try {
            content = decodeResponseData(content);
//            LogUtils.json(content);
//            MHLogUtil.d(TAG, "result=" + content);

            T baseResponse = new Gson().fromJson(content, object);

            if (ConstantsValue.isDeveloperMode(null)) {
                if (null != baseResponse && null != baseResponse.getBase()) {
                    MHLogUtil.d(TAG, "command=" + baseResponse.getBase().getCommand() + "--result=" + content);
                } else {
                    MHLogUtil.d(TAG, "result=" + content);
                }
            }
            assert baseResponse != null;
            if (baseResponse.getBase().getStatus() != 0) {
                String errtext = baseResponse.getBase().getErrtext();
                if (MHStringUtils.isEmpty(errtext))
                    errtext = ConstantsValue.Other.NETWORK_ERROR_TIP_MSG;
                if (null != baseActivity)
                    showToast(baseActivity, errtext);
                if (responseHandler instanceof MHHttpHandler) {
                    final String finalErrtext = errtext;
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ((MHHttpHandler) responseHandler).onStatusIsError(finalErrtext);
                        }
                    });
                }
                if (null != baseActivity) baseActivity.isRetring = false;
                doErrorCode(baseActivity, baseResponse.getBase().getStatus());
            } else {
                callSuccess(baseResponse, content, responseHandler, baseActivity);
            }

        } catch (Exception e) {
            //json解析失败
            MHLogUtil.d(TAG, "json解析失败=" + e.toString());
            callFail(e.toString(), responseHandler, baseActivity);
        }
    }

    private <T extends BaseResponse> void callSuccess(final T response, final String content, final MHHttpBaseHandler responseHandler, final BaseActivity baseActivity) {

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null != baseActivity) {
                        baseActivity.hiddenLoadingView();
                        baseActivity.hideErrorView();
                    }
                    if (null != response && responseHandler instanceof MHHttpHandler) {
                        ((MHHttpHandler) responseHandler).onSuccess(response);
                    } else
                        responseHandler.onSuccess(content);
                } catch (Exception e) {
                    MHLogUtil.e(TAG,e);
                }
            }
        });
    }


    /**
     * 当网络错误,或者json格式错误就会调这个方法
     */
    private void callFail(final String content, final MHHttpBaseHandler responseHandler, final BaseActivity baseActivity) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null != baseActivity) {
                        baseActivity.hiddenLoadingView();
                        baseActivity.isRetring = false;
                        baseActivity.showToastAtCenter(null);
                    }
                    responseHandler.onFailure(content);
                } catch (Exception e) {
                    MHLogUtil.e(TAG,e);
                }

            }
        });
    }

    private void showToast(final BaseActivity baseActivity, final String content) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (null != baseActivity) {
                    baseActivity.hiddenLoadingView();
                    baseActivity.showToastAtCenter(content);
                }
            }
        });
    }

    private Request getRequest(String data) {
        String time = System.currentTimeMillis() + "";
        getRequestUrl(data, time);

        FormBody.Builder formBody = new FormBody.Builder();
        data = encodeRequestData(data);
        if (null != data)
            formBody.add("data", data);
        if (null != MHApplication.mac)
            formBody.add("mac", MHApplication.mac);
        if (null != MHApplication.imei)
            formBody.add("imei", MHApplication.imei);

        formBody.add("time", time);

        return new Request.Builder()
                .url(ConstantsValue.SERVER_URL)
                .post(formBody.build())
                .addHeader("User-agent", "Haiqiu CUS/2.2")
                .build();
    }

    /**
     * 加密请求的东西
     *
     * @param data 请求里面的data内容
     */
    private String encodeRequestData(String data) {
        if (TextUtils.isEmpty(data))
            return null;

        return SecurityMethod.aesEncryptToBase64(data, MHApplication.securityKey, MHApplication.securityVector);
    }

    /**
     * 解密返回的东西
     *
     * @param data 服务端返回的东西
     */
    private String decodeResponseData(String data) {
        if (TextUtils.isEmpty(data))
            return null;

        return SecurityMethod.aesBase64StringDecrypt(data, MHApplication.securityKey, MHApplication.securityVector);
    }

    /**
     * @param statusCode 服务端返回的状态码
     * @return true 表示已经处理了,后面的逻辑不会再处理了
     */
    private boolean doErrorCode(BaseActivity baseActivity, int statusCode) {
//        switch (statusCode) {
//            case ConstantsValue.ErrorCode.ERROR_CODE_CHOOSE_LABEL:
//                if (null != baseActivity) {
//                    baseActivity.startActivity(new Intent(baseActivity, RecommendSportsActivity.class));
//                }
//                return true;
//        }
        return false;
    }
}
