package Mundo;

import java.util.ArrayList;

public class PlayGround {
	
	public final static int LENGHT=1000;
	
	public final static int WIDTH=1000;
	
	private ArrayList<Ball> balls;

	public PlayGround() {
<<<<<<< HEAD
<<<<<<< HEAD
	
		balls = new ArrayList<Ball>();
		
=======

		ballsF = new ArrayList<Ball>();

>>>>>>> parent of 6afae7c... correccion menor
		for (int i = 0; i < 600; i++) {
			Food a=new Food(5);
=======
	
		balls = new ArrayList<Ball>();
		
		for (int i = 0; i < 600; i++) {
			Food a=new Food();
>>>>>>> parent of f5fd159... mundo terminado
			balls.add(a);
		}
	}
	
	public void addPlayer(String name) {
		
		Player a=new Player(name);
		balls.add(a);
		
	}
	
	public void removePlayer(Player a) {
		for (int i = 0; i < balls.size()-1; i++) {
			if(balls.get(i).equals(a)) {
				balls.remove(a);
			}
		}	
	}
	
	 public void checkForCollision(){
		 ArrayList<Ball>  checked=new  ArrayList<Ball>();
		 
		 
		 for (int i = 0; i < balls.size()-1; i++) {
			
			 
			 if(!checked.contains(balls.get(i))) {
				 
				 for (int j = 0; j < balls.size()-1; j++) {
					
					 if(!balls.get(i).equals(balls.get(j))){
						 
						 if(distance(balls.get(i).getPosX(),balls.get(i).getPosY(), balls.get(j).getPosX(),balls.get(j).getPosY())<balls.get(i).getRadious()+balls.get(j).getRadious()) {
							 
<<<<<<< HEAD
							// if() {
								 
							 //}else if() {
								 
							 //}
=======
							 if() {
								 
							 }else if() {
								 
							 }
>>>>>>> parent of f5fd159... mundo terminado
							 
						 }
						 
						 
					 }
				}
				 
				 
				 
				 
			 }
			
		}
		 
	 }
	 
	 public static int distance(int x1, int y1, int x2, int y2){
	      return (int)Math.sqrt(((x2-x1)*(x2-x1))+((y2-y1)*(y2-y1)));
	   }

}
