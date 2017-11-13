package com.haiqiu.miaohi.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.haiqiu.miaohi.utils.MHLogUtil;

/**
 * 不能随便改这个类,要在这个类里面加一行代码都要通知到其它组员!!!!!
 * Created by ningl on 2016/5/20.
 */
public class BaseFragment extends Fragment {
    protected final String TAG = getClass().getSimpleName() + "_TAG";
    protected boolean hasInited;
    public BaseActivity context;
    protected boolean isVisibleToUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (BaseActivity) getActivity();
    }


    public BaseActivity getContext() {
        return context;
    }


    public boolean isLogin() {
        return getContext().isLogin();
    }

    /**
     * 判断是否登陆,如果没有登陆就会自动登陆
     *
     * @param needReturnMainActivity 是否需要返回首页
     * @return 是否登陆
     */
    public boolean isLogin(boolean needReturnMainActivity) {
        return getContext().isLogin(needReturnMainActivity);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
//        MHLogUtil.d("---", "isVisibleToUser=" + isVisibleToUser + "----" + this.getClass().getSimpleName());
        if (isVisibleToUser) {
            if (!hasInited) {
                hasInited = true;
                lazyLoad();
            } else
                onMyResume();
        } else if (hasInited) {
            onMyPause();
        }
    }


    /**
     * 延迟加载
     * 子类必须重写此方法,这个方法只会调用一次,相当于activity的onCreate
     */
    protected void lazyLoad() {
//        MHLogUtil.d("---", "lazyLoad---" + getClass().getSimpleName());
//        TCAgent.onPageStart(context, getClass().getSimpleName());
    }


    /**
     * 第一次不会调用这个方法,第一次会调用{@link #lazyLoad()},这样来保证数据加载一次
     * 当重新回到这个页面会调用这个方法
     */
    protected void onMyResume() {
//        TCAgent.onPageStart(context, getClass().getSimpleName());
        MHLogUtil.d("---", "onMyResume---" + getClass().getSimpleName());
    }

    /**
     * 页面失去焦点
     */
    protected void onMyPause() {
//        MHLogUtil.d("---", "onMyPause---" + getClass().getSimpleName());
//        TCAgent.onPageEnd(context, getClass().getSimpleName());
    }

    public void refreshData() {

    }
}
