package hilos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.net.ssl.SSLSocket;

public class HiloEscuchaSSL implements Runnable{
	SSLSocket sslsocket;
	public HiloEscuchaSSL(SSLSocket sslsocket) {
		this.sslsocket= sslsocket;
	}
	@Override
	public void run() {
		ObjectInputStream is = null;
	    ObjectOutputStream os = null;
	    try {
	        is = new ObjectInputStream(sslsocket.getInputStream());
	        os = new ObjectOutputStream(sslsocket.getOutputStream());
	        while(true){
	            String p = (String) is.readObject();
	            System.out.println("We got: " + p);

	            os.writeObject(p.concat(p));
	            os.flush();
	        }
	    } catch (Exception e) {
	       e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	            os.close();
	            this.sslsocket.close();
	        } catch (IOException e) {
	         e.printStackTrace();   
	        }
	    }
	}

}
