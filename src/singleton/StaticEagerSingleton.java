package singleton;

public class StaticEagerSingleton {

	private StaticEagerSingleton() {
		super();
	}
	
	// Eager initialization
	// Adv - 
	// 1. We can handle exception
	// Disadv - 
	// 1. The object will always be created no matter whether it is required or not 
	// and hence there will be a wastage of heap memory in case we don’t need the instance of this singleton class.
	// 2. if there is exception during initialisation we cant handle that
	// as we cant enclose the initialisation with a try/catch block
	
	private static StaticEagerSingleton eagerSingleton = null;
	
	static {
		try {
			eagerSingleton = new StaticEagerSingleton();
		}catch(Exception e) {
			
		}
		
	}
	
	public static StaticEagerSingleton getSingleton() {
		return eagerSingleton;
	}
	
	
	
}

