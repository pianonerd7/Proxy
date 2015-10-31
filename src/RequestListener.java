import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RequestListener {
	
	private static final int portNum = 5026;
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("Listening");
		ServerSocket welcomeSocket = new ServerSocket(portNum);
		

		System.out.println("waiting..");
		Socket connectionSocket = welcomeSocket.accept();
		System.out.println("connected");
		RequestSender sender = new RequestSender(connectionSocket);
		Thread newThread = new Thread(sender);
		newThread.start();	
		
			
		connectionSocket.close();
		System.out.println("exited");
	}
}
