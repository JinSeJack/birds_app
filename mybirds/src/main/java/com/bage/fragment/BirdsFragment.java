package com.bage.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BirdsFragment extends Fragment {

    protected View mCurrentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentView = inflateView(inflater, container, savedInstanceState);
        return mCurrentView;
    }

    /**
     * 给当前的fragment设置view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    abstract View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(mCurrentView,savedInstanceState);
    }

    /**
     * 给当前fragment设置数据
     * @param mCurrentView
     * @param savedInstanceState
     */
    protected abstract void initData(View mCurrentView, Bundle savedInstanceState);
}
