package com.haiqiu.miaohi.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.widget.MyWebView;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by zhandalin on 2016-07-05 10:33.
 * 说明:浏览器页面(该界面不可以处理有输入框的界面)
 * 此界面导航栏在顶部，使用时请区分使用
 */
public class WebActivity extends BaseActivity {
    private String uri;
    private MyWebView myWebView;
    private boolean happenError;
    private CommonNavigation navigation;
    private LinearLayout ll_root_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_actiivty);
        navigation = (CommonNavigation) findViewById(R.id.navigation);
        Intent intent = getIntent();
        uri = intent.getStringExtra("uri");
        setResult(1);
        navigation.setOnLeftLayoutClickListener(new CommonNavigation.OnLeftLayoutClick() {
            @Override
            public void onClick(View v) {
                if (myWebView.canGoBack()) {
                    myWebView.goBack(); // goBack()表示返回WebView的上一页面
                    navigation.setTitle("秒嗨认证");
                } else {
                    finish();
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                }
            }
        });
        navigation.setTitle(intent.getStringExtra("title"));
        ll_root_view = (LinearLayout) findViewById(R.id.ll_root_view);
        myWebView = new MyWebView(getApplicationContext());
        ll_root_view.addView(myWebView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initData();
    }

    private void initData() {
        happenError = false;
        showMHLoading();
        myWebView.loadUrl(uri);
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                happenError = true;
                myWebView.setVisibility(View.INVISIBLE);
                showErrorView();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hiddenLoadingView();
                if (!happenError) {
                    myWebView.setVisibility(View.VISIBLE);
                    hideErrorView();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (MHStringUtils.isEmpty(url)) return true;
                if ("http://res.release.miaohi.com/main/index.html".equals(url)) {
                    startActivity(new Intent(WebActivity.this, OpenQaActivity.class));
                    finish();
                    return true;
                }
                switch (url) {
                    case "http://weixin.miaohi.com/ident/indent.html":
                        navigation.setTitle("秒嗨认证");
                        break;
                    case "http://weixin.miaohi.com/ident/person.html":
                        navigation.setTitle("个人认证");
                        break;
                    case "http://weixin.miaohi.com/ident/org.html":
                        navigation.setTitle("机构认证");
                        break;
                }
                if (url != null) {
                    if (url.startsWith("miaohi://action")) {
                        Uri uri = Uri.parse(url);
                        skipPageByUri(uri);
                        return true;
                    }
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

    }

    @Override
    protected void reTry() {
        super.reTry();
        initData();
    }

    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack(); // goBack()表示返回WebView的上一页面
            navigation.setTitle("秒嗨认证");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击链接跳转到相关页面
     */
    private void skipPageByUri(Uri uri) {
        if (uri != null) {
            String type = uri.getQueryParameter("type");
            final String id = uri.getQueryParameter("id");
            int mType = 0;
            try {
                mType = Integer.parseInt(type);
            } catch (Exception e) {
                MHLogUtil.e(TAG,e);
            }
            if (id == null) return;
            final Intent mIntent = new Intent();
            switch (mType) {
                case 1://视频详情
                    new android.os.Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            mIntent.setClass(context, VideoDetailActivity.class)
                                    .putExtra("video_id", id);
                            context.startActivity(mIntent);
                        }
                    });
                    break;
                case 2: //应答详情
                    mIntent.setClass(context, InterlocutionDetailsActivity.class)
                            .putExtra("question_id", id);
                    context.startActivity(mIntent);
                    break;
                case 3: //专题详情
                    mIntent.setClass(context, ActiveDetailActivity.class)
                            .putExtra("activityId", id);
                    context.startActivity(mIntent);
                    break;
                case 4: //个人中心
                    if (!isLogin(false)) {
                    } else {
                        skipPersonalCenter(context, id);
                    }
                    break;
            }

        }
    }

    @Override
    protected void onPause() {
        myWebView.reload();

        super.onPause();
    }

    /**
     * 跳转到个人中心
     *
     * @param context
     * @param userId
     */
    private void skipPersonalCenter(Context context, String userId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ll_root_view.removeAllViews();
    }
}
