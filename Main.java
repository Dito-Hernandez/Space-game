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


import java.util.*;
import java.text.*;
import java.io.*;
import java.lang.*;
import javafx.application.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import java.net.*;
import javafx.geometry.*;


public class Main extends Application
{
   FlowPane fp;
   
   Canvas theCanvas = new Canvas(600,600);
   
   Player thePlayer = new Player(300,300);
   
   Player orgin = new Player(300,300);
   
   Mine tester = new Mine(500,500);
   

   
   boolean heldUp;
   boolean heldLeft;
   boolean heldDown;
   boolean heldRight;
   boolean xClicked;
   boolean yClicked;
   
   int distance = 0; 
   
   static int highest = 0;
   
   Random rand = new Random();
   
   ArrayList<Mine> list = new ArrayList<Mine>();
   
   int lastgridx = 0;
   int lastgridy = 0;

   

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
   
   public void minefield(int posx,int posy){
       float ranX = rand.nextFloat() * 100;
       float ranY = rand.nextFloat() * 100;
       float random = rand.nextFloat();
       
      if(random<.3){
       Mine temp = new Mine(posx*100+ranX,posy*100+ranY);
       list.add(temp);}

         
        
   }  
   public void gridCheck(int cgridx,int cgridy){

      int posX = cgridx;
      int posY= cgridy;
      for(int i=0;i<10;i++){
         posX = ((cgridx-5)) + (i);
         posY = cgridy-5;
         int distance = (int)(Math.sqrt((posX*100-300)*(posX*100-300) +  (posY*100-300)*(posY*100-300))/1000);
         if(distance!=0){
            for(int j=1;j<distance+1;j++){
               minefield(posX,posY);
            }
         }
      }
      for(int i=0;i<10;i++){
         posX = ((cgridx-5)) + (i);
         posY = cgridy+4;
         int distance = (int)(Math.sqrt((posX*100-300)*(posX*100-300) +  (posY*100-300)*(posY*100-300))/1000);
         if(distance!=0){
            for(int j=1;j<distance+1;j++){
               minefield(posX,posY);
            }
         }

      }
      for(int i=0;i<10;i++){
         posX = (cgridx-5);
         posY = (cgridy-5) + (i);
         int distance = (int)(Math.sqrt((posX*100-300)*(posX*100-300) +  (posY*100-300)*(posY*100-300))/1000);
         if(distance!=0){
            for(int j=1;j<distance+1;j++){
               minefield(posX,posY);
            }
         }

      }
      for(int i=0;i<10;i++){
         posX = (cgridx+4);
         posY = (cgridy-5) + (i);
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
         gc.clearRect(0,0,600,600);
         
         drawBackground(thePlayer.getX(),thePlayer.getY(),gc); 

         thePlayer.act(xClicked,yClicked,heldUp, heldLeft, heldDown,heldRight, gc);
         distance = (int)thePlayer.distance(orgin);
         if(highest<distance){
            highest = distance;
         }


         int cgridx = ((int)thePlayer.getX())/100;
         int cgridy = ((int)thePlayer.getY())/100;
         
         if((cgridx != lastgridx) || (cgridy != lastgridy)){
            gridCheck(cgridx,cgridy);}
         
         for(int i = 0;i<list.size();i++){
            list.get(i).shadeChanger();
            list.get(i).draw(thePlayer.getX(),thePlayer.getY(),gc,false);
            int dis = (int)thePlayer.distance(list.get(i));
            if(dis>800){
               list.remove(list.get(i));
            }
         }



	      //example calls of draw - this should be the player's call for draw
         thePlayer.draw(300,300,gc,true); //all other objects will use false in the parameter.
         //min.draw(350,350,gc,false);
         //example call of a draw where m is a non-player object. Note that you are passing the player's position in and not m's position.
         
         lastgridx = cgridx;
         lastgridy = cgridy;
         
      }
   }
   public class KeyListenerDown implements EventHandler<KeyEvent>  
   {
      public void handle(KeyEvent event) 
      {
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
    public class KeyListenerUp implements EventHandler<KeyEvent>  
    {
      public void handle(KeyEvent event) 
      {
          
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
   
   public static void main(String[] args)
   {
      launch(args);
   }
}

