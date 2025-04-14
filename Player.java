import javafx.scene.paint.*;
import javafx.scene.canvas.*;

//this is an example object
public class Player extends DrawableObject
{
	float xForce=0;
   float yForce=0;
   //takes in its position
   public Player(float x, float y)
   {
      super(x,y);
   }
   //draws itself at the passed in x and y.
   public void drawMe(float x, float y, GraphicsContext gc)
   {
      gc.setFill(Color.BLACK);
      gc.fillOval(x-14,y-14,27,27);
      gc.setFill(Color.GRAY);
      gc.fillOval(x-13,y-13,25,25);
   }
   //act method takes in wich keys are being pressed and if itll effect x or y, this will then move the player in the direction with force accounted for
   public void act(boolean xClicked,boolean yClicked, boolean w, boolean a, boolean s, boolean d, GraphicsContext gc){
      //if x is being affected, if the force is between -5 and 5, then it will take away -.1 to the left and +.1 to the right
      if(xClicked){
         if(xForce>-5 && xForce<5){
            if(a){
               xForce-=.1;
            }
            else if(d){
               xForce+=.1;
            }
         }
      }
      //if not clided, then itll add or subtract .025 until force is bwtween .25/-.25 wich will make it 0
      else{
         if(xForce>0){
            xForce-=(float).025;
            }
         else if(xForce<0){
            xForce+=(float).025;
            }
         if(.25 < xForce && xForce < .25){
            xForce = 0; 
         }
      }
      //if y is being affected, if the force is between -5 and 5, then it will take away -.1 for up and +.1 for down
      if(yClicked){
         if(yForce>-5 && yForce<5){
            if(w){
               yForce-=.1;
            }
            else if(s){
               yForce+=.1;
            }
         }
       }
      //if not clided, then itll add or subtract .025 until force is bwtween .25/-.25 wich will make it 0
       else{
         if(yForce>0){
            yForce-=(float).025;
         }
         else if(yForce<0){
            yForce+=(float).025;
         }
         if(.25 < yForce && yForce < .25){
            yForce = 0;
         }
       }
       //set the player positon to what the xforce effects it
       setX(getX()+xForce);
       setY(getY()+yForce);
   }
   //death method makes it so force is 0
   public void death(){
      yForce = 0;
      xForce = 0;
   }
   
}
