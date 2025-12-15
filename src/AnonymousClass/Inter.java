package AnonymousClass;

import java.util.Collections;

public interface Inter {

    default void show_() {
        System.out.println("Thread-safe default method.");
    }

    static void show() {
        System.out.println("Thread-safe default method.");
    }

    void print();
}
