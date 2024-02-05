package streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Test1 {
	//static Map<? extends Object,? extends Object> res;
	public static void main(String[] args) {
		
		test();
		
		ArrayList<User> list = new User().getData();
		
		//Map<Double, List<User>> res = list.stream().collect(Collectors.groupingBy(User::getSalary));
		//Map<String, List<User>> res = list.stream().collect( Collectors.groupingBy( User::getRole ) );
		
		//Map<String, Double> res = list.stream().collect(Collectors.groupingBy(User::getRole,Collectors.summingDouble(User::getSalary)));
		Map<String, Double> res = list.stream().collect(Collectors.groupingBy(User::getRole,Collectors.averagingDouble(User::getSalary)));
		// 1st parameter of groupingBy decides the type of key of resultant HashMap
		// here the parameter is an identity function, so key will be same as the type of arraylist
		//Map<User, Long> res = list.stream().collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
		
		//Map<User, Long> res = list.stream().filter(c -> c.getSalary()>5000).collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
		
		res.keySet().stream().forEach(b -> System.out.println(b + " : " + res.get(b)));
		
		//res.keySet().stream().forEach(b -> System.out.println((b=="" ? "Unassigned" : b) +" : "+res.get(b)));
		
	}
	static void test() {
		Map<? extends Object,? extends Object> res;
		List<String> list = Arrays.asList("abc","xyz","xyz","xac","xbvc");
		
		Map<String,Long> res1 = list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		res1.keySet().stream().forEach(a -> System.out.println(a + " : " + res1.get(a)) );
		System.out.println("=================================");
		// creating a custom static function to group the elements according to 1st character
		res = list.stream().collect(Collectors.groupingBy(Test1::cutString , Collectors.counting()));
		 
		res.keySet().forEach(c -> System.out.println(c + " : " + res.get(c)) );
	}
	static String cutString(String a) {
		return a.substring(0, 1);
	}
}
