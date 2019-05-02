package com.example.water;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.water.Base.BaseActicty;
import com.example.water.Model.DetailModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.water.R.color.black;
import static com.example.water.R.color.blue2;
import static com.example.water.R.color.grey;
import static com.example.water.R.color.light_black;
import static com.example.water.R.color.white;

/**
 * Created by 华南理工大学物理与光电学院 on 2019/3/9.
 */

public class ChartActivity extends BaseActicty {

    @BindView(R.id.tv_title1)
    TextView chart_title1;
    @BindView(R.id.tv_title2)
    TextView chart_title2;

    private YAxis yAxisLeft;
    private YAxis yAxisRight;
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
        //根据参数类型选择单位，为了markerview显示格式统一，将单位都设置为四个字符长度
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
                symbol = "ntu ";
                break;
            case "Tss":
                symbol = "mg/l";
                break;
            case "Tempreture":
                symbol = "℃   ";
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

        chart_title1.setText(title);
        chart_title2.setText("数据走势折线图");

        LineChart chart = (LineChart) findViewById(R.id.chart);

        xAxis = chart.getXAxis();
        yAxisLeft = chart.getAxisLeft();

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
            Entry entry = new Entry(value, i, dModel.getAt() + symbol);
            y.add(entry);
        }
        //public LineDataSet(List<Entry> yVals, String label),label为图例的标签
        LineDataSet cLineDataSet = new LineDataSet(y,"");
        //线条部分
        //粗细
        cLineDataSet.setLineWidth(3.0f);
        //数据点圆圈大小
        cLineDataSet.setCircleSize(5.0f);
        //线条颜色
        cLineDataSet.setColor(getResources().getColor(grey));
        //数据点外圈颜色
        cLineDataSet.setCircleColor(getResources().getColor(grey));

        //纵横线部分
        // 暂时先选是,有了markerview下三角后，取消纵横线
        cLineDataSet.setDrawHighlightIndicators(false);
        //颜色
        cLineDataSet.setHighLightColor(getResources().getColor(grey));
        //宽度
        cLineDataSet.setHighlightLineWidth(2f);

        //设置数据点
        //大小
        cLineDataSet.setValueTextSize(10.0f);
        //颜色
        cLineDataSet.setValueTextColor(getResources().getColor(blue2));
        //圆圈中心颜色
        cLineDataSet.setCircleColorHole(getResources().getColor(white));

        //折线与横轴之间部分的填充
        cLineDataSet.setDrawFilled(true);
        //填充程度，0-255，越高越饱和
        cLineDataSet.setFillAlpha(210);
        //填充颜色
        cLineDataSet.setFillColor(getResources().getColor(blue2));

        ArrayList<LineDataSet> cLineDataSets = new ArrayList<LineDataSet>();
        cLineDataSets.add(cLineDataSet);

        LineData cLineData = new LineData(x, cLineDataSets);

        return cLineData;
    }

    //图表风格设置
    private void setChartStyle(LineChart mLineChart, LineData lineData, int color) {
        //图标边界选项
        mLineChart.setDrawBorders(true);
        //图表描述，默认显示位置右下角
        mLineChart.setDescription("单位：" + symbol);
        mLineChart.setDescriptionTextSize(20f);
        mLineChart.setDescriptionColor(black);
        //无数据时的显示内容
        mLineChart.setNoDataTextDescription("没有数据熬");

        mLineChart.setTouchEnabled(true);
        mLineChart.setDragEnabled(true);
        //是否显示背景网格
        mLineChart.setDrawGridBackground(true);

        //x轴设置
        //网格线
        xAxis.setDrawGridLines(false);
        //标签间隔,自定义的标签无法设置间隔
        //xAxis.setSpaceBetweenLabels(2);
        //标签大小
        xAxis.setTextSize(12f);
        //避免首末标签出界
        //xAxis.setAvoidFirstLastClipping(true);
        //标签格式修改
        xAxis.setValueFormatter(new XAxisValueFormatter (){
            @Override
            public String getXValue(String value,int index,ViewPortHandler xAxis){
                return (value.substring(5,19));
            }
        });

        //y轴设置
        //标签大小
        yAxisLeft.setTextSize(12f);
        //顶部预留空间大小
        yAxisLeft.setSpaceTop(45);
        //数据格式转换
        yAxisLeft.setValueFormatter((new YAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value, YAxis yAxis){
                //TODO Auto_generated method stub
                return ("  " + Float.toString(value)+"  ");
            }
        }));


        //设置横纵滑动动画
        mLineChart.setDragDecelerationEnabled(true);
        //滑动是的减速系数，0-1，越大减速越慢
        mLineChart.setDragDecelerationFrictionCoef(0.96f);

        //设置放大缩小
        mLineChart.setScaleEnabled(true);
        //双指操控
        mLineChart.setPinchZoom(false);
        //x轴位置
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        //右侧纵轴显示
        mLineChart.getAxisRight().setEnabled(false);
        //背景颜色
        mLineChart.setBackgroundColor(color);
        //载入数据
        mLineChart.setData(lineData);

        //图例部分
        //隐藏
        mLineChart.getLegend().setEnabled(false);

        //数据载入动画
        //mLineChart.animateX(600);
        mLineChart.animateY(900);


        //设置分级线相关样式
        LimitLine lev1_A = new LimitLine(Lev1_a,"一级标准A");
        lev1_A.enableDashedLine(10f,3f,0);
        lev1_A.setLineColor(getResources().getColor(light_black));
        lev1_A.setLineWidth(2f);
        lev1_A.setTextColor(getResources().getColor(light_black));
        lev1_A.setTextSize(12f);

        LimitLine lev1_B = new LimitLine(Lev1_b,"一级标准B");
        lev1_B.enableDashedLine(10f,3f,0);
        lev1_B.setLineColor(getResources().getColor(light_black));
        lev1_B.setLineWidth(2f);
        lev1_B.setTextColor(getResources().getColor(light_black));
        lev1_B.setTextSize(12f);

        LimitLine lev2 = new LimitLine(Lev2,"二级标准");
        lev2.enableDashedLine(10f,3f,0);
        lev2.setLineColor(getResources().getColor(light_black));
        lev2.setLineWidth(2f);
        lev2.setTextColor(getResources().getColor(light_black));
        lev2.setTextSize(12f);

        LimitLine lev3 = new LimitLine(Lev3,"三级标准");
        lev3.enableDashedLine(10f,3f,0);
        lev3.setLineColor(getResources().getColor(light_black));
        lev3.setLineWidth(2f);
        lev3.setTextColor(getResources().getColor(light_black));
        lev3.setTextSize(12f);

        if ((Lev1_a*Lev1_b*Lev2*Lev3) != 0){
            yAxisLeft.addLimitLine(lev1_A);
            yAxisLeft.addLimitLine(lev1_B);
            yAxisLeft.addLimitLine(lev2);
            yAxisLeft.addLimitLine(lev3);
        }
    }

}
