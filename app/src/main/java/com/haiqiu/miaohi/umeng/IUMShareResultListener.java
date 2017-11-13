package com.haiqiu.miaohi.umeng;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.base.BaseActivity;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * umeng分享监听
 * Created by ningl on 16/12/26.
 */

public class IUMShareResultListener implements UMShareListener {

    public BaseActivity context;

    public IUMShareResultListener(BaseActivity context) {
        this.context = context;
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        context.showToastAtCenter("分享成功");
        context.hiddenLoadingView();
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        context.showToastAtCenter("分享失败");
        TCAgent.onEvent(context, "分享失败" + ConstantsValue.android);
        context.hiddenLoadingView();
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        context.showToastAtCenter("分享取消");
        context.hiddenLoadingView();
    }
}
