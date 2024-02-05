package Serialization;

import java.io.Serializable;

public class Bean implements Serializable {
	
	public int i;
	public String name;
	public static int a=10;
	public transient int c;
	public Bean(int i, String name, int c) {
		super();
		this.i = i;
		this.name = name;
		this.c = c;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public static int getA() {
		return a;
	}
	public static void setA(int a) {
		Bean.a = a;
	}
	public int getC() {
		return c;
	}
	public void setC(int c) {
		this.c = c;
	}
	@Override
	public String toString() {
		return "Bean [i=" + i + ", name=" + name + ", c=" + c + "]";
	}
	
}
