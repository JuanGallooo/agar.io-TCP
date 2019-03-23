package hilos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import conexion.Server;

public class HiloJugadorMovimiento extends Thread{
    private String name; 
    final DataInputStream dis; 
    final DataOutputStream dos; 
    Socket s; 
    boolean isloggedin; 
	
	public HiloJugadorMovimiento(Socket s, String name, 
                            DataInputStream dis, DataOutputStream dos) {
        this.dis = dis; 
        this.dos = dos; 
        this.name = name; 
        this.s = s; 
        this.isloggedin=true; 
	}
	@Override
    public void run() { 
    	  
        String received=""; 
        while (true)  
        { 
            try
            { 
            		received = dis.readUTF(); 
                    if(received.equals("logout")){ 
                        this.isloggedin=false; 
                        this.s.close(); 
                        break; 
                    } 
                    StringTokenizer st = new StringTokenizer(received, "#"); 
                    int x= Integer.parseInt(st.nextToken()); 
                    int y= Integer.parseInt(st.nextToken());
      
                    for (HiloJugadorMovimiento mc : Server.ar)  
                    { 
                    	mc.dos.writeUTF(this.name+ " "+ x+ " "+y); 
                    } 
            		
            } catch (IOException e) { 
                  
                e.printStackTrace(); 
            } 
              
        } 
        try
        { 
            this.dis.close(); 
            this.dos.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
}
