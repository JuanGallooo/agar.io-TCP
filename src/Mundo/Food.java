package Mundo;

public class Food extends Ball{

	private boolean comido;
	
	public boolean isComido() {
		return comido;
	}

	public void setComido(boolean comido) {
		this.comido = comido;
	}

	public Food() {
		super(DEFAULT_MASS);
		comido= false;
		// TODO Auto-generated constructor stub
	}

	public final static int DEFAULT_MASS=10;
	

	
	
}
