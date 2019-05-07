package conexion;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


import Mundo.Food;
import Mundo.Player;
import hilos.HiloActualizarJugadores;
import hilos.HiloAudioUDPClient;
import hilos.HiloEscuchaRespuestaSSL;
import hilos.HiloEscuchaStreaming;

@SuppressWarnings("serial")
public class Table {
	
	
	/**
	 * This parameter represents the width of the table of the player but it is constant 600
	 */
	public static int ANCHO_TABLERO=600;
	/**
	 * This parameter represents the long of the table of the player but it is constant 400
	 */
	public static int LARGO_TABLERO=400;
	
	
	public static final int PORT_AUDIO = 9999;
	public static final String DIRECCION_MULTICAST = "224.0.0.1";
	
	
	public static String ENABLE="ENABLE";
	public static String DISABLE="DISABLE";
	public static String WAITING="WAITING";
	public static String WINNER="WINNER";
	
	public static String TYPE_PLAYER="TYPE_PLAYER";
	public static String TYPE_STREAMER="TYPE_STREAMER";
	
	public static int STREAMING_PORT= 5000;
	
	private String tipoCliente;
	
	

	private MulticastSocket dtSocket;
	private HiloAudioUDPClient hiloAudioCliente;
	
	
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
	 * This atribute represents if the client is conected to the server
	 */
	private String conected;
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
     * Thread that represents if we can actualizate the players 
     */
    private Thread actualizar;
    
    private ObjectOutputStream salidaSSL;
    
    private ObjectInputStream entradaSSL;
    
    private SSLSocket sslsocket;
    /**
     * This atribute indicates if the client is avaible to login in the server
     */
    private String Estado;
    
    private String Ganador;

