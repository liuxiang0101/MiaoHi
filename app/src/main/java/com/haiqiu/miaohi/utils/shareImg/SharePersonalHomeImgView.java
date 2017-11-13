package com.haiqiu.miaohi.utils.shareImg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.UserInfo;
import com.haiqiu.miaohi.bean.UserWork;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.TimeFormatUtils;
import com.haiqiu.miaohi.view.MyCircleView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import static com.haiqiu.miaohi.R.id.rl_sharepersonalhome_outside;
import static com.haiqiu.miaohi.utils.shareImg.ViewToImageUtil.getSimpleViewToBitmap;

/**
 * 分享个人主页view
 * Created by ningl on 16/12/10.
 */
public class SharePersonalHomeImgView extends AbsShareImg {

    private View view;
    private RecyclerView rv;
    private MyCircleView mcv_sharepersonalhome_head;
    private RelativeLayout rl_sharepersonalhome_top;
    private ImageView iv_sharepersonalhome_vip;
    private TextView tv_sharepersonalhome_name;
    private TextView tv_sharepersonalhome_describe;
    private TextView tv_sharepersonalhome_jointime;
    private ImageView iv_sharepersonalhome_qrcode;
    private LinearLayout ll_rl_sharepersonalhome_bottom;
    private LinearLayout ll_sharepersonalhome_empty;
    private TextView tv_qatip;
    private int loadResult;
    private List<UserWork> userWorks;
    private boolean isHeaderLoadFinish;
    private SHARE_MEDIA platform;


    public SharePersonalHomeImgView(Context context) {
        super(context);
    }

    public SharePersonalHomeImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SharePersonalHomeImgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 生成分享图片
     *
     * @param userInfo
     * @param data
     * @param QRCodeStr
     */
    public void genderImg(BaseActivity activity, UserInfo userInfo, List<UserWork> data, String QRCodeStr, SHARE_MEDIA platform) {
        this.platform = platform;
        if(platform == SHARE_MEDIA.QZONE){
            if(onLoadFinish != null) onLoadFinish.onFinish(R.mipmap.ic_launcher);
            return;
        }
        if (data == null
                ||userInfo == null
                ||QRCodeStr == null) {
            activity.hiddenLoadingView();
            return;
        }
        userWorks = new ArrayList<>();
        int size = ScreenUtils.getScreenWidth(getContext()) - 2* DensityUtil.dip2px(getContext(), 15);
        view = View.inflate(getContext(), R.layout.share_personalhome_img, null);
        rl_sharepersonalhome_top = (RelativeLayout) view.findViewById(rl_sharepersonalhome_outside);
        ll_rl_sharepersonalhome_bottom = (LinearLayout) view.findViewById(R.id.ll_rl_sharepersonalhome_bottom);
        rv = (RecyclerView) view.findViewById(R.id.rv_sharepersonalhome_img);
        mcv_sharepersonalhome_head = (MyCircleView) view.findViewById(R.id.mcv_sharepersonalhome_head);
        iv_sharepersonalhome_vip = (ImageView) view.findViewById(R.id.iv_sharepersonalhome_vip);
        iv_sharepersonalhome_qrcode = (ImageView) view.findViewById(R.id.iv_sharepersonalhome_qrcode);
        tv_sharepersonalhome_name = (TextView) view.findViewById(R.id.tv_sharepersonalhome_name);
        tv_sharepersonalhome_describe = (TextView) view.findViewById(R.id.tv_sharepersonalhome_describe);
        tv_sharepersonalhome_jointime = (TextView) view.findViewById(R.id.tv_sharepersonalhome_jointime);
        ll_sharepersonalhome_empty = (LinearLayout) view.findViewById(R.id.ll_sharepersonalhome_empty);
        tv_qatip = (TextView) view.findViewById(R.id.tv_qatip);
//        rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        if(data.size() == 1&&data.get(0).getContent_type() == 0) {
//            rv.setVisibility(GONE);
            ll_sharepersonalhome_empty.setVisibility(VISIBLE);
        } else {
            ll_sharepersonalhome_empty.setVisibility(GONE);
        }
        if(getChildCount()>0) removeAllViews();
        addView(view);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) rl_sharepersonalhome_top.getLayoutParams();
        lp.height = ScreenUtils.getScreenWidth(getContext());
