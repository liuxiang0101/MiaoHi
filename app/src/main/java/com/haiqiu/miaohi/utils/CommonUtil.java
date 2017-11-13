package com.haiqiu.miaohi.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.UpdateData;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHHttpProgressHandler;
import com.haiqiu.miaohi.response.UpdateResponse;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by zhandalin on 2016/5/29.
 */
public class CommonUtil {

    private static final String TAG = "CommonUtil";

    /**
     * @param second 单位为秒
     * @return 返回格式为 23:21
     */
    public static String formatTime(long second) {
        if (second < 0) {
            return "00:00";
        }
        long hh = (long) (second / 3600 + 0.5);
        long mm = (long) (second % 3600 / 60 + 0.5);
        long ss = second % 60;
        String strTemp;
        if (0 != hh) {
            strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            strTemp = String.format("%02d:%02d", mm, ss);
        }
        return strTemp;
    }

    /**
     * @param second 单位为秒
     * @return 返回格式为 23'21''
     */
    public static String formatTime2(long second) {
        if (second < 0) {
            return "00:00";
        }
        long hh = (long) (second / 3600 + 0.5);
        long mm = (long) (second % 3600 / 60 + 0.5);
        long ss = second % 60;
        String strTemp;
        if (0 != hh) {
            strTemp = String.format("%02d'%02d''%02d'''", hh, mm, ss);
        } else {
            strTemp = String.format("%02d'%02d''", mm, ss);
        }
        return strTemp;
    }


    /**
     * @return 大于10万的返回以万为单位的数字, 如12.5万
     */
    public static String formatCount(long count) {
        if (count < 0)
            return "0";
        if (count < 100000) {
            return count + "";
        } else {
            return String.format("%.1f万", count / 10000.0f);
        }
    }

    /**
     * @param price 格式价格,以分为单位
     * @return 保留两位小数
     */
    public static String formatPrice(long price) {
        if (price < 0)
            price = 0;
        long temp = price / 100;
        long decimal = price % 100;

        if (decimal > 0) {//表示有分数,这时候要返回两位小数
            return String.format("%.2f", temp + 0.01 * decimal);
        } else {
            return temp + ".00";
        }
    }

    /**
     * @param price 格式价格,以分为单位
     * @return 如果包含小数就保留两位小数, 如果没有小数就直接返回
     */
    public static String formatPrice2(long price) {
        if (price < 0)
            price = 0;
        long temp = price / 100;
        long decimal = price % 100;

        if (decimal > 0) {//表示有分数,这时候要返回两位小数
            return String.format("%.2f", temp + 0.01 * decimal);
        } else {
            return temp + "";
        }
    }

    /**
     * @param price 格式价格,以分为单位
     * @return 保留两位小数
     */
    public static String formatPrice4Point(long price) {
        if (price < 0)
            price = 0;
        return String.format("%.2f", price / 100 + price % 100 * 0.01);
    }


    /**
     * 获取版本更新的信息,直接调用这个就好了
     *
     * @param isComeMainActivity 创建的时候是否是来自首页
     */
    public static void checkUpdate(final Context context, final boolean isComeMainActivity, final CheckUpdateCallback checkUpdateCallback) {
        final BaseActivity baseActivity = (BaseActivity) context;
        if (!isComeMainActivity) {
            baseActivity.showLoading();
        }
        MHHttpClient.getInstance().post(UpdateResponse.class, ConstantsValue.Url.CHECK_UPDATE, new MHHttpHandler<UpdateResponse>() {
            @Override
            public void onSuccess(UpdateResponse response) {
                if (!isComeMainActivity) {
                    baseActivity.hiddenLoadingView();
                }

                if (null != response && null != response.getData()) {//处理更新逻辑
                    setUpdateData(context, response.getData());
                } else if (!isComeMainActivity) {//表示当前是最新版本,来自首页不显示信息
                    baseActivity.showToastAtCenter("当前已经是最新版本");
                }
                //如果是强制更新就不选择标签了
                if (null == response || null == response.getData() || !response.getData().force_update) {
                    if (null != checkUpdateCallback)
                        checkUpdateCallback.doWithSelectedLabel();
                }
            }

            @Override
            public void onFailure(String content) {
                if (!isComeMainActivity) {
                    baseActivity.hiddenLoadingView();
                    baseActivity.showToastAtCenter(null);
                }
                if (null != checkUpdateCallback)
                    checkUpdateCallback.doWithSelectedLabel();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                if (null != checkUpdateCallback)
                    checkUpdateCallback.doWithSelectedLabel();
            }
        });

    }

    /**
     * 设置更新数据
     */
    private static void setUpdateData(final Context context, final UpdateData data) {
        if (MHStringUtils.isEmpty(data.download_uri) || MHStringUtils.isEmpty(data.version_image))
            return;
//        data.force_update = true;
//        data.image_url = "http://a.hiphotos.baidu.com/image/pic/item/95eef01f3a292df560db8c44be315c6034a87342.jpg";
//        data.image_url = "http://img1.imgtn.bdimg.com/it/u=3191263381,4256867030&fm=214&gp=0.jpg";
//        data.image_url = "http://c.hiphotos.baidu.com/image/pic/item/63d9f2d3572c11dfd3042623612762d0f603c2dd.jpg";

        View view = View.inflate(context, R.layout.activity_app_update_downloaded_info_show, null);
        View iv_close = view.findViewById(R.id.iv_close);
        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);


