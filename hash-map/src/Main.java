public class Main {
    public static void main(String[] args) {
        MyHashMap<Integer, Integer> map = new MyHashMap<>();


        for (int i = 0; i < 30; i++) {
            map.put(i, i);
        }

        System.out.println(map.toString());

        throwExpectedException(map);
    }

    private static void throwExpectedException(MyHashMap<Integer, Integer> map) {
        try {
            map.put(1, 1);
            map.put(1,1);
        } catch (IllegalArgumentException ex) {
            System.out.println("Threw exception for IllegalArgument as expected");
        }
    }
}