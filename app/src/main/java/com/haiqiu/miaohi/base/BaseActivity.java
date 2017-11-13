package com.haiqiu.miaohi.base;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.fragment.LoginDialogFragment;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.view.CircleLoadingProgressDialog;
import com.haiqiu.miaohi.view.LoadingProgressDialog;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import java.util.List;


/**
 * 不能随便改这个类,要在这个类里面加一行代码都要通知到其它组员!!!!!
 */
public class BaseActivity extends FragmentActivity {
    protected final String TAG = getClass().getSimpleName() + "_TAG";
    protected boolean isDestroyed;
    protected Context context;
    protected CircleLoadingProgressDialog loadingProgress;
    protected LoadingProgressDialog mhLoadingProgress;
    private Handler handler;
    private Toast mToast;
    private Toast rectangleToast;

    private View errorView;//错误View
    private View blankView;//空View

    public boolean isRetring;//防止重试重复点击

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        context = this;
        isDestroyed = false;
//        setStausBarColor();
    }

    protected void reTry() {
        isRetring = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        TCAgent.onPageStart(context, getClass().getSimpleName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            //app 进入后台
            SpUtils.put(ConstantsValue.Sp.IS_APP_ON_FOREGROUND, 1);
        } else {
            SpUtils.put(ConstantsValue.Sp.IS_APP_ON_FOREGROUND, 0);
        }
        TCAgent.onPageEnd(context, getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        if (!isAppOnForeground()) {
            //app 进入后台
            SpUtils.put(ConstantsValue.Sp.IS_APP_ON_FOREGROUND, 1);
        } else {
            SpUtils.put(ConstantsValue.Sp.IS_APP_ON_FOREGROUND, 0);
        }
        MobclickAgent.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        isDestroyed = true;
        if (loadingProgress != null && loadingProgress.isShowing()) {
            loadingProgress.dismiss();
            loadingProgress = null;
        }
        if (mhLoadingProgress != null && mhLoadingProgress.isShowing()) {
            mhLoadingProgress.dismiss();
            mhLoadingProgress = null;
        }
        super.onDestroy();
    }

    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }


    public void showRectangleToast(String message) {
        showRectangleToast(R.drawable.svg_icon_tip, message);
    }

    /**
     * @param iconResourceId 要显示的图片
     * @param message        显示的内容
     */
    public void showRectangleToast(int iconResourceId, String message) {
        if (isDestroyed) {
            return;
        }
        if (null == rectangleToast) {
            rectangleToast = new Toast(context);
            LinearLayout container = new LinearLayout(context);
            container.setGravity(Gravity.CENTER);

            LinearLayout linearLayout = new LinearLayout(context);
            int size = DensityUtil.dip2px(context, 114);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(size, size));
            linearLayout.setBackgroundResource(R.drawable.shape_corner_rectangle);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            ImageView imageView = new ImageView(context);
            imageView.setImageResource(iconResourceId);

            TextView textView = new TextView(context);
            textView.setTextColor(Color.WHITE);
            textView.setPadding(0, DensityUtil.dip2px(context, 14), 0, 0);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            textView.setGravity(Gravity.CENTER);
            textView.setId(android.R.id.message);
            textView.setText(message);

            linearLayout.addView(imageView);
            linearLayout.addView(textView);
            container.addView(linearLayout);
            rectangleToast.setView(container);
        } else
            rectangleToast.setText(message);
        rectangleToast.setGravity(Gravity.CENTER, 0, 0);
        rectangleToast.show();
    }

    /**
     * 很久才会弹一个Toast,不提升为成员变量
     *
     * @param message 传null则默认显示为 ConstantsValue.Other.NETWORK_ERROR_TIP_MSG
     */
    public void showToastAtCenter(String message) {
        if (isDestroyed) {
            return;
        }
        if (null == message) {
            message = ConstantsValue.Other.NETWORK_ERROR_TIP_MSG;
        }
        hiddenLoadingView();
        if (null == mToast) {
            mToast = new Toast(context);
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

            TextView textView = new TextView(context);
            textView.setTextColor(Color.WHITE);
            int paddingLeftAndRight = DensityUtil.dip2px(context, 14);
            int paddingTopAndBottom = DensityUtil.dip2px(context, 7);
            textView.setPadding(paddingLeftAndRight, paddingTopAndBottom, paddingLeftAndRight, paddingTopAndBottom);

            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            textView.setGravity(Gravity.CENTER);
            textView.setId(android.R.id.message);
            textView.setText(message);
            textView.setMinimumWidth(DensityUtil.dip2px(context, 200));
            textView.setBackgroundColor(context.getResources().getColor(R.color.transparent_70));

            linearLayout.addView(textView);
            mToast.setView(linearLayout);
            mToast.setMargin(0, 0);
        } else
            mToast.setText(message);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public void showToastAtBottom(String message) {
        if (isDestroyed) {
            return;
        }
        hiddenLoadingView();
        hiddenKeyboard();
        if (null == mToast) {
            mToast = new Toast(context);
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

            TextView textView = new TextView(context);
            textView.setTextColor(Color.WHITE);
            int paddingLeftAndRight = DensityUtil.dip2px(context, 14);
            int paddingTopAndBottom = DensityUtil.dip2px(context, 7);
            textView.setPadding(paddingLeftAndRight, paddingTopAndBottom, paddingLeftAndRight, paddingTopAndBottom);

            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            textView.setGravity(Gravity.CENTER);
            textView.setId(android.R.id.message);
            textView.setText(message);
            textView.setMinimumWidth(DensityUtil.dip2px(context, 200));
            textView.setBackgroundColor(context.getResources().getColor(R.color.transparent_70));
            linearLayout.addView(textView);
            mToast.setView(linearLayout);
            mToast.setMargin(0, 0);
        } else
            mToast.setText(message);

        mToast.setGravity(Gravity.BOTTOM, 0, DensityUtil.dip2px(context, 70));
        mToast.show();
    }


    public void hiddenKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView()
                        .getApplicationWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    /**
     * 切换输入法,如果显示就隐藏,如果隐藏就显示
     */
    public void toggleKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示"加载中"对话框
     */
    public void showLoading() {
        showLoading(null, true, true);
    }

    /**
     * 显示加载中对话框
     *
     * @param messageID 默认为“加载中...”
     * @return
     */
    public void showLoading(int messageID) {
        showLoading(getString(messageID));
    }

    /**
     * 显示加载中对话框
     *
     * @param message 默认为“加载中...”
     * @return
     */
    public void showLoading(String message) {
        showLoading(message, true, true);
    }

    /**
     * 显示加载中对话框
     *
     * @param message              默认为“加载中...”
     * @param cancelable           设置进度条是否可以按退回键取消
     * @param canceledTouchOutside 设置点击进度对话框外的区域对话框不消失
     */
    public void showLoading(String message, boolean cancelable, boolean canceledTouchOutside) {
        //由于于要调用hiddenLoadingView,会把loadingProgress置空,所以是不影响的,而且这样也能保证只显示一个
        if (isDestroyed) return;

        if (loadingProgress == null) {
            loadingProgress = new CircleLoadingProgressDialog(this);
        }
        if (message != null) {
            loadingProgress.setMessage(message);
        }
        loadingProgress.setCancelable(cancelable);
        loadingProgress.setCanceledOnTouchOutside(canceledTouchOutside);
        loadingProgress.show();
    }

    /**
     * 显示秒嗨,飞的小鲸鱼,加载中对话框
     */
    public void showMHLoading() {
//        showMHLoading(true, true);
        showLoading();
    }

    /**
     * 显示秒嗨,飞的小鲸鱼,加载中对话框
     *
     * @param cancelable           设置进度条是否可以按退回键取消
     * @param canceledTouchOutside 设置点击进度对话框外的区域对话框不消失
     */
    public void showMHLoading(boolean cancelable, boolean canceledTouchOutside) {
        showLoading(null, cancelable, canceledTouchOutside);
//        if (isDestroyed) return;
//        //由于于要调用hiddenLoadingView,会把loadingProgress置空,所以是不影响的,而且这样也能保证只显示一个
//        if (mhLoadingProgress == null) {
//            mhLoadingProgress = new LoadingProgressDialog(this);
//        }
//        mhLoadingProgress.setCancelable(cancelable);
//        mhLoadingProgress.setCanceledOnTouchOutside(canceledTouchOutside);
//        mhLoadingProgress.show();
    }


    /**
     * 隐藏加载框
     */
    public void hiddenLoadingView() {
        if (null == handler)
            handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (loadingProgress != null && loadingProgress.isShowing()) {
                    loadingProgress.dismiss();
                    loadingProgress = null;
                }
                if (mhLoadingProgress != null && mhLoadingProgress.isShowing()) {
                    mhLoadingProgress.dismiss();
                    mhLoadingProgress = null;
                }
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        MHLogUtil.d("onKeyDown----keyCode=" + keyCode + "---KeyEvent=" + event);
        if (KeyEvent.KEYCODE_BACK == keyCode && event.getRepeatCount() > 0)
            return true;

        if (KeyEvent.KEYCODE_BACK == keyCode) {
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }


    public void startActivityNoAnimation(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public boolean isLogin() {
        return !MHStringUtils.isEmpty(SpUtils.getString(ConstantsValue.Sp.TOKEN_MIAOHI));
    }


    /**
     * 判断是否登陆,如果没有登陆就会自动登陆
     *
     * @param needReturnMainActivity 是否需要返回首页
     * @return 是否登陆 true 已经登陆, false没有登陆
     */
    public boolean isLogin(boolean needReturnMainActivity) {
        if (!isLogin()) {
            //            showToastAtCenter("您还没有登录");
//            Intent intent = new Intent(context, LoginActivity.class);
//            intent.putExtra("needReturnMainActivity", needReturnMainActivity);
//            startActivity(intent);
            LoginDialogFragment loginDialog = new LoginDialogFragment();
            loginDialog.show(getSupportFragmentManager(), "loginDialog");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断是否登陆,如果没有登陆就会自动登陆(登录后会自动执行后续操作)
     */
    public boolean isLogin(boolean needReturnMainActivity, int jumpType) {
        if (!isLogin()) {
            LoginDialogFragment loginDialog = new LoginDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("jumpType", jumpType);
            loginDialog.setArguments(bundle);
            loginDialog.show(getSupportFragmentManager(), "loginDialog");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 点击屏幕关闭软键盘
     *
     * @param event
     */
    public void onHideSoftInput(MotionEvent event) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 不要轻易使用这个方法,请使用PullToRefreshBase里面的方法
     * 一般在pageIndex=0才错误页面显示,有内容了就不再展示错误页面,显示错误View之后就会移除空view
     */
    @Deprecated
    public void showErrorView() {
        isRetring = false;
        if (null != errorView) return;
        errorView = createErrorView();
        hideBlankView();
    }

    /**
     * 不要轻易使用这个方法,请使用PullToRefreshBase里面的方法
     */
    @Deprecated
    public void hideErrorView() {
        isRetring = false;
        if (null == errorView) return;
        ((ViewGroup) getWindow().getDecorView()).removeView(errorView);
        errorView = null;
    }

    /**
     * 不要轻易使用这个方法,请使用PullToRefreshBase里面的方法
     * 一般在pageIndex=0才显示空页面,显示空View之后就会移除错误view
     */
    @Deprecated
    public void showBlankView() {
        isRetring = false;
        if (null != blankView) return;
        blankView = createTipView(0, getResources().getString(R.string.empty_page_message));
        hideErrorView();
    }

    @Deprecated
    public void showBlankView(String content) {
        isRetring = false;
        if (null != blankView) return;
        blankView = createTipView(0, content);
        hideErrorView();
    }

    /**
     * 不要轻易使用这个方法,请使用PullToRefreshBase里面的方法
     */
    @Deprecated
    public void hideBlankView() {
        isRetring = false;
        if (null == blankView) return;
        ((ViewGroup) getWindow().getDecorView()).removeView(blankView);
        blankView = null;
    }

    private View createTipView(int drawable_id, String tip_message) {
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setClickable(true);

        linearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(drawable_id);
        TextView textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textView.setTextColor(context.getResources().getColor(R.color.color_999));
        textView.setPadding(0, DensityUtil.dip2px(context, 14), 0, 0);
        textView.setGravity(Gravity.CENTER);
        textView.setText(tip_message);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = DensityUtil.dip2px(context, 160);
        textView.setLayoutParams(params);

        linearLayout.addView(imageView);
        linearLayout.addView(textView);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = ScreenUtils.getStatusBarHeight(context) + context.getResources().getDimensionPixelSize(R.dimen.navigation_height);
        decorView.addView(linearLayout, layoutParams);
        return linearLayout;
    }

    private View createErrorView() {
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setClickable(true);
//        linearLayout.setBackgroundColor(context.getResources().getColor(R.color.common_bg));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);


        TextView textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        textView.setTextColor(context.getResources().getColor(R.color.color_333));
        textView.setGravity(Gravity.CENTER);
        textView.setText("网络抽风中...");

        TextView textView_2 = new TextView(context);
        textView_2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textView_2.setTextColor(context.getResources().getColor(R.color.gray_text_1));
        int margin = DensityUtil.dip2px(context, 14);
        textView_2.setPadding(0, margin, 0, margin);
        textView_2.setGravity(Gravity.CENTER);
        textView_2.setText("每天总有那么一会儿不舒服");

        TextView textView_3 = new TextView(context);
        textView_3.setText(context.getResources().getString(R.string.network_error_message));
        textView_3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textView_3.setGravity(Gravity.CENTER);
        textView_3.setTextColor(context.getResources().getColor(R.color.color_666));
        textView_3.setBackgroundResource(R.drawable.selector_corners_stroke);
        textView_3.setLayoutParams(new ViewGroup.LayoutParams(DensityUtil.dip2px(context, 150), DensityUtil.dip2px(context, 35)));
        textView_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRetring)
                    reTry();
            }
        });

        linearLayout.addView(textView);
        linearLayout.addView(textView_2);
        linearLayout.addView(textView_3);

        decorView.addView(linearLayout);
        return linearLayout;
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }

}
