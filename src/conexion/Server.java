package conexion;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import hilos.HiloJugadorMovimiento;


public class Server {
	public static ArrayList<HiloJugadorMovimiento> ar= new ArrayList<HiloJugadorMovimiento>();
    
    // counter for clients 
    static int i = 0; 
  
    public static PlayGround juego;
    
    @SuppressWarnings("resource")
	public static void main(String[] args) throws IOException  
    { 
        // server is listening on port 1234 
        ServerSocket ss = new ServerSocket(8000); 
        
        Socket s; 
        juego= new PlayGround();
        // running infinite loop for getting 
        // client request 
        while (true)  
        { 
            // Accept the incoming request 
            s = ss.accept(); 
  
            System.out.println("New client request received : " + s); 
              
            // obtain input and output streams 
            DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
            DataInputStream dis = new DataInputStream(s.getInputStream()); 
              
            System.out.println("Creating a new handler for this player..."); 
  
            // Create a new handler object for handling this request. 
            HiloJugadorMovimiento mtch = new HiloJugadorMovimiento(s,"Player " + i, dis, dos); 
  
            // Create a new Thread with this object. 
            Thread t = new Thread(mtch); 
              
            System.out.println("Adding this client to active player list"); 
  
            // add this client to active clients list 
            ar.add(mtch); 
  
            // start the thread. 
            t.start(); 
  
            // increment i for new client. 
            // i is used for naming only, and can be replaced 
            // by any naming scheme 
            i++; 
  
        }
    } 
}
