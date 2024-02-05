package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Child8 extends Child6 {
	
	//public static Consumer<List<Integer>> printer = t -> t.stream().forEach(a -> System.out.println("Round is "+a));
	
	public static void main(String args[]) {
		/*
		String a = "abcd";
		
		if(a.length() != a.chars().distinct().count()) {
			System.out.println("has duplicates");
		}
		else {
			System.out.println("no duplicates");
		}
		a.chars().sorted().forEach(b -> System.out.println(b));
		
		
		List<String> list = new ArrayList<>();
		list.add("java");
		list.add("is");
		list.add("fun");
		System.out.println(String.join("-", list));
		String match = "is";
		list.stream().filter(match::equalsIgnoreCase);
		
		Integer b = 10;
		List<Child1> list1 = new ArrayList<Child1>();
		list1.add(new Child1("blue", 10));
		list1.add(new Child1("red", 20));
		list1.add(new Child1("green", 30));
		list.stream().filter(b::equals);
		//System.out.println(list.stream().filter(l -> {l.ge}).toString());
		
		list1.stream().filter(l -> l.marks == 10).findFirst();
		System.out.println(list1.stream().filter(l -> l.marks == 10).findFirst());
		*/
		
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		
		//for(int a:list) {
		//	System.out.println("Round is "+a);
		//}
		
		Consumer<List<Integer>> printer = t -> t.stream().forEach(a -> System.out.println("Round is "+a));
		
		Consumer<List<Integer>> doubler = t -> t.stream().forEach(a -> System.out.println("Nested round is "+a));
		
		doubler.andThen(printer).accept(list);
		
		
		// 2000 lines of code
		
		List<Integer> someList = new ArrayList<Integer>();
		someList.add(5);
		someList.add(6);
		someList.add(7);
		someList.add(8);
		
		printer.accept(someList);
		
	}
	
	
}
