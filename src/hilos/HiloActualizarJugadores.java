package hilos;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import Mundo.Player;
import conexion.Table;

public class HiloActualizarJugadores extends Thread{
	
	private ObjectInputStream dos;
	private Table corres;
	
	public HiloActualizarJugadores(InputStream newe , Table t) {
		try {
			dos=new ObjectInputStream(newe);
		} catch (IOException e) {
			e.printStackTrace();
		}
		corres=t;
	}
	@Override
	public void run() {
		while (true) {
			try {
            Player msg =(Player) dos.readObject();
            System.out.println("recibo jugador" + msg.getName());
            corres.actualizarJugador(msg);
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
		}
	}
}
