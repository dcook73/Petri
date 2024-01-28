package com.example.cookd.petri.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Nick on 6/27/2016.
 */
public class Food
{
    private Bitmap bitmap;
    private int x;
    private int y;

    //Paramaterized constructor
    public Food(Bitmap bitmap, int x, int y)
    {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
    }//ends Food constructor

    //draw function
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y -( bitmap.getHeight() / 2), null);
    }//ends draw method

    //getX function
    public int getX()
    {
        return this.x;
    }//ends getX function

    //getY function
    public int getY()
    {
        return this.y;
    }//ends getY function

    //setX function
    public void setX( int newX )
    {
       this.x = newX;
    }//ends getX function

    //setY function
    public void setY( int newY )
    {
        this.y = newY;
    }//ends getY function

}//Ends Food class
