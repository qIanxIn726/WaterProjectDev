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
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.water.R.color.blue2;
import static com.example.water.R.color.grey;
import static com.example.water.R.color.white;

/**
 * Created by 华南理工大学物理与光电学院 on 2019/3/9.
 */

public class ChartActivity extends BaseActicty {

    @BindView(R.id.tv_title)
    TextView chart_title;

    private YAxis yAxis;

    private XAxis xAxis;

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
            case "NTU":
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

        xAxis = chart.getXAxis();
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
        setChartStyle(chart, cLineData, getResources().getColor(white));

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
        cLineDataSet.setColor(getResources().getColor(grey));

        //数据点外圈颜色
        cLineDataSet.setCircleColor(getResources().getColor(grey));

        cLineDataSet.setDrawHighlightIndicators(true);

        //数据点高亮时的横纵线颜色
        cLineDataSet.setHighLightColor(getResources().getColor(grey));

        cLineDataSet.setHighlightLineWidth(2f);

        //设置数据点数字格式
        cLineDataSet.setValueTextSize(10.0f);

        cLineDataSet.setValueTextColor(getResources().getColor(blue2));

        //数据点颜色
        cLineDataSet.setCircleColorHole(getResources().getColor(white));

        cLineDataSet.setDrawFilled(true);

        cLineDataSet.setFillAlpha(210);

        //折线下方填充颜色
        cLineDataSet.setFillColor(getResources().getColor(blue2));


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

        mLineChart.setTouchEnabled(true);

        mLineChart.setDragEnabled(true);

        mLineChart.setDrawGridBackground(false) ;

        xAxis.setDrawGridLines(false);

        yAxis.setValueFormatter((new YAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value, YAxis yAxis){
                //TODO Auto_generated method stub
                return ("  " + Float.toString(value)+"  ");
            }
        }));


        //设置横纵滑动动画
        mLineChart.setDragDecelerationEnabled(true);
        mLineChart.setDragDecelerationFrictionCoef(0.96f);

        //设置放大缩小
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

        //数据载入动画
        //mLineChart.animateX(600);
        mLineChart.animateY(600);
        //图例结束


        //设置分级线相关样式
        LimitLine lev1_A = new LimitLine(Lev1_a,"一级标准A");
        lev1_A.enableDashedLine(10f,3f,0);
        lev1_A.setLineColor(getResources().getColor(grey));
        lev1_A.setLineWidth(2f);
        lev1_A.setTextColor(getResources().getColor(grey));
        lev1_A.setTextSize(12f);

        LimitLine lev1_B = new LimitLine(Lev1_b,"一级标准B");
        lev1_B.enableDashedLine(10f,3f,0);
        lev1_B.setLineColor(getResources().getColor(grey));
        lev1_B.setLineWidth(2f);
        lev1_B.setTextColor(getResources().getColor(grey));
        lev1_B.setTextSize(12f);

        LimitLine lev2 = new LimitLine(Lev2,"二级标准");
        lev2.enableDashedLine(10f,3f,0);
        lev2.setLineColor(getResources().getColor(grey));
        lev2.setLineWidth(2f);
        lev2.setTextColor(getResources().getColor(grey));
        lev2.setTextSize(12f);

        LimitLine lev3 = new LimitLine(Lev3,"三级标准");
        lev3.enableDashedLine(10f,3f,0);
        lev3.setLineColor(getResources().getColor(grey));
        lev3.setLineWidth(2f);
        lev3.setTextColor(getResources().getColor(grey));
        lev3.setTextSize(12f);

        if ((Lev1_a*Lev1_b*Lev2*Lev3) != 0){
            yAxis.addLimitLine(lev1_A);
            yAxis.addLimitLine(lev1_B);
            yAxis.addLimitLine(lev2);
            yAxis.addLimitLine(lev3);
        }
    }

}
