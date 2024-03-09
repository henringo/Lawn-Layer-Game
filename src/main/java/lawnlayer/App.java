package lawnlayer;

import java.lang.reflect.Array;
import java.sql.Struct;

import org.checkerframework.checker.units.qual.A;

// import javafx.scene.image.Image;
import netscape.javascript.JSObject;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;
import processing.core.PFont;

import java.util.*;

import javax.lang.model.element.Element;
import javax.lang.model.type.NullType;
import javax.sound.midi.Patch;

import java.io.*;

public class App extends PApplet {
    int powerChose;
     int level_total;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int TOPBAR = 80;
    int frame_count = 0;
    int frame_count_1 = 0;

    int goal;
    public static final int FPS = 60;
    int lives;
    boolean hasEaten  =  false;
    int powerTime;
    int dirtTotal =0;
    public PFont font;
    public PImage power;
    int powerX;
    int powerY;

    int timeBeginPower;


    int powerX_2;
    int powerY_2;
    String outlay;
    public String configPath;
    public int TopBarRow = TOPBAR/SPRITESIZE ;
    public int Collumn = WIDTH/SPRITESIZE ;
    public int Row = HEIGHT/SPRITESIZE;
    public int[][] corList = new int[Row][Collumn];

    int level_now_1 = 0;

	public PImage grass;
    public PImage concrete;
    public worm worm;
    public beetle beetle;
    public PImage green;
    public PImage red;
    public PImage power_2;
    public ball ball;
    JSONObject json;
    int now = 0;
    ArrayList<String> xy = new ArrayList<String>();
    ArrayList<String> ballPath = new ArrayList<String>();
    ArrayList<String> greenArea = new ArrayList<String>();
    int score =0;
    ArrayList<worm> wormList = new ArrayList<worm>();
    ArrayList<beetle> beetleList = new ArrayList<beetle>();



    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
    */

