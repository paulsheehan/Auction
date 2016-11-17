
public class Timer extends Thread{
	
	private static double startTime;
	private static double endTime;
	private static double timeRemaining;
	private static final int duration = 60000;
	
	public Timer() {
		startTime = System.currentTimeMillis();
		endTime = startTime + duration;
		timeRemaining = startTime;
		start();
	}
	
	public void run() {
		while(timeRemaining > 0){
			timeRemaining = endTime - System.currentTimeMillis();
		}
		System.out.println("NEXT BID");
	}
	
	public synchronized void updateEndTime() {
		endTime = System.currentTimeMillis() + duration;
	}
	
	public double getTimeRemaining() {
		
		return timeRemaining;
	}
}
