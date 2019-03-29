package interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Mundo.Food;
import Mundo.Player;
import conexion.Table;
import hilos.HiloVerificarComer;

@SuppressWarnings("serial")
public class PrincipalFrame extends JFrame{
	
	private Table mundo;
	private PanelPlayGround miPanelPlay;
	private JPanel panelAux;
	private PanelIngreso miPanelIngreso;

	public PrincipalFrame(){
		mundo= new Table();
		
		setLayout(new BorderLayout());
		
		panelAux= new JPanel();
		panelAux.setLayout(new BorderLayout());
		
		miPanelIngreso= new PanelIngreso(this);
		panelAux.add(miPanelIngreso,BorderLayout.CENTER);

		add(panelAux, BorderLayout.CENTER);
		
		pack();
	}
	public static void main(String[] args) {
		PrincipalFrame principal= new PrincipalFrame();
		principal.show();
		principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void cambiarAJuego(String nombre) {
		panelAux.remove(0);
		miPanelPlay= new PanelPlayGround(this);
		miPanelPlay.setFocusable(true);
		mundo.setNombreJugador(nombre);
		mundo.conectarAServidor();
		panelAux.add(miPanelPlay, BorderLayout.CENTER);
		setPreferredSize(new Dimension(600,600));
		HiloVerificarComer nuevo= new HiloVerificarComer(this);
		nuevo.start();
		pack();
	}
	public ArrayList<Food> getComida() {
		return mundo.getComida();
	}

	public Player getPrincipal() {
		return mundo.getJugador();
	}

	public void interaccion(int x, int y) {
	    if( mundo.getConected()) {
		if(getPrincipal().getAlive()) {
			
		mundo.Movimiento(x, y);
		miPanelPlay.revalidate();
		miPanelPlay.repaint();
		}
		else {
			JOptionPane.showMessageDialog(this, "Has muerto","Muerto",JOptionPane.INFORMATION_MESSAGE);
			mundo= new Table();
			miPanelPlay.disconnect();
			panelAux.remove(0);
			panelAux.add(miPanelIngreso,BorderLayout.CENTER);
			pack();
		}
	    }
	}
	public void verificarComer() {
		if(getPrincipal().getAlive() && mundo.getConected()) {
		mundo.toco();
		mundo.CollisionPlayers();
		miPanelPlay.revalidate();
		miPanelPlay.repaint();
		}
	}
	public ArrayList<Player> getJugadores() {
		return mundo.getOtrosJugadores();
	}

}
