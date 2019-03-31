package hilos;

import java.io.ObjectInputStream;

import conexion.Table;

public class HiloEscuchaRespuestaSSL implements Runnable{

	private Table tablero;
	
	 private ObjectInputStream entradaSSL;
	
	public HiloEscuchaRespuestaSSL(Table t,ObjectInputStream entrada) {
		tablero= t;
		entradaSSL=entrada;
	}
	
	
	@Override
	public void run() {
		try {
		while(true) {
			if(tablero.getConected()) {
					String s = (String) entradaSSL.readObject();
					tablero.setInicia(Boolean.parseBoolean(s));
				
			}
			else {
				entradaSSL.close();
				break;
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
