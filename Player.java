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
   public void act(boolean xClicked,boolean yClicked, boolean w, boolean a, boolean s, boolean d, GraphicsContext gc){
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
       setX(getX()+xForce);
       setY(getY()+yForce);
   }
}
