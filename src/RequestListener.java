import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RequestListener {
	
	private static final int portNum = 5026;
	
	public static void main(String[] args) throws IOException {
		
		ServerSocket welcomeSocket = new ServerSocket(portNum);
		
		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			RequestSender sender = new RequestSender(connectionSocket);
			Thread newThread = new Thread(sender);
			newThread.start();	
		}
	}
}