	/**
	 * The constructor of the class Table
	 */
	public Table() {
		//iniciaLogin= true;
		Estado= null;
		conected=Table.DISABLE;
		comida= new ArrayList<>();
		otrosJugadores= new ArrayList<>();

		jugador= new Player("Nothing");
		
		System.setProperty("javax.net.ssl.trustStore", "./docs/server.jks");
		
		
		
	}
	public void conectarAServidor() {
		try {
			if(hiloStreaming!= null) {
				hiloStreaming.dejarEscuchar();
			}
			hiloAudioCliente = new HiloAudioUDPClient(this);
			hiloAudioCliente.start();
			tipoCliente= Table.TYPE_PLAYER;
			ip= InetAddress.getByName(Server.HOST);
			s = new Socket(ip, ServerPort);
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			conected= Table.ENABLE;
			HiloActualizarJugadores nuevo= new HiloActualizarJugadores(this);
            actualizar = new Thread(nuevo); 
            actualizar.start(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private HiloEscuchaStreaming hiloStreaming;
	public void conectarAStreaming() {
		hiloAudioCliente = new HiloAudioUDPClient(this);
		hiloAudioCliente.start();
		tipoCliente= Table.TYPE_STREAMER;
		hiloStreaming = new HiloEscuchaStreaming(this);
		Thread escuchar= new Thread(hiloStreaming); 
        escuchar.start(); 
	}
	
	public MulticastSocket getDtSocket() {
		return dtSocket;
	}

	public void setDtSocket(MulticastSocket dtSocket) {
		this.dtSocket = dtSocket;
	}
	public void conectarSSL() {
		try {
			SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
			sslsocket = (SSLSocket) f.createSocket(Server.HOST, 6500); 
			sslsocket.startHandshake();
			System.out.println("Authentication done");
			
			salidaSSL = new ObjectOutputStream(sslsocket.getOutputStream());
			entradaSSL = new ObjectInputStream(sslsocket.getInputStream());
			
			HiloEscuchaRespuestaSSL nuevo= new HiloEscuchaRespuestaSSL(this,entradaSSL, salidaSSL);
			Thread iniciar= new Thread(nuevo);
			iniciar.start();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	

	public ObjectInputStream getEntradaSSL() {
		return entradaSSL;
	}
	public void setEntradaSSL(ObjectInputStream entradaSSL) {
		this.entradaSSL = entradaSSL;
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
        	//DataOutputStream nuevo = new DataOutputStream(s.getOutputStream());
        	//setDos(nuevo);
        	
            dos.writeUTF("&"+"#"+jugador.getName()+"#"+jugador.getAlive()+"#"+round(jugador.getPosX())+"#"+round(jugador.getPosY())+"#"+jugador.getMass());
            dos.flush();
            
          //  System.out.println("&"+"#"+jugador.getName()+"#"+jugador.getAlive()+"#"+round(jugador.getPosX())+"#"+round(jugador.getPosY())+"#"+jugador.getMass());
            dos.flush();
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
	}
	
	
	public double round(double entry) {
		return Math.round(entry * 1000000) / 1000000;
	}
	
	/**
	 * This method used to move the player to an x and an y recived by parameter, then change the direction of the player and send the new information 
	 * @param x the new posX
	 * @param y the new posY
	 */
	public void Movimiento(int x, int y) {
		if( jugador.getAlive()) {
		jugador.move();
		jugador.changeDirection(x, y);
		mandarInfo();
		}
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
			    eliminarComida(retorno);
				comida.remove(comida.get(j));
				}
			}
			}
		}
		return retorno;
	}
	
		public Player CollisionPlayers() {
			Player retorno= null;
			boolean toco= false;
			for(int j=0; j<otrosJugadores.size()&& !toco;j++) {
				
				if(distance(jugador.getCenterH(),jugador.getCenterK(), otrosJugadores.get(j).getCenterH(),otrosJugadores.get(j).getCenterK())<(jugador.getRadious()+otrosJugadores.get(j).getRadious())/10) {

					if(jugador.getArea()>otrosJugadores.get(j).getArea() && otrosJugadores.get(j).getAlive()) {
						jugador.winPoints(otrosJugadores.get(j).getMass());
						toco= true;
						otrosJugadores.get(j).setAlive(false);
						mandarInfo();
					}else if(jugador.getArea()<otrosJugadores.get(j).getArea()&& otrosJugadores.get(j).getAlive()) {
						retorno= jugador;
						if(jugador.getAlive()) {
					    System.out.println("Acabe de morir");
						toco= true;
						jugador.playerDies();
						mandarInfo();
						
						}
						try {
							if(s!=null) {
								s.close();
								s=null;
							}
						} catch (IOException e) {
							e.printStackTrace();
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
            dos.writeUTF("@"+"#"+food.getColor().getRGB()+"#"+round(food.getPosX())+"#"+round(food.getPosY())+"#"+food.getMass()); 
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
	public Player actualizarJugador(String msg) {
		Player nuevoPlayer= null;
		
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
        			  if(alive==true) {
        				  encontro= true;
        				  if( otrosJugadores.get(i).getAlive()) {
        				  otrosJugadores.get(i).setAlive(alive);
        				  otrosJugadores.get(i).setPosX(posX);
        				  otrosJugadores.get(i).setPosY(posy);
        				  otrosJugadores.get(i).setMass(getMass);
        				  otrosJugadores.get(i).updateRadious();
        				  otrosJugadores.get(i).updateArea();
        				  otrosJugadores.get(i).updateCenters();        				  
        				  }
        			  }else {
        				  otrosJugadores.remove(otrosJugadores.get(i));
        				  encontro=true;
        			  }
        		  }
        	  }
        	  if(!encontro && !nombre.equals(jugador.getName()) && alive==true) {
        		  Player nuevo= new Player(nombre);
        		  nuevo.setAlive(alive);
        		  nuevo.setPosX(posX);
        		  nuevo.setPosY(posy);
        		  nuevo.setMass(getMass);
        		  nuevo.updateRadious();
        		  nuevo.updateArea();
        		  nuevo.updateCenters();
        		  otrosJugadores.add(nuevo);
        		  nuevoPlayer= nuevo;
        	  }
          }
          
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(msg);
		}
		return nuevoPlayer;
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
		int contador=0;
		try {
	          StringTokenizer st = new StringTokenizer(msg, "#"); 
	          st.nextToken();
	          int comidaIn= Integer.parseInt(st.nextToken());
	          comida.removeAll(comida);
	          
	          for (int i = 0; i < comidaIn; i++) {
	        	  contador++;
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
			System.out.println(contador);
			e.printStackTrace();
		}
	}
	
	public String getConected() {
		return conected;
	}
	public void setConected(String conected) {
		this.conected = conected;
	}
	public String isInicia() {
		return Estado;
	}
	public void setInicia(String inicia) {
		this.Estado = inicia;
	}
	public void verificarSesion(String text, String contrasena,boolean ini) {
		boolean crear=ini;
		//this.inicia=true;
        try { 
        	String envio= text+ " " + contrasena + " "+crear;
        	salidaSSL.writeObject(envio);
        } catch (IOException e) {
        	//System.out.println("Socket cerrado, se habilita inicio de sesion");
            e.printStackTrace(); 
        } 
	}

	public ObjectOutputStream getSalidaSSL() {
		return salidaSSL;
	}
	public void setSalidaSSL(ObjectOutputStream salidaSSL) {
		this.salidaSSL = salidaSSL;
	}
	public SSLSocket getSslsocket() {
		return sslsocket;
	}
	public void setSslsocket(SSLSocket sslsocket) {
		this.sslsocket = sslsocket;
	}
	public void salaLlena() {
		setConected(Table.WAITING);
	}
	public void esperaUnJugador() {
		setConected(Table.DISABLE);
	}
	public void empiezaJuego() {
		setConected(Table.ENABLE);
	}
	public void cambiaAGanador(String ganador) {
		jugador.playerDies();
		//mandarInfo();
		try {
			if(s!=null) {
				s.close();
				s=null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		setGanador(ganador);
		setConected(Table.WINNER);
	}
	
	public String getGanador() {
		return Ganador;
	}
	public void setGanador(String ganador) {
		Ganador = ganador;
	}
	public void dejarEscucharSSL() {
		try {
			salidaSSL.writeObject("--salida");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getTipoCliente() {
		return tipoCliente;
	}
	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}
	public void actualizarDatosStreaming(String msg) {
		try {
			String[] arreglo= msg.split("/");
			String[] jugadores= arreglo[0].split("--");
			
			int jugardoresActu= Integer.parseInt(jugadores[0]);
			
			for (int i = 0; i < jugardoresActu; i++) {
				actualizarJugador(jugadores[i+1]);
			}
			actualizarComida(arreglo[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
