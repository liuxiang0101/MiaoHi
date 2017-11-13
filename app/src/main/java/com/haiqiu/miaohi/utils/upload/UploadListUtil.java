package com.haiqiu.miaohi.utils.upload;


import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.bean.VideoUploadInfo;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.DraftsUploadResponse;
import com.haiqiu.miaohi.response.UploadPictureResponse;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.tendcloud.tenddata.TCAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 上传的list
 * Created by hackest on 2016/9/5.
 */
public class UploadListUtil {

    private volatile static UploadListUtil instance;

    public List<String> mImagePathes = new ArrayList<>(); //上传的图片集合

    public List<String> mVideoPathes = new ArrayList<>(); //上传的视频路径集合

    private List<VideoUploadInfo> queue;
    private OnUpLoad onUpLoad;

    public interface OnUpLoad {
        /**
         * 开始上传
         */
        void preUpload(VideoUploadInfo task);

        void progress(double percent, VideoUploadInfo task);

        void uploadSuccess(String videoName, String imgName, VideoUploadInfo task);

        void uploadFaile(VideoUploadInfo task);

        void isEmpty();
    }

    public void setOnUpladListener(OnUpLoad onUpLoad) {
        this.onUpLoad = onUpLoad;
    }

    public static UploadListUtil getInstance() {
        if (instance == null) {
            synchronized (UploadListUtil.class) {
                if (instance == null) {
                    instance = new UploadListUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 向队列中添加任务
     *
     * @param task
     */
    public void addTask(Context context, VideoUploadInfo task) {
        if (null == queue) {
            queue = new Vector<>();
        }
        if (null == task)
            return;
        queue.add(task);
        /**
         * 如果队列中任务数量=1个则认为在添加之前队列中无任务 立即上传
         * 若队列中任务>1则认为队列中有任务正在上传 当前任务排在队尾等待
         */
        if (queue.size() == 1) {
            if (task.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO) {
                //视频和封面
                uploadVideo(context, task);
            } else if (task.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE) {
                //图片
                uploadPicture(context, task);
            }

        }
    }

    /**
     * 上传图片
     *
     * @param context
     * @param task
     */
    private void uploadPicture(Context context, VideoUploadInfo task) {
        final UploadPictureUtil uploadPictureUtil = new UploadPictureUtil(task.getPicturePath(), context);
        //获取token
        GetToken.getToken(context, new GetToken.IGetToken() {
            @Override
            public void getResult(boolean isOk) {
                if(isOk){
                    //开始上传图片到七牛
                    uploadPictureUtil.uploadPicture();
                }
            }
        });
        uploadPictureUtil.setOnProgressListener(new UploadCallback(context, task));
    }

    /**
     * 上传图片成功通知业务后台
     *
     * @param context
     * @param task
     * @param imgName
     */
    private void uploadPictureToServer(final Context context, final VideoUploadInfo task, final String imgName) {
        MHRequestParams params = new MHRequestParams();
        params.addParams("photo_note", Base64Util.getBase64Str(task.getVideoNote().toString().replaceAll("\\\n+", " ")));
        params.addParams("photo_height", task.getPictureHeight() + "");
        params.addParams("photo_width", task.getPictureWidth() + "");
        params.addParams("photo_uri", "http://" + SpUtils.get(ConstantsValue.Sp.QINIU_WEB_IMAGE_BASE, "") + "/" + imgName);
        if (null != task.getNotifyUserId() && task.getNotifyUserId().size() > 0) {
            params.addParams("notify_user", new Gson().toJson(task.getNotifyUserId()));
        }
        MHHttpClient.getInstance().post(UploadPictureResponse.class, ConstantsValue.Url.UPLOADPHOTO, params, new MHHttpHandler<UploadPictureResponse>() {
            @Override
            public void onSuccess(UploadPictureResponse response) {
                TCAgent.onEvent(context,"图片上传成功"+ConstantsValue.android);
                task.setPhotoId(response.getData().getPhoto_id());
                //上传成功后添加图片路径
                task.setPictureUrl("http://" + SpUtils.get(ConstantsValue.Sp.QINIU_WEB_IMAGE_BASE, "") + "/" + imgName);
                task.setShare_link_address(response.getData().getShare_link_address());
                if (1 == task.getUpLoadType()) {
                    //检测草稿箱是否包含这条数据
                    if (null != onUpLoad) onUpLoad.uploadSuccess("", imgName, task);
                    if (!queue.isEmpty()) queue.remove(0);
                    if (!queue.isEmpty()) uploadVideo(context, queue.get(0));
                    else if (null != onUpLoad) onUpLoad.isEmpty();
                } else {
                    if (null != onUpLoad) onUpLoad.uploadSuccess("", imgName, task);
                    queue.remove(0);
                    if (!queue.isEmpty()) {
                        //队列不为空 继续上传
                        if (queue.get(0).getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO) {
                            //上传视频
                            uploadVideo(context, queue.get(0));
                        } else {
                            //上传图片
                            uploadPicture(context, queue.get(0));
                        }
                    } else if (null != onUpLoad) {
                        onUpLoad.isEmpty();
                    }
                }
            }

            @Override
            public void onFailure(String content) {
                TCAgent.onEvent(context,"图片上传失败"+ConstantsValue.android);
                if (null != onUpLoad)
                    onUpLoad.uploadFaile(task);
                queue.remove(0);
                if (!queue.isEmpty())
                    uploadVideo(context, queue.get(0));
                else if (null != onUpLoad)
                    onUpLoad.isEmpty();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                if (null != onUpLoad)
                    onUpLoad.uploadFaile(task);
                queue.remove(0);
                if (!queue.isEmpty())
                    uploadVideo(context, queue.get(0));
                else if (null != onUpLoad)
                    onUpLoad.isEmpty();
            }

        });
    }

    /**
     * 获取上传等待队列
     *
     * @return
     */
    public List<VideoUploadInfo> getQueue() {
        return queue;
    }

    /**
     * 上传视频和视频封面
     *
     * @param task
     */
    private void uploadVideo(Context context, VideoUploadInfo task) {
        final UploadVideoUtil uploadVideoUtil = new UploadVideoUtil(context, task.getVideoPreviewImagePath(), task.getVideoPath());
        uploadVideoUtil.setDuration(task.getVideoDuration());
        //获取token
        GetToken.getToken(context, new GetToken.IGetToken() {
            @Override
            public void getResult(boolean isOk) {
                //开始上传到七牛
                uploadVideoUtil.uploadFile();
            }
        });
        uploadVideoUtil.setOnProgressListener(new UploadCallback(context, task));
    }

    /**
     * 提交上传信息到服务器
     *
     * @param videoName
     * @param imgName
     */
    private void uploadVideoToServer(final Context context, final VideoUploadInfo videoUploadInfo, final String videoName, final String imgName) {
        MHRequestParams requestParams = new MHRequestParams();
        String url = null;
        if (MHStringUtils.isEmpty(videoUploadInfo.getQuestionId())) {
            requestParams.addParams("video_note", Base64Util.getBase64Str(videoUploadInfo.getVideoNote().toString().replaceAll("\\\n+", " ")));
            try {
                requestParams.addParams("activity_id", videoUploadInfo.getActivity_id());

                url = ConstantsValue.Url.UPLOAD_VIDEO;
                if (null != videoUploadInfo.getNotifyUserId() && videoUploadInfo.getNotifyUserId().size() > 0)
                    requestParams.addParams("notify_user", new Gson().toJson(videoUploadInfo.getNotifyUserId()));

//                if (draftsBean.getInviteUser() != null && draftsBean.getInviteUser().size() > 0)
//                    requestParams.addParams("invite_user", new Gson().toJson(draftsBean.getInviteUser()));
            } catch (Exception e) {
                MHLogUtil.e("UploadListUtil",e);
            }
        } else {//映答录制
            requestParams.addParams("question_id", videoUploadInfo.getQuestionId());
            url = ConstantsValue.Url.UPLOADANSWERVIDEO;
        }

        requestParams.addParams("video_uri", "http://" + SpUtils.get(ConstantsValue.Sp.QINIU_WEB_VIDEO_BASE, "") + "/" + videoName);
        requestParams.addParams("cover_uri", "http://" + SpUtils.get(ConstantsValue.Sp.QINIU_WEB_IMAGE_BASE, "") + "/" + imgName);
        requestParams.addParams("video_width", videoUploadInfo.getVideoWidth() + "");
        requestParams.addParams("video_height", videoUploadInfo.getVideoHeight() + "");

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        //获取视频时长
        int duration_second = (int) (videoUploadInfo.getVideoDuration() / 1000 + 0.5);
        if (duration_second == 0) {
            if (!TextUtils.isEmpty(videoUploadInfo.getVideoPath())) {
                mediaMetadataRetriever.setDataSource(videoUploadInfo.getVideoPath());
            }
            String duration = mediaMetadataRetriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
            duration_second = (int) (Integer.parseInt(duration) / 1000 + 0.5);
        }
        requestParams.addParams("duration_second", duration_second + "");

        MHHttpClient.getInstance().post(DraftsUploadResponse.class, url, requestParams, new MHHttpHandler<DraftsUploadResponse>() {
            @Override
            public void onSuccess(DraftsUploadResponse response) {
                TCAgent.onEvent(context,"视频上传成功"+ConstantsValue.android);
                //分享信息
                videoUploadInfo.setVideoId(response.getData().getVideo_id());
                videoUploadInfo.setUploadUserName(UserInfoUtil.getUserName(context));
                videoUploadInfo.setServiceImageUrl(response.getData().getCover_uri());
                videoUploadInfo.setVideoUrl("http://" + SpUtils.get(ConstantsValue.Sp.QINIU_WEB_VIDEO_BASE, "") + "/" + videoName);
                videoUploadInfo.setCoverUrl("http://" + SpUtils.get(ConstantsValue.Sp.QINIU_WEB_IMAGE_BASE, "") + "/" + imgName);
                videoUploadInfo.setShare_link_address(response.getData().getShare_link_address());
                if (1 == videoUploadInfo.getUpLoadType()) {
//                    draftHandlerUtil.updateQuestionState(context, draftsBean.getQuestionId(), VideoUploadInfo.VIDEO_UPLOAD_SUCCESS);
//                    draftHandlerUtil.setOnSaeToDraftListener(new DraftHandlerUtil.OnSaveToDraft() {
//                        @Override
//                        public void onSaveToDraft(String questionID, int state) {
//                            Intent intent = new Intent(VideoUploadInfo.UPDATE_QUESTION_STATE_ACTION);
//                            intent.putExtra("questionId", questionID);
//                            intent.putExtra("videoState", state);
//                            context.sendBroadcast(intent);
//                        }
//                    });
                    if (null != onUpLoad)
                        onUpLoad.uploadSuccess(videoName, imgName, videoUploadInfo);
                    if (!queue.isEmpty()) queue.remove(0);
                    if (!queue.isEmpty()) uploadVideo(context, queue.get(0));
                    else if (null != onUpLoad) onUpLoad.isEmpty();
                    return;
                } else if (null != videoUploadInfo.getQuestionId()) {
//                    draftHandlerUtil.updateQuestionState(context, draftsBean.getQuestionId(), VideoUploadInfo.VIDEO_UPLOAD_SUCCESS);
//                    draftHandlerUtil.setOnSaeToDraftListener(new DraftHandlerUtil.OnSaveToDraft() {
//                        @Override
//                        public void onSaveToDraft(String questionID, int state) {
//                            Intent intent = new Intent(VideoUploadInfo.UPDATE_QUESTION_STATE_ACTION);
//                            intent.putExtra("questionId", questionID);
//                            intent.putExtra("videoState", state);
//                            context.sendBroadcast(intent);
//                        }
//                    });
                }

                if (null != onUpLoad) onUpLoad.uploadSuccess(videoName, imgName, videoUploadInfo);
                queue.remove(0);
                if (!queue.isEmpty()) {
                    //队列不为空 继续上传
                    if (queue.get(0).getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO) {
                        //上传视频
                        uploadVideo(context, queue.get(0));
                    } else {
                        //上传图片
                        uploadPicture(context, queue.get(0));
                    }
                } else if (null != onUpLoad) {
                    onUpLoad.isEmpty();
                }
            }

            @Override
            public void onFailure(String content) {
                TCAgent.onEvent(context,"视频上传失败"+ConstantsValue.android);
                if (null != onUpLoad)
                    onUpLoad.uploadFaile(videoUploadInfo);
                queue.remove(0);
                if (!queue.isEmpty())
                    uploadVideo(context, queue.get(0));
                else if (null != onUpLoad)
                    onUpLoad.isEmpty();
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                if (null != onUpLoad)
                    onUpLoad.uploadFaile(videoUploadInfo);
                queue.remove(0);
                if (!queue.isEmpty())
                    uploadVideo(context, queue.get(0));
                else if (null != onUpLoad)
                    onUpLoad.isEmpty();
            }
        });

    }


    public class UploadCallback implements UploadVideoUtil.UploadCallback {

        private Context context;
        private VideoUploadInfo task;

        public UploadCallback(Context context, VideoUploadInfo task) {
            this.context = context;
            this.task = task;
        }

        @Override
        public void preUpload(String imgPath) {
            if (null != onUpLoad) {
                task.setLocalCoverPath(imgPath);//添加本地图片地址
                onUpLoad.preUpload(task);
            }
        }

        @Override
        public void progress(double percent) {
            if (null != onUpLoad)
                onUpLoad.progress(percent, task);
        }

        @Override
        public void uploadResultCallback(String videoName, String imgName, boolean uploadResult) {
            if (uploadResult) {//上传七牛成功
                if (task.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO) {
                    //上传视频通知业务后台
                    uploadVideoToServer(context, task, videoName, imgName);
                } else {
                    //上传图片通知业务后台
                    uploadPictureToServer(context, task, imgName);
                }
            } else {//上传七牛失败
                if (!queue.isEmpty() && task == queue.get(0)) {
                    if (null != onUpLoad)
                        onUpLoad.uploadFaile(task);
                    queue.remove(0);
                    if (!queue.isEmpty())
                        uploadVideo(context, queue.get(0));
                    else if (null != onUpLoad)
                        onUpLoad.isEmpty();
                } else {
                    if (null != onUpLoad)
                        onUpLoad.isEmpty();
                }
            }
        }
    }


}
