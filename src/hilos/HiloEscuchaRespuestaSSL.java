package hilos;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import conexion.Table;

public class HiloEscuchaRespuestaSSL implements Runnable{

	private Table tablero;
	
	 private ObjectInputStream entradaSSL;
	boolean sigue;
	public HiloEscuchaRespuestaSSL(Table t,ObjectInputStream entrada, ObjectOutputStream salida) {
		sigue= true;
		tablero= t;
		entradaSSL=entrada;
	}
	
	
	@Override
	public void run() {
		try {
		while(sigue) {
					String s = (String) entradaSSL.readObject();
					if(s.equals("--salida")) {
						terminar();
					}
					else {
					tablero.setInicia(s);
					}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void terminar() {
		try {
			sigue= false;
			entradaSSL.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
