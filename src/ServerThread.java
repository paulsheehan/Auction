import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread{
	
	private int ID = -1;
	private Server server = null;
	private Socket socket = null;
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;

	public ServerThread(Server _server, Socket _socket)
	{
		super();
	    server = _server;
	    socket = _socket;
	    ID = socket.getPort();

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
	   
}