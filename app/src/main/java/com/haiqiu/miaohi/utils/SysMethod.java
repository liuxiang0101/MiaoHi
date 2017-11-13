package com.haiqiu.miaohi.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;


public class SysMethod {
		
	public static void toastShowCenter(Context cnt, CharSequence txt, int type){
		try {
			Toast toast = Toast.makeText(cnt, txt, type);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} catch (Exception e) {
			MHLogUtil.d("SysMethod : toastShowCenter : " + e.getMessage());
		}
	}
	
	public static void toastShowCenter(Context cnt, int id, int type){
		try {
			Toast toast = Toast.makeText(cnt, id, type);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} catch (Exception e) {
			MHLogUtil.d("SysMethod : toastShowCenter : " + e.getMessage());
		}
	}

	public static String getVersionName(Context cnt) {
		String version = "0";
		
		PackageManager pm = cnt.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(cnt.getPackageName(), 0);
			version = pi.versionName;
		} catch (NameNotFoundException e) {
			MHLogUtil.i("SysMethod : getVersionName : " + e.getMessage());
		}
		
		return version;
	}
	
	public static String getVersionCode(Context context) {
		String verCode = "0";
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			verCode = Integer.toString(pi.versionCode);
		} catch (NameNotFoundException e) {
			MHLogUtil.i("SysMethod : getVersionCode : " + e.getMessage());
		}
		
		return verCode;
	}
	
	public static String getImei(Context cnt) {
		TelephonyManager tm = (TelephonyManager) cnt.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		if (TextUtils.isEmpty(imei)) {
			return "0";
		} else {
			return imei;
		}
	}
	
	public static String getMacAddress(Context cnt) {
		WifiInfo wifiInfo = null;
		String result = "0";

		WifiManager wm = (WifiManager) cnt.getSystemService(Context.WIFI_SERVICE);
		if (wm != null) {
			wifiInfo = wm.getConnectionInfo();
		}
		
		if (wifiInfo != null) {
			result= wifiInfo.getMacAddress();
		}
		
		if (TextUtils.isEmpty(result)) {
			result = "0";
		}

		return result;
	}
	
	public static void exitApplication(Context cnt){
		try {
			NotificationManager nm = ((NotificationManager) cnt.getSystemService(Context.NOTIFICATION_SERVICE));
			nm.cancelAll();
			
			android.os.Process.killProcess(android.os.Process.myPid());
		} catch (Exception e) {
			MHLogUtil.d("SysMethod : exit miaohi : " + e.getMessage());
		} 
	}

}
