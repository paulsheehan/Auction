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
	public void handle(String msg, int ID, Thread thread) {
		
		switch (msg) {
        case "quit":  
        	server.remove(ID);
            thread = null;
                 break;
        case "bye":  ;
                 break;
        case "exit":  ;
                 break;
        case "help":  ;
        break;
        case "me":  ;
        break;
        default:  
        	String regex = "\\d+";
    		Pattern pattern = Pattern.compile(regex);
    		
        	if(pattern.matcher(msg).matches()){
        		
        	}
        	else {
        		msg = "No such command. Enter a bidding price, or 'help' for list of commands";
        	}
        break;
		}
		
		server.broadcastToAllClients(msg, ID);
	}
	
	public void run()
	{
		System.out.println("Server Thread " + ID + " running.");
		  thread = new Thread(this);
	      while (true){
			 try{
				 //This is where the clients message is received by the server
				 handle(streamIn.readUTF(), ID, thread);

	         	 int pause = (int)(Math.random()*3000);
			 	 Thread.sleep(pause);
			 }
			 catch (InterruptedException e)
			 {
			 	System.out.println(e);
			 }
	         catch(IOException ioe){
	            server.remove(ID);
	            thread = null;
	         }
	      }
		
	}
	   
}