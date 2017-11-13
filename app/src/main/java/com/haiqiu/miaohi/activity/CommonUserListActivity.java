package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.fragment.CommonUserFragment;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.view.CommonNavigation;


/**
 * Created by zhandalin on 2016-12-23 10:30.
 * 说明:用来展示普通的用户列表,要展示自己的请用UserListActivity,如果不传type 默认是展示的是关注列表
 */
public class CommonUserListActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_user_list);
        CommonNavigation navigation = (CommonNavigation) findViewById(R.id.navigation);
        navigation.hideRightLayout();
        ImageView imageView = new ImageView(context);
        imageView.setPadding(0, 0, DensityUtil.dip2px(context, 12), 0);
        imageView.setImageResource(R.drawable.search_right_black);
        navigation.setRightLayoutView(imageView);
        navigation.setOnRightLayoutClickListener(new CommonNavigation.OnRightLayoutClick() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, FindFriendActivity.class));
            }
        });
        CommonUserFragment commonUserFragment = (CommonUserFragment) getSupportFragmentManager().findFragmentByTag("CommonUserFragment");
        Intent intent = getIntent();
        int type = intent.getIntExtra("type", UserListActivity.USER_LIST_TYPE_ATTENTION);
        if (type == UserListActivity.USER_LIST_TYPE_ATTENTION) {
            navigation.setTitle("关注");
        } else {
            navigation.setTitle("粉丝");
        }

        String user_id = intent.getStringExtra("user_id");
        commonUserFragment.setInitParam(type, user_id);
    }
}
