package com.haiqiu.miaohi.utils.upload;

import android.content.Context;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.GetPhoneStateUtils;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.MessageDigestUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 上传图片
 * Created by ningl on 16/12/17.
 */
public class UploadPictureUtil {

    //图片路径
    private String picturePath;
    //上传图片文件状态
    private UploadState uploadPictureState;
    //七牛上传管理器
    private UploadManager uploadManager;
    private Context context;
    //上传图片token
    private String imgToken;
    //图片名称
    private String imgName;
    private UploadListUtil.UploadCallback uploadCallback;

    /**
     * 上传状态
     */
    public enum UploadState{
        //未上传
        UNLOAD,
        //上传失败
        UPLOADFAIL,
        //上传成功
        UPLAODSUCCESS,
        //正在上传
        UPLOADING
    }

    public UploadPictureUtil(String picturePath, Context context) {
        this.picturePath = picturePath;
        this.context = context;
        uploadManager = new UploadManager();
    }

    /**
     * 上传图片
     */
    public void uploadPicture(){
        //获取上传图片的必要参数
        uploadPictureState = UploadState.UPLOADING;
        imgToken = SpUtils.getString(ConstantsValue.Sp.TOKEN_QINIU_UPLOAD_IMAGE);
        imgName = "img_" + getName() + "_" + formatDate();
        if(null == imgName||null == imgName||null == imgToken){
            uploadPictureState = UploadState.UPLOADFAIL;
            return;
        } else {
            File pictureFile = new File(picturePath);
            if(pictureFile==null||!pictureFile.exists()){//文件不存在
                uploadPictureState = UploadState.UPLOADFAIL;
                return;
            }
        }
        //开始上传
        uploadManager.put(picturePath, imgName, imgToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                //上传完成
                if (info.isOK()){
                    //上传成功
                    uploadPictureState = UploadState.UPLAODSUCCESS;
                    if(uploadCallback!=null) uploadCallback.uploadResultCallback(null, imgName, true);
                } else {
                    //上传失败
                    uploadPictureState = UploadState.UPLOADFAIL;
                    if(uploadCallback!=null) uploadCallback.uploadResultCallback(null, imgName, false);
                }
            }
        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                //上传进度
                uploadCallback.progress(percent);
            }
        }, null));

    }

    /**
     * 获取加密后名字
     * @return
     */
    private String getName(){
        //得到手机mac值的SHA256加密结果
        String mac = GetPhoneStateUtils.getPhoneMac();
        if (!MHStringUtils.isEmpty(mac)) {
            String encryptedMac = MessageDigestUtils.getStringSHA256(mac);
            //得到手机IMEI值的SHA256加密结果
            String imei = GetPhoneStateUtils.getPhoneImei(context);
            if (!MHStringUtils.isEmpty(imei)) {
                String encryptedIMEI = MessageDigestUtils.getStringSHA256(imei);
                //获取最终的MD5加密值
                return MessageDigestUtils.getStringMD5(encryptedMac + encryptedIMEI);
            }
        }
        return "";
    }

    /**
     * 格式化当前时间
     */
    private String formatDate() {
        // 获取"yyyy-MM-dd-HH:mm:ss"格式的当前时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
        String time = format.format(new Date(System.currentTimeMillis()));
        return time;
    }

    /**
     * 设置进度监听
     * @param uploadCallback
     */
    public void setOnProgressListener(UploadListUtil.UploadCallback uploadCallback){
        this.uploadCallback = uploadCallback;
    }
}
