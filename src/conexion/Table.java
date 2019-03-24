package conexion;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.sun.xml.internal.ws.db.DatabindingImpl;

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
	final static int ServerPortComida = 1555; 
	  
	public InetAddress ip; 
      
    public Socket s; 
      
    public DataInputStream dis; 
    public DataOutputStream dos; 
    
    
    public Socket sComida; 
      
    public DataInputStream disComida; 
    public DataOutputStream dosComida; 
	
	public Table() {
	
		comida= new ArrayList<>();
		otrosJugadores= new ArrayList<>();
		try {
			ip= InetAddress.getByName("localhost");
			
			s = new Socket(ip, ServerPort);
			//s.getOutputStream().write("2\n".getBytes());
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			
			//sComida = new Socket(ip, ServerPortComida);
			
			//disComida = new ObjectInputStream(sComida.getInputStream());
			//dosComida = new ObjectOutputStream(sComida.getOutputStream());
			System.out.println(true);
			
			
			
			HiloActualizarJugadores nuevo= new HiloActualizarJugadores(this);
            Thread t = new Thread(nuevo); 
            
            System.out.println("clienthilo"); 
  
            // add this client to active clients list 
  
            // start the thread. 
            t.start(); 
//            Thread readMessage = new Thread(new Runnable()  
//            { 
//                public void run() { 
//      
//                    while (true) { 
//                        try { 
//                            Player msg =(Player) dis.readObject();
//                            System.out.println("recibo jugador" + msg.getName());
//                            actualizarJugador(msg);
//                            //System.out.println(msg); 
//                        } catch (Exception e) { 
//      
//                            e.printStackTrace(); 
//                        } 
//                    } 
//                } 
//            }); 
//            readMessage.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		jugador= new Player("Nothing");
		crearComida();
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
            dos.writeUTF(jugador.getName()+"#"+jugador.getAlive()+"#"+jugador.getPosX()+"#"+jugador.getPosY()+"#"+jugador.getMass()); 
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
	public void actualizarJugador(String msg) {
		
		try {
          StringTokenizer st = new StringTokenizer(msg, "#"); 
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
        if(!encontro) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ArrayList<Player> getOtrosJugadores() {
		return otrosJugadores;
	}
	public void setOtrosJugadores(ArrayList<Player> otrosJugadores) {
		this.otrosJugadores = otrosJugadores;
	}
	
}
