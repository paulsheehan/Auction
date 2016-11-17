import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable{
	
	private Socket socket = null;
	private Thread thread = null;
	private BufferedReader console = null;
	private DataOutputStream streamOut = null;
	
	private ClientThread client = null;
	private String name;

	   
	public Client(String serverName, int serverPort, String name) {
		// TODO Auto-generated constructor stub
		
		System.out.println("Establishing connection. Please wait ...");

		  this.name = name;
	      try{
			 socket = new Socket(serverName, serverPort);
	         System.out.println("Connected: " + socket);
	         start();
	      }
	      catch(UnknownHostException uhe){
			  System.out.println("Host unknown: " + uhe.getMessage());
		  }
	      catch(IOException ioe){
			  System.out.println("Unexpected exception: " + ioe.getMessage());
		  }

	}

	public void start() throws IOException
	{
		console = new BufferedReader(new InputStreamReader(System.in));
	    streamOut = new DataOutputStream(socket.getOutputStream());
	    if (thread == null)
	    {  
	    	client = new ClientThread(this, socket);
	        thread = new Thread(this);
	        thread.start();
	    }
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (thread != null){
			 try {
				String message = console.readLine();
				streamOut.writeUTF(message);
	            streamOut.flush();
	         }
	         catch(IOException ioe)
	         {  
	        	System.out.println("Sending error: " + ioe.getMessage());
	            stop();
	         }
	      }

		
	}
	
	public void handle(String msg)
	{  
		if (msg.equals(".bye"))
	    {  
			System.out.println("Good bye. Press RETURN to exit ...");
	        stop();
	    }
	    else
	        System.out.println(msg);
	}

	
	public void stop() {
		// TODO Auto-generated method stub
		try
	      {  if (console   != null)  console.close();
	         if (streamOut != null)  streamOut.close();
	         if (socket    != null)  socket.close();
	      }
	      catch(IOException ioe)
	      {
			  System.out.println("Error closing ...");

	      }
	      client.close();
	      thread = null;

	}

	public static void main(String args[]) {
		
		Client client = null;
		
		if (args.length != 3)
			System.out.println("Usage: java ChatClient host port name");
		else
		    client = new Client(args[0], Integer.parseInt(args[1]), args[2]);
		
	}

}
