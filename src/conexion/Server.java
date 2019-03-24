package conexion;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Mundo.Food;
import hilos.HiloJugadorMovimiento;


public class Server {
	public static ArrayList<HiloJugadorMovimiento> ar= new ArrayList<HiloJugadorMovimiento>();
    
    static int i = 0; 
  
    public static ArrayList<Food> comida;
    
    @SuppressWarnings("resource")
	public static void main(String[] args) throws IOException  
    { 
        ServerSocket ss = new ServerSocket(8000); 
        Socket s; 
        
		comida= new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Food nueva= new Food();
			comida.add(nueva);
		}
        
        while (true)  
        { 
            s = ss.accept(); 
  
            System.out.println("New client request received : " + s); 
              
            DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
            DataInputStream dis = new DataInputStream(s.getInputStream()); 
              
            System.out.println("Creating a new handler for this player..."); 
            HiloJugadorMovimiento mtch = new HiloJugadorMovimiento(s,"Player " + i, dis, dos); 
  
            Thread t = new Thread(mtch); 
              
            System.out.println("Adding this client to active player list"); 

            ar.add(mtch); 
  
            t.start();
            
            broadCastingComida();
            
            i++; 
  
        }
    }

	public static void broadCastingComida() {
		try {
			for (HiloJugadorMovimiento mc : Server.ar)  
			{ 
				String mensajePelotas="@";
				mensajePelotas+="#"+comida.size();
				for (int i = 0; i < comida.size(); i++) {
					mensajePelotas+="#"+comida.get(i).getColor().getRGB()+"#"+comida.get(i).getPosX()+"#"+comida.get(i).getPosY()+"#"+comida.get(i).getMass();
				}
				mc.getDos().writeUTF(mensajePelotas); 
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}
