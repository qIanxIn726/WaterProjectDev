package com.example.water;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.water.Base.BaseListViewActicty;
import com.example.water.Model.HomepageModel;
import com.example.water.Utils.Constants;
import com.example.water.View.HomepageItemView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseListViewActicty {

    private SwipeRefreshLayout swipe;

    private String url;

    private IntentFilter intentFilter;

    private NetworkChangeReceiver networkChangeReceiver;

    private int netCheckTimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = Constants.BASE_URL + "datastreams?datastream_ids=" + Constants.PARAM_ALL;
        getData(url);

        //设置下拉刷新数据
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setColorSchemeResources(R.color.blue2);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                refreshData(url);
            }
        });

        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver,intentFilter);
    }

    @Override
    protected int getResourceID() {
        return R.layout.activity_main;
    }

    @Override
    protected void handleData(JSONObject responseObject) {
        try {
            JSONArray jsonArray = responseObject.getJSONArray("data");
            Gson gson = new Gson();
            listArray.clear();
            for (int i = 0;i < jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                HomepageModel model = gson.fromJson(jsonObject.toString(), HomepageModel.class);
                listArray.add(model);
            }
            adapter.notifyDataSetChanged();
            swipe.setRefreshing(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BaseListAdapter getAdapter() {
        return new CustomAdapter();
    }

    protected class CustomAdapter extends BaseListViewActicty.BaseListAdapter {

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null){
                view = new HomepageItemView(MainActivity.this);
            }
            HomepageItemView itemView = (HomepageItemView)view;
            itemView.setData((HomepageModel)listArray.get(i));
            return itemView;
        }
    }

    protected void refreshData(String url){
        getData(url);
    }

    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent){
            ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isAvailable()){
                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI && netCheckTimes > 0){
                    Toast.makeText(MainActivity.this,"WIFI连接已恢复", Toast.LENGTH_SHORT).show();
                    netCheckTimes = 0;
                }else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE && netCheckTimes > 0){
                    Toast.makeText(MainActivity.this,"移动数据连接已恢复", Toast.LENGTH_SHORT).show();
                    netCheckTimes = 0;
                }
                getData(url);
            }
            netCheckTimes ++;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

}