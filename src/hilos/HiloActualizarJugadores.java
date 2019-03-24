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
            System.out.println("recibo jugador" + msg.getName());
            corres.actualizarJugador(msg);
            sleep(10);
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
		}
	}
}
