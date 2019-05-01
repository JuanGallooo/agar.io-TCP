package interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Mundo.Food;
import Mundo.Player;
import conexion.Table;
import hilos.HiloVerificarComer;
import conexion.*;

@SuppressWarnings("serial")
public class PrincipalFrame extends JFrame {

	

	private Table mundo;
	private PanelPlayGround miPanelPlay;
	private JPanel panelAux;
	private PanelIngreso miPanelIngreso;
	private PanelLogin miPanelLogin;

	public PrincipalFrame() {

		mundo = new Table();
		mundo.conectarSSL();

		setLayout(new BorderLayout());

		panelAux = new JPanel();
		panelAux.setLayout(new BorderLayout());

		miPanelLogin = new PanelLogin(this);

//		miPanelIngreso= new PanelIngreso(this);
		panelAux.add(miPanelLogin, BorderLayout.CENTER);

		add(panelAux, BorderLayout.CENTER);

		pack();
	}

	public void cambiarAIngreso() {
		panelAux.remove(0);
		miPanelIngreso = new PanelIngreso(this);
		panelAux.add(miPanelIngreso, BorderLayout.CENTER);
		repaint();
		pack();
	}

	public static void main(String[] args) {
		PrincipalFrame principal = new PrincipalFrame();
		principal.show();
		principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void cambiarAJuego(String nombre) {
		mundo.conectarAServidor();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if (mundo.getConected().equals(Table.WAITING)) {
					playerWaiting();
				} else if(mundo.getConected().equals(Table.ENABLE)) {
					iniciaJuego(nombre);
					iniciarVerificarComer();
				}
				else if(mundo.getConected().equals(Table.DISABLE)){
					iniciaJuego(nombre);
				}
			}
		}, 500);
	}
	private HiloVerificarComer nuevo;
	private void iniciarVerificarComer() {
		nuevo = new HiloVerificarComer(this);
		nuevo.start();
		pack();
	}
	private void iniciaJuego(String nombre) {
		panelAux.remove(0);
		miPanelPlay = new PanelPlayGround(this);
		miPanelPlay.setFocusable(true);
		miPanelPlay.iniciar();
		
		mundo.setNombreJugador(nombre);
		panelAux.add(miPanelPlay, BorderLayout.CENTER);
		setPreferredSize(new Dimension(600, 600));

	}
	public Cronometro cc;
	private void playerWaiting() {
		JFrame aux= new JFrame();
		cc= new Cronometro(this);
		aux.add(cc);
		aux.pack();
		aux.show();
	}

	public ArrayList<Food> getComida() {
		return mundo.getComida();
	}

	public Player getPrincipal() {
		return mundo.getJugador();
	}
	public Table getMundo() {
		return mundo;
	}

	public void interaccion(int x, int y) {
		if (mundo.getConected().equals(Table.ENABLE)) {
			miPanelPlay.setMostrarMensaje(false);
			if (getPrincipal().getAlive()) {
				mundo.Movimiento(x, y);
				miPanelPlay.revalidate();
				miPanelPlay.repaint();
			} else {
				mundo = new Table();
				miPanelPlay.disconnect();
				panelAux.remove(0);

				miPanelIngreso = new PanelIngreso(this);
				panelAux.add(miPanelIngreso, BorderLayout.CENTER);

				JOptionPane.showMessageDialog(this, "Has muerto", "Muerto", JOptionPane.INFORMATION_MESSAGE);
				pack();
			}
		}
		else if(mundo.getConected().equals(Table.DISABLE)) {
			miPanelPlay.setMostrarMensaje(true);
			if( nuevo==null) {
				iniciarVerificarComer();
			}
		}
		else if(mundo.getConected().equals(Table.WINNER)) {
			JOptionPane.showMessageDialog(this, "El ganador del juego ha sido el jugador :"+ mundo.getGanador()+" puntos en total acumulados.", "Ganadores", JOptionPane.INFORMATION_MESSAGE);
			
			mundo = new Table();
			miPanelPlay.disconnect();
			panelAux.remove(0);

			miPanelIngreso = new PanelIngreso(this);
			panelAux.add(miPanelIngreso, BorderLayout.CENTER);

			
			pack();
		}
	}

	public void verificarComer() {
		if (getPrincipal().getAlive() && mundo.getConected().equals(Table.ENABLE)) {
			mundo.toco();
			mundo.CollisionPlayers();
			miPanelPlay.revalidate();
			miPanelPlay.repaint();
		}
	}

	public void mostrarGanadores(String[] Ganadores) {
		JOptionPane.showMessageDialog(this, "Primer Lugar: " + Ganadores[0] + "\n" + "Segundo Lugar: " + Ganadores[1]
				+ "\n" + "Tercer Lugar: " + Ganadores[2], "Ganadores", JOptionPane.INFORMATION_MESSAGE);
	}

	public ArrayList<Player> getJugadores() {
		return mundo.getOtrosJugadores();
	}

	public void iniciarSesion(String text, char[] password) {
		String contrasena = String.valueOf(password);
		mundo.verificarSesion(text, contrasena, false);

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if (Boolean.parseBoolean(mundo.isInicia())) {
					inicioExitoso();
				} else
					usuarioIncorrecto();
			}
		}, 500);

	}

	public void registrarUsuario(String text, char[] password) {
		String contrasena = String.valueOf(password);

		mundo.verificarSesion(text, contrasena, true);
		inicioExitoso();
	}

	public void inicioExitoso() {
		JOptionPane.showMessageDialog(this, "Se ha iniciado sesion con exito", "Login",
				JOptionPane.INFORMATION_MESSAGE);
		cambiarAIngreso();
	}

	public void usuarioIncorrecto() {
		JOptionPane.showMessageDialog(this, "Contrase�a o usuario incorrecto, favor registrarse o correguir los campos",
				"Login", JOptionPane.INFORMATION_MESSAGE);
	}

	public void cambiarAStreaming() {
		//falta
		
	}
	

}
