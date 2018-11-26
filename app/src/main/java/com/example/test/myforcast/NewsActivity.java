package com.example.test.myforcast;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.test.myforcast.news.NewsFragment;
import com.example.test.myforcast.news.NewsViewpagerAdapter;

public class NewsActivity extends AppCompatActivity {
    private Toolbar titleBar;

    private ViewPager viewPager;

    private TabLayout tabLayout;

    private NewsViewpagerAdapter adapter;

    public static final int TYPE_TOP=0;
    public static final int TYPE_SHEHUI=1;
    public static final int TYPE_GUONEI=2;
    public static final int TYPE_GUOJI=3;
    public static final int TYPE_YULE=4;
    public static final int TYPE_TIYU=5;
    public static final int TYPE_JUNSHI=6;
    public static final int TYPE_KEJI=7;
    public static final int TYPE_CAIJING=8;
    public static final int TYPE_SHISHANG=9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        //设置状态栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.myStatusBarColor));
        }
        initView();
    }

    private void initView() {
        titleBar = findViewById(R.id.title_bar);
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.back);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewpager);

        adapter = new NewsViewpagerAdapter(getSupportFragmentManager());

        adapter.addFragment(NewsFragment.newInstance(TYPE_TOP), "头条");//top
        adapter.addFragment(NewsFragment.newInstance(TYPE_SHEHUI), "社会");//shehui
        adapter.addFragment(NewsFragment.newInstance(TYPE_GUONEI), "国内");//guonei
        adapter.addFragment(NewsFragment.newInstance(TYPE_GUOJI), "国际");//guoji
        adapter.addFragment(NewsFragment.newInstance(TYPE_YULE), "娱乐");//yule
        adapter.addFragment(NewsFragment.newInstance(TYPE_TIYU), "体育");//tiyu
        adapter.addFragment(NewsFragment.newInstance(TYPE_JUNSHI), "军事");//junshi
        adapter.addFragment(NewsFragment.newInstance(TYPE_KEJI), "科技");//keji
        adapter.addFragment(NewsFragment.newInstance(TYPE_CAIJING), "财经");//caijing
        adapter.addFragment(NewsFragment.newInstance(TYPE_SHISHANG), "时尚");//shishang
        viewPager.setAdapter(adapter);
        //预加载界面页数
//        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
