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
			ByteBuffer bufferInput = ByteBuffer.allocate(16*1024);

			int temp = requestInput.read();
			System.out.println("LALALALALA");
			while (temp != -1) {
				bufferInput.put((byte)temp);
				
				if (isEndReq((char)temp)) {
					RequestEditor editor = new RequestEditor(bufferInput, requestIP.getHostAddress());
					bufferInput.clear();
					System.out.print(editor);
					System.out.flush();
					Socket outSocket = new Socket(editor.getHostName(), 80);
					RequestProcessor processor = new RequestProcessor(requestSocket, outSocket);
					System.out.println("here1");
					new Thread(processor).start();
					
					OutputStream outputStream = outSocket.getOutputStream();
					outputStream.write(editor.toByteArr());
					
				}
				temp = requestInput.read();
				
			}
			requestSocket.close();
		} 
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	int endCount = 0;
	private boolean isEndReq(char currChar) {
		
		if (endCount == 0 && currChar == '\r') {
			endCount++;
			System.out.println(endCount);
		}
		else if (endCount == 1 && currChar == '\n') {
			endCount++;
			System.out.println(endCount);
		}
		else if (endCount == 2 && currChar == '\r') {
			endCount++;
			System.out.println(endCount);
		}
		else if (endCount == 3 && currChar == '\n') {
			return true;
		}
		else {
			endCount = 0;
		}
		return false;
	}

}
