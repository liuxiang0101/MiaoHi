package com.haiqiu.miaohi.utils.shareImg;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.haiqiu.miaohi.utils.QRCode;
import com.haiqiu.miaohi.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static com.haiqiu.miaohi.utils.shareImg.ViewToImageUtil.getSimpleViewToBitmap;

/**
 * 分享图片抽象类
 * Created by ningl on 16/12/10.
 */
public abstract class AbsShareImg extends LinearLayout {

    protected int loadCount;//整体检查次数
    protected int loadChildCount;//内容检查次数
    protected static final int UNFINISH = -1;//未完成
    protected static final int FAIL = 0;//加载失败
    protected static final int SUCCESS = 1;//加载成功

    protected Vector<ShareImgInfo> shareImgInfos;

    protected Vector<Boolean> loadRecord;
    protected int size;
    public OnLoadFinish onLoadFinish;


    public AbsShareImg(Context context) {
        this(context, null);
    }

    public AbsShareImg(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsShareImg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    /**
//     * 重置
//     */
//    protected void reset() {
//        if (shareImgInfos == null) shareImgInfos = new Vector<>();
//        for (int i = 0; i < shareImgInfos.size(); i++) {
//            shareImgInfos.get(i).setUploadState(ShareImgInfo.UNLOAD);
//        }
//        loadChildCount = 0;
//    }

    /**
     * 初始化
     */
    protected void init(int count) {
//        if (shareImgInfos == null) shareImgInfos = new Vector<>();
//        for (int i = 0; i < count; i++) {
//            shareImgInfos.add(new ShareImgInfo());
//        }
        loadRecord = new Vector<>();
        size = count;
    }

    /**
     * 是否所有的图片都加载成功
     *
     * @return
     */
    protected boolean checkIsAllLoadSuccess() {
//        loadChildCount += 1;
//        if(loadChildCount != count) return UNFINISH;
//            for (int i = 0; i < shareImgInfos.size(); i++) {
//                int uploadState = shareImgInfos.get(i).getUploadState();
//                if (uploadState == ShareImgInfo.LOADFAIL) {
//                    loadCount += 1;
//                    return FAIL;
//                }
//            }
//        loadCount += 1;
        return loadRecord.size() == size;
    }

    /**
     * 获取二维码位图
     * @param qrCode_str
     * @return
     */
    public Bitmap getQRBitmap(String qrCode_str){
        return  QRCode.createQRCode(qrCode_str);
    }

    /**
     * 生成最后的bitmap并存储图片到sdcard
     */
    public void genderPath(View topView, View bottomView, ViewToImageUtil.OnImageSavedCallback onImageSavedCallback){
        //生成顶部view的bitmap
        ViewToImageUtil.BitmapWithHeight top_bitmapWithHeight = getSimpleViewToBitmap(topView, ScreenUtils.getScreenWidth(getContext()));
        //生成底部view的bitmap
        ViewToImageUtil.BitmapWithHeight bottom_bitmapWithHeight = ViewToImageUtil.getSimpleViewToBitmap(bottomView, ScreenUtils.getScreenWidth(getContext()));
        int height = top_bitmapWithHeight.height + bottom_bitmapWithHeight.height;
        List<ViewToImageUtil.BitmapWithHeight> bitmapWithHeightList = new ArrayList<>();
        bitmapWithHeightList.add(top_bitmapWithHeight);
        bitmapWithHeightList.add(bottom_bitmapWithHeight);
        Bitmap resultBitmap = ViewToImageUtil.generateBigBitmap(bitmapWithHeightList, ScreenUtils.getScreenWidth(getContext()), height);
        top_bitmapWithHeight.bitmap.recycle();
        bottom_bitmapWithHeight.bitmap.recycle();
        ViewToImageUtil.saveDisk(getContext(), resultBitmap, onImageSavedCallback);
    }

    /**
     * 加载完成
     */
    public interface OnLoadFinish{
        void onFinish(Object path);
    }

    public void setOnLoadFinishListener(OnLoadFinish onLoadFinish){
        this.onLoadFinish = onLoadFinish;
    }


}