    public void settings() {
        size(WIDTH, HEIGHT);
        this.ball = new ball();   
    }
        // For next level
    public void nextlevel(){
        ball = new ball();
        int[][] corList = new int[Row][Collumn];
        dirtTotal=0;
        frame_count = 0;
        frame_count_1 = 0;
        timeBeginPower = 0;
        powerTime = 0;
        ArrayList<worm> wormList = new ArrayList<worm>();
        ArrayList<beetle> beetleList = new ArrayList<beetle>();
        int powerChose; 
        this.setup();
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        frameRate(FPS);

        // Load images during setup
        font = createFont("Arial",30,true);
		this.grass = loadImage(this.getClass().getResource("grass.png").getPath());
        this.concrete = loadImage(this.getClass().getResource("concrete_tile.png").getPath());
    
        // this.beetle = loadImage(this.getClass().getResource("beetle.png").getPath());
        this.ball.setSprite(loadImage(this.getClass().getResource("ball.png").getPath()));
        this.green = loadImage(this.getClass().getResource("green.png").getPath());
        this.red = loadImage(this.getClass().getResource("red.png").getPath());
        this.power = loadImage(this.getClass().getResource("clock.png").getPath());
        this.power_2 = loadImage(this.getClass().getResource("lightning.png").getPath());

        this.green.resize(20,20);
        this.red.resize(20,20);

        this.power.resize(20,20);
        this.power_2.resize(20,20);
        
        
        Random rand = new Random();
 
        timeBeginPower = (rand.nextInt(9)+1)*60;

        /** read json file
        * @throws if can not find file
         */

        json = loadJSONObject(this.configPath);
        lives = json.getInt("lives");
        JSONArray jsonList = json.getJSONArray("levels");

        level_total = jsonList.size();
        
        JSONObject level_now;
        if (level_now_1<level_total){
            level_now = jsonList.getJSONObject(level_now_1);
        }
        else{
            win();
            return;
        }
        outlay = level_now.getString("outlay");
        JSONArray enemiesJList = level_now.getJSONArray("enemies");

        int jalength = enemiesJList.size();


        for(int i = 0; i < jalength; i++)
        {
            JSONObject enemy = enemiesJList.getJSONObject(i);
            int type = enemy.getInt("type");
            String spawn = enemy.getString("spawn");
            int x= 0;
            int y = 0;
            if (spawn.equals("random")){
                Random random = new Random();  
                x = random.nextInt(1220);
                if (x%2 == 1){
                    x = x+21;
                }
                else{
                    x = x+20;
                }
                y = random.nextInt(580);
                if (y%2 == 1){
                    y = y+101;
                }
                else{
                    y = y+100;
                }
            }
            else{
                String[] spawning = spawn.split(",");
                x = Integer.parseInt(spawning[0]);
                y = Integer.parseInt(spawning[1]);

            }
            if (type == 0){
                worm = new worm(x,y);
                this.wormList.add(worm);                
            }
            else if(type == 1){
                beetle = new beetle(x,y);
                this.beetleList.add(beetle);
            }

            for (int r=0; r<this.Row; r++){
                for (int c=0; c< this.Collumn; c++){
                    corList[r][c] = -1;
                }
            }
    
        }
        for (worm w :wormList){
            w.setSprite(loadImage(this.getClass().getResource("worm.png").getPath()));
        }
        for (beetle b :beetleList){
            b.setSprite(loadImage(this.getClass().getResource("beetle.png").getPath()));
        }

        float hehe = (float) level_now.getFloat("goal")*100;
        goal = Math.round(hehe);
        now ++;


/**
* Given a target, a cat will attempt to pounce
* and attack it with its claws.
* If successful, this will return true, otherwise false
* @param t, A cat's enemy target
* @return success
*/
        try {
            int y = 60;
            char ch = 'X';
            File file = new File(outlay);

            Scanner scanner = new Scanner(file);
            int r = TOPBAR/SPRITESIZE;

            while (scanner.hasNextLine()) {
                y+=20;
                int x = 0;
                String line = scanner.nextLine();
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == ch){
                        this.xy.add(String.format("%d %d",x,y));
                    }
                    x+=20;                       
                }

                for (String i:this.xy){
                    this.greenArea.add(i);
                    }
                /**
                * Given a dimensional array
                * put 1 as concrete
                * 0 as dirt
                * 7 as red path
                * -1 as upper area for text
                * 2 as path
                * 1 asconcrete
                */
                char[] chList = line.toCharArray();
                for (int c=0; c<line.length(); c++){        
                    if (chList[c] == 'X'){
                        corList[r][c] = 1;   //concrete
                    }
                    else{
                        dirtTotal+=1;
                        corList[r][c] = 0;  //dirt
                    }
                }
                r+=1;
            }
            // Randomly generate cordinates of power

