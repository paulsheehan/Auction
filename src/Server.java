import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	
	private Thread thread = null;
	private ServerSocket server = null;
	// Array of clients	
	private ServerThread clients[] = new ServerThread[50];
	private int clientCount = 0;
	
	
	//connects to post that was passed in cwd
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
	
	//creates a tread
	public void start() {
		// TODO Auto-generated method stub
		if (thread == null) {
			  thread = new Thread(this);
			  //calls run()
	          thread.start();
	       }

	}
	
	public void stop() {
		thread = null;
	}
	
	   private int findClient(int ID)
	   {
		   for (int i = 0; i < clientCount; i++)
	         if(clients[i].getID() == ID)
	            return i;
		   return -1;
	   }
	   
	   public synchronized void broadcast(String input)
	   {
	         for (int i = 0; i < clientCount; i++)
	         {
	        	 clients[i].send(input); // sends messages to clients
			 }
	         
	         notifyAll();
	   }
	
	//adds thread for the client
	private void addThread(Socket socket)
	   {
		//max 50 clients
		  if (clientCount < clients.length){

			 System.out.println("Client accepted: " + socket);
			 //new server thread for client
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
	   
	   public synchronized void remove(int ID)
	   {
		  int pos = findClient(ID);
	      if (pos >= 0){
			 ServerThread toTerminate = clients[pos];
	         System.out.println("Removing client thread " + ID + " at " + pos);

	         if (pos < clientCount-1)
	            for (int i = pos+1; i < clientCount; i++)
	               clients[i-1] = clients[i];
	         clientCount--;

	         try{
				 toTerminate.close();
		     }
	         catch(IOException ioe)
	         {
				 System.out.println("Error closing thread: " + ioe);
			 }
			 toTerminate = null;
			 System.out.println("Client " + pos + " removed");
			 notifyAll();
	      }
	   }
	   
	   @Override
		public void run() {
			// TODO Auto-generated method stub
			//continues until thread is closed
		    
			while (thread != null)
		      {
				 try{
					System.out.println("Waiting for a client ...");
		            try {
						addThread(server.accept());
						this.broadcast("I Love You.");
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
