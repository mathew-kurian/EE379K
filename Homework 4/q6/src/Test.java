import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import q6.Lock;	

public class Test implements Runnable {
	
	Lock lock;
	CyclicBarrier cb;
	CyclicBarrier start;
	long startTime;
	int threadCount;
	int max = 0;
	Integer count = 0;
	
	public Test(Lock lock, int threads, int max){
		this.lock = lock;
		this.max = max;
		this.threadCount = threads;
		this.cb = new CyclicBarrier(threads, new Runnable(){
			@Override
			public void run() {
				System.out.println(System.nanoTime() - startTime);
			}			
		});
		this.start = new CyclicBarrier(threads, new Runnable(){
			@Override
			public void run() {
				startTime = System.nanoTime();				
			}
		});
	}
	
	public void run(){
		try {
			this.start.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
		
		while(true){
			lock.lock();
			
			if(count >= max){
				break;
			}
			
			count++;
			
			lock.unlock();
		}
		
		try {
			this.cb.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
	
	public void start(){
		for(int i = 0; i < threadCount; i++){
			new Thread(this).start();
		}
	}
}
