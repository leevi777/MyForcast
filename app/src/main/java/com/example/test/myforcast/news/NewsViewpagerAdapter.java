package com.example.test.myforcast.news;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsViewpagerAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragments = new ArrayList<>();

    private List<String> tabTitles = new ArrayList<>();

    public NewsViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * 添加新闻布局碎片和新闻类型标题
     */
    public void addFragment(Fragment fragment,String tabTitle){
        fragments.add(fragment);
        tabTitles.add(tabTitle);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}
