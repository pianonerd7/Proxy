import java.nio.ByteBuffer;

public class RequestEditor {

	private String request;
	
	public RequestEditor(ByteBuffer buffer, String ip) {
		initializeRequestEditor(buffer, ip);
	}
	
	private void initializeRequestEditor(ByteBuffer buffer, String ip) {
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
