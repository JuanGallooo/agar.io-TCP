package hilos;

import java.io.ObjectInputStream;

import Mundo.Player;
import conexion.Table;

public class HiloActualizarJugadores extends Thread{
	
	private ObjectInputStream dos;
	private Table corres;
	
	public HiloActualizarJugadores(ObjectInputStream d, Table t) {
		dos=d;
	}
	@Override
	public void run() {
		while (true) {
			try {
            Player msg =(Player) dos.readObject();
            corres.actualizarJugador(msg);
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
		}
	}
}
