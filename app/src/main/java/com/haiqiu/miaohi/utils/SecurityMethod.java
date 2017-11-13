package com.haiqiu.miaohi.utils;

import android.text.TextUtils;

import com.umeng.socialize.net.utils.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * 安全相关的工具类
 */
public class SecurityMethod {
	private static final String TAG="SecurityMethod";
	public static String aesEncryptToBase64(String data ,String key,String vector) {
		String result = "";
		if (TextUtils.isEmpty(data) || TextUtils.isEmpty(key) || TextUtils.isEmpty(vector)) return result;
	        
		if(key.length() != 16) return result;
	
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
	        
	        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
	        IvParameterSpec iv = new IvParameterSpec(vector.getBytes()); 
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
	        byte[] encrypted = cipher.doFinal(data.getBytes("utf-8"));
	        
	        result = Base64.encodeBase64String(encrypted);
		} catch (Exception e) {
			MHLogUtil.e(TAG,e);
		}
        
        return result;
	}
	    
	public static String aesBase64StringDecrypt(String data, String key, String vector) {
		String result = "";
		if (TextUtils.isEmpty(data) || TextUtils.isEmpty(key) || TextUtils.isEmpty(vector)) return result;
        
		if(key.length() != 16) return result;
		
		try {
			byte[] raw = key.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(vector.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			
			byte[] plainbyte = Base64.decodeBase64(data);
            byte[] resbyte = cipher.doFinal(plainbyte);
            
            result = new String(resbyte, "utf-8");
		} catch (Exception e) {
			MHLogUtil.e(TAG,e);
		}
	
		return result;
	}

}