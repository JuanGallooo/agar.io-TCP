package interfaz;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import Mundo.Ball;
import Mundo.Food;
import Mundo.Player;
import hilos.HiloMovimientoMouse;

@SuppressWarnings("serial")
public class PanelPlayGround extends JPanel implements   MouseMotionListener{
	
	private PrincipalFrame principal;
	
	public PanelPlayGround(PrincipalFrame p) {
		principal= p;
		addMouseMotionListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		ArrayList<Food> bolitas = new ArrayList<>();
		bolitas.addAll(principal.getComida());
		for (int i = 0; i < bolitas.size(); i++) {
			if (i<bolitas.size()) {
				Food p = bolitas.get(i);
			if(p !=null) {
			g.setColor(p.getColor());
			g.fillOval((int)p.getPosX(), (int)p.getPosY(), p.getRadious()*2, p.getRadious()*2);
			}
			}
		}	
		Player nuevo= principal.getPrincipal();
		g.setColor(nuevo.getColor());
		g.fillOval((int)nuevo.getPosX(), (int)nuevo.getPosY(), (nuevo.getRadious())*2, (nuevo.getRadious())*2);
		g.setColor(Color.black);
		g.drawString(nuevo.getName(), nuevo.getCenterH()-10, nuevo.getCenterK()+5);
		ArrayList<Player> juga= principal.getJugadores();
		
		for (int i = 0; i < juga.size(); i++) {
			 nuevo= juga.get(i);
			g.setColor(nuevo.getColor());
			g.fillOval((int)nuevo.getPosX(), (int)nuevo.getPosY(), (nuevo.getRadious())*2, (nuevo.getRadious())*2);
			g.setColor(Color.black);
			g.drawString(nuevo.getName(), nuevo.getCenterH()-10, nuevo.getCenterK()+5);
		}
	}


	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private HiloMovimientoMouse hilo;
	@Override
	public void mouseMoved(MouseEvent arg0) {
		if( hilo !=null) {
			hilo.stop();
		}
		hilo= new HiloMovimientoMouse(principal, arg0.getX(), arg0.getY());
		hilo.start();
		
		//hilo.run();
		//int x=arg0.getX()-principal.getPrincipal().getCenterH();
		//int y=principal.getPrincipal().getCenterK()-arg0.getY();
//		int x= Math.abs(getX() - principal.getPrincipal().getPosX());
//		int y=Math.abs(getY() - principal.getPrincipal().getPosY());
		//principal.interaccion(x,y);
//		System.out.println(arg0.getX());
//		System.out.println(arg0.getY());
	}
}
