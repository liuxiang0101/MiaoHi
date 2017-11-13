package com.haiqiu.miaohi.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.haiqiu.miaohi.receiver.RoateScreenEvent;

import org.greenrobot.eventbus.EventBus;

public class ScreenSwitchUtils {
    private volatile static ScreenSwitchUtils mInstance;
    // 是否是竖屏
    private boolean isPortrait = true;

    private SensorManager sm;
    private OrientationSensorListener listener;
    private Sensor sensor;

    private SensorManager sm1;
    private Sensor sensor1;
    private OnScreenRotate onScreenRotate;
    private Activity context;
//    private OrientationSensorListener1 listener1;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 888:
                    int orientation = msg.arg1;
                    if (orientation > 45 && orientation < 135) {
                        if (isPortrait) {
                            //切换成横屏反向：ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
//                            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                            isPortrait = false;
                            if(null != onScreenRotate) onScreenRotate.onScreenRotate(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                            EventBus.getDefault().post(new RoateScreenEvent(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE));
                            Log.i("触发", "触发");
                        }
                    } else if (orientation > 135 && orientation < 225) {
                        if (!isPortrait) {
                        /*
                         * 切换成竖屏反向：ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT(9),
                         * ActivityInfo.SCREEN_ORIENTATION_SENSOR:根据重力感应自动旋转
                         * 此处正常应该是上面第一个属性，但是在真机测试时显示为竖屏正向，所以用第二个替代
                         */
//                            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                            isPortrait = true;
                            if(null != onScreenRotate) onScreenRotate.onScreenRotate(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                            EventBus.getDefault().post(new RoateScreenEvent(ActivityInfo.SCREEN_ORIENTATION_SENSOR));
                            Log.i("触发", "触发");
                        }
                    } else if (orientation > 225 && orientation < 315) {
                        if (isPortrait) {
                            //切换成横屏：ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//                            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            if(null != onScreenRotate) onScreenRotate.onScreenRotate(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            EventBus.getDefault().post(new RoateScreenEvent(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE));
                            isPortrait = false;
                            Log.i("触发", "触发");
                        }
                    } else if ((orientation > 315 && orientation < 360) || (orientation > 0 && orientation < 45)) {
                        if (!isPortrait) {
                            //切换成竖屏ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//                            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            if(null != onScreenRotate) onScreenRotate.onScreenRotate(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            EventBus.getDefault().post(new RoateScreenEvent(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT));
                            isPortrait = true;
                            Log.i("触发", "触发");
                        }
                    }
                    break;
                default:
                    break;
            }

        };
    };

    /** 返回ScreenSwitchUtils单例 **/
    public static ScreenSwitchUtils init(Activity context) {
        if (mInstance == null) {
            synchronized (ScreenSwitchUtils.class) {
                if (mInstance == null) {
                    mInstance = new ScreenSwitchUtils(context);
                }
            }
        }
        return mInstance;
    }

    private ScreenSwitchUtils(Activity context) {
        // 注册重力感应器,监听屏幕旋转
        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new OrientationSensorListener(mHandler);

        // 根据 旋转之后/点击全屏之后 两者方向一致,激活sm.
        sm1 = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor1 = sm1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        listener1 = new OrientationSensorListener1();
        this.context = context;

    }

    /** 开始监听 */
    public void start() {
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    /** 停止监听 */
    public void stop() {
        sm.unregisterListener(listener);
    }

    public boolean isPortrait(){
        return this.isPortrait;
    }

    /**
     * 重力感应监听者
     */
    public class OrientationSensorListener implements SensorEventListener {
        private static final int _DATA_X = 0;
        private static final int _DATA_Y = 1;
        private static final int _DATA_Z = 2;

        public static final int ORIENTATION_UNKNOWN = -1;

        private Handler rotateHandler;

        public OrientationSensorListener(Handler handler) {
            rotateHandler = handler;
        }

        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            int orientation = ORIENTATION_UNKNOWN;
            float X = -values[_DATA_X];
            float Y = -values[_DATA_Y];
            float Z = -values[_DATA_Z];
            float magnitude = X * X + Y * Y;
            // Don't trust the angle if the magnitude is small compared to the y
            // value
            if (magnitude * 4 >= Z * Z) {
                // 屏幕旋转时
                float OneEightyOverPi = 57.29577957855f;
                float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
                orientation = 90 - (int) Math.round(angle);
                // normalize to 0 - 359 range
                while (orientation >= 360) {
                    orientation -= 360;
                }
                while (orientation < 0) {
                    orientation += 360;
                }
            }
            if (rotateHandler != null&&checkAllowSystemRotate()==1) {
                rotateHandler.obtainMessage(888, orientation, 0).sendToTarget();
            }
        }
    }

    /**
     * 屏幕旋转监听
     * @param onScreenRotate
     */
    public void setOnScreenRotateListener(OnScreenRotate onScreenRotate){
        this.onScreenRotate = onScreenRotate;
    }


    public interface OnScreenRotate{
        /**
         * 回调屏幕旋转参数
         * @param requestedOrientation 旋转参数
         */
        void onScreenRotate(int requestedOrientation);
    }

    /**
     * 检测是否打开屏幕锁定
     * 打开自动旋转 返回flag=1
     * 关闭自动旋转 返回flag=0
     * @return
     */
    private int checkAllowSystemRotate() {
        int flag = 0;
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS)
                != PackageManager.PERMISSION_GRANTED){//未授权
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_SETTINGS}, 0);
        } else {//已授权
            flag = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION, 0);
        }
        return flag;
    }

}
