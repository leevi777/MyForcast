package com.example.test.myforcast.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.test.myforcast.db.City;
import com.example.test.myforcast.db.County;
import com.example.test.myforcast.db.Province;
import com.example.test.myforcast.gson.HeWeather;
import com.example.test.myforcast.gson.News;
import com.example.test.myforcast.gson.WeekWeather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 解析和处理服务器返回的省级数据
 */
public class Utility {
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = (JSONObject) allProvinces.get(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("TAG", "获取省级数据失败");
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = (JSONObject) allCities.get(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("TAG", "获取市级数据失败");
        return false;
    }

    /**
     * 解析和处理服务器返回县级数据
     */
    public static boolean handleCountyResponse(String response, int CityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = (JSONObject) allCounties.get(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(CityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("TAG", "获取县级数据失败");
        return false;
    }

    /**
     * 将返回的JSON数据解析成Weather实体类列表
     */
    public static HeWeather.Weather handleWeatherResponse(String response) {
        HeWeather heWeather = new Gson().fromJson(response, HeWeather.class);
        return heWeather.weatherList.get(0);
    }


    /**
     * 将返回的JSON数据解析成WeekWeather实体类
     */
    public static WeekWeather handleWeekWeatherResponse(String response) {
        return new Gson().fromJson(response,WeekWeather.class);
    }

    /**
     * 将返回的JSON数据解析成News实体类
     */
    public static News handleNewsResponse(String response) {
        return new Gson().fromJson(response,News.class);
    }

}
