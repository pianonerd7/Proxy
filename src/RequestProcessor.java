import java.io.*;
import java.net.Socket;

public class RequestProcessor implements Runnable {

	private Socket listenerSocket;
	private Socket outSocket;
	
	public RequestProcessor(Socket listenerSocket, Socket outSocket) {
		this.listenerSocket = listenerSocket;
		this.outSocket = outSocket;
	}
	
	@Override
	public void run() {
		try {
			InputStream inputStream = outSocket.getInputStream();
			OutputStream outputStream = listenerSocket.getOutputStream();
			
			int temp = inputStream.read();	
			while (temp != -1) {
				outputStream.write(temp);
				temp = inputStream.read();
			}			
			outSocket.close();
		} 
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
