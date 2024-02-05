package main;

public class Parent {
	
	public Parent() {
		System.out.println("super");
	}

	public String name;
	
	public void print(String a) {
		System.out.println("parent - ");
	}
	
	//public volatile int a = 1000;
	public static int a = 1000;
	
	public static void test() {
		System.out.println("test");
	}
	
	public final void test1() {
		System.out.println("test1");
	}
	
	public static final void test2() {
		System.out.println("test2");
	}
	
	
}
