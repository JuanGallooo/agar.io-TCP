package hilos;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.StringTokenizer;

import Mundo.Player;
import conexion.Server;
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
			if(corres.s==null) {
				break;
			}else {
				try {
					if(!corres.s.isClosed()) {
					String msg = dos.readUTF();
					
					StringTokenizer st = new StringTokenizer(msg, "#"); 
					String tipo= st.nextToken();
					if(tipo.equals("&")) {
						corres.actualizarJugador(msg);
					}
					else if( tipo.equals("@")) {
						corres.actualizarComida(msg);
					}
					else if( tipo.equals("--disconnect")) {
						corres.salaLlena();
					}
					else if( tipo.equals("--wait")) {
						corres.esperaUnJugador();
					}
					else if(tipo.equals("--newPlayer")) {
						corres.empiezaJuego();
					}
					else if(tipo.equals("--ganador")) {
						corres.cambiaAGanador(st.nextToken());
					}
					Thread.sleep(10*5);
					}
					} catch (Exception e) { 
					break;
				    } 				
			}
		}
	}
}
