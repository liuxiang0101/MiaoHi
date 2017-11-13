package com.haiqiu.miaohi.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haiqiu.miaohi.bean.VideoUploadInfo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhandalin on 2017-01-04 13:14.
 * 说明:草稿箱, 以前是直接存对象,如果数据类型变了,就废了, 用json存通用性好一点
 */
public class DraftUtil {
    private final static String TAG = "DraftUtil";
    private final static String FILE_POSTFIX = ".mhdraft";

    private static List<VideoUploadInfo> videoUploadList;//数据量不会太大,直接放在内存里
    private static File videoUploadFile;

    /**
     * 程序初始化执行, 登陆成功后执行
     *
     * @param context applicationContext
     */
    public static void init(Context context) {
        String userId = UserInfoUtil.getUserId(context);
        if (!MHStringUtils.isEmpty(userId)) {
            try {
                videoUploadFile = new File(context.getFilesDir().getAbsolutePath() + "/" + userId.replace("-", "") + FILE_POSTFIX);
                if (!videoUploadFile.exists()) videoUploadFile.createNewFile();

                String json = FileUtils.readFile(videoUploadFile.getAbsolutePath());
                videoUploadList = new Gson().fromJson(json, new TypeToken<ArrayList<VideoUploadInfo>>() {
                }.getType());
            } catch (Exception e) {
                MHLogUtil.e(TAG,e);
            }
        }
        if (null == videoUploadList) videoUploadList = new ArrayList<>();
    }

    /**
     * @param videoUploadInfo 上传信息封装类
     * @return true 成功
     */
    public static boolean saveDraft(VideoUploadInfo videoUploadInfo) {
        if (null == videoUploadList || null == videoUploadInfo) {
            MHLogUtil.w(TAG, "videoUploadList ||videoUploadInfo is null");
            return false;
        }
        deleteDraft(videoUploadInfo);//删除重复的
        videoUploadList.add(0, videoUploadInfo);
        return writeToLocal();
    }

    /**
     * 写入本地
     */
    private static boolean writeToLocal() {
        try {
            String toJson = new Gson().toJson(videoUploadList);
            FileUtils.writeFile(videoUploadFile.getAbsolutePath(), toJson);
            return true;
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
            MHLogUtil.e(TAG, e.toString());
        }
        return false;
    }

    /**
     * @return 当前用户的所有草稿箱, 并会去除已经把文件删除了的数据, 映答过期的也会过滤掉
     */
    public static List<VideoUploadInfo> getDraftList() {
        if (null != videoUploadList && videoUploadList.size() > 0) {
            ArrayList<VideoUploadInfo> tempList = new ArrayList<>();
            for (VideoUploadInfo uploadInfo : videoUploadList) {
                if (uploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO) {
                    if (MHStringUtils.isEmpty(uploadInfo.getQuestionId())) {
                        File file = new File(uploadInfo.getVideoSrcPath());
                        if (file.exists()) {
                            tempList.add(uploadInfo);
                        }
                    } else {
                        File file = new File(uploadInfo.getVideoSrcPath());
                        if (file.exists() && uploadInfo.getQuestionEndTime() - TimeFormatUtils.getCurrentTimeMillis_CH() > 0) {
                            tempList.add(uploadInfo);
                        }
                    }
                } else if (uploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE) {
                    File file = new File(uploadInfo.getPictureSrcPath());
                    if (file.exists()) {
                        tempList.add(uploadInfo);
                    }
                }
            }
            videoUploadList = tempList;
            writeToLocal();
        }
        //防止外界增删改查对草稿箱的影响
        ArrayList<VideoUploadInfo> uploadInfoArrayList = new ArrayList<>();
        uploadInfoArrayList.addAll(videoUploadList);
        return uploadInfoArrayList;
    }

    /**
     * 删除
     *
     * @return true 成功
     */
    public static boolean deleteDraft(VideoUploadInfo videoUploadInfo) {
        if (null == videoUploadInfo) {
            MHLogUtil.w(TAG, "null==videoUploadInfo");
            return false;
        }
        List<VideoUploadInfo> temp = new ArrayList<>();
        if (null != videoUploadList && videoUploadList.size() > 0) {
            for (VideoUploadInfo uploadInfo : videoUploadList) {
                if (uploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO) {
                    if (null != uploadInfo.getVideoSrcPath() && uploadInfo.getVideoSrcPath().equals(videoUploadInfo.getVideoSrcPath())) {
                        temp.add(uploadInfo);
//                        break;
                    }
                    //根据questionId删除
                    if (null != uploadInfo.getQuestionId() && uploadInfo.getQuestionId().equals(videoUploadInfo.getQuestionId())) {
                        temp.add(uploadInfo);
//                        break;
                    }
                } else if (uploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE) {
                    if (null != uploadInfo.getPictureSrcPath() && uploadInfo.getPictureSrcPath().equals(videoUploadInfo.getPictureSrcPath())) {
                        temp.add(uploadInfo);
//                        break;
                    }
                }
            }
            videoUploadList.removeAll(temp);
            writeToLocal();
        }
        return false;
    }


    /**
     * @return 获取当前用户草稿箱的数量
     */
    public static int getDraftCount() {
        if (null == videoUploadList) return 0;

        return videoUploadList.size();
    }

    /**
     * @return true 所有用户是否有草稿, 建议子线程调用
     */
    public static boolean hasDraft(Context context) {
        if (getDraftCount() > 0) {//当前用户就有草稿
            MHLogUtil.d(TAG, "当前用户草稿箱有东西");
            return true;
        } else {//判断其他用户是否有草稿
            File filesDir = context.getFilesDir();
            File[] files = filesDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return null != filename && filename.endsWith(FILE_POSTFIX);
                }
            });
            int count = 0;
            if (null != files && files.length > 0) {
                for (File file : files) {
                    try {
                        String json = FileUtils.readFile(file.getAbsolutePath());
                        List<VideoUploadInfo> draftList = new Gson().fromJson(json, new TypeToken<ArrayList<VideoUploadInfo>>() {
                        }.getType());
                        count += draftList.size();
                        if (count > 0) break;
                    } catch (Exception e) {
                        MHLogUtil.e(TAG,e);
                    }
                }
            }
            if (count > 0) {
                MHLogUtil.d(TAG, "其它用户草稿箱还有东西--count=" + count);
            } else {
                MHLogUtil.d(TAG, "其它用户草稿箱没有东西");
            }
            return count > 0;
        }
    }

    /**
     * @return 返回映答 map
     */
    public static HashMap<String, VideoUploadInfo> getDraftQuestionMap() {
        HashMap<String, VideoUploadInfo> hashMap = new HashMap<>();
        if (null == videoUploadList) return hashMap;

        for (VideoUploadInfo uploadInfo : videoUploadList) {
            if (null == uploadInfo) continue;
            if (uploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO
                    && !MHStringUtils.isEmpty(uploadInfo.getQuestionId())) {
                hashMap.put(uploadInfo.getQuestionId(), uploadInfo);
            }
        }
        return hashMap;
    }

    /**
     * @return true 存在
     */
    public static boolean isExitInDraft(VideoUploadInfo uploadInfo) {
        if (uploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO) {
            return new File(uploadInfo.getVideoSrcPath()).exists();
        } else if (uploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE) {
            return new File(uploadInfo.getPictureSrcPath()).exists();
        }
        return false;
    }
}
