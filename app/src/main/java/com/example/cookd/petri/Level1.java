package com.example.cookd.petri;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Nick on 8/16/2016.
 */
public class Level1 extends Activity {

    private static final String TAG = Level1.class.getSimpleName();

    private TextView myText = null;

    private Object mPauseLock = new Object();
    private boolean mPaused;

    public Handler fHandler;

    Level1View myLevel1View;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

    /*
        fHandler = new Handler()
        {
            public void handleMessage( android.os.Message msg )
            {
                if ( msg.what == 100 )
                {
                    onBackPressed();
                }
            }
        }; */

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        myLevel1View = new Level1View(this);
        setContentView(myLevel1View);
        Log.d(TAG, "View added");

    }//ends onCreate method

    @Override
    protected void onDestroy()
    {

        Log.d(TAG, "Destroying...");
        super.onDestroy();
    }//ends onDestroy method

    @Override
    protected void onStop()
    {
        Log.d(TAG, "Stopping...");
        super.onStop();
    }//ends onStop method

    @Override
    protected void onResume()
    {
        Log.d(TAG, "Resuming...");

        super.onResume();
    }//ends onStop method

    /*@Override
    public void onBackPressed()
    {
        Log.d(TAG, "Backing...");

        super.onBackPressed();
    }//ends onStop method*/

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Log.d(TAG, "onTouchEvent...");

        if( myLevel1View.activityFinish == true)
        {
            super.onBackPressed();
        }

        return true;
    }//ends onStop method


}//ends Level1 class

