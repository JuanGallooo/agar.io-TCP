package hilos;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.StringTokenizer;

import Mundo.Player;
import conexion.Table;

public class HiloActualizarJugadores implements Runnable{
	
	private DataInputStream dos;
	private Table corres;
	
	public HiloActualizarJugadores(Table t) {
		try {
			dos=t.getDis();
		} catch (Exception e) {
			e.printStackTrace();
		}
		corres=t;
	}
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
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
		}
	}
}
