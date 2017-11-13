package com.haiqiu.miaohi.view;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.TimeFormatUtils;
import com.haiqiu.miaohi.widget.wheelview.OnWheelChangedListener;
import com.haiqiu.miaohi.widget.wheelview.WheelView;
import com.haiqiu.miaohi.widget.wheelview.WheelViewSelectCallBack;
import com.haiqiu.miaohi.widget.wheelview.adapters.ArrayWheelAdapter;

/**
 * Created by ningl on 2016/5/31.
 */
public class SelectTimeDialog extends Dialog implements View.OnClickListener, OnWheelChangedListener {

    private WheelView wv_y;
    private WheelView wv_m;
    private WheelView wv_d;
    private Activity activity;
    private TextView tv_selectplace_cancle, tv_selectplace_ok;
    private String[] years, months, months1,days1, days2, days3, days4, days5;
    private String year, month, day;
    private WheelViewSelectCallBack wheelViewSelectCallBack;

    public SelectTimeDialog(Activity activity) {
        super(activity, R.style.MiaoHiDialog);
        setContentView(R.layout.dialog_selectplace);
        this.activity = activity;
//        this.onSelectPlaceCallBack = onSelectPlaceCallBack;
        initView();
        initData(activity);
        initParams(activity);
        setUpData(activity);
    }

    public void initView(){
        tv_selectplace_cancle = (TextView) this.findViewById(R.id.tv_selectplace_cancle);
        tv_selectplace_ok = (TextView) this.findViewById(R.id.tv_selectplace_ok);
        wv_y = (WheelView) this.findViewById(R.id.wheelview1);
        wv_m = (WheelView) this.findViewById(R.id.wheelview2);
        wv_d = (WheelView) this.findViewById(R.id.wheelview3);
        wv_y.setVisibleItems(7);
        wv_m.setVisibleItems(7);
        wv_d.setVisibleItems(7);
        tv_selectplace_cancle.setOnClickListener(this);
        tv_selectplace_ok.setOnClickListener(this);
        wv_y.addChangingListener(this);
        wv_m.addChangingListener(this);
        wv_d.addChangingListener(this);
        // 设置可见条目数量
//        wv_y.setVisibleItems(7);
//        wv_m.setVisibleItems(7);
//        wv_d.setVisibleItems(7);
    }

    public void initData(Activity activity){
        //初始化年数据
        year = "1900年";
        int maxYear = Integer.parseInt(TimeFormatUtils.getY());
        int yearCount = maxYear - 1900 +1;
        years = new String[yearCount];
        for(int i = 0; i<yearCount; i++){
            years[i] = i+1900+"年";
        }
        //初始化月数据
        month = "1月";
        months = new String[12];
        for(int i = 0; i<12; i++){
            months[i] = String.valueOf(i+1)+"月";
        }
        //当年到本月已经过了多少月
        int currentMonth = TimeFormatUtils.getCurrentMonth();
        months1 = new String[currentMonth];
        for (int i = 0; i < months1.length; i++) {
            months1[i] = i+1+"月";
        }
        //初始化日数据
        day = "1日";
        //平年28天
        days1 = new String[28];
        for(int i = 0; i<days1.length; i++){
            days1[i] = String .valueOf(i+1)+"日";
        }
        //闰年30天
        days2 = new String[29];
        for(int i = 0; i<days2.length; i++){
            days2[i] = String.valueOf(i + 1)+"日";
        }
        //30天
        days3 = new String[30];
        for(int i = 0; i<days3.length; i++){
            days3[i] = String.valueOf(i+1)+"日";
        }
        //31天
        days4 = new String[31];
        for(int i = 0; i<days4.length; i++){
            days4[i] = String.valueOf(i+1)+"日";
        }
        //当月到今天已经过了多少天
        int currentDay = TimeFormatUtils.getCurrentDay();
        days5 = new String[currentDay];
        for (int i = 0; i < days5.length; i++) {
            days5[i] = i+1+"日";
        }
    }

    public void initParams(Activity activity){
        Window win = getWindow();
        WindowManager wm = activity.getWindowManager();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = ScreenUtils.getScreenSize(activity).x;
        win.setAttributes(lp);
        win.setGravity(Gravity.BOTTOM);
        win.setWindowAnimations(R.style.BottomInAnim);
    }

