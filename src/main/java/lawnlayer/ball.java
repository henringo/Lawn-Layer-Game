package lawnlayer;

import javax.print.attribute.Size2DSyntax;

import org.checkerframework.checker.units.qual.A;

// import javafx.scene.transform.Scale;
import processing.core.PApplet;
import processing.core.PImage;

public class ball {
    protected int x;
    protected int y;
    protected PImage sprite;

    public int speed = 2;
    public boolean moveLeft;
    public boolean moveRight;

    public boolean moveDown;
    public boolean moveUp;

/**
* Ball Position
* 
* @param speed, speed of the ball
* @param moveLeft, when move left
* @param moveRight, when move right
* @param moveDown, when move down
* @param moveUp, when move up

*/
    
    public ball(){
        this.x = 0;
        this.y = 80;
        this.moveLeft =false;
        this.moveDown = false;
    }
    public void setSprite(PImage sprite){
        this.sprite = sprite;
        
    }   
    public void tick(){

        if (moveLeft){
            if (this.x > 0){
                this.x -=speed;
            }
            else{
                this.moveLeft = false;

            }
        }
        if (moveRight){
            if (this.x < 1260 ){
                this.x +=speed;
            }
            else{
                this.moveRight = false;
            }
        }
    
        if (moveDown){
            if (this.y < 700){
                this.y += speed;      
            }
            else{
                this.moveDown = false;
            }
        }

        if (moveUp){
            if (this.y > 80){
                this.y-= speed;
            }
            else{
                this.moveUp = false;
            }
        }
        
    }
    public void pressLeft(){
        if (this.y %20 !=0){
            tick();
            return;
        }
        this.moveLeft = true;
        this.moveRight = false;
        this.moveUp = false;
        this.moveDown = false;
    }
    
    public void pressRight(){

        if (this.y %20 !=0){
            tick();
            return;
        }
        this.moveRight = true;
        this.moveDown = false;
        this.moveUp = false;
        this.moveLeft = false;

    }

    public void pressUp(){

        if (this.x %20 !=0){
            tick();
            return;
        }

        this.moveUp = true;
        this.moveDown = false;
        this.moveLeft = false;
        this.moveRight = false;
    }

    public void pressDown(){

        if (this.x %20 !=0){
            tick();
            return;
        }

        this.moveDown = true;
        this.moveLeft= false;
        this.moveRight = false;
        this.moveUp = false;
    }

    public void draw(PApplet app){
        app.image(this.sprite,this.x,this.y);
    }
}