import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;

import q7.LinkedList;


public class Test {

    //Runnable task for each thread
	private abstract class Task implements Runnable {
    	
    	protected LinkedList list;
    	protected CyclicBarrier start, block, end;
    	protected Random rand;
    	protected int max;

        public Task(CyclicBarrier start, CyclicBarrier block, CyclicBarrier end, LinkedList list, int max) {
            this.start = start;
        	this.block = block;
            this.end = end;
            this.list = list;
            this.rand = new Random();
            this.max = max;
        }

        @Override
        public void run() {  
        	
        	try {
				start.await();
			} catch (InterruptedException | BrokenBarrierException e1) {
				e1.printStackTrace();
			}
        	
        	for(int i = 0; i < max; i++){
	            try {
	            	block.await();
	                execute();
	            } catch (InterruptedException | BrokenBarrierException ex) {
	                ex.printStackTrace();
	            }
        	}
        	
        	try {
				end.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
        }
        
        public abstract void execute();
    }
	
	class AddTask extends Task {
		public AddTask(CyclicBarrier start, CyclicBarrier block, CyclicBarrier end, LinkedList list, int max) {
			super(start, block, end, list, max);
		}

		@Override
		public void execute() {
			list.add(rand.nextInt());			
		}		
	}
	
    public void testAdd(final int threadCount, final int maxCount, final LinkedList list){
    	final AtomicLong startTime = new AtomicLong();
    	
    	CyclicBarrier start = new CyclicBarrier(threadCount, new Runnable(){
			@Override
			public void run() {
				startTime.set(System.nanoTime());
				
			}    		
    	});
    	
    	CyclicBarrier block = new CyclicBarrier(threadCount);
    	CyclicBarrier end = new CyclicBarrier(threadCount, new Runnable(){
			@Override
			public void run() {
				System.out.println("Time: " + (double)(System.nanoTime() - startTime.get()) / 1000000000.0);
			}    		
    	});
    	
    	for(int i = 0; i < threadCount; i++){
    		new Thread(new AddTask(start, block, end, list, (int) maxCount/threadCount), i + "").start();
    	}
    }
    
    class AddRemove extends Task {
		public AddRemove(CyclicBarrier start, CyclicBarrier block, CyclicBarrier end, LinkedList list, int max) {
			super(start, block, end, list, max);
		}

		@Override
		public void execute() {
			list.remove(rand.nextInt());			
		}		
	}
	
    public void testRemove(final int threadCount, final int maxCount, final LinkedList list){
    	final AtomicLong startTime = new AtomicLong();
    	
    	CyclicBarrier start = new CyclicBarrier(threadCount, new Runnable(){
			@Override
			public void run() {
				startTime.set(System.nanoTime());
				
			}    		
    	});
    	
    	CyclicBarrier block = new CyclicBarrier(threadCount);
    	CyclicBarrier end = new CyclicBarrier(threadCount, new Runnable(){
			@Override
			public void run() {
				System.out.println("Time: " + (double)(System.nanoTime() - startTime.get()) / 1000000000.0);
			}    		
    	});
    	
    	for(int i = 0; i < threadCount; i++){
    		new Thread(new AddRemove(start, block, end, list, (int) maxCount/threadCount), i + "").start();
    	}
    }
}
