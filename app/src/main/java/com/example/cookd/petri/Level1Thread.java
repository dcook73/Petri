package com.example.cookd.petri;

import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.graphics.Canvas;

/**
 * Created by Nick on 8/16/2016.
 */
public class Level1Thread extends Thread
{
    private static final String TAG = Level1View.class.getSimpleName();

    private SurfaceHolder surfaceHolder;
    private Level1View gameView;
    private boolean running;

   // public boolean activityFinish = false;

    //lock shit
    private Object mPauseLock;
    private boolean mPaused;
    private boolean mFinished;

    //menu shit
    boolean menuUp;

    public Level1Thread(SurfaceHolder surfaceHolder, Level1View gameView)
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

    }//ends Level1Thread constructor

    public void setRunning(boolean running)
    {
        this.running = running;
    }//ends setRunning method

    public void setmFinished( boolean finished )
    {
        mFinished = finished;
    }

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



}//ends Level1Thread class
