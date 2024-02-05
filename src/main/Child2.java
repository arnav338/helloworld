package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Child2 extends Parent {
	
	String name;
	int rollNumber;
	
	public static void test() {
		System.out.println("testChild");
	}
	
	public void print(int a) {
		System.out.println("child - ");
	}
	
	/*
	public final void test1() {
		System.out.println("test1Child");
	}
	
	public static final void test2() {
		System.out.println("test2Child");
	}
	*/
	
	
	public Child2(String name, int rollNumber) {
		super();
		this.name = name;
		this.rollNumber = rollNumber;
		System.out.println("main constructor");
	}
	
	public Child2() {
		super();
		this.name = name;
		this.rollNumber = rollNumber;
		System.out.println("main constructor");
	}
	
	@Override
	public String toString() {
		return "Child2 [name=" + name + ", rollNumber=" + rollNumber + "]";
	}
	
	

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRollNumber() {
		return rollNumber;
	}

	public void setRollNumber(int rollNumber) {
		this.rollNumber = rollNumber;
	}

	
	public void print() {
		System.out.println("based child 2");
		
		final String a;
		
		class localClass {
			void test() {
				System.out.println("Local inner class");
			}
		}
		
		localClass l = new localClass();
		l.test();
		
	}
	
	public static void main(String args[]) {
		List<Integer> list = new ArrayList<>();
		list.add(17);
		list.add(1);
		list.add(190);
		System.out.println(list);
	}
	
	
}
