package com.example.water;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.water.Base.BaseActicty;
import com.example.water.Model.DetailModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.graphics.Color.YELLOW;
import static com.example.water.R.color.black;

/**
 * Created by 华南理工大学物理与光电学院 on 2019/3/9.
 */

public class ChartActivity extends BaseActicty {

    @BindView(R.id.tv_title)
    TextView chart_title;
    private YAxis yAxis;

    int Lev1_a = 0;
    int Lev1_b = 0;
    int Lev2 = 0;
    int Lev3 = 0;

    String symbol;

    float value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);


        Bundle bundleObject = getIntent().getExtras();

        ArrayList<DetailModel> dm = (ArrayList<DetailModel>) bundleObject.getSerializable("data");

        String title = bundleObject.getString("param_Id");
        switch(title){
            case "Cod":
                symbol = "mg/l";
                Lev1_a = 50;
                Lev1_b = 60;
                Lev2 = 100;
                Lev3 = 120;
                break;
            case "Toc":
                symbol = "mg/l";
                break;
            case "Bod":
                symbol = "mg/l";
                break;
            case "Ntu":
                symbol = "ntu";
                break;
            case "Tss":
                symbol = "mg/l";
                break;
            case "Tempreture":
                symbol = "℃";
                break;
            case "PH":
                symbol = "无";
                break;
            case "TDS":
                symbol = "mg/l";
                break;
            default:
                break;
        }

        chart_title.setText(title);

        LineChart chart = (LineChart) findViewById(R.id.chart);

        yAxis = chart.getAxisLeft();

        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Collections.reverse(dm);
        LineData cLineData = makeLineData(dm);
        setChartStyle(chart, cLineData, Color.WHITE);

        MarkerView mv = new LineChartMarkView(this);
        chart.setMarkerView(mv);


    }

    @Override
    protected int getResourceID(){
        return R.layout.activity_chart;
    }

    @Override
    protected void handleData(JSONObject responseObject){}

    private LineData makeLineData(ArrayList detailModel) {
        ArrayList<String> x = new ArrayList<>();
        ArrayList<Entry> y = new ArrayList<>();

        for (int i = 0; i < detailModel.size(); i++) {
            DetailModel dModel = (DetailModel) detailModel.get(i);

            x.add(dModel.getAt());

            value = Float.parseFloat(dModel.getValue());
            Entry entry = new Entry(value, i);
            y.add(entry);
        }

        LineDataSet cLineDataSet = new LineDataSet(y, "");

        cLineDataSet.setLineWidth(3.0f);

        cLineDataSet.setCircleSize(5.0f);

        //线条颜色
        cLineDataSet.setColor(getResources().getColor(black));

        //数据点外圈颜色
        cLineDataSet.setCircleColor(getResources().getColor(black));

        cLineDataSet.setDrawHighlightIndicators(true);

        //数据点高亮时的横纵线颜色
        cLineDataSet.setHighLightColor(YELLOW);

        cLineDataSet.setValueTextSize(10.0f);

        //数据点颜色
        cLineDataSet.setCircleColorHole(YELLOW);

        cLineDataSet.setDrawFilled(true);

        cLineDataSet.setFillAlpha(200);

        //折线下方填充颜色
        cLineDataSet.setFillColor(YELLOW);


        /*cLineDataSet.setValueFormatter(new ValueFormatter(){
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler){
                //TODO Auto_generated method stub
                int n = (int) value;
                String s = n;
                return s;
            }
        });*/

        ArrayList<LineDataSet> cLineDataSets = new ArrayList<LineDataSet>();
        cLineDataSets.add(cLineDataSet);

        LineData cLineData = new LineData(x, cLineDataSets);

        return cLineData;
    }

    private void setChartStyle(LineChart mLineChart, LineData lineData, int color) {

        mLineChart.setDrawBorders(false);

        mLineChart.setDescription("单位：" + symbol);

        mLineChart.setNoDataTextDescription("没有数据熬");

        mLineChart.setDrawGridBackground(false);

        mLineChart.setGridBackgroundColor(YELLOW);

        mLineChart.setTouchEnabled(true);

        mLineChart.setDragEnabled(true);

        mLineChart.setScaleEnabled(true);

        mLineChart.setPinchZoom(false);

        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        mLineChart.getAxisRight().setEnabled(true);

        mLineChart.setBackgroundColor(color);

        mLineChart.setData(lineData);

        //将图例隐藏
        Legend mLegend = mLineChart.getLegend();

        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        mLegend.setForm(Legend.LegendForm.CIRCLE);

        mLegend.setFormSize(0.0f);

        mLegend.setTextColor(Color.RED);

        mLineChart.animateX(2500);
        //图例结束


        //设置分级线相关样式
        LimitLine lev1_A = new LimitLine(Lev1_a,"一级标准A");
        lev1_A.setLineColor(getResources().getColor(black));
        lev1_A.setLineWidth(2f);
        lev1_A.setTextColor(getResources().getColor(black));
        lev1_A.setTextSize(12f);

        LimitLine lev1_B = new LimitLine(Lev1_b,"一级标准B");
        lev1_B.setLineColor(getResources().getColor(black));
        lev1_B.setLineWidth(2f);
        lev1_B.setTextColor(getResources().getColor(black));
        lev1_B.setTextSize(12f);

        LimitLine lev2 = new LimitLine(Lev2,"二级标准");
        lev2.setLineColor(getResources().getColor(black));
        lev2.setLineWidth(2f);
        lev2.setTextColor(getResources().getColor(black));
        lev2.setTextSize(12f);

        LimitLine lev3 = new LimitLine(Lev3,"三级标准");
        lev3.setLineColor(getResources().getColor(black));
        lev3.setLineWidth(2f);
        lev3.setTextColor(getResources().getColor(black));
        lev3.setTextSize(12f);

        if ((Lev1_a*Lev1_b*Lev2*Lev3) != 0){
            yAxis.addLimitLine(lev1_A);
            yAxis.addLimitLine(lev1_B);
            yAxis.addLimitLine(lev2);
            yAxis.addLimitLine(lev3);
        }
    }

}
