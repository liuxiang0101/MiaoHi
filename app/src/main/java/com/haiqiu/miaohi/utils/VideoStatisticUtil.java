package com.haiqiu.miaohi.utils;

import com.google.gson.Gson;
import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.bean.VideoInfo;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhandalin on 2016-06-24 11:44.
 * 说明:视频播放次数统计
 */
public class VideoStatisticUtil {
    private static final String TAG = "VideoStatisticUtil";
    private static List<VideoInfo> videoInfoList = new ArrayList<>();

    /**
     * 加入统计队列
     *
     * @param videoInfo 视频信息
     */
    public static void startPlay(VideoInfo videoInfo) {
        if (null == videoInfo.getVideo_id()) return;

        MHLogUtil.d(TAG, "加入统计队列成功");
        for (VideoInfo temp : videoInfoList) {
            if (null != temp.getVideo_id() && temp.getVideo_id().equals(videoInfo.getVideo_id())) {
                temp.setLocalPlayCount(temp.getLocalPlayCount() + 1);
                return;
            }
        }
        videoInfoList.add(videoInfo);
    }

    /**
     * 上传统计次数
     */
    public static void uploadCount() {
        if (videoInfoList.size() == 0) {
            MHLogUtil.d(TAG, "videoInfoList没有数据");
            return;
        }

        MHRequestParams params = new MHRequestParams();
        params.addParams("video_statistic", new Gson().toJson(videoInfoList));
        final int size = videoInfoList.size();
        MHHttpClient.getInstance().post(BaseResponse.class, ConstantsValue.Url.ADD_VIDEO_PLAYCOUNT, params, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                //在统计的同时又加入了新数据
                if (videoInfoList.size() > size) {
                    MHLogUtil.d(TAG, "在统计的同时又加入了新数据");
                    videoInfoList = videoInfoList.subList(size - 1, videoInfoList.size());
                } else {
                    videoInfoList.clear();
                }
                MHLogUtil.d(TAG, "视频播放次数统计成功");
            }

            @Override
            public void onFailure(String content) {

            }
        });
    }


}
