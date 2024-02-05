package streams;

import java.util.ArrayList;

public class User {
	private int id;
	private String name;
	private String role;
	private double salary;
	
	/*getters and setters*/
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	/*constructors*/
	public User(int id, String name, String role, double salary) {
		super();
		this.id = id;
		this.name = name;
		this.role = role;
		this.salary = salary;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", "
			+ "role=" + role + ", salary=" + salary + "]";
	}
	public User() { 	}
	/*utility method to load hard coded data*/
	public ArrayList<User> getData(){
		ArrayList<User> result = new ArrayList<User>();
		result.add(new User(1,"ravi","engineer",2000));
		result.add(new User(2,"ram","sales",3000));
		result.add(new User(3,"rahul","accounts",4000));
		result.add(new User(4,"kavi","engineer",5000));
		result.add(new User(5,"shyam","sales",6000));
		result.add(new User(6,"preeti","engineer",4000));
		result.add(new User(7,"simran","hr",5000));
		result.add(new User(8,"happy","accounts",4000));
		result.add(new User(9,"suresh","engineer",3000));
		result.add(new User(10,"ramesh","engineer",5000));
		result.add(new User(11,"romesh","engineer",5000));
		return result;
	}
	
	
}
