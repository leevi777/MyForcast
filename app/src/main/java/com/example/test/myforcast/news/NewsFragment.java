package com.example.test.myforcast.news;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.test.myforcast.NewsActivity;
import com.example.test.myforcast.R;
import com.example.test.myforcast.gson.News;
import com.example.test.myforcast.util.HttpUtil;
import com.example.test.myforcast.util.Utility;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewsFragment extends Fragment {
    private RecyclerView news_recycle_view;

    private SwipeRefreshLayout new_refresh;

    private LinearLayoutManager manager;

    private List<News.Data> dataList;

    private int news_type;

    private NewsRecycleViewAdapter adapter;

    private boolean ifRefresh = false;

    public static NewsFragment newInstance(int type){
        Bundle bundle = new Bundle();
        NewsFragment fragment = new NewsFragment();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news,null);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        news_type = getArguments().getInt("type");
        news_recycle_view = view.findViewById(R.id.news_recycle_view);
        new_refresh = view.findViewById(R.id.new_refresh);
        new_refresh.setColorSchemeResources(R.color.myStatusBarColor);
        new_refresh.setRefreshing(true);
        new_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ifRefresh = true;
                showNews();
            }
        });

        showNews();
    }

    public void showNews() {
        switch (news_type){
            case NewsActivity.TYPE_TOP:
                if (!getCache("top"))
                    requestNews("top");
                break;
            case NewsActivity.TYPE_SHEHUI:
                if (!getCache("shehui"))
                    requestNews("shehui");
                break;
            case NewsActivity.TYPE_GUONEI:
                if (!getCache("guonei"))
                    requestNews("guonei");
                break;
            case NewsActivity.TYPE_GUOJI:
                if (!getCache("guoji"))
                    requestNews("guoji");
                break;
            case NewsActivity.TYPE_YULE:
                if (!getCache("yule"))
                    requestNews("yule");
                break;
            case NewsActivity.TYPE_TIYU:
                if (!getCache("tiyu"))
                    requestNews("tiyu");
                break;
            case NewsActivity.TYPE_JUNSHI:
                if (!getCache("junshi"))
                    requestNews("junshi");
                break;
            case NewsActivity.TYPE_KEJI:
                if (!getCache("keji"))
                    requestNews("keji");
                break;
            case NewsActivity.TYPE_CAIJING:
                if (!getCache("caijing"))
                    requestNews("caijing");
                break;
            case NewsActivity.TYPE_SHISHANG:
                if (!getCache("shishang"))
                    requestNews("shishang");
                break;
        }
    }

    /**
     * 从缓存区拿取新闻数据
     */
    private boolean getCache(String type){
        if (ifRefresh)
            return false;
        Log.i("TAG", "=========拿取缓存数据=======");
        String data = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(type,null);
        if (data !=null){
            News news = Utility.handleNewsResponse(data);
            dataList = news.result.dataList;
            setData();
            new_refresh.setRefreshing(false);
            return true;
        }
        return false;
    }

    private void setData(){
        adapter = new NewsRecycleViewAdapter(getActivity(),dataList);
        manager = new LinearLayoutManager(getActivity());
        news_recycle_view.setLayoutManager(manager);
        news_recycle_view.setAdapter(adapter);
    }

    private void requestNews(final String type){
        String url = "http://v.juhe.cn/toutiao/index?type="+type+"&key=99be233f8cf208b2a43a78a02f66d871";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"获取新闻数据失败",Toast.LENGTH_SHORT).show();
                        new_refresh.setRefreshing(false);
                        ifRefresh = false;
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String gson = response.body().string();
                Log.i("TAG", gson);
                News news = Utility.handleNewsResponse(gson);
                if (news.code == 0 && news != null) {
                    if (dataList.size() >0){
                        dataList.clear();
                        dataList.addAll(news.result.dataList);
                    }else {
                        dataList = news.result.dataList;
                    }
                    for (News.Data data : dataList){
                        Log.i("TAG","标题："+data.title+"\n"+data.author_name+"\n"+data.url+"\n"+data
                                .img1_url+ "\n"+data.img2_url+"\n"+data.img3_url);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (ifRefresh){
                                Log.i("TAG", "通知适配器改变数据");
                                adapter.notifyDataSetChanged();
                            }else {
                                Log.i("TAG", "设置请求的数据");
                                setData();
                            }
                            ifRefresh = false;
                            new_refresh.setRefreshing(false);
                        }
                    });
                    saveNewsData(news_type,gson);
                }else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"获取新闻数据失败",Toast.LENGTH_SHORT).show();
                            new_refresh.setRefreshing(false);
                            ifRefresh = false;
                        }
                    });
                }

            }
        });
    }

    private void saveNewsData(int type,String data){
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(getActivity()).edit();
        switch (type){
            case NewsActivity.TYPE_TOP:
                editor.putString("top",data);
                break;
            case NewsActivity.TYPE_SHEHUI:
                editor.putString("shehui",data);
                break;
            case NewsActivity.TYPE_GUONEI:
                editor.putString("guonei",data);
                break;
            case NewsActivity.TYPE_GUOJI:
                editor.putString("guoji",data);
                break;
            case NewsActivity.TYPE_YULE:
                editor.putString("yule",data);
                break;
            case NewsActivity.TYPE_TIYU:
                editor.putString("tiyu",data);
                break;
            case NewsActivity.TYPE_JUNSHI:
                editor.putString("junshi",data);
                break;
            case NewsActivity.TYPE_KEJI:
                editor.putString("keji",data);
                break;
            case NewsActivity.TYPE_CAIJING:
                editor.putString("caijing",data);
                break;
            case NewsActivity.TYPE_SHISHANG:
                editor.putString("shishang",data);
                break;
        }
        editor.apply();
    }

}
