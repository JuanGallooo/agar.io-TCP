package hilos;

import conexion.Server;

public class HiloEnviaStreamers implements Runnable{

	private boolean enviarDatos;
	
	public HiloEnviaStreamers() {
		enviarDatos= true;
	}
	
	@Override
	public void run() {
		while (enviarDatos) {
			try {
			Server.mandarInfoStreamers();
            Thread.sleep(10*30);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void noEnviar() {
		enviarDatos=false;
	}
}
