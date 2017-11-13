package com.haiqiu.miaohi.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.fragment.MineFragment;
import com.haiqiu.miaohi.utils.AnimateFirstDisplayListener;
import com.haiqiu.miaohi.view.picturezoom.OnShowPicture;
import com.haiqiu.miaohi.view.picturezoom.PhotoView;
import com.haiqiu.miaohi.view.picturezoom.PhotoViewAttacher;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.UMShareAPI;

/**
 * 个人主页
 * Created by ningl on 16/12/7.
 */
public class PersonalHomeActivity extends BaseActivity implements OnShowPicture {

    private String userId;
    private FrameLayout fl_showpicturemine;
    private PhotoView pv_mine;
    UMShareAPI mShareAPI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalhome);
        initView();
        mShareAPI = UMShareAPI.get(context);
        userId = getIntent().getExtras().getString("userId");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MineFragment mineFragment = new MineFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        mineFragment.setArguments(bundle);
        ft.replace(R.id.fl_personalhomecotent, mineFragment);
        ft.commit();
    }

    public void initView(){
        fl_showpicturemine = (FrameLayout) findViewById(R.id.fl_showpicturemine);
        pv_mine = (PhotoView) findViewById(R.id.pv_mine);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            mShareAPI.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onShowPicture(String url) {
        fl_showpicturemine.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(fl_showpicturemine, "alpha", 0 , 1f)
                .setDuration(400)
                .start();
        ImageLoader.getInstance().displayImage(url, pv_mine, new AnimateFirstDisplayListener(pv_mine));
        pv_mine.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                fl_showpicturemine.setVisibility(View.GONE);
            }

            @Override
            public void onOutsidePhotoTap() {
                fl_showpicturemine.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(fl_showpicturemine.getVisibility() == View.VISIBLE){
                fl_showpicturemine.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
