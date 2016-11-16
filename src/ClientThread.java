import java.io.DataInputStream;
import java.net.Socket;

public class ClientThread extends Thread{
	
	private Socket socket = null;
	private Client client = null;
	private DataInputStream streamIn = null;

	   
	public ClientThread(Client _client, Socket _socket)
	{  
		client = _client;
	    socket = _socket;
	    open();
	    start();
	}

//	private void start() {
//		// TODO Auto-generated method stub
//		
//	}
	
	private void open() {
		// TODO Auto-generated method stub
		
	}
}
