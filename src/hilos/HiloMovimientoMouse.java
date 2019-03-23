package hilos;

import interfaz.PrincipalFrame;

public class HiloMovimientoMouse extends Thread{
	private PrincipalFrame principal;
	private int x;
	private int y;
	
	public HiloMovimientoMouse(PrincipalFrame p, int xd, int yd) {
		principal= p;
		x=xd;
		y= yd;
	}
	@Override
	public void run(){
            while (true) { 
                try { 
                	//if(x==principal.getPrincipal().getPosX()&& y==principal.getPrincipal().getPosY()) break; 
            		int xde=x-principal.getPrincipal().getCenterH();
            		int yde=principal.getPrincipal().getCenterK()-y;
            		principal.interaccion(xde,yde);
            		sleep(5);
                } catch (Exception e) { 

                    e.printStackTrace(); 
                } 
            } 
	}

}
