package AnonymousClass;

public class AnonymousClass {
	// Conventional approach
	// without using anonymous class
	public interface Abc{
		public void show(String a);
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
	}
	
}
