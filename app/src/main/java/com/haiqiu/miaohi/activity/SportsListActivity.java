package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.HotResponderAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.HotResponderData;
import com.haiqiu.miaohi.bean.HotResponderKindResult;
import com.haiqiu.miaohi.bean.HotResponderPageResult;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.HotResponderResponse;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.SetClickStateUtil;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshListView;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LiuXiang on 2016/12/7.
 * 选择不同运动时的列表页
 */
public class SportsListActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private int pageIndex = 0;
    private String kind_id;

    private ImageView iv_back;
    private ImageView iv_pull_down;
    private TextView tv_sport;
    private ListView listView;
    private LinearLayout ll_sport;
    private PullToRefreshListView pulltorefresh_listview;
    private HotResponderAdapter adapter;
    private UMShareAPI umShareAPI = null;

    private HashMap<String, Boolean> hashMap;
    private List<HotResponderPageResult> listPage = new ArrayList<>();      //存放九宫格视图数据的总集合
    private List<HotResponderKindResult> listLabel = new ArrayList<>();     //存放分类标签的集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_list);
        umShareAPI = UMShareAPI.get(context);
        kind_id = getIntent().getStringExtra("kind_id");

        initView();
        initListView();

        getNetData();
    }

    /**
     * 初始化
     */
    private void initView() {
        hashMap = new HashMap<>();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_sport = (TextView) findViewById(R.id.tv_sport);
        tv_sport.setText(getIntent().getStringExtra("kind_name"));
        ll_sport = (LinearLayout) findViewById(R.id.ll_sport);
        iv_pull_down = (ImageView) findViewById(R.id.iv_pull_down);
        pulltorefresh_listview = (PullToRefreshListView) findViewById(R.id.pull_lv);
        pulltorefresh_listview.setPullLoadEnabled(true);
        pulltorefresh_listview.setPullRefreshEnabled(true);

        iv_back.setOnClickListener(this);
        ll_sport.setOnClickListener(this);
        iv_pull_down.setOnClickListener(this);
        listView = pulltorefresh_listview.getRefreshableView();
        listView.setDividerHeight(0);
        SetClickStateUtil.getInstance().setStateListener(iv_back);
        SetClickStateUtil.getInstance().setStateListener(ll_sport);
    }

    private void initListView() {
        pulltorefresh_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 0;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                getNetData();
            }

        });
    }

    /**
     * 服务端获取数据
     */
    private void getNetData() {
        if (MHStringUtils.isEmpty(kind_id)) return;
        final MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_index", "" + pageIndex);
        requestParams.addParams("page_size", "20");
        requestParams.addParams("kind_id", kind_id);
        MHHttpClient.getInstance().post(HotResponderResponse.class, context, ConstantsValue.Url.TOP_QUESTION_VIP, requestParams, new MHHttpHandler<HotResponderResponse>() {
            @Override
            public void onSuccess(HotResponderResponse response) {
                pageLoadLogic(response);
            }

            @Override
            public void onFailure(String content) {
                pulltorefresh_listview.onLoadComplete(true);
            }

        });
    }

    /**
     * 分页逻辑
     *
     * @param response
     */
    private void pageLoadLogic(HotResponderResponse response) {
        //空数据页逻辑
        if (pageIndex == 0 && (response.getData() == null || null == response.getData().getPage_result() || response.getData().getPage_result().size() == 0)) {
            pulltorefresh_listview.showBlankView();
        } else {
            pulltorefresh_listview.hideAllTipView();
        }
        //弹出的运动列表数据
        HotResponderData data = response.getData();
        listLabel = data.getKind_result();
        for (HotResponderKindResult result : listLabel) {
            hashMap.put(result.getKind_id(), false);
            if (result.getKind_id().equals(kind_id))
                tv_sport.setText(result.getKind_name());
        }
        hashMap.put(kind_id, true);

        //主体内容数据
        List<HotResponderPageResult> li = response.getData().getPage_result();
        if (pageIndex == 0) {
            listPage.clear();
        }
        if (null != li)
            listPage.addAll(li);
        if (null == li || li.size() == 0)
            pulltorefresh_listview.onLoadComplete(false);
        else {
            pulltorefresh_listview.onLoadComplete(true);
        }

        //数据正常，进行显示
        if (adapter == null) {
            adapter = new HotResponderAdapter(listPage, context);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        pageIndex++;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.iv_pull_down:
            case R.id.ll_sport:
                showPopupWindow();
                break;
        }
    }

    /**
     * 弹出运动类型的列表
     */
    private void showPopupWindow() {
        View popView = View.inflate(this, R.layout.pop_sports_list, null);
        final PopupWindow popupWindow = new PopupWindow(popView, ScreenUtils.getScreenWidth(this), ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.popwin_anim_fade_style);
        initPopupLv((ListView) popView.findViewById(R.id.listview), popupWindow);
        popView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(getWindow().getDecorView().findViewById(android.R.id.content), Gravity.NO_GRAVITY, 0, ScreenUtils.getStatusBarHeight(this));
    }

    /**
     * 设置popupWindow中的列表
     *
     * @param listView
     * @param popupWindow
     */
    private void initPopupLv(ListView listView, final PopupWindow popupWindow) {
        final PopupLvAdapter adapter = new PopupLvAdapter(listLabel);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                kind_id = listLabel.get(position).getKind_id();
                pageIndex = 0;
                getNetData();
                popupWindow.dismiss();
            }
        });
    }

    class PopupLvAdapter extends BaseAdapter {
        private List<HotResponderKindResult> list;

        public PopupLvAdapter(List<HotResponderKindResult> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_sports_sort, null);
                vh = new ViewHolder();
                vh.textView = (TextView) convertView.findViewById(R.id.textView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            if (hashMap.get(list.get(position).getKind_id())) {
                vh.textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
                vh.textView.getPaint().setFakeBoldText(true);
                vh.textView.setTextColor(context.getResources().getColor(R.color.fontblue));
            } else {
                vh.textView.getPaint().setFakeBoldText(false);
                vh.textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
                vh.textView.setTextColor(context.getResources().getColor(R.color.white));
            }
            vh.textView.setText(list.get(position).getKind_name());
            return convertView;
        }

        class ViewHolder {
            TextView textView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) umShareAPI.onActivityResult(requestCode, resultCode, data);
    }
}
