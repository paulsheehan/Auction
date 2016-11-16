import java.io.DataInputStream;
import java.io.IOException;
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

	public void close() {
		// TODO Auto-generated method stub
		try
	      {  if (streamIn != null) streamIn.close();
	      }
	      catch(IOException ioe)
	      {  
	    	  System.out.println("Error closing input stream: " + ioe);
	      }
	}
	
	public void open() {
		// TODO Auto-generated method stub
		try
	      {
			  streamIn  = new DataInputStream(socket.getInputStream());
	      }
	      catch(IOException ioe)
	      {
			 System.out.println("Error getting input stream: " + ioe);
	         client.stop();
	      }
		}
	
	public void run() {
		while (true && client!= null){
			  try {

				  client.handle(streamIn.readUTF());
	          }
	          catch(IOException ioe)
	          {
				  client = null;
				  System.out.println("Listening error: " + ioe.getMessage());

	         }
	      }
	}
}