        final Button confirmBtn = (Button) view.findViewById(R.id.app_update_confirm);
        iv_close.setVisibility(data.force_update ? View.GONE : View.VISIBLE);
        final Dialog dialog = new Dialog(context, R.style.Dialog_loading);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        final String apkPath = ConstantsValue.Video.VIDEO_BASE_PATH + "/temp/apk_" + System.currentTimeMillis() + ".apk";
        final boolean[] downloadSuccess = {false};

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadSuccess[0]) {
                    CommonUtil.installApk(context, apkPath);
                    return;
                }
                confirmBtn.setEnabled(false);
                if (data.force_update)
                    UserInfoUtil.logout();
                MHHttpClient.getInstance().downloadAsyn(data.download_uri, apkPath, new MHHttpProgressHandler() {
                    @Override
                    public void onSuccess(String content) {
                        downloadSuccess[0] = true;
                        if (dialog.isShowing()) {
                            CommonUtil.installApk(context, apkPath);
                        }
                        confirmBtn.setEnabled(true);
                        confirmBtn.setText("去安装");
                    }

                    @Override
                    public void onFailure(String content) {
                        confirmBtn.setEnabled(true);
                        ToastUtils.showToastAtCenter(context, "下载出错啦,请稍后再试");
                    }

                    @Override
                    public void onProgress(float progress) {
                        confirmBtn.setText(String.format("正在升级...%.1f", progress * 100) + "%");
                    }
                });
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ImageLoader.getInstance().displayImage(data.version_image, iv_image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (null == bitmap || bitmap.getWidth() == 0 || bitmap.getHeight() == 0)
                    return;

                int padding = context.getResources().getDimensionPixelSize(R.dimen.update_padding);
                int width = ScreenUtils.getScreenWidth(context) * 3 / 4 - padding * 2;//固定宽度
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.width = width;
                params.height = width * bitmap.getHeight() / bitmap.getWidth();

                int height = ScreenUtils.getScreenHeight(context) * 4 / 5 - padding * 2;
                if (params.height > height) {//太高了这时候要重新处理
                    params.height = height;
                    params.width = height * bitmap.getWidth() / bitmap.getHeight();
                }
                view.setLayoutParams(params);
                bitmap = BitmapUtil.scaleBitmap(bitmap, params.width, params.height);

                //处理圆角图片
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                Bitmap targetBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(targetBitmap);
                float radius = context.getResources().getDisplayMetrics().density * 6;
                RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight() + radius * 2);
                canvas.drawRoundRect(rect, radius, radius, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(bitmap, 0, 0, paint);

                ((ImageView) view).setImageBitmap(targetBitmap);
                dialog.show();
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }


    /**
     * 这个效率高一些
     *
     * @return http://www.rogerblog.cn/2016/03/17/android-proess/
     */
    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {

            MHLogUtil.e(TAG, e);
            return null;
        }
    }


    /**
     * 获取当前网络状态
     *
     * @return NetworkInfo
     */
    public static NetworkInfo getCurrentNetStatus(Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo();
    }

    /**
     * 获取网络连接状态
     *
     * @param ctx
     * @return true:有网 false：没网
     */
    public static boolean isNetworkAvailable(Context ctx) {
        NetworkInfo nki = getCurrentNetStatus(ctx);
        if (nki != null) {
            return nki.isAvailable();
        } else
            return false;
    }


    // 启动安装界面
    public static void installApk(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {// 获取版本号(内部识别号)
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {

            MHLogUtil.e(TAG, e);
            return "0";
        }
    }

    /**
     * @param millisecond 毫秒值
     * @return 返回分, 秒
     */
    public static String formatTime4Second(long millisecond) {
        if (millisecond <= 0) {
            return "0秒";
        } else if (millisecond < 60 * 1000) {
            return String.format("%.1f秒", Math.floor(millisecond / 100.0f) / 10);
        } else {
            long min = millisecond / (60 * 1000);
            long second = millisecond % (60 * 1000);
            return min + "分" + String.format("%.1f秒", Math.floor(second / 100.0f) / 10);
        }
    }

    /**
     * @param millisecond 毫秒值
     * @return 返回xx.xs
     */
    public static String formatTime4S(long millisecond) {
        if (millisecond <= 0) {
            return "0s";
        } else {
            float time = millisecond / 1000.0f;
            return String.format("%.1f秒", time);
        }
    }

    public static String[] formatTimes(long millisecond) {
        String[] strings = new String[2];
        if (millisecond <= 0) {
            strings[0] = "0";
            return strings;
        } else if (millisecond < 60 * 1000) {
            strings[0] = String.format("%.1f", Math.floor(millisecond / 100.0f) / 10);
            return strings;
        } else {
            long min = millisecond / (60 * 1000);
            long second = millisecond % (60 * 1000);
            strings[0] = String.format("%.1f", Math.floor(second / 100.0f) / 10);
            strings[1] = min + "";
            return strings;
        }
    }

}
