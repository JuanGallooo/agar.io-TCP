package Mundo;

public class Player extends Ball implements Moves{
	
	public final static int DEFAULT_MASS=10;
	
	public final static int POSITION_DELTA_MAGNITUDE=2;
	
	private String name;
	
	private double angularDireccion;
	

	
	private boolean alive;

	public Player(String name) {
		super(DEFAULT_MASS);
		this.name = name;
		alive=true;
		this.angularDireccion=0;
		
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
		this.setPosX((getPosX()+(int)(Math.round(POSITION_DELTA_MAGNITUDE*Math.cos(Math.toRadians(getAngularDireccion()))))));
		this.setPosY((getPosY()+(int)(Math.round(POSITION_DELTA_MAGNITUDE*Math.sin(Math.toRadians(getAngularDireccion()))))));

		
	}
	/**
	 * This method updates the relative angle formed from one player to a certain x,y location in space.
	 */
	@Override
	public void changeDirection(int x, int y) {
		
		double alpha=Math.toDegrees(Math.atan(y/x));
		
		if(x>0&&y>0) {
			
			this.setAngularDireccion(alpha);
			
		}else if(x<0&&y>0) {
			
			this.setAngularDireccion(180-alpha);
			
		}else if(x<0&&y<0) {
			this.setAngularDireccion(alpha+180);
			
		}else if(x>0&&y<0) {
			this.setAngularDireccion(360-alpha);
			
		}else if(x==0&&y==0) {
			this.setAngularDireccion(this.getAngularDireccion());
		}
			
	}

	
	
	
	
	
	
}
