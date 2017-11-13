package com.haiqiu.miaohi.statistics;

import android.content.Context;
import android.text.TextUtils;

import com.haiqiu.miaohi.statistics.bean.PageDurationTimeBean;
import com.haiqiu.miaohi.statistics.bean.StatisticalDataStorageBean;
import com.haiqiu.miaohi.utils.MHLogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by LiuXiang on 2016/11/16.
 * 统计页面数据，存储
 */
public class ActivityStatistics {
    private static final String TAG = "ActivityStatistics";
    private final Map<String, Long> hashMap = new HashMap();
    private final ArrayList list = new ArrayList();

    public ActivityStatistics() {

    }

    public void saveOnResumePageName(Context context, String pageName) {
        if (!TextUtils.isEmpty(pageName)) {
            MHLogUtil.i(TAG,"OnResume\t页面:" + pageName.substring(pageName.lastIndexOf(".")+1));
//            Intent serviceIntent=new Intent(context,StatisticsService.class);
//            serviceIntent.putExtra("tag","onResume");
//            context.startService(serviceIntent);
            Map map = this.hashMap;
            synchronized (this.hashMap) {
                this.hashMap.put(pageName, Long.valueOf(System.currentTimeMillis()));
            }
        }

    }

    public void saveOnPausePageName(Context context,String pageName) {
        if (!TextUtils.isEmpty(pageName)) {
            MHLogUtil.i(TAG,"onPause\t页面:" + pageName.substring(pageName.lastIndexOf(".")+1));
//            Intent serviceIntent=new Intent(context,StatisticsService.class);
//            serviceIntent.putExtra("tag","onResume");
//            context.startService(serviceIntent);
            Map map = this.hashMap;
            Long time;//界面时长计时结束的时间
            synchronized (this.hashMap) {
                time = this.hashMap.remove(pageName);
            }

            if (time == null) {
                MHLogUtil.e(TAG, "please call \'onPageStart(%s)\' before onPageEnd\t" + pageName);
                return;
            }

            long timeDifference = System.currentTimeMillis() - time.longValue();//界面停留时长
            ArrayList arrayList = this.list;
            synchronized (this.list) {
                PageDurationTimeBean bean = new PageDurationTimeBean();
                bean.setPageName(pageName);
                bean.setDurationTime(timeDifference);
                list.add(bean);
                StatisticalDataStorageBean.pageStopList.add(bean);
            }
        }

    }

}
