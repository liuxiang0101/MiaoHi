package com.haiqiu.miaohi.utils.upload;

import android.content.Context;
import android.media.MediaMetadataRetriever;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.GetPhoneStateUtils;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.MessageDigestUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 上传视频工具类
 * Created by ningl on 2016/7/21.
 */
public class UploadVideoUtil {

    //上传视频文件状态
    private UploadState uploadVideoState;
    //上传图片文件状态
    private UploadState uploadCoverState;
    //上传状态
    private UploadState uploadState;
    //图片路径
    private String imgPath;
    //视频路径
    private String videoPath;
    //七牛上传管理器
    private UploadManager uploadManager;
    //视频时长
    private String videoDuration;
    //上传图片token
    private String imgToken;
    //上传视频token
    private String videoToken;
    //上传视频是否完成
    private boolean isVideoUploadFinish;
    //上传视频封面是否完成
    private boolean isCoverUploadFinish;
    //视频名称
    private String videoName;
    //视频封面名称
    private String imgName;

    private UploadCallback uploadCallback;

    private long duration;

    private Context mContext;

    public UploadVideoUtil(Context mContext, String imgPath, String videoPath) {
        this.imgPath = imgPath;
        this.videoPath = videoPath;
        this.mContext = mContext;
        uploadManager = new UploadManager(getConfig());
//        videoDuration = getVideoDuration();
        uploadVideoState = UploadState.UNLOAD;
        uploadCoverState = UploadState.UNLOAD;
        uploadState = UploadState.UNLOAD;
    }

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

