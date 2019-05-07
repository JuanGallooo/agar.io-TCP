package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

import hilos.hiloServerChat;

public class ClientHandler implements Runnable{
	Scanner scn = new Scanner(System.in); 
    private String name; 
    final DataInputStream dis; 
    final DataOutputStream dos; 
    Socket s; 
    boolean isloggedin; 
      
    public ClientHandler(Socket s, String name, 
                            DataInputStream dis, DataOutputStream dos) { 
        this.dis = dis; 
        this.dos = dos; 
        this.name = name; 
        this.s = s; 
        this.isloggedin=true; 
    } 
    
    public void run() { 
  
        String received=""; 
        while (true)  
        { 
            try
            { 
            		received = dis.readUTF(); 
                    if(received.equals("logout")){ 
                        this.isloggedin=false; 
                        this.s.close(); 
                        break; 
                    } 
                      
                    StringTokenizer st = new StringTokenizer(received, "#"); 
                    String recipient = st.nextToken(); 
                    String MsgToSend = st.nextToken(); 
                    name=recipient;
                    for (ClientHandler mc : hiloServerChat.ar)  
                    { 
                    	mc.dos.writeUTF(this.name+" : "+MsgToSend); 
                    } 
            		
            } catch (IOException e) { 
                  
                e.printStackTrace(); 
            } 
              
        } 
        try
        { 
            // closing resources 
            this.dis.close(); 
            this.dos.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 

}
