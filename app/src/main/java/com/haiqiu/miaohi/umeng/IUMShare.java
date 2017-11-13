package com.haiqiu.miaohi.umeng;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by ningl on 2016/8/11.
 */
public interface IUMShare {

    /**
     * 获取分享描述
     * @return
     */
    public String getDescribe(SHARE_MEDIA platform);

    /**
     * 获取分享标题
     * @return
     */
    public String getTitle(SHARE_MEDIA platform);

    /**
     * 是否有删除功能
     * @return
     */
    public boolean isHasDetete();

    /**
     * 是否有举报功能
     * @return
     */
    public boolean isHasReport();

    /**
     * 是否有复制链接功能
     * @return
     */
    public boolean isHasCopyLink();

    /**
     * 是否有取消按钮
     * @return
     */
    public boolean isHasCancleBtn();

    /**
     * 是否是自己
     * @return
     */
    public boolean isMySelf();

    /**
     * 获取分享链接
     * @return
     */
    public String getShareLink();

}
