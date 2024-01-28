package com.example.cookd.petri;

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
import android.widget.ImageButton;

public class TitleScreen extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        //launch sandbox from sandbox button
        ImageButton ibtnSandBox = (ImageButton) findViewById(R.id.SandBox);
        ibtnSandBox.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent sandBoxIntent = new Intent(v.getContext(),SandBox.class);
                startActivityForResult(sandBoxIntent, 0);
            }//ends OnClick
        });//ends setOnClickListener

        //launch LevelSelect from level select button
        ImageButton ibtnLevelSelect = (ImageButton) findViewById(R.id.LevelSelect);
        ibtnLevelSelect.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent levelSelectIntent = new Intent(v.getContext(),LevelSelect.class);
                startActivityForResult(levelSelectIntent, 0);
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

}//ends TitleScreen class
