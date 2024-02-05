package main;

import java.util.concurrent.CountDownLatch;

public class Child5 extends AbsClass {

	
	
	@Override
	public void test1() {
		System.out.println("Child");
	};
	
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch c = new CountDownLatch(100);
		
		Thread t = new Thread(() -> {
			for(int i=0;i<100;i++) {
				System.out.println("->"+i);
				c.countDown();
			}	
		});
		t.start();
		c.await();
		System.out.println("Done");
		
	}
	
}
