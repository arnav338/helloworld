package main;


public class Child3 extends Thread {
	
	private String name = "test1";
	
	public String pname = "test2";
	
	private static String sname = "test3";
	
	public static String rname = "test4";
	
	public void print() {
		
	}
	
	public void run() {
		System.out.println("Run method");
	}
	
	class InnerClass {
		void test() {
			System.out.println("->->"+name+" "+pname+" "+sname+" "+rname);
		}
	}
	
	static interface nestedinterface {
		
		void test();
	}
	
}
