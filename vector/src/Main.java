import java.util.Vector;

public class Main {
    public static void main(String[] args) {

        Vector<Integer> v3 = new Vector<>();
        v3.contains(1);


        MyVector<Integer> v = new MyVector<>(Integer.class);

        for (int i = 0; i < 10; i++) {
            v.add(i);
            System.out.println(v.get(i));
        }

        MyVector<Integer> v2 = new MyVector<>(Integer.class);
        v2.add(100);
        v.addAll(v2);

        System.out.println("Size:" + v.size());
    }
}