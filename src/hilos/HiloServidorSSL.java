package hilos;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class HiloServidorSSL implements Runnable{

	 SSLServerSocket s;
	public HiloServidorSSL() {
		System.setProperty("javax.net.ssl.trustStore", "./docs/server.jks");
		String ksPassword = "12345678";
		String ctPassword = "12345678";
	    String ksName = "./docs/server.jks";
	    char ksPass[] = ksPassword.toCharArray();
	    char ctPass[] = ctPassword.toCharArray();
	    KeyStore ks;
	    try {
	        ks = KeyStore.getInstance("JKS");
	        ks.load(new FileInputStream(ksName), ksPass);
	        KeyManagerFactory kmf = 
	        KeyManagerFactory.getInstance("SunX509");
	        kmf.init(ks, ctPass);
	        SSLContext sc = SSLContext.getInstance("TLS");
	        sc.init(kmf.getKeyManagers(), null, null);
	        SSLServerSocketFactory ssf = sc.getServerSocketFactory();
	        s   = (SSLServerSocket) ssf.createServerSocket(6500);                
	    } catch (Exception e) {
	        e.printStackTrace();
	    } 
	}
	
	
	@Override
	public void run() {
		try {
			while(true){                
	            SSLSocket sslsocket = (SSLSocket) s.accept();
	            System.out.println("New Client accepted");
	            HiloEscuchaSSL t = new HiloEscuchaSSL(sslsocket);
	            Thread o= new Thread(t);
	            o.start();      
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
