package hilos;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import conexion.Table;

public class HiloAudioUDPClient extends Thread{
	
	   
	    private SourceDataLine sLine;
	    private AudioFormat audioFormat;
	    byte[] buffer=new byte[4096];
	    DatagramPacket packet;
	    private Table cliente;

	    public HiloAudioUDPClient (Table cliente) {
	        
	        this.cliente=cliente;
	        init();
	        Thread t1=new Thread(new Reader());
	        t1.start();
	    }

	    public void init() {
	        audioFormat = new AudioFormat(44100, 16, 2, true, false);
	        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

	        try  {
	            System.out.println(info);
	            sLine=(SourceDataLine) AudioSystem.getLine(info);
	            System.out.println(sLine.getLineInfo() + " - sample rate : "+audioFormat.getSampleRate());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }       
	    }

	    public void run() {
	        System.out.println("Client started");
	        try {
	            sLine.open(audioFormat);
	        } catch (Exception e){
	            e.printStackTrace();
	        }
	        sLine.start();
	        System.out.println("Line started");

	        try {
				cliente.setDtSocket(new MulticastSocket(cliente.PORT_AUDIO));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while (true) {
			    try {
			        packet = new DatagramPacket(buffer, buffer.length);
			        //System.out.println("Reception beggins for host "+host+" : "+port);
			        cliente.getDtSocket().receive(packet);
			        //System.out.println("Reception ends");
			        buffer=packet.getData();

			        //sLine.write(packet.getData(), 0, buffer.length);
			        packet.setLength(buffer.length);
			    } catch (UnknownHostException e) {
			        e.printStackTrace();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}

	    }

	    public class Reader implements Runnable {
	        public void run() {
	            while (true) {
	                if (packet!=null) {
	                    sLine.write(packet.getData(), 0, buffer.length);
	                }
	            }           
	        }       
	    }   
	}
