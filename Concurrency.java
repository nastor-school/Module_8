package concur;

import java.util.Random;

class SumClass extends Thread {
	public int start;
	public int end;
	
	// Constructor to set origin
	// @param start lowerbound for thread to review sumArr
	// @param end uppperbound for thread to review sumArr
	public SumClass(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	// Code for thread to run
	// Counts sum for threads slice of sumArr and increments total
	public void run() {
		int sum = 0;
		
		for (int x = start; x < end; x++) {
			sum += Concurrency.sumArr[x];
		}
		
		Concurrency.incrementTotal(sum);
		
	}
}

public class Concurrency {
	private static int total;
	private static long time;
	// Create 200 million length array with random integers 1 - 10
	static Random random = new Random();
	public static int[] sumArr = random.ints(200000000,1,11).toArray();

	public static void main(String[] args) {
		// Start the counter for multiple threads
		long startTime = System.nanoTime();
		// Create array filled with 200 Threads
		// Each thread will review 1 million elements in sumArr
		SumClass[] threadArr = new SumClass[200];
		// Creating and starting threads
		for (int x = 0; x < threadArr.length; x++) {
			threadArr[x] = new SumClass(x * 1000000, (x * 1000000) + 1000000);
			threadArr[x].start();
		}
		
		// Joining threads
		for (int x = 0; x < threadArr.length; x++) {
			try {
				threadArr[x].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Ending counter and evaluating time
		long endTime = System.nanoTime();
		long multiThreadTime = endTime - startTime;
		
		// Printing results
		System.out.println("Multiple Threads | Time: " + multiThreadTime + " | Total: " + total);
	
		// Restarting to review single thread
		clearTotal();
		startTime = System.nanoTime();
		
		SumClass single = new SumClass(0, 200000000);
		single.start();
		try {
			single.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		endTime = System.nanoTime();
		long singleThreadTime = endTime - startTime;
		
		// Printing results
		System.out.println("Single Thread | Time: " + singleThreadTime + " | Total: " + total);
		
		// Printing some metrics
		System.out.println("Difference in thread time: " + (multiThreadTime - singleThreadTime) + " nanoseconds.");
	}
	
	// Increments total variable 
	// Is synchronized so that multiple threads can access without race condition
	// @param increment amount to increment by
	public static synchronized void incrementTotal(int increment) {
		total += increment;
	}
	
	// Sets total to 0
	public static void clearTotal() {
		total = 0;
	}
}
