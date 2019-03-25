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
	/**
	 * This parameter represents the width of the table of the player but it is constant 600
	 */
	public static int ANCHO_TABLERO=600;
	/**
	 * This parameter represents the long of the table of the player but it is constant 400
	 */
	public static int LARGO_TABLERO=400;
	/**
	 * This parameter represents the width of the table of the player 
	 */
	private int largoMaximo;
	/**
	 * This parameter represents the long of the table of the player 
	 */
	private int anchoMaximo;
	/**
	 * This array represents the food that have the player in this table, this is constantly refresh whit the information of the table 
	 */
	private ArrayList<Food> comida;
	/**
	 * This parameter represents the player in the table
	 */
	private Player jugador;
	/**
	 * This parameter of type array represents the players that are in the table except the principal player, this is use to see the collisions 
	 */
	private ArrayList<Player> otrosJugadores;
	/**
	 * This constant represent the port of the server
	 */
	final static int ServerPort = 8000;
	/**
	 * This is the IP to connect, in this case we use the localhost , the 127.0.0.1 
	 */
	public InetAddress ip; 
	/**
	 * This represents the socket of the table, is the media to connect to the server
	 */
    public Socket s; 
    /**
     * The flush of data that becomes of the server
     */
    public DataInputStream dis; 
    /**
     * The flush of data that we use to send information relevant to the server.
     */
    public DataOutputStream dos; 
	/**
	 * The constructor of the class Table
	 */
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
	/**
	 * This method return the DataInputStream initialized in the class
	 * @return dis
	 */
	public DataInputStream getDis() {
		return dis;
	}
	/**
	 * The set of the datainputStream
	 * @param dis the new flush
	 */
	public void setDis(DataInputStream dis) {
		this.dis = dis;
	}
	/**
	 * This method return the DataOutPutStream initialized in the class
	 * @return dos
	 */
	public DataOutputStream getDos() {
		return dos;
	}
	/**
	 * The set of the DataOutPutStream
	 * @param dis the new flush
	 */
	public void setDos(DataOutputStream dos) {
		this.dos = dos;
	}
	/**
	 * This method send to the server the information of the player that has in the moment a change
	 */
	public void mandarInfo() {
        try { 
            dos.writeUTF("&"+"#"+jugador.getName()+"#"+jugador.getAlive()+"#"+jugador.getPosX()+"#"+jugador.getPosY()+"#"+jugador.getMass()); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
	}
	/**
	 * This method used to move the player to an x and an y recived by parameter, then change the direction of the player and send the new information 
	 * @param x the new posX
	 * @param y the new posY
	 */
	public void Movimiento(int x, int y) {
		jugador.move();
		jugador.changeDirection(x, y);
		mandarInfo();
	}
	/**
	 * Get of the max length 
	 * @return largoMaximo
	 */
	public int getLargoMaximo() {
		return largoMaximo;
	}
	/**
	 * set of the max length
	 * @param largoMaximo new max lenght
	 */
	public void setLargoMaximo(int largoMaximo) {
		this.largoMaximo = largoMaximo;
	}
	/**
	 * Get of the max width
	 * @return anchoMaximo
	 */
	public int getAnchoMaximo() {
		return anchoMaximo;
	}
	/**
	 * Set of the max width 
	 * @param anchoMaximo the new max width
	 */
	public void setAnchoMaximo(int anchoMaximo) {
		this.anchoMaximo = anchoMaximo;
	}
	/**
	 * The get of the array of food
	 * @return comida
	 */
	public ArrayList<Food> getComida() {
		return comida;
	}
	/**
	 * The set of the array the food
	 * @param comida the new Array of food
	 */
	public void setComida(ArrayList<Food> comida) {
		this.comida = comida;
	}
	/**
	 * The get of the player 
	 * @return jugador 
	 */
	public Player getJugador() {
		return jugador;
	}
	/**
	 * the set of the player 
	 * @param jugador the new player
	 */
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
	/**
	 * this method return a food if a player touch a food and eaten, else this return a null if a player do no touch any food
	 * @return food return a food if a player touch , return a null if a player do no touch any food
	 */
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
	/**
	 * This method is used when the player eat a food , we use this new information to send to the server to say to eliminate the food
	 * @param food the food that has eaten 
	 */
	private void eliminarComida(Food food) {
        try { 
            dos.writeUTF("@"+"#"+food.getColor().getRGB()+"#"+food.getPosX()+"#"+food.getPosY()+"#"+food.getMass()); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
	}
	/**
	 * Set of the name of the player
	 * @param nombre
	 */
	public void setNombreJugador(String nombre) {
		jugador.setName(nombre);
	}
	/**
	 * This method refresh the other player in the party by the information becomes by parameter , this information is received become the server.
	 * @param msg the information of the players.
	 */
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
	/**
	 * Get of the arry of others players 
	 * @return otrosJugadores 
	 */
	public ArrayList<Player> getOtrosJugadores() {
		return otrosJugadores;
	}
	/**
	 * Set of the other players 
	 * @param otrosJugadores the new Array of player that are in the party
	 */
	public void setOtrosJugadores(ArrayList<Player> otrosJugadores) {
		this.otrosJugadores = otrosJugadores;
	}
	/**
	 * This method refresh the food in the party by the information becomes by parameter , this information is received become the server,
	 * @param msg the information of new food in the server.
	 */
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
