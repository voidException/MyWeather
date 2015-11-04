package com.example.luoxiao.myweather;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.luoxiao.app.MyApplication;
import com.example.luoxiao.db.CityDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.City;


public class SelectCity extends ActionBarActivity implements View.OnClickListener {
    private static final String TAG = "SelectCity";
    private ImageView mBackBtn;
    private List<City> mCityList;
    private CityDB mCityDB;
    private ListView mListView;
    List<Map<String, String>> list;
    private static final int UPDATE_LISTVIEW = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_LISTVIEW:
                    SimpleAdapter sa = new SimpleAdapter(SelectCity.this, list, R.layout.city_listitem, new String[]{"cityname"}, new int[]{R.id.item_city_name});
                    mListView.setAdapter(sa);
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        mCityDB = ((MyApplication) MyApplication.getInstance()).getCityDB();
        mListView = (ListView) findViewById(R.id.city_listview);
        list = new ArrayList<>();
        initCityList();

        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void initCityList() {
        mCityList = new ArrayList<City>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareCityList();
            }
        }).start();
    }

    private void prepareCityList() {
        mCityList = mCityDB.getAllCity();
        for (City city : mCityList) {
            String cityName = city.getCity();
            Map<String, String> map = new HashMap<>();
            map.put("cityname", cityName);
            list.add(map);
        }
        Message msg = new Message();
        msg.what = UPDATE_LISTVIEW;
        mHandler.sendMessage(msg);
    }
}
