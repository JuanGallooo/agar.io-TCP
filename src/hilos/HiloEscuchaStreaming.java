package hilos;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import conexion.Server;
import conexion.Table;

public class HiloEscuchaStreaming implements Runnable{
	private boolean escuchar;
	public Table corres;
	public HiloEscuchaStreaming(Table table) {
		escuchar= true;
		corres= table;
	}
	@Override
	public void run() {
			try {
				//byte[] buffer = new byte[5000];
				MulticastSocket socket = new MulticastSocket(Table.STREAMING_PORT);
				
				InetAddress group = InetAddress.getByName(Server.direccionMulticast);
				System.out.println(group);
				
				socket.joinGroup(group);
				
				while (escuchar) {
					
					byte buf[]= new byte[3000];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					
					socket.receive(packet);
					
					
					String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
					
					corres.actualizarDatosStreaming(msg);
					
					if ("OK".equals(msg)) {
						System.out.println("No more message. Exiting : " + msg);
						escuchar= false;
					}
		            Thread.sleep(10*10);
				}
				socket.leaveGroup(group);
				socket.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	public void dejarEscuchar() {
		escuchar= false;
	}
}
