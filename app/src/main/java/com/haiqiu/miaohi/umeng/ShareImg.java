package com.haiqiu.miaohi.umeng;

import android.app.Activity;

/**
 * Created by ningl on 16/12/29.
 */

public class ShareImg extends AbsUMShare {

    private boolean isShowDelete;

    public ShareImg(Activity activity, String shareId, String imgUrl, String shareUserName, String userId, String describe) {
        super(activity, shareId, imgUrl, shareUserName, userId, describe);
    }

    @Override
    public boolean isHasDetete() {
        return isShowDelete;
    }

    public boolean isShowDelete() {
        return isShowDelete;
    }

    public void setShowDelete(boolean showDelete) {
        isShowDelete = showDelete;
    }

    @Override
    public String getHost() {
        return null;
    }

}
