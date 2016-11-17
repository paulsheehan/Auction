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
		
		//provides different functionality based on the clients command
		switch (msg) {
	        case "quit":  {	//quit program
	        	server.remove(ID);
	            thread = null;
	            msg = "Press enter to quit";
	            break;
	        }
	        case "bye": { //quit program
	        	server.remove(ID);
	        	thread = null;
	        	msg = "Press enter to quit";
	            break;
	        }
	        case "exit": {  //quit program
	        	server.remove(ID);
	            thread = null;
	            msg = "Press enter to quit";
	            break;
	        }
	        case "help":  {	//show commands to user
	        	msg = "Enter a number without currency symbol to place a bid; exit = exit bidding; me = display your purchased items ";
	        	break;
	        }
	        case "me":  { //I was going to write a users purchases and total money spent to a file but didn't have time sorry
	        	//write functionality to print purchased items
	        	break;
	        }
	        default:
	        	//if the client command is numbers only
	        	String regex = "\\d+";
	        	if(msg.matches(regex)) {
	        		int bid = Integer.parseInt(msg);
	        		msg = server.newBidder(bid, bidder);
	        	}
	        	else{	//invalid command because the command contains spaces and or letters
	        		msg = "No such command. Enter a bidding price, or 'help' for list of commands";
	        	}
		}
		//send message to all clients
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