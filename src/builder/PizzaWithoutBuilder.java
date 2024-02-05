package builder;

public class PizzaWithoutBuilder {
	
	private String base;
	private String sauce;
	private String topping;
	/*
	 * If we go with initializing the object using constructor
	 * We need to take into consideration all permutations,i.e.,
	 * pizza w/o topping 
	 * pizza w/o sauce
	 * etc (Imagine having more choices like sides,beverage)
	 * 
	 * This way we will have to construct constructors for all 
	 * permutations
	 * 
	 * Setters are not there as class is to be made immutable
	 * 
	 * */
	public PizzaWithoutBuilder(String base, String sauce, String topping) {
		this.base = base;
		this.sauce = sauce;
		this.topping = topping;
	}
	public PizzaWithoutBuilder(String base, String sauce) {
		this.base = base;
		this.sauce = sauce;
	}
	public String getBase() {
		return base;
	}
	public String getSauce() {
		return sauce;
	}
	public String getTopping() {
		return topping;
	}	
}
