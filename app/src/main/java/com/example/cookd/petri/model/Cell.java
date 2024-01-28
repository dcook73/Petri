package com.example.cookd.petri.model;

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.cookd.petri.R;
import com.example.cookd.petri.model.components.Speed;

import java.util.Random;

import com.example.cookd.petri.model.Cell;
import com.example.cookd.petri.model.Food;
import com.example.cookd.petri.model.Predator;
import com.example.cookd.petri.model.components.Speed;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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
 * Date: 1/6/2016
 * Class for the cells
 */
public class Cell
{
    private Bitmap bitmap;
    private int x;
    private int y;
    private boolean touched;
    public Speed speed;

    private int vigilance;
    private int velocity;
    private int fear;
    private int strength;
    private int maxHealth;
    private int health;

    private Predator closestPred;
    private int predRadius;

    private Food closestFood;
    private int foodRadius;
    private int foodCount;

    private int generation;

    //Parameterized constructor
    public Cell(Bitmap bitmap, int x, int y)
    {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.speed = new Speed();

        Random randSeed = new Random();

        this.vigilance = randSeed.nextInt(91) + 10;  //random vigilance within range 10 - 100

        this.velocity = randSeed.nextInt(8) + 2;     //random speed within range 2 - 9

        this.fear = randSeed.nextInt(11) - 5;      //random fear within range -5 to 5

        this.strength = randSeed.nextInt(11);        //random strength 0 - 10

        this.maxHealth = randSeed.nextInt(76) + 25;    //random maxHealth from 25 - 100

        this.health = maxHealth;

        this.speed.setXv(velocity);
        this.speed.setYv(velocity);

        this.foodCount = 0;
        this.foodRadius = 999999;

        this.predRadius = 999999;

        this.generation = 0;

        //limits for health/speed ratio
        if (this.maxHealth >= 200 )
        {
            if( this.velocity > 7)
            {
                this.velocity = 7;
            }//ends if
        }//ends if
        else if ( this.maxHealth >= 150 )
        {
            if ( this.velocity > 10 )
            {
                this.velocity = 10;
            }//ends if
        }//ends else if
        else if ( this.maxHealth >= 100 )
        {
            if (this.velocity > 15)
            {
                this.velocity = 15;
            }//ends if
        }//ends else if
        else if ( this.maxHealth >= 50 )
        {
            if (this.velocity > 25 )
            {
                this.velocity = 25;
            }//ends if
        }//ends else if
        else if ( this.maxHealth >= 30 )
        {
            if ( this.velocity > 35 )
            {
                this.velocity = 35;
            }//ends if
        }//ends else if

    }//ends Cell constructor

    //Parameterized constructor with parent cell, for cells of successive generations
    public Cell(Bitmap bitmap, Cell parentCell )
    {
        this.bitmap = bitmap;

        Random randSeed = new Random();

        this.x = parentCell.getX() + randSeed.nextInt(100) - 50;  //new x is +- 50 of old
        this.y = parentCell.getY() + randSeed.nextInt(100) - 50;  //new y is +- 50 of old

        this.speed = new Speed();

        this.vigilance = Math.abs(parentCell.getVigilance() + randSeed.nextInt(7) - 3 );  //new vigilance is -3 to +3 of old

        this.velocity = Math.abs(parentCell.getVelocity() + randSeed.nextInt(5) - 2 );    //new velocity is -2 to + 2 of old

        this.fear = parentCell.getFear() + randSeed.nextInt(5) - 2;   //new fear is -2 to +2 of old

        this.maxHealth =  Math.abs(parentCell.getMaxHealth() + randSeed.nextInt(31) - 15);  //new maxHealth is -30 to +30 of old
        this.health = maxHealth;

        this.strength = Math.abs(parentCell.getStrength() + randSeed.nextInt(5) - 2);   //new Strength is -2 to +2 of old

        this.speed.setXv(velocity);
        this.speed.setYv(velocity);

        this.foodCount = 0;
        this.foodRadius = 999999;

        this.predRadius = 999999;

        this.generation = parentCell.getGeneration() + 1;

        //limits for health/speed ratio
        if (this.maxHealth >= 200 )
        {
            if( this.velocity > 7)
            {
                this.velocity = 7;
            }//ends if
        }//ends if
        else if ( this.maxHealth >= 150 )
        {
            if ( this.velocity > 10 )
            {
                this.velocity = 10;
            }//ends if
        }//ends else if
        else if ( this.maxHealth >= 100 )
        {
            if (this.velocity > 15)
            {
                this.velocity = 15;
            }//ends if
        }//ends else if
        else if ( this.maxHealth >= 50 )
        {
            if (this.velocity > 25 )
            {
                this.velocity = 25;
            }//ends if
        }//ends else if
        else if ( this.maxHealth >= 30 )
        {
            if ( this.velocity > 35 )
            {
                this.velocity = 35;
            }//ends if
        }//ends else if
    }//ends constructor

