package com.haiqiu.miaohi.utils.upload;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.haiqiu.miaohi.bean.VideoUploadInfo;
import com.haiqiu.miaohi.receiver.RefreshUploadEvent;
import com.haiqiu.miaohi.utils.DraftUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 上传刷新进度的service
 * Created by hackest on 2016/9/5.
 */
public class UploadService extends Service {

    private static UploadService instance;
    static final int Flag_Upload = 0; // 上传
    static final int Flag_Box = 1; // 草稿箱

    VideoUploadInfo task;


    public static UploadService getInstance() {
        return instance;
    }

    private int flag;

    public int getFlag() {
        return flag;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        MHLogUtil.e("service.........onCreate");
        instance = this;
        super.onCreate();
    }


    @Override
    public void onStart(Intent intent, int startId) {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent && null != intent.getExtras()) {
            task = intent.getExtras().getParcelable("uploadTask");
        }
        final int uploadType;
        if (null != intent && null != intent.getExtras()) {
            uploadType = intent.getExtras().getInt("uploadType");
        } else {
            uploadType = VideoUploadInfo.FROM_COMMON;
        }
        UploadListUtil uploadListUtil = UploadListUtil.getInstance();
        if (task != null) {
            uploadListUtil.addTask(getApplicationContext(), task);
            if(task.getFromInfo() == VideoUploadInfo.FROM_COMMON){
                //如果是上传视频和图片
                //需先通知关注和草稿箱设置成待上传状态
                task.setUploadState(VideoUploadInfo.UPLOAD_PRE);
                task.setProsess(0);
                task.setUpLoadType(uploadType);
                RefreshUploadEvent refreshUploadEvent = new RefreshUploadEvent(task);
                EventBus.getDefault().post(refreshUploadEvent);
            }
        }
        uploadListUtil.setOnUpladListener(new UploadListUtil.OnUpLoad() {
            @Override
            public void preUpload(VideoUploadInfo task) {
                //待上传状态
                switch (task.getFromInfo()){
                    case VideoUploadInfo.FROM_COMMON://一般拍摄流程
                    case VideoUploadInfo.FROM_ASK_AND_ANSWER://映答
                    case VideoUploadInfo.FROM_DRAFTS://草稿箱
                        task.setUploadState(VideoUploadInfo.UPLOAD_PRE);
                        task.setProsess(0);
                        task.setUpLoadType(uploadType);
                        RefreshUploadEvent refreshUploadEvent = new RefreshUploadEvent(task);
                        EventBus.getDefault().post(refreshUploadEvent);
                        break;
                }
            }

            @Override
            public void progress(double percent, VideoUploadInfo task) {
                //正在上传刷新进度
                switch (task.getFromInfo()){
                    case VideoUploadInfo.FROM_COMMON://一般拍摄流程
                    case VideoUploadInfo.FROM_ASK_AND_ANSWER://映答
                    case VideoUploadInfo.FROM_DRAFTS://草稿箱
                        task.setUploadState(VideoUploadInfo.UPLOAD_PROGRESS);

//                        DecimalFormat df = new DecimalFormat("#.###");
//                        double mPercent = Double.parseDouble(df.format(percent));
                        double mPercent = Double.parseDouble(String.format("%.1f", percent*100));
                        task.setProsess(mPercent);
                        task.setUpLoadType(uploadType);
                        RefreshUploadEvent refreshUploadEvent = new RefreshUploadEvent(task);
                        EventBus.getDefault().post(refreshUploadEvent);
                        break;
                }
                MHLogUtil.i("上传进度", percent+"");
                MHLogUtil.i("上传类型", uploadType+"");
            }

            @Override
            public void uploadSuccess(String videoName, String imgName, VideoUploadInfo task) {
                MHLogUtil.i("上传成功", "上传成功");
                //上传成功
                if(task.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE){
                    MHLogUtil.i("上传图片", "上传图片成功");
                }
                DraftUtil.deleteDraft(task);

                switch (task.getFromInfo()){
                    case VideoUploadInfo.FROM_COMMON://一般拍摄流程
                    case VideoUploadInfo.FROM_ASK_AND_ANSWER://映答
                    case VideoUploadInfo.FROM_DRAFTS://草稿箱
                        task.setUploadState(VideoUploadInfo.UPLOAD_SUCCESS);
                        task.setProsess(100);
                        task.setUpLoadType(uploadType);
                        RefreshUploadEvent refreshUploadEvent = new RefreshUploadEvent(task);
                        EventBus.getDefault().post(refreshUploadEvent);
                        break;
                }
            }

            @Override
            public void uploadFaile(VideoUploadInfo task) {
                switch (task.getFromInfo()){
                    case VideoUploadInfo.FROM_COMMON://一般拍摄流程
                    case VideoUploadInfo.FROM_ASK_AND_ANSWER://映答
                    case VideoUploadInfo.FROM_DRAFTS://草稿箱
                        task.setUploadState(VideoUploadInfo.UPLOAD_FAILE);
                        task.setProsess(0);
                        task.setUpLoadType(uploadType);
                        RefreshUploadEvent refreshUploadEvent = new RefreshUploadEvent(task);
                        EventBus.getDefault().post(refreshUploadEvent);
                        break;
                }
            }

            @Override
            public void isEmpty() {

            }
        });


        return super.onStartCommand(intent, flags, startId);
    }

}
