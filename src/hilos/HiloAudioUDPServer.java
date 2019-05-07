package hilos;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;


import conexion.Server;

public class HiloAudioUDPServer extends Thread {

	    private SourceDataLine sLine;
	    private AudioFormat audioFormat;
	    private AudioInputStream audioInputStream=null;
	    private String host=Server.HOST;
	    private int port=5000;
	    private DatagramSocket server;
	    private DatagramPacket packet;
	    private long startTime;
	    private long endTime=System.nanoTime();;
	    private long elapsed=System.nanoTime();;
	    private double sleepTime;
	    private long sleepTimeMillis;
	    private int sleepTimeNanos, epsilon;

	    public HiloAudioUDPServer(String host, int port) {      
	        this.host=host;
	        this.port=port;
	        init();
	    }

	    public void init() {
	        File file = new File("./docs/test.wav");
	        try {
	            audioInputStream=AudioSystem.getAudioInputStream(file);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        audioFormat = audioInputStream.getFormat();
	        //audioFormat = new AudioFormat(44100, 16, 2, true, false);
	        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
	        System.out.println(info);

	        try {
	            server = new DatagramSocket();
	            System.out.println("Server started");

	        } catch (SocketException e) {
	            e.printStackTrace();
	        }       
	    }

	    public void run() {
	        try {
	        	
	            byte bytes[] =  new byte[4096];
	            byte bytes2[] =  new byte[1024];
	            int bytesRead=0;
	            //The sending rythm of the data have to be compatible with an audio streaming.
	            //So, I'll sleep the streaming thread for (1/SampleRate) seconds * (bytes.lenght/4) - epsilon
	            //=> bytes.lenght/4 because 4 values = 1 frame => For ex, in  1024 bits, there are 1024/4 = 256 frames
	            //epsilon because the instructions themselves takes time.
	            //The value have to be convert in milliseconds et nanoseconds.
	            sleepTime=(1024/audioFormat.getSampleRate());
	            epsilon=400000;
	            sleepTimeMillis=(long)(sleepTime*1000);
	            sleepTimeNanos=(int)((sleepTime*1000-sleepTimeMillis)*1000000);
	            System.out.println("Sleep time :"+sleepTimeMillis+" ms, "+sleepTimeNanos+" ns");

	            while ((bytesRead=audioInputStream.read(bytes, 0, bytes.length))!= -1) {
	                //getSignalLevel(bytes);
	            	
	                try {                   
	                 //   startTime=System.nanoTime();
	                    packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(host), port);
	                    packet.setData(bytes);
	                    server.send(packet);                    
	                    packet.setLength(bytes.length);  
	                    
	                   // endTime=System.nanoTime();
	                    //System.out.println(endTime-startTime);
	                    Thread.sleep(sleepTimeMillis,sleepTimeNanos);                   
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }   
	            System.out.println("No bytes anymore !");                   
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        sLine.close();
	        System.out.println("Line closed");

	    }

	}