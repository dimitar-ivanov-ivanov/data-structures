import java.util.Hashtable;

public class Main {
    public static void main(String[] args) {
        Hashtable<Integer, Integer> h = new Hashtable<>();
        h.put(1, 1);

        System.out.println(h.get(1));

        MyHashTable<Integer, Integer> h1 = new MyHashTable<>();
        h1.put(1, 1);

        System.out.println(h1.get(1));
    }
}