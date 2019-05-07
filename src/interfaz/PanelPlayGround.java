package interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Mundo.Ball;
import Mundo.Food;
import Mundo.Player;
import conexion.Table;
import hilos.HiloMovimientoMouse;

@SuppressWarnings("serial")
public class PanelPlayGround extends JPanel implements   MouseMotionListener{
	
	private PrincipalFrame principal;
	private boolean mostrarMensaje;
	public PanelPlayGround(PrincipalFrame p) {
		principal= p;
		mostrarMensaje= false;
	}
	public void iniciar() {
	    hilo= new HiloMovimientoMouse(principal, this, a.x, a.y);
		hilo.start();
		addMouseMotionListener(this);
	}
	public Point getA() {
		return a;
	}

	public void setA(Point a) {
		this.a = a;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if( mostrarMensaje) {
            AttributedString text = new AttributedString("Esperando otro jugador");
            text.addAttribute(TextAttribute.FONT, new Font("Arial", Font.BOLD, 24), 0, "Esperando otro jugador".length());
            text.addAttribute(TextAttribute.FOREGROUND, Color.RED, 0, "Esperando otro jugador".length());
			
            g.drawString(text.getIterator(),250,250); 
		}
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
		if(principal.getMundo().getTipoCliente().equals(Table.TYPE_PLAYER)) {
			
		g.setColor(nuevo.getColor());
		g.fillOval((int)nuevo.getPosX(), (int)nuevo.getPosY(), (nuevo.getRadious())*2, (nuevo.getRadious())*2);
		g.setColor(Color.black);
		g.drawString(nuevo.getName(), nuevo.getCenterH()-10, nuevo.getCenterK()+5);
		}
		
		ArrayList<Player> juga= principal.getJugadores();
		
		for (int i = 0; i < juga.size(); i++) {
			 nuevo= juga.get(i);
			 if( nuevo.getAlive()) {
			g.setColor(nuevo.getColor());
			g.fillOval((int)nuevo.getPosX(), (int)nuevo.getPosY(), (nuevo.getRadious())*2, (nuevo.getRadious())*2);
			g.setColor(Color.black);
			g.drawString(nuevo.getName(), nuevo.getCenterH()-10, nuevo.getCenterK()+5);
			 }
		}
	}


	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void disconnect(){
		try {
			hilo.wait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private HiloMovimientoMouse hilo;
	Point a= MouseInfo.getPointerInfo().getLocation();
	
	@Override
	public void mouseMoved(MouseEvent arg0) {
		a= MouseInfo.getPointerInfo().getLocation();
		//if( hilo !=null) {
			//hilo.stop();
		//}
		//hilo= new HiloMovimientoMouse(principal, arg0.getX(), arg0.getY());
		//hilo.start();
		//hilo.run();
		//int x=arg0.getX()-principal.getPrincipal().getCenterH();
		//int y=principal.getPrincipal().getCenterK()-arg0.getY();
//		int x= Math.abs(getX() - principal.getPrincipal().getPosX());
//		int y=Math.abs(getY() - principal.getPrincipal().getPosY());
		//principal.interaccion(x,y);
//		System.out.println(arg0.getX());
//		System.out.println(arg0.getY());
	}
	public boolean isMostrarMensaje() {
		return mostrarMensaje;
	}
	public void setMostrarMensaje(boolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}
}
