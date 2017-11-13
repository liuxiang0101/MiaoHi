package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.DiscoveryLabelObj;
import com.haiqiu.miaohi.fragment.FoundSquareFragment;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.response.SelectSportsResponse;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.SetClickStateUtil;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.view.HeaderGridView;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuXiang on 2016/11/30.
 * 用户选择运动标签界面
 */
public class RecommendSportsActivity extends BaseActivity {
    private int pageIndex = 0;
    private boolean isFromSetting = false;

    private Button bt_next;
    private ImageView iv_close;
    private GridView gv_recommend_sports;
    private PullToRefreshGridView pull_to_refresh_gridview;

    private GvAdapter adapter;
    private List<String> listLabelId = new ArrayList<>();
    private List<DiscoveryLabelObj> listPage = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_sports);
        isFromSetting = getIntent().getBooleanExtra("isFromSetting", false);
        showLoading();
        initView();
        initData();
    }

    private void initView() {
        iv_close = (ImageView) findViewById(R.id.iv_close);
        iv_close.setVisibility(isFromSetting ? View.VISIBLE : View.INVISIBLE);
        pull_to_refresh_gridview = (PullToRefreshGridView) findViewById(R.id.pull_to_refresh_gridview);
        gv_recommend_sports = pull_to_refresh_gridview.getRefreshableView();
        gv_recommend_sports.setNumColumns(3);
        bt_next = (Button) findViewById(R.id.bt_next);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin(false))
                    selectSports();
            }
        });
        pull_to_refresh_gridview.setPullRefreshEnabled(false);
        pull_to_refresh_gridview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<HeaderGridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<HeaderGridView> refreshView) {
                pageIndex = 0;
                initData();
            }

            @Override
            public void onLoadMore() {
                initData();
            }

        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SetClickStateUtil.getInstance().setStateListener(iv_close);
    }

    private void initData() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_size", "12");
        requestParams.addParams("page_index", pageIndex + "");
        MHHttpClient.getInstance().post(SelectSportsResponse.class, context, ConstantsValue.Url.SELECT_PREFERENCE_LABEL, requestParams, new MHHttpHandler<SelectSportsResponse>() {
            @Override
            public void onSuccess(SelectSportsResponse response) {
                pageLoadLogic(response);
            }

            @Override
            public void onFailure(String content) {
                pull_to_refresh_gridview.showErrorView();
                pull_to_refresh_gridview.onLoadComplete(true);
            }
        });
    }

    private void pageLoadLogic(SelectSportsResponse response) {
        List<DiscoveryLabelObj> li = response.getData().getPage_result();
        if (pageIndex == 0 && (null == li || li.size() == 0)) {
            pull_to_refresh_gridview.showBlankView("很抱歉，还没有推荐的运动类型");
        } else {
            pull_to_refresh_gridview.hideAllTipView();
        }
        if (pageIndex == 0) {
            listPage.clear();
        }
        if (null != li)
            listPage.addAll(li);
        if (null == li || li.size() == 0)
            pull_to_refresh_gridview.onLoadComplete(false);
        else {
            pull_to_refresh_gridview.onLoadComplete(true);
        }
        //数据正常，进行显示
        if (adapter == null) {
            adapter = new GvAdapter(listPage);
            gv_recommend_sports.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        pageIndex++;
    }

    /**
     * 适配器
     */
    private class GvAdapter extends BaseAdapter {
        private List<DiscoveryLabelObj> list;
        private ImageLoader imageLoader = ImageLoader.getInstance();

        public GvAdapter(List<DiscoveryLabelObj> list) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final DiscoveryLabelObj obj = list.get(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(RecommendSportsActivity.this, R.layout.item_recommend_sports, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //运动名称
            viewHolder.textView.setText(list.get(position).getLabel_name());
            //展示图片
            imageLoader.displayImage(list.get(position).getImg_uri(), viewHolder.imageView, DisplayOptionsUtils.getSilenceDisplayBuilder());
            //判断当前运动标签是否已经选择过
            if (obj.getIs_select() == 1) {
                viewHolder.textView.setVisibility(View.INVISIBLE);
                viewHolder.imageView.setAlpha(1f);
                if (!listLabelId.contains(obj.getLabel_id()))
                    listLabelId.add(obj.getLabel_id());
            } else {
                viewHolder.textView.setVisibility(View.VISIBLE);
                viewHolder.imageView.setAlpha(0.25f);
                if (listLabelId.contains(obj.getLabel_id()))
                    listLabelId.remove(obj.getLabel_id());
            }
            //底部按钮逻辑
            if (listLabelId.size() > 0) {
                bt_next.setText("继续");
                bt_next.setBackgroundResource(R.drawable.selector_button_black);
            } else {
                bt_next.setText("至少选择1项");
                bt_next.setBackgroundColor(getResources().getColor(R.color.color_c4));
            }
            //点击该项时
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    obj.setIs_select(obj.getIs_select() == 1 ? 0 : 1);
                    listLabelId.clear();
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            TextView textView;
        }
    }

    private void selectSports() {
        bt_next.setEnabled(false);
        showLoading("设置中...");
        if (listLabelId.size() == 0) {
            showToastAtBottom("最少选择1项");
            bt_next.setEnabled(true);
            return;
        }
        MHRequestParams requestParams = new MHRequestParams();
        Object[] objects = new Object[listLabelId.size()];
        for (int i = 0; i < listLabelId.size(); i++) {
            objects[i] = listLabelId.get(i);
        }
        String string = new Gson().toJson(objects);
        String string1 = string.replace("[", "").replace("]", "");
        requestParams.addParams("label_id", string1);
        MHHttpClient.getInstance().post(BaseResponse.class, context, ConstantsValue.Url.CONFIRM_PREFERENCE_LABEL, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                FoundSquareFragment.isNeedRefresh = true;
                bt_next.setEnabled(true);
//                showToastAtBottom("选择成功");
                SpUtils.put(ConstantsValue.Sp.CHOOSE_LABEL_FLAG, true);
                startActivity(new Intent(RecommendSportsActivity.this, RecommendUserActivity.class).putExtra("isFromSetting", isFromSetting));
                finish();
            }

            @Override
            public void onFailure(String content) {
                bt_next.setEnabled(true);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                bt_next.setEnabled(true);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isFromSetting)
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
