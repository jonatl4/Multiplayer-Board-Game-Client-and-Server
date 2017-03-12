import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable{
	
	private static Socket clientSocket = null;
	private static ObjectOutputStream output = null;
	private static ObjectInputStream input = null;
	
	public static void main(String[] args){
		System.out.println("Starting client connection...");
		try {
			clientSocket = new Socket("127.0.0.1", 5000);
			OutputStream o = clientSocket.getOutputStream();
			output = new ObjectOutputStream(o);
			InputStream i = clientSocket.getInputStream();
			input = new ObjectInputStream(i);
			Thread clientThread = new Thread(new Client());
			clientThread.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		NetworkProtocol outgoingData = null;
		outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.TESTCLIENT);
		try {
			output.writeObject(outgoingData);
			output.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while(true){
			try {
				NetworkProtocol incoming = (NetworkProtocol)input.readObject();
				if(incoming.getDataType() == NetworkProtocol.ProtocolType.TESTSERVER){
					System.out.println("Receiving TESTSERVER data from server");
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
