package com.haiqiu.miaohi.utils;

import com.haiqiu.miaohi.bean.VideoDetailUserCommentBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LiuXiang on 2017/3/2.
 * 用于同步评论数据
 */
public class MHContentSyncUtil {
    //用于存储photo_id/video_id对应的评论集合数据
    private static HashMap<String, List<VideoDetailUserCommentBean>> contentMap;
    //用于存储photo_id/video_id对应的评论总数量(注:因为评论分页逻辑的存在,此处数量不等于评论集合size)
    private static HashMap<String, Integer> contentSizeMap;

    /**
     * 设置同步评论数据
     */
    public static void setContentSync(String id, List<VideoDetailUserCommentBean> content) {
        if (null == content || null == id) return;

        if (null == contentMap)
            contentMap = new HashMap<>();
        List<VideoDetailUserCommentBean> list = new ArrayList<>();
        list.addAll(content);
        contentMap.put(id, list);
    }

    /**
     * 设置对应id对象的评论的总数量
     */
    public static void setContentSize(String id, int size) {
        if (null == id) return;
        if (null == contentSizeMap)
            contentSizeMap = new HashMap<>();
        contentSizeMap.put(id, size);
    }


    /**
     * 获取同步评论数据
     */
    public static List<VideoDetailUserCommentBean> getContentSync(String id) {
        if (null == id || null == contentMap)
            return null;

        if (contentMap.containsKey(id))
            return contentMap.get(id);
        else
            return null;
    }

    /**
     * 获取对应id对象的评论的总数量
     */
    public static int getContentSize(String id) {
        if (null == id || null == contentSizeMap)
            return -1;
        if (contentSizeMap.containsKey(id))
            return contentSizeMap.get(id);
        else
            return -1;
    }

    /**
     * 发表新评论
     */
    public static void addContentSync(String id, VideoDetailUserCommentBean bean) {
        //评论集合中添加一条
        if (null == id || null == bean)
            return;
        if (null == contentMap)
            contentMap = new HashMap<>();
        ArrayList<VideoDetailUserCommentBean> list = new ArrayList<>();
        list.add(bean);
        if (contentMap.containsKey(id))
            list.addAll(contentMap.get(id));
        contentMap.put(id, list);

        //对应id的评论总数量+1
        if (null == contentSizeMap)
            contentSizeMap = new HashMap<>();
        if (contentSizeMap.containsKey(id))
            contentSizeMap.put(id, contentSizeMap.get(id) + 1);
        else
            contentSizeMap.put(id, 1);

    }

    /**
     * 删除评论
     */
    public static void deleteContentSync(String id, String contentId) {
        if (null == id || null == contentId || null == contentMap)
            return;
        List<VideoDetailUserCommentBean> list = contentMap.get(id);
        for (VideoDetailUserCommentBean bean : list) {
            if (contentId.equals(bean.getComment_id()))
                list.remove(bean);
        }
    }

    /**
     * 删除评论时,评论数量减去1
     */
    public static void reduceContentSize(String id) {
        if (null == id || null == contentSizeMap) return;
        if (contentSizeMap.containsKey(id))
            contentSizeMap.put(id, contentSizeMap.get(id) - 1);
    }

    /**
     * 清除各种状态,在登陆成功后调用
     */
    public static void clearContent() {
        //清除评论集合数据
        if (null != contentMap)
            contentMap.clear();
        //清除评论数量数据
        if (null != contentSizeMap)
            contentSizeMap.clear();
    }

}
