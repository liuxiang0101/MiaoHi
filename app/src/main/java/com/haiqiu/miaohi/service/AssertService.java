package com.haiqiu.miaohi.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.ResourceUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.ZipUtils;

/**
 * Created by zhandalin on 2016-12-06 18:32.
 * 说明:Copy Assert 下指定的文件到私有的file目录下
 */
public class AssertService extends Service implements Runnable {
    public final static String[] resourceNames = new String[]{ConstantsValue.VideoEdit.PASTER_DIR_NAME, ConstantsValue.VideoEdit.MUSIC_DIR_NAME};
    private final static String TAG = "AssertService";

    /**
     * 是否正在运行
     */
    private static boolean mIsRunning;

    @Override
    public void onCreate() {
        super.onCreate();
        MHLogUtil.d(TAG, "onCreate");
        mIsRunning = true;
        new Thread(this).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {
        try {
            boolean result = false;
            for (String resourceName : resourceNames) {
                String path = getFilesDir() + "/" + resourceName + ".zip";
                result = ResourceUtils.copyFile(getApplicationContext(), resourceName + ".zip", path);
                if (!result) break;
                result = ZipUtils.UnZipFolder(path, getFilesDir().getAbsolutePath() + "/" + resourceName);
                if (!result) break;
            }
            SpUtils.put(ConstantsValue.Sp.COPY_FILE_FLAG, result);
            MHLogUtil.d(TAG, "Copy Success");
        } catch (Exception e) {
            MHLogUtil.d(TAG, "Exception=" + e.getMessage());
            MHLogUtil.e(TAG,e);
        }
        mIsRunning = false;
        stopSelf();
    }

    public static boolean isRunning() {
        return mIsRunning;
    }

}
