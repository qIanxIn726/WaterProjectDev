package com.example.water;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.text.DecimalFormat;

import static com.example.water.R.color.blue1;

/**
 * Created by 华南理工大学物理与光电学院 on 2019/3/14.
 */

public class LineChartMarkView extends MarkerView {
    private TextView tvDate;
    private TextView tvValue;
    DecimalFormat df = new DecimalFormat(".00");

    int cordinate1;
    int cordinate2;
    int xpos1;

    Resources resources = this.getResources();
    DisplayMetrics dm = resources.getDisplayMetrics();
    int width = dm.widthPixels;
    int height = dm.heightPixels;

    public LineChartMarkView(Context context){
        super(context,R.layout.mark_view);
        tvDate = findViewById(R.id.tv_date);
        tvValue = findViewById(R.id.tv_value);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight){
        //展示自定义x轴值后的内容
        tvDate.setText("时间:"+ e.getData().toString().substring(5,19));
        tvValue.setText("当前值:"+ df.format(e.getVal())+e.getData().toString().substring(23,27));
    }
    //分别设置以markView左下角为基准点的偏移量,横向正值向右，纵向正值向下
    //xpos为markview中心点横坐标
    @Override
    public int getXOffset(float xpos) {
        int offsetX = -(getWidth()/2);
        if (xpos < getWidth() / 2){
            offsetX = (int) -xpos;
        }else if (xpos > width - getWidth() / 2){
            offsetX = (int) -(getWidth() + xpos - width);
        }
        xpos1 = (int) xpos;
        return offsetX;
    }

    @Override
    public int getYOffset(float ypos) {
        return -(getHeight()*7/6);
    }

    protected void onDraw(Canvas canvas){

        cordinate1 = (int) getX();
        cordinate2 = (int) getY();
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(getResources().getColor(blue1));
        Path path = new Path();
        //顺时针方向描点
        //x轴向右为正方向
        //y轴向下为正方向
        if (xpos1 < getWidth() / 2){
            path.moveTo(cordinate1+getWidth()/2 - 30 - getWidth()/2 + xpos1,cordinate2+getHeight()-5);
            path.lineTo(cordinate1+getWidth()/2 - getWidth()/2 + xpos1,cordinate2 + 190);
            path.lineTo(cordinate1+getWidth()/2 + 30- getWidth()/2 + xpos1,cordinate2+getHeight()-5);
        }else if (xpos1 > width - getWidth() / 2){
            path.moveTo(cordinate1+getWidth()/2 - 30 + (getWidth()/2 - (width - xpos1)),cordinate2+getHeight()-5);
            path.lineTo(cordinate1+getWidth()/2 + (getWidth()/2 - (width - xpos1)),cordinate2 + 190);
            path.lineTo(cordinate1+getWidth()/2 + 30 + (getWidth()/2 - (width - xpos1)),cordinate2+getHeight()-5);
        }else{
            path.moveTo(cordinate1+getWidth()/2 - 30,cordinate2+getHeight()-5);
            path.lineTo(cordinate1+getWidth()/2,cordinate2 + 190);
            path.lineTo(cordinate1+getWidth()/2 + 30,cordinate2+getHeight()-5);
        }
        path.close();
        canvas.drawPath(path,p);
    }

}
