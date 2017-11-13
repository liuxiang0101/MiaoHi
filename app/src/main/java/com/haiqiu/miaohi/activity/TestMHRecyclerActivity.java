package com.haiqiu.miaohi.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.TestRecyclerAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.ItemInfo;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.widget.HeaderRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Random;

public class TestMHRecyclerActivity extends BaseActivity {
    private final static String TAG = "TestLuRecycler_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mh_recycler);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        final ArrayList<ItemInfo> arrayList = getData();
        final TestRecyclerAdapter adapter = new TestRecyclerAdapter(arrayList);

        HeaderRecyclerViewAdapter headerRecyclerViewAdapter = new HeaderRecyclerViewAdapter(adapter);


        TextView textView = new TextView(this);
        textView.setText("这是头View");
        headerRecyclerViewAdapter.addHeaderView(textView);

        TextView textView2 = new TextView(this);
        textView2.setText("这是尾View");
        headerRecyclerViewAdapter.addFooterView(textView2);


        recyclerView.setAdapter(headerRecyclerViewAdapter);
        findViewById(R.id.bt_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int temp = 1 / 0;
                } catch (Exception e) {
//                    MHLogUtil.e(e);
                    MHLogUtil.e("Test", e);
                }
            }
        });
    }

    private ArrayList<ItemInfo> getData() {
        final ArrayList<ItemInfo> list = new ArrayList<>();
        Random random = new Random(System.currentTimeMillis());

        ItemInfo itemInfo;
        for (int i = 0; i < 30; i++) {
            itemInfo = new ItemInfo();
            itemInfo.setName("测试数据" + i);
            itemInfo.setHeight(200 + random.nextInt(900));
            itemInfo.setType(i % 2 == 0 ? TestRecyclerAdapter.TEXT : TestRecyclerAdapter.IMAGE);
            list.add(itemInfo);
        }
        return list;
    }
}
