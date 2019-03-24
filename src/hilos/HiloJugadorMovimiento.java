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

public class HiloJugadorMovimiento implements Runnable{
    private String name; 
    final DataInputStream dis;
    final DataOutputStream dos; 
    Socket s; 
    boolean isloggedin; 
	
    public HiloJugadorMovimiento(Socket s, String name,DataInputStream dis, DataOutputStream dos) {
        this.dis = dis; 
        this.dos = dos; 
        this.name = name; 
        this.s = s; 
        this.isloggedin=true; 
	}
    public void run() { 
        while (true)  
        { 
            try
            { 
            	String player=dis.readUTF();
    	            StringTokenizer st = new StringTokenizer(player, "#"); 
    	            String tipo= st.nextToken();
    	            
    	            if(tipo.equals("&")) {
                        for (HiloJugadorMovimiento mc : Server.ar)  
                        { 
                        	mc.dos.writeUTF(player); 
                        } 
    	            }
    	            else if( tipo.equals("@")) {
    		          String color= st.nextToken();
    		          Color c= Color.decode(color);
    		          double x= Double.parseDouble(st.nextToken());
    		          double y= Double.parseDouble(st.nextToken());
    		          int mass= Integer.parseInt(st.nextToken());
    	            	ArrayList<Food> comidaServer=Server.comida;
    	            	for (int i = 0; i < comidaServer.size(); i++) {
							if( comidaServer.get(i).getPosX()==x && comidaServer.get(i).getPosY()==y) {
								Server.comida.remove(i);
								break;
							}
						}
    	            	Server.broadCastingComida();
    	            }
            } catch (Exception e) { 
                  
                e.printStackTrace(); 
            } 
        }
    }
	public DataOutputStream getDos() {
		return dos;
	} 
    

}
