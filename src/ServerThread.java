import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerThread extends Thread{
	
	private int ID = -1;
	private Server server = null;
	private Socket socket = null;
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;
	private Thread thread;
	
	public ServerThread(Server _server, Socket _socket)
	{
		super();
	    server = _server;
	    socket = _socket;
	    ID = socket.getPort();

	}
	
	public int getID()
	{
		return ID;
	}
	
	public void send(String msg)
	{
		try
		{
			streamOut.writeUTF(msg);
	        streamOut.flush();
	    }
		catch(IOException ioe)
		{
			System.out.println(ID + " ERROR sending: " + ioe.getMessage());
	        server.remove(ID);
	        thread=null;
	    }
	}
	

	public void open() throws IOException
	{
		streamIn = new DataInputStream(new
				BufferedInputStream(socket.getInputStream()));
		streamOut = new DataOutputStream(new
				BufferedOutputStream(socket.getOutputStream()));
	}

	public void close() throws IOException
	{
	   if (socket != null)
		   socket.close();
	   if (streamIn != null)
		   streamIn.close();
	   if (streamOut != null)
		   streamOut.close();
	}
	
	//handles the clients message
	public void handle(String msg, Thread thread) {
		
		String bidder = msg.substring(0, msg.indexOf(" "));
		msg = msg.substring(msg.indexOf(" ")+1);
		
		switch (msg) {
	        case "quit":  {
	        	server.remove(ID);
	            thread = null;
	            msg = "Press enter to quit";
	            break;
	        }
	        case "bye": { 
	        	server.remove(ID);
	        	thread = null;
	        	msg = "Press enter to quit";
	            break;
	        }
	        case "exit": {  
	        	server.remove(ID);
	            thread = null;
	            msg = "Press enter to quit";
	            break;
	        }
	        case "help":  {
	        	//msg = write help page
	        	break;
	        }
	        case "me":  {
	        	//write functionality to print purchased items
	        	break;
	        }
	        default:
	        	String regex = "\\d+";
	        	if(msg.matches(regex)) {
	        		int bid = Integer.parseInt(msg);
	        		msg = server.newBidder(bid, bidder);
	        	}
	        	else{
	        		msg = "No such command. Enter a bidding price, or 'help' for list of commands";
	        	}
		}
		
		server.broadcastToAllClients(msg);
	}
	
	public void run()
	{
		System.out.println("Server Thread " + ID + " running.");
		  thread = new Thread(this);
	      while (true){
				 //This is where the clients message is received by the server
				 
					try {
						handle(streamIn.readUTF(), thread);
						int pause = (int)(Math.random()*3000);
					 	 Thread.sleep(pause);
					} 
					catch (IOException ioe) {
						// TODO Auto-generated catch block
			            //server.remove(ID);
			            thread = null;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						System.out.println(e);
					}
					
			 
	      }
		
	}
	   
}