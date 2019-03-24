package conexion;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;


import Mundo.Food;
import Mundo.Player;
import hilos.HiloActualizarJugadores;

@SuppressWarnings("serial")
public class Table implements Serializable{
	
	public static int ANCHO_TABLERO=600;
	public static int LARGO_TABLERO=400;
	private int largoMaximo;
	private int anchoMaximo;

	private ArrayList<Food> comida;
	private Player jugador;
	
	private ArrayList<Player> otrosJugadores;
	
	final static int ServerPort = 8000;
	public InetAddress ip; 
      
    public Socket s; 
      
    public DataInputStream dis; 
    public DataOutputStream dos; 
	
	public Table() {
	
		comida= new ArrayList<>();
		otrosJugadores= new ArrayList<>();
		try {
			ip= InetAddress.getByName("localhost");
			s = new Socket(ip, ServerPort);
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			HiloActualizarJugadores nuevo= new HiloActualizarJugadores(this);
            Thread t = new Thread(nuevo); 
            
  
            t.start(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		jugador= new Player("Nothing");
	}
	public DataInputStream getDis() {
		return dis;
	}
	public void setDis(DataInputStream dis) {
		this.dis = dis;
	}
	public DataOutputStream getDos() {
		return dos;
	}
	public void setDos(DataOutputStream dos) {
		this.dos = dos;
	}
	public void agregarComida(String mensaje) {
		
		
	}
	public void mandarInfo() {
        try { 
            dos.writeUTF("&"+"#"+jugador.getName()+"#"+jugador.getAlive()+"#"+jugador.getPosX()+"#"+jugador.getPosY()+"#"+jugador.getMass()); 
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
	public void Movimiento(int x, int y) {
		jugador.move();
		jugador.changeDirection(x, y);
		mandarInfo();
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
			if( comida.get(j)!=null) {
				
			if(distance(jugador.getCenterH(),jugador.getCenterK(), comida.get(j).getCenterH(),comida.get(j).getCenterK())<jugador.getRadious()+jugador.getRadious()) {
				jugador.winPoints(1);
				retorno= comida.get(j);
				toco= true;
				if(comida.get(j)!=null) {
				eliminarComida(comida.get(j));
				comida.remove(comida.get(j));
				}
			}
			}
		}
		return retorno;
	}
	private void eliminarComida(Food food) {
        try { 
            dos.writeUTF("@"+"#"+food.getColor().getRGB()+"#"+food.getPosX()+"#"+food.getPosY()+"#"+food.getMass()); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
	}
	public void setNombreJugador(String nombre) {
		jugador.setName(nombre);
	}
	public void actualizarJugador(String msg) {
		
		try {
          StringTokenizer st = new StringTokenizer(msg, "#"); 
          if(msg.split(" ").length<=1) {
        	  
          st.nextToken();
          String nombre= st.nextToken(); 
          boolean alive= Boolean.parseBoolean(st.nextToken());
          double posX= Double.parseDouble(st.nextToken());
          double posy= Double.parseDouble(st.nextToken());
          int getMass=Integer.parseInt(st.nextToken());
          boolean encontro= false;
          for (int i = 0; i < otrosJugadores.size() && !encontro; i++) {
			if( otrosJugadores.get(i).getName().equals(nombre)) {
				encontro= true;
				otrosJugadores.get(i).setAlive(alive);
				otrosJugadores.get(i).setPosX(posX);
				otrosJugadores.get(i).setPosY(posy);
				otrosJugadores.get(i).setMass(getMass);
				otrosJugadores.get(i).updateRadious();
				otrosJugadores.get(i).updateArea();
				otrosJugadores.get(i).updateCenters();
			}
		}
        if(!encontro && !nombre.equals(jugador.getName())) {
        	Player nuevo= new Player(nombre);
			nuevo.setAlive(alive);
			nuevo.setPosX(posX);
			nuevo.setPosY(posy);
			nuevo.setMass(getMass);
			nuevo.updateRadious();
			nuevo.updateArea();
			nuevo.updateCenters();
			otrosJugadores.add(nuevo);
        }
          }
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(msg);
		}
	}
	public ArrayList<Player> getOtrosJugadores() {
		return otrosJugadores;
	}
	public void setOtrosJugadores(ArrayList<Player> otrosJugadores) {
		this.otrosJugadores = otrosJugadores;
	}
	public void actualizarComida(String msg) {
		try {
	          StringTokenizer st = new StringTokenizer(msg, "#"); 
	          st.nextToken();
	          int comidaIn= Integer.parseInt(st.nextToken());
	          comida.removeAll(comida);
	          for (int i = 0; i < comidaIn; i++) {
	          String color= st.nextToken();
	          Color c= Color.decode(color);
	          double x= Double.parseDouble(st.nextToken());
	          double y= Double.parseDouble(st.nextToken());
	          int mass= Integer.parseInt(st.nextToken());
	        	  Food nueva= new Food();
	        	  nueva.setColor(c);
	        	  nueva.setPosX(x);
	        	  nueva.setPosY(y);
	        	  nueva.setMass(mass);
	        	  nueva.updateRadious();
	        	  nueva.updateArea();
	        	  nueva.updateCenters();
	        	  comida.add(nueva);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
