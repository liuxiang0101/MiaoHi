package com.haiqiu.miaohi.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.umeng.AbsUMShare;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.widget.ShareLayout;

/**
 * 分享dialog
 * Created by ningl on 2016/6/25.
 */
public class ShareDialog extends Dialog implements ShareLayout.ShareRsultListener
        , ShareLayout.DeleteVideoListener, ShareLayout.CloseDialog {

    private Activity activity;
    public ShareLayout shareLayout;
    private String title;
    private String url;
    private String drawable;
    private ShareLayout.DeleteVideoListener deleteVideoListener;
    private int shareLable;

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
        shareLayout.setImgPath(imgPath);
    }

    private String imgPath;

    public static final int IMGANDTEXT = 0;
    public static final int IMG = 1;

    public ShareDialog(BaseActivity activity) {
        super(activity, R.style.MiaoHiDialog);
        this.activity = activity;
        shareLayout = new ShareLayout(activity);
        shareLayout.setActivity(activity);
        setContentView(shareLayout);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = ScreenUtils.getScreenSize(activity).x;
        win.setAttributes(lp);
        win.setGravity(Gravity.BOTTOM);
        win.setWindowAnimations(R.style.BottomInAnim);
        shareLayout.setOnShareRsultListener(this);
        shareLayout.setDeleteListener(this);
        shareLayout.setCloseDialog(this);
        show();
        shareLayout.setShareDialog(this);
    }

    /**
     * 填充分享平台数据
     */
    public void setData() {
        shareLayout.setData();
    }

    /**
     * 设置分享内容
     */
    public void setShareInfo(AbsUMShare umShare) {
        shareLayout.setShareInfo(umShare);
    }

    /**
     * 删除视频监听
     */
    public void setOnDeleteListener(ShareLayout.DeleteVideoListener deleteVideoListener) {
        this.deleteVideoListener = deleteVideoListener;
    }

    public void hidenReport() {
        shareLayout.hidenReport();
    }

    public void hidenDelete() {
        shareLayout.hidenDeleteBtn();
    }

    public void setShareLable(int lable) {
        this.shareLable = lable;
        shareLayout.setShareLable(lable);
    }

    /**
     * 设置链接
     */
    public void setShareLink(String shareLink) {
        shareLayout.setLink(shareLink);
    }

    public void setOnShareImgPath(ShareLayout.OnShareImgPath onShareImgPath) {
        shareLayout.setOnShareImgPath(onShareImgPath);
    }

    /**
     * 设置删除或者举报监听
     *
     * @param onDeleteOrReportListener
     */
    public void setDeleteOrReportListener(ShareLayout.OnDeleteOrReportListener onDeleteOrReportListener) {
        shareLayout.setDeleteOrReportListener(onDeleteOrReportListener);
    }

    public void setRootType(int rootType) {
        shareLayout.setReportType(rootType);
    }

    @Override
    public void success() {
        dismiss();
    }

    @Override
    public void fail() {
        dismiss();
    }

    @Override
    public void reportSuccess() {
        dismiss();
    }

    @Override
    public void cancle() {
        dismiss();
    }

    @Override
    public void reportFail() {
        dismiss();
    }

    @Override
    public void deleteVideoSuccess() {
        deleteVideoListener.deleteVideoSuccess();
    }

    @Override
    public void deleteVideoFail() {
        deleteVideoListener.deleteVideoFail();
    }

    @Override
    public void close() {
        dismiss();
    }
}
