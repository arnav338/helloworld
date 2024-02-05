package Serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serialize {
	
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		Bean b = new Bean(1,"ram",100);
		FileOutputStream fos = new FileOutputStream(new File("test.txt"));
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(b);
		oos.close();
		
		FileInputStream fis = new FileInputStream("test.txt");
		ObjectInputStream ois = new ObjectInputStream(fis);
		Bean b1 = (Bean) ois.readObject();
		ois.close();
		System.out.println("--"+b1);
		System.out.println("--"+Bean.a);
	}

}
