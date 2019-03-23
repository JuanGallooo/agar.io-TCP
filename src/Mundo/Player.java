package Mundo;

import java.util.ArrayList;

public class Player extends Ball implements Moves{
	
	public final static int DEFAULT_MASS=50;
	
	public final static int POSITION_DELTA_MAGNITUDE=1;
	
	private String name;
	
	private int centerH;
	
	private int centerK;
	
	private double angularDireccion;
	
	private ArrayList<Food> comida;
	
	private boolean alive;

	public Player(String name) {
		super(DEFAULT_MASS);
		this.name = name;
		alive=true;
		comida=new ArrayList<Food>();
		this.angularDireccion=0;
		centerH=(int) (this.getPosX()+this.getRadious());
		centerK=(int) (this.getPosY()+this.getRadious());
	}
	public void comer(Food a) {
		comida.add(a);
	}
	public int getMasaGanada() {
		int suma= 0;
		for (int i = 0; i < comida.size(); i++) {
			suma+= comida.get(i).getMass();
		}
		return suma;
	}
	
	public int getCenterH() {
		return centerH;
	}

	public void setCenterH(int centerH) {
		this.centerH = centerH;
	}

	public int getCenterK() {
		return centerK;
	}

	public void setCenterK(int centerK) {
		this.centerK = centerK;
	}

	/**
	 * It adds mass to the actual ball
	 * @param massToAdd: the mass that will be added to the ball
	 */
	public void winPoints(int massToAdd) {
		setMass(getMass()+massToAdd);
		updateRadious();
		updateArea();
	}
	
	/**
	 * It defines the state of the alive variable to false
	 */
	public void playerDies() {
		setAlive(false);
	}
	
	public void updateRadious(){
		setRadious(getMass()/3);
		
	}
	public void updateArea() {
		
		double a=(Math.PI*Math.pow(getRadious(), 2));
		
		setArea(a);
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public boolean getAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	
	public double getAngularDireccion() {
		return angularDireccion;
	}

	public void setAngularDireccion(double angularDireccion) {
		this.angularDireccion = angularDireccion;
	}

	
	/**
	 * Updates the position of one player.
	 */
	@Override
	public void move() {
		// TODO Auto-generated method stub
		double deltax=Math.abs(POSITION_DELTA_MAGNITUDE*Math.cos(Math.toRadians(getAngularDireccion())));
		double deltay=Math.abs(POSITION_DELTA_MAGNITUDE*Math.sin(Math.toRadians(getAngularDireccion())));
		
		
		if(getAngularDireccion()<=90&&getAngularDireccion()>=0) {
			this.setPosX((getPosX()+deltax));
			this.setPosY((getPosY()-deltay));
			this.setCenterH((int) (this.getPosX()+this.getRadious()));
			this.setCenterK((int) (this.getPosY()+this.getRadious()));
			
			
		}else if(getAngularDireccion()<=180&&getAngularDireccion()>90) {
			this.setPosX((getPosX()-deltax));
			this.setPosY((getPosY()-deltay));
			this.setCenterH((int) (this.getPosX()+this.getRadious()));
			this.setCenterK((int) (this.getPosY()+this.getRadious()));
			
		}else if(getAngularDireccion()<=270&&getAngularDireccion()>180) {
			this.setPosX((getPosX()-deltax));
			this.setPosY((getPosY()+deltay));
			this.setCenterH((int) (this.getPosX()+this.getRadious()));
			this.setCenterK((int) (this.getPosY()+this.getRadious()));
			
		}else if(getAngularDireccion()<=360&&getAngularDireccion()>=270) {
			this.setPosX((getPosX()+deltax));
			this.setPosY((getPosY()+deltay));
			this.setCenterH((int) (this.getPosX()+this.getRadious()));
			this.setCenterK((int) (this.getPosY()+this.getRadious()));
		}

		
	}
	/**
	 * This method updates the relative angle formed from one player to a certain x,y location in space.
	 */
	@Override
	public void changeDirection(double x, double y) {
		
		if( x!=0&& y!=0) {
			
		double alpha=Math.toDegrees(Math.atan(y/x));

//		System.out.println("alpha "+alpha);
//		System.out.println("divi "+y/x);
//		System.out.println("X "+x);
//		System.out.println("Y "+y);
//		System.out.println("--------------");
		
		if(x>0&&y>0) {
			
			this.setAngularDireccion(alpha);
			
		}else if(x<0&&y>0) {
			
			this.setAngularDireccion(alpha+180);
			
		}else if(x<0&&y<0) {
			this.setAngularDireccion(alpha+180);
			
		}else if(x>0&&y<0) {
			this.setAngularDireccion(360+alpha);
			
		}else if(x==0&&y==0) {
			this.setAngularDireccion(this.getAngularDireccion());
		}
		}
		else {
			this.setAngularDireccion(0);
		}
		//System.out.println(this.getAngularDireccion());
	}

	
	
	
	
	
	
}