    public Bitmap getBitmap()
    {
        return bitmap;
    }//ends getBitmap method

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }//ends setBitmap method

    public int getX()
    {
        return x;
    }//ends getX method

    public void setX(int x)
    {
        this.x = x;
    }//ends setX method

    public int getY()
    {
        return y;
    }//end getY method

    public void setY(int y)
    {
        this.y = y;
    }//ends setY method

    public boolean isTouched()
    {
        return touched;
    }//ends isTouched method

    public void setTouched(boolean touched)
    {
        this.touched = touched;
    }//ends setTouched method

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y -( bitmap.getHeight() / 2), null);
    }//ends draw method

    public void update()
    {

        if(!touched)
        {
            x += (speed.getXv() * speed.getxDir());
            y += (speed.getYv() * speed.getyDir());
        }//ends if statement
    }//ends update method

    public Speed getSpeed()
    {
        return speed;
    }//Fuck Speed and all you stand for, you piece of shit-ass fuck

    public int getVigilance()
    {
        return vigilance;
    }

    public void setVigilance( int newVig )
    {
        this.vigilance = newVig;
    }

    public int getVelocity()
    {
        return velocity;
    }

    public void setVelocity( int newVel )
    {
        this.velocity = newVel;
    }

    public int getFoodCount()
    {
        return this.foodCount;
    }

    public void setFoodCount( int newFoodCount )
    {
        this.foodCount = newFoodCount;
    }

    public int getGeneration()
    {
        return this.generation;
    }

    public void setGeneration( int newGeneration )
    {
        this.generation = newGeneration;
    }

    public int getFear()
    {
        return this.fear;
    }

    public void setFear( int newFear )
    {
        this.fear = newFear;
    }

    public int getMaxHealth()
    {
        return this.maxHealth;
    }

    public void setMaxHealth( int newMaxHealth )
    {
        this.maxHealth = newMaxHealth;
    }

    public int getStrength()
    {
        return this.strength;
    }

    public void setStrength( int newStrength )
    {
        this.strength = newStrength;
    }

    public int getHealth()
    {
        return this.health;
    }

    public void setHealth( int newHealth )
    {
        this.health = newHealth;
    }


    //function to swap the stats of two cells
    public void swapStats( Cell otherCell )
    {
        //Swap stats using temp variables
        int tempVel = this.getVelocity();
        int tempVig = this.getVigilance();
        int tempFoodCount = this.getFoodCount();
        int tempGeneration = this.getGeneration();
        int tempFear = this.getFear();
        int tempMaxHealth = this.getMaxHealth();
        int tempHealth = this.getHealth();
        int tempStrength = this.getStrength();
        Bitmap tempBitmap = this.getBitmap();

        this.setVigilance( otherCell.getVigilance() );
        this.setVelocity( otherCell.getVelocity() );
        this.setFoodCount( otherCell.getFoodCount() );
        this.setGeneration( otherCell.getGeneration() );
        this.setFear( otherCell.getFear() );
        this.setMaxHealth( otherCell.getMaxHealth() );
        this.setHealth( otherCell.getHealth() );
        this.setStrength( otherCell.getStrength() );
        this.setBitmap( otherCell.getBitmap() );

        otherCell.setVigilance(tempVig);
        otherCell.setVelocity(tempVel);
        otherCell.setFoodCount(tempFoodCount);
        otherCell.setGeneration(tempGeneration);
        otherCell.setFear(tempFear);
        otherCell.setMaxHealth( tempMaxHealth );
        otherCell.setHealth( tempHealth );
        otherCell.setStrength( tempStrength );
        otherCell.setBitmap( tempBitmap );

        //Set speed XV and YV with new velocities so behavior is correct
        this.getSpeed().setXv( this.getVelocity() );
        this.getSpeed().setYv( this.getVelocity() );

        otherCell.getSpeed().setXv(otherCell.getVelocity());
        otherCell.getSpeed().setYv(otherCell.getVelocity());
    }//ends swapstats function

    //Function to wrap cells around screen
    public void wrap( int screenWidth, int screenHeight)
    {
        //right wall collision check
        if (this.getSpeed().getxDir() == Speed.DIRECTION_RIGHT && this.getX() - this.getBitmap().getWidth()/ 2 >= screenWidth )
        {
            //cell[j].getSpeed().toggleXDirection();
            this.setX(0);

        }//ends if statement

        //left wall collision check
        if (this.getSpeed().getxDir() == Speed.DIRECTION_LEFT && this.getX() - this.getBitmap().getWidth() / 2 <= 0)
        {
            //cell[j].getSpeed().toggleXDirection();
            this.setX(screenWidth );

        }//ends if statement

        //bottom wall collision check
        if (this.getSpeed().getyDir() == Speed.DIRECTION_DOWN && this.getY() + this.getBitmap().getHeight() / 2 >= ( screenHeight * 0.9) )
        {
            //cell[j].getSpeed().toggleYDirection();
            this.setY( screenHeight/10 );

        }//ends if statement

        //top wall collision check
        if (this.getSpeed().getyDir() == Speed.DIRECTION_UP && this.getY() - this.getBitmap().getHeight() / 2 <= (screenHeight / 10) )
        {
            //cell[j].getSpeed().toggleYDirection();
            this.setY( (int) (screenHeight * 0.9) );

        }//ends if statement
    }

    //Function to destroy a cell when it is eaten
    public int checkEaten( int numCells, Cell cell[], int numPreds, Predator pred[] )
    {
        if ( numPreds > 0 && this.predRadius < 50  )
        {
            Random randSeed = new Random();

            int predX;
            int predY;

            //take appropriate damage
            this.health -= closestPred.getStrength();

            if ( this.getFear() < 0 ) //inflict damage to predator if cell ain't no bitch
            {
                closestPred.setHealth( closestPred.getHealth() - this.getStrength() );
            }
            /// Predator will die in killPred function if its health has fallen below 0////

            //spasm cell in pain :(
            this.x += ( randSeed.nextInt(3) - 1 ) * 15;   //jump -15, 0, or +15
            this.y += ( randSeed.nextInt(3) - 1 ) * 15;   //jump -15, 0, or +15

            if ( this.getFear() < 0 )
            {
                //spasm predator in pain :) (?)
                predX = closestPred.getX();
                predX += (randSeed.nextInt(3) - 1) * 15;   //jump -15, 0, or +15
                closestPred.setX(predX);
            }

            predY = closestPred.getY();
            predY += ( randSeed.nextInt(3) - 1 ) * 15;   //jump -15, 0, or +15
            closestPred.setY( predY );

            //If we've witnessed the murder of a cell
            if ( this.health <= 0 )
            {
                closestPred.setHealth( closestPred.getMaxHealth() );   //predator heals if it gets the kill

                this.setX(cell[numCells - 1].getX());
                this.setY(cell[numCells - 1].getY());

                this.swapStats(cell[numCells - 1]);  //swap stats so death is correct

                numCells--;
            }
        }

        return numCells;
    }

    //function to kill nearest predator when necessary
    public int killPred( int numPreds, Predator pred[] )
    {
       //if we've witnissed the murder of a predator
       if ( numPreds > 0 && closestPred.getHealth() <= 0 )
       {
           closestPred.setX(pred[numPreds -1].getX() );
           closestPred.setY(pred[numPreds-1].getY() );

           closestPred.setHealth( pred[numPreds-1].getHealth() );

           numPreds--;

           this.foodCount += 2;  //Cell will gain food if it kills a predator
           this.setHealth(this.getMaxHealth());   //Cell regains health if it kills a predator
       }

        return numPreds;
    }


    //Function to find the closest predator to cell
    public void findClosestPredator( int screenWidth, int screenHeight, int numPreds, Predator pred[])
    {
        int closestDist = 999999;
        int absoluteDist;
        int xDiff, yDiff, closerDiffX, closerDiffY;
        double xDiffWrap, yDiffWrap;

        //Find closest predator
        for ( int i = 0; i < numPreds; i++ )
        {
            //calculate conventional distances
            xDiff = pred[i].getX() - this.getX();
            yDiff = pred[i].getY() - this.getY();

            //calculate differences accross wrapped screen
            if ( xDiff >= 0 )  //if cell is to the right of pred
            {
                xDiffWrap = this.getX() + (screenWidth - pred[i].getX()); // x-distance across wrapped screen
            }
            else  //if cell is to the left of pred
            {
                xDiffWrap = pred[i].getX()  + (screenWidth - this.getX()); // x-distance across wrapped screen
            }

            if ( yDiff >= 0 )  //if cell is below pred
            {
                yDiffWrap = this.getY() +  (screenHeight*0.8 - pred[i].getY() ) ; //y-distance across wrapped screen
            }
            else // if cell is above pred
            {
                yDiffWrap = pred[i].getY() + (screenHeight*0.8 - this.getY() );  //y-distance across wrapped screen
            }

            //determine closer distance
            xDiff = Math.abs(xDiff);
            yDiff = Math.abs(yDiff);
            if( yDiff < yDiffWrap)
            {
                closerDiffY = yDiff;
            }
            else
            {
                closerDiffY = (int) yDiffWrap;
            }

            if( xDiff < xDiffWrap)
            {
                closerDiffX = xDiff;
            }
            else
            {
                closerDiffX = (int) xDiffWrap;
            }

            absoluteDist = (int) Math.sqrt(Math.pow(closerDiffX, 2) + Math.pow(closerDiffY, 2));

            if(absoluteDist < closestDist)
            {
                this.closestPred = pred[i];
                closestDist = absoluteDist;
                this.predRadius = absoluteDist;
            }//ends if statement
        } // ends for loop
    }//ends findClosestPredator function

    //Function to find the closest Food to cell
    public void findClosestFood( int screenWidth, int screenHeight, int numFood, Food food[])
    {
        int closestDist = 999999;
        int absoluteDist;
        int xDiff, yDiff, closerDiffX, closerDiffY;
        double xDiffWrap, yDiffWrap;

        //Find closest predator
        for ( int i = 0; i < numFood; i++ )
        {
            //calculate conventional distances
            xDiff = food[i].getX() - this.getX();
            yDiff = food[i].getY() - this.getY();

            //calculate differences accross wrapped screen
            if ( xDiff >= 0 )  //if cell is to the right of pred
            {
                xDiffWrap = this.getX() + (screenWidth - food[i].getX()); // x-distance across wrapped screen
            }
            else  //if cell is to the left of pred
            {
                xDiffWrap = food[i].getX()  + (screenWidth - this.getX()); // x-distance across wrapped screen
            }

            if ( yDiff >= 0 )  //if cell is below pred
            {
                yDiffWrap = this.getY() +  (screenHeight*0.8 - food[i].getY() ) ; //y-distance across wrapped screen
            }
            else // if cell is above pred
            {
                yDiffWrap = food[i].getY() + (screenHeight*0.8 - this.getY() );  //y-distance across wrapped screen
            }

            //determine closer distance
            xDiff = Math.abs(xDiff);
            yDiff = Math.abs(yDiff);
            if( yDiff < yDiffWrap)
            {
                closerDiffY = yDiff;
            }
            else
            {
                closerDiffY = (int) yDiffWrap;
            }

            if( xDiff < xDiffWrap)
            {
                closerDiffX = xDiff;
            }
            else
            {
                closerDiffX = (int) xDiffWrap;
            }

            absoluteDist = (int) Math.sqrt(Math.pow(closerDiffX, 2) + Math.pow(closerDiffY, 2));

            if(absoluteDist < closestDist)
            {
                this.closestFood = food[i];
                closestDist = absoluteDist;
                this.foodRadius = absoluteDist;
            }//ends if statement
        } // ends for loop
    }//ends findClosestFood function

    //Function for the cell to eat food
    public int eatFood ( int numFood, Food food[], int numCells, int maxCells )
    {
        if ( this.foodRadius < 50 && numFood > 0 ) //if closest food is within eating range
        {
            //Replenish health
            this.health = this.maxHealth;

            //Swap/Delete food in array
            int tempX;
            int tempY;

            tempX = this.closestFood.getX();
            tempY = this.closestFood.getY();

            this.closestFood.setX( food[numFood - 1].getX() );
            this.closestFood.setY( food[numFood - 1].getY() );

            food[numFood - 1].setX( tempX );
            food[numFood - 1].setY( tempY );

            numFood--;

            if ( numCells < maxCells )
            {
                this.foodCount++;
            }

            this.foodRadius = 999999;   //reset foodRadius arbitrarily large
        }

        return numFood;
    }



    //Function to choose direction for cell
    public void chooseDirection( int screenWidth, int screenHeight, int numPreds,Predator pred[], int numFood, Food food[] )
    {
        int currxDir, curryDir, rndxDir, rndyDir;

        Random randSeed = new Random();

        if (numPreds < 1 && numFood < 1)   //if no predators and no food, just move at random
        {
            currxDir = this.getSpeed().getxDir();
            curryDir = this.getSpeed().getyDir();


            //bias toward current direction
            if (randSeed.nextInt(100) > 90)
            {
                rndxDir = randSeed.nextInt(3) - 1;
                rndyDir = randSeed.nextInt(3) - 1;

                this.getSpeed().setxDir(rndxDir);
                this.getSpeed().setyDir(rndyDir);
            } //ends if
            else
            {
                this.getSpeed().setxDir(currxDir);
                this.getSpeed().setyDir(curryDir);
            }//ends else
        }//ends if statement
        else if ( numFood < 1 || this.predRadius < this.foodRadius || this.predRadius < 100 )  //if cell should be concerned with pred
        {
           this.reactToPred( screenWidth, screenHeight, numPreds, pred );
        }
        else    //if cell should be concerned with food
        {
            this.goToFood( screenWidth, screenHeight, numFood, food );
        }

    } //ends chooseDirection method

    //function for cell to react to predator; either run from or run to
    public void reactToPred( int screenWidth, int screenHeight, int numPreds, Predator pred[]  )
    {
        int xDiff, yDiff, closerDiffY, closerDiffX, urgency,
                currxDir, curryDir, rndxDir, rndyDir;
        double xDiffWrap, yDiffWrap;

        Random randSeed = new Random();

        if ( this.predRadius == 999999 )
        {
            this.closestPred = pred[numPreds - 1];
        }

        //calculate conventional distances
        xDiff = this.getX() - this.closestPred.getX();
        yDiff = this.getY() - this.closestPred.getY();

        //calculate differences accross wrapped screen
        if (xDiff >= 0)  //if cell is to the right of pred
        {
            xDiffWrap = this.closestPred.getX() + (screenWidth - this.getX()); // x-distance across wrapped screen
        } else  //if cell is to the left of pred
        {
            xDiffWrap = this.getX() + (screenWidth - this.closestPred.getX()); // x-distance across wrapped screen
        }

        if (yDiff >= 0)  //if cell is below pred
        {
            yDiffWrap = this.closestPred.getY() + (screenHeight * 0.8 - this.getY()); //y-distance across wrapped screen
        } else // if cell is above pred
        {
            yDiffWrap = this.getY() + (screenHeight * 0.8 - this.closestPred.getY());  //y-distance across wrapped screen
        }

        //determine closer distance
        xDiff = Math.abs(xDiff);
        yDiff = Math.abs(yDiff);
        if (yDiff < yDiffWrap) {
            closerDiffY = yDiff;
        } else {
            closerDiffY = (int) yDiffWrap;
        }

        if (xDiff < xDiffWrap) {
            closerDiffX = xDiff;
        } else {
            closerDiffX = (int) xDiffWrap;
        }


        predRadius = (int) Math.sqrt(Math.pow(closerDiffX, 2) + Math.pow(closerDiffY, 2));


        if (predRadius < 400) {
            urgency = 400 - predRadius;

        }//ends if statement
        else {
            urgency = 0;
        }//ends else

        //Assign direction:
        //React to stimuli, and random behavior
        if (randSeed.nextInt(400) < urgency)     //if predator sufficiently near
        {
            if (randSeed.nextInt(100) < Math.abs(this.getFear()) * 10 ) //if passes vigilance/fear check
            {
                if (this.getX() > this.closestPred.getX())   //if cell to the right of pred
                {
                    if (xDiff < xDiffWrap) {
                        this.getSpeed().setxDir(Speed.DIRECTION_RIGHT);
                    } else {
                        this.getSpeed().setxDir(Speed.DIRECTION_LEFT);
                    }
                }//ends if statement
                else    // if cell to the left of pred
                {
                    if (xDiff < xDiffWrap) {
                        this.getSpeed().setxDir(Speed.DIRECTION_LEFT);
                    } else {
                        this.getSpeed().setxDir(Speed.DIRECTION_RIGHT);
                    }
                }//ends else

                if (this.getY() > this.closestPred.getY())      // if cell below pred
                {
                    if (yDiff < yDiffWrap) {
                        this.getSpeed().setyDir(Speed.DIRECTION_DOWN);
                    } else {
                        this.getSpeed().setyDir(Speed.DIRECTION_UP);
                    }
                }//ends if statement
                else        // if cell above pred
                {
                    if (yDiff < yDiffWrap) {
                        this.getSpeed().setyDir(Speed.DIRECTION_UP);
                    } else {
                        this.getSpeed().setyDir(Speed.DIRECTION_DOWN);
                    }
                }//ends else

                if (Math.pow(closerDiffX, 2) > Math.pow(closerDiffY, 2) + 2500)      //To avoid diagonal-only
                {
                    this.getSpeed().setyDir(Speed.DIRECTION_NONE);
                }//ends if statement
                else if (Math.pow(closerDiffY, 2) > Math.pow(closerDiffX, 2) + 2500) //To avoid diagonal-onlay  <-- Nick's a fuck up
                {
                    this.getSpeed().setxDir(Speed.DIRECTION_NONE);
                }//ends else if


                //if fear is negative, flip directions so that cell attacks pred
                if ( this.getFear() < 0 )
                {
                    this.getSpeed().setyDir(this.getSpeed().getyDir() * -1);    //flip y direction
                    this.getSpeed().setxDir(this.getSpeed().getxDir() * -1);    //flip x direction
                }
            }//ends if
        } //ends if
        else    //if unconcerned with predator
        {
            currxDir = this.getSpeed().getxDir();
            curryDir = this.getSpeed().getyDir();


            //bias toward current direction
            if (randSeed.nextInt(100) > 90) {
                rndxDir = randSeed.nextInt(3) - 1;
                rndyDir = randSeed.nextInt(3) - 1;

                this.getSpeed().setxDir(rndxDir);
                this.getSpeed().setyDir(rndyDir);
            } //ends if
            else
            {
                this.getSpeed().setxDir(currxDir);
                this.getSpeed().setyDir(curryDir);
            }//ends else
        }//ends else
    }//ends runFromPred method

    public void goToFood( int screenWidth, int screenHeight, int numFood, Food food[] )
    {
        int xDiff, yDiff, closerDiffY, closerDiffX, urgency,
                currxDir, curryDir, rndxDir, rndyDir;
        double xDiffWrap, yDiffWrap;

        Random randSeed = new Random();

        if ( foodRadius == 999999 )
        {
            closestFood = food[numFood - 1];
        }

        //calculate conventional distances
        xDiff = this.getX() - this.closestFood.getX();
        yDiff = this.getY() - this.closestFood.getY();

        //calculate differences accross wrapped screen
        if (xDiff >= 0)  //if cell is to the right of food
        {
            xDiffWrap = this.closestFood.getX() + (screenWidth - this.getX()); // x-distance across wrapped screen
        } else  //if cell is to the left of food
        {
            xDiffWrap = this.getX() + (screenWidth - this.closestFood.getX()); // x-distance across wrapped screen
        }

        if (yDiff >= 0)  //if cell is below food
        {
            yDiffWrap = this.closestFood.getY() + (screenHeight * 0.8 - this.getY()); //y-distance across wrapped screen
        } else // if cell is above food
        {
            yDiffWrap = this.getY() + (screenHeight * 0.8 - this.closestFood.getY());  //y-distance across wrapped screen
        }

        //determine closer distance
        xDiff = Math.abs(xDiff);
        yDiff = Math.abs(yDiff);
        if (yDiff < yDiffWrap) {
            closerDiffY = yDiff;
        } else {
            closerDiffY = (int) yDiffWrap;
        }

        if (xDiff < xDiffWrap) {
            closerDiffX = xDiff;
        } else {
            closerDiffX = (int) xDiffWrap;
        }


        foodRadius = (int) Math.sqrt(Math.pow(closerDiffX, 2) + Math.pow(closerDiffY, 2));


        if (foodRadius < 400) {
            urgency = 400 - foodRadius;

        }//ends if statement
        else {
            urgency = 0;
        }//ends else

        //Assign direction:
        //React to stimuli, and random behavior
        if (randSeed.nextInt(400) < urgency)     //if food sufficiently near
        {
            if (this.getX() > this.closestFood.getX())   //if cell to the right of food
            {
                if (xDiff < xDiffWrap) {
                    this.getSpeed().setxDir(Speed.DIRECTION_LEFT);
                } else {
                    this.getSpeed().setxDir(Speed.DIRECTION_RIGHT);
                }
            }//ends if statement
            else    // if cell to the left of food
            {
                if (xDiff < xDiffWrap) {
                    this.getSpeed().setxDir(Speed.DIRECTION_RIGHT);
                } else {
                    this.getSpeed().setxDir(Speed.DIRECTION_LEFT);
                }
            }//ends else

            if (this.getY() > this.closestFood.getY())      // if cell below food
            {
                if (yDiff < yDiffWrap) {
                    this.getSpeed().setyDir(Speed.DIRECTION_UP);
                } else {
                    this.getSpeed().setyDir(Speed.DIRECTION_DOWN);
                }
            }//ends if statement
            else        // if cell above food
            {
                if (yDiff < yDiffWrap) {
                    this.getSpeed().setyDir(Speed.DIRECTION_DOWN);
                } else {
                    this.getSpeed().setyDir(Speed.DIRECTION_UP);
                }
            }//ends else

            if (Math.pow(closerDiffX, 2) > Math.pow(closerDiffY, 2) + 2500)      //To avoid diagonal-only
            {
                this.getSpeed().setyDir(Speed.DIRECTION_NONE);
            }//ends if statement
            else if (Math.pow(closerDiffY, 2) > Math.pow(closerDiffX, 2) + 2500) //To avoid diagonal-onlay  <-- Nick's a fuck up
            {
                this.getSpeed().setxDir(Speed.DIRECTION_NONE);
            }//ends else if
        } //ends if
        else    //if unconcerned with food (not sufficiently near)
        {
            currxDir = this.getSpeed().getxDir();
            curryDir = this.getSpeed().getyDir();


            //bias toward current direction
            if (randSeed.nextInt(100) > 90) {
                rndxDir = randSeed.nextInt(3) - 1;
                rndyDir = randSeed.nextInt(3) - 1;

                this.getSpeed().setxDir(rndxDir);
                this.getSpeed().setyDir(rndyDir);
            } //ends if
            else
            {
                this.getSpeed().setxDir(currxDir);
                this.getSpeed().setyDir(curryDir);
            }//ends else
        }//ends else
    }//ends goToFood method

    //Function for cell to reproduce
    public int reproduce ( int numCells, int maxCells, Cell cell[] )
    {
        if ( this.foodCount >= 3 && numCells < maxCells )
        {
            //create child cells
            Cell childOne = new Cell(this.bitmap, this );
            Cell childTwo = new Cell(this.bitmap, this );


            this.setX(cell[numCells - 1].getX());
            this.setY(cell[numCells - 1].getY());

            this.swapStats(cell[numCells - 1]);  //swap stats so death is correct

            cell[numCells - 1] = childOne;
            numCells++;
            cell[numCells - 1] = childTwo;
        }

        return numCells;
    }//ends reproduce method


}//ends Cell class
