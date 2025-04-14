import java.net.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.scene.image.*;

import java.io.*;
import java.io.FileOutputStream;
import java.io.PrintWriter;


import java.text.*;
import java.lang.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.animation.*;


public class Main extends Application
{
   FlowPane fp;
   
   Canvas theCanvas = new Canvas(600,600);
   
   //player object
   Player thePlayer = new Player(300,300);
   
   //orgin object
   Player orgin = new Player(300,300);
      

   //booleans to keep track of what is being clicked
   boolean heldUp;
   boolean heldLeft;
   boolean heldDown;
   boolean heldRight;
   boolean xClicked;
   boolean yClicked;
   
   //holders for the currents distance, the highscore, and the previous highscore
   int distance = 0; 
   static int highest = 0;
   static int lastHighest = 0;
   
   Random rand = new Random();
   
   //array of all mines created
   ArrayList<Mine> list = new ArrayList<Mine>();
   
   //keeps track of the previous grid
   int lastgridx = 0;
   int lastgridy = 0;
   
   //keeps track if the player hasnt touched a mine
   boolean NotDead = true;

   public void start(Stage stage)
   {
      
   
      fp = new FlowPane();
      
      gc = theCanvas.getGraphicsContext2D();
      drawBackground(300,300,gc);

      fp.getChildren().add(theCanvas);
      
         theCanvas.setOnKeyPressed(new KeyListenerDown());
         theCanvas.setOnKeyReleased(new KeyListenerUp());
      
      
      Scene scene = new Scene(fp, 600, 600);
      stage.setScene(scene);
      stage.setTitle("Project :)");
      stage.show();
      
      theCanvas.requestFocus();

      AnimationHandler ta = new AnimationHandler();
      ta.start();

      
   }
   
   GraphicsContext gc;
   
   

