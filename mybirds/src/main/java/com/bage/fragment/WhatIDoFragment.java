package com.bage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bage.mybirds.R;

public class WhatIDoFragment extends BirdsFragment {

    @Override
    View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_birds2, null);
    }

    @Override
    protected void initData(View mCurrentView, Bundle savedInstanceState) {

    }

}