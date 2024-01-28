package com.example.cookd.petri;

import android.app.Activity;
import android.os.Bundle;
import  android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SandBox extends Activity
{
    private static final String TAG = SandBox.class.getSimpleName();

    private TextView myText = null;

    private Object mPauseLock = new Object();
    private boolean mPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new SandBoxView(this));
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



}//ends SandBox class
