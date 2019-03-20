package Mundo;

public class Player extends Ball{
	
	public final static int DEFAULT_MASS=10;
	
	private String Name;

	public Player(String name) {
		super(DEFAULT_MASS);
		Name = name;
	}
	
	

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}
	
	
	
	
	
}
