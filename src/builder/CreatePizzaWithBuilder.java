package builder;

public class CreatePizzaWithBuilder {
	
	public static void main(String[] args) {
		PizzaWithBuilder.Pizza p =  new PizzaWithBuilder.Pizza();
		
		//p.base("normal").sauce("red").topping("onion");
		//p.base("normal").topping("onion");
		p.base("normal");
		/*
		 * It solves the initial problem 
		 * we can choose to have or not to have any ingredient
		 * 
		 * */
		PizzaWithBuilder pizza = p.create();
		System.out.println(pizza.getBase());
		System.out.println(pizza.getSauce());
		System.out.println(pizza.getTopping()); 
	}
	
}
