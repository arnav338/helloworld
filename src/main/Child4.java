package main;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Child4 {
	
	public static void main(String args[]) {
		
		Map<Child1, Integer> map = new HashMap<Child1, Integer>();
		
		map.put(new Child1("Blue", 1), 5);
		map.put(new Child1("Black", 3), 6);
		map.put(new Child1("Red", 4), 9);
		map.put(new Child1("Orange", 15), 15);
		map.put(new Child1("Yellow", 7), 10);
		map.put(new Child1("Violet", 9), 2);
		
		//map.entrySet()
		//	.stream()
		//	.sorted(Map.Entry.comparingByKey(Comparator.comparing(Child1::getMarks)))
		//	.forEach(System.out::println);
		
		map.entrySet()
		.stream()
		.sorted(Map.Entry.comparingByValue())
		.forEach(System.out::println);
	}
	
}
