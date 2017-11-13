package com.haiqiu.miaohi.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.fragment.QAFragment;
import com.haiqiu.miaohi.widget.tablayout.CommonTabLayout;
import com.haiqiu.miaohi.widget.tablayout.CustomTabEntity;
import com.haiqiu.miaohi.widget.tablayout.OnTabSelectListener;
import com.haiqiu.miaohi.widget.tablayout.TabEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的映答
 * Created by ningl on 16/12/7.
 */
public class MyQaActivity extends BaseActivity implements OnTabSelectListener {

    private CommonTabLayout ctl_myqa;
    private FrameLayout fl_myqacontent;
    private List<CustomTabEntity> tabs;
    private List<QAFragment> fragments;
    private static final int MYANSWER = 0;      //我的回答
    private static final int MYQUESTION = 1;    //我的提问
    private static final int MYCIRCUSEE = 2;    //我的围观
    private int currentIndex;
    private Fragment toFragment;
    private Fragment currentFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ImageView iv_myqaback;
    private Handler handler;
    private boolean isDistroy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myqa);
        initView();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        tabs = new ArrayList<>();
        tabs.add(new TabEntity("我的回答", 0, 0));
        tabs.add(new TabEntity("我的提问", 0, 0));
        tabs.add(new TabEntity("我的围观", 0, 0));
        ctl_myqa.setTabData((ArrayList<CustomTabEntity>) tabs);
        fragments = new ArrayList<>();
        //添加我回答的fragment
        QAFragment myAnserQAFragment = new QAFragment();
        Bundle myAnserBundle = new Bundle();
        myAnserBundle.putInt("qaType", MYANSWER);
        myAnserQAFragment.setArguments(myAnserBundle);
        fragments.add(myAnserQAFragment);
        //添加我问的fragment
        QAFragment myQuestionQAFragment = new QAFragment();
        Bundle myQuestionBundle = new Bundle();
        myQuestionBundle.putInt("qaType", MYQUESTION);
        myQuestionQAFragment.setArguments(myQuestionBundle);
        fragments.add(myQuestionQAFragment);
        //添加围观的fragment
        QAFragment MyCircuseeQAFragment = new QAFragment();
        Bundle myCircuseeBundle = new Bundle();
        myCircuseeBundle.putInt("qaType", MYCIRCUSEE);
        MyCircuseeQAFragment.setArguments(myCircuseeBundle);
        fragments.add(MyCircuseeQAFragment);

        ctl_myqa.setOnTabSelectListener(this);
        currentFragment = fragments.get(0);
        ft.add(R.id.fl_myqacontent, currentFragment).commit();
        handlerCountDown();
    }

    private void initView() {
        ctl_myqa = (CommonTabLayout) findViewById(R.id.ctl_myqa);
        fl_myqacontent = (FrameLayout) findViewById(R.id.fl_myqacontent);
        iv_myqaback = (ImageView) findViewById(R.id.iv_myqaback);
        iv_myqaback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onTabSelect(int position) {
        switchFragment(position);
        return false;
    }

    @Override
    public void onTabReselect(int position) {

    }

    public void switchFragment(int index) {
        currentFragment = fragments.get(currentIndex);
        currentIndex = index;
        toFragment = fragments.get(index);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (currentFragment != toFragment) {
            if (!toFragment.isAdded()) {    // 先判断是否被add过
                fragmentTransaction.hide(currentFragment).add(R.id.fl_myqacontent, toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                fragmentTransaction.hide(currentFragment).show(toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDistroy = true;
    }

    /**
     * 倒计时
     */
    public void handlerCountDown(){
        if(handler!=null) return;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isDistroy) return;
                handler.postDelayed(this, 1000);
                fragments.get(0).countDown();
                fragments.get(1).countDown();
                fragments.get(2).countDown();
            }
        }, 1000);
    }
}
