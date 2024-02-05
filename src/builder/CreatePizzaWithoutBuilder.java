package builder;

public class CreatePizzaWithoutBuilder {
	
	public static void main(String[] args) {
		PizzaWithoutBuilder p = new PizzaWithoutBuilder("normal", 
										"red", "onion");
		/*
		 * Problem with this approach is there is no customization
		 * for every new combination we have to create a new constructor
		 * 
		 * 
		 * */
		System.out.println(p.getBase());
		System.out.println(p.getSauce());
		System.out.println(p.getTopping()); 
	}
	
}



