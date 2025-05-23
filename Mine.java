import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import java.util.*;

//this is an example object
public class Mine extends DrawableObject
{
	Random rand = new Random();
   float xForce=0;
   float yForce=0;
   float shade=rand.nextFloat();
   //takes in its position
   public Mine(float x, float y)
   {
      super(x,y);
   }
   //draws itself at the passed in x and y.
   public void drawMe(float x, float y, GraphicsContext gc)
   {
      gc.setFill(new Color(1,1,1,1));
      gc.fillOval(x-7,y-7,14,14);
      //color values green and blue are effected by the value of shade wich is effected by shadeChanger
      gc.setFill(new Color(1,1*shade,1*shade,1));
      gc.fillOval(x-6,y-6,12,12);
   }
   //shadChanger method takes in the tick from animation handler, and decreaes chade value until below 0, where itll loop back to 1 and start again
   public void shadeChanger(){
      shade-=.01;
      if(shade<=0){
         shade = 1;
      }
      
   }
   }
