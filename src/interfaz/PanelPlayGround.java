package interfaz;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import Mundo.Ball;
import Mundo.Food;
import Mundo.Player;

public class PanelPlayGround extends JPanel implements   MouseMotionListener{
	
	private PrincipalFrame principal;
	
	public PanelPlayGround(PrincipalFrame p) {
		principal= p;
		addMouseMotionListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		ArrayList<Food> bolitas = principal.getComida();
		for (int i = 0; i < bolitas.size(); i++) {
			Food p = bolitas.get(i);
			g.setColor(p.getColor());
			g.fillOval(p.getPosX(), p.getPosY(), p.getRadious(), p.getRadious());
		}	
		Player nuevo= principal.getPrincipal();
		g.setColor(nuevo.getColor());
		g.fillOval(nuevo.getPosX(), nuevo.getPosY(), nuevo.getRadious(), nuevo.getRadious());
	}


	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		
//		int x= Math.abs(getX() - principal.getPrincipal().getPosX());
		int x=- 10;
		int y=-10;
//		int y=Math.abs(getY() - principal.getPrincipal().getPosY());
		principal.interaccion(x,y);
		System.out.println(arg0.getX());
		System.out.println(arg0.getY());
	}
}
