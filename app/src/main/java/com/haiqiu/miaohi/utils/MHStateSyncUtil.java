package com.haiqiu.miaohi.utils;

import android.content.Context;

import com.haiqiu.miaohi.db.UserInfoManager;

import java.util.HashMap;

/**
 * Created by zhandalin on 2016-11-09 21:51.
 * <p>
 * 说明:全工程视频点赞,关注同步
 */
public class MHStateSyncUtil {

    private static HashMap<String, Boolean> stateMap;

    /**
     * 发布同步事件
     */
    public static void pushSyncEvent(Context context, String id, boolean state) {
        if (null == context || null == id) return;
        UserInfoManager.updateAttentionState(context, id, state);

        if (null == stateMap)
            stateMap = new HashMap<>();
        stateMap.put(id, state);
    }


    /**
     * @param id 可能是图片id,视频id,userId
     * @return ATTENTION_STATE_NOT_FOUND 表示池子里面不存在该数据,请自行处理状态
     */
    public static State getSyncState(String id) {
        if (null == id || null == stateMap)
            return State.ATTENTION_STATE_NOT_FOUND;

        if (stateMap.containsKey(id)) {
            return stateMap.get(id) ? State.ATTENTION_STATE_IS_TRUE : State.ATTENTION_STATE_IS_FALSE;
        }
        return State.ATTENTION_STATE_NOT_FOUND;
    }

    /**
     * 清除各种状态,在登陆成功后调用
     */
    public static void clearState() {
        if (null != stateMap) stateMap.clear();
    }


    public enum State {
        /**
         * 池子里面不存在该数据
         */
        ATTENTION_STATE_NOT_FOUND,

        /**
         * 该条数据全局是true状态
         */
        ATTENTION_STATE_IS_TRUE,

        /**
         * 该条数据全局是false关注
         */
        ATTENTION_STATE_IS_FALSE
    }
}