//        if(data.size()>=7){
//        } else {
//            lp.height = size;
//        }
        rl_sharepersonalhome_top.setLayoutParams(lp);
        if(data.size() == 1&&data.get(0).getContent_type() == 0) {
        } else {
            if(data.size()>9){
                for (int i = 0; i < 9; i++) {
                    userWorks.add(data.get(i));
                }
            } else {
                for (int i = 0; i < data.size(); i++) {
                    userWorks.add(data.get(i));
                }
            }
        }
        tv_sharepersonalhome_jointime.setText("于"+ TimeFormatUtils.formatYMD(userInfo.getRegister_time())+"加入秒嗨");
        Bitmap qrBitmap = getQRBitmap(QRCodeStr);
        iv_sharepersonalhome_qrcode.setImageBitmap(qrBitmap);
        if(userWorks.size() == 7){
            UserWork userWork = new UserWork();
            userWork.setContent_type(0);
            userWorks.add(6, userWork);
        }
        if(platform == SHARE_MEDIA.SINA){
            tv_qatip.setText(getResources().getString(R.string.share_sinaqrtip));
        }
        iv_sharepersonalhome_vip.setVisibility(userInfo.getUser_type() > 10 ? VISIBLE : GONE);
        tv_sharepersonalhome_name.setText(userInfo.getUser_name());
        Drawable genderDrawable = null;
        //性别
        if (userInfo.getUser_gender() == 2) {//女
            genderDrawable = getResources().getDrawable(R.drawable.gender_women);
        } else {//男
            genderDrawable = getResources().getDrawable(R.drawable.gender_man);
        }
        genderDrawable.setBounds(0, 0, genderDrawable.getMinimumWidth(), genderDrawable.getMinimumHeight());
        tv_sharepersonalhome_name.setCompoundDrawables(null, null, genderDrawable, null);
        if (MHStringUtils.isEmpty(userInfo.getUser_authentic()))
            tv_sharepersonalhome_describe.setVisibility(GONE);
        tv_sharepersonalhome_describe.setText(userInfo.getUser_authentic());
        GridLayoutManager glm = null;
        RelativeLayout.LayoutParams rv_Lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(userWorks.size() == 1){
            glm = new GridLayoutManager(getContext(), 1);
        } else if(userWorks.size() == 2){
            glm = new GridLayoutManager(getContext(), 2);
        } else if(userWorks.size() == 4){
            glm = new GridLayoutManager(getContext(), 2);
        }else if(userWorks.size()>=3){
            glm = new GridLayoutManager(getContext(), 3);
        }

        if(userWorks.size() == 8&&userWorks.get(6).getType() == 0){
            init(7);
        } else if(userWorks.size()<=9){
            init(userWorks.size());
        } else if(userWorks.size()>9){
            init(9);
        }

        //处理recyclerview的高度
        if(userWorks.size() == 1){
            rv_Lp.height = size;
        } else if(userWorks.size() == 2){
            rv_Lp.height = size/2;
        } else if(userWorks.size() == 3){
            rv_Lp.height = size/3;
        } else if(userWorks.size() == 4){
            rv_Lp.height = size;
        }else if(userWorks.size()>3&&userWorks.size()<=6){
            rv_Lp.height = size/3*2;
        } else if(userWorks.size()>6){
            rv_Lp.height = size;
        }
        rv_Lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        rv.setLayoutParams(rv_Lp);
        rv.setLayoutManager(glm);
        ImageLoader.getInstance().displayImage(userInfo.getPortrait_uri(), mcv_sharepersonalhome_head, DisplayOptionsUtils.getHeaderDefaultImageOptions(), new SimpleImageLoadingListener() {

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                isHeaderLoadFinish = true;
                checkAllFinish();
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                isHeaderLoadFinish = true;
                checkAllFinish();
            }
        });
        if(data.size() == 1&&data.get(0).getContent_type() == 0) {
            ll_sharepersonalhome_empty.setVisibility(VISIBLE);
        } else {
            ll_sharepersonalhome_empty.setVisibility(GONE);
        }
    }

    public void addData(){
        if(userWorks == null||platform == SHARE_MEDIA.QZONE){
            return;
        }
        Adapter adapter = new Adapter(getContext(), userWorks);
        rv.setAdapter(adapter);
//        rv.addItemDecoration(new DividerGridItemDecoration(getContext()));
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private Context context;
        private List<UserWork> userWorks;

        public Adapter(Context context, List<UserWork> userWorks) {
            this.context = context;
            this.userWorks = userWorks;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new ViewHolder(View.inflate(getContext(), R.layout.item_minelist, null));
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_minelist, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            UserWork userWork = userWorks.get(position);

            if(userWork.getContent_type() == 0){//当itemcount=7时position=5时itemview透明度为1
                holder.itemView.setVisibility(INVISIBLE);
                return;
            }
            String uri = null;
            if(userWork.getContent_type() == 1){//视频
                //如果无缩略图则使用视频封面
                if(userWork.getPhoto_thumb_url() == null){
                    uri = userWork.getVideo_cover_uri();
                } else {
                    uri = userWork.getPhoto_thumb_url();
                }
            } else if(userWork.getContent_type() == 2){//图片
                uri = userWork.getPhoto_thumb_url();
            }
            int size = ScreenUtils.getScreenWidth(context) - DensityUtil.dip2px(context, 30);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.iv_minelistimg.getLayoutParams();
            if(userWorks.size() == 1){
                lp.width = size;
                lp.height = size;
            } else if(userWorks.size() == 2) {
                lp.width = size/2 - 1;
                lp.height = size/2;
            } else if(userWorks.size() == 4){
                lp.width = size/2 -1;
                lp.height = size/2;
            } else if(userWorks.size()>=3){
                lp.width = size/3-1;
                lp.height = size/3;
            }
            holder.itemView.setLayoutParams(lp);
            ImageLoader.getInstance().displayImage(uri, holder.iv_minelistimg, DisplayOptionsUtils.getDefaultMinRectImageOptions(),new SimpleImageLoadingListener(){
                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    super.onLoadingFailed(imageUri, view, failReason);
                    loadRecord.add(true);
                    checkAllFinish();
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    loadRecord.add(true);
                    checkAllFinish();
                }
            });
            holder.iv_minelistisvideo.setVisibility(userWork.getContent_type()==1?VISIBLE:GONE);
        }

        @Override
        public int getItemCount() {
            return userWorks==null?0:userWorks.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView iv_minelistimg;
            private ImageView iv_minelistisvideo;
            public ViewHolder(View itemView) {
                super(itemView);
                iv_minelistimg = (ImageView) itemView.findViewById(R.id.iv_minelistimg);
                iv_minelistisvideo = (ImageView) itemView.findViewById(R.id.iv_minelistisvideo);
            }
        }
    }

    /**
     * 检测全部图片加载完成
     */
    private void checkAllFinish(){
        boolean listfinish = loadRecord.size() == size;
        if(listfinish&&isHeaderLoadFinish){
            //生成顶部view的bitmap
            ViewToImageUtil.BitmapWithHeight top_bitmapWithHeight = getSimpleViewToBitmap(rl_sharepersonalhome_top, ScreenUtils.getScreenWidth(getContext()));
            //生成底部view的bitmap
            ViewToImageUtil.BitmapWithHeight bottom_bitmapWithHeight = ViewToImageUtil.getSimpleViewToBitmap(ll_rl_sharepersonalhome_bottom, ScreenUtils.getScreenWidth(getContext()));
            int height = top_bitmapWithHeight.height + bottom_bitmapWithHeight.height;
            List<ViewToImageUtil.BitmapWithHeight> bitmapWithHeightList = new ArrayList<>();
            bitmapWithHeightList.add(top_bitmapWithHeight);
            bitmapWithHeightList.add(bottom_bitmapWithHeight);
            Bitmap resultBitmap = ViewToImageUtil.generateBigBitmap(bitmapWithHeightList, ScreenUtils.getScreenWidth(getContext()), height);
            ViewToImageUtil.saveDisk(getContext(), resultBitmap, new ViewToImageUtil.OnImageSavedCallback() {
                @Override
                public void onFinishCallback(String path) {
                    onLoadFinish.onFinish(path);
                }
            });
        }
//        if(onLoadFinish!=null) onLoadFinish.onFinish();
    }

}