    /**
     * 设置数据
     * @param activity
     */
    public void setUpData(Activity activity){
        wv_y.setViewAdapter(new ArrayWheelAdapter<String>(activity, years));
        wv_y.setCurrentItem(1990 - 1900);
        wv_m.setViewAdapter(new ArrayWheelAdapter<String>(activity, months));
        wv_m.setCurrentItem(0);
        updateTime(1900, 1);
    }

    /**
     * 更新日
     * @param year
     * @param month
     */
    public void updateTime(int year, int month){
        int currentMonth = TimeFormatUtils.getCurrentMonth();
        int currentYear = TimeFormatUtils.getCurrentYear();
        if(year == currentYear&&month == currentMonth){
            wv_d.setViewAdapter(new ArrayWheelAdapter<String>(activity, days5));
        } else {
            if(month == 2){
                if(isLeapYear(year)){
                    wv_d.setViewAdapter(new ArrayWheelAdapter<String>(activity, days2));
                } else {
                    wv_d.setViewAdapter(new ArrayWheelAdapter<String>(activity, days1));
                }
            } else {
                if(month == 1||month == 3||month == 5||month == 7||month == 8||month == 10||month == 12){
                    wv_d.setViewAdapter(new ArrayWheelAdapter<String>(activity, days4));
                } else {
                    wv_d.setViewAdapter(new ArrayWheelAdapter<String>(activity, days3));
                }
            }
            wv_d.setCurrentItem(0);
        }
    }

    /**
     * 更新月
     */
    public void updateMonth(int year){
        int currentYear = TimeFormatUtils.getCurrentYear();
        if(year == currentYear){
            wv_m.setViewAdapter(new ArrayWheelAdapter<String>(activity, months1));
        } else {
            wv_m.setViewAdapter(new ArrayWheelAdapter<String>(activity, months));
        }
        wv_m.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_selectplace_cancle:
                dismiss();
                break;
            case R.id.tv_selectplace_ok:
                if(wheelViewSelectCallBack!=null){
                    String resultMonth = null;
                    String resultDay = null;
                    if(month.length()==2){
                        resultMonth = "0"+ month.replace("月", "");
                    } else {
                        resultMonth = month.replace("月", "");
                    }
                    if(day.length()==2){
                        resultDay = "0"+ day.replace("日", "");
                    } else {
                        resultDay = day.replace("日", "");
                    }
                    wheelViewSelectCallBack.select(year.replace("年", ""), resultMonth, resultDay);
                }
                dismiss();
                break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if(wheel == wv_y){
            year = years[newValue];
            updateMonth(Integer.parseInt(year.replace("年", "")));
        } else if(wheel == wv_m){
            month = months[newValue];
            updateTime(Integer.parseInt(year.replace("年", "")), Integer.parseInt(month.replace("月","")));
        } else if(wheel == wv_d){
            day = days4[newValue];
        }
    }

    /**
     * 判断是否是闰年
     * @param year 年份
     */
    public boolean isLeapYear(int year){
        boolean b1 = (year % 400 == 0);
        boolean b2 = (year % 4 == 0) && (year % 100 != 0);
        return (b1||b2)?true:false;
    }

    public SelectTimeDialog setOnWheelViewSelectListener(WheelViewSelectCallBack wheelViewSelectCallBack){
        this.wheelViewSelectCallBack = wheelViewSelectCallBack;
        return SelectTimeDialog.this;
    }

    /**
     * 设置日期
     * @param date 日期
     * @return
     */
    public SelectTimeDialog setDate(String date){
        if(date!=null&&!TextUtils.equals("", date)){
            try {
                String[] dates = date.split("/");
                int year = Integer.parseInt(dates[0]);
                int month = Integer.parseInt(dates[1]);
                int day = Integer.parseInt(dates[2]);
                wv_y.setCurrentItem(year-1900);
                wv_m.setCurrentItem(month-1);
                wv_d.setCurrentItem(day-1);
            } catch (Exception e){
                MHLogUtil.e("setDate",e);
            }
        }
        return SelectTimeDialog.this;
    }
}
