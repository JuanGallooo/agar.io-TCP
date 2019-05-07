package hilos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import chat.ClientHandler;

public class hiloServerChat implements Runnable{
	
	public static ArrayList<ClientHandler> ar= new ArrayList<ClientHandler>();
	int i;
	@Override
	public void run() {
		try {
			 ServerSocket ss = new ServerSocket(2000); 
		        Socket s; 
		        while (true)  
		        { 
		            s = ss.accept(); 
		  
		            System.out.println("New client request received : " + s); 
		              
		            DataInputStream dis = new DataInputStream(s.getInputStream()); 
		            DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
		              
		            System.out.println("Creating a new handler for this client..."); 
		  
		            ClientHandler mtch = new ClientHandler(s,"client " + i, dis, dos); 
		  
		            Thread t = new Thread(mtch); 
		              
		            System.out.println("Adding this client to active client list"); 
		  
		            ar.add(mtch); 
		  
		            t.start(); 
		  
		            i++; 
		  
		        } 
		} catch (Exception e) {
			e.printStackTrace();
		}
       
	}

}
