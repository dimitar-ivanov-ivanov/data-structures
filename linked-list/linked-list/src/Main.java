public class Main {
    public static void main(String[] args) {
        LinkedList<Integer> linkedList = new LinkedList<>(1);
        for (int i = 2; i <= 10 ; i++) {
            linkedList.add(i);
        }
        for (Integer val: linkedList) {
            System.out.println(val);
        }
    }
}