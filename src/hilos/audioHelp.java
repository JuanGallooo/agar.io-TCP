package hilos;

import java.net.MulticastSocket;

public class audioHelp {
	MulticastSocket dtSocketAudio ;
	
	 public static final String DIRECCION_MULTICAST = "224.0.0.1";
	    
	    public static final int PORT_AUDIO = 9999;
	
	public audioHelp(){
		
	}
	
	
	
	
	
	public MulticastSocket getDtSocketAudio() {
		return dtSocketAudio;
	}



	public void setDtSocketAudio(MulticastSocket dtSocketAudio) {
		this.dtSocketAudio = dtSocketAudio;
	}

}
