package com.example.cookd.petri.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.cookd.petri.model.components.Speed;

import java.util.Random;

/**
 * Author: Darian Cook, Nicholas Harris, David Nielsen
 * Date: 6/1/2016
 * Class for the Predator
 */
public class Predator
{
    private Bitmap bitmap;
    private int x;
    private int y;
    private boolean touched;
    public Speed speed;
    private float velocity = 7;
    private int maxHealth;
    private int health;
    private int strength;

    private int id;

    private Cell closestCell;

    public Predator(Bitmap bitmap, int x, int y, float givenVelocity, int givenHealth, int givenStrength, int givenID )
    {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.speed = new Speed();
        this.velocity = givenVelocity;
        this.maxHealth = givenHealth;
        this.health = maxHealth;
        this.strength = givenStrength;

        this.speed.setxDir(1);
        this.speed.setyDir(1);
        this.speed.setXv(velocity);
        this.speed.setYv(velocity);

        this.id = givenID;

    }//ends Predator constructor

    public void setVelocity(int velocity)
    {
        this.speed.setXv(velocity);
        this.speed.setYv(velocity);
    }//ends setVelocity method

    public float getVelocity()
    {
        return velocity;
    }//ends getVelocity method

    public void setVelocity( float newVelocity )
    {
        this.velocity = newVelocity;
        this.speed.setXv(velocity);
        this.speed.setYv(velocity);
    }

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
    }//enjds asdgf

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

    public int getID()
    {
        return this.id;
    }

    public int getStrength()
    {
        return this.strength;
    }

    public void setStrength( int newStrength )
    {
        this.strength = newStrength;
    }

    public int getMaxHealth()
    {
        return this.maxHealth;
    }

    public void setMaxHealth( int newMaxHealth )
    {
        this.maxHealth = newMaxHealth;
    }

    public int getHealth()
    {
        return this.health;
    }

    public void setHealth( int newHealth )
    {
        this.health = newHealth;
    }

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


    //function to wrap predator around screen when at the edge
    public void wrap ( int screenWidth, int screenHeight )
    {
        if (this.getSpeed().getxDir() == Speed.DIRECTION_RIGHT && this.getX() - this.getBitmap().getWidth() / 2 >= screenWidth )
        {
            this.setX(0);
        }//ends if statement

        //left wall collision check
        if (this.getSpeed().getxDir() == Speed.DIRECTION_LEFT && this.getX() - this.getBitmap().getWidth() / 2 <= 0)
        {
            this.setX(screenWidth );
        }//ends if statement

        //bottom wall collision check
        if (this.getSpeed().getyDir() == Speed.DIRECTION_DOWN && this.getY() + this.getBitmap().getHeight() / 2 >= (screenHeight * 0.9) )
        {
            this.setY( screenHeight/10 );
        }//ends if statement

        //top wall collision check
        if (this.getSpeed().getyDir() == Speed.DIRECTION_UP && this.getY() - this.getBitmap().getHeight() / 2 <= (screenHeight/10))
        {
            this.setY( (int) (screenHeight*0.9)  );
        }//ends if statement
    }

    //function to prevent predators from overlapping
    public void avoidOverlap ( Predator pred[], int numPreds )
    {
        int xDiff, yDiff;
        if ( numPreds > 1 )
        {
            for ( int i = 0; i < numPreds; i++ )
            {
                xDiff = Math.abs(this.getX() - pred[i].getX() );
                yDiff = Math.abs(this.getY() - pred[i].getY() );

                //If cells have completely overlapped, offset one slightly
                if ( xDiff == 0 && yDiff == 0 && this.id != pred[i].getID())
                {
                    this.setX( this.getX() + 25 );
                    this.setY ( this.getY() + 25 );
                }

                //collision check going right
                if (this.getSpeed().getxDir() == Speed.DIRECTION_RIGHT && pred[i].getX() - this.getX() < 35 && pred[i].getX() - this.getX() > 0
                                                                                                            && yDiff < 35)
                {
                    this.speed.toggleXDirection();
                }//ends if statement

                //collision check going left
                if (this.getSpeed().getxDir() == Speed.DIRECTION_LEFT && this.getX() - pred[i].getX() < 35 && this.getX() - pred[i].getX() > 0
                                                                                                            && yDiff < 35)
                {
                    this.speed.toggleXDirection();
                }//ends if statement

                //collision check going down
                if (this.getSpeed().getyDir() == Speed.DIRECTION_DOWN && pred[i].getY() - this.getY() < 35 && pred[i].getY() - this.getY() > 0
                                                                                                            && xDiff < 35)
                {
                    this.speed.toggleYDirection();
                }//ends if statement

                //collision check going up
                if (this.getSpeed().getyDir() == Speed.DIRECTION_DOWN && this.getY() - pred[i].getY()  < 35 && this.getY() - pred[i].getY() > 0
                                                                                                             && xDiff < 35)
                {
                    this.speed.toggleYDirection();
                }//ends if statement
            }
        }
    }

    public void findClosestCell( int screenWidth, int screenHeight, int numCells, Cell cell[])
    {
        int closestDist = 999999;
        int absoluteDist;
        int xDiff, yDiff, closerDiffX, closerDiffY;
        double xDiffWrap, yDiffWrap;

        //Find closest cell
        for ( int i = 0; i < numCells; i++ )
        {
            //calculate conventional distances
            xDiff = cell[i].getX() - this.getX();
            yDiff = cell[i].getY() - this.getY();

            //calculate differences accross wrapped screen
            if ( xDiff >= 0 )  //if cell is to the right of pred
            {
                xDiffWrap = this.getX() + (screenWidth - cell[i].getX()); // x-distance across wrapped screen
            }
            else  //if cell is to the left of pred
            {
                xDiffWrap = cell[i].getX()  + (screenWidth - this.getX()); // x-distance across wrapped screen
            }

            if ( yDiff >= 0 )  //if cell is below pred
            {
                yDiffWrap = this.getY() +  (screenHeight*0.8 - cell[i].getY() ); //y-distance across wrapped screen
            }
            else // if cell is above pred
            {
                yDiffWrap = cell[i].getY() + (screenHeight*0.8 - this.getY() );  //y-distance across wrapped screen
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
                this.closestCell = cell[i];
                closestDist = absoluteDist;
            }//ends if statement
        } // ends for loop
    }// ends find closest cell method

    public Cell getClosestCell()
    {
        return this.closestCell;
    }

    public void chooseDirection( int screenWidth, int screenHeight, int numCells )
    {
        int xDiff, yDiff,closerDiffY, closerDiffX, currxDir, curryDir, rndxDir, rndyDir;
        double xDiffWrap, yDiffWrap;

        Random randSeed = new Random();

        if (numCells > 0 ) //if there are cells to chase
        {

            //calculate conventional distances
            xDiff = this.getClosestCell().getX() - this.getX();
            yDiff = this.getClosestCell().getY() - this.getY();


            //calculate differences accross wrapped screen
            if (xDiff >= 0)  //if cell is to the right of pred
            {
                xDiffWrap = this.getX() + (screenWidth - this.getClosestCell().getX()); // x-distance across wrapped screen
            } else  //if cell is to the left of pred
            {
                xDiffWrap = this.getClosestCell().getX() + (screenWidth - this.getX()); // x-distance across wrapped screen
            }

            if (yDiff >= 0)  //if cell is below pred
            {
                yDiffWrap = this.getY() + (screenHeight * 0.8 - this.getClosestCell().getY()); //y-distance across wrapped screen
            } else // if cell is above pred
            {
                yDiffWrap = this.getClosestCell().getY() + (screenHeight * 0.8 - this.getY());  //y-distance across wrapped screen
            }


            //Choose direction for predator
            xDiff = Math.abs(xDiff);
            yDiff = Math.abs(yDiff);
            if (this.getClosestCell().getX() > this.getX()) {
                if (xDiff < xDiffWrap) {
                    this.getSpeed().setxDir(Speed.DIRECTION_RIGHT);
                } else {
                    this.getSpeed().setxDir(Speed.DIRECTION_LEFT);
                }
            }//ends if statement
            else {
                if (xDiff < xDiffWrap) {
                    this.getSpeed().setxDir(Speed.DIRECTION_LEFT);
                } else {
                    this.getSpeed().setxDir(Speed.DIRECTION_RIGHT);
                }
            }//ends else

            if (this.getClosestCell().getY() > this.getY()) {
                if (yDiff < yDiffWrap) {
                    this.getSpeed().setyDir(Speed.DIRECTION_DOWN);
                } else {
                    this.getSpeed().setyDir(Speed.DIRECTION_UP);
                }
            }//ends if statement
            else {
                if (yDiff < yDiffWrap) {
                    this.getSpeed().setyDir(Speed.DIRECTION_UP);
                }
            }//ends else


            //determine closer distance
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


            //avoid diagonal-only choices
            if (Math.pow(closerDiffX, 2) > Math.pow(closerDiffY, 2) + 2500)      //To avoid diagonal-only
            {
                this.getSpeed().setyDir(0);
            }//ends if statement
            else if (Math.pow(closerDiffY, 2) > Math.pow(closerDiffX, 2) + 2500) //To avoid diagonal-onlay
            {
                this.getSpeed().setxDir(0);
            }//ends else if
        }//ends if statement
        else    //if there are no cells, move randomly
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
        }
    }//ends chooseDirection Method

}//ends Predator class
