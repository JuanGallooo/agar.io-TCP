package hilos;

import interfaz.PrincipalFrame;

public class HiloVerificarComer extends Thread{

	private PrincipalFrame principal;
	
	public HiloVerificarComer(PrincipalFrame p) {
		principal= p;
	}
	
	@Override
	public void run(){
            while (true) { 
                try { 
            		principal.verificarComer();
            		sleep(100);
                } catch (Exception e) { 
                    e.printStackTrace(); 
                } 
            } 
	}
}
