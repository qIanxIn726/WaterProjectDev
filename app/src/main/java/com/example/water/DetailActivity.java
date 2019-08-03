package com.example.water;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.water.Base.BaseListViewActicty;
import com.example.water.Model.DetailModel;
import com.example.water.Utils.Constants;
import com.example.water.View.DetailItemView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class DetailActivity extends BaseListViewActicty {


    @BindView(R.id.tv_title1)
    TextView tvTitle1;

    @BindView(R.id.tv_title2)
    TextView tvTitle2;

    @BindView(R.id.list_view)
    ListView listView;

    ArrayList<DetailModel> dm = new ArrayList<DetailModel>();

    private int num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String datastreamId = getIntent().getStringExtra("datastream_id");

        // 选择获得多少历史数据
        num = 500;
        tvTitle1.setText(datastreamId);
        tvTitle2.setText("最近"+num+"条历史数据");
        //APIKey vL3MUb=BBsmII6Q6scQNlArS1ck=
        //获取limit条历史数据
        String url = Constants.BASE_URL + "datapoints?datastream_id=" + datastreamId + "&limit=" + num;
        getData(url);
        /*String url = Constants.BASE_URL + "datastreams/" + datastreamId ;//+ "&amp;start=2018-11-25T00:00:00&amp;limit=1000&amp;cursor=182370_502129186_1543116108193 ";
        getData(url);*/
        //LineData cLineData = makeLineData(dm);

        //向ChartActivity传递数据
        ImageButton chart_Go = (ImageButton) findViewById(R.id.chart_go);
        chart_Go.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent1 = new Intent(DetailActivity.this,ChartActivity.class);
                Bundle bundleObject = new Bundle();
                bundleObject.putSerializable("data",dm);
                bundleObject.putString("param_Id",datastreamId);
                intent1.putExtras(bundleObject);
                startActivity(intent1);
            }
        });
    }

    @Override
    protected int getResourceID() {
        return R.layout.activity_detail;
    }

    @Override
    protected void handleData(JSONObject responseObject) {
        try {
            JSONObject dataObject = responseObject.getJSONObject("data");
            JSONArray datastreams = dataObject.getJSONArray("datastreams");
            if (datastreams.length() > 0) {
                JSONObject itemData = datastreams.getJSONObject(0);
                JSONArray dataArray = itemData.getJSONArray("datapoints");
                Gson gson = new Gson();
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject jsonObject = dataArray.getJSONObject(i);
                    DetailModel model = gson.fromJson(jsonObject.toString(), DetailModel.class);
                    dm.add(model);
                    listArray.add(model);
                }
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BaseListAdapter getAdapter() {
        return new CustomAdapter();
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }

    protected class CustomAdapter extends BaseListAdapter {

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = new DetailItemView(DetailActivity.this);
            }
            DetailItemView itemView = (DetailItemView) view;
            itemView.setData((DetailModel) listArray.get(i));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?>parent,View view,int position,long id){
                    DetailModel detailModel = (DetailModel)listArray.get(position);
                    Toast.makeText(DetailActivity.this,detailModel.getAt(),Toast.LENGTH_SHORT);
                }
            });
            return itemView;
        }
    }

}
