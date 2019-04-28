package conexion;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import Mundo.Food;
import Mundo.Player;
import hilos.HiloEscuchaJugador;
import hilos.HiloServidorSSL;


public class Server {
	
	
	public static ArrayList<HiloEscuchaJugador> ar= new ArrayList<HiloEscuchaJugador>();
    /**
     * Property that represents the numbers of client that are conected.
     */
    static int i = 0;
    /**
     * The server has the arrayList of Food that represents the food that are in the table of every playe.
     */
    public static ArrayList<Player> players;
    
    public static boolean reiniciar;
    
    public static ArrayList<Food> comida;
    /**
     * Main of the class
     * @param args
     * @throws IOException
     */
    @SuppressWarnings("resource")
	public static void main(String[] args) throws IOException  
    {
    	players= new ArrayList<Player>();
    	reiniciar= false;
    	Server server= new Server();
    	Timer timer= new Timer();
    	
    	TimerTask tarea= new TimerTask() {
			
			@Override
			public void run() {
				reiniciar=true;
				reiniciarJuego();				
			}
		};
		
		timer.scheduleAtFixedRate(tarea, 0, 15000);
    	
        ServerSocket ss = new ServerSocket(8000); 
        Socket s; 
        
		comida= new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Food nueva= new Food();
			comida.add(nueva);
		}
        
		System.out.println("Start the server of the SSL conection"); 
		
		
		HiloServidorSSL hiloSSL= new HiloServidorSSL();
		
        Thread t = new Thread(hiloSSL); 
        

        t.start(); 
		
        System.out.println("Start to wait for the clients"); 
		
        HiloEscuchaJugador espera=null;
        while (true)  
        { 
            s = ss.accept(); 
            System.out.println("New client request received : " + s); 
            DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
            DataInputStream dis = new DataInputStream(s.getInputStream()); 
            if(i<5) {   
            	if( i==0) {
            		dos.writeUTF("--wait");
            	}
            	if(i==1) {
            		ar.get(0).dos.writeUTF("--newPlayer");
            	}
        		System.out.println("Creating a new handler for this player..."); 
        		HiloEscuchaJugador mtch = new HiloEscuchaJugador(s,"Player " + i, dis, dos); 
        		Thread c = new Thread(mtch); 
        		System.out.println("Adding this client to active player list"); 
        		ar.add(mtch); 
        		c.start();
        		broadCastingComida();

            	i++; 
            }
            else {
            	dos.writeUTF("--disconnect");
            }
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
					mensajePelotas+="#"+comida.get(i).getColor().getRGB()+"#"+round(comida.get(i).getPosX())+"#"+round(comida.get(i).getPosY())+"#"+comida.get(i).getMass();
				}
				mc.getDos().writeUTF(mensajePelotas); 
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	public static double round(double entry) {
		return Math.round(entry * 1000000) / 1000000;
	}
	
	public static Player[] Ganadores() {
		Player[] jugadores= new Player[i];
		Arrays.sort(players.toArray(jugadores), java.util.Collections.reverseOrder());
		return jugadores;
	}
	
	public static void reiniciarJuego() {
		
	}

	public static boolean comprobarContra(String p) {
		String[] split= p.split(" ");
		boolean retorno= false;
		ArrayList<String> contras= new ArrayList<String>();	
		try {
			File archivo = new File ("./docs/usucontra.txt");
	        FileReader fr = new FileReader (archivo);
	        BufferedReader sr = new BufferedReader(fr);			
	        
	        String mensaje= sr.readLine();
	        
	        while (mensaje!=null && !mensaje.isEmpty() && !retorno) {
                contras.add(mensaje);
				String[] u= mensaje.split(" ");
				
				if (u[0].equals(split[0])&& u[1].equals(split[0])) retorno=true;
				mensaje= sr.readLine();
			}
	        sr.close();
		if(Boolean.parseBoolean(split[2])) {
    		FileWriter fichero = new FileWriter("./docs/usucontra.txt");
            PrintWriter pw = new PrintWriter(fichero);
			for (int i = 0; i < contras.size(); i++) {
            pw.println(contras.get(i));
            }
            
            pw.println(split[0]+" "+split[1]);
            pw.close();
            retorno= true;
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	 
}
