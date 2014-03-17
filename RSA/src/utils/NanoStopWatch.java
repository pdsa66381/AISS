package utils;
/**
 * Stop watch with nano precision 
 */
public class NanoStopWatch {

	private double start_time = 0;
	private double elapsed_time = 0;
	
	public void startClock(){
		this.start_time = System.nanoTime();
	}
	
	public double stopClock(){
		
		this.elapsed_time = System.nanoTime() - this.start_time;
		this.start_time = 0;
		return this.elapsed_time;
	}
}
