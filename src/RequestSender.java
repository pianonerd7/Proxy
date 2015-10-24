import java.net.Socket;

public class RequestSender implements Runnable {

	Socket requestSocket;
	
	public RequestSender (Socket requestSocket) {
		this.requestSocket = requestSocket;
	}
	
	@Override
	public void run() {
		
	}

}
