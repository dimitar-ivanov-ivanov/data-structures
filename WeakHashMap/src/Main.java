import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;

public class Main {
    public static void main(String[] args) {
        int[] a = new int[10];
        Arrays.fill(a, 10);


        Person p = new Person();
        String personValue = "1";
        WeakHashMap<Person, String> personMap = new WeakHashMap<>();
        personMap.put(p, personValue);

        p = null; // the pointer strong pointer to key is null
        // meaning the String object is eligible for garbage collection

        for (int i = 0; i < 3; i++) {
            System.gc();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(personMap.isEmpty());

        String key = "Mitko"; // creates and puts this string in the string pool
        String val = "1";
        WeakHashMap<String, String> m = new WeakHashMap<>();
        m.put(key, val); // the weakhashmap holds a reference to the string in the string pool

        key = null;

        for (int i = 0; i < 3; i++) {
            System.gc();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // should be empty
        System.out.println(m.isEmpty());
    }

    static class Person {

    }
}
