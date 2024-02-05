package Thread;

import main.B;

public class Thread1 extends Thread {
	public static int a;
	public void run() {
		for(int i=0;i<1000;i++) {
			System.out.println("Thread 1"+i);
		}
	}
	
	public static int implement() {
		System.out.println("implement");
		return 0;
	}
	
	public static void main(String args[]) {
		System.out.println("Hello");
		
		B b = Thread1::implement;
		b.showA();
		
	}
	
}
