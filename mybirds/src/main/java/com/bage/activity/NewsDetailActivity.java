package com.bage.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bage.domain.Comment;
import com.bage.domain.Event;
import com.bage.mybirds.R;
import com.bage.utils.LogUtils;
import com.bage.utils.MyImageLoaderUtils;
import com.bage.utils.UrlUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * Description:<br>
 * This is a description of the class
 * <br>
 * User: bage<br>
 * Date: 2015-10-17 <br>
 * Copyright @ 2015 www.bage.com<br>
 */
public class NewsDetailActivity extends Activity implements View.OnClickListener {

    private TextView tv_title;
    private TextView tv_time;
    private EditText et_comment;
    private TextView tv_content;
    private TextView tv_loadmore;
    private TextView tv_collectnumber;
    private TextView tv_likenumber;
    private TextView tv_unlikenumber;
    private TextView tv_sharenumber;
    private TextView tv_remark;
    private LayoutInflater layoutInflater;
    private ImageView iv_img;
    private ListView lv_contents;
    private View header;
    private View footer;

    private List<Comment> listComments;
    private MyAdapter myAdapter;
    private Event event;
    private View tv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);

        event = (Event) getIntent().getSerializableExtra("event");
        // 查找组件
        tv_title = (TextView) findViewById(R.id.acnede_tv_title);
        tv_back = (View) findViewById(R.id.acnede_tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_contents = (ListView) findViewById(R.id.acnede_lv_contents);
        et_comment = (EditText) findViewById(R.id.acnede_et_comment);
        layoutInflater = getLayoutInflater();

        header = layoutInflater.inflate(R.layout.acnede_lv_header, null);
        iv_img = (ImageView) header.findViewById(R.id.aclvhe_iv_img);

        tv_time = (TextView) header.findViewById(R.id.acnede_tv_time);
        tv_collectnumber = (TextView) header.findViewById(R.id.aclvhe_tv_collectnumber);
        tv_likenumber = (TextView) header.findViewById(R.id.aclvhe_tv_likenumber);
        tv_unlikenumber = (TextView) header.findViewById(R.id.aclvhe_tv_unlikenumber);
        tv_sharenumber = (TextView) header.findViewById(R.id.aclvhe_tv_sharenumber);
        tv_remark = (TextView) header.findViewById(R.id.aclvhe_tv_remark);

        tv_content = (TextView) header.findViewById(R.id.aclvhe_tv_content);
        footer = layoutInflater.inflate(R.layout.acnede_lv_footer, null);
        tv_loadmore = (TextView) footer.findViewById(R.id.aclvfo_tv_loadmore);
        lv_contents.addFooterView(footer);
        lv_contents.addHeaderView(header);
        iv_img = (ImageView) header.findViewById(R.id.aclvhe_iv_img);
        tv_content = (TextView) header.findViewById(R.id.aclvhe_tv_content);
        tv_loadmore = (TextView) footer.findViewById(R.id.aclvfo_tv_loadmore);


        tv_loadmore.setOnClickListener(NewsDetailActivity.this);

        listComments = new ArrayList<Comment>();

        showNewsDetail();
        //loadComments(0);

    }

    private void loadComments(int from_id) {

        Toast.makeText(NewsDetailActivity.this, "现在先不实现", Toast.LENGTH_SHORT).show();

    }


    private void showNewsDetail() {

        String url = UrlUtils.getFilePreUrl(this) + event.getEve_pictureurl();
        LogUtils.sysoln("url:" + url);
        ImageLoader.getInstance().displayImage(url, iv_img, MyImageLoaderUtils.getCarOption());
        tv_title.setText(event.getEve_question());
        tv_content.setText(event.getEve_description());
        tv_time.setText(event.getEve_time().substring(0, event.getEve_time().length() - 5));
        tv_likenumber.setText("赞：" + event.getEve_positivecount());
        myAdapter = new MyAdapter();
        lv_contents.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

    }


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listComments.size();
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
            Comment temp = listComments.get(position);

            View view = getLayoutInflater().inflate(R.layout.acnede_lv_item, null);

            TextView tv_address = (TextView) view.findViewById(R.id.aclvit_tv_address);
            tv_address.setText(temp.getCom_answer());

            TextView tv_bad = (TextView) view.findViewById(R.id.aclvit_tv_bad);
            tv_bad.setText("点菜数：" + temp.getCom_negativecount());

            TextView tv_content = (TextView) view.findViewById(R.id.aclvit_tv_content);
            tv_content.setText("\t" + temp.getCom_content());

            TextView tv_good = (TextView) view.findViewById(R.id.aclvit_tv_good);
            tv_good.setText("点赞数：" + temp.getCom_positivecount());

            TextView tv_time = (TextView) view.findViewById(R.id.aclvit_tv_time);
            tv_time.setText(temp.getCom_time());

            TextView tv_username = (TextView) view.findViewById(R.id.aclvit_tv_username);
            tv_username.setText("userid" + temp.getUse_id());

            return view;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aclvfo_tv_loadmore:
                loadmore();
                break;
        }
    }
    public void mySend(View view) {
        Toast.makeText(NewsDetailActivity.this, "你点击了发表按钮", Toast.LENGTH_SHORT).show();

    }

    private void loadmore() {
        Toast.makeText(NewsDetailActivity.this, "你点击了加载更多", Toast.LENGTH_SHORT).show();
    }
}

