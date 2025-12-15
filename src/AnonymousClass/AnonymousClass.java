package AnonymousClass;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnonymousClass {
	// Conventional approach
	// without using anonymous class
	public interface Abc{
        public default void show(String a) {
			System.out.println("abc");
        }
    }

	public interface Abc_1{
        public default void show(String a) {
			System.out.println("abc_1");
        }
    }

	public static class A implements Abc,Abc_1{

		@Override
		public void show(String a) {
			Abc.super.show(a);
			throw new RuntimeException();
		}
	}
	
	public class AbcImpl implements Abc{
		@Override
		public void show(String a) {
			System.out.println(a);
		}
	}
	
	public static void main(String args[]) {
		AbcImpl a = new AnonymousClass().new AbcImpl();
		a.show("Hi");


		// Lets say we have a list of strings
		List<String> list = new ArrayList<>(Arrays.asList("Hello","World","Good","Good"));

		// if we want to join all strings with a delimiter,we can use Collectors.joining
		String s = list.stream().collect(Collectors.joining(","));
		System.out.println("after join operation - "+s);

		// if we only want distinct elements but in a list instead of a set, then we can use distinct method
		List<String> distinctList = list.stream().distinct().toList();
		System.out.println("distinct - "+distinctList);

		// if we want to collect the list as a map having key as first character of string and value as the string itself
		// we can use the following : Collectors.toMap(b -> String.valueOf(b.charAt(0)), Function.identity())
		Map<String,String> map = distinctList.stream().collect(Collectors.toMap(b -> String.valueOf(b.charAt(0)), Function.identity()));
		System.out.println(map);

		// in the above scenario, what if 2 duplicate strings are there in the list
		// for that we provide a mapper function, which takes in 2 values which are the 2 duplicate values which are present in the list
		// in this mapper function we provide the logic on how to resolve the conflict that has arised from these 2 duplicate values
		Map<String,String> map_ = list.stream().collect(Collectors.toMap(b -> String.valueOf(b.charAt(0)), Function.identity(), (p,q) -> q));
		System.out.println(map_);





	}
	
}
