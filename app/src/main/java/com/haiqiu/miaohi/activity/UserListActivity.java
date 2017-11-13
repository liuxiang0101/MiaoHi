package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.fragment.CommonUserFragment;
import com.haiqiu.miaohi.fragment.UserListFragment;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.view.NoScrollViewPager;
import com.haiqiu.miaohi.widget.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhandalin on 2016-12-22 18:23.
 * 说明:自己相关的用户列表,要展示别人的请用CommonUserListActivity,如果不传type 默认是展示的是关注列表
 * 如果不传 user_id 默认是展示自己的, 展示别人的展示不用这个,展示别人的没有A_Z逻辑
 * <p>
 * 这个页面还包含了@好友的逻辑
 */
public class UserListActivity extends BaseActivity {
    public static final int USER_LIST_TYPE_FANS = 23;
    public static final int USER_LIST_TYPE_ATTENTION = 24;
    public static final int USER_LIST_TYPE_AT_FRIENDS = 25;

    public static final int AT_FRIENDS_REQUEST_CODE = 102;


    //存储所有选择的用户Id与名字
    public static HashMap<String, String> nameUserIdHashMap = new HashMap<>();

    private static final String[] title = new String[]{"默认", "A-Z"};
    private int type;
    private String user_id;
    private SlidingTabLayout tab_layout;
    private NoScrollViewPager view_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_fans);
        nameUserIdHashMap.clear();
        Intent intent = getIntent();
        type = intent.getIntExtra("type", USER_LIST_TYPE_ATTENTION);
        user_id = intent.getStringExtra("user_id");
        initView();
    }

    private void initView() {
        CommonNavigation navigation = (CommonNavigation) findViewById(R.id.navigation);
        ImageView imageView = new ImageView(context);
        imageView.setPadding(0, 0, DensityUtil.dip2px(context, 12), 0);
        imageView.setImageResource(R.drawable.search_right_black);
        navigation.setRightLayoutView(imageView);
        navigation.setOnRightLayoutClickListener(new CommonNavigation.OnRightLayoutClick() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, FindFriendActivity.class).putExtra("type", type));
            }
        });
        navigation.setOnLeftLayoutClickListener(new CommonNavigation.OnLeftLayoutClick() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        if (type == USER_LIST_TYPE_ATTENTION) {
            navigation.setTitle("关注");
        } else if (type == USER_LIST_TYPE_FANS) {
            navigation.setTitle("粉丝");
        } else if (type == USER_LIST_TYPE_AT_FRIENDS) {
            View tv_conform = findViewById(R.id.tv_conform);
            tv_conform.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back();
                }
            });
            tv_conform.setVisibility(View.VISIBLE);
            navigation.setTitle("选择好友");
        }

        tab_layout = (SlidingTabLayout) findViewById(R.id.tab_layout);
        tab_layout.setVisibility(View.GONE);
        view_pager = (NoScrollViewPager) findViewById(R.id.view_pager);

        view_pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                BaseFragment baseFragment = null;
                switch (position) {
                    case 0:
                        CommonUserFragment userFragment = new CommonUserFragment();
                        userFragment.setInitParam(type, user_id);
                        baseFragment = userFragment;
                        break;
                    case 1:
                        UserListFragment userListFragment = new UserListFragment();
                        String tableName = ConstantsValue.DB.TABLE_NAME_ATTENTION;
                        if (USER_LIST_TYPE_FANS == type) {
                            tableName = ConstantsValue.DB.TABLE_NAME_FANS;
                        }
                        userListFragment.setInitParam(tableName, type);
                        baseFragment = userListFragment;
                        break;

                }
                return baseFragment;
            }

            @Override
            public int getCount() {
                return title.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }
        });
        tab_layout.setViewPager(view_pager);
        view_pager.setNoScroll(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nameUserIdHashMap.clear();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void back() {
        ArrayList<String> userIdList = new ArrayList<>();
        ArrayList<String> nameList = new ArrayList<>();

        for (Map.Entry<String, String> nameAndId : nameUserIdHashMap.entrySet()) {
            userIdList.add(nameAndId.getKey());
            nameList.add("@" + nameAndId.getValue() + " ");
        }

        Intent intent = new Intent();
        intent.putStringArrayListExtra("userIds", userIdList);
        intent.putStringArrayListExtra("nameList", nameList);

        setResult(AT_FRIENDS_REQUEST_CODE, intent);
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    public void setTabVisibility(int visibility) {
        tab_layout.setVisibility(visibility);
        view_pager.setNoScroll(View.VISIBLE != visibility);
    }
}
