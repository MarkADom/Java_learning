package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrintService<T> {

   /** GENERICS
    * Class Parameterized with type T can be anything (int, double, string, object,...)
    * Allow the reuse, we can use the PrintService for any type, and allows the type safety.
    **/

    private List<T> list = new ArrayList<>();

    public void addValue(T value) {
        list.add(value);
    }

    public T first() {
        if (list.isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        return list.get(0);
    }

    public void print() {
        System.out.print("[");
        if (!list.isEmpty()) {
            System.out.print(list.get(0));
        }
        for (int i = 1; i < list.size(); i++) {
            System.out.print(", " + list.get(i));
        }
        System.out.println("]");
    }
}
