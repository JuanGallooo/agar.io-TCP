package Mundo;

import java.util.ArrayList;

public class Table {
	
	public static int ANCHO_TABLERO=600;
	public static int LARGO_TABLERO=400;
	private int largoMaximo;
	private int anchoMaximo;

	private ArrayList<Food> comida;
	private Player jugador;
	
	public Table() {
		crearComida();
	}
	public void crearComida() {
		comida= new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Food nueva= new Food();
			comida.add(nueva);
		}
		jugador= new Player("Nothing");
		
	}
	public void Movimiento(int x, int y) {
		jugador.changeDirection(x, y);
		jugador.move();
	}
	public int getLargoMaximo() {
		return largoMaximo;
	}
	public void setLargoMaximo(int largoMaximo) {
		this.largoMaximo = largoMaximo;
	}
	public int getAnchoMaximo() {
		return anchoMaximo;
	}
	public void setAnchoMaximo(int anchoMaximo) {
		this.anchoMaximo = anchoMaximo;
	}
	public ArrayList<Food> getComida() {
		return comida;
	}
	public void setComida(ArrayList<Food> comida) {
		this.comida = comida;
	}
	public Player getJugador() {
		return jugador;
	}
	public void setJugador(Player jugador) {
		this.jugador = jugador;
	}
	
}
