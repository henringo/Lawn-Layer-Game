package lawnlayer;
import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PImage;

public abstract class enemy {
    int x;
    int y;
    boolean moveUpLeft = false;
    boolean moveDownLeft = false;
    boolean moveUpRight =  true;
    boolean moveDownRight = false;
    private PImage sprite ;
    int up;
    int left;
    int right;
    int down;
    public int speed = 2;

/**
* enemy Position
* 
* @param speed, speed of the ball
* @param moveUpLeft, when move up left
* @param moveDownRight, when move  down right
* @param moveDownLeft, when move down left
* @param moveUpRight, when move up right

*/
    public enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    public void tick(int[][] areaCollide){
        float xfloat = (float) this.x /20;
        float yfloat = (float) this.y /20;
        int corX = Math.round(xfloat);
        int corY = Math.round(yfloat);
    
        int up = areaCollide[corY-1][corX];
        int left = areaCollide[corY][corX-1];

        int down = areaCollide[corY+1][corX];
        int right = areaCollide[corY][corX+1];
        if (this.moveUpLeft){

            if (areaCollide[this.y%20][this.x%20] ==0){
                UpLeft();
                return;
            }

            if (up != 0){
                DownLeft();
                this.moveDownLeft = true;
                this.moveUpLeft = false;
                this.moveUpRight = false;
                this.moveDownRight = false;
                return;   
            }

            if (left !=0){

                UpRight();
                this.moveUpLeft = false;
                this.moveUpRight = true;
                this.moveDownLeft = false;
                this.moveDownRight = false;
                return;   
            }
            UpLeft();
            return;
   
        }
        if (this.moveDownLeft){
            if (areaCollide[this.y%20][this.x%20] ==0){
                DownLeft();
                return;
            }
            if(left !=0){
           
                DownRight();
                this.moveDownLeft = false;
                this.moveUpLeft = false;
                this.moveUpRight = false;
                this.moveDownRight = true;
                return;
            }
            if (down!=0){

                       
                UpLeft();
                this.moveDownLeft = false;
                this.moveUpLeft = true;
                this.moveUpRight = false;
                this.moveDownRight = false;
                return;
            }
            DownLeft();
            return;
        }
        


        if (this.moveUpRight){
            if (areaCollide[this.y%20][this.x%20] ==0){
                UpRight();
                return;
            }
            if (right !=0){

                UpLeft();
                this.moveDownLeft = false;
                this.moveUpLeft = true;
                this.moveUpRight = false;
                this.moveDownRight = false;
                return;
            }
            if (up!=0 ){

                DownRight();

                this.moveDownLeft = false;
                this.moveUpLeft = false;
                this.moveUpRight = false;
                this.moveDownRight = true;
                return;
            }
            UpRight();
            return;

        }

        if (this.moveDownRight){
            if (areaCollide[this.y%20][this.x%20] ==0){
                DownRight();
                return;
            }
            if (right !=0){

                DownLeft();
                this.moveDownLeft = true;
                this.moveUpLeft = false;
                this.moveUpRight = false;
                this.moveDownRight = false;
                return;
            }
            if (down!=0 ){

                UpRight();
                this.moveDownLeft = false;
                this.moveUpLeft = false;
                this.moveUpRight = true;
                this.moveDownRight = false;
                return;
            }
            DownRight();
            return;

        }
    }
 
    public void DownRight(){
        this.x +=speed;
        this.y +=speed;
    }

    public void UpRight(){
        this.x +=speed;
        this.y -=speed;
    }

    public void UpLeft(){
        this.x -=speed;
        this.y -=speed;
    }

    public void DownLeft(){
        this.x -=speed;
        this.y +=speed;
    }
    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);

    }


}