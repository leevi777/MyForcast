package com.example.test.myforcast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.myforcast.gson.HeWeather;
import com.example.test.myforcast.gson.WeekWeather;
import com.example.test.myforcast.service.UpdateService;
import com.example.test.myforcast.util.HttpUtil;
import com.example.test.myforcast.util.Utility;
import com.qiushui.blurredview.BlurredView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private RecyclerViewAdapter adapter;

    public DrawerLayout drawerLayout;

    private BlurredView mBlurredView;

    private RecyclerView mRecyclerView;

    private int mScrollerY;

    private int mAlpha;

    private TextView title;

    private Button add;

    private Button search;

    private EditText etCityName;

    public SwipeRefreshLayout swipeRefresh;

    private List<HeWeather.Weather> weatherList = new ArrayList<>();

    private List<WeekWeather> weekWeatherList = new ArrayList<>();

    //判断是否访问成功和风天气和本周天气
    boolean flag1 = false;
    boolean flag2 = false;

    private final int REQUEST_WEATHER_SUCCESS = 0;

    private final int REQUEST_IMG_SUCCESS = 1;

    private HeWeather.Weather return_weather;

    private WeekWeather return_weekWeather;

    private String return_weatherStr;

    private String return_weekWeatherStr;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_WEATHER_SUCCESS:
                    if (flag1 && flag2) {
                        flag1 = false;
                        flag2 = false;
                        SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(WeatherActivity.this).edit();
                        editor.putString("weather", return_weatherStr);
                        editor.putString("weather_name", return_weather.basic.cityName);
                        editor.putString("weekWeather", return_weekWeatherStr);
                        editor.apply();
                        weatherList.clear();
                        weatherList.add(return_weather);
                        weekWeatherList.clear();
                        weekWeatherList.add(return_weekWeather);
                        adapter.notifyDataSetChanged();
                        title.setText(weatherList.get(0).basic.cityName);
                        swipeRefresh.setRefreshing(false);
                        Intent intent = new Intent(WeatherActivity.this, UpdateService.class);
                        startService(intent);
                    }
                    break;
                case REQUEST_IMG_SUCCESS:
                    byte[] bytes = (byte[]) msg.obj;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    mBlurredView.setBlurredImg(bitmap);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();
    }

    private void initView() {
        add = findViewById(R.id.add);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        drawerLayout = findViewById(R.id.dw);
        search = findViewById(R.id.search);
        etCityName = findViewById(R.id.city_name);
        title = findViewById(R.id.title);

        mBlurredView = (BlurredView) findViewById(R.id.blurred_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, weatherList, weekWeatherList);
        mRecyclerView.setAdapter(adapter);

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                hintKeyBoard();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = etCityName.getText().toString();
                if (!TextUtils.isEmpty(cityName)) {
                    swipeRefresh.setRefreshing(true);
                    requestWeather(cityName);
                    requestWeekWeather(cityName);
                    etCityName.setText("");
                    drawerLayout.closeDrawers();
                } else {
                    Toast.makeText(WeatherActivity.this, "请输入城市名字或拼音", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        String weekWeatherString = prefs.getString("weekWeather", null);
        if (weatherString != null & weekWeatherString != null) {
            HeWeather.Weather weather = Utility.handleWeatherResponse(weatherString);
            WeekWeather weekWeather = Utility.handleWeekWeatherResponse(weekWeatherString);
            Log.i("TAG", weatherString+"\n"+weekWeatherString);
            weatherList.add(weather);
            weekWeatherList.add(weekWeather);
            title.setText(weather.basic.cityName);
            adapter.notifyDataSetChanged();
        } else {
            //无缓存时去服务器查询天气
            String weatherName = getIntent().getStringExtra("weather_name");
            title.setText(weatherName);
            requestWeather(weatherName);
            requestWeekWeather(weatherName);
        }


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String weatherName = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this)
                        .getString("weather_name", null);
                Log.i("TAG", "CityName" + weatherName);
                requestWeather(weatherName);
                requestWeekWeather(weatherName);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollerY += dy;
                if (Math.abs(mScrollerY) > 1000) {
                    //设置图片上移距离
                    mBlurredView.setBlurredTop(100);
                    mAlpha = 100;
                } else {
                    //设置图片上移距离
                    mBlurredView.setBlurredTop(mScrollerY / 10);
                    //获取绝对值
                    mAlpha = Math.abs(mScrollerY) / 10;
                }
                mBlurredView.setBlurredLevel(mAlpha);//决定模糊的级别，alpha在0到100之间。
            }
        });
        loadPic();

    }

    /**
     * 根据城市名 请求城市天气信息
     */
    public void requestWeather(final String weatherName) {
        String weatherUrl = "https://free-api.heweather.com/s6/weather?location=" + weatherName
                + "&key=13c83bc650e8483aac1464558aaf62e4";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                Log.i("TAG", responseText);
                final HeWeather.Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
//                            SharedPreferences.Editor editor = PreferenceManager
//                                    .getDefaultSharedPreferences(WeatherActivity.this).edit();
//                            editor.putString("weather", responseText);
//                            editor.putString("weather_name", weather.basic.cityName);
//                            editor.apply();
                            return_weatherStr = responseText;
                            flag1 = true;
                            Message msg = new Message();
                            msg.obj = responseText;
                            msg.what = REQUEST_WEATHER_SUCCESS;
                            return_weather = weather;
                            handler.sendMessage(msg);
//                            weatherList.clear();
//                            weatherList.add(weather);
//                            handler.sendEmptyMessage(REQUEST_WEATHER_SUCCESS);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                });
            }
        });
        loadPic();
    }

    /**
     * 根据城市名 请求本周城市天气信息
     */
    public void requestWeekWeather(final String weatherName) {
        String weatherUrl = "https://www.apiopen.top/weatherApi?city=" + weatherName;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取本周天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.i("TAG", responseText);
                WeekWeather weekWeather = Utility.handleWeekWeatherResponse(responseText);
                if (weekWeather.code.equals("200") && weekWeather != null) {
//                    SharedPreferences.Editor editor = PreferenceManager
//                            .getDefaultSharedPreferences(WeatherActivity.this).edit();
//                    editor.putString("weekWeather", responseText);
//                    editor.apply();
                    return_weekWeatherStr = responseText;
                    flag2 = true;
                    Message msg = new Message();
                    msg.obj = responseText;
                    msg.what = REQUEST_WEATHER_SUCCESS;
                    return_weekWeather = weekWeather;
                    handler.sendMessage(msg);
//                    weekWeatherList.clear();
//                    weekWeatherList.add(weekWeather);
//                    handler.sendEmptyMessage(REQUEST_WEATHER_SUCCESS);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WeatherActivity.this, "获取本周天气信息失败", Toast.LENGTH_SHORT).show();
                            swipeRefresh.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }

    /**
     * 加载必应每日一图
     */
    private void loadPic() {
        final String pic_url = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(pic_url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String url = response.body().string();
                HttpUtil.sendOkHttpRequest(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        byte[] bytes = response.body().bytes();
                        Message message = new Message();
                        message.obj = bytes;
                        message.what = REQUEST_IMG_SUCCESS;
                        handler.sendMessage(message);
                    }
                });
            }
        });
    }


    /**
     * 关闭软件盘
     */
    public void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}
