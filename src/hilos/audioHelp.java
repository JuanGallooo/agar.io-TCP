package hilos;

import java.net.MulticastSocket;

public class audioHelp {
	MulticastSocket dtSocketAudio ;
	
	 public static final String DIRECCION_MULTICAST = "localhost";
	    
	    public static final int PORT_AUDIO = 1024;
	
	public audioHelp(){
		
	}
	
	
	
	
	
	public MulticastSocket getDtSocketAudio() {
		return dtSocketAudio;
	}



	public void setDtSocketAudio(MulticastSocket dtSocketAudio) {
		this.dtSocketAudio = dtSocketAudio;
	}

}
