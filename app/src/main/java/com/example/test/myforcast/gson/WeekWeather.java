package com.example.test.myforcast.gson;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeekWeather {
    public String code;

    public Data data;

    public class Data{

        public Yesterday yesterday;

        @SerializedName("forecast")
        public List<Forecast> forecastList;

        public class Yesterday{
            public String date;

            @SerializedName("high")
            public String temp_max;

            @SerializedName("low")
            public String temp_min;

            @SerializedName("fl")
            public String wind_degree;

            @SerializedName("fx")
            public String wind_dir;

            public String type;
        }

    }

    public class Forecast{
        public String date;

        @SerializedName("high")
        public String temp_max;

        @SerializedName("low")
        public String temp_min;

        @SerializedName("fengli")
        public String wind_degree;

        @SerializedName("fengxiang")
        public String wind_dir;

        public String type;
    }

}
