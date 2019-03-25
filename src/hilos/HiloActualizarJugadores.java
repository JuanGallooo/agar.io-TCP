package hilos;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.StringTokenizer;

import Mundo.Player;
import conexion.Table;
/**
 * Thread that is use to refresh the player by the server in every moment
 *
 */
public class HiloActualizarJugadores implements Runnable{
	/**
	 * The flush of the data that becomes from the information of the server
	 */
	private DataInputStream dos;
	/**
	 * The table that are referents 
	 */
	private Table corres;
	/**
	 * Constructor of the class
	 * @param t the table
	 */
	public HiloActualizarJugadores(Table t) {
		try {
			dos=t.getDis();
		} catch (Exception e) {
			e.printStackTrace();
		}
		corres=t;
	}
	/**
	 * Method run of the thread 
	 */
	@Override
	public void run() {
		while (true) {
			try {
            String msg = dos.readUTF();
            StringTokenizer st = new StringTokenizer(msg, "#"); 
            String tipo= st.nextToken();
            if(tipo.equals("&")) {
            	corres.actualizarJugador(msg);
            }
            else if( tipo.equals("@")) {
            	corres.actualizarComida(msg);
            }
            //Thread.sleep(30);
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
		}
	}
}
