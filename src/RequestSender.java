import java.io.IOException;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

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
		System.out.println("Your request comes from IP address: "+ requestIP + "with port of: "+ requestPort);
		
		try {
			InputStream requestInput = requestSocket.getInputStream();
			ByteBuffer bufferInput = ByteBuffer.allocate(16384);

			int temp = requestInput.read();
			System.out.println("LALALALALA");
			while (temp != -1) {
				System.out.print((char)temp);
				bufferInput.put((byte)temp);
				
				if (isEndReq((char)temp)) {
					
				}
				temp = requestInput.read();
				
				Socket outSocket = new Socket("case.edu", 80);
				OutputStream output = outSocket.getOutputStream();
				RequestProcessor processor = new RequestProcessor(requestSocket, outSocket);
				new Thread(processor).start();
			}
		} 
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private boolean isEndReq(char curChar) {
		
		return true;
	}

}
