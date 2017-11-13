package com.haiqiu.miaohi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.SearchHistoryRecordAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SetClickStateUtil;
import com.haiqiu.miaohi.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索界面--历史搜索记录
 * Created by zhandalin on 2016/6/23.
 */
public class SearchHistoryRecordActivity extends BaseActivity {
    private EditText et_search_user;
    private ListView lv_search_history_record;
    private List<String> list = new ArrayList<>();
    private Gson gson = new Gson();
    private SearchHistoryRecordAdapter adapter;
    private Button footerBotton;
    private View headerView;
    private View footerView;
    private View rootView;
    private LinearLayout ll_clear_et_text;
    private int searchType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history_record);
        searchType = getIntent().getIntExtra("searchType", 0);
        //获取本地保存的历史搜索记录
        String historyRecord = SpUtils.getString("history_record");
        if (MHStringUtils.isEmpty(historyRecord)) {
            list = new ArrayList<>();
        } else {
            list = gson.fromJson(historyRecord, list.getClass());
        }
        initView();
    }

    @Override
    protected void onResume() {
        if (list.size() == 0) {
            headerView.setVisibility(View.GONE);
            footerView.setVisibility(View.GONE);
        } else {
            headerView.setVisibility(View.VISIBLE);
            footerView.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    private void initView() {
        rootView = findViewById(android.R.id.content);
        et_search_user = (EditText) findViewById(R.id.et_search_user);
        lv_search_history_record = (ListView) findViewById(R.id.lv_search_history_record);
        et_search_user = (EditText) findViewById(R.id.et_search_user);
        ll_clear_et_text = (LinearLayout) findViewById(R.id.ll_clear_et_text);
        String keyWords = getIntent().getStringExtra("keyWords");
        if (!MHStringUtils.isEmpty(keyWords)) {
            et_search_user.setText(keyWords);
            et_search_user.setSelection(keyWords.length());
            ll_clear_et_text.setVisibility(View.VISIBLE);
        }
        ll_clear_et_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search_user.setText("");
            }
        });
        headerView = View.inflate(this, R.layout.header_history_record, null);
        footerView = View.inflate(this, R.layout.footer_history_record, null);
        headerView.findViewById(R.id.tv_history_record).setVisibility(View.VISIBLE);
        (headerView.findViewById(R.id.ll_tag_search)).setVisibility(View.GONE);
        lv_search_history_record.addHeaderView(headerView);
        lv_search_history_record.addFooterView(footerView);

        footerBotton = (Button) footerView.findViewById(R.id.bt_clear_history_record);
        //清空本地的搜索历史记录
        footerBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CommonDialog commonDialog = new CommonDialog(context);
//                commonDialog.setLeftButtonMsg("取消");
//                commonDialog.setRightButtonMsg("确定");
//                commonDialog.setContentMsg("是否清空全部历史记录?");
//                commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
//                    @Override
//                    public void onRightButtonOnClick() {
                SpUtils.put("history_record", "");
                headerView.setVisibility(View.GONE);
                footerView.setVisibility(View.GONE);
                list.clear();
                adapter.notifyDataSetChanged();
//                    }
//                });
//                commonDialog.show();
            }
        });
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });

        initEditView();
        initListView();
    }

    public void setfooterViewText() {
        headerView.setVisibility(View.GONE);
        footerView.setVisibility(View.GONE);
    }

    private void initEditView() {
        et_search_user.setHint("搜索你想要的");
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        SetClickStateUtil.getInstance().setStateListener(findViewById(R.id.iv_close));
        et_search_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ll_clear_et_text.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        });
        et_search_user.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startSearch(true);
                    return true;
                }
                return false;
            }

        });
        et_search_user.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    et_search_user.setCursorVisible(true);// 再次点击显示光标
                }
                return false;
            }
        });
    }

    private void initListView() {
        adapter = new SearchHistoryRecordAdapter(this, list, searchType);
        lv_search_history_record.setAdapter(adapter);
    }

    private void startSearch(boolean showTip) {
        String keyWords = et_search_user.getText().toString().trim();
        if (MHStringUtils.isEmpty(keyWords)) {
            if (showTip)
                showToastAtCenter("请输入搜索内容");
            return;
        }

//        saveHistoryRecord(keyWords);

        Intent intent = new Intent(this, SearchBodyActivity.class);
        intent.putExtra("keyWords", keyWords);
        intent.putExtra("searchType", searchType);
        startActivityNoAnimation(intent);
        finish();
    }

//    //将历史记录保存至本地
//    private void saveHistoryRecord(String keyWords) {
//        if (list.size() >= 10) {
//            for (int i = 0; i < 10; i++) {
//                if (i < 9)
//                    list.set(i, list.get(i + 1));
//                else list.set(9, keyWords);
//            }
//        } else {
//            list.add(list.size(), keyWords);
//        }
//        SpUtils.put("history_record", gson.toJson(list));
//    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && event.getRepeatCount() > 0)
            return true;

        if (KeyEvent.KEYCODE_BACK == keyCode) {
            finish();
            overridePendingTransition(0, 0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 隐藏键盘
     */
    private void hideKeyboard(View view) {
        et_search_user.setCursorVisible(false);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            //隐藏键盘
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//
//            //获取当前获得当前焦点所在View
//            View view = getCurrentFocus();
//            if (isClickEt(view, event)) {
//                //如果不是edittext，则隐藏键盘
//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (inputMethodManager != null) {
//                    //隐藏键盘
//                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
//            }
//            return super.dispatchTouchEvent(event);
//        }
//        /**
//         * 看源码可知superDispatchTouchEvent  是个抽象方法，用于自定义的Window
//         * 此处目的是为了继续将事件由dispatchTouchEvent(MotionEvent event)传递到onTouchEvent(MotionEvent event)
//         * 必不可少，否则所有组件都不能触发 onTouchEvent(MotionEvent event)
//         */
//        if (getWindow().superDispatchTouchEvent(event)) {
//            return true;
//        }
//        return onTouchEvent(event);
//    }
//
//    public boolean isClickEt(View view, MotionEvent event) {
//        if (view != null && (view instanceof EditText)) {
//            int[] leftTop = {0, 0};
//            //获取输入框当前的location位置
//            view.getLocationInWindow(leftTop);
//            int left = leftTop[0];
//            int top = leftTop[1];
//            //此处根据输入框左上位置和宽高获得右下位置
//            int bottom = top + view.getHeight();
//            int right = left + view.getWidth();
//            return !(event.getX() > left && event.getX() < right
//                    && event.getY() > top && event.getY() < bottom);
//        }
//        return false;
//    }
}
