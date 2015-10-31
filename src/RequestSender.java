import java.io.IOException;
import java.io.*;
import java.net.*;

public class RequestSender implements Runnable {

	Socket requestSocket;
	
	public RequestSender (Socket requestSocket) {
		this.requestSocket = requestSocket;
	}
	
	@Override
	public void run() {
		System.out.println("inside run()");

		InetAddress requestIP = requestSocket.getInetAddress();
		int requestPort = requestSocket.getPort();
		System.out.println(requestPort + ","+ requestIP);
		
		try {
			InputStream requestInput = requestSocket.getInputStream();
			Socket outSocket = new Socket("case.edu", 80);
			OutputStream output = outSocket.getOutputStream();
			
		} 
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
