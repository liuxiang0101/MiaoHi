package com.haiqiu.miaohi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.DiscoveryLabelObj;
import com.haiqiu.miaohi.utils.MHStringUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by LiuXiang on 2016/10/28.
 */
public class SortDialogNew extends DialogFragment {
    private String currentKindTag;
    private String currentCommend;
    private ArrayList<DiscoveryLabelObj> list;
    private HashMap<Integer, Boolean> pageMap = new HashMap<>();

    private Button bt_ok;
    private ImageView iv_close;
    private GridView gridview;

    public void setCurrentCommend(String currentCommend) {
        this.currentCommend = currentCommend;
    }

    public void setCurrentKindTag(String currentKindTag) {
        this.currentKindTag = currentKindTag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //接收数据
        list = getArguments().getParcelableArrayList("list");
        currentKindTag = getArguments().getString("kind_tag");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        View view = inflater.inflate(R.layout.sort_dialog_new, container);
        currentCommend = getArguments().getString("commend");
        initView(view);
        initData();

        //设置默认状态
        for (int i = 0; i < list.size(); i++) {
            pageMap.put(i,false);
            if (list.get(i).getKind_tag().equals(currentKindTag)) {
                pageMap.put(i,true);
            }
        }
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        bt_ok = (Button) view.findViewById(R.id.bt_ok);
        iv_close = (ImageView) view.findViewById(R.id.iv_close);
        gridview = (GridView) view.findViewById(R.id.gridview);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //点击OK按钮
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MHStringUtils.isEmpty(currentKindTag)) {
                    Toast.makeText(getActivity(), "未选择标签", Toast.LENGTH_SHORT).show();
                    return;
                }
                //点击按钮时切换发现页的分类内容
                ((FoundSquareFragment) getParentFragment()).changeLabel(currentKindTag, currentCommend, true);
                SortDialogNew.this.dismiss();
            }
        });
        //点击"X"关闭
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortDialogNew.this.dismiss();
            }
        });
        gridview.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(final int position, View view, ViewGroup viewGroup) {
                view= View.inflate(getActivity(),R.layout.item_sort_gv,null);
                final TextView textView= (TextView) view.findViewById(R.id.textView);
                textView.setText(list.get(position).getKind_name());
                if(pageMap.get(position)){
                    textView.setSelected(true);
                    textView.setTextColor(getResources().getColor(R.color.color_1d));
                }else {
                    textView.setSelected(false);
                    textView.setTextColor(getResources().getColor(R.color.color_c4));
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //设置当前点击控件置为选中状态
                        for (int i = 0; i < pageMap.size(); i++) {
                            pageMap.put(i, false);
                            textView.setTextColor(getResources().getColor(R.color.color_c4));
                        }
                        pageMap.put(position, true);
                        textView.setTextColor(getResources().getColor(R.color.color_1d));

                        notifyDataSetChanged();

                        //切换label标签，列表接口刷新
                        setCurrentKindTag(list.get(position).getKind_tag());
                        setCurrentCommend(list.get(position).getKind_Command());
                    }
                });
                return view;
            }
        });
    }

    @Override
    public void onDestroyView() {
        currentKindTag = null;
        currentCommend = null;
        super.onDestroyView();
    }
}
