package hilos;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import Mundo.Food;
import Mundo.Player;
import conexion.Server;
import javafx.util.Pair;
/**
 * Thread that is use to refresh the information of the players and the table referenced to the food, this is communicated whit the server. 
 *
 */
public class HiloEscuchaJugador implements Runnable{
	/**
	 * The name of the client
	 */
    private String name; 
    /**
     * The flush of data that becomes of the server
     */
    public DataInputStream dis; 
    /**
     * The flush of data that we use to send information relevant to the server.
     */
    public DataOutputStream dos; 
	/**
	 * This represents the socket of the table, is the media to connect to the server
	 */
    public Socket s;
    /**
     * Boolean if is logined the players in the server.
     */
    boolean isloggedin; 
    	
    public HiloEscuchaJugador(Socket s, String name,DataInputStream dis, DataOutputStream dos) {
        this.dis = dis; 
        this.dos = dos; 
        this.name = name; 
        this.s = s; 
        this.isloggedin=true; 
	}
    /**
     * Run of the thread
     */
    public void run() { 
        while (true)  
        {        	
           	if(isloggedin!= false && !s.isClosed()) {  		       	
            try
            { 
            	    String player=dis.readUTF();
    	            StringTokenizer st = new StringTokenizer(player, "#"); 
    	            String tipo= st.nextToken();
    	            
    	            
    	            if(tipo.equals("&")) {
                        for (HiloEscuchaJugador mc : Server.ar)  
                        { 
                        	if(!mc.equals(this)) {
                        		mc.dos.writeUTF(player); 
                        	}
                        }                 
                        
                        String name= st.nextToken();
                        boolean alive= Boolean.parseBoolean(st.nextToken());
                        st.nextToken();
                        st.nextToken();
                        int mass= Integer.parseInt(st.nextToken());
                        
                        actualizarJugadoresWinner(name,mass);
                        actualizarJugadoresInfo(name, player);
                        if(alive== false) {
                        	this.isloggedin= false;
                        	this.s.close();
                        	Server.ar.remove(this);
                        	break;
                        }
    	            }
    	            else if( tipo.equals("@")) {
    		          String color= st.nextToken();
    		        Color c= Color.decode(color);
    		          double x= Double.parseDouble(st.nextToken());
    		          double y= Double.parseDouble(st.nextToken());
    		         // int mass= Integer.parseInt(st.nextToken());
    	            	ArrayList<Food> comidaServer=Server.comida;
    	            	for (int i = 0; i < comidaServer.size(); i++) {
							if( comidaServer.get(i).getPosX()==x && comidaServer.get(i).getPosY()==y) {
								Server.comida.remove(i);
								break;
							}
						}
    	            	Server.broadCastingComida();
    	            }
    	            Thread.sleep(10*5);
            } catch (Exception e) { 
                  
                e.printStackTrace(); 
            } 
        	}else {
        		break;
        	}
        }
    }
    
    /**
     * Method get of the DataOutputStream
     * @return dos
     */
	public DataOutputStream getDos() {
		return dos;
	}
	public void disconect() {
		try {
	    	this.isloggedin= false;
	    	this.s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	public static void actualizarAStreamer() {
		
	}
	public static void actualizarJugadoresWinner(String name, int mass) {
		boolean encontro= false;
		for (int i = 0; i < Server.playersForWinner.size()&& !encontro; i++) {
			if(Server.playersForWinner.get(i).getKey().equals(name)&& Server.playersForWinner.get(i).getValue()!=mass) {
				Server.playersForWinner.set(i, new Pair<String, Integer>(name, mass));
				encontro= true;
			}
		}
		if( encontro==false) {
			Server.playersForWinner.add(new Pair<String, Integer>(name, mass));
		}
	}
	public static void actualizarJugadoresInfo(String name, String info) {
		boolean encontro= false;
		for (int i = 0; i < Server.playersInfo.size()&& !encontro; i++) {
			if(Server.playersInfo.get(i).getKey().equals(name)&& Server.playersInfo.get(i).getValue()!=info) {
				Server.playersInfo.set(i, new Pair<String, String>(name, info));
				encontro= true;
			}
		}
		if( encontro==false) {
			Server.playersInfo.add(new Pair<String, String>(name, info));
		}
	}
	
}
