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
			RequestProcessor processor = new RequestProcessor(requestSocket, outSocket);
			new Thread(processor).start();
			
			
			int temp = requestInput.read();
			System.out.println("LALALALALA");
			while (temp != -1) {
				System.out.print((char)temp);
				output.write(temp);
				temp = requestInput.read();
			}
		} 
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
