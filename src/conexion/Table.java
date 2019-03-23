package conexion;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import Mundo.Food;
import Mundo.Player;

public class Table {
	
	public static int ANCHO_TABLERO=600;
	public static int LARGO_TABLERO=400;
	private int largoMaximo;
	private int anchoMaximo;

	private ArrayList<Food> comida;
	private Player jugador;
	
	private ArrayList<Player> otrosJugadores;
	
	
	final static int ServerPort = 1234; 
	  
	public InetAddress ip; 
      
    public Socket s; 
      
    public DataInputStream dis; 
    public ObjectOutputStream dos; 
	
	
	
	public Table() {
		
		comida= new ArrayList<>();
		otrosJugadores= new ArrayList<>();
		try {
			ip= InetAddress.getByName("localhost");
			s = new Socket(ip, ServerPort);
			
			dis = new DataInputStream(s.getInputStream());
			dos = new ObjectOutputStream(s.getOutputStream());
			
		} catch (Exception e) {
		}
		jugador= new Player("Nothing");
	}
	public void agregarComida(String mensaje) {
		
		
	}
	public void mandarInfo() {
        try { 
            dos.writeObject(jugador); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
	}
	public void crearComida() {
		comida= new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Food nueva= new Food();
			comida.add(nueva);
		}
		jugador= new Player("Nothing");
		
	}
	//
	public void Movimiento(int x, int y) {
		jugador.move();
		jugador.changeDirection(x, y);
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
	/**
	 * It calculates the distance if any pair a points in space.
	 * @param x1 first x coordinate
	 * 
	 * @param y1 first y coordinate
	 * @param x2 second x coordinate
	 * @param y2 second y coordinate
	 * @return the distance between those two points
	 */
	public int distance(double x1, double y1, double x2, double y2){
		return (int)Math.sqrt(Math.abs((x2-x1)*(x2-x1))+Math.abs(((y2-y1)*(y2-y1))));
	}
	public Food toco() {
		Food retorno=null;
		boolean toco= false;
		for (int j = 0; j < comida.size()-1 && !toco; j++) {
			if(distance(jugador.getCenterH(),jugador.getCenterK(), comida.get(j).getCenterH(),comida.get(j).getCenterK())<jugador.getRadious()+jugador.getRadious()) {
				jugador.winPoints(1);
				retorno= comida.get(j);
				toco= true;
				comida.remove(comida.get(j));
			}
		}
		return retorno;
	}
	public void setNombreJugador(String nombre) {
		jugador.setName(nombre);
	}
}
