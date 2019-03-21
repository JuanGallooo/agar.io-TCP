package interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;

import Mundo.Food;
import Mundo.Player;
import Mundo.Table;

@SuppressWarnings("serial")
public class PrincipalFrame extends JFrame{
	
	private Table mundo;
	private PanelPlayGround miPanel;

	public PrincipalFrame(){
		mundo= new Table();
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(600,600));
		miPanel= new PanelPlayGround(this);
		miPanel.setFocusable(true);
		add(miPanel, BorderLayout.CENTER);
		pack();
	}
	public static void main(String[] args) {
		PrincipalFrame principal= new PrincipalFrame();
		principal.show();
		principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public ArrayList<Food> getComida() {
		return mundo.getComida();
	}

	public Player getPrincipal() {
		return mundo.getJugador();
	}

	public void interaccion(int x, int y) {
		mundo.Movimiento(x, y);
		miPanel.revalidate();
		miPanel.repaint();
	}

}
