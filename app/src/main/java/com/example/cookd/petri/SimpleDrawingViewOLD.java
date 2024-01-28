package com.example.cookd.petri;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author: Darian Cook
 * Date: 1/5/2016
 * View Class for using a canvas
 */
public class SimpleDrawingViewOLD extends View
{
    private final int paintColor = Color.BLACK;
    private Paint drawPaint;

    public SimpleDrawingViewOLD(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }//ends SimpleDrawingView constructor

    private void setupPaint()
    {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }//ends setupPaint method

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawPaint(drawPaint);
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.smallpowerhouseofthecell), 10, 10, null);
    }//ends onDraw method
}//ends SimpleDrawingView class
