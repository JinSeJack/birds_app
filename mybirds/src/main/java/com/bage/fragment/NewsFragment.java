package com.bage.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bage.mybirds.R;
import com.bage.view.PagingEnabledViewPager;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends BirdsFragment {

    private List<NewsFragment> newsFragments;
    private PagingEnabledViewPager pager;
    //private TabPageIndicator indicator;
    BottomNavigationView bottomNavigationView;
    private int current = 0;

    @Override
    View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, null);
    }

    @Override
    protected void initData(View mCurrentView, Bundle savedInstanceState) {

        System.out.println("current:" + current);

        // 找到对应的组件
        findViews(mCurrentView);
        // 初始化viewpagers
        initViewPagers();
        // 设置禁止滑动
        pager.setPagingEnabled(false);
        // 设置适配器
        FragmentPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        // 将indicator于viewpager绑定
        //indicator.setViewPager(pager);
        if (bottomNavigationView != null) {
            bottomNavigationView.isWithText(true);
            bottomNavigationView.isColoredBackground(true);
            bottomNavigationView.setItemActiveColorWithoutColoredBackground(getResources().getColor(R.color.fourthColor));
        }

        BottomNavigationItem bottomNavigationItem = new BottomNavigationItem
                ("图片", getResources().getColor(R.color.baseColor), R.drawable.ic_favorite_black_24dp);
        BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem
                ("音频", getResources().getColor(R.color.baseColor), R.drawable.ic_mic_black_24dp);
        BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem
                ("文本", getResources().getColor(R.color.baseColor), R.drawable.ic_book_black_24dp);
        bottomNavigationView.addTab(bottomNavigationItem);
        bottomNavigationView.addTab(bottomNavigationItem1);
        bottomNavigationView.addTab(bottomNavigationItem2);
        bottomNavigationView.setOnBottomNavigationItemClickListener(new BottomNavigationView.OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                current = index;
                System.out.println("index:" + index);
                pager.setCurrentItem(index);
            }
        });
    }

    private void initViewPagers() {
        newsFragments = new ArrayList<>();
        newsFragments.add(new NewsPictureFragment());
        newsFragments.add(new NewsAudioFragment());
        newsFragments.add(new NewsTextFragment());
    }

    private void findViews(View mCurrentView) {
        pager = (PagingEnabledViewPager) mCurrentView.findViewById(R.id.pager);
        //indicator = (TabPageIndicator) mCurrentView.findViewById(R.id.indicator);
        bottomNavigationView = (BottomNavigationView) mCurrentView.findViewById(R.id.bottomNavigation);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return newsFragments.get(position);
        }

        @Override
        public int getCount() {
            return newsFragments.size();
        }
    }

}
