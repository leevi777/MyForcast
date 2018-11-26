package com.example.test.myforcast.gson;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class News {
    @SerializedName("error_code")
    public int code;

    public Result result;

    public class Result{
        public String stat;

        @SerializedName("data")
        public List<Data> dataList;
    }

    public class Data{
        public String title;

        public String date;

        public String author_name;

        public String url;

        @SerializedName("thumbnail_pic_s")
        public String img1_url;

        @SerializedName("thumbnail_pic_s02")
        public String img2_url;

        @SerializedName("thumbnail_pic_s03")
        public String img3_url;
    }

}
