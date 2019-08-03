package com.example.water.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.water.StatusBarCompat;
import com.example.water.Utils.HttpUtils;

import org.json.JSONObject;

import butterknife.ButterKnife;

import static com.example.water.R.color.blue2;

public abstract class BaseActicty extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //为活动找到布局文件
        setContentView(getResourceID());
        ButterKnife.bind(this);

        //给状态栏上色
        StatusBarCompat.compat(this, getResources().getColor(blue2));
    }

    //获得布局文件的方法
    protected abstract int getResourceID();

    //处理数据的方法
    protected abstract void handleData(JSONObject responseObject);

    //获取数据的方法
    //调用HttpUtils中的getData方法，并在其中重新定义RequestDataCallback中的onSuccess方法
    public void getData(final String url){
        HttpUtils.getData(this, url, new HttpUtils.RequestDataCallback() {
            @Override
            public void onSuccess(final JSONObject responseObject) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleData(responseObject);
                    }
                });
            }
        });
    }

}
