package com.bage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bage.mybirds.R;

public class NewsTextFragment extends NewsFragment{

    private WebView new_webview;
    @Override
    View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_news_text, null);
        return inflater.inflate(R.layout.fragment_news_text_web, null);
    }

//    @Override
//    protected void initData(View mCurrentView, Bundle savedInstanceState) {
//
//        mCurrentView.setBackgroundResource(R.drawable.newsbg);
//
//    }

    //用webview代替原布局
    @Override
    protected void initData(View mCurrentView, Bundle savedInstanceState) {
        new_webview = (WebView)mCurrentView.findViewById(R.id.fragment_puretext_webview);
        //获取WebSettings对象
        WebSettings wSettings=new_webview.getSettings();
        wSettings.setJavaScriptEnabled(true);
        //启用触控缩放
        wSettings.setBuiltInZoomControls(true);
        //启用支持视窗meta标记（可实现双击缩放）
        wSettings.setUseWideViewPort(true);
        //以缩略图模式加载页面
        wSettings.setLoadWithOverviewMode(true);
        //启用JavaScript支持
        wSettings.setJavaScriptEnabled(true);
        //设置将接收各种通知和请求的WebViewClient（在WebView加载所有的链接）
        new_webview.setWebViewClient(new WebViewClient());
        new_webview.loadUrl("http://120.27.105.82:8080/MyBirds/index/puretext");
    }

}
