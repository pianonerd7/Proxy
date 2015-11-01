import java.io.IOException;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class RequestSender implements Runnable {

	private String request;
	Socket requestSocket;
	
	public RequestSender (Socket requestSocket) {
		this.requestSocket = requestSocket;
	}
	
	@Override
	public void run() {
		InetAddress requestIP = requestSocket.getInetAddress();
		int requestPort = requestSocket.getPort();
		System.out.println("Your request comes from IP address: "+ requestIP + " with port of: "+ requestPort);
		
		try {
			InputStream requestInput = requestSocket.getInputStream();
			ByteBuffer bufferInput = ByteBuffer.allocate(16*1024);

			int temp = requestInput.read();
			while (temp != -1) {
				bufferInput.put((byte)temp);
				
				if (isEndReq((char)temp)) {
					startEditor(bufferInput, requestIP.getHostAddress());
					bufferInput.clear();
					System.out.print(toString());
					Socket outSocket = new Socket(getHostName(), 80);
					RequestProcessor processor = new RequestProcessor(requestSocket, outSocket);
					new Thread(processor).start();
					
					OutputStream outputStream = outSocket.getOutputStream();
					outputStream.write(toByteArr());
					
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
		}
		else if (endCount == 1 && currChar == '\n') {
			endCount++;
		}
		else if (endCount == 2 && currChar == '\r') {
			endCount++;
		}
		else if (endCount == 3 && currChar == '\n') {
			return true;
		}
		else {
			endCount = 0;
		}
		return false;
	}
	
	private void startEditor(ByteBuffer buffer, String ip) {
		buffer.flip();
		StringBuilder sb = new StringBuilder();
		
		while (buffer.hasRemaining()) {
			sb.append((char)buffer.get());
		}
		
		buffer.flip();
		request = sb.toString();
		
		addHeader("Connection", "closed");
		addHeader("X-Forwarded-For", ip);
		removeHeader("Proxy-Connection");
	}
	
	private void removeHeader(String header) {
		int i = headerFieldStart(header);
		if (i != -1) {
			int j = i;
			while (request.charAt(j) != '\n') {
				j++;
			}
			request = request.substring(0, i) + request.substring(j + 1);
		}
	}
	
	private void addHeader(String header, String value) {
		
		if (value == null) {
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		int i = headerFieldStart(header);
		if (i == -1) {
			i = newHeaderFieldIndex();
			sb.append(request.substring(0, i));
			sb.append(header + ": " + value + "\r\n");
			sb.append(request.substring(i));
		}
		else {
			i = i + header.length() + 2;
			
			int j = i;
			
			while (request.charAt(j) != '\r') {
				j++;
			}
			
			sb.append(request.substring(0, i));
			sb.append(value);
			sb.append(request.substring(j));
		}
		request = sb.toString();
	}
	
	private int newHeaderFieldIndex() {
		
		int i = headerFieldStart("Host");
		if (i != -1) {
			while (request.charAt(i) != '\n') {
				i++;
			}
		}
		else {
			System.exit(1);
		}
		return i+1;
	}
	
	private int headerFieldStart(String header) {
		
		int i = 0;
		int temp = 0;
		while (true) {
			temp = request.indexOf(header, i);
			if (temp == -1 || request.charAt(temp-1)=='\n') {
				return temp;
			}
			i++;
				
		}
	}
	
	public String getHostName() {
		int i = request.indexOf("Host");
		
		if (i == -1) {
			System.exit(1);
		}
		i = i + "Host".length() + 2;
		int j = i;
		
		while (request.charAt(j) != '\r') {
			j++;
		}
		
		return request.substring(i, j);
	}
	
	public byte[] toByteArr() {
		return request.getBytes();
	}
	
	public String toString() {
		return request;
	}

}
