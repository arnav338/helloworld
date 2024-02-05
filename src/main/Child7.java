package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class Child7 extends Child6 {
	
	public static void main(String args[]) {
		
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(1);
		list.add(null);
		list.add(null);
		list.add(null);
		System.out.println(list);
		
		Set<Integer> set = new HashSet<>();
		set.add(1);
		set.add(1);
		set.add(null);
		System.out.println("--"+set);
		
		Hashtable<String, Integer> ht = new Hashtable<>();
		ht.put("test", 1);
		// neither key nor object can be null
		//ht.put("test1", null);
		//ht.put(null, 4);
		System.out.println(ht);
		
		String a ="abcdcba";
		
		StringBuilder s = new StringBuilder(a);
		s.reverse();
		String rev = s.toString();
		if(rev.equals(a)) {System.out.println("true");}
		else {System.out.println("false");}
		
		
		String b = "";
		
		for(int i=a.toCharArray().length-1;i>=0;i--) {
			b+=a.toCharArray()[i];
		}
		System.out.println("b->"+b);
		if(b.equals(a)) {System.out.println("true");}
		else {System.out.println("false");}
		
		
		
	}
	
	
}
