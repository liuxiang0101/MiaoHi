package com.haiqiu.miaohi.umeng;

import android.app.Activity;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by ningl on 2016/8/11.
 */
public abstract class AbsUMShare implements IUMShare {

    public Activity activity;
    public String shareId;
    public String imgUrl;
    public String shareUserName;
    public String userId;
    public String describe;
    public String params;
    public int deleteBtnType;

    public AbsUMShare(Activity activity, String shareId, String imgUrl, String shareUserName, String userId, String describe) {
        this.activity = activity;
        this.shareId = shareId;
        this.imgUrl = imgUrl;
        this.shareUserName = shareUserName;
        this.userId = userId;
        this.describe = describe;
        if (MHStringUtils.isEmpty(shareId)) this.shareId = "";
        if (MHStringUtils.isEmpty(describe)) this.describe = "";
        params = shareId + "&version=" + CommonUtil.getVersionName(activity);

//        if(activity == null
//                || MHStringUtils.isEmpty(shareId)
//                ||MHStringUtils.isEmpty(imgUrl)
//                ||MHStringUtils.isEmpty(shareUserName)
//                ||MHStringUtils.isEmpty(userId)
//                ||MHStringUtils.isEmpty(describe)){
//            throw new IllegalStateException("params is null or \"\"");
//        }
    }

    @Override
    public String getDescribe(SHARE_MEDIA platform) {
        return "";
    }

    @Override
    public String getTitle(SHARE_MEDIA platform) {
        return "";
    }

    @Override
    public boolean isHasDetete() {
        return false;
    }

    @Override
    public boolean isHasReport() {
        return true;
    }

    @Override
    public boolean isHasCopyLink() {
        return true;
    }

    @Override
    public boolean isHasCancleBtn() {
        return true;
    }

    @Override
    public boolean isMySelf() {
        return false;
    }

    public abstract String getHost();

    public int getDeleteBtnType() {
        return deleteBtnType;
    }

    public void setDeleteBtnType(int deleteBtnType) {
        this.deleteBtnType = deleteBtnType;
    }

    /**
     * 获取分享链接
     *
     * @return
     */
    public String getShareLink() {
        if (MHStringUtils.isEmpty(getHost())) {
            MHLogUtil.e("分享", "Host为空");
            return "";
        }
        if (getHost().contains(ConstantsValue.Shared.SHARED_SUBJECTDETAIL_BASEURL))
            return getHost() + params;
        else
            return getHost();
    }

    /**
     * 统计复制链接
     */
    public void setCopyLinkStatistics() {

    }
}
