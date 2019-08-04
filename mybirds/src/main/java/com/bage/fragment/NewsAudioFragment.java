package com.bage.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bage.domain.Event;
import com.bage.mybirds.R;
import com.bage.view.PullToRefreshListView;

import java.util.LinkedList;

public class NewsAudioFragment extends NewsFragment implements AdapterView.OnItemClickListener {

    private LinkedList<Event> mListItems;
    private PullToRefreshListView ptflv_news;

    private WebView new_webview;

    @Override
    View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_news_audio, null);
        return inflater.inflate(R.layout.fragment_news_audio_web, null);
    }

//    @Override
//    protected void initData(View mCurrentView, Bundle savedInstanceState) {
//        System.out.println("NewsAudioFragment *** initData");
//        mCurrentView.setBackgroundResource(R.drawable.newsbg);
//        ptflv_news = (PullToRefreshListView) mCurrentView.findViewById(R.id.ptrlv_audio);
//        ptflv_news.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new GetDataTask().execute();
//            }
//        });
//        mListItems = new LinkedList<Event>();
//        int n = 10;
//        for (int i = 0; i < n; i++) {
//            Event e = new Event(i, i, "这是第"+i+"个问题", "2016-06-0" + i, i, i, i, "这是我的一些描述信息这是我的一些描述信息这是我的一些描述信息" + i, "路径" + i, "eve_pictrueurl" + i, "这是我的一些描述信息这是我的一些描述信息这是我的一些描述信息" + i, "备注" + i);
//            mListItems.add(e);
//        }
//
//        MyAdapter adapter = new MyAdapter();
//
//        ptflv_news.setAdapter(adapter);
//        View footer = getActivity().getLayoutInflater().inflate(R.layout.frnews_lv_footer, null);
//        final TextView tv_loadmore = (TextView) footer.findViewById(R.id.frlvfo_tv_loadmore);
//        footer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tv_loadmore.setText("正在加载。。。");
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getActivity(), "这个功能先不实现。。。", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//        ptflv_news.addFooterView(footer);
//        ptflv_news.setOnItemClickListener(this);
//    }

    //用webview代替原布局
    @Override
    protected void initData(View mCurrentView, Bundle savedInstanceState) {
        new_webview = (WebView)mCurrentView.findViewById(R.id.fragment_audio_webview);
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
        new_webview.loadUrl("http://120.27.105.82:8080/MyBirds/index/audio");
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 因为加上了头部和尾部
        if (position == mListItems.size() + 1) {
            if (mListItems.size() >= 1) {
            }
        } else if (position == 0) {
            Toast.makeText(getActivity(), "这是点击了轮播条。。。", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(getActivity(), "还没有获得录音资源", Toast.LENGTH_SHORT).show();
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mListItems.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.frnews_lv_item, null);
            ImageView iv_audioIcon = (ImageView) view.findViewById(R.id.frlvit_iv_image);
            iv_audioIcon.setImageResource(R.drawable.play);
            TextView tv_audioContent = (TextView) view.findViewById(R.id.frlvit_tv_intro);
            tv_audioContent.setText("这是我最近录的鸟叫声，请大家听一下这是什么鸟");
            TextView tv_title = (TextView) view.findViewById(R.id.frlvit_tv_title);
            TextView tv_time = (TextView) view.findViewById(R.id.frlvit_tv_time);

            TextView tv_intro = (TextView) view.findViewById(R.id.frlvit_tv_intro);
            TextView tv_likenumber = (TextView) view.findViewById(R.id.frlvit_tv_likenumber);
            tv_title.setText(mListItems.get(position).getEve_question());
            tv_time.setText(mListItems.get(position).getEve_time());
            tv_intro.setText(mListItems.get(position).getEve_description());
            tv_likenumber.setText(mListItems.get(position).getEve_id() + " 赞");
            return view;
        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, LinkedList<Event>> {

        @Override
        protected LinkedList<Event> doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mListItems;
        }

        @Override
        protected void onPostExecute(LinkedList<Event> result) {
            Event e = new Event();
            e.setEve_id((int) Math.floor(Math.random() * 10));
            e.setEve_time("2016-03-13 10:26:0" + e.getEve_id());
            mListItems.addFirst(e);

            // Call onRefreshComplete when the list has been refreshed.
            ptflv_news.onRefreshComplete();
            super.onPostExecute(result);
        }
    }

}
