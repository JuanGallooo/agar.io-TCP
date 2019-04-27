package Mundo;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;
import conexion.Table;

public class Ball {
	
	/**
	 * Defines the radious of a certain ball.
	 */
	private int radious;
	/**
	 * Defines the area that a certain circles ocupies.
	 */
	private double area;
	/**
	 * Defines the mass that is containes inside a ball.
	 */
	private int mass;
	/**
	 * The X coordinates of a ball in space, according to the cartesian plane;
	 */
	private double posX;
	/**
	 * The Y coordinates of a ball in space, according to the cartesian plane;
	 */
	private double posY;
	/**
	 * Defines the color of a certain ball.
	 */
	private Color color;
	
	/**
	 * Initialices a new ball with the default settings wich are:
	 * mass=DEFAULT_MASS
	 * radious=DEFAULT_MASS/3
	 * area=pi*radious^2
	 * posX random value
	 * posY random value
	 * color random value
	 */
	public Ball(int mass) {
		this.mass = mass;
		
		this.radious = mass/3;
		
		this.area = Math.PI*Math.pow(radious, 2);
		
		this.posX =  ThreadLocalRandom.current().nextInt(0, Table.ANCHO_TABLERO + 1);
		
		this.posY = ThreadLocalRandom.current().nextInt(0, Table.LARGO_TABLERO + 1);
		
		this.color = new Color((int)(Math.random() * 0x1000000));
	}

	public int getRadious() {
		return radious;
	}

	public void setRadious(int radious) {
		this.radious = radious;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public int getMass() {
		return mass;
	}

	public void setMass(int mass) {
		this.mass = mass;
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double d) {
		this.posX = d;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	

}
