package com.example.test.myforcast.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.test.myforcast.WeatherActivity;
import com.example.test.myforcast.gson.HeWeather;
import com.example.test.myforcast.gson.WeekWeather;
import com.example.test.myforcast.util.HttpUtil;
import com.example.test.myforcast.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpdateService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateWeekWeather();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8*60*60*1000; //8小时
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this,UpdateService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 更新和风天气
     */
    private void updateWeather(){
        String weatherName = PreferenceManager
                .getDefaultSharedPreferences(this).getString("weather_name", null);
        if (weatherName !=null){
            String weatherUrl = "https://free-api.heweather.com/s6/weather?location=" + weatherName
                    + "&key=13c83bc650e8483aac1464558aaf62e4";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    HeWeather.Weather weather = Utility.handleWeatherResponse(responseText);
                    if (weather != null && "ok".equals(weather.status)) {
                        SharedPreferences.Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(UpdateService.this).edit();
                        editor.putString("weather", responseText);
                        editor.apply();
                    }
                }
            });
        }
    }

    /**
     * 更新本周天气
     */
    private void updateWeekWeather(){
        String weatherName = PreferenceManager
                .getDefaultSharedPreferences(this).getString("weather_name", null);
        if (weatherName != null){
            String weatherUrl = "https://www.apiopen.top/weatherApi?city=" + weatherName;
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    WeekWeather weekWeather = Utility.handleWeekWeatherResponse(responseText);
                    if (weekWeather.code.equals("200") && weekWeather != null) {
                        SharedPreferences.Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(UpdateService.this).edit();
                        editor.putString("weekWeather", responseText);
                        editor.apply();
                    }
                }
            });
        }

    }


}
