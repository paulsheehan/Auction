
public class Timer extends Thread{
	
	private static double startTime;
	private static double endTime;
	private static double timeRemaining;
	private static final int duration = 60000;
	private static Server s = null;
	
	public Timer(Server server) {
		startTime = System.currentTimeMillis();
		endTime = startTime + duration;
		timeRemaining = startTime;
		s = server;
		start();
	}
	
	public void run() {
		while(timeRemaining > 0 && !this.isInterrupted()){
			timeRemaining = endTime - System.currentTimeMillis();
		}
		s.startAuction();
	}
	
	public double getTimeRemaining() {
		
		return timeRemaining;
	}
}
