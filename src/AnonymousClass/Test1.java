package AnonymousClass;


import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test1 {
    static {
        Student.print();
    }

    static class Student{
        static void print(){
            System.out.println("Hello");
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ThreadLocal<SimpleDateFormat> t = new ThreadLocal<>() {

        };

    }

}
