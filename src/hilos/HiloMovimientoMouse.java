package hilos;

import interfaz.PanelPlayGround;
import interfaz.PrincipalFrame;
/**
 * Thread to follow the mouse everymoment
 * @author Juan Esteban Gallo
 *
 */
public class HiloMovimientoMouse extends Thread{
	/**
	 * The frame that becomes 
	 */
	private PrincipalFrame principal;
	/**
	 * The int x that need to follow
	 */
	private int x;
	/**
	 * The int y that need to follow
	 */
	private int y;
	PanelPlayGround e;
	
	/**
	 * The constructor of the thread
	 * @param p the frame
	 * @param xd the x that follow
	 * @param yd the y that follow
	 */
	public HiloMovimientoMouse(PrincipalFrame p, PanelPlayGround e, int xd, int yd) {
		principal= p;
		x=xd;
		y= yd;
		this.e= e;
	}
	/**
	 * The run of the thread
	 */
	@Override
	public void run(){
            while (true) { 
                try { 
                	
                	//if(x==principal.getPrincipal().getPosX()&& y==principal.getPrincipal().getPosY()) break; 
            		int xde=e.getA().x-principal.getPrincipal().getCenterH();
            		int yde=principal.getPrincipal().getCenterK()-e.getA().y;
            		principal.interaccion(xde,yde);
            		sleep(15*5);
                } catch (Exception e) { 

                    e.printStackTrace(); 
                } 
            } 
	}

}
