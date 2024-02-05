package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class Child9 extends Child6 {
	
	public Child9() {
		
	}
	
	public static void main(String args[]) {
		
		//List<Child1> list = new ArrayList<>();
		List<Child1> list = new CopyOnWriteArrayList<>();
		list.add(new Child1("Red", 1));
		list.add(new Child1("Black", 1));
		list.add(new Child1("Black", 1));
		list.add(new Child1("Blue", 1));
		list.add(new Child1("Blue", 1));
		list.add(new Child1("Blue", 1));
		list.add(new Child1("Green", 1));
		list.add(new Child1("Green", 1));
		list.add(new Child1("Green", 1));
		
		//System.out.println(list.stream().collect(Collectors.groupingBy(a -> a.getColor() ,Collectors.counting()) ) );
		
		
		
		
		System.out.println(list.stream().collect(Collectors.groupingBy(a -> a.getColor() ) ) );
		Map<String, List<Child1>> map = list.stream().collect(Collectors.groupingBy(a -> a.getColor() ) ); 
		
		
		
		//list.stream().collect(Collectors.counting());
		/*
		Iterator<Child1> iterator = list.listIterator();
		
		while(iterator.hasNext()) {
			System.out.println(iterator.next().getColor());
			list.add(new Child1("Violet", 1));
		}
		System.out.println("----------");
		System.out.println(list);
		*/
		//failSafeMap();
		//failSafeSet();
		
		List<Integer> someList = new ArrayList<Integer>();
		someList.add(5);
		someList.add(85);
		someList.add(27);
		someList.add(11);
		
		List<Integer> avgList = new ArrayList<Integer>();
		avgList.add(5);
		avgList.add(5);
		avgList.add(5);
		avgList.add(5);
		
		int c = someList.stream().reduce(0, (a,b)->a+b);
		int d = someList.stream().reduce(1, (a,b)->a*b);
		int avg = (int) someList.stream().mapToInt(i->i).average().getAsDouble();
		System.out.println(c);
		System.out.println(d);
		System.out.println(avg);
		System.out.println("+++++");
		someList.stream().sorted().forEach(System.out::println);
		
	}

	private static void failSafeSet() {
		Set<Integer> set = new CopyOnWriteArraySet<>();
		set.add(1);
		set.add(2);
		
		Iterator<Integer> it = set.iterator();
		
		while(it.hasNext()) {
			System.out.println(it.next());
			set.add(3);
		}
		
	}

	private static void failSafeMap() {
		Map<String, Integer> map = new ConcurrentHashMap<>();
		map.put("a", 1);
		map.put("b", 2);
		
		
		Iterator<String> it = map.keySet().iterator();
		
		while(it.hasNext()) {
			System.out.println(""+it.next());
			map.put("c", 3);
		}
		
	}
	
	
}
