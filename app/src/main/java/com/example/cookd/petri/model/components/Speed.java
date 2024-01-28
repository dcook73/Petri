package com.example.cookd.petri.model.components;
import java.lang.*;

import com.example.cookd.petri.SandBox;
import com.example.cookd.petri.SandBoxThread;

/**
 * Author: Darian Cook
 * Date: 1/9/2016
 * Speed for moving cell around screen
 */
public class Speed
{
    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_LEFT = -1;
    public static final int DIRECTION_UP = -1;
    public static final int DIRECTION_DOWN = 1;
    public static final int DIRECTION_NONE = 0;

    private float xv = 5;    //x velocity
    private float yv = 5;    //y velocity

    private int xDir;
    private int yDir;

    public Speed()
    {
        this.xv = 5;
        this.yv = 5;
    }//ends Speed constructor

    public Speed(float xv, float yv)
    {
        this.xv = xv;
        this.yv = yv;
    }//ends Speed parametrized constructor

    public float chooseXDir(int xDir)
    {
        try
        {
            Thread.sleep(1000);
        }
        catch(Exception e)
        {}


        return xDir = 1;
    }//ends chooseXDir method

    public float chooseYDir(int yDir)
    {
        try
        {
            Thread.sleep(1000);
        }
        catch(Exception e)
        {}


       return yDir = 1;
    }//ends chooseYDir method

    public float getXv()
    {
        return xv;
    }//ends getXv method

    public void setXv(float xv)
    {
        this.xv = xv;
    }//ends setXv method

    public float getYv()
    {
        return yv;
    }//ends getYv method

    public void setYv(float yv)
    {
        this.yv = yv;
    }//ends setYz method

    public int getxDir()
    {
        return xDir;
    }//ends getxDir method

    public void setxDir(int xDir)
    {
        this.xDir = xDir;
    }//ends setxDir method

    public int getyDir()
    {
        return yDir;
    }//ends getyDir method

    public void setyDir(int yDir)
    {
        this.yDir = yDir;
    }//ends setyDir method

    //changes direction on respected axis
    public void  toggleXDirection()
    {
        xDir = xDir * -1;
    }//ends toggleXDirection method

    public void toggleYDirection()
    {
        yDir = yDir * -1;
    }//ends toggleYDirection method



}//end Speed class
