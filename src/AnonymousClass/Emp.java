package AnonymousClass;

import java.util.*;
import java.util.stream.Collectors;

public class Emp {
    static class Employee{
        public int id;

        public Employee(int id, String name) {
            this.id = id;
            this.name = name;
        }

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

        public String name;

        @Override
        public String toString() {
            return "Employee{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            //return ((Employee) o).getId() == this.getId() && ((Employee) o).getName().equals(this.getName());
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Employee employee = (Employee) o;
            return id == employee.id && name.equalsIgnoreCase(employee.name);
        }
    }

    public static void main(String[] args) {
        Employee e1 = new Employee(1,"ab");
        Employee e2 = new Employee(2,"fd");
        Employee e3 = new Employee(1,"ab");

        List<Employee> el = new ArrayList<>(Arrays.asList(e1,e2,e3));

        List<Employee> collect = el.stream().distinct().toList();

        System.out.println("l - "+collect);

    }
}
