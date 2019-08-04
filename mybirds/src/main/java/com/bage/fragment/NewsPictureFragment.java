package com.bage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bage.domain.Event;
import com.bage.activity.NewsDetailActivity;
import com.bage.mybirds.R;
import com.bage.utils.JsonUtils;
import com.bage.utils.LogUtils;
import com.bage.utils.MyImageLoaderUtils;
import com.bage.utils.UrlUtils;
import com.bage.view.PullToRefreshListView;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NewsPictureFragment extends NewsFragment implements AdapterView.OnItemClickListener {

    private List<Event> mListItems;
    private PullToRefreshListView ptflv_mews;
    private View footer;
    private TextView tv_loadmore;
    private String getPictrueUrl = "";
    private MyAdapter adapter;
    private int currentPage = 1;

    @Override
    View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_picture, null);
    }

    @Override
    protected void initData(View mCurrentView, Bundle savedInstanceState) {

        System.out.println("NewsPictureFragment *** initData");

        mListItems = new ArrayList<Event>();
        getPictrueUrl = UrlUtils.getControllerUrl(getActivity(), "api/event", "getDataOfPictureByPage");
        // 找到对应的组件
        findViews(mCurrentView);
        currentPage = 1;
        // 加载数据
        loadPictrues();
        // 设置下拉刷新
        ptflv_mews.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                currentPage = 1;
                mListItems = new ArrayList<Event>();
                loadPictrues();
            }
        });
        ptflv_mews.setTapVisibility(View.GONE);
        // 给ptflv_mews设置适配器
        adapter = new MyAdapter();
        ptflv_mews.setAdapter(adapter);
        // 将加载更多加入到listView中
        ptflv_mews.addFooterView(footer);
        // 增加监听事件
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_loadmore.setText("正在加载");
                currentPage ++;
                loadPictrues();
            }
        });
        ptflv_mews.setOnItemClickListener(this);

    }

    private void findViews(View mCurrentView) {
        ptflv_mews = (PullToRefreshListView) mCurrentView.findViewById(R.id.frnews_ptflv_mews);
        footer = getActivity().getLayoutInflater().inflate(R.layout.frnews_lv_footer, null);
        tv_loadmore = (TextView) footer.findViewById(R.id.frlvfo_tv_loadmore);
    }

    private void loadPictrues() {
        RequestParams params = new RequestParams();
        params.put("pageNow", "" + currentPage);
        new AsyncHttpClient().post(getActivity(), getPictrueUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                tv_loadmore.setText("点击加载更多");
                if (i == 200) {
                    String res = new String(bytes);
                    List mListItemss = JsonUtils.fromJson(res, new TypeToken<List<Event>>() {
                    }.getType());
                    if (mListItemss != null) {
                        mListItems.addAll(mListItemss);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    LogUtils.shownToast(getActivity(), "加载失败");
                }
                if(ptflv_mews != null ){
                    ptflv_mews.onRefreshComplete();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if(ptflv_mews != null ){
                    ptflv_mews.onRefreshComplete();
                }
                LogUtils.sysoln("请检查网络"+i);
                tv_loadmore.setText("加载更多");
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 因为加上了尾部
        if (position == mListItems.size() + 1) {
            if (mListItems.size() >= 1) {

            }
        } else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), NewsDetailActivity.class);
            // 因为加上了尾部
            Bundle extras = new Bundle();
            extras.putSerializable("event", mListItems.get(position - 1));
            intent.putExtras(extras);
            startActivityForResult(intent, 0);
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

            TextView tv_title = (TextView) view.findViewById(R.id.frlvit_tv_title);
            TextView tv_time = (TextView) view.findViewById(R.id.frlvit_tv_time);
            TextView tv_intro = (TextView) view.findViewById(R.id.frlvit_tv_intro);
            TextView tv_location = (TextView) view.findViewById(R.id.vilation_tv_location);
            TextView tv_likenumber = (TextView) view.findViewById(R.id.frlvit_tv_likenumber);
            ImageView iv_image = (ImageView) view.findViewById(R.id.frlvit_iv_image);
            Event event = mListItems.get(position);
            String url = UrlUtils.getFilePreUrl(getActivity()) + event.getEve_pictureurl();
            LogUtils.sysoln("url:" + url);
            ImageLoader.getInstance().displayImage(url, iv_image, MyImageLoaderUtils.getCarOption());
            tv_title.setText(event.getEve_question());
            String substring = "";
            if(event.getEve_time() != null){
                substring = event.getEve_time().substring(0, event.getEve_time().length() - 5);
            }
            tv_time.setText(substring);
            tv_intro.setText(event.getEve_description());
            tv_likenumber.setText(event.getEve_count() + " 只");
            tv_location.setText(event.getEve_remark() + "");

            return view;
        }
    }
}
