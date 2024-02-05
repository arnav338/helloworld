package consumer_supplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConsumerTest {
	
	//Consumer interface has two methods:
	//void accept(T t);
	//default Consumer<T> andThen(Consumer<? super T> after);
	
	public static void main(String args[]) {
		List<String> list = new ArrayList<>(); 
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("d");
		list.add("e");
		
		// providing implementation of void accept(T t) of consumer class
		Consumer<List<String>> print = a -> System.out.println("Round "+a);
		
		Consumer<List<String>> printCaps = b -> b.forEach(System.out::println);;
		
		//print.accept(list);
		
		
		//composing multiple consumer implementations 
		//to make a chain of consumers
		print.andThen(printCaps).accept(list);
	}
	
	
	
}
