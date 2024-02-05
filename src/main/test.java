package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import main.Child3.InnerClass;

public class test {
	
	public static void main(String args[]) throws InterruptedException {
		/*
		Child2 child2 = new Child2() {
			
			public void print() {
				System.out.println("anon child 2");
				}
		};
		*/
		//child2.print(); 
		
		//String s = new String("test");
		//String s1 = new String("test");
		
		//generateFibbonacci(15);
		
		//checkIfArmstrongNumber(153);
		
		//int[] arr = new int[] {1,9,7,13,10,15,17,3};
		//bubbleSort(arr);
		
		//anagramCheck("abc","cba");
		
		//mapStreamExample();
		
		//Map<String, Child4> map = new HashMap<>();
		/*
		Map<String, Child4> map = new LinkedHashMap<>(); //LinkedHashMap maintains insertion order as apart from using buckets(like Hashmap), it also uses doubly linked list to keep track of order. hence is costly in terms of memory
		map.put("key1", new Child4("value1"));
		map.put("key2", new Child4("value2"));
		map.put("key3", new Child4("value3"));
		map.put("key4", new Child4("value4"));
		map.put("key5", new Child4("value6"));
		map.put("key3", new Child4("value7"));
		map.put("key4", new Child4("value4"));
		
		System.out.println(map);
		
		List<String> list = new ArrayList<>(); 
		list.add("1");
		list.add("90");
		list.add("5");
		list.add("57");
		list.add("25");
		System.out.println(list);
		
		Set<Integer> set = new TreeSet<>();
		set.add(1);
		set.add(90);
		set.add(57);
		set.add(25);
		set.add(1);
		System.out.println(set);
		*/
		
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		List<Integer> list1 = Arrays.asList(1,2,3,4,5);
		List<Integer> list2 = Arrays.asList(8,9);
		List<Integer> list3 = Arrays.asList(11,12,13);
		list.add(list1);
		list.add(list2);
		list.add(list3);
		System.out.println(list.stream()
								.flatMap( l -> l.stream())
								.collect(Collectors.toList()) );
		// will flatten the nested lists to single stream of list
		
		
		//System.out.println(new Child2().name.toString());// it will throw NPE as the name field is null
		// everytime we create anonymous object it will be initialized with null
		// it will not retain values
		
		
		/*
		for(Map.Entry<String, Child4> entry : map.entrySet()) {
			System.out.println(entry);
		}
		
		map.merge("key5", new Child4("value5"), (p1,p2) -> p1.getT().toString().charAt(p1.getT().toString().length()-1)==p2.getT().toString().charAt(p1.getT().toString().length()-1)? new Child4("null") : new Child4(p2.getT().toString().substring(p2.getT().toString().length()-1)) );
		System.out.println("---------------------------------");
		
		for(Map.Entry<String, Child4> entry : map.entrySet()) {
			System.out.println(entry);
		}
		
		*/
		/*
		List<String> teamIndia = Arrays.asList("Virat", "Dhoni", "Jadeja"); 
		List<String> teamAustralia = Arrays.asList("Warner", "Watson", "Smith"); 
		List<String> teamEngland = Arrays.asList("Valex", "Bell", "Broad"); 
		
		List<List<String>> playersInWorldCup2016 = new ArrayList<>(); 
		playersInWorldCup2016.add(teamIndia); 
		playersInWorldCup2016.add(teamAustralia); 
		playersInWorldCup2016.add(teamEngland);
		*/
		//System.out.println(playersInWorldCup2016.stream()
		//		 .flatMap(i -> i.stream().filter(name -> name.startsWith("V")))
		//		 .collect(Collectors.toList()));
		/*
		System.out.println(
				Arrays.asList(
						  Arrays.asList("a"),
						  Arrays.asList("b")
						  ).stream()
						   .flatMap(i -> i.stream())
						   .collect(Collectors.toList())
						   
				);
		
		System.out.println(
				Arrays.asList(
						  Arrays.asList(Arrays.asList("a"),Arrays.asList("b")),
						  Arrays.asList(Arrays.asList("c"),Arrays.asList("d"))
						  ).stream()
						   .flatMap(i -> i.stream())
						   .collect(Collectors.toList())
						   
				);
		*/
		
		
		//list.stream().filter(a%2!=0)
		
		//Set<Integer> set = new HashSet<>();
		
	}



	private static void mapStreamExample() {
		List<Integer> list = Arrays.asList(1,5,9,3,17,11,100,42);
		System.out.println(list.stream()
								.map(i -> Math.pow(i, 3))
								.collect(Collectors.toList()));
		// output [1.0, 125.0, 729.0, 27.0, 4913.0, 1331.0, 1000000.0, 74088.0]
		
		List<Child1> children = new ArrayList<Child1>();
		children.add(new Child1("red", 1));
		children.add(new Child1("blue", 5));
		children.add(new Child1("red", 6));
		children.add(new Child1("white", 7));
		children.add(new Child1("red", 9));
		children.add(new Child1("black", 2));
		children.add(new Child1("red", 2));
		children.add(new Child1("blue", 9));
		
		int sum = children.stream()
				.filter(child -> child.getColor().equalsIgnoreCase("Red") && child.getMarks() > 5)
				.mapToInt(child -> child.getMarks())
				.sum();
		System.out.println("->->"+sum);// prints 15
		
	}



	private static void anagramCheck(String string, String string2) {
		char[] s1 = string.toCharArray();
		char[] s2 = string2.toCharArray();
		Arrays.sort(s1);
		Arrays.sort(s2);
		if(Arrays.equals(s1, s2)) {
			System.out.println("anagram");
		}
		else {
			System.out.println("not an anagram");
		}
	}

	private static void bubbleSort(int[] arr) {
		for(int i=0;i<arr.length;i++) {
			for(int j=0;j<arr.length-1;j++) {
				if(arr[j]>arr[j+1]) {
					int temp = arr[j+1];
					arr[j+1]=arr[j];
					arr[j]=temp;
				}
			}
		}
		for(int a:arr) {
			System.out.println(a);
		}
	}

	private static void checkIfArmstrongNumber(int i) {
		int sumOfIndividualDigitCubes = 0;
		int copy = i;
		int temp = copy;
		int numberOfDigits = 0;
		while(temp!=0) {
			temp/=10;
			numberOfDigits++;
		}
		System.out.println("numberOfDigits->"+numberOfDigits);
		while(copy!=0) {
			int digit = copy%10;
			sumOfIndividualDigitCubes += Math.pow(digit, numberOfDigits);
			System.out.println("sumOfIndividualDigitCubes->"+sumOfIndividualDigitCubes);
			copy/=10;
		}
		
		if(sumOfIndividualDigitCubes==i) {
			System.out.println("Armstrong number");
		}
		else {
			System.out.println("Not an Armstrong number");
		}
		
	}

	private static void generateFibbonacci(int numberOfTerms) {
		int a,b,c;
		System.out.println("->->"+"1");
		System.out.println("->->"+"1");
		a=1;
		b=1;
		for(int i=3;i<=numberOfTerms;i++) {
			System.out.println("->->"+(a+b));
			c=b;
			b=a+b;
			a=c;
		}
		
	}
	
	
	
}
