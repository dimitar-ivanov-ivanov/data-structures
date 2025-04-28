public class Main {
    public static void main(String[] args) {
        MySet set = new MySet();
        for (int i = 0; i < 32; i++) {
            set.add(i);
        }

        throwExpectedException(set);

        System.out.println(set.toString());
    }

    private static void throwExpectedException(MySet set) {
        try {
            set.add(1);
            set.add(1);
        } catch (IllegalArgumentException ex) {
            System.out.println("IllegalArgument exception thrown as expected.");
        }
    }
}