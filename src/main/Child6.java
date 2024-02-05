package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Child6 {
	
	
	protected int a=110;
	/*
	protected static void print(Integer a) {
		System.out.println("integer");
	}
	public void print(int a) {
		System.out.println("int");
	}
	public void print(double a) {
		System.out.println("double");
	}
	*/
	public static void main(String args[]) {
		
		List<Child1> list = new ArrayList<Child1>();
		
		list.add(new Child1("Red", 1));
		list.add(new Child1("Green", 3));
		list.add(new Child1("Black", 2));
		list.add(new Child1("Green", 2));
		
		//Collections.sort(list,new MarksComparator());
		
		//Collections.sort(list, (a1,a2) -> a1.getMarks()-a2.getMarks() );
		Collections.sort(list, (a1,a2) -> a1.getColor().compareTo(a2.getColor()) );
		
		Map<String, Long> result = list.stream().collect(Collectors.groupingBy(Child1::getColor,Collectors.counting()));

		System.out.println(result);
		System.out.println("=======");
		System.out.println(list);
	}
	
	public static class MarksComparator implements Comparator<Child1>{

		@Override
		public int compare(Child1 o1, Child1 o2) {
			return o1.getMarks()-o2.getMarks();
		}
		
	}
	
	
	
	
}
