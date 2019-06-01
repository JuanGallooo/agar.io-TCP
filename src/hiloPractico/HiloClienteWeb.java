package hiloPractico;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class HiloClienteWeb implements Runnable{
	private final Socket socket;

	public HiloClienteWeb(Socket socket)
	{
		this.socket =  socket;
	}
	
	@Override
	public void run() {
		System.out.println("\nClientHandler Started for " + this.socket);
		while(true) 
		{
			handleRequest(this.socket);
		}		
	}
	
	public void handleRequest(Socket socket)
	{
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String headerLine = in.readLine();
			if(headerLine!=null)
			{
				System.out.println(headerLine);
				// A tokenizer is a process that splits text into a series of tokens
				StringTokenizer tokenizer =  new StringTokenizer(headerLine);
				//The nextToken method will return the next available token
				String httpMethod = tokenizer.nextToken();
				// The next code sequence handles the GET method. A message is displayed on the
				// server side to indicate that a GET method is being processed
				if(httpMethod.equals("GET"))
				{
					System.out.println("Get method processed");
					String httpQueryString = tokenizer.nextToken();
					System.out.println(httpQueryString);
					if(httpQueryString.equals("/"))
					{
						StringBuilder responseBuffer =  new StringBuilder();
						String str="";
						BufferedReader buf = new BufferedReader(new FileReader(System.getProperty("user.dir") +"/docs/Login.html"));
						while ((str = buf.readLine()) != null) {
							responseBuffer.append(str);
					    }
						sendResponse(socket, 200, responseBuffer.toString());		
					    buf.close();
					}
					//?txtLogin=Juan+&txtContraseña=&btnCrear=Iniciar+Sesion
					
					if(httpQueryString.contains("/?txtLogin="))
					{
						System.out.println("Get method processed segunda ");
						 //"hamburger".substring(4, 8) returns "urge"
						
						
						String[] response =  httpQueryString.split("&");
						String nombre= response[0].substring(11, response[0].length());
						ArrayList<String> infoJugador= new ArrayList<String>();
						System.out.println(nombre);
						try {
							File archivo = new File("./docs/PuntajesJugadores.txt");
							FileReader fr = new FileReader(archivo);
							BufferedReader sr = new BufferedReader(fr);

							String mensaje = sr.readLine();

							while (mensaje != null && !mensaje.isEmpty()) {
								if(mensaje.split("&")[0].equals(nombre)) {
									infoJugador.add(mensaje.split("&")[1]);
								}
								mensaje = sr.readLine();
							}
							sr.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
						String retornoAppend="";
						retornoAppend= "<html>" + 
								"    <style>" + 
								"        .tabla{" + 
								"            text-align: center;" + 
								"            border: 2px solid black;" + 
								"            margin-left: 30%;" + 
								"            margin-right: 30%;" + 
								"            padding: 5px;" + 
								"            border-spacing: 15px 5px;" + 
								"        }" + 
								"" + 
								"    </style>" + 
								"    <head></head>" + 
								"    <body>" + 
								"        <center><h1>Cantidad de juegos: </h1> </center>" + 
								"        <table class=\"tabla\">" + 
								"            <tr>" + 
								"                <th>Fecha</th>" + 
								"                <th>Numero de Alimentos digeridos</th>" + 
								"                <th>Score</th>" + 
								"                <th>Gano?</th>" + 
								"                <th>Tiempo transcurrido de juego</th>" + 
								"            </tr>";
						
						System.out.println(infoJugador.size());
						for (int i = 0; i < infoJugador.size(); i++) {
							String[] split= infoJugador.get(i).split(" ");
							String nuevo= "<tr>"+"<td>"+ split[0]+"</td>"+"<td>"+ split[1]+"</td>"+"<td>"+ split[2]+"</td>"+
									"<td>"+ split[3]+"</td>"+"<td>"+ split[4]+"</td>"+"</tr>";
							retornoAppend+=nuevo;
						}				
						retornoAppend+=
								"        </table>" + 
								"    </body>" + 
								"</html>";

						StringBuilder responseBuffer =  new StringBuilder();
						responseBuffer
						.append(retornoAppend);
						sendResponse(socket, 200, responseBuffer.toString());		
					}
				}
				
				else
				{
					System.out.println("The HTTP method is not recognized");
					sendResponse(socket, 405, "Method Not Allowed");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void sendResponse(Socket socket, int statusCode, String responseString)
	{
		String statusLine;
		String serverHeader = "Server: WebServer\r\n";
		String contentTypeHeader = "Content-Type: text/html\r\n";
		
		try {
			DataOutputStream out =  new DataOutputStream(socket.getOutputStream());
			if (statusCode == 200) 
			{
				statusLine = "HTTP/1.0 200 OK" + "\r\n";
				String contentLengthHeader = "Content-Length: "
				+ responseString.length() + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes(serverHeader);
				out.writeBytes(contentTypeHeader);
				out.writeBytes(contentLengthHeader);
				out.writeBytes("\r\n");
				out.writeBytes(responseString);
				} 
			else if (statusCode == 405) 
			{
				statusLine = "HTTP/1.0 405 Method Not Allowed" + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes("\r\n");
			} 
			else 
			{
				statusLine = "HTTP/1.0 404 Not Found" + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes("\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
