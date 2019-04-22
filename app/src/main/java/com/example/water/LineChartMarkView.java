package com.example.water;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.text.DecimalFormat;

/**
 * Created by 华南理工大学物理与光电学院 on 2019/3/14.
 */

public class LineChartMarkView extends MarkerView {

    //private TextView tvDate;
    private TextView tvValue;

    DecimalFormat df = new DecimalFormat(".00");

    public LineChartMarkView(Context context){
        super(context,R.layout.mark_view);

        //tvDate = findViewById(R.id.tv_date);
        tvValue = findViewById(R.id.tv_value);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight){
        //展示自定义x轴值后的内容
        //tvDate.setText("时间："+ e.getXIndex());
        tvValue.setText("当前值："+"\n"+ df.format(e.getVal()));
    }

    //分别设置以markView左下角为基准点的偏移量,横向正值向右，纵向正值向下
    @Override
    public int getXOffset(float xpos) {
        return -(getWidth()/2);
    }

    @Override
    public int getYOffset(float ypos) {
        return -(getHeight()*7/6);
    }
}
