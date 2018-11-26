package com.example.test.myforcast.gson;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HeWeather {
    @SerializedName("HeWeather6")
    public List<Weather> weatherList;

    public class Weather{
        public String status;

        public Basic basic;

        public Update update;

        public Now now;

        @SerializedName("daily_forecast")
        public List<Forecast> forecastList;

        @SerializedName("lifestyle")
        public List<Suggestion> suggestionList;

    }

    public class Basic{
        @SerializedName("location")
        public String cityName;

        @SerializedName("cid")
        public String weatherId;
    }

    public class Now{
        @SerializedName("wind_sc")
        public String windDeg;
        @SerializedName("wind_dir")
        public String windDir;

        @SerializedName("cond_txt")
        public String info;

        @SerializedName("cond_code")
        public String icon;

        @SerializedName("vis")
        public String visibility;

        @SerializedName("pres")
        public String pressure;

        @SerializedName("tmp")
        public String temperature;

        @SerializedName("hum")
        public String humidity;
    }

    public class Forecast{
        public String date;

        @SerializedName("cond_code_d")
        public String iconDay;

        @SerializedName("cond_code_n")
        public String iconNight;

        @SerializedName("sr")
        public String sunrise;//

        @SerializedName("ss")
        public String sunset;//

        @SerializedName("mr")
        public String moonrise;//

        @SerializedName("ms")
        public String moonset;//

        @SerializedName("pres")
        public String pressure;//

        @SerializedName("cond_txt_d")
        public String dayInfo;//

        @SerializedName("cond_txt_n")
        public String nightInfo;//

        @SerializedName("tmp_max")
        public String temp_max;//

        @SerializedName("tmp_min")
        public String temp_min;//

        public String wind_dir;

        public String wind_sc;
    }

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }

    public class Suggestion{
        @SerializedName("brf")
        public String brief;

        @SerializedName("txt")
        public String info;

    }

}
