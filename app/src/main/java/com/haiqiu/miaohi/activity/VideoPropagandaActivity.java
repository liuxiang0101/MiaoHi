package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.fragment.LoginFragment;
import com.haiqiu.miaohi.fragment.VideoPropagandaFragment;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.UMShareAPI;

/**
 * 用户未登录时展示界面
 */
public class VideoPropagandaActivity extends CommonLRActivity {
    private final String TAG = getClass().getSimpleName();
    private int videoTime;
    private boolean isFirstComplete = true;
    private View view_cover;
    private TextView bt_next;
    private VideoView videoView;
    private LinearLayout linearlayout;
    private LoginFragment loginFragment;
    private VideoPropagandaFragment propagandaFragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private UMShareAPI umShareAPI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_propaganda);
        umShareAPI = UMShareAPI.get(context);
        view_cover = findViewById(R.id.view_cover);
        propagandaFragment = new VideoPropagandaFragment();
        loginFragment = new LoginFragment();
        initStatusBar();
        initVideoView();
        if (fragmentManager == null) fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.linearlayout, propagandaFragment);
        fragmentTransaction.add(R.id.linearlayout, loginFragment);
        fragmentTransaction.commit();
        changePtopagandFragment();
    }

    /**
     * 切换至GO页面
     */
    public void changePtopagandFragment() {
        view_cover.setBackgroundColor(getResources().getColor(R.color.transparent));
        if (fragmentManager == null)
            fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.inputodown, R.anim.outdowntoup);
        fragmentTransaction.hide(loginFragment);
        fragmentTransaction.show(propagandaFragment);
        fragmentTransaction.commit();
        currentFragment = propagandaFragment;
        TCAgent.onEvent(context, "GO" + ConstantsValue.android);
    }

    /**
     * 切换至登录页面
     */
    public void changeLoginFragment() {
        isFirstComplete = false;
        view_cover.setBackgroundColor(getResources().getColor(R.color.black_opacity_70));
        if (fragmentManager == null)
            fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.inputodown, R.anim.outdowntoup);
        fragmentTransaction.hide(propagandaFragment);
        fragmentTransaction.show(loginFragment);
        fragmentTransaction.commit();
        currentFragment = loginFragment;
        TCAgent.onEvent(context, "登录页" + ConstantsValue.android);
    }

    @Override
    protected void onResume() {
        videoView.start();
        videoView.seekTo(videoTime);
        super.onResume();
    }

    @Override
    protected void onPause() {
        videoTime = videoView.getCurrentPosition();
        super.onPause();
    }

    private void initStatusBar() {
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = ScreenUtils.getStatusBarHeight(this);
        linearlayout.setLayoutParams(layoutParams);
    }

    private void initVideoView() {
        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse("android.resource://com.haiqiu.miaohi/" + R.raw.miaohi_muxed));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0, 0);
                mp.start();
                videoView.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.seekTo(0);
                videoView.start();
                if (isFirstComplete) {
                    changeLoginFragment();
                }
            }
        });
    }

//    /**
//     * 进入登录页
//     */
//    private void inComeNext() {
//        Intent intent = new Intent(VideoPropagandaActivity.this, LoginActivity.class);
//        intent.putExtra("isFromVideoPropaganda", true);
//        startActivityNoAnimation(intent);
//        overridePendingTransition(R.anim.slide_bottom_out, 0);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MHLogUtil.e(TAG, "resultCode------" + resultCode);
        try {
            if (data != null) umShareAPI.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
        if (resultCode == 200) {
            Intent intent = new Intent(VideoPropagandaActivity.this, LRPerfectInformationActivity.class);
            intent.putExtra("undefine", "undefine");
            startActivity(intent);
        }
        if (resultCode == 50) {

        }
        if (resultCode == 30 || resultCode == 0) {
            hiddenLoadingView();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (currentFragment == loginFragment) {
                changePtopagandFragment();
                return true;
            }
            if (getIntent().getExtras() != null && getIntent().getExtras().getInt("loginType") == 0) {
                finish();
            } else {
                setResult(MainActivity.MAIN);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        onHideSoftInput(event);
        return super.onTouchEvent(event);
    }
}