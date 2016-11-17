
public class Timer extends Thread{
	
	private static double startTime;
	private static double endTime;
	private static double timeRemaining;
	private static final int duration = 60000;
	private static Server s = null;	//passing in this so I can broadcast theres most likely a better way to do this
	
	public Timer(Server server) {
		startTime = System.currentTimeMillis();
		endTime = startTime + duration;	//end time is 1 minute away from the current time
		timeRemaining = startTime;
		s = server;
		start();
	}
	
	public void run() {
		while(timeRemaining > 0 && !this.isInterrupted()){
			timeRemaining = endTime - System.currentTimeMillis();	//remaining time counts down from 60 seconds or 60000 milliseconds
			if(timeRemaining % 10000 == 0){	//Every 10 seconds the client gets a time reminder
				s.broadcastToAllClients(timeRemaining + " seconds left to bid");
			}
		}
		s.startAuction();
	}
	
	public double getTimeRemaining() {
		
		return timeRemaining;
	}
}
