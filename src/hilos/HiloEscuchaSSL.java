package hilos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.net.ssl.SSLSocket;

import conexion.Server;

public class HiloEscuchaSSL implements Runnable{
	SSLSocket sslsocket;
	
	private ObjectInputStream is ;
	private ObjectOutputStream os ;
	
	public HiloEscuchaSSL(SSLSocket sslsocket) {
		try {
			this.sslsocket= sslsocket;
			System.out.println(sslsocket.getInputStream());
			
	    	is = new ObjectInputStream(sslsocket.getInputStream());
	    	os = new ObjectOutputStream(sslsocket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
	    try {
	    	while(true){
	    			
	            String p = (String) is.readObject();
	            //System.out.println("We got: " + p);
	            if(p.equals("--salida")) {
	            	os.writeObject(p);
	            	os.flush();
	            	sslsocket.close();
		            break;
	            }
	            else {
	            boolean confirmado= Server.comprobarContra(p);
	            String envio=""+confirmado;
//	            os.writeObject(p.concat(p));
	            os.writeObject(envio);
	            os.flush();
	            }
	        }
	    } catch (Exception e) {
	       e.printStackTrace();
	    }
//	    } finally {
//	        try {
//	            is.close();
//	            os.close();
//	            this.sslsocket.close();
//	        } catch (IOException e) {
//	         e.printStackTrace();   
//	        }
//	    }
	}

}