   Image background = new Image("stars.png");
   Image overlay = new Image("starsoverlay.png");
   Random backgroundRand = new Random();
   //this piece of code doesn't need to be modified
   public void drawBackground(float playerx, float playery, GraphicsContext gc)
   {
	  //re-scale player position to make the background move slower. 
      playerx*=.1;
      playery*=.1;
   
	//figuring out the tile's position.
      float x = (playerx) / 400;
      float y = (playery) / 400;
      
      int xi = (int) x;
      int yi = (int) y;
      
	  //draw a certain amount of the tiled images
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(background,-playerx+i*400,-playery+j*400);
         }
      }
      
	  //below repeats with an overlay image
      playerx*=2f;
      playery*=2f;
   
      x = (playerx) / 400;
      y = (playery) / 400;
      
      xi = (int) x;
      yi = (int) y;
      
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(overlay,-playerx+i*400,-playery+j*400);
         }
      }
      
      gc.setFill(new Color(1,1,1,1));
      gc.setFont(new Font(15));
      gc.fillText("Distance: "+distance, 10,30);
      gc.fillText("Best: "+highest, 10,60);


   }
   
   //method that creates a mine in a random position of the grid. There is a 30% chance that a mine will be created
   public void minefield(int posx,int posy){
       float ranX = rand.nextFloat() * 100;
       float ranY = rand.nextFloat() * 100;
       float random = rand.nextFloat();
       
      if(random<.3){
       Mine temp = new Mine(posx*100+ranX,posy*100+ranY);
       list.add(temp);}  
   }  
   //creates mines in the grids surrounding the player
   public void gridCheck(int cgridx,int cgridy){

      int posX = cgridx;
      int posY= cgridy;
      //for the ten grids above the player, it will create mines in each one
      for(int i=0;i<10;i++){
         posX = ((cgridx-5)) + (i);
         posY = cgridy-5;
         //depending on how far away the mine is from the origin, it will create that many amount if mines
         int distance = (int)(Math.sqrt((posX*100-300)*(posX*100-300) +  (posY*100-300)*(posY*100-300))/1000);
         if(distance!=0){
            for(int j=1;j<distance+1;j++){
               minefield(posX,posY);
            }
         }
      }
      //for the ten grids below the player, it will create mines in each one
      for(int i=0;i<10;i++){
         posX = ((cgridx-5)) + (i);
         posY = cgridy+4;
         //depending on how far away the mine is from the origin, it will create that many amount if mines
         int distance = (int)(Math.sqrt((posX*100-300)*(posX*100-300) +  (posY*100-300)*(posY*100-300))/1000);
         if(distance!=0){
            for(int j=1;j<distance+1;j++){
               minefield(posX,posY);
            }
         }
      }
      //for the ten grids to the left the player, it will create mines in each one
      for(int i=0;i<10;i++){
         posX = (cgridx-5);
         posY = (cgridy-5) + (i);
         //depending on how far away the mine is from the origin, it will create that many amount if mines
         int distance = (int)(Math.sqrt((posX*100-300)*(posX*100-300) +  (posY*100-300)*(posY*100-300))/1000);
         if(distance!=0){
            for(int j=1;j<distance+1;j++){
               minefield(posX,posY);
            }
         }
      }
      //for the ten grids to the right the player, it will create mines in each one
      for(int i=0;i<10;i++){
         posX = (cgridx+4);
         posY = (cgridy-5) + (i);
         //depending on how far away the mine is from the origin, it will create that many amount if mines
         int distance = (int)(Math.sqrt((posX*100-300)*(posX*100-300) +  (posY*100-300)*(posY*100-300))/1000);
         if(distance!=0){
            for(int j=1;j<distance+1;j++){
               minefield(posX,posY);
            }
         }

      }



   }   
   
   boolean check = false;
   public class AnimationHandler extends AnimationTimer
   {
      public void handle(long currentTimeInNanoSeconds) 
      {
         //draws the background
         gc.clearRect(0,0,600,600);
         drawBackground(thePlayer.getX(),thePlayer.getY(),gc); 
         //if the player is alive, then it will call the act method for the player wich allows it to move, if the player is dead, it will call the death method of player
         if(NotDead){
            thePlayer.act(xClicked,yClicked,heldUp, heldLeft, heldDown,heldRight, gc);
         }
         else{
            thePlayer.death();

         }
         //gets the distance of the player between the origin, and if the distance is higher than the highscore, it will make it the highscore
         distance = (int)thePlayer.distance(orgin);
         if(highest<distance){
            highest = distance;
            lastHighest = highest;
         }

         //gets the current grid that the player is in
         int cgridx = ((int)thePlayer.getX())/100;
         int cgridy = ((int)thePlayer.getY())/100;
         
         //if the player changes grids, then the gridCheck method is called
         if((cgridx != lastgridx) || (cgridy != lastgridy)){
            gridCheck(cgridx,cgridy);}
         
         //for each mine in the mine array, the shadeChanger method is called so it can cycle between colors, znd the mine is drawn
         for(int i = 0;i<list.size();i++){
            list.get(i).shadeChanger();
            list.get(i).draw(thePlayer.getX(),thePlayer.getY(),gc,false);
            //if the mine is father than 800 from the player, then the mine is removed, if the mine is closer than 20, then its removed and the player is dead
            int dis = (int)thePlayer.distance(list.get(i));
            if(dis>800){
               list.remove(list.get(i));
            }
            if(dis<=20){
            list.remove(list.get(i));
            NotDead = false;
            }
         }


         //example calls of draw - this should be the player's call for draw
         
         
         //if the playe is not dead, then player is drawn
         if(NotDead){
            thePlayer.draw(300,300,gc,true);
         } 
         
         //all other objects will use false in the parameter.
         //min.draw(350,350,gc,false);
         //example call of a draw where m is a non-player object. Note that you are passing the player's position in and not m's position.
         
         //the previous grid the player was at is stored 
         lastgridx = cgridx;
         lastgridy = cgridy;
         
      }
   }
   public class KeyListenerDown implements EventHandler<KeyEvent>  
   {
      public void handle(KeyEvent event) 
      {
          // if the player is not dead, then if w,a,s,d is pressed, then its recorded
          if(NotDead){
             if (event.getCode() == KeyCode.W) 
             {
               heldUp = true;
               yClicked = true;
             }
             if (event.getCode() == KeyCode.S) 
             {
               heldDown = true;
               yClicked = true;
             }
             if (event.getCode() == KeyCode.A)
             {
               heldLeft = true;
               xClicked = true;
             }
             if (event.getCode() == KeyCode.D) 
             {
               heldRight = true;
               xClicked = true;
             }
          }
      }
    }
    public class KeyListenerUp implements EventHandler<KeyEvent>  
    {
      public void handle(KeyEvent event) 
      {
          //if a,w,s,d is lifted up, then it is recorded
          if (event.getCode() == KeyCode.W) 
          {
            heldUp = false;
            yClicked = false;
          }
          else if (event.getCode() == KeyCode.S) 
          {
            heldDown = false;
            yClicked = false;
          }
          if (event.getCode() == KeyCode.A)
          {
            heldLeft = false;
            xClicked=false;
          }
          else if (event.getCode() == KeyCode.D) 
          {
            heldRight = false;
            xClicked=false;
          }
      }
      
   }
   static boolean start = false;
   public static void main(String[] args)
   {
           //attemps to read the file highScore and get the highscore recorded in there, if highscore dosent exist, then start is true
           try{
               Scanner readList = new Scanner(new File("highScrore.txt"));
               lastHighest = readList.nextInt();
               highest = lastHighest;
            }
            catch(FileNotFoundException fnfe){
               start = true;
            }
      //allows methods of application run
      launch(args);
      
      //if start, the lasthighest is the current highest
      if(start){
         lastHighest = highest;
      }
      //Creates or edits a file called highScore, if start is true, then it puts the lasthighest in, then makes false. If not, then if highest is greater 
      //than lasthighest, then it writes highest. If not any of those then lasthighest is printed.
      try{ 
         FileOutputStream fos = new FileOutputStream("highScrore.txt",false);
         PrintWriter pw = new PrintWriter(fos);
         if(start){
            pw.println(lastHighest);
            start = false;
         }
         else if(highest > lastHighest){
            pw.println(highest);
         }
         else{
            pw.println(lastHighest);
         }
         pw.close();
      }
      catch(FileNotFoundException fnfe)
      {
         System.out.print("not working");
      
      }
      
      
   }
}

