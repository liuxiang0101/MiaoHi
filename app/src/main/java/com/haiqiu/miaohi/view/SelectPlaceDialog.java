package com.haiqiu.miaohi.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.CityModel;
import com.haiqiu.miaohi.bean.DistrictModel;
import com.haiqiu.miaohi.bean.ProvinceModel;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.widget.wheelview.OnWheelChangedListener;
import com.haiqiu.miaohi.widget.wheelview.WheelView;
import com.haiqiu.miaohi.widget.wheelview.WheelViewSelectCallBack;
import com.haiqiu.miaohi.widget.wheelview.XmlParserHandler;
import com.haiqiu.miaohi.widget.wheelview.adapters.ArrayWheelAdapter;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 选择时间对话框
 * Created by ningl on 2016/5/31.
 */
public class SelectPlaceDialog extends Dialog implements View.OnClickListener, OnWheelChangedListener {
    private static final String TAG = "SelectPlaceDialog";

    private TextView tv_selectplace_cancle;
    private TextView tv_selectplace_ok;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private Activity context;
    private WheelViewSelectCallBack wheelViewSelectCallBack;
    private ArrayWheelAdapter cityAdapter;
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";

    private int indexProvince, indexCity, indexArea;

    public SelectPlaceDialog(Activity context) {
        super(context, R.style.MiaoHiDialog);
        setContentView(R.layout.dialog_selectplace);
        this.context = context;
        initView();
        initParams(context);
        setUpData(context);
    }

    public void initView() {
        tv_selectplace_cancle = (TextView) this.findViewById(R.id.tv_selectplace_cancle);
        tv_selectplace_ok = (TextView) this.findViewById(R.id.tv_selectplace_ok);
        mViewProvince = (WheelView) this.findViewById(R.id.wheelview1);
        mViewCity = (WheelView) this.findViewById(R.id.wheelview2);
        mViewDistrict = (WheelView) this.findViewById(R.id.wheelview3);
        tv_selectplace_cancle.setOnClickListener(this);
        tv_selectplace_ok.setOnClickListener(this);
        mViewProvince.addChangingListener(this);
        mViewCity.addChangingListener(this);
        mViewDistrict.addChangingListener(this);
    }

    public void initParams(Activity activity) {
        Window win = getWindow();
        WindowManager wm = activity.getWindowManager();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = ScreenUtils.getScreenSize(activity).x;
        win.setAttributes(lp);
        win.setGravity(Gravity.BOTTOM);
        win.setWindowAnimations(R.style.BottomInAnim);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_selectplace_cancle:
                dismiss();
                break;
            case R.id.tv_selectplace_ok:
                if (wheelViewSelectCallBack != null) {
                    wheelViewSelectCallBack.select(mCurrentProviceName, mCurrentCityName, mCurrentDistrictName);
                }
                dismiss();
                break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities(context);
        } else if (wheel == mViewCity) {
            cityAdapter.setSelectColor(newValue);
            updateAreas(context);
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    private void setUpData(Context context) {
        initProvinceDatas(context);
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities(context);
        updateAreas(context);
    }

    /**
     * 解析省市区的XML数据
     */
    protected void initProvinceDatas(Context context) {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {

            MHLogUtil.e(TAG, e);
        } finally {

        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities(Context context) {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        cityAdapter = new ArrayWheelAdapter<String>(context, cities);
        mViewCity.setViewAdapter(cityAdapter);
        mViewCity.setCurrentItem(0);
        updateAreas(context);
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas(Context context) {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
        if (areas.length != 0) {
            mCurrentDistrictName = areas[0];
            mViewDistrict.setCurrentItem(0);
        }
    }

    /**
     * 设置默认省市县
     *
     * @param province 省
     * @param city     市
     * @param area     县
     */
    public SelectPlaceDialog setDefaultAddress(String province, String city, String area) {
//        mCurrentProviceName, mCurrentCityName, mCurrentDistrictName;
        try {
            if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(province) && !TextUtils.isEmpty(province)) {
                for (int i = 0; i < mProvinceDatas.length; i++) {
                    if (TextUtils.equals(province, mProvinceDatas[i])) {
                        indexProvince = i;
                        mCurrentProviceName = province;
                        mViewProvince.setCurrentItem(indexProvince);
                    }
                }
                String[] cities = mCitisDatasMap.get(province);
                mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
                for (int i = 0; i < cities.length; i++) {
                    if (TextUtils.equals(city, cities[i])) {
                        indexCity = i;
                        mCurrentCityName = city;
                        mViewCity.setCurrentItem(indexCity);
                    }
                }
                String[] areas = mDistrictDatasMap.get(city);
                mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
                for (int i = 0; i < areas.length; i++) {
                    if (TextUtils.equals(area, areas[i])) {
                        indexArea = i;
                        mCurrentDistrictName = area;
                        mViewDistrict.setCurrentItem(indexArea);
                    }
                }
            }
        } catch (Exception e) {

            MHLogUtil.e(TAG, e);
        }

        return SelectPlaceDialog.this;
    }

    public SelectPlaceDialog setOnWheelViewSelectListener(WheelViewSelectCallBack wheelViewSelectCallBack) {
        this.wheelViewSelectCallBack = wheelViewSelectCallBack;
        return SelectPlaceDialog.this;
    }
}
