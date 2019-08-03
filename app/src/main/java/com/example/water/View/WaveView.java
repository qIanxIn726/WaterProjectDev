package com.example.water.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.water.R;

/**
 * Created by 华南理工大学物理与光电学院 on 2019/5/24.
 */

public class WaveView extends View {

    private int screenWidth;
    private int screenHeight;
    private int waveWidth;
    private int waveHeight;

    public WaveView(Context context){
        super(context);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs){
        super(context,attrs);
    }

    public WaveView(Context context,@Nullable AttributeSet attrs,int desStyleAttr){
        super(context,attrs,desStyleAttr);
    }

    @Override
    public void onDraw(Canvas canvas){

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        waveWidth = screenWidth/10;
        waveHeight = screenHeight/20;

        Paint p1 = new Paint();
        p1.setColor(getResources().getColor(R.color.blue2));

        Path path1 = new Path();
        path1.moveTo(screenWidth/2,6/7 * screenHeight);
        for (int i = 0;i <= 20;i ++){
            path1.rQuadTo(waveWidth/4,-waveHeight,waveWidth/2,0);
            path1.rQuadTo(waveWidth/4,waveHeight,waveWidth/2,0);
        }
        path1.rLineTo(0,screenHeight/7);
        path1.rLineTo(-screenWidth,0);
        path1.close();

        canvas.drawPath(path1,p1);

    }
}
