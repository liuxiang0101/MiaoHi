package com.haiqiu.miaohi.statistics;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.haiqiu.miaohi.utils.MHLogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;


/**
 * Created by LiuXiang on 2016/11/21.
 * 获取手机的一些基本信息，用于统计
 */
public class StatisticsStateValue {
    protected static final String TAG = StatisticsStateValue.class.getName();
    public static final String NETWORK_TYPE = "";
    public static final String MOBILE_NETWORK = "2G/3G";
    public static final String WIFI_NETWORK = "Wi-Fi";
    public static final int e = 8;
    private static final String OS_VERSION_NAME = "ro.miui.ui.version.name";

    public StatisticsStateValue() {
    }

    //获取App版本号
    public static String getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return String.valueOf(versionCode);
        } catch (PackageManager.NameNotFoundException exception) {
            return "";
        }
    }

    //获取App版本名称
    public static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException exception) {
            return "";
        }
    }

    public static boolean isCanRead(Context context, String string) {
        boolean isNormal = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class aClass = Class.forName("android.content.Context");
                Method method = aClass.getMethod("checkSelfPermission", String.class);
                int intValue = ((Integer) method.invoke(context, string)).intValue();
                isNormal = intValue == 0;
            } catch (Exception var6) {
                isNormal = false;
            }
        } else {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager.checkPermission(string, context.getPackageName()) == 0) {
                isNormal = true;
            }
        }

        return isNormal;
    }

    //获取程序ApplicationLabel
    public static String getApplicationLabel(Context context) {
        PackageManager packageManager = context.getPackageManager();

        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException var4) {
            applicationInfo = null;
        }

        String applicationLabel = applicationInfo != null ? (String) packageManager.getApplicationLabel(applicationInfo) : "";
        return applicationLabel;
    }

    //获取设备唯一标识
    private static String getEquipmentIdentification() {
        String[] strings = new String[]{"/sys/class/net/wlan0/address", "/sys/class/net/eth0/address", "/sys/devices/virtual/net/wlan0/address"};
        for (int i = 0; i < strings.length; ++i) {
            try {
                String unique_identifier = getUniqueIdentifier(strings[i]);
                if (unique_identifier != null) {
                    return unique_identifier;
                }
            } catch (Exception exception) {
                MHLogUtil.e(TAG, "open file  Failed" + exception.getMessage());
            }
        }

        return null;
    }

    private static String getUniqueIdentifier(String mark_id) throws FileNotFoundException {
        String unique_identifier = null;//获取Linux层唯一标识
        FileReader fileReader = new FileReader(mark_id);
        BufferedReader bufferedReader = null;
        if (fileReader != null) {
            try {
                bufferedReader = new BufferedReader(fileReader, 1024);
                unique_identifier = bufferedReader.readLine();
            } catch (IOException exception) {
                MHLogUtil.e(TAG, "Could not read from file " + mark_id + exception);
            } finally {
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }

                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }

            }
        }

        return unique_identifier;
    }

    //获取手机CPU信息
    public static String getCpuInfo() {
        String cpu_info = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        try {
            fileReader = new FileReader("/proc/cpuinfo");
            if (fileReader != null) {
                try {
                    bufferedReader = new BufferedReader(fileReader, 1024);
                    cpu_info = bufferedReader.readLine();
                    bufferedReader.close();
                    fileReader.close();
                } catch (IOException exception) {
                    MHLogUtil.e(TAG, "Could not read from file /proc/cpuinfo" + exception.getMessage());
                }
            }
        } catch (FileNotFoundException exception) {
            MHLogUtil.e(TAG, "Could not open file /proc/cpuinfo" + exception.getMessage());
        }

        if (cpu_info != null) {
            int i = cpu_info.indexOf(58) + 1;
            cpu_info = cpu_info.substring(i);
            return cpu_info.trim();
        } else {
            return "";
        }
    }

    //获取国际移动用户识别码
    public static String getImsi(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        String imsi = null;
        if (isCanRead(context, "android.permission.READ_PHONE_STATE")) {
            imsi = telephonyManager.getSubscriberId();
        }

        return imsi;
    }

    //获取手机网络状态
    public static String getNetworkState(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        String networkState = null;
        if (isCanRead(context, "android.permission.READ_PHONE_STATE")) {
            networkState = telephonyManager.getNetworkOperator();
        }

        return networkState;
    }

    //获取屏幕信息
    public static String getScreenSizeNeedViewFinish(Context context) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService("window");
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            int widthPixels = displayMetrics.widthPixels;
            int heightPixels = displayMetrics.heightPixels;
            String screenSize = heightPixels + "*" + widthPixels;
            return screenSize;
        } catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }
    }

    //获取网络信息
    public static String[] getNetworkInfo(Context context) {
        String[] networkInfo = new String[]{"", ""};

        try {
            if (!isCanRead(context, "android.permission.ACCESS_NETWORK_STATE")) {
                networkInfo[0] = "";
                return networkInfo;
            }

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                networkInfo[0] = "";
                return networkInfo;
            }

            NetworkInfo networkInfo1 = connectivityManager.getNetworkInfo(1);
            if (networkInfo1 != null && networkInfo1.getState() == NetworkInfo.State.CONNECTED) {
                networkInfo[0] = "Wi-Fi";
                return networkInfo;
            }

            NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(0);
            if (networkInfo2 != null && networkInfo2.getState() == NetworkInfo.State.CONNECTED) {
                networkInfo[0] = "2G/3G";
                networkInfo[1] = networkInfo2.getSubtypeName();
                return networkInfo;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return networkInfo;
    }

    //判断是否连接WiFi
    public static boolean isUseWifi(Context context) {
        return "Wi-Fi".equals(getNetworkInfo(context)[0]);
    }

    //判断是否连接
    public static boolean isConnected(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null ? networkInfo.isConnectedOrConnecting() : false;
        } catch (Exception exception) {
            return true;
        }
    }

    //是否挂载SD卡
    public static boolean isHasSdCard() {
        return Environment.getExternalStorageState().equals("mounted");
    }


    //获取设备标识
    public static String getEquipmentId(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            String string = getEquipmentIdentification();
            if (string == null) {
                string = getWifiMac(context);
            }

            return string;
        } else {
            return getWifiMac(context);
        }
    }

    //获取设备WiFi mac地址
    private static String getWifiMac(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (isCanRead(context, "android.permission.ACCESS_WIFI_STATE")) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                return wifiInfo.getMacAddress();
            } else {
                MHLogUtil.e(TAG, new Object[]{"Could not get mac address.[no permission android.permission.ACCESS_WIFI_STATE"}.toString());
                return "";
            }
        } catch (Exception exception) {
            MHLogUtil.e(TAG, new Object[]{"Could not get mac address." + exception.toString()}.toString());
            return "";
        }
    }

    //获取屏幕信息
    public static String getScreenSize(Context context) {
        int[] ints = getWidthAndHeightPixels(context);
        if (ints != null) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(ints[0]);
            stringBuffer.append("*");
            stringBuffer.append(ints[1]);
            return stringBuffer.toString();
        } else {
            return "Unknown";
        }
    }

    //获取屏幕宽高
    public static int[] getWidthAndHeightPixels(Context context) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService("window");
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            int widthPixels = -1;
            int heightPixels = -1;
            if ((context.getApplicationInfo().flags & 8192) == 0) {
                widthPixels = getFieldInt(displayMetrics, "noncompatWidthPixels");
                heightPixels = getFieldInt(displayMetrics, "noncompatHeightPixels");
            }

            if (widthPixels == -1 || heightPixels == -1) {
                widthPixels = displayMetrics.widthPixels;
                heightPixels = displayMetrics.heightPixels;
            }

            int[] ints = new int[2];
            if (widthPixels > heightPixels) {
                ints[0] = heightPixels;
                ints[1] = widthPixels;
            } else {
                ints[0] = widthPixels;
                ints[1] = heightPixels;
            }

            return ints;
        } catch (Exception exception) {
            MHLogUtil.e(TAG, "read resolution fail" + exception);
            return null;
        }
    }

    private static int getFieldInt(Object object, String string) {
        try {
            Field field = DisplayMetrics.class.getDeclaredField(string);
            field.setAccessible(true);
            return field.getInt(object);
        } catch (Exception exception) {
            exception.printStackTrace();
            return -1;
        }
    }

    //返回注册的网络运营商的名字
    public static String getOperatorName(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getNetworkOperatorName();
        } catch (Exception exception) {
            MHLogUtil.e(TAG, "read carrier fail" + exception);
            return "Unknown";
        }
    }

    //获取精确到秒的时间字符
    public static String getExactTimeBySec(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String dateString = format.format(date);
        return dateString;
    }

    //获取精确到天的时间字符
    public static String getExactTimeByDay() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String dateString = format.format(date);
        return dateString;
    }

    //获取日期的date数据
    public static Date getTimeForData(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            Date date = format.parse(dateString);
            return date;
        } catch (Exception exception) {
            return null;
        }
    }

    //获取两个时间的差
    public static int getTimeDifference(Date date0, Date date1) {
        if (date0.after(date1)) {
            Date date = date0;
            date0 = date1;
            date1 = date;
        }

        long time1 = date0.getTime();
        long time2 = date1.getTime();
        long timeDifference = time2 - time1;
        return (int) (timeDifference / 1000L);
    }

    //获取包名
    public static String getAppPackageName(Context context) {
        return context.getPackageName();
    }

    //获取AppLabel
    public static String getAppLabel(Context context) {
        String appLabel = null;

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appLabel = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
        } catch (Exception exception) {
            MHLogUtil.e(TAG, exception.getMessage());
        }

        return appLabel;
    }

    /**
     * 获取最终获得的设备标识符
     *
     * @param context
     * @return
     */
    private static String getFinalNeedDeviceId1(Context context) {
        String deviceId = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (Build.VERSION.SDK_INT >= 23) {
            if (telephonyManager != null) {
                try {
                    if (isCanRead(context, "android.permission.READ_PHONE_STATE")) {
                        deviceId = telephonyManager.getDeviceId();
                        MHLogUtil.e(TAG, new Object[]{"getDeviceId, IMEI: " + deviceId}.toString());
                    }
                } catch (Exception exception) {
                    MHLogUtil.e(TAG, "No IMEI." + exception);
                }
            }

            if (TextUtils.isEmpty(deviceId)) {
                deviceId = getEquipmentIdentification();
                MHLogUtil.e(TAG, "getDeviceId, mc: " + deviceId);
                if (TextUtils.isEmpty(deviceId)) {
                    deviceId = Settings.Secure.getString(context.getContentResolver(), "android_id");
                    MHLogUtil.e(TAG, new Object[]{"getDeviceId, android_id: " + deviceId}.toString());
                    if (TextUtils.isEmpty(deviceId)) {
                        if (Build.VERSION.SDK_INT >= 9) {
                            deviceId = Build.SERIAL;
                        }

                        MHLogUtil.e(TAG, "getDeviceId, serial no: ");
                    }
                }
            }
        } else {
            if (telephonyManager != null) {
                try {
                    if (isCanRead(context, "android.permission.READ_PHONE_STATE")) {
                        deviceId = telephonyManager.getDeviceId();
                    }
                } catch (Exception exception) {
                    MHLogUtil.e(TAG, "No IMEI." + exception);
                }
            }

            if (TextUtils.isEmpty(deviceId)) {
                MHLogUtil.e(TAG, new Object[]{"No IMEI."}.toString());
                deviceId = getEquipmentId(context);
                if (TextUtils.isEmpty(deviceId)) {
                    MHLogUtil.e(TAG, new Object[]{"Failed to take mac as IMEI. Try to use Secure.ANDROID_ID instead."}.toString());
                    deviceId = Settings.Secure.getString(context.getContentResolver(), "android_id");
                    MHLogUtil.e(TAG, new Object[]{"getDeviceId: Secure.ANDROID_ID: " + deviceId}.toString());
                }
            }
        }

        return deviceId;
    }

    private static String getFinalNeedDeviceId2(Context context) {
        String deviceId = "";
        //手机信息管理器
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (Build.VERSION.SDK_INT >= 23) {//新版本Android处理方法
            deviceId = Settings.Secure.getString(context.getContentResolver(), "android_id");
            MHLogUtil.e(TAG, new Object[]{"getDeviceId, android_id: " + deviceId}.toString());
            if (TextUtils.isEmpty(deviceId)) {
                deviceId = getEquipmentIdentification();
                MHLogUtil.e(TAG, new Object[]{"getDeviceId, mc: " + deviceId}.toString());
                if (TextUtils.isEmpty(deviceId)) {
                    if (telephonyManager != null) {
                        try {
                            if (isCanRead(context, "android.permission.READ_PHONE_STATE")) {
                                deviceId = telephonyManager.getDeviceId();
                                MHLogUtil.e(TAG, new Object[]{"getDeviceId, IMEI: " + deviceId}.toString());
                            }
                        } catch (Exception var5) {
                            MHLogUtil.e(TAG, "No IMEI." + var5.getMessage());
                        }
                    }

                    if (TextUtils.isEmpty(deviceId)) {
                        if (Build.VERSION.SDK_INT >= 9) {
                            deviceId = Build.SERIAL;
                        }

                        MHLogUtil.e(TAG, new Object[]{"getDeviceId, serial no: " + deviceId}.toString());
                    }
                }
            }
        } else {//旧版本Android处理方法
            deviceId = Settings.Secure.getString(context.getContentResolver(), "android_id");
            MHLogUtil.e(TAG, new Object[]{"getDeviceId:  Secure.ANDROID_ID: " + deviceId}.toString());
            if (TextUtils.isEmpty(deviceId)) {
                MHLogUtil.e(TAG, new Object[]{"No IMEI."}.toString());
                deviceId = getEquipmentId(context);
                if (TextUtils.isEmpty(deviceId) && telephonyManager != null) {
                    try {
                        if (isCanRead(context, "android.permission.READ_PHONE_STATE")) {
                            deviceId = telephonyManager.getDeviceId();
                        }
                    } catch (Exception exception) {
                        MHLogUtil.e(TAG, "No IMEI." + exception.getMessage());
                    }
                }
            }
        }

        return deviceId;
    }

    //获取系统OS
    public static String getEquipmentOS(Context context) {
        String phoneOs = null;
        Properties properties = getProperties();//获取配置信息

        try {
            phoneOs = properties.getProperty("ro.miui.ui.version.name");
            if (TextUtils.isEmpty(phoneOs)) {
                if (isFlyme()) {
                    phoneOs = "Flyme";
                } else if (!TextUtils.isEmpty(getYunOsProperty(properties))) {
                    phoneOs = "YunOS";
                }
            } else {
                phoneOs = "MIUI";
            }
        } catch (Exception exception) {
            phoneOs = null;
            exception.printStackTrace();
        }

        return phoneOs;
    }

    //获取OS配置信息
    public static String getEquipmentOSProperties(Context context) {
        String OS = null;
        Properties properties = getProperties();

        try {
            OS = properties.getProperty("ro.miui.ui.version.name");
            if (TextUtils.isEmpty(OS)) {
                if (isFlyme()) {
                    try {
                        OS = getFlymeOsProperty(properties);
                    } catch (Exception exception) {

                    }
                } else {
                    try {
                        OS = getYunOsProperty(properties);
                    } catch (Exception exception) {

                    }
                }
            }
        } catch (Exception exception) {
            OS = null;
            exception.printStackTrace();
        }

        return OS;
    }

    //YunOS(阿里巴巴集团旗下智能操作系统)
    private static String getYunOsProperty(Properties properties) {
        String property = properties.getProperty("ro.yunos.version");
        return !TextUtils.isEmpty(property) ? property : null;
    }

    //FlymeOS(魅族)
    private static String getFlymeOsProperty(Properties properties) {
        try {
            String property = properties.getProperty("ro.build.display.id").toLowerCase(Locale.getDefault());
            if (property.contains("flyme os")) {
                return property.split(" ")[2];
            }
        } catch (Exception exception) {

        }

        return null;
    }

    //获取设备配置信息
    private static Properties getProperties() {
        Properties properties = new Properties();
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
            properties.load(fileInputStream);
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

        }

        return properties;
    }

    private static boolean isFlyme() {
        try {
            Method method = Build.class.getMethod("hasSmartBar");
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    // 获取设备类型（平板/手机）
    public static String EquipmentType(Context context) {
        if (context == null) {
            return null;
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            int phoneType = telephonyManager.getPhoneType();
            return phoneType == 0 ? "Tablet" : "Phone";
        }
    }
}
