package hilos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

import Mundo.Player;
import conexion.Server;

public class HiloJugadorMovimiento extends Thread{
    private String name; 
    final ObjectInputStream dis;
    final ObjectOutputStream dos; 
    Socket s; 
    boolean isloggedin; 
	
	public HiloJugadorMovimiento(Socket s, String name,ObjectInputStream dis, ObjectOutputStream dos) {
        this.dis = dis; 
        this.dos = dos; 
        this.name = name; 
        this.s = s; 
        this.isloggedin=true; 
	}
	@Override
    public void run() { 
        while (true)  
        { 
            try
            { 
            	Object h=(Player) dis.readObject();
            	
//            		received = dis.readUTF(); 
//                    if(received.equals("logout")){ 
//                        this.isloggedin=false; 
//                        this.s.close(); 
//                        break; 
//                    } 
//                    StringTokenizer st = new StringTokenizer(received, "#"); 
//                    int x= Integer.parseInt(st.nextToken()); 
//                    int y= Integer.parseInt(st.nextToken());
//      
                    for (HiloJugadorMovimiento mc : Server.ar)  
                    { 
                    	mc.dos.writeObject(h); 
                    } 
            		
            } catch (Exception e) { 
                  
                e.printStackTrace(); 
            } 
        }
    } 

}
