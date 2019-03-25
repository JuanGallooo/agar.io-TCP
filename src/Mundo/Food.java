package Mundo;

public class Food extends Ball{

	
	private int centerH;
	
	private int centerK;
	

	public void updateRadious(){
		setRadious(getMass()/3);
		
	}

	public void updateArea() {
		
		double a=(Math.PI*Math.pow(getRadious(), 2));
		
		setArea(a);
	}
	public Food() {
		super(DEFAULT_MASS);
		centerH=(int) (this.getPosX()+this.getRadious());
		centerK=(int) (this.getPosY()+this.getRadious());
		// TODO Auto-generated constructor stub
	}

	public final static int DEFAULT_MASS=15;
	
	public void updateCenters() {
		centerH=(int) (this.getPosX()+this.getRadious());
		centerK=(int) (this.getPosY()+this.getRadious());
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

	
}
