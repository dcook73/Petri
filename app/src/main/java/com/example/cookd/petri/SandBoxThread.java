package com.example.cookd.petri;

import android.util.Log;
import android.view.SurfaceHolder;
import android.graphics.Canvas;

/**
 * Author: Darian Cook
 * Date: 1/6/2016
 * Thread for the SandBox activity
 */
public class SandBoxThread extends Thread
{
    private static final String TAG = SandBoxThread.class.getSimpleName();

    private SurfaceHolder surfaceHolder;
    private SandBoxView gameView;
    private boolean running;

    //lock shit
    private Object mPauseLock;
    private boolean mPaused;
    private boolean mFinished;

    //menu shit
    boolean menuUp;

    public SandBoxThread(SurfaceHolder surfaceHolder, SandBoxView gameView)
    {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;

        //lock shit
        mPauseLock = new Object();
        mPaused = false;
        mFinished = false;

        //menu shit
        menuUp = false;

    }//ends SandBoxThread constructor

    public void setRunning(boolean running)
    {
        this.running = running;
    }//ends setRunning method

    public boolean getRunning()
    {
        return running;
    }//ends getRunning method

    public void setMenuUp(boolean menuValue )
    {
        this.menuUp = menuValue;
    }//ends setMenuUp method

    public boolean getMenuUp()
    {
        return this.menuUp;
    }//ends getMenuUp function

    @Override
    public void run()
    {
        while ( !mFinished )
        {
            //running = this.gameView.isRunning;
            Canvas canvas;
            long tickCount = 0L;
            Log.d(TAG, "Starting game loop");
            //this while loop will update game state and render state to screen
            while (running)
            {
                Log.d(TAG, "while running");
                canvas = null;
                tickCount++;

                try
                {
                    canvas = this.surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder)
                    {

                        if ( mPaused == false )
                        {
                            Log.d(TAG, "About to draw/update");

                            if ( mPaused == false && menuUp == false)
                            {
                                this.gameView.update();
                            }//ends if

                            if ( mPaused == false )
                            {
                                this.gameView.onDraw(canvas);
                            }//ends if
                        }//ends if


                    }//ends synchronized


                }//ends try
                finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }//ends if
                    //running = false;
                }//ends finally?

                //Pauses thread
                synchronized (mPauseLock)
                {
                    while(mPaused)
                    {
                        try
                        {
                            mPauseLock.wait();
                        }//ends try
                        catch (InterruptedException e)
                        {

                        }//ends catch
                    }//ends while loop
                }//ends synchronized

            }//ends while loop
            Log.d(TAG, "Game loop executed " + tickCount + " times");
        }//end while loop

    }//ends run method


    //on pause method
    public void onPause()
    {
        Log.d(TAG, "onPause called");
        synchronized (mPauseLock)
        {
            mPaused = true;
        }
    }//ends onPause method

    //onResume method
    public void onResume()
    {
        synchronized (mPauseLock)
        {
            mPaused = false;
            mPauseLock.notifyAll();
        }
    }//ends onResume method

}//ends SandBoxThread class
