package com.haiqiu.miaohi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.VideoPropagandaActivity;
import com.haiqiu.miaohi.base.BaseFragment;

import java.lang.reflect.Field;

/**
 * Created by LiuXiang on 2016/12/22.
 */
public class VideoPropagandaFragment extends BaseFragment {
    private TextView bt_next;
    private ImageView iv_slogan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_propaganda, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        bt_next = (TextView) rootView.findViewById(R.id.bt_next);
        iv_slogan = (ImageView) rootView.findViewById(R.id.iv_slogan);
        final TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1,
                Animation.RELATIVE_TO_PARENT, 0);
        ta.setDuration(800);
        iv_slogan.postDelayed(new Runnable() {
            @Override
            public void run() {
                bt_next.startAnimation(ta);
                bt_next.setVisibility(View.VISIBLE);
            }
        }, 2600);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VideoPropagandaActivity) getActivity()).changeLoginFragment();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
