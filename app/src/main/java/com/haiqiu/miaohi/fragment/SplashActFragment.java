package com.haiqiu.miaohi.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class SplashActFragment extends BaseFragment {

    public SplashActFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(R.layout.frag_splash, container, false);
    }
}
