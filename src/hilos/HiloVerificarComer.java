package hilos;

import interfaz.PrincipalFrame;
/**
 * The thread that it is used for the player to verificated that are eat some food
 * @author Juan Esteban Gallo
 *
 */
public class HiloVerificarComer extends Thread{
	/**
	 * The principal frame
	 */
	private PrincipalFrame principal;
	/**
	 * The constructor of the class HiloVerificarComer
	 * @param p the principal frame
	 */
	public HiloVerificarComer(PrincipalFrame p) {
		principal= p;
	}
	/**
	 * The run of the class
	 */
	@Override
	public void run(){
            while (true) { 
                try { 
            		principal.verificarComer();
            		sleep(30*5);
                } catch (Exception e) { 
                    e.printStackTrace(); 
                } 
            } 
	}
}
