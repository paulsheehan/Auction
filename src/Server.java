import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	
	private Thread thread = null;
	private ServerSocket server = null;
	// Array of clients	
	private ServerThread clients[] = new ServerThread[50];
	private int clientCount = 0;
	
	public Server(int port)
	{
		try {
				System.out.println("Binding to port " + port + ", please wait  ...");
	        	server = new ServerSocket(port);
	        	System.out.println("Server started: " + server.getInetAddress());
	        	start();
	        }
	    catch(IOException ioe)
	    {
	    	System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
	    }
	}

	
	public void start() {
		// TODO Auto-generated method stub
		if (thread == null) {
			  thread = new Thread(this);
	          thread.start();
	       }

	}
	
	public void stop() {
		thread = null;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (thread != null)
	      {
			 try{

				System.out.println("Waiting for a client ...");
	            try {
					addThread(server.accept());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				int pause = (int)(Math.random()*3000);
				Thread.sleep(pause);

	         }
	         catch (InterruptedException e){
			 	System.out.println(e);
			 }
	      }

	}
	
	private void addThread(Socket socket)
	   {
		  if (clientCount < clients.length){

			 System.out.println("Client accepted: " + socket);
	         clients[clientCount] = new ServerThread(this, socket);
	         try{
				clients[clientCount].open();
	            clients[clientCount].start();
	            clientCount++;
	         }
	         catch(IOException ioe){
				 System.out.println("Error opening thread: " + ioe);
			  }
		  }
	      else
	         System.out.println("Client refused: maximum " + clients.length + " reached.");
	   }

	
	public static void main(String args[]) {
		
		System.out.println("I love you");
		
		Server server = null;
	    if (args.length != 1) {
	         System.out.println("Usage: java Server port");
	    }
	    else {
	       server = new Server(Integer.parseInt(args[0]));
	    }
	}
	
}
