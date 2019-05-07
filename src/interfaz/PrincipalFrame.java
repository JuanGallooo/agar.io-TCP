package interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Mundo.Food;
import Mundo.Player;
import chat.Principal;
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
	private Principal miPanelChat;

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
	
	final static int ServerPort = 2000; 
	  
	public InetAddress ip; 
      
    public Socket s; 
      
    public DataInputStream dis; 
    public DataOutputStream dos; 

	public void iniciarChat() {
		try {
            ip= InetAddress.getByName("localhost");
            s = new Socket(ip, ServerPort);
            
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
              
            Thread readMessage = new Thread(new Runnable()  
            { 
                public void run() { 
      
                    while (true) { 
                        try { 
                            String msg = dis.readUTF(); 
                            miPanelChat.agregarMensaje(msg);
                        } catch (IOException e) { 
      
                            e.printStackTrace(); 
                        } 
                    } 
                } 
            }); 
            readMessage.start(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
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
					// esperar con cronometro
					playerWaiting();
				} else if(mundo.getConected().equals(Table.ENABLE)) {
					//inicia juego
					iniciaJuego(nombre,false);
					iniciarVerificarComer();
				}
				else if(mundo.getConected().equals(Table.DISABLE)){
					//conecta pero espera otro jugador
					iniciaJuego(nombre,false);
				}
			}
		}, 500);
	}

	public void cambiarAStreaming(String string) {
		iniciarChat();
		miPanelChat= new Principal();
		miPanelChat.asignarMundo(this);

		
		mundo.conectarAStreaming();
		iniciaStreaming(string, true);
		iniciarVerificarComer();
	}

	private HiloVerificarComer nuevo;
	
	private void iniciarVerificarComer() {
		nuevo = new HiloVerificarComer(this);
		nuevo.start();
		pack();
	}
	private void iniciaStreaming(String nombre, boolean streamer) {
		panelAux.remove(0);
		miPanelPlay = new PanelPlayGround(this);
		miPanelPlay.setFocusable(true);
		if(!streamer)miPanelPlay.iniciar();
		mundo.setNombreJugador(nombre);
		panelAux.add(miPanelPlay, BorderLayout.CENTER);
		JFrame nada= new JFrame();
		nada.setLayout(new BorderLayout());
		nada.add(miPanelChat,BorderLayout.CENTER);
		nada.setVisible(true);
		nada.setLocationRelativeTo(null);
		nada.pack();
		
		setPreferredSize(new Dimension(600, 600));
	}
	private void iniciaJuego(String nombre, boolean streamer) {
		panelAux.remove(0);
		miPanelPlay = new PanelPlayGround(this);
		miPanelPlay.setFocusable(true);
		if(!streamer)miPanelPlay.iniciar();
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
		if(mundo.getTipoCliente().equals(Table.TYPE_STREAMER)) {
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
					mundo.dejarEscucharSSL();
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

	public void sendMessage(String string) {
        try { 
            dos.writeUTF(string); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
	}


}
