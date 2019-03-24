package hilos;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

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
            corres.actualizarJugador(msg);
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
		}
	}
}
