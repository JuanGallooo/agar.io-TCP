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
import hilos.HiloEscuchaJugador;


public class Server {
	public static ArrayList<HiloEscuchaJugador> ar= new ArrayList<HiloEscuchaJugador>();
    /**
     * Property that represents the numbers of client that are conected.
     */
    static int i = 0;
    /**
     * The server has the arrayList of Food that represents the food that are in the table of every playe.
     */
    public static ArrayList<Food> comida;
    /**
     * Main of the class
     * @param args
     * @throws IOException
     */
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
            HiloEscuchaJugador mtch = new HiloEscuchaJugador(s,"Player " + i, dis, dos); 
  
            Thread t = new Thread(mtch); 
              
            System.out.println("Adding this client to active player list"); 

            ar.add(mtch); 
  
            t.start();
            
            broadCastingComida();
            
            i++; 
  
        }
    }
    /**
     * This method represents the sending of the information of the food in the table
     */
	public static void broadCastingComida() {
		try {
			for (HiloEscuchaJugador mc : Server.ar)  
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
