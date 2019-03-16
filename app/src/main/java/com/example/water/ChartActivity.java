package com.example.water;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.water.Model.DetailModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.graphics.Color.CYAN;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.YELLOW;

/**
 * Created by 华南理工大学物理与光电学院 on 2019/3/9.
 */

public class ChartActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView chart_title;

    String symbol;

    float value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ButterKnife.bind(this);

        Bundle bundleObject = getIntent().getExtras();

        String title = bundleObject.getString("paramId");
        switch(title){
            case "Cod":
                symbol = "mg/l";
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

        ArrayList<DetailModel> dm = (ArrayList<DetailModel>) bundleObject.getSerializable("data");

        chart_title.setText(title);

        LineChart chart = (LineChart) findViewById(R.id.chart);

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

    private LineData makeLineData(ArrayList detailModel) {
        ArrayList<String> x = new ArrayList<>();
        ArrayList<Entry> y = new ArrayList<>();

        //100表示存入最近一百条历史数据
        final String[] values = new String[100];

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

        cLineDataSet.setColor(Color.DKGRAY);

        cLineDataSet.setCircleColor(GREEN);

        cLineDataSet.setDrawHighlightIndicators(true);

        cLineDataSet.setHighLightColor(YELLOW);

        cLineDataSet.setValueTextSize(10.0f);

        cLineDataSet.setCircleColorHole(YELLOW);

        cLineDataSet.setDrawFilled(true);

        cLineDataSet.setFillColor(CYAN);


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

        mLineChart.setGridBackgroundColor(CYAN);

        mLineChart.setTouchEnabled(true);

        mLineChart.setDragEnabled(true);

        mLineChart.setScaleEnabled(true);

        mLineChart.setPinchZoom(false);

        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        mLineChart.getAxisRight().setEnabled(true);

        mLineChart.setBackgroundColor(color);

        mLineChart.setData(lineData);

        //Legend mLegend = mLineChart.getLegend();

        //mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        //mLegend.setForm(Legend.LegendForm.CIRCLE);

        //mLegend.setFormSize(15.0f);

        //mLegend.setTextColor(Color.BLUE);

        mLineChart.animateX(2000);

    }

}
