package Mundo;

import java.util.ArrayList;

public class PlayGround {

	public final static int LENGHT=1000;

	public final static int WIDTH=1000;

	private ArrayList<Player> ballsP;

	private ArrayList<Ball> ballsF;



	public PlayGround() {

		ballsF = new ArrayList<Ball>();
		
		ballsP=new ArrayList<Player>();

		for (int i = 0; i < 600; i++) {
<<<<<<< HEAD
			Food a=new Food(5);
			balls.add(a);
=======
			
			Food a=new Food();
			
			ballsF.add(a);
>>>>>>> 30ff4ccb261c343ec1dc58f691c1472e841bd67c
		}
	}
	/**
	 * Adds a new player to the game
	 * @param name: the name of a new player
	 */
	public void addPlayer(String name) {

		Player a=new Player(name);
		
		ballsP.add(a);

	}
	/**
	 * This method checks if a payer is eating any food in game
	 */
	public void checkForColisionPlayerFood() {

		for (int i = 0; i < ballsP.size()-1; i++) {

			for (int j = 0; j < ballsF.size()-1; j++) {

				if(distance(ballsP.get(i).getPosX(),ballsP.get(i).getPosY(), ballsF.get(j).getPosX(),ballsF.get(j).getPosY())<ballsP.get(i).getRadious()+ballsP.get(j).getRadious()) {

					ballsP.get(i).winPoints(ballsF.get(j).getMass());

					ballsF.remove(ballsF.get(j));
					
					Food a=new Food();
					
					ballsF.add(a);
				}
			}
		}
	}
<<<<<<< HEAD
	
	 public void checkForCollision(){
		 ArrayList<Ball>  checked=new  ArrayList<Ball>();
		 
		 
		 for (int i = 0; i < balls.size()-1; i++) {
			
			 
			 if(!checked.contains(balls.get(i))) {
				 
				 for (int j = 0; j < balls.size()-1; j++) {
					
					 if(!balls.get(i).equals(balls.get(j))){
						 
						 if(distance(balls.get(i).getPosX(),balls.get(i).getPosY(), balls.get(j).getPosX(),balls.get(j).getPosY())<balls.get(i).getRadious()+balls.get(j).getRadious()) {
							 
							// if() {
								 
							 //}else if() {
								 
							 //}
							 
						 }
						 
						 
					 }
=======
	/**
	 * This method checks if any pair of players is colliding at any given time of the game.
	 */
	public void checkForCollisionPlayers(){

		ArrayList<Player>  checked=new  ArrayList<Player>();

		for (int i = 0; i < ballsP.size()-1; i++) {

			if(!checked.contains(ballsP.get(i))) {

				checked.add(ballsP.get(i));

				for (int j = 0; j < ballsP.size()-1; j++) {

					if(!checked.contains(ballsP.get(j))) {

						checked.add(ballsP.get(j));

						if(!ballsP.get(i).equals(ballsP.get(j))){

							if(distance(ballsP.get(i).getPosX(),ballsP.get(i).getPosY(), ballsP.get(j).getPosX(),ballsP.get(j).getPosY())<ballsP.get(i).getRadious()+ballsP.get(j).getRadious()) {

								if(ballsP.get(i).getArea()>ballsP.get(j).getArea()) {

									ballsP.get(i).winPoints(ballsP.get(j).getMass());

									ballsP.get(j).playerDies();

									ballsP.remove(ballsP.get(j));

								}else if(ballsP.get(i).getArea()<ballsP.get(j).getArea()) {

									ballsP.get(j).winPoints(ballsP.get(i).getMass());

									ballsP.get(i).playerDies();

									ballsP.remove(ballsP.get(i));

								}else if(ballsP.get(i).getArea()==ballsP.get(j).getArea()) {
									//nothing really happens here just say hi
									System.out.println("hello buddy");
								}
							}
						}
					}
>>>>>>> 30ff4ccb261c343ec1dc58f691c1472e841bd67c
				}
			}
		}
	}

	/**
	 * It calculates the distance if any pair a points in space.
	 * @param x1 first x coordinate
	 * @param y1 first y coordinate
	 * @param x2 second x coordinate
	 * @param y2 second y coordinate
	 * @return the distance between those two points
	 */
	public static int distance(int x1, int y1, int x2, int y2){
		return (int)Math.sqrt(((x2-x1)*(x2-x1))+((y2-y1)*(y2-y1)));
	}

}
