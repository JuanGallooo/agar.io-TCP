package Mundo;

public class Food extends Ball{

	private boolean comido;
	
	public boolean isComido() {
		return comido;
	}

	public void setComido(boolean comido) {
		this.comido = comido;
	}

	public Food(int mass) {
		super(mass);
		comido= false;
		// TODO Auto-generated constructor stub
	}

	public final static int DEFAULT_MASS=5;
	

	
	
}