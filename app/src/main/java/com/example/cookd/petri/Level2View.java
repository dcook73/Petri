package com.example.cookd.petri;

import com.example.cookd.petri.model.Cell;
import com.example.cookd.petri.model.Food;
import com.example.cookd.petri.model.Predator;
import com.example.cookd.petri.model.components.Speed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.*;


/**
 * Author: Darian Cook, Nicholas Harris, David Neilsen
 * Date: 10/13/2016
 * This will create the view for the Level2 activity
 */

//////////////////////////////////////////////////////////////////////////////////
    /*LEVEL DESCRIPTION*/
////////////////////////////////////////////////////////////////////////////////
    /*USER WILL BE GIVEN 30 GENERATIONS TO PREPARE FOR A WAVE OF VICIOUS CELLS
        -GOAL IS TO BREED CELLS WHICH WILL HUNT AND KILL PREDATORS
        -SIMPLE, BASIC GOAL FOR FIRST SECOND
     */
////////////////////////////////////////////////////////////////////////////////////

public class Level2View  extends SurfaceView implements SurfaceHolder.Callback
{
    private static final String TAG = Level2View.class.getSimpleName();

    private boolean levelBeginFlag = true;
    private boolean finalTrialFlag = false;
    private boolean finalStageFlag = false;
    private boolean gameOverFlag = false;
    private boolean levelVictoryFlag = false;
    private boolean interactable = false;

    private int finalStageTimer = 20;
    private int finalStageTimerHelper = 1;



    public boolean started = false;
    public boolean isRunning;
    public boolean activityFinish = false;

    private Level2Thread thread;

    //Cell/Predator/Food information
    private Cell[] cell;
    private Predator[] pred;
    private Food[] food;

    public int predIDCounter = 0;

    public int numCells = 0;
    public int numPreds = 0;
    public int maxCells = 50;
    public int maxPreds = 15;
    public int numFood = 0;
    public int maxFood = 30;

    public int numCellCounter1 = 0;
    public int numCellCounter2 = 0;

    public Random randDir = new Random();

    //Paints for use in canvas
    Paint textPaint;
    Paint rektPaint;  //#rekt

    //stats/initial values for preds/cells
    public float avgVelocity;
    public float avgVigilance;
    public float avgFear;
    public float avgHealth;
    public float avgStrength;
    public int statsScroll = 0;


    public int predVelocity = 5;
    public int predHealth = 100;
    public int predStrength = 10;

    public int foodPerBlock = 1;

    public int highestGen;

    //Food stuff
    public boolean automaticFood = false;
    public int feedTimer = 1;
    public int feedSpeed = 51;


    //UI stuff
    public int buttonSelect;
    public int clearSelect = 0;
    public RectF predButtonRect = new RectF();
    public RectF predSettingsRect = new RectF();
    public RectF foodSettingsRect = new RectF();
    public RectF highlightRect = new RectF();
    public RectF foodButtonRect = new RectF();
    public RectF cellButtonRect = new RectF();
    public RectF moreButtonRect = new RectF();
    public RectF menuRect = new RectF();
    public RectF menuBorderRect = new RectF();
    public RectF confButtonRect = new RectF();
    public RectF clearButton1 = new RectF();
    public RectF clearButton2 = new RectF();
    public RectF clearHighlightRect1 = new RectF();
    public RectF clearHighlightRect2 = new RectF();
    public RectF statsButton = new RectF();

    public RectF minusRectOne = new RectF();
    public RectF plusRectOne = new RectF();
    public RectF minusRectTwo = new RectF();
    public RectF plusRectTwo = new RectF();
    public RectF minusRectThree = new RectF();
    public RectF plusRectThree = new RectF();
    public Bitmap statsIcon = BitmapFactory.decodeResource(getResources(), R.drawable.stats);

