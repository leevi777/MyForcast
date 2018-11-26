package com.example.test.myforcast;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.test.myforcast.gson.HeWeather;
import com.example.test.myforcast.gson.WeekWeather;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Activity context;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_WEEK_ITEM = 1;
    private static final int TYPE_WEATHER_ITEM = 2;
    private static final int TYPE_SUGGEST_ITEM = 3;

    private PopupWindow popupWindow;

    private List<HeWeather.Weather> weatherList;

    private List<WeekWeather> weekWeatherList;

    public RecyclerViewAdapter(Context context, List<HeWeather.Weather> weather, List<WeekWeather> weekWeather) {
        this.context = (Activity) context;
        this.weatherList = weather;
        this.weekWeatherList = weekWeather;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycleview_header, parent, false);
            HeaderHolder headerHolder = new HeaderHolder(view);
            headerHolder.goto_news.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, NewsActivity.class);
                    context.startActivity(intent);
                }
            });
            return headerHolder;
        } else if (viewType == TYPE_WEATHER_ITEM) {
            return new WeatherItem(LayoutInflater.from(context).inflate(R.layout.recycleview_weather_item, parent, false));
        } else if (viewType == TYPE_SUGGEST_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycleview_suggest_item, parent, false);
            SuggestItem suggestItem = new SuggestItem(view);
            setOnClick(suggestItem.suggest_item1,parent);
            setOnClick(suggestItem.suggest_item2,parent);
            setOnClick(suggestItem.suggest_item3,parent);
            setOnClick(suggestItem.suggest_item4,parent);
            setOnClick(suggestItem.suggest_item5,parent);
            setOnClick(suggestItem.suggest_item6,parent);
            setOnClick(suggestItem.suggest_item7,parent);
            return suggestItem;
        }
        View view = LayoutInflater.from(context).inflate(R.layout.recycleview_week_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //头布局
        if (weekWeatherList.size() > 0 && weatherList.size() > 0) {
            if (holder instanceof HeaderHolder) {
                Log.i("TAG","=====进入头布局====");
                ((HeaderHolder) holder).update.setText("更新于 "+weatherList.get(0).update.updateTime.split(" ")[1]);
                ((HeaderHolder) holder).temp.setText(weatherList.get(0).now.temperature + "°");
                ((HeaderHolder) holder).brf.setText(weatherList.get(0).now.info);
                String airType = weatherList.get(0).suggestionList.get(7).brief;
                ((HeaderHolder) holder).air.setText(airType);
                setAirQualityColor(((HeaderHolder) holder).air, airType);
                ((HeaderHolder) holder).wind_deg.setText(weatherList.get(0).now.windDeg + "级");
                ((HeaderHolder) holder).wind_dir.setText(weatherList.get(0).now.windDir);
                int time = Integer.valueOf(weatherList.get(0).update.updateTime.split(" ")[1].split(":")[0]);
                setImg(((HeaderHolder) holder).icon, weatherList.get(0).now.info);
                if (time > 6 && time <18) {
                    ((HeaderHolder) holder).brf1.setText(weatherList.get(0).forecastList.get(1).dayInfo);
                    ((HeaderHolder) holder).brf2.setText(weatherList.get(0).forecastList.get(2).dayInfo);
                    setImg(((HeaderHolder) holder).icon1, weatherList.get(0).forecastList.get(1).dayInfo);
                    setImg(((HeaderHolder) holder).icon2, weatherList.get(0).forecastList.get(2).dayInfo);
                }else {
                    ((HeaderHolder) holder).brf1.setText(weatherList.get(0).forecastList.get(1).nightInfo);
                    ((HeaderHolder) holder).brf2.setText(weatherList.get(0).forecastList.get(2).nightInfo);
                    setImg(((HeaderHolder) holder).icon1, weatherList.get(0).forecastList.get(1).nightInfo);
                    setImg(((HeaderHolder) holder).icon2, weatherList.get(0).forecastList.get(2).nightInfo);
                }
                ((HeaderHolder) holder).temp1.setText(weatherList.get(0).forecastList.get(1).temp_min + "~"
                        + weatherList.get(0).forecastList.get(1).temp_max + "℃");
                ((HeaderHolder) holder).temp2.setText(weatherList.get(0).forecastList.get(2).temp_min + "~"
                        + weatherList.get(0).forecastList.get(2).temp_max + "℃");
                ((HeaderHolder) holder).wind_dir1.setText(weatherList.get(0).forecastList.get(1).wind_dir);
                ((HeaderHolder) holder).wind_dir2.setText(weatherList.get(0).forecastList.get(2).wind_dir);
            } else if (holder instanceof WeatherItem) {
                //天气基本布局
                Log.i("TAG","=====进入基本天气布局====");
                ((WeatherItem) holder).sunrise.setText(weatherList.get(0).forecastList.get(0).sunrise);
                ((WeatherItem) holder).sunset.setText(weatherList.get(0).forecastList.get(0).sunset);
                ((WeatherItem) holder).moonrise.setText(weatherList.get(0).forecastList.get(0).moonrise);
                ((WeatherItem) holder).moonset.setText(weatherList.get(0).forecastList.get(0).moonset);
                ((WeatherItem) holder).temp.setText(weatherList.get(0).now.temperature + "°");
                ((WeatherItem) holder).humidity.setText(weatherList.get(0).now.humidity + "%");
                ((WeatherItem) holder).pressure.setText(weatherList.get(0).now.pressure + "hpa");
                ((WeatherItem) holder).visibility.setText(weatherList.get(0).now.visibility + "km");
            } else if (holder instanceof MyViewHolder) {//周布局
                if (position == 2) {
                    Log.i("TAG","=====进入周布局====");
                    holder.week_day.setText("昨天");
                    holder.week_temp.setText(weekWeatherList.get(0).data.yesterday.temp_min.split(" ")[1]
                            + "~" + weekWeatherList.get(0).data.yesterday.temp_max.split(" ")[1]);
                    holder.week_weather.setText(weekWeatherList.get(0).data.yesterday.type);
                    holder.week_wind_dir.setText(weekWeatherList.get(0).data.yesterday.wind_dir);
                    holder.week_weather.setText(weekWeatherList.get(0).data.yesterday.type);
                    setImg(holder.week_img,weekWeatherList.get(0).data.yesterday.type);
                } else if (position == 3) {
                    holder.week_day.setText("今天");
                    holder.week_weather.setText(weekWeatherList.get(0).data.forecastList.get(position - 3).type);
                    holder.week_weather.setText(weekWeatherList.get(0).data.forecastList.get(position - 3).type);
                    holder.week_temp.setText(weekWeatherList.get(0).data.forecastList.get(position).temp_min.split(" ")[1] + "~"
                            + weekWeatherList.get(0).data.forecastList.get(position).temp_max.split(" ")[1]);
                    holder.week_wind_dir.setText(weekWeatherList.get(0).data.forecastList.get(position - 3).wind_dir);
                    setImg(holder.week_img,weekWeatherList.get(0).data.forecastList.get(position-3).type);
                } else if (position < 8) {
                    setImg(holder.week_img,weekWeatherList.get(0).data.forecastList.get(position-3).type);
                    holder.week_day.setText(weekWeatherList.get(0).data.forecastList.get(position - 3).date.split("日")[1]);
                    holder.week_weather.setText(weekWeatherList.get(0).data.forecastList.get(position - 3).type);
                    holder.week_temp.setText(weekWeatherList.get(0).data.forecastList.get(position - 3).temp_min.split(" ")[1] + "~"
                            + weekWeatherList.get(0).data.forecastList.get(position - 3).temp_max.split(" ")[1]);
                    holder.week_wind_dir.setText(weekWeatherList.get(0).data.forecastList.get(position - 3).wind_dir);
                }

            }
            if (holder instanceof SuggestItem) {
                //建议布局
                Log.i("TAG","=====进入底布局====");
                ((SuggestItem) holder).info.setText(weatherList.get(0).suggestionList.get(0).info);
                ((SuggestItem) holder).brf1.setText(weatherList.get(0).suggestionList.get(0).brief);
                ((SuggestItem) holder).brf2.setText(weatherList.get(0).suggestionList.get(1).brief);
                ((SuggestItem) holder).brf3.setText(weatherList.get(0).suggestionList.get(2).brief);
                ((SuggestItem) holder).brf4.setText(weatherList.get(0).suggestionList.get(3).brief);
                ((SuggestItem) holder).brf5.setText(weatherList.get(0).suggestionList.get(4).brief);
                ((SuggestItem) holder).brf6.setText(weatherList.get(0).suggestionList.get(5).brief);
                ((SuggestItem) holder).brf7.setText(weatherList.get(0).suggestionList.get(6).brief);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == 1) {
            return TYPE_WEATHER_ITEM;
        } else if (position == 8) {
            return TYPE_SUGGEST_ITEM;
        } else {
            return TYPE_WEEK_ITEM;
        }
    }

    private class HeaderHolder extends MyViewHolder {
        public TextView update,air, temp, brf, wind_deg, wind_dir, wind_dir1, wind_dir2, temp1, temp2, brf1, brf2, goto_news;
        public ImageView icon,icon1, icon2;

        public HeaderHolder(View itemView) {
            super(itemView);
            update = itemView.findViewById(R.id.update);
            air = itemView.findViewById(R.id.air);
            temp = itemView.findViewById(R.id.temp);
            brf = itemView.findViewById(R.id.brf);
            temp = itemView.findViewById(R.id.temp);
            wind_deg = itemView.findViewById(R.id.wind_deg);
            wind_dir = itemView.findViewById(R.id.wind_dir);
            wind_dir1 = itemView.findViewById(R.id.wind_dir1);
            wind_dir2 = itemView.findViewById(R.id.wind_dir2);
            temp1 = itemView.findViewById(R.id.temp1);
            temp2 = itemView.findViewById(R.id.temp2);
            brf1 = itemView.findViewById(R.id.brf1);
            brf2 = itemView.findViewById(R.id.brf2);
            goto_news = itemView.findViewById(R.id.goto_news);
            icon = itemView.findViewById(R.id.main_icon);
            icon1 = itemView.findViewById(R.id.icon1);
            icon2 = itemView.findViewById(R.id.icon2);
        }
    }

    private class WeatherItem extends MyViewHolder {
        public TextView sunrise, sunset, moonrise, moonset, temp, humidity, pressure, visibility;

        public WeatherItem(View itemView) {
            super(itemView);
            sunrise = itemView.findViewById(R.id.sunrise);
            sunset = itemView.findViewById(R.id.sunset);
            moonrise = itemView.findViewById(R.id.moonrise);
            moonset = itemView.findViewById(R.id.moonset);
            temp = itemView.findViewById(R.id.temp);
            humidity = itemView.findViewById(R.id.humidity);
            pressure = itemView.findViewById(R.id.pressure);
            visibility = itemView.findViewById(R.id.visibility);
        }
    }

    private class SuggestItem extends MyViewHolder {
        private LinearLayout suggest_item1, suggest_item2, suggest_item3, suggest_item4, suggest_item5, suggest_item6, suggest_item7;
        private TextView info, brf1, brf2, brf3, brf4, brf5, brf6, brf7;
        public SuggestItem(View itemView) {
            super(itemView);
            suggest_item1 = itemView.findViewById(R.id.suggest_item1);
            suggest_item2 = itemView.findViewById(R.id.suggest_item2);
            suggest_item3 = itemView.findViewById(R.id.suggest_item3);
            suggest_item4 = itemView.findViewById(R.id.suggest_item4);
            suggest_item5 = itemView.findViewById(R.id.suggest_item5);
            suggest_item6 = itemView.findViewById(R.id.suggest_item6);
            suggest_item7 = itemView.findViewById(R.id.suggest_item7);
            info = itemView.findViewById(R.id.dress_info);
            brf1 = itemView.findViewById(R.id.brf1);
            brf2 = itemView.findViewById(R.id.brf2);
            brf3 = itemView.findViewById(R.id.brf3);
            brf4 = itemView.findViewById(R.id.brf4);
            brf5 = itemView.findViewById(R.id.brf5);
            brf6 = itemView.findViewById(R.id.brf6);
            brf7 = itemView.findViewById(R.id.brf7);
        }
    }

    /**
     * 根据空气质量改变背景标签颜色
     */
    private void setAirQualityColor(TextView tv, String type) {
        if (type.equals("优")) {
            tv.setBackgroundResource(R.drawable.air1);
        } else if (type.equals("良")) {
            tv.setBackgroundResource(R.drawable.air2);
        } else if (type.equals("中")) {
            tv.setBackgroundResource(R.drawable.air3);
        } else if (type.equals("较差")) {
            tv.setBackgroundResource(R.drawable.air4);
        } else if (type.equals("重")) {
            tv.setBackgroundResource(R.drawable.air5);
        } else if (type.equals("严重")) {
            tv.setBackgroundResource(R.drawable.air6);
        }
    }


    /**
     * 设置天气图标
     */
    private void setImg(ImageView iv,String type){
        if (type.equals("晴")){
            iv.setImageResource(R.drawable.w100);
        }else if (type.equals("多云")){
            iv.setImageResource(R.drawable.w101);
        }else if (type.equals("少云")){
            iv.setImageResource(R.drawable.w102);
        }else if (type.equals("晴间多云")){
            iv.setImageResource(R.drawable.w103);
        }else if (type.equals("阴")){
            iv.setImageResource(R.drawable.w104);
        }else if (type.equals("有风")){
            iv.setImageResource(R.drawable.w200);
        }else if (type.equals("平静")){
            iv.setImageResource(R.drawable.w201);
        }else if (type.equals("微风")){
            iv.setImageResource(R.drawable.w202);
        }else if (type.equals("和风")){
            iv.setImageResource(R.drawable.w203);
        }else if (type.equals("清风")){
            iv.setImageResource(R.drawable.w204);
        }else if (type.equals("强风")||type.equals("劲风")){
            iv.setImageResource(R.drawable.w205);
        }else if (type.equals("疾风")){
            iv.setImageResource(R.drawable.w206);
        }else if (type.equals("大风")){
            iv.setImageResource(R.drawable.w207);
        }else if (type.equals("烈风")){
            iv.setImageResource(R.drawable.w208);
        }else if (type.equals("风暴")){
            iv.setImageResource(R.drawable.w209);
        }else if (type.equals("狂爆风")){
            iv.setImageResource(R.drawable.w210);
        }else if (type.equals("飓风")){
            iv.setImageResource(R.drawable.w211);
        }else if (type.equals("龙卷风")){
            iv.setImageResource(R.drawable.w212);
        }else if (type.equals("热带风暴")){
            iv.setImageResource(R.drawable.w213);
        }else if (type.equals("阵雨")){
            iv.setImageResource(R.drawable.w300);
        }else if (type.equals("强阵雨")){
            iv.setImageResource(R.drawable.w301);
        }else if (type.equals("雷阵雨")){
            iv.setImageResource(R.drawable.w302);
        }else if (type.equals("强雷阵雨")){
            iv.setImageResource(R.drawable.w303);
        }else if (type.equals("雷阵雨伴有冰雹")){
            iv.setImageResource(R.drawable.w304);
        }else if (type.equals("小雨")){
            iv.setImageResource(R.drawable.w305);
        }else if (type.equals("中雨")){
            iv.setImageResource(R.drawable.w306);
        }else if (type.equals("大雨")){
            iv.setImageResource(R.drawable.w307);
        }else if (type.equals("极端降雨")){
            iv.setImageResource(R.drawable.w308);
        }else if (type.equals("毛毛雨")||type.equals("细雨")){
            iv.setImageResource(R.drawable.w309);
        }else if (type.equals("暴雨")){
            iv.setImageResource(R.drawable.w310);
        }else if (type.equals("大暴雨")){
            iv.setImageResource(R.drawable.w311);
        }else if (type.equals("特大暴雨")){
            iv.setImageResource(R.drawable.w312);
        }else if (type.equals("冻雨")){
            iv.setImageResource(R.drawable.w313);
        }else if (type.equals("小到中雨")){
            iv.setImageResource(R.drawable.w314);
        }else if (type.equals("中到大雨")){
            iv.setImageResource(R.drawable.w315);
        }else if (type.equals("大到暴雨")){
            iv.setImageResource(R.drawable.w316);
        }else if (type.equals("暴雨到大暴雨")){
            iv.setImageResource(R.drawable.w317);
        }else if (type.equals("大暴雨到特大暴雨")){
            iv.setImageResource(R.drawable.w318);
        }else if (type.equals("雨")){
            iv.setImageResource(R.drawable.w309);
        }else if (type.equals("小雪")){
            iv.setImageResource(R.drawable.w400);
        }else if (type.equals("中雪")){
            iv.setImageResource(R.drawable.w401);
        }else if (type.equals("大雪")){
            iv.setImageResource(R.drawable.w402);
        }else if (type.equals("暴雪")){
            iv.setImageResource(R.drawable.w403);
        }else if (type.equals("雨夹雪")){
            iv.setImageResource(R.drawable.w404);
        }else if (type.equals("雨雪天气")){
            iv.setImageResource(R.drawable.w405);
        }else if (type.equals("阵雨夹雪")){
            iv.setImageResource(R.drawable.w406);
        }else if (type.equals("阵雪")){
            iv.setImageResource(R.drawable.w407);
        }else if (type.equals("小到中雪")){
            iv.setImageResource(R.drawable.w408);
        }else if (type.equals("中到大雪")){
            iv.setImageResource(R.drawable.w409);
        }else if (type.equals("大到暴雪")){
            iv.setImageResource(R.drawable.w410);
        }else if (type.equals("雪")){
            iv.setImageResource(R.drawable.w499);
        }else if (type.equals("薄雾")){
            iv.setImageResource(R.drawable.w500);
        }else if (type.equals("雾")){
            iv.setImageResource(R.drawable.w501);
        }else if (type.equals("霾")){
            iv.setImageResource(R.drawable.w502);
        }else if (type.equals("扬沙")){
            iv.setImageResource(R.drawable.w503);
        }else if (type.equals("浮尘")){
            iv.setImageResource(R.drawable.w504);
        }else if (type.equals("沙尘暴")){
            iv.setImageResource(R.drawable.w507);
        }else if (type.equals("强沙尘暴")){
            iv.setImageResource(R.drawable.w508);
        }else if (type.equals("浓雾")){
            iv.setImageResource(R.drawable.w509);
        }else if (type.equals("强浓雾")){
            iv.setImageResource(R.drawable.w510);
        }else if (type.equals("中度霾")){
            iv.setImageResource(R.drawable.w511);
        }else if (type.equals("重度霾")){
            iv.setImageResource(R.drawable.w512);
        }else if (type.equals("严重霾")){
            iv.setImageResource(R.drawable.w513);
        }else if (type.equals("大雾")){
            iv.setImageResource(R.drawable.w514);
        }else if (type.equals("特强浓雾")){
            iv.setImageResource(R.drawable.w515);
        }else if (type.equals("热")){
            iv.setImageResource(R.drawable.w900);
        }else if (type.equals("冷")){
            iv.setImageResource(R.drawable.w901);
        }else {
            iv.setImageResource(R.drawable.w999);
        }


    }



    /**
     * 绑定按键设置点击事件
     */
    private void setOnClick(View view, final ViewGroup parent) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("=======按键被点击=======");
                View v = LayoutInflater.from(context).inflate(R.layout.suggest_layout, null);
                setPopupWindow(view,v);
                popupWindow = new PopupWindow(v, WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setAnimationStyle(R.style.mypop);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setTouchable(true);
                setBackgroundAlpha(0.3f);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        setBackgroundAlpha(1f);
                    }
                });
                popupWindow.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 0);
            }
        });
    }

    /**
     * 设置popupWindow的数据
     */
    private void setPopupWindow(View view1,View view2) {
        TextView suggestData = view2.findViewById(R.id.suggest_data);
        TextView suggestInfo = view2.findViewById(R.id.suggest_info);
        TextView suggestBrf = view2.findViewById(R.id.suggest_brf);
        ImageView suggestIcon = view2.findViewById(R.id.suggest_icon);
        switch (view1.getId()) {
            case R.id.suggest_item1:
                System.out.println("======设置属性1=====");
                suggestData.setText(weatherList.get(0).forecastList.get(0).temp_min + "℃~" +
                        weatherList.get(0).forecastList.get(0).temp_max + "℃\n" +
                        "今天 " + weatherList.get(0).now.info + "\n" +
                        weatherList.get(0).now.windDir + weatherList.get(0).now.windDeg + "级\n" +
                        "空气质量" + weatherList.get(0).suggestionList.get(7).brief);
                suggestInfo.setText(weatherList.get(0).suggestionList.get(0).info);
                suggestBrf.setText(weatherList.get(0).suggestionList.get(0).brief);
                suggestIcon.setImageResource(R.drawable.comf);
                break;
            case R.id.suggest_item2:
                System.out.println("======设置属性2=====");
                suggestData.setText(weatherList.get(0).forecastList.get(0).temp_min + "℃~" +
                        weatherList.get(0).forecastList.get(0).temp_max + "℃\n" +
                        "今天 " + weatherList.get(0).now.info + "\n" +
                        "空气质量" + weatherList.get(0).suggestionList.get(7).brief);
                suggestInfo.setText(weatherList.get(0).suggestionList.get(1).info);
                suggestBrf.setText(weatherList.get(0).suggestionList.get(1).brief);
                suggestIcon.setImageResource(R.drawable.dress);
                break;
            case R.id.suggest_item3:
                System.out.println("======设置属性3=====");
                suggestData.setText(weatherList.get(0).forecastList.get(0).temp_min + "℃~" +
                        weatherList.get(0).forecastList.get(0).temp_max + "℃\n" +
                        "湿度" + weatherList.get(0).now.humidity + "%\n" +
                        weatherList.get(0).now.windDir + weatherList.get(0).now.windDeg + "级\n" +
                        "空气质量" + weatherList.get(0).suggestionList.get(7).brief);
                suggestInfo.setText(weatherList.get(0).suggestionList.get(2).info);
                suggestBrf.setText(weatherList.get(0).suggestionList.get(2).brief);
                suggestIcon.setImageResource(R.drawable.flu);
                break;
            case R.id.suggest_item4:
                System.out.println("======设置属性4=====");
                suggestData.setText(weatherList.get(0).forecastList.get(0).temp_min + "℃~" +
                        weatherList.get(0).forecastList.get(0).temp_max + "℃\n" +
                        "气压" + weatherList.get(0).now.pressure + "hpa\n" +
                        weatherList.get(0).now.windDir + weatherList.get(0).now.windDeg + "级\n" +
                        "空气质量" + weatherList.get(0).suggestionList.get(7).brief);
                suggestInfo.setText(weatherList.get(0).suggestionList.get(3).info);
                suggestBrf.setText(weatherList.get(0).suggestionList.get(3).brief);
                suggestIcon.setImageResource(R.drawable.sport);
                break;
            case R.id.suggest_item5:
                System.out.println("======设置属性5=====");
                suggestData.setText(weatherList.get(0).forecastList.get(0).temp_min + "℃~" +
                        weatherList.get(0).forecastList.get(0).temp_max + "℃\n" +
                        "湿度" + weatherList.get(0).now.humidity + "%\n" +
                        weatherList.get(0).now.windDir + weatherList.get(0).now.windDeg + "级\n" +
                        "空气质量" + weatherList.get(0).suggestionList.get(7).brief);
                suggestInfo.setText(weatherList.get(0).suggestionList.get(4).info);
                suggestBrf.setText(weatherList.get(0).suggestionList.get(4).brief);
                suggestIcon.setImageResource(R.drawable.travel);
                break;
            case R.id.suggest_item6:
                System.out.println("======设置属性6=====");
                suggestData.setText(weatherList.get(0).forecastList.get(0).temp_min + "℃~" +
                        weatherList.get(0).forecastList.get(0).temp_max + "℃\n" +
                        "今天 " + weatherList.get(0).now.info + "\n" +
                        weatherList.get(0).now.windDir + weatherList.get(0).now.windDeg + "级\n");
                suggestInfo.setText(weatherList.get(0).suggestionList.get(5).info);
                suggestBrf.setText(weatherList.get(0).suggestionList.get(5).brief);
                suggestIcon.setImageResource(R.drawable.uv);
                break;
            case R.id.suggest_item7:
                System.out.println("======设置属性7=====");
                suggestData.setText("明天 " + weatherList.get(0).forecastList.get(1).dayInfo + "\n" +
                        "湿度" + weatherList.get(0).now.humidity + "%\n" +
                        weatherList.get(0).forecastList.get(1).wind_dir +
                        weatherList.get(0).forecastList.get(1).wind_sc + "级");
                suggestInfo.setText(weatherList.get(0).suggestionList.get(6).info);
                suggestBrf.setText(weatherList.get(0).suggestionList.get(6).brief);
                suggestIcon.setImageResource(R.drawable.car_wash);
                break;
            default:
                break;
        }

    }

    /**
     * 设置屏幕的背景透明度
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }

}

class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView week_day, week_weather, week_temp, week_wind_dir;
    public ImageView week_img;

    public MyViewHolder(View itemView) {
        super(itemView);
        week_day = itemView.findViewById(R.id.week_day);
        week_weather = itemView.findViewById(R.id.week_weather);
        week_temp = itemView.findViewById(R.id.week_temp);
        week_wind_dir = itemView.findViewById(R.id.week_wind_dir);
        week_img = itemView.findViewById(R.id.week_img);
    }
}

