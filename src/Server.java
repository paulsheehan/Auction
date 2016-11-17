import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server implements Runnable {
	private Thread thread = null;
	private Socket socket = null;
	private ServerSocket server = null;
	private Timer timer = null;
	
	// Array of clients	
	private ServerThread clients[] = new ServerThread[50];
	//number of clients connected to the server
	private int clientCount = 0;
	//list of items
	private static LinkedList<Item> items = new LinkedList<Item>();
	
	//connects to post that was passed in cwd
	public Server(int port)
	{
		try {
				System.out.println("Binding to port " + port + ", please wait  ...");
	        	server = new ServerSocket(port);
	        	System.out.println("Server started: " + server.getInetAddress());
	        	startAuction();
	        	start();
	        }
	    catch(IOException ioe)
	    {
	    	System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
	    }
	}
	
	public void startAuction() {
		// TODO Auto-generated method stub
		
		//if no bidders then item goes to end of queue
		if(items.isEmpty()) {
			
			if(items.getFirst().getHighestBidder() == null) {
				//pop un-sold item to end of list
				items.add(new Item(items.getFirst().getName(), items.getFirst().getPrice(), items.getFirst().getBrand()));
				items.removeFirst();
			}
			else{	//remove the sold item
				this.broadcastToAllClients(items.getFirst().getName() + " sold to" + items.getFirst().getHighestBidder() + " for " + items.getFirst().getPrice());
				items.removeFirst();
			}
			timer = new Timer(this);
			this.broadcastToAllClients("Next item on sale iiiiiss a " + items.getFirst().toString() + " Remaining time left to bid is " + timer.getTimeRemaining());
		}
		else {
			this.broadcastToAllClients("All items have been sold, the bidding has ended!");
		}
	}
	
	public synchronized String newBidder(int bid, String bidder) {
		String responce;
		
		if(bid > items.getFirst().getPrice()) {
			items.getFirst().setPrice(bid);
			items.getFirst().setHighestBidder(bidder);
			responce = bidder + " is the new higest bidder with " + bid + "!";
		}else {
			responce = "Your bid is too low the highest bid is $" + items.getFirst().getPrice();
		}
		
		timer = new Timer(this);
		System.out.println(timer.getTimeRemaining());
		return responce;
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
		   for (int i = 0; i < clientCount; i++) {
			   if(clients[i].getID() == ID){
			       return i;
			   }
		   }
		   return -1;
	   }
	   
	   public synchronized void broadcastToAllClients(String input)
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
						//Send message to newly connected client
						this.broadcastToAllClients(items.getFirst().toString() + " Remaining time left to bid is " + timer.getTimeRemaining());
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
		
		items.add(new Item("Keyboard", 10.0f, "HP"));
		items.add(new Item("Raberry Pi", 15.0f, "Element14"));
		items.add(new Item("4TB External HD", 62.0f, "Seagate"));
		items.add(new Item("Table-Lamp", 14.0f, "Glow"));
		items.add(new Item("Fitbit Charge 2", 110.0f, "Fitbit"));
		
		Server server = null;
	    if (args.length != 1) {
	         System.out.println("Usage: java Server port");
	    }
	    else {
	       server = new Server(Integer.parseInt(args[0]));
	    }
	}
	
}