    /**
     * 上传视频文件
     */
    public void uploadVideoFile(){
        uploadState = UploadState.UPLOADING;
        videoName = "video_" + getName() + "_" + formatDate();
        videoToken = SpUtils.getString(ConstantsValue.Sp.TOKEN_QINIU_UPLOAD_VIDEO);
        new android.os.Handler().post(new Runnable() {
            @Override
            public void run() {
                if(uploadCallback!=null) uploadCallback.preUpload(imgName);
            }
        });
        if(null == videoPath||null == videoName||null == videoToken){
            uploadVideoState = UploadState.UPLOADFAIL;
            uploadCallback.uploadResultCallback(null, null, false);
            isVideoUploadFinish = true;
            if(isVideoUploadFinish&&isCoverUploadFinish) checkUploadResult();
            return;
        } else {
            File videoFile = new File(videoPath);
            if(videoFile==null||!videoFile.exists()){//文件不存在
                uploadVideoState = UploadState.UPLOADFAIL;
                uploadCallback.uploadResultCallback(null, null, false);
                isVideoUploadFinish = true;
                if(isVideoUploadFinish&&isCoverUploadFinish) checkUploadResult();
                return;
            }
        }
        uploadManager.put(videoPath, videoName, videoToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if(info.isOK()){//上传视频文件成功
                    uploadVideoState = UploadState.UPLAODSUCCESS;
                } else {
                    uploadVideoState = UploadState.UPLOADFAIL;
                    uploadCallback.uploadResultCallback(null, null, false);
                }
                isVideoUploadFinish = true;
                if(isVideoUploadFinish&&isCoverUploadFinish) checkUploadResult();
            }
        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                if(percent == 100&&uploadCoverState == UploadState.UPLOADFAIL) return;
                uploadCallback.progress(percent*0.95);
            }
        }, null));
    }

    /**
     * 上传视频封面图片文件
     */
    public void uploadCoverFile(){
        uploadState = UploadState.UPLOADING;
        imgToken = SpUtils.getString(ConstantsValue.Sp.TOKEN_QINIU_UPLOAD_IMAGE);
        imgName = "img_" + getName() + "_" + formatDate();
        if(null == imgName||null == imgName||null == imgToken){
            uploadCoverState = UploadState.UPLOADFAIL;
            isCoverUploadFinish = true;
            if(isVideoUploadFinish&&isCoverUploadFinish) checkUploadResult();
            return;
        } else {
            File coverFile = new File(imgPath);
            if(coverFile==null||!coverFile.exists()){//文件不存在
                uploadCoverState = UploadState.UPLOADFAIL;
                isCoverUploadFinish = true;
                if(isVideoUploadFinish&&isCoverUploadFinish) checkUploadResult();
                return;
            }
        }
        uploadManager.put(imgPath, imgName, imgToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if(info.isOK()){//上传视频封面成功
                    uploadCoverState = UploadState.UPLAODSUCCESS;
                } else {
                    uploadCoverState = UploadState.UPLOADFAIL;
                }
                isCoverUploadFinish = true;
                if(isVideoUploadFinish&&isCoverUploadFinish) checkUploadResult();
            }
        }, null);
    }

    /**
     * 上传
     */
    public void uploadFile(){
        //上传到七牛成功 通知业务后台失败
        if(uploadVideoState == UploadState.UPLAODSUCCESS&&uploadCoverState == UploadState.UPLAODSUCCESS){
            uploadCallback.progress(0.95);
            uploadCallback.uploadResultCallback(videoName, imgName, true);
        }
        //视频上传失败或者未上传状态要上传
        if(uploadVideoState == UploadState.UNLOAD || uploadVideoState == UploadState.UNLOAD) uploadVideoFile();
        //视频封面上传失败或者未上传要上传
        if(uploadCoverState == UploadState.UNLOAD || uploadCoverState == UploadState.UNLOAD) uploadCoverFile();
    }

    /**
     * 获取上传视频参数
     * @return
     */
    private Configuration getConfig (){
        Configuration config = new Configuration.Builder()
                .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认 256K
                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认 512K
                .connectTimeout(10) // 链接超时。默认 10秒
                .responseTimeout(60) // 服务器响应超时。默认 60秒
//                .recorder(recorder)  // recorder 分片上传时，已上传片记录器。默认 null
//                .recorder(recorder, keyGen)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。默认 Zone.zone0
                .build();
        return config;
    }

    /**
     * 获取视频时长
     * @return
     */
    private String getVideoDuration(){
        long second = 0;
        try {
            if(duration!=0){
                return duration+"";
            }
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath);
            //获取视频时长
            String duration = mediaMetadataRetriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
            second = Long.valueOf(duration) / 1000;
        }catch (Exception e){
            return 0+"";
        }

        return "" + second;
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
     * 获取加密后名字
     * @return
     */
    private String getName(){
        //得到手机mac值的SHA256加密结果
        String mac = GetPhoneStateUtils.getPhoneMac();
        if (!MHStringUtils.isEmpty(mac)) {
            String encryptedMac = MessageDigestUtils.getStringSHA256(mac);
            //得到手机IMEI值的SHA256加密结果
            String imei = GetPhoneStateUtils.getPhoneImei(mContext);
            if (!MHStringUtils.isEmpty(imei)) {
                String encryptedIMEI = MessageDigestUtils.getStringSHA256(imei);
                //获取最终的MD5加密值
                return MessageDigestUtils.getStringMD5(encryptedMac + encryptedIMEI);
            }
        }
        return "";
    }

    /**
     * 检测上传结果
     * @return
     */
    private void checkUploadResult(){
        //全部成功回调成功
        if(uploadVideoState == UploadState.UPLAODSUCCESS&&uploadCoverState == UploadState.UPLAODSUCCESS){
            uploadCallback.uploadResultCallback(videoName, imgName, true);
            uploadState = UploadState.UPLAODSUCCESS;
        } else {
            uploadCallback.uploadResultCallback(null, null, false);
            uploadState = UploadState.UPLOADFAIL;
        }
        if(uploadVideoState == UploadState.UPLAODSUCCESS){
            uploadVideoState = UploadState.UPLAODSUCCESS;
        }
        if(uploadCoverState == UploadState.UPLAODSUCCESS){
            uploadCoverState = UploadState.UPLAODSUCCESS;
        }
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * 获取上传状态
     */
    private UploadState getUploadState(){
        return uploadState;
    }

    /**
     * 设置进度监听
     * @param uploadCallback
     */
    public void setOnProgressListener(UploadCallback uploadCallback){
        this.uploadCallback = uploadCallback;
    }

    public interface UploadCallback{
        //上传前
        public void preUpload(String imgPath);
        //上传进度
        public void progress(double percent);
        //上传结果
        public void uploadResultCallback(String videoName, String imgName, boolean uploadResult);
    }
}