            powerX = (rand.nextInt(62)+1)*20;
            powerY = (rand.nextInt(27)+5)*20;
            powerChose = rand.nextInt(2);
            powerX_2 =  (rand.nextInt(62)+1)*20;
            powerY_2 =  (rand.nextInt(27)+5)*20;
    
            
            while (! (corList[powerY/20][powerX/20] == 0)){
                powerX = (rand.nextInt(62)+1)*20;
                powerY = (rand.nextInt(27)+5)*20;
                powerX_2 =  (rand.nextInt(62)+1)*20;
                powerY_2 =  (rand.nextInt(27)+5)*20;         
            }
            scanner.close();
          } catch (FileNotFoundException e) {System.out.println(e);}

    }
	
    /**
     * Draw all elements in the game by current frame. 
    */
    public void draw() {
        background(43,29,20);

        // For power up slow enemy
        timeBeginPower -- ;
        if (!hasEaten && timeBeginPower <=0){
            if (powerChose ==0){
                image(this.power,powerX,powerY);
            }
            else if (powerChose == 1){
                image(this.power_2,powerX_2,powerY_2);
            }
        }
        if (powerChose == 0){
            if (corList[powerY/20][powerX/20]==3){
                hasEaten = true;
            }
        }
        else if (powerChose == 1){
            if (corList[powerY_2/20][powerX_2/20]==3){
                hasEaten = true;
            }
        }

        if (this.ball.x %20 == 0 && this.ball.y %20 ==0 && this.ball.x == powerX && this.ball.y == powerY && !hasEaten && powerChose==0){
            Random rand = new Random();

            powerTime = (rand.nextInt(9)+1)*20;
            for (worm w:wormList){
                w.speed = 0;
            }
            for (beetle b:beetleList){
                b.speed = 0;
            }
            hasEaten = true;
            float textTime = (float) powerTime/60;
            text(String.format("time: %.02f",textTime),1200,50);           

        }

        if (this.ball.x %20 == 0 && this.ball.y %20 ==0 && this.ball.x == powerX_2 && this.ball.y == powerY_2 && !hasEaten && powerChose==1){
            Random rand = new Random();

            powerTime = (rand.nextInt(9)+1)*20;
            ball.speed = 4;
            hasEaten = true;
            float textTime = (float) powerTime/60;
            text(String.format("time: %.02f",textTime),1200,50);           

        }

        if (powerChose == 0){
            if (powerTime>=0){
                powerTime --;
            }
            else{
                for (worm w:wormList){
                    w.speed = 2;
                }
                for (beetle b:beetleList){
                    b.speed = 2;
                }   
            }
        }
        else if (powerChose ==1){
            if (powerTime>=0){
                powerTime --;
            }
            else{
                ball.speed =2;
            }
        }

        if (hasEaten){
            float textTime = (float) powerTime/60;
            if (textTime<=0){
                textTime = 0;
            }
            text(String.format("time: %.02f",textTime),1000,50);           
        }

        // if lose
        int score_1 =0;
        if (lives == 0){
            lose();
            return;
        }
        // if win
        if (level_now_1> level_total){
            win();
            return;
        }
                     /**
                * Given a dimensional array
                * put 1 as concrete
                * 0 as dirt
                * 7 as red path
                * -1 as upper area for text
                * 2 as path
                * 1 asconcrete
                */
        for (int r=0; r<Row; r++){
            for (int c=0; c< Collumn; c++){
                if (corList[r][c]==1){
                    image(concrete,c*SPRITESIZE,r*SPRITESIZE);
                    }
                else if (corList[r][c]==2){
                    image(green,c*SPRITESIZE,r*SPRITESIZE);
                    }
                else if (corList[r][c]==3){
                    image(grass,c*SPRITESIZE,r*SPRITESIZE);
                    score_1++;
                    }
                else if (corList[r][c] == 7){
                    image(red,c*SPRITESIZE,r*SPRITESIZE);
                    }
                }
            }
        this.ball.tick();
        frame_count ++;
        frame_count_1 ++;
        // Display text
        textFont(font,30);
        String result = String.format("Score: %.02f",100*(float)score_1/dirtTotal);

        text(String.format("Level %d",level_now_1+1),150,50);       
        text(result,550,50);              
        text(String.format("Lives: %d",lives),800,50);
     
        fill(0);
        if ((100*(float)score_1/dirtTotal)>=goal){
            level_now_1+=1;
            beetleList.clear();
            wormList.clear();
            hasEaten = false;
            nextlevel();
        }

        /**
         * if object is beetle and collide path (2), change value to 0 (dirt)
         * if both worm an beetle collide with path, change to red path each frame by 1 outwards
         */
        for (worm w : wormList){
            w.tick(this.corList);
            w.draw(this);
            float xfloat = (float) w.x /20;
            float yfloat = (float) w.y /20;
            int corX = Math.round(xfloat);
            int corY = Math.round(yfloat);
            if (corList[corY-1][corX] == 2 ){    //up
                corList[corY-1][corX] = 7;
            }
            else if (corList[corY][corX-1] ==2 ){ //left
                corList[corY][corX-1] = 7;
            }
            else if (corList[corY][corX+1] ==2 ){ //right

                corList[corY][corX+1] = 7;
            }
            else if (corList[corY+1][corX] ==2 ){   //down

                corList[corY+1][corX] = 7;
            } 
        }

        for (beetle b: beetleList){

                float xfloat = (float) b.x /20;
                float yfloat = (float) b.y /20;
                int corX = Math.round(xfloat);
                int corY = Math.round(yfloat);

                b.tick(this.corList);

                if (corList[corY-1][corX] == 3 ){    //up
                    corList[corY-1][corX] = 0;
                    score --;
                }
                else if (corList[corY][corX-1] ==3 ){ //left
                    corList[corY][corX-1] = 0;
                    score --;      
                }
                else if (corList[corY][corX+1] ==3 ){ //right

                    corList[corY][corX+1] = 0;
                    score --;
                }
                else if (corList[corY+1][corX] ==3 ){   //down

                    score --;
                    corList[corY+1][corX] = 0;
                }
                
                if (corList[corY-1][corX] == 2 ){    //up
                    corList[corY-1][corX] = 7;
                    frame_count_1 = 0;
                    frame_count = 0;
                }
                else if (corList[corY][corX-1] ==2 ){ //left
                    corList[corY][corX-1] = 7;
                    frame_count = 0;
                    frame_count_1 = 0;

                    
                }
                else if (corList[corY][corX+1] ==2 ){ //right

                    frame_count = 0;
                    corList[corY][corX+1] = 7;
                    frame_count_1 = 0;
                }
                else if (corList[corY+1][corX] ==2 ){   //down

                    frame_count = 0;
                    frame_count_1 = 0;
                    
                    corList[corY+1][corX] = 7;
                }    
            b.draw(this);
        }
        /**
         * If cordinate of ball is not in concrete, create a new path
         * if cordinate of ball is already in ball path list, ball die and minus 1 life
        */
        String ballXY = String.format("%d %d",this.ball.x,this.ball.y);
        if (!ballPath.contains(ballXY)){
            if (!xy.contains(ballXY)&&this.ball.x %20 == 0 &&this.ball.y %20 == 0 ){
                this.ballPath.add(ballXY);
            }
        }
        else{
            this.ball.x = 0;
            this.ball.y = 80;
            this.ballPath.clear();
            lives -- ;
            for (int r=0; r<Row; r++){
                for (int c=0; c< Collumn; c++){
                    if (corList[r][c]==7 ||corList[r][c]==2){
                        corList[r][c] = 0;
                        }
                    }
                }
        }
    

        /**
         * Loop through cordinate list and change to red path each frame
        */
        for (String i : ballPath){
            if (! xy.contains(i)){
                String[] ballCor = i.split(" ");

                int position = ballPath.indexOf(i);
                int beforePost;

                if (position-1>0 && position-1 < ballPath.size()){
                    beforePost = ballPath.indexOf(i) -1;
                }
                else{
                    beforePost = 0;
                }


                String[] ballCorBefore = ballPath.get(beforePost).split(" ");


                if ((Integer.parseInt(ballCor[0])%20 == 0 && Integer.parseInt(ballCor[1])%20==0)&&(corList[Integer.parseInt(ballCor[1])/SPRITESIZE][Integer.parseInt(ballCor[0])/SPRITESIZE] !=1)&&(corList[Integer.parseInt(ballCor[1])/SPRITESIZE][Integer.parseInt(ballCor[0])/SPRITESIZE] !=3)){
                    if (corList[Integer.parseInt(ballCor[1])/SPRITESIZE][Integer.parseInt(ballCor[0])/SPRITESIZE] != 7){ 
                        corList[Integer.parseInt(ballCor[1])/SPRITESIZE][Integer.parseInt(ballCor[0])/SPRITESIZE] = 2;
                    }
                    else{
                        if (frame_count >= 3){
                                corList[Integer.parseInt(ballCorBefore[1])/SPRITESIZE][Integer.parseInt(ballCorBefore[0])/SPRITESIZE] = 7;
                        }
                    }
                }
            }
        }

        for (int g = ballPath.size()-1; g> 0; g--){

            String[] ballCor = ballPath.get(g).split(" ");
            int position = g;
            int afterPost;

            if (position+1>0 && position+1 < ballPath.size()){
                afterPost = position+1;
            }
            else{
                afterPost = position;

            }
            String[] ballCorAfter  =  ballPath.get(afterPost).split(" ");    
                if ((corList[Integer.parseInt(ballCor[1])/SPRITESIZE][Integer.parseInt(ballCor[0])/SPRITESIZE] == 7)&&(Integer.parseInt(ballCor[0])%20 == 0 && Integer.parseInt(ballCor[1])%20==0)){ 
                    
                    if (frame_count_1 >= 3){
                            corList[Integer.parseInt(ballCorAfter[1])/SPRITESIZE][Integer.parseInt(ballCorAfter[0])/SPRITESIZE] = 7;
                        }
            }
            
        }
 
        /**
         * fill path
         */
        if (!xy.contains(ballXY)&&this.ball.x %20 == 0 &&this.ball.y %20 == 0 ){
            int x = this.ball.x /20;
            int y = this.ball.y /20;
            if (corList[y][x] == 7){
                this.ball.x = 0;
                this.ball.y = 80;
                this.ballPath.clear();
                lives -- ;
                for (int r=0; r<Row; r++){
                    for (int c=0; c< Collumn; c++){
                        if (corList[r][c]==7 ||corList[r][c]==2){
                            corList[r][c] = 0;
                            }
                        }
                    }
                }   
            }

        if ((this.ball.x%20==0)&&(this.ball.y%20==0)){
            if ((corList[this.ball.y/SPRITESIZE][this.ball.x/SPRITESIZE]==1)||(corList[this.ball.y/SPRITESIZE][this.ball.x/SPRITESIZE]==3)){
                
                

                if (ballPath.size()!=0){
                    for(String e : ballPath){
                        String[] cor = e.split(" ");
                        int e1 = Integer.parseInt(cor[0])/20;
                        int e2 = Integer.parseInt(cor[1])/20;
                        if ((e2>4)&&(e2<35)&&(e1<63)&&(e1>0)){
                            
                            corList[e2][e1] = 3;
                        }
                    }
                    boolean stop = false;

                    if (this.ballPath.size()>0){

                        int area_1 = 0;
                        int area_2 =0;
                        ArrayList<String> group_1 = new ArrayList<String>();
                        int areaWorm = 0;
                        ArrayList<String> worms = new ArrayList<String>();

                        for (worm w: wormList){
                                int wormY = w.y/20;
                                int wormX = w.x/20;

                                areaWorm ++;
                                String wormCor = String.format("%d %d",w.y/20,w.x/20);
                                corList[w.y/20][w.x/20] = 6;
                                if (!worms.contains(wormCor)){
                                    worms.add(wormCor);
                                    
                                }                           
                        }

                        for (beetle w: beetleList){
                            int wormY = w.y/20;
                            int wormX = w.x/20;

                            areaWorm ++;
                            String wormCor = String.format("%d %d",w.y/20,w.x/20);
                            corList[w.y/20][w.x/20] = 6;
                            if (!worms.contains(wormCor)){
                                worms.add(wormCor);
                                
                            }                           
                        }
                        if (worms.size()>0){
                            for (int a = 0;a< worms.size();a++){
                                String element = worms.get(a);
                                String[] cor = element.split(" ");
                                int v = Integer.parseInt(cor[0]);
                                int h = Integer.parseInt(cor[1]);
            
                                String LeftBlock = String.format("%d %d",v-1,h); 
                                String RightBlock = String.format("%d %d",v+1,h);
                                String UppperBlock = String.format("%d %d",v,h-1);
                                String BelowBlock = String.format("%d %d",v,h+1);
                                if ((v-1>4)&&(v-1<35)&&(h<63)&&(h>0)){
                                    if ((corList[v-1][h] == 0)&&(!worms.contains(LeftBlock))){
                                            worms.add(LeftBlock);
                                            corList[v-1][h] = 6;
                                            areaWorm +=1;
                                    }
                                }
    
                                if ((v+1>4)&&(v+1<35)&&(h<63)&&(h>0)){
                                    if ((corList[v+1][h] == 0)&&(!worms.contains(RightBlock))){
                                            worms.add(RightBlock);
                                            corList[v+1][h] = 6;
                                            areaWorm +=1;
                                    }
                                }
    
                                if ((v>4)&&(v<35)&&(h+1<63)&&(h+1>0)){
                                    if ((corList[v][h+1] == 0)&&(!worms.contains(BelowBlock))){
                                            worms.add(BelowBlock);
                                            corList[v][h+1] = 6;
                                            areaWorm +=1;
                                    }
                                }
    
                                if ((v>4)&&(v<35)&&(h-1<63)&&(h-1>0)){
                                    if ((corList[v][h-1] == 0)&&(!worms.contains(UppperBlock))){
                                            worms.add(UppperBlock);
                                            corList[v][h-1] = 6;
                                            areaWorm++;
                                    }
                                }
                            }
                        }
           


                    for (int c=1; c<Collumn;c++){

                        for (int r=TopBarRow+1; r < Row;r++){

                            if (corList[r][c] == 0){
                                corList[r][c] = 4;
                                String elementG1 = String.format("%d %d",r,c);
                                group_1.add(elementG1);
                                stop=true;
                                area_1+=1;
                                break;
                            }
                        }
           
                        }


                    for(int z = 0; z<group_1.size();z++){
                        String element = group_1.get(z);
                        String[] cor = element.split(" ");
                        int v = Integer.parseInt(cor[0]);
                        int h = Integer.parseInt(cor[1]);

                        String LeftBlock = String.format("%d %d",v-1,h); 
                        String RightBlock = String.format("%d %d",v+1,h);
                        String UppperBlock = String.format("%d %d",v,h-1);
                        String BelowBlock = String.format("%d %d",v,h+1);


                            if ((v-1>4)&&(v-1<35)&&(h<63)&&(h>0)){
                                if ((corList[v-1][h] == 0)&&(!group_1.contains(LeftBlock))){
                                    if (!xy.contains(LeftBlock)){
                                        group_1.add(LeftBlock);
                                        corList[v-1][h] = 4;
                                        area_1 +=1;
                                    }
                                }
                            }

                            if ((v+1>4)&&(v+1<35)&&(h<63)&&(h>0)){
                                if ((corList[v+1][h] == 0)&&(!group_1.contains(RightBlock))){
                                    if (!xy.contains(RightBlock)){
                                        group_1.add(RightBlock);
                                        corList[v+1][h] = 4;
                                        area_1 +=1;
                                    }
                                }
                            }

                            if ((v>4)&&(v<35)&&(h+1<63)&&(h+1>0)){
                                if ((corList[v][h+1] == 0)&&(!group_1.contains(BelowBlock))){
                                    if (!xy.contains(BelowBlock)){
                                        group_1.add(BelowBlock);
                                        corList[v][h+1] = 4;
                                        area_1 +=1;
                                    }
                                }
                            }

                            if ((v>4)&&(v<35)&&(h-1<63)&&(h-1>0)){
                                if ((corList[v][h-1] == 0)&&(!group_1.contains(UppperBlock))){
                                    if (!xy.contains(UppperBlock)){
                                        group_1.add(UppperBlock);
                                        corList[v][h-1] = 4;
                                        area_1++;
                                        }
                                }
                            }
                        }
                        if (area_2 ==0){
                            for (int i=0; i<HEIGHT/SPRITESIZE; i++){
                                for (int y=0; y< WIDTH/SPRITESIZE; y++){
                                    if (corList[i][y]==4){
                                        score++;
                                        corList[i][y]=3;
                                    }
                                }
                            }  
                        }

                        if (area_1 == 0){
                            for (int i=0; i<Row; i++){
                                for (int y=0; y< Collumn; y++){
                                    if (corList[i][y]==0){
                                        score +=1;
                                        corList[i][y]=3;
                                    }
                                    if (corList[i][y]==4){
                                        corList[i][y]=0;
                                    }
                                }
                            }
                        
                        }

                        for (int i=0; i<Row; i++){
                            for (int y=0; y< Collumn; y++){
                                if (corList[i][y]==0){
                                    area_2 +=1;
                                }
                            }
                        }
                        if (area_2<area_1 && area_2 <areaWorm){
                            for (int i=0; i<Row; i++){
                                for (int y=0; y< Collumn; y++){
                                    if (corList[i][y]==0){
                                        score +=1;
                                        corList[i][y]=3;
                                    }
                                    if (corList[i][y]==4){
                                        corList[i][y]=0;
                                    }
                                }
                            }
                        }
                        else if (area_2>area_1 && area_2 <areaWorm) {
                            for (int i=0; i<HEIGHT/SPRITESIZE; i++){
                                for (int y=0; y< WIDTH/SPRITESIZE; y++){
                                    if (corList[i][y]==4){
                                        score++;
                                        corList[i][y]=3;
                                    }
                                }
                            }
                        }
                        

                        for (int c=1; c<Collumn;c++){

                            for (int r=TopBarRow+1; r < Row;r++){
                                if (corList[r][c] == 6){
                                    corList[r][c] =0;
                                }
                            }
                        }

                        
                        

                }
                    ballPath.clear();
                }
               
            }
        }


        // check if ball is on concrete
        
            // If ball is on concrete, change to not moving
        if (this.xy.contains(String.format("%d %d",this.ball.x,this.ball.y))){
        
            this.ball.moveDown = false;
            this.ball.moveRight = false;
            this.ball.moveUp = false;
            this.ball.moveLeft = false;
        }

        this.ball.draw(this);
  
            
        
    
    }
    // Lose
    public void lose(){
        background(100,0,0);
        // ball = null;
        int[][] corList = new int[Row][Collumn];
        textFont(font,80);                  
        fill(0,0,0);                         
        text("Game Over",300,300);
    }

    //Win
    public void win(){
        background(50,205,50);
        // ball = null;
        textFont(font,80);                  
        fill(0,0,0);                         
        text("You Win !!!",450,300);
    }
    //When press key
    public void keyPressed(){

        if (this.keyCode == 37){
            if(this.ball.moveRight != true){
                while (this.ball.y %20 !=0){
                    this.ball.pressLeft();
                    this.ball.draw(this);
                    }
                this.ball.pressLeft();
                    
                if (!xy.contains(String.format("%d %d",this.ball.x,this.ball.y))&&this.ball.x%20==0&&this.ball.y%20==0){
                    this.ballPath.add(String.format("%d %d",this.ball.x,this.ball.y));
                }
            }
        }
        else if (this.keyCode == 39){

            if(this.ball.moveLeft != true){
                while (this.ball.y %20 !=0){
                    this.ball.pressRight();
                    this.ball.draw(this);
                    }
                this.ball.pressRight();
                if (!xy.contains(String.format("%d %d",this.ball.x,this.ball.y))&&this.ball.x%20==0&&this.ball.y%20==0){
                    this.ballPath.add(String.format("%d %d",this.ball.x,this.ball.y));
                }            
            }
                

        }
        
        if (this.keyCode == 38){
            if(this.ball.moveDown != true){
                while (this.ball.x %20 !=0){
                    this.ball.pressUp();
                    
                    this.ball.draw(this);
                    }
                this.ball.pressUp();
                if (!xy.contains(String.format("%d %d",this.ball.x,this.ball.y))&&this.ball.x%20==0&&this.ball.y%20==0){
                    this.ballPath.add(String.format("%d %d",this.ball.x,this.ball.y));
                }
            }

        }
        else if (this.keyCode == 40){
            if(this.ball.moveUp != true){
                while (this.ball.x %20 !=0){
                    this.ball.pressDown();
                    
                    this.ball.draw(this);
                    }
                this.ball.pressDown();
                if (!xy.contains(String.format("%d %d",this.ball.x,this.ball.y))&&this.ball.x%20==0&&this.ball.y%20==0){
                    this.ballPath.add(String.format("%d %d",this.ball.x,this.ball.y));
                }
            }

        }
    }

    public static void main(String[] args) {
        PApplet.main("lawnlayer.App");
    }
}
