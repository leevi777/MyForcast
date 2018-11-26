package com.example.test.myforcast.news;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.test.myforcast.R;
import com.example.test.myforcast.gson.News;

import java.util.List;

public class NewsRecycleViewAdapter extends RecyclerView.Adapter<NewsViewHolder>{
    //返回一张图的新闻类型
    private final int TYPE_ITEM1 = 0;
    //返回两张图的新闻类型
    private final int TYPE_ITEM2 = 1;
    //返回三张图的新闻类型
    private final int TYPE_ITEM3 = 2;

    private Context context;

    private List<News.Data> dataList;

    public NewsRecycleViewAdapter(Context context, List<News.Data> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM1){
            return new NewsItem1(LayoutInflater.from(context).inflate(R.layout.news_item1, parent, false));
        }else if (viewType == TYPE_ITEM2){
            return new NewsItem2(LayoutInflater.from(context).inflate(R.layout.news_item2, parent, false));
        }
        View view = LayoutInflater.from(context).inflate(R.layout.news_item3, parent, false);
        NewsViewHolder holder = new NewsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, final int position) {
        if (dataList.size()>0) {
            if (holder instanceof NewsItem1) {
                ((NewsItem1) holder).news_title.setText(dataList.get(position).title);
                ((NewsItem1) holder).author_name.setText(dataList.get(position).author_name);
                ((NewsItem1) holder).date.setText(dataList.get(position).date);
                ((NewsItem1) holder).news_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("url", dataList.get(position).url);
                        intent.putExtra("title", dataList.get(position).title);
                        context.startActivity(intent);
                    }
                });
                Glide.with(context).load(dataList.get(position).img1_url).error(R.drawable.error).into(((NewsItem1) holder).img);
            } else if (holder instanceof NewsItem2) {
                ((NewsItem2) holder).news_title.setText(dataList.get(position).title);
                ((NewsItem2) holder).author_name.setText(dataList.get(position).author_name);
                ((NewsItem2) holder).date.setText(dataList.get(position).date);
                ((NewsItem2) holder).news_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("url", dataList.get(position).url);
                        intent.putExtra("title", dataList.get(position).title);
                        context.startActivity(intent);
                    }
                });
                Glide.with(context).load(dataList.get(position).img1_url).error(R.drawable.error).into(((NewsItem2) holder).img1);
                Glide.with(context).load(dataList.get(position).img2_url).error(R.drawable.error).into(((NewsItem2) holder).img2);
            } else if (holder instanceof NewsViewHolder) {
                ((NewsViewHolder) holder).news_title.setText(dataList.get(position).title);
                ((NewsViewHolder) holder).author_name.setText(dataList.get(position).author_name);
                ((NewsViewHolder) holder).date.setText(dataList.get(position).date);
                ((NewsViewHolder) holder).news_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("url", dataList.get(position).url);
                        intent.putExtra("title", dataList.get(position).title);
                        context.startActivity(intent);
                    }
                });
                Glide.with(context).load(dataList.get(position).img1_url).error(R.drawable.error).into(((NewsViewHolder) holder).img1);
                Glide.with(context).load(dataList.get(position).img2_url).error(R.drawable.error).into(((NewsViewHolder) holder).img2);
                Glide.with(context).load(dataList.get(position).img3_url).error(R.drawable.error).into(((NewsViewHolder) holder).img3);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!TextUtils.isEmpty(dataList.get(position).img1_url)
                && TextUtils.isEmpty(dataList.get(position).img2_url)
                && TextUtils.isEmpty(dataList.get(position).img3_url)){
            return TYPE_ITEM1;
        }else if (!TextUtils.isEmpty(dataList.get(position).img1_url)
                && !TextUtils.isEmpty(dataList.get(position).img2_url)
                && TextUtils.isEmpty(dataList.get(position).img3_url)){
            return TYPE_ITEM2;
        }else {
            return TYPE_ITEM3;
        }
    }

    private class NewsItem1 extends NewsViewHolder{
        public LinearLayout news_item;
        public TextView news_title,author_name,date;
        public ImageView img;
        public NewsItem1(View itemView) {
            super(itemView);
            news_item = itemView.findViewById(R.id.news_item1);
            news_title = itemView.findViewById(R.id.news_title1);
            author_name = itemView.findViewById(R.id.author_name1);
            date = itemView.findViewById(R.id.date1);
            img = itemView.findViewById(R.id.news_item1_img1);
        }
    }

    private class NewsItem2 extends NewsViewHolder{
        public LinearLayout news_item;
        public TextView news_title,author_name,date;
        public ImageView img1,img2;
        public NewsItem2(View itemView) {
            super(itemView);
            news_item = itemView.findViewById(R.id.news_item2);
            news_title = itemView.findViewById(R.id.news_title2);
            author_name = itemView.findViewById(R.id.author_name2);
            date = itemView.findViewById(R.id.date2);
            img1 = itemView.findViewById(R.id.news_item2_img1);
            img2 = itemView.findViewById(R.id.news_item2_img2);
        }
    }
}

class NewsViewHolder extends RecyclerView.ViewHolder{
    public LinearLayout news_item;
    public TextView news_title,author_name,date;
    public ImageView img1,img2,img3;
    public NewsViewHolder(View itemView) {
        super(itemView);
        news_item = itemView.findViewById(R.id.news_item3);
        news_title = itemView.findViewById(R.id.news_tile3);
        author_name = itemView.findViewById(R.id.author_name3);
        date = itemView.findViewById(R.id.date3);
        img1 = itemView.findViewById(R.id.news_item3_img1);
        img2 = itemView.findViewById(R.id.news_item3_img2);
        img3 = itemView.findViewById(R.id.news_item3_img3);
    }
}


