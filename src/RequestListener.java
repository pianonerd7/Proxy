import java.net.ServerSocket;
import java.net.Socket;

public class RequestListener {
	
	public static void main(String[] args) {

		final int portNum = 5026;
		
		try {
			ServerSocket welcomeSocket = new ServerSocket(portNum);
			System.out.println("A connection with portNumber: " + portNum + "is being created!");
		
			while(true) {
				Socket connectionSocket = welcomeSocket.accept();
				RequestSender sender = new RequestSender(connectionSocket);
				Thread newThread = new Thread(sender);
				newThread.start();	
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
