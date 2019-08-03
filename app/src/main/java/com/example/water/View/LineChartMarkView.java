package com.example.water.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.example.water.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.text.DecimalFormat;

import static com.example.water.R.color.blue2;

/**
 * Created by 华南理工大学物理与光电学院 on 2019/3/14.
 */

public class LineChartMarkView extends MarkerView {
    private TextView tvDate;
    private TextView tvValue;
    DecimalFormat df = new DecimalFormat(".00");

    private int cordinate1;
    private int cordinate2;

    //用xpos1获得marker View当前选中点的横坐标
    private float xpos1;
    private float osX;
    private float osY;

    Resources resources = this.getResources();
    DisplayMetrics dm = resources.getDisplayMetrics();
    int width = dm.widthPixels;
    int height = dm.heightPixels;

    public LineChartMarkView(Context context){
        super(context, R.layout.mark_view);
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
    //xpos为highlight点的横坐标
    @Override
    public int getXOffset(float xpos) {
        int offsetX = -(getWidth()/2);
        if (xpos < getWidth() / 2){
            offsetX = (int) -xpos;
        }else if (xpos > width - getWidth() / 2){
            offsetX = (int) -(getWidth() + xpos - width);
        }
        xpos1 = xpos;
        return offsetX;
    }

    @Override
    public int getYOffset(float ypos) {
        return -(getHeight()*7/6);
    }

    //根据矩形框画一个三角形
    protected void onDraw(Canvas canvas){

        //因为在markview 上调用draw,所以getX(),getY()得到的相对坐标值都为0
        //getWidth(),getHeight()分别得到该marker View的横纵长度
        //osX,osY分别为三角形底边长的一半，三角形的高
        osX = getWidth()/10;
        osY = getHeight()/5;
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(getResources().getColor(blue2));
        Path path = new Path();
        //以当前mark view 左上角位置为基准，顺时针方向描点
        //x轴向右为正方向
        //y轴向下为正方向
        if (xpos1 < getWidth() / 2){
            path.moveTo(xpos1 - osX,getHeight() * 9/10);
            path.lineTo(xpos1  + osX,getHeight() * 9/10);
            path.lineTo(xpos1,getHeight() + osY);
        }else if (xpos1 > width - getWidth() / 2) {
            path.moveTo(xpos1 + getWidth() - width - osX,getHeight() * 9/10);
            path.lineTo(xpos1 + getWidth() - width + osX,getHeight() * 9/10);
            path.lineTo(xpos1 + getWidth() - width,getHeight() + osY);
        }else{
            path.moveTo(getWidth()/2 - osX,getHeight() * 9/10);
            path.lineTo(getWidth()/2 + osX,getHeight() * 9/10);
            path.lineTo(getWidth()/2 ,getHeight() + osY);
        }
        path.close();
        canvas.drawPath(path,p);
    }

}
