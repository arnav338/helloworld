package builder;

public class PizzaWithBuilder {
	
	private String base;
	private String sauce;
	private String topping;
	
	public static class Pizza{
		private String inbase;
		private String insauce;
		private String intopping;
		public Pizza() {		}
		public Pizza base(String base) {
			this.inbase = base;
			return this;
		}
		public Pizza sauce(String sauce) {
			this.insauce = sauce;
			return this;
		}
		public Pizza topping(String topping) {
			this.intopping = topping;
			return this;
		}
		public PizzaWithBuilder create() {
			return new PizzaWithBuilder(this);
		}
	}
	public PizzaWithBuilder(Pizza p) {
		this.base = p.inbase;
		this.sauce = p.insauce;
		this.topping = p.intopping;
	}
	public PizzaWithBuilder() { }
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
