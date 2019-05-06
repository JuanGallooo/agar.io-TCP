package conexion;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


import Mundo.Food;
import Mundo.Player;
import hilos.HiloAudioUDPServer;
import hilos.HiloEscuchaJugador;
import hilos.HiloServidorSSL;
import hilos.audioHelp;
import javafx.util.Pair;


public class Server {
	
	
	public static ArrayList<HiloEscuchaJugador> ar= new ArrayList<HiloEscuchaJugador>();
    /**
     * Property that represents the numbers of client that are conected.
     */
    static int i = 0;
    /**
     * The server has the arrayList of Food that represents the food that are in the table of every playe.
     */
    
    public static audioHelp help;

    public static ArrayList<Food> comida;
    
    public static ArrayList<Pair<String, Integer>> players;
    
   
    
    private static HiloAudioUDPServer hiloAudioServer;
    
    public static  void crearHiloMusica() {
    	help=new audioHelp ();
    	
    	hiloAudioServer = new HiloAudioUDPServer(help.DIRECCION_MULTICAST,help.PORT_AUDIO);
	    hiloAudioServer.start();
    	
    }
    
    /**
     * Main of the class
     * @param args
     * @throws IOException
     */
    @SuppressWarnings("resource")
	public static void main(String[] args) throws IOException  
    {
    	crearHiloMusica();
		 
        ServerSocket ss = new ServerSocket(8000); 
        Socket s; 
		comida= new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Food nueva= new Food();
			comida.add(nueva);
		}
		players= new ArrayList<Pair<String, Integer>>();
		System.out.println("Start the server of the SSL conection"); 
		HiloServidorSSL hiloSSL= new HiloServidorSSL();
        Thread t = new Thread(hiloSSL); 
        t.start(); 
        System.out.println("Start to wait for the clients"); 
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
            		new Timer().schedule(new TimerTask() {
            			@Override
            			public void run() {
            				reportarGanadores();
            				System.out.println(true);
            			}
            		}, 10000);
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
    
    protected static void reportarGanadores() {
    	try {
    		String envio= getGanador();
    		for (int i = 0; i < ar.size(); i++) {
    			ar.get(i).getDos().writeUTF("--ganador#"+envio);
    		}
    		ar= new ArrayList<HiloEscuchaJugador>();
    		comida= new ArrayList<>();
    		for (int i = 0; i < 100; i++) {
    			Food nueva= new Food();
    			comida.add(nueva);
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getGanador() {
		String retorno="";
		int mayor=Integer.MIN_VALUE;
		for (int i = 0; i <players.size(); i++) {
			if(players.get(i).getValue()>mayor) {
				retorno= players.get(i).getKey()+" "+players.get(i).getValue(); 
				mayor= players.get(i).getValue();
			}
		}
		return retorno;
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
	

	public static void actualizarJugador(String name, int mass) {
		for (int i = 0; i < players.size(); i++) {
			if(players.get(i).getKey().equals(name)&& players.get(i).getValue()!=mass) {
				players.set(i, new Pair<String, Integer>(name, mass));
				break;
			}
		}
	}
	
	 
}
