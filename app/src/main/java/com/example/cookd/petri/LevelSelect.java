package com.example.cookd.petri;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

/**
 * Created by Nick on 8/14/2016.
 */
public class LevelSelect extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_level_select);

        //launch level 1 from level 1 button
        ImageButton ibtnLvl1Slct = (ImageButton) findViewById(R.id.Level1Select);
        ibtnLvl1Slct.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent level1Intent = new Intent(v.getContext(),Level1.class);
                startActivityForResult(level1Intent, 0);
            }//ends OnClick
        });//ends setOnClickListener

        //launch level 2 from level 2 button
        ImageButton ibtnLvl2Slct = (ImageButton) findViewById(R.id.Level2Select);
        ibtnLvl2Slct.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent level2Intent = new Intent(v.getContext(),Level2.class);
                startActivityForResult(level2Intent, 0);
            }//ends OnClick
        });//ends setOnClickListener

        //launch level 3 from level 3 button
        ImageButton ibtnLvl3Slct = (ImageButton) findViewById(R.id.Level3Select);
        ibtnLvl3Slct.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent level3Intent = new Intent(v.getContext(),Level3.class);
                startActivityForResult(level3Intent, 0);
            }//ends OnClick
        });//ends setOnClickListener


    }//ends onCreate method

    @Override
    protected void onDestroy()
    {

        super.onDestroy();
    }//ends onDestroy method

    @Override
    protected void onStop()
    {

        super.onStop();
    }//ends onStop method

    @Override
    protected void onPause()
    {

        super.onPause();
    }//ends onStop method

}//ends LevelSelect activty