    //constructor for Level2View
    public Level2View(Context context)
    {

        super(context);
        getHolder().addCallback(this);

        Log.d(TAG, "Level2View created");

        //Initialize cell array and any initial cells
        numCells = 10;

        cell = new Cell[maxCells + 1];
        buttonSelect = 0;

        Random randDir = new Random();

        //Log.d(TAG, "Width, height," + getWidth() + " , " + getHeight() );

        for(int i = 0; i < numCells; i++)
        {
            cell[i] =  new Cell(BitmapFactory.decodeResource(getResources(), R.drawable.cell), 0, 0);
        }//ends for loop

        //assign bitmaps for cells based on health/size
        for ( int i = 0; i < numCells; i++ )
        {
            if (cell[i].getMaxHealth() < 30 )
            {
                cell[i].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell50percent));
            }
            else if (cell[i].getMaxHealth() < 50 )
            {
                cell[i].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell75percent));
            }
            else if ( cell[i].getMaxHealth() < 100 )
            {
                cell[i].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell));
            }
            else if ( cell[i].getMaxHealth() < 150 )
            {
                cell[i].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell125percent));
            }
            else if ( cell[i].getMaxHealth() < 200 )
            {
                cell[i].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell150percent));
            }
            else
            {
                cell[i].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell200percent));
            }

        }


        //initialize pred array and any initial predators
        pred = new Predator[maxPreds + 1];
        for( int i = 0; i < numPreds; i++ )
        {
            pred[i] = new Predator(BitmapFactory.decodeResource(getResources(), R.drawable.pred),randDir.nextInt(getWidth()) ,
                    randDir.nextInt(getHeight() - getHeight() / 5) + getHeight()/10, predVelocity, predHealth, predStrength, predIDCounter);
            predIDCounter++;
        }

        //initialize food array
        food = new Food[maxFood];

        //creates a new game loop for the Level2 activity
        thread = new Level2Thread(getHolder(), this);


        //Initialize paint stuff for canvas
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize( getHeight() / 40 );

        Paint rektPaint = new Paint();
        rektPaint.setColor( Color.rgb(0,100,100) );




        setFocusable(true);
    }//ends Level2View constructor


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }//ends surfaceChanged method

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        isRunning = true;
        thread.setRunning(true);
        thread.onResume();

        if ( !started )
        {
            started = true;
            thread.start();
        }
    }//ends surfaceCreated method

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        Log.d(TAG, "Surface Destroyed");
        //isRunning = false;
        boolean retry = true;
        while(retry)
        {
            thread.onPause();
            retry = false;
        }//ends while loop
    }//ends surfaceDestroyed method

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            Log.d(TAG, "MotionEven.Action_Down detected");

            if( levelBeginFlag == false && levelVictoryFlag == false && finalTrialFlag == false && finalStageFlag == false && gameOverFlag == false)
            {
                interactable = true;
            }
            else
            {
                interactable = false;
            }

            //check within Confirm button for game ending condition
            if (event.getX() > getWidth() / 4 && event.getX() < (float) ((getWidth() / 4) * 3) &&
                    event.getY() < (float) ((getHeight() / 3)*1.1) + 4*(getHeight()/11) && event.getY() > (float) (getHeight() / 3.25) + 4*(getHeight()/11))
            {
                if ( levelVictoryFlag == true || gameOverFlag == true )
                {
                    Log.d(TAG, "ATTEMPTING TO END THREAD\nATTEMPTING TO END THREAD\nATTEMPTING TO END THREAD\n");
                    thread.setmFinished( true );
                    thread.setRunning( false );

                    activityFinish = true;
                }

            }//ends if statement


            if ( levelBeginFlag == true )  // if in initial info menu for level, allow confirm button to be hit
            {
                //check within Confirm button
                if (event.getX() > getWidth() / 4 && event.getX() < (float) ((getWidth() / 4) * 3) &&
                        event.getY() < (float) ((getHeight() / 3)*1.1) + 4*(getHeight()/11) && event.getY() > (float) (getHeight() / 3.25) + 4*(getHeight()/11))
                {
                    buttonSelect = 0;
                    levelBeginFlag = false;
                    finalTrialFlag = false;
                    thread.setMenuUp(false);
                }//ends else if statement
            }//ends else if
            else  if ( finalTrialFlag == true )  // if in final trial menu, allow confirm button to be hit
            {
                //check within Confirm button
                if (event.getX() > getWidth() / 4 && event.getX() < (float) ((getWidth() / 4) * 3) &&
                        event.getY() < (float) ((getHeight() / 3)*1.1) + 4*(getHeight()/11) && event.getY() > (float) (getHeight() / 3.25) + 4*(getHeight()/11))
                {
                    buttonSelect = 0;
                    levelBeginFlag = false;
                    finalTrialFlag = false;
                    finalStageFlag = true;

                    //Spawn 15 predators for the final trial///////
                    numPreds = 15;

                    predVelocity = 7;
                    predHealth = 200;
                    predStrength = 15;

                    Random randSeed = new Random();


                    for( int i = 0; i < 15; i++ )
                    {
                        pred[i] = new Predator(BitmapFactory.decodeResource(getResources(), R.drawable.pred),randDir.nextInt(getWidth()) ,
                                randSeed.nextInt(getHeight() - getHeight() / 5) + getHeight()/10, predVelocity, predHealth, predStrength, predIDCounter);
                        predIDCounter++;
                    }

                    thread.setMenuUp(false);

                }//ends else if statement
            }//ends else if


            //if touched within game-field, place appropriate object
            if ( event.getY() > getHeight()/10 && event.getY() < getHeight()*0.9 )
            {
                if ( buttonSelect == 1) //if Predators button selected
                {
                    //place predator
                    if (numPreds < maxPreds )
                    {
                        pred[numPreds] = new Predator(BitmapFactory.decodeResource(getResources(), R.drawable.pred), (int) event.getX() , (int) event.getY(),predVelocity, predHealth, predStrength, predIDCounter );
                        numPreds++;
                        predIDCounter++;
                        predIDCounter = predIDCounter % 1000000;
                    }//ends if statement
                }//ends if statement
                else if ( buttonSelect == 2 )   // if food button is selected
                {
                    //place food
                    if ( (numFood + foodPerBlock) < maxFood )
                    {
                        for ( int i = 0; i < foodPerBlock; i++ )
                        {
                            food[numFood] = new Food(BitmapFactory.decodeResource(getResources(), R.drawable.food), (int) event.getX(), (int) event.getY());
                            numFood++;
                        }
                    }
                }

                /************** Cells button disables for level 2 ****************************************************
                 else if ( buttonSelect == 3)    // if Cells button selected
                 {
                 //place cell
                 if ( numCells < maxCells )
                 {
                 cell[numCells] = new Cell(BitmapFactory.decodeResource(getResources(), R.drawable.cell), (int) event.getX() , (int) event.getY() );
                 if (cell[numCells].getMaxHealth() < 30 )
                 {
                 cell[numCells].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell50percent));
                 }
                 else if (cell[numCells].getMaxHealth() < 50 )
                 {
                 cell[numCells].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell75percent));
                 }
                 else if ( cell[numCells].getMaxHealth() < 100 )
                 {
                 cell[numCells].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell));
                 }
                 else if ( cell[numCells].getMaxHealth() < 150 )
                 {
                 cell[numCells].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell125percent));
                 }
                 else if ( cell[numCells].getMaxHealth() < 200 )
                 {
                 cell[numCells].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell150percent));
                 }
                 else
                 {
                 cell[numCells].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell200percent));
                 }

                 numCells++;
                 }
                 }*/
                //**************************************************************************
            }//ends if statement

            //Check within Predators Button
            if ( event.getX() > getWidth()/35 && event.getX() < (float) (getWidth()/4.25) &&
                    event.getY() < ( getHeight() - (getHeight()/35) ) && event.getY() > ( getHeight() - ( getHeight()/12 ) ) && interactable == true  )
            {
                if (buttonSelect != 1 )
                {
                    buttonSelect = 1;
                    thread.setMenuUp(false);
                }
                else
                {
                    buttonSelect = 0;
                }
            }//ends if statement

            // Check within Food Button
            if ( event.getX() > (getWidth()*16)/60 && event.getX() <  (getWidth()/4 - getWidth()/30) + (getWidth()*16)/60 &&
                    event.getY() < (getHeight() - (getHeight()/35)) && event.getY() > ( getHeight() - ( getHeight()/12 ) )  && interactable == true )
            {
                if (buttonSelect != 2 )
                {
                    buttonSelect = 2;
                    thread.setMenuUp(false);
                }
                else
                {
                    buttonSelect = 0;
                }
            }//ends if statement

            /////Cells button disabled for level 2********************************
            /*
            //Check within Cells button
            if ( event.getX() > (getWidth()*31)/60 && event.getX() <  (getWidth()/4 - getWidth()/30) + (getWidth()*31)/60 &&
                    event.getY() < (getHeight() - (getHeight()/35)) && event.getY() > ( getHeight() - ( getHeight()/12 ) )  && interactable == true  )
            {
                if (buttonSelect != 3 )
                {
                    buttonSelect = 3;
                    thread.setMenuUp(false);
                }
                else
                {
                    buttonSelect = 0;
                }
            }//ends if statement
            */
            //////**********************************************************************

            //Check within More button
            if ( event.getX() > (getWidth()*46)/60 && event.getX() < (getWidth()/4 - getWidth()/30) + (getWidth()*45)/60 &&
                    event.getY() < (getHeight() - (getHeight()/35)) && event.getY() > ( getHeight() - ( getHeight()/12 ) ) && interactable == true  )
            {
                if (buttonSelect != 4 )
                {
                    buttonSelect = 4;
                    thread.setMenuUp(true);
                }
                else
                {
                    buttonSelect = 0;
                    thread.setMenuUp(false);
                }
            }//ends if statement

            //check menu button press/////////
            if(thread.getMenuUp() == true )
            {
                if ( buttonSelect == 4 )  //if in primary MORE menu
                {
                    //check within Predator settings button
                    if (event.getX() > getWidth() / 4 && event.getX() < (float) ((getWidth() / 4) * 3) &&
                            event.getY() < (float) ((getHeight() / 4.5) * 1.25) && event.getY() > (float) (getHeight() / 4.5))
                    {
                        if (buttonSelect != 5)
                        {
                            buttonSelect = 5;
                            thread.setMenuUp(true);
                        }//ends if statement
                        else
                        {
                            buttonSelect = 0;
                            thread.setMenuUp(false);
                        }//ends else
                    }//ends if statement
                    //check within Food settings button
                    else if (event.getX() > getWidth() / 4 && event.getX() < (float) ((getWidth() / 4) * 3) &&
                            event.getY() < (float) ((getHeight() / 3)*1.1) && event.getY() > (float) (getHeight() / 3.25))
                    {
                        if (buttonSelect != 6)
                        {
                            buttonSelect = 6;
                            thread.setMenuUp(true);
                        }//ends if statement
                        else
                        {
                            buttonSelect = 0;
                            thread.setMenuUp(false);
                        }//ends else
                    }//ends if statement
                    //check within Clear Predators button
                    else if (event.getX() > getWidth() / 4 && event.getX() < (float) ((getWidth() / 4) * 3) &&
                            event.getY() < (float) ((getHeight() / 3)*1.1) + getHeight()/11 && event.getY() > (float) (getHeight() / 3.25) + getHeight()/11)
                    {
                        if (clearSelect != 1)
                        {
                            clearSelect = 1;
                        }//ends if statement
                        else
                        {
                            clearSelect = 0;
                        }//ends else
                    }//ends if statement
                    //**** Clear Dish button disabled for level 2
                    //check within Clear Dish button
                    /*else if (event.getX() > getWidth() / 4 && event.getX() < (float) ((getWidth() / 4) * 3) &&
                            event.getY() < (float) ((getHeight() / 3)*1.1) + 2*(getHeight()/11) && event.getY() > (float) (getHeight() / 3.25) + 2*(getHeight()/11))
                    {
                        if (clearSelect != 2)
                        {
                            clearSelect = 2;
                        }//ends if statement
                        else
                        {
                            clearSelect = 0;
                        }//ends else
                    }//ends if statement*/

                    //check within Confirm button
                    else if (event.getX() > getWidth() / 4 && event.getX() < (float) ((getWidth() / 4) * 3) &&
                            event.getY() < (float) ((getHeight() / 3)*1.1) + 4*(getHeight()/11) && event.getY() > (float) (getHeight() / 3.25) + 4*(getHeight()/11))
                    {

                        if (clearSelect == 1)   //if clear Predators has been selected
                        {
                            numPreds = 0;
                            clearSelect = 0;
                            buttonSelect = 0;
                            thread.setMenuUp(false);
                        }//ends if statement
                        else if ( clearSelect == 2 )    //if clear dish has been selected
                        {
                            numPreds = 0;
                            numFood = 0;
                            numCells = 0;

                            buttonSelect = 0;
                            clearSelect = 0;
                            thread.setMenuUp(false);
                        }//ends else
                        else
                        {
                            buttonSelect = 0;
                            clearSelect = 0;
                            thread.setMenuUp(false);
                        }
                    }//ends if statement


                }//ends if
                else if ( buttonSelect == 5 )   //if in predator settings menu
                {
                    //Check within first minus button, affects velocity
                    if (event.getX() > getWidth()/5 + getWidth()/40 && event.getX() < getWidth()/5 + getWidth()/8 &&
                            event.getY() < getHeight()/5 + getWidth()/8  && event.getY() >  getHeight()/5 + getWidth()/40)
                    {
                        if( predVelocity > 0 )
                        {
                            predVelocity--;

                            for ( int i = 0; i < numPreds; i++ )
                            {
                                pred[i].setVelocity( predVelocity );
                            }
                        }
                        else
                        {
                            predVelocity = 100;

                            for ( int i = 0; i < numPreds; i++ )
                            {
                                pred[i].setVelocity( predVelocity );
                            }
                        }
                    }//ends if
                    //check within first plus button, affects velocity
                    else if (event.getX() > (float) (getWidth()*0.8) - getWidth()/8 && event.getX() < (float) (getWidth()*0.8) - getWidth()/40 &&
                            event.getY() < getHeight()/5 + getWidth()/8  && event.getY() >  getHeight()/5 + getWidth()/40)
                    {
                        if ( predVelocity < 100 )
                        {
                            predVelocity++;

                            for ( int i = 0; i < numPreds; i++ )
                            {
                                pred[i].setVelocity( predVelocity );
                            }
                        }
                        else
                        {
                            predVelocity = 0;

                            for ( int i = 0; i < numPreds; i++ )
                            {
                                pred[i].setVelocity( predVelocity );
                            }
                        }
                    }//ends else if
                    //check within second minus button, affects strength
                    else if (event.getX() > getWidth()/5 + getWidth()/40 && event.getX() < getWidth()/5 + getWidth()/8 &&
                            event.getY() < (float) (getHeight()/5 + getWidth()/4.2)  && event.getY() >   getHeight()/5 + getWidth()/7)
                    {
                        if( predStrength > 0 )
                        {
                            predStrength--;

                            for ( int i = 0; i < numPreds; i++ )
                            {
                                pred[i].setStrength( predStrength );
                            }
                        }
                        else
                        {
                            predStrength = 100;

                            for ( int i = 0; i < numPreds; i++ )
                            {
                                pred[i].setStrength( predStrength );
                            }
                        }
                    }//ends else if
                    //check within second plus button, affects strength
                    else if (event.getX() > (float) (getWidth()*0.8) - getWidth()/8 && event.getX() < (float) (getWidth()*0.8) - getWidth()/40 &&
                            event.getY() < (float) (getHeight()/5 + getWidth()/4.2)   && event.getY() >  getHeight()/5 + getWidth()/7)
                    {
                        if ( predStrength < 100 )
                        {
                            predStrength++;

                            for ( int i = 0; i < numPreds; i++ )
                            {
                                pred[i].setStrength( predStrength );
                            }
                        }
                        else
                        {
                            predStrength = 0;

                            for ( int i = 0; i < numPreds; i++ )
                            {
                                pred[i].setStrength( predStrength );
                            }
                        }
                    }//ends else if
                    //check within third minus button, affects health
                    else if (event.getX() > getWidth()/5 + getWidth()/40 && event.getX() < getWidth()/5 + getWidth()/8 &&
                            event.getY() < (float) (getHeight()/5 + getWidth()/2.85)   && event.getY() >  (float) (getHeight()/5 + getWidth()/3.9))
                    {
                        if( predHealth > 1 )
                        {
                            if ( predHealth >= 125 )
                            {
                                predHealth -= 25;
                            }
                            else if ( predHealth >= 15 )
                            {
                                predHealth -= 5;
                            }
                            else
                            {
                                predHealth--;
                            }

                            for ( int i = 0; i < numPreds; i++ )
                            {
                                pred[i].setMaxHealth( predHealth );
                                pred[i].setHealth( pred[i].getMaxHealth() );
                            }
                        }//ends if
                        else
                        {
                            predHealth = 1000;

                            for ( int i = 0; i < numPreds; i++ )
                            {
                                pred[i].setMaxHealth( predHealth );
                                pred[i].setHealth( pred[i].getMaxHealth() );
                            }
                        }//ends else
                    }//ends else if
                    //check within third plus button, affects health
                    else if (event.getX() > (float) (getWidth()*0.8) - getWidth()/8 && event.getX() < (float) (getWidth()*0.8) - getWidth()/40 &&
                            event.getY() < (float) (getHeight()/5 + getWidth()/2.85)    && event.getY() >  (float) (getHeight()/5 + getWidth()/3.9))
                    {
                        if ( predHealth < 1000 )
                        {
                            if ( predHealth >= 100 )
                            {
                                predHealth += 25;
                            }
                            else if ( predHealth >= 10)
                            {
                                predHealth += 5;
                            }
                            else
                            {
                                predHealth++;
                            }


                            for ( int i = 0; i < numPreds; i++ )
                            {
                                pred[i].setMaxHealth( predHealth );
                                pred[i].setHealth( pred[i].getMaxHealth() );
                            }
                        }
                        else
                        {
                            predHealth = 1;

                            for ( int i = 0; i < numPreds; i++ )
                            {
                                pred[i].setMaxHealth( predHealth );
                                pred[i].setHealth( pred[i].getMaxHealth() );
                            }
                        }//ends else
                    }//ends else if

                    //check within Confirm button
                    else if (event.getX() > getWidth() / 4 && event.getX() < (float) ((getWidth() / 4) * 3) &&
                            event.getY() < (float) ((getHeight() / 3)*1.1) + 4*(getHeight()/11) && event.getY() > (float) (getHeight() / 3.25) + 4*(getHeight()/11))
                    {
                        buttonSelect = 4;
                    }//ends else if statement

                }//ends else if
                else if ( buttonSelect == 6 )   //if in food settings menu
                {
                    //Check within first minus button, affects food per block
                    if (event.getX() > getWidth()/5 + getWidth()/40 && event.getX() < getWidth()/5 + getWidth()/8 &&
                            event.getY() < getHeight()/5 + getWidth()/8  && event.getY() >  getHeight()/5 + getWidth()/40)
                    {
                        if( foodPerBlock > 1 )
                        {
                            foodPerBlock--;
                        }
                        else
                        {
                            foodPerBlock = 6;
                        }
                    }
                    //check within first plus button, affects food per block
                    else if (event.getX() > (float) (getWidth()*0.8) - getWidth()/8 && event.getX() < (float) (getWidth()*0.8) - getWidth()/40 &&
                            event.getY() < getHeight()/5 + getWidth()/8  && event.getY() >  getHeight()/5 + getWidth()/40)
                    {
                        if ( foodPerBlock < 6 )
                        {
                            foodPerBlock++;
                        }
                        else
                        {
                            foodPerBlock = 1;
                        }
                    }//ends else if

                    //check within second minus button, affects feed speed
                    else if (event.getX() > getWidth()/5 + getWidth()/40 && event.getX() < getWidth()/5 + getWidth()/8 &&
                            event.getY() < (float) (getHeight()/5 + getWidth()/4.2)  && event.getY() >   getHeight()/5 + getWidth()/7)
                    {
                        if( automaticFood == false )
                        {
                            automaticFood = true;
                            feedSpeed = 21;         //set feedSpeed to fast
                        }
                        else if ( feedSpeed == 21 )
                        {
                            feedSpeed = 51;         // set to normal
                        }
                        else if ( feedSpeed == 51 )
                        {
                            feedSpeed = 91;        // set to slow
                        }
                        else
                        {
                            automaticFood = false;  //set to off
                        }
                    }//ends else if
                    //check within second plus button, affects feed speed
                    else if (event.getX() > (float) (getWidth()*0.8) - getWidth()/8 && event.getX() < (float) (getWidth()*0.8) - getWidth()/40 &&
                            event.getY() < (float) (getHeight()/5 + getWidth()/4.2)   && event.getY() >  getHeight()/5 + getWidth()/7)
                    {
                        if( automaticFood == false )
                        {
                            automaticFood = true;
                            feedSpeed = 91;         //set feedSpeed to slow
                        }
                        else if ( feedSpeed == 21 )
                        {
                            automaticFood = false;  //set to off
                        }
                        else if ( feedSpeed == 51 )
                        {
                            feedSpeed = 21;        // set to fast
                        }
                        else
                        {
                            feedSpeed = 51;         //set to normal
                        }
                    }//ends else if

                    //check within Confirm button
                    else if (event.getX() > getWidth() / 4 && event.getX() < (float) ((getWidth() / 4) * 3) &&
                            event.getY() < (float) ((getHeight() / 3)*1.1) + 4*(getHeight()/11) && event.getY() > (float) (getHeight() / 3.25) + 4*(getHeight()/11))
                    {
                        buttonSelect = 4;
                    }//ends else if statement

                }//ends else if

            }//ends if statement
            if ( event.getX() > (float)(getWidth()*0.8)  && event.getX() < (float) (getWidth()*0.95)  && event.getY() >(float)(getHeight()*0.03) && event.getY() < getHeight()*0.08)
            {
                statsScroll++;
                statsScroll = statsScroll%2;
            }
        }//ends if statement
        return super.onTouchEvent(event);
    }//ends onTouchEvent method

    @Override   //onDraw draws every object on the screen every time it is called
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        //Average stats to be calculated
        avgVelocity = 0;
        //avgVigilance = 0;
        avgFear = 0;
        avgHealth = 0;
        avgStrength = 0;

        //Highest gen to be calculated
        highestGen = 0;

        //background color
        //canvas.drawColor(Color.rgb(79, 40, 200));   //Sui-Blue (Blui)
        canvas.drawColor(Color.rgb(69, 46, 130 ) );  //testing colors

        //Draw food
        for ( int i = 0; i < numFood; i++ )
        {
            food[i].draw(canvas);
        }

        //Draw cells, calculate average stats
        for(int k = 0; k < numCells; k++)
        {
            cell[k].draw(canvas);

            if ( cell[k].getGeneration() > highestGen )
            {
                highestGen = cell[k].getGeneration();
            }

            //avgVigilance += cell[k].getVigilance();
            avgVelocity += cell[k].getVelocity();
            avgFear += cell[k].getFear();
            avgHealth += cell[k].getMaxHealth();
            avgStrength += cell[k].getStrength();

        }//ends for loop

        //Draw predators
        for ( int i = 0; i < numPreds; i++ )
        {
            pred[i].draw(canvas);
            //predVelocity = pred[i].getVelocity();
        }

        //Calculate average stats
        if (numCells > 0)
        {
            avgVelocity /= numCells;
            // avgVigilance /= numCells;
            avgFear /= numCells;
            avgHealth /= numCells;
            avgStrength /= numCells;
        }
        else
        {
            avgVelocity = 0;
            // avgVigilance = 0;
            avgFear = 0;
            avgHealth = 0;
            avgStrength = 0;
        }



        //round avg stats to 2 decimal places
        avgVelocity = round(avgVelocity, 2);
        //avgVigilance = round(avgVigilance, 2);
        avgFear = round(avgFear, 2);
        avgHealth = round(avgHealth, 2);
        avgStrength = round(avgStrength, 2);


        //convert avg stats to strings for use as text
        String savgVelocity = Float.toString(avgVelocity);
        String savgVigilance = Float.toString(avgVigilance);
        String savgFear = Float.toString(avgFear);
        String savgHealth = Float.toString(avgHealth);
        String savgStrength = Float.toString(avgStrength);
        String snumPreds = Integer.toString(numPreds);
        String smaxPreds = Integer.toString(maxPreds);
        String snumCells = Integer.toString(numCells);
        String smaxCells = Integer.toString(maxCells);


        String shighestGen = Integer.toString(highestGen);

        //convert Predator stats to strings for settings menu
        if(numPreds > 0)
        {
            String sPredSpeed = Float.toString(predVelocity);
        }//ends if statement


        //set rect paint
        rektPaint = new Paint();
        //0-255
        rektPaint.setColor(Color.rgb(0,150,150));   //aqua color


        //Draw upper rectangle for top UI stuff
        //Left, top, right, bottom, paint
        canvas.drawRect(0, 0,getWidth() , getHeight() / 10 , rektPaint);

        //Draw lower rectangle for bottom UI stuff
        //Left, top, right, bottom, paint
        canvas.drawRect(0, getHeight() - ( getHeight()/10) , getWidth(), getHeight(), rektPaint  );


        if ( levelBeginFlag == true )  //If the game has just begun, and the initial info panel must be shown
        {
            //create white border rectangle
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(255, 255, 255)); //white color

            menuBorderRect = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            menuBorderRect.set(getWidth() / 5 - getWidth() / 75, getHeight() / 5 - getWidth() / 75, (float) (getWidth() * 0.8) + getWidth() / 75, (float) (getHeight() * 0.8) + getWidth() / 75);

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( menuBorderRect, 15, 15, rektPaint );


            //create primary menu rectangle
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(0, 150, 150)); //aqua color

            menuRect = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            menuRect.set(getWidth() / 5, getHeight() / 5, (float) (getWidth() * 0.8), (float) (getHeight() * 0.8));

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(menuRect, 15, 15, rektPaint);

            //Info text for level 2////////////////////////////////////////////////
            Typeface font = Typeface.create("Arial", Typeface.BOLD);

            //set text paint
            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 24));//getHeight / 40 previously
            textPaint.setTypeface(font);

            canvas.drawText("Level 2", (float) (getWidth() / 2.3), (float) (getHeight() / 3.9), textPaint);

            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize( (float) (getWidth() / 26) );

            canvas.drawText("It's time to fight the power!", (float) (getWidth() / 3.7), (float) (getHeight() / 3.2), textPaint);
            canvas.drawText("You have 30 generations to", (float) (getWidth() / 3.7), (float) (getHeight() / 2.85), textPaint);
            canvas.drawText("make some scary cells with a", (float) (getWidth() / 3.7), (float) (getHeight() / 2.55), textPaint);
            canvas.drawText("taste for predators. ", (float) (getWidth() / 3.7), (float) (getHeight() / 2.3), textPaint);
            canvas.drawText("Good Luck!",(float) (getWidth() / 3.7), (float) (getHeight() / 2.1), textPaint);

            //Confirm button////////////////////////////////////////////
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb ( 100, 100, 200 ) );
            confButtonRect = new RectF();

            confButtonRect.set(getWidth() / 4,(float) (getHeight() / 3.25) + 4*(getHeight()/11), (float) ((getWidth() /4) * 3), (float) ((getHeight() / 3)*1.1) + 4*(getHeight()/11));
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(confButtonRect, 15, 15, rektPaint);

            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 26));
            canvas.drawText("Confirm",(float) (getWidth() / 2.4), (float) ((getHeight() / 3) * 1.31) + 3*(getHeight()/11), textPaint);
        }//ends else if
        else if ( finalTrialFlag == true )  //If it's time for the final trial, the trial info panel must be shown
        {
            //create white border rectangle
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(255, 255, 255)); //white color

            menuBorderRect = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            menuBorderRect.set(getWidth() / 5 - getWidth() / 75, getHeight() / 5 - getWidth() / 75, (float) (getWidth() * 0.8) + getWidth() / 75, (float) (getHeight() * 0.8) + getWidth() / 75);

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( menuBorderRect, 15, 15, rektPaint );


            //create primary menu rectangle
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(0, 150, 150)); //aqua color

            menuRect = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            menuRect.set(getWidth() / 5, getHeight() / 5, (float) (getWidth() * 0.8), (float) (getHeight() * 0.8));

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(menuRect, 15, 15, rektPaint);

            //Info text for level 2////////////////////////////////////////////////
            Typeface font = Typeface.create("Arial", Typeface.BOLD);

            //set text paint
            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 24));//getHeight / 40 previously
            textPaint.setTypeface(font);

            canvas.drawText("It's Time!", (float) (getWidth() / 2.3), (float) (getHeight() / 3.9), textPaint);

            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize( (float) (getWidth() / 26) );

            canvas.drawText("Time to see if your cells will", (float) (getWidth() / 3.7), (float) (getHeight() / 3.2), textPaint);
            canvas.drawText("make you proud. They have ", (float) (getWidth() / 3.7), (float) (getHeight() / 2.85), textPaint);
            canvas.drawText("20 seconds to brutally kill", (float) (getWidth() / 3.7), (float) (getHeight() / 2.55), textPaint);
            canvas.drawText("a group of innocent predators.", (float) (getWidth() / 3.7), (float) (getHeight() / 2.3), textPaint);
            canvas.drawText("Proceed when mentally",(float) (getWidth() / 3.7), (float) (getHeight() / 2.1), textPaint);
            canvas.drawText("prepared.",(float) (getWidth() / 3.7), (float) (getHeight() / 1.95), textPaint);

            //Confirm button////////////////////////////////////////////
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb ( 100, 100, 200 ) );
            confButtonRect = new RectF();

            confButtonRect.set(getWidth() / 4,(float) (getHeight() / 3.25) + 4*(getHeight()/11), (float) ((getWidth() /4) * 3), (float) ((getHeight() / 3)*1.1) + 4*(getHeight()/11));
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(confButtonRect, 15, 15, rektPaint);

            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 26));
            canvas.drawText("Confirm",(float) (getWidth() / 2.4), (float) ((getHeight() / 3) * 1.31) + 3*(getHeight()/11), textPaint);
        }//ends else if
        else if ( levelVictoryFlag == true )  //If the user beat the level, show the victory panel
        {
            //create white border rectangle
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(255, 255, 255)); //white color

            menuBorderRect = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            menuBorderRect.set(getWidth() / 5 - getWidth() / 75, getHeight() / 5 - getWidth() / 75, (float) (getWidth() * 0.8) + getWidth() / 75, (float) (getHeight() * 0.8) + getWidth() / 75);

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( menuBorderRect, 15, 15, rektPaint );


            //create primary menu rectangle
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(0, 150, 150)); //aqua color

            menuRect = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            menuRect.set(getWidth() / 5, getHeight() / 5, (float) (getWidth() * 0.8), (float) (getHeight() * 0.8));

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(menuRect, 15, 15, rektPaint);

            //Info text for level 2////////////////////////////////////////////////
            Typeface font = Typeface.create("Arial", Typeface.BOLD);

            //set text paint
            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 24));//getHeight / 40 previously
            textPaint.setTypeface(font);

            canvas.drawText("Level Cleared!", (float) (getWidth() / 2.7), (float) (getHeight() / 3.9), textPaint);

            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize( (float) (getWidth() / 26) );

            canvas.drawText("Look at me.", (float) (getWidth() / 3.7), (float) (getHeight() / 3.2), textPaint);
            canvas.drawText("I am the predator now.", (float) (getWidth() / 3.7), (float) (getHeight() / 2.85), textPaint);
            canvas.drawText("", (float) (getWidth() / 3.7), (float) (getHeight() / 2.55), textPaint);
            canvas.drawText("", (float) (getWidth() / 3.7), (float) (getHeight() / 2.3), textPaint);
            canvas.drawText("",(float) (getWidth() / 3.7), (float) (getHeight() / 2.1), textPaint);

            //Confirm button////////////////////////////////////////////
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb ( 100, 100, 200 ) );
            confButtonRect = new RectF();

            confButtonRect.set(getWidth() / 4,(float) (getHeight() / 3.25) + 4*(getHeight()/11), (float) ((getWidth() /4) * 3), (float) ((getHeight() / 3)*1.1) + 4*(getHeight()/11));
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(confButtonRect, 15, 15, rektPaint);

            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 26));
            canvas.drawText("Confirm",(float) (getWidth() / 2.4), (float) ((getHeight() / 3) * 1.31) + 3*(getHeight()/11), textPaint);
        }//ends else if
        else if ( gameOverFlag == true )  //If the user failed the level, show them the failure panel. Losers.
        {
            //create white border rectangle
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(255, 255, 255)); //white color

            menuBorderRect = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            menuBorderRect.set(getWidth() / 5 - getWidth() / 75, getHeight() / 5 - getWidth() / 75, (float) (getWidth() * 0.8) + getWidth() / 75, (float) (getHeight() * 0.8) + getWidth() / 75);

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( menuBorderRect, 15, 15, rektPaint );


            //create primary menu rectangle
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(0, 150, 150)); //aqua color

            menuRect = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            menuRect.set(getWidth() / 5, getHeight() / 5, (float) (getWidth() * 0.8), (float) (getHeight() * 0.8));

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(menuRect, 15, 15, rektPaint);

            //Info text for level 2////////////////////////////////////////////////
            Typeface font = Typeface.create("Arial", Typeface.BOLD);

            //set text paint
            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 24));//getHeight / 40 previously
            textPaint.setTypeface(font);

            canvas.drawText("Level Failed!", (float) (getWidth() / 2.7), (float) (getHeight() / 3.9), textPaint);

            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize( (float) (getWidth() / 26) );

            canvas.drawText("Your cells just weren't", (float) (getWidth() / 3.7), (float) (getHeight() / 3.2), textPaint);
            canvas.drawText("tough enough. Maybe give", (float) (getWidth() / 3.7), (float) (getHeight() / 2.85), textPaint);
            canvas.drawText("them a nice talking to,", (float) (getWidth() / 3.7), (float) (getHeight() / 2.55), textPaint);
            canvas.drawText("the sissies.", (float) (getWidth() / 3.7), (float) (getHeight() / 2.3), textPaint);
            canvas.drawText("",(float) (getWidth() / 3.7), (float) (getHeight() / 2.1), textPaint);

            //Confirm button////////////////////////////////////////////
           rektPaint = new Paint();
            rektPaint.setColor(Color.rgb ( 100, 100, 200 ) );
            confButtonRect = new RectF();

            confButtonRect.set(getWidth() / 4,(float) (getHeight() / 3.25) + 4*(getHeight()/11), (float) ((getWidth() /4) * 3), (float) ((getHeight() / 3)*1.1) + 4*(getHeight()/11));
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(confButtonRect, 15, 15, rektPaint);

            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 26));
            canvas.drawText("Confirm",(float) (getWidth() / 2.4), (float) ((getHeight() / 3) * 1.31) + 3*(getHeight()/11), textPaint);
        }//ends else if


        //Highlight selected button, if necessary
        if (buttonSelect == 1)  //highlight Predators button if selected
        {
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(255, 255, 0 ) ); //highlight in yellow

            highlightRect = new RectF();
            //Set dimensions of rectangle:  left, top, right, bottom
            highlightRect.set( (float) (getWidth()/38), getHeight() - (float) ( getHeight()/11.5 ),(float) (getWidth()/4.18), getHeight() - (getHeight()/37) );
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( highlightRect, 15, 15, rektPaint );
        }
        else if (buttonSelect == 2)  //highlight Food button if selected
        {
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(255, 255, 0 ) ); //highlight in yellow

            highlightRect = new RectF();
            //Set dimensions of rectangle:  left, top, right, bottom
            highlightRect.set( (getWidth()*16)/61 , getHeight() - (float) ( getHeight()/11.5 ), (getWidth()/4 - getWidth()/30) + (getWidth()*16)/59, getHeight() - (getHeight()/37) );
            //highlightRect.set( 50, 50, 1000, 1000 );    //testing box
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( highlightRect, 15, 15, rektPaint );
        }

        //************ Cells button disabled for level 2 ************************
        /*
        else if (buttonSelect == 3)  //highlight Cells button if selected
        {
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(255, 255, 0 ) ); //highlight in yellow

            highlightRect = new RectF();
            //Set dimensions of rectangle:  left, top, right, bottom
            highlightRect.set( (float) ((getWidth()*31)/60.5) , getHeight() - (float) ( getHeight()/11.5 ), (getWidth()/4 - getWidth()/30) + (float) ((getWidth()*31)/59.5), getHeight() - (getHeight()/37));
            //highlightRect.set( 50, 50, 1000, 1000 );    //testing box
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( highlightRect, 15, 15, rektPaint );
        } */
        else if (buttonSelect == 4) //Draw MORE menu if selected
        {
            //create white border rectangle
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(255, 255, 255)); //white color

            menuBorderRect = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            menuBorderRect.set(getWidth() / 5 - getWidth() / 75, getHeight() / 5 - getWidth() / 75, (float) (getWidth() * 0.8) + getWidth() / 75, (float) (getHeight() * 0.8) + getWidth() / 75);

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( menuBorderRect, 15, 15, rektPaint );


            //create primary menu rectangle
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(0, 150, 150)); //aqua color

            menuRect = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            menuRect.set(getWidth() / 5, getHeight() / 5, (float) (getWidth() * 0.8), (float) (getHeight() * 0.8));

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(menuRect, 15, 15, rektPaint);


            //Menu Buttons and Text///////////////////////////////////////////////////////////////////////////////
            //predator settings button////////////////////////////////
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(150, 0, 0)); //red color
            predSettingsRect = new RectF();

            //set dimensions of rect     //left, top, right bottom
            predSettingsRect.set(getWidth() / 4,(float) (getHeight() / 4.5), (float) ((getWidth() /4) * 3), (float) ((getHeight() / 4.5) * 1.25));
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(predSettingsRect, 15, 15, rektPaint);

            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 26));
            canvas.drawText("Predator Settings",(float) (getWidth() / 2.8), (float) (getHeight() / 3.9), textPaint);

            //Food settings button////////////////////////////////////
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(220, 130, 0)); //orange color
            foodSettingsRect = new RectF();

            //set dimensions of rect     //left, top, right bottom
            foodSettingsRect.set(getWidth() / 4,(float) (getHeight() / 3.25), (float) ((getWidth() /4) * 3), (float) ((getHeight() / 3)*1.1));
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(foodSettingsRect, 15, 15, rektPaint);

            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 26));
            canvas.drawText("Food Settings",(float) (getWidth() / 2.6), (float) (getHeight() / 2.9), textPaint);

            //Clear Highlight Rectangles////////////////////////////////////////////
            //clear highlight 1/////////////////////////
            if ( clearSelect == 1 )
            {
                rektPaint = new Paint();
                rektPaint.setColor(Color.rgb(255, 255, 0));
                clearHighlightRect1 = new RectF();

                //set dimensions of rect     //left, top, right bottom
                clearHighlightRect1.set(getWidth() / 4 - getWidth() / 100, (float) (getHeight() / 3.25) + getHeight() / 11 - getWidth() / 100,
                        (float) ((getWidth() / 4) * 3) + getWidth() / 100, (float) ((getHeight() / 3) * 1.1) + getHeight() / 11 + getWidth() / 100);
                //rectangle, x-radius of arc, y-radius of arc, paint
                canvas.drawRoundRect(clearHighlightRect1, 15, 15, rektPaint);
            }
            else if ( clearSelect == 2 )
            {
                rektPaint = new Paint();
                rektPaint.setColor(Color.rgb(255, 255, 0));
                clearHighlightRect2 = new RectF();

                //set dimensions of rect     //left, top, right bottom
                clearHighlightRect2.set(getWidth() / 4 - getWidth()/100 ,(float) (getHeight() / 3.25) + 2*(getHeight()/11) - getWidth()/100,
                        (float) ((getWidth() /4) * 3) + getWidth()/100, (float) ((getHeight() / 3)*1.1) + 2*(getHeight()/11) + getWidth()/100);
                //rectangle, x-radius of arc, y-radius of arc, paint
                canvas.drawRoundRect(clearHighlightRect2, 15, 15, rektPaint);
            }


            //Clear Preds button///////////////////////////////////////
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb ( 220, 100, 100 ) );
            clearButton1 = new RectF();

            //set dimensions of rect     //left, top, right bottom
            clearButton1.set(getWidth() / 4,(float) (getHeight() / 3.25) + getHeight()/11, (float) ((getWidth() /4) * 3), (float) ((getHeight() / 3)*1.1) + getHeight()/11);
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(clearButton1, 15, 15, rektPaint);

            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 26));
            canvas.drawText("Clear Predators",(float) (getWidth() / 2.7), (float) ((getHeight() / 3) * 1.31), textPaint);

            //Clear Dish button///////////////////////////////////////
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb ( 10, 50, 10 ) );
            clearButton2 = new RectF();

            //set dimensions of rect     //left, top, right bottom
            clearButton2.set(getWidth() / 4,(float) (getHeight() / 3.25) + 2*(getHeight()/11), (float) ((getWidth() /4) * 3), (float) ((getHeight() / 3)*1.1) + 2*(getHeight()/11));
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(clearButton2, 15, 15, rektPaint);

            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 26));
            canvas.drawText("[Blocked]",(float) (getWidth() / 2.5), (float) ((getHeight() / 3) * 1.31) + getHeight()/11, textPaint);

            //Confirm button////////////////////////////////////////////
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb ( 100, 100, 200 ) );
            confButtonRect = new RectF();

            confButtonRect.set(getWidth() / 4,(float) (getHeight() / 3.25) + 4*(getHeight()/11), (float) ((getWidth() /4) * 3), (float) ((getHeight() / 3)*1.1) + 4*(getHeight()/11));
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(confButtonRect, 15, 15, rektPaint);

            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 26));
            canvas.drawText("Confirm",(float) (getWidth() / 2.4), (float) ((getHeight() / 3) * 1.31) + 3*(getHeight()/11), textPaint);


        }//ends else if
        else if(buttonSelect == 5)   //predator settings menu is selected
        {
            //create white border rectangle
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(255, 255, 255)); //white color

            menuBorderRect = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            menuBorderRect.set(getWidth() / 5 - getWidth() / 75, getHeight() / 5 - getWidth() / 75, (float) (getWidth() * 0.8) + getWidth() / 75, (float) (getHeight() * 0.8) + getWidth() / 75);

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( menuBorderRect, 15, 15, rektPaint );

            //create primary menu rectangle
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(150, 0, 0)); //red color

            menuRect = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            menuRect.set(getWidth() / 5, getHeight() / 5, (float) (getWidth() * 0.8), (float) (getHeight() * 0.8));

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(menuRect, 15, 15, rektPaint);


            //Draw Settings
            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 26));
            canvas.drawText("Speed: " + predVelocity, (float) (getWidth() / 2.5), (float) (getHeight() / 3.9), textPaint);
            canvas.drawText("Strength: " + predStrength, (float) (getWidth() / 2.5), (float) (getHeight() / 3.1), textPaint );
            canvas.drawText("Health: " + predHealth, (float) (getWidth() / 2.5), (float) (getHeight() / 2.55), textPaint );



            //Draw Plus/minus buttons

            //first minus//////////////////////////////////////
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(0, 150, 150 ) ); //aqua color

            minusRectOne = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            minusRectOne.set( getWidth()/5 + getWidth()/40 , getHeight()/5 + getWidth()/40  , getWidth()/5 + getWidth()/8  , getHeight()/5 + getWidth()/8  );

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( minusRectOne, 15, 15, rektPaint );

            //first plus//////////////////////////////////////////
            plusRectOne = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            plusRectOne.set( (float) (getWidth()*0.8) - getWidth()/8 , getHeight()/5 + getWidth()/40  , (float) (getWidth()*0.8) - getWidth()/40 , getHeight()/5 + getWidth()/8  );

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( plusRectOne, 15, 15, rektPaint );

            //second minus/////////////////////////////////////////
            minusRectTwo = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            minusRectTwo.set( getWidth()/5 + getWidth()/40 , getHeight()/5 + getWidth()/7  , getWidth()/5 + getWidth()/8  , (float) (getHeight()/5 + getWidth()/4.2)  );

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( minusRectTwo, 15, 15, rektPaint );

            //second plus/////////////////////////////////////////
            plusRectTwo = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            plusRectTwo.set( (float) (getWidth()*0.8) - getWidth()/8 , getHeight()/5 + getWidth()/7  , (float) (getWidth()*0.8) - getWidth()/40 , (float) (getHeight()/5 + getWidth()/4.2)  );

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( plusRectTwo, 15, 15, rektPaint );

            //third minus/////////////////////////////////////////
            minusRectThree = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            minusRectThree.set( getWidth()/5 + getWidth()/40 , (float) (getHeight()/5 + getWidth()/3.9)  , getWidth()/5 + getWidth()/8  , (float) (getHeight()/5 + getWidth()/2.85)  );

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( minusRectThree, 15, 15, rektPaint );

            //third plus/////////////////////////////////////////
            plusRectThree = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            plusRectThree.set( (float) (getWidth()*0.8) - getWidth()/8 , (float) (getHeight()/5 + getWidth()/3.9)   , (float) (getWidth()*0.8) - getWidth()/40 , (float) (getHeight()/5 + getWidth()/2.85)  );

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( plusRectThree, 15, 15, rektPaint );

            //Confirm button////////////////////////////////////////////
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb ( 100, 100, 200 ) );
            confButtonRect = new RectF();

            confButtonRect.set(getWidth() / 4,(float) (getHeight() / 3.25) + 4*(getHeight()/11), (float) ((getWidth() /4) * 3), (float) ((getHeight() / 3)*1.1) + 4*(getHeight()/11));
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(confButtonRect, 15, 15, rektPaint);

            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 26));
            canvas.drawText("Confirm",(float) (getWidth() / 2.4), (float) ((getHeight() / 3) * 1.31) + 3*(getHeight()/11), textPaint);

        }//ends else if
        else if(buttonSelect == 6)   //Food settings menu is selected
        {
            //create white border rectangle////////////////////////
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(255, 255, 255)); //white color

            menuBorderRect = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            menuBorderRect.set(getWidth() / 5 - getWidth() / 75, getHeight() / 5 - getWidth() / 75, (float) (getWidth() * 0.8) + getWidth() / 75, (float) (getHeight() * 0.8) + getWidth() / 75);

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( menuBorderRect, 15, 15, rektPaint );

            //create primary menu rectangle//////////////////////
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(200, 105, 0)); //orange color

            menuRect = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            menuRect.set(getWidth() / 5, getHeight() / 5, (float) (getWidth() * 0.8), (float) (getHeight() * 0.8));

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(menuRect, 15, 15, rektPaint);

            //draw text for food menu//////////////////////////////////
            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 26));
            canvas.drawText("Food Per Block: " + foodPerBlock, (float) (getWidth() / 2.8), (float) (getHeight() / 3.9), textPaint);
            String sFeedSpeed;
            if ( automaticFood == false )
            {
                sFeedSpeed = "Off";
            }
            else if ( feedSpeed == 21 )
            {
                sFeedSpeed = "Fast";
            }
            else if ( feedSpeed == 51 )
            {
                sFeedSpeed = "Norm.";
            }
            else
            {
                sFeedSpeed = "Slow";
            }
            canvas.drawText("Auto-Feed: " + sFeedSpeed, (float) (getWidth() / 2.75), (float) (getHeight() / 3.1), textPaint );

            //draw plus/minus rectangles///////////////////////////////////////////////////////////////////////

            //first minus//////////////////////////////////////
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(0, 150, 150 ) ); //aqua color

            minusRectOne = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            minusRectOne.set( getWidth()/5 + getWidth()/40 , getHeight()/5 + getWidth()/40  , getWidth()/5 + getWidth()/8  , getHeight()/5 + getWidth()/8  );

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( minusRectOne, 15, 15, rektPaint );


            //first plus//////////////////////////////////////////
            plusRectOne = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            plusRectOne.set( (float) (getWidth()*0.8) - getWidth()/8 , getHeight()/5 + getWidth()/40  , (float) (getWidth()*0.8) - getWidth()/40 , getHeight()/5 + getWidth()/8  );

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( plusRectOne, 15, 15, rektPaint );


            //second minus/////////////////////////////////////////
            minusRectTwo = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            minusRectTwo.set( getWidth()/5 + getWidth()/40 , getHeight()/5 + getWidth()/7  , getWidth()/5 + getWidth()/8  , (float) (getHeight()/5 + getWidth()/4.2)  );

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( minusRectTwo, 15, 15, rektPaint );


            //second plus/////////////////////////////////////////
            plusRectTwo = new RectF();

            //Set dimensions of rectangle:  left, top, right, bottom
            plusRectTwo.set( (float) (getWidth()*0.8) - getWidth()/8 , getHeight()/5 + getWidth()/7  , (float) (getWidth()*0.8) - getWidth()/40 , (float) (getHeight()/5 + getWidth()/4.2)  );

            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect( plusRectTwo, 15, 15, rektPaint );

            //Confirm button////////////////////////////////////////////
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb ( 100, 100, 200 ) );
            confButtonRect = new RectF();

            confButtonRect.set(getWidth() / 4,(float) (getHeight() / 3.25) + 4*(getHeight()/11), (float) ((getWidth() /4) * 3), (float) ((getHeight() / 3)*1.1) + 4*(getHeight()/11));
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(confButtonRect, 15, 15, rektPaint);

            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 26));
            canvas.drawText("Confirm",(float) (getWidth() / 2.4), (float) ((getHeight() / 3) * 1.31) + 3*(getHeight()/11), textPaint);

        }//ends else if


        if ( finalStageFlag == false ) //only draw buttons if we are not in the final trial stage
        {
            //Draw buttons for bottom UI //////////

            //predator button
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(150, 0, 0)); //red color
            predButtonRect = new RectF();

            //set dimensions of rect     //left, top, right bottom
            predButtonRect.set(getWidth() / 35, getHeight() - (getHeight() / 12), (float) (getWidth() / 4.25), getHeight() - (getHeight() / 35));
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(predButtonRect, 15, 15, rektPaint);

            //Food Button////////////////
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(220, 130, 0)); // food color

            foodButtonRect = new RectF();
            //set dimensions of rect     //left, top, right bottom
            foodButtonRect.set((getWidth() * 16) / 60, getHeight() - (getHeight() / 12), (getWidth() / 4 - getWidth() / 30) + (getWidth() * 16) / 60, getHeight() - (getHeight() / 35));
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(foodButtonRect, 15, 15, rektPaint);

            // ****************   Cells button disabled for level 2///////////////////////////////////////
            //Spawn Cells Button////////////////
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(10, 50, 10)); //black color

            //set dimensions of rect     //left, top, right bottom
            cellButtonRect.set((getWidth() * 31) / 60, getHeight() - (getHeight() / 12), (getWidth() / 4 - getWidth() / 30) + (getWidth() * 31) / 60, getHeight() - (getHeight() / 35));
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(cellButtonRect, 15, 15, rektPaint);



            //MORE button///////////////////
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(100, 100, 100)); //Grey color

            //set dimensions of rect     //left, top, right bottom
            moreButtonRect.set((getWidth() * 46) / 60, getHeight() - (getHeight() / 12), (getWidth() / 4 - getWidth() / 30) + (getWidth() * 45) / 60, getHeight() - (getHeight() / 35));
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(moreButtonRect, 15, 15, rektPaint);

            //Stats Button
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(69, 46, 130)); //highlight in yellow

            statsButton = new RectF();
            //Set dimensions of rectangle:  left, top, right, bottom
            statsButton.set((float) (getWidth() * 0.8), (float) (getHeight() * 0.03), (float) (getWidth() * 0.95), (float) (getHeight() * 0.08));
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(statsButton, 15, 15, rektPaint);
            // statsIcon.createBitmap( BitmapFactory.decodeResource(getResources(), R.drawable.stats));
            canvas.drawBitmap(statsIcon, (float) (getWidth() * 0.81), (float) (getHeight() * 0.033), null);


            //Text for bottom UI
            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 26));
            canvas.drawText("Predators", getWidth() / 22, getHeight() - (getHeight() / 20), textPaint);
            canvas.drawText("Food", (float) (getWidth() * 16.5) / 50, getHeight() - (getHeight() / 20), textPaint);
            canvas.drawText("[Blocked]", (float) (getWidth() * 29) / 53, getHeight() - (getHeight() / 20), textPaint);
            canvas.drawText("More", (float) (getWidth() * 41) / 50, getHeight() - (getHeight() / 20), textPaint);
            /////////////////////////////end buttons///////////////////
        }
        else    //if in the final stage, need timer display
        {
            textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize((float) (getWidth() / 22) );

            canvas.drawText("Time Remaining: " + finalStageTimer , getWidth() / 22, getHeight() - (getHeight() / 20), textPaint);

            //Stats Button
            rektPaint = new Paint();
            rektPaint.setColor(Color.rgb(69, 46, 130)); //highlight in yellow

            statsButton = new RectF();
            //Set dimensions of rectangle:  left, top, right, bottom
            statsButton.set((float) (getWidth() * 0.8), (float) (getHeight() * 0.03), (float) (getWidth() * 0.95), (float) (getHeight() * 0.08));
            //rectangle, x-radius of arc, y-radius of arc, paint
            canvas.drawRoundRect(statsButton, 15, 15, rektPaint);
            // statsIcon.createBitmap( BitmapFactory.decodeResource(getResources(), R.drawable.stats));
            canvas.drawBitmap(statsIcon, (float) (getWidth() * 0.81), (float) (getHeight() * 0.033), null);
        }


        Typeface font = Typeface.create("Arial", Typeface.BOLD);

        //set text paint
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize((float) (getWidth() / 24));//getHeight / 40 previously
        textPaint.setTypeface(font);


        //Text for top UI
        if( statsScroll == 0) {
            canvas.drawText("Avg Fear  " + savgFear, getWidth() / 40, getHeight() / 22, textPaint);
            canvas.drawText("Avg Speed " + savgVelocity, getWidth() / 40, getHeight() / 13, textPaint);
            canvas.drawText("Avg HP " + savgHealth, (float) (getWidth()/40 + getWidth()/2.9), getHeight()/22, textPaint);
            canvas.drawText("Avg Strength " + savgStrength, (float) (getWidth()/40 + getWidth()/2.9), getHeight()/13, textPaint);
        }
        else if( statsScroll == 1) {
            canvas.drawText("Cell Population " + snumCells + " / " + smaxCells , (float) (getWidth()/40), getHeight()/22, textPaint  );
            canvas.drawText("Pred Population " + snumPreds + " / " + smaxPreds, (float) (getWidth()/40), getHeight()/13, textPaint );
            canvas.drawText("Latest Gen. " + shighestGen, (float) (getWidth()/40 + getWidth()/2.2), getHeight()/ 22, textPaint);
        }
        else if( statsScroll == 2){

        }
        else if( statsScroll == 3){

        }
        else if( statsScroll == 4){

        }
        else if( statsScroll == 5){

        }
        else if( statsScroll == 6){

        }





    }//ends onDraw method


    //function to update components of game
    public void update()
    {
        //check if user has lost, i.e. all cells are dead
        if ( numCells <= 0 )
        {
            gameOverFlag = true;
            thread.setMenuUp( true );
        }

        //Determine food placement if necessary
        if ( automaticFood == true && feedTimer % feedSpeed == 0 )
        {
            numFood = autoFeed(getWidth(), getHeight(), food, maxFood, numFood, foodPerBlock );
        }
        feedTimer++;
        feedTimer = feedTimer % feedSpeed;



        //Determine behavior for preds
        for ( int i = 0; i < numPreds; i++ )
        {
            //Wall wrap for Predator
            pred[i].wrap(getWidth(), getHeight());

            //Find closest cell for predator
            pred[i].findClosestCell(getWidth(), getHeight(), numCells, cell);

            //Choose direction for predator
            pred[i].chooseDirection(getWidth(), getHeight(), numCells);

            //avoid overlapping of predators
            pred[i].avoidOverlap(pred, numPreds);

            //update predator
            pred[i].update();
        }//ends for-loop


        //Determine behavior for cells
        for(int j = 0; j < numCells; j++)
        {
            //wrap cells around screen
            cell[j].wrap(getWidth(), getHeight());

            //find closest predator for cell
            cell[j].findClosestPredator( getWidth(), getHeight(), numPreds, pred);

            //destroy cell if it is eaten
            numCells = cell[j].checkEaten( numCells, cell, numPreds, pred );

            //kill nearest predator if cell has killed it
            numPreds = cell[j].killPred( numPreds, pred );

            //find closest food to cell
            cell[j].findClosestFood( getWidth(), getHeight(), numFood, food);

            //Eat food if cell is close enough
            numFood = cell[j].eatFood( numFood, food, numCells, maxCells );

            numCellCounter1 = numCells;

            //Reproduce if appropriate
            numCells = cell[j].reproduce( numCells, maxCells, cell );

            numCellCounter2 = numCells;

            if ( numCellCounter2 - numCellCounter1 != 0 )   //if a new cell has been born, redo bitmaps
            {
                //assign bitmaps for cells based on health/size
                for ( int i = 0; i < numCells; i++ )
                {
                    if (cell[i].getMaxHealth() < 30 )
                    {
                        cell[i].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell50percent));
                    }
                    else if (cell[i].getMaxHealth() < 50 )
                    {
                        cell[i].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell75percent));
                    }
                    else if ( cell[i].getMaxHealth() < 100 )
                    {
                        cell[i].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell));
                    }
                    else if ( cell[i].getMaxHealth() < 150 )
                    {
                        cell[i].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell125percent));
                    }
                    else if ( cell[i].getMaxHealth() < 200 )
                    {
                        cell[i].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell150percent));
                    }
                    else
                    {
                        cell[i].setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cell200percent));
                    }

                }
            }

            //choose direction for cell
            cell[j].chooseDirection( getWidth(), getHeight(), numPreds, pred, numFood, food );

            //update cell
            cell[j].update();
        }//ends for loop


        //Check for final trial condition
        if ( highestGen >= 30 && finalStageFlag == false)
        {
            numPreds = 0;
            buttonSelect = 0;
            finalTrialFlag = true;
            thread.setMenuUp(true);
        }

        if ( numCells < 1 && finalStageFlag == true )
        {
            finalStageTimer = 0;
        }

        //Manage timer and end condition in the final stage
        if ( finalStageFlag == true )
        {
            finalStageTimerHelper++;

            if( finalStageTimerHelper % 35 == 0 && finalStageTimer > 0 )
            {
                finalStageTimer--;
                finalStageTimerHelper = 1;
            }//ends if

            if ( numPreds <= 0 && numCells > 0)
            {
                levelVictoryFlag = true;
                thread.setMenuUp(true);
            }

            if ( finalStageTimer <= 0 )
            {
                if ( numCells > 0 && numPreds <= 0 )
                {
                    levelVictoryFlag = true;
                    thread.setMenuUp(true);
                }//ends if
                else
                {
                    gameOverFlag = true;
                    thread.setMenuUp(true);
                }//ends else
            }//ends if
        }//ends if

    }//ends update method

    //Function to round decimals
    public static float round(float d, int decimalPlace)
    {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    //Function to place food automatically
    public int autoFeed( int screenWidth, int screenHeight, Food foodArray[], int maxFood, int numFood, int foodPerBlock)
    {
        Random randSeed = new Random();

        //find random placement spot
        int randX = randSeed.nextInt( screenWidth );
        int randY = randSeed.nextInt( (int) (screenHeight * 0.8) ) + (int) ( screenHeight * 0.1);   //within appropriate range on screen

        //place food
        if ( (numFood + foodPerBlock) < maxFood )
        {
            for ( int i = 0; i < foodPerBlock; i++ )
            {
                food[numFood] = new Food(BitmapFactory.decodeResource(getResources(), R.drawable.food), randX, randY);
                numFood++;
            }
        }

        return numFood;
    }

}//ends LevelwView class

