public class Main {
    public static void main(String[] args) {

        List<Integer> l = new List(new Integer[]{-1,-2,-3});
        for (int i = 1; i <= 20; i++) {
            l.add(i);
        }

        l.insert(100, 13);
        printList(l);
        System.out.println("Removing element at 8 index");
        l.remove(8);
        printList(l);
        System.out.println("Removing element equal to 12");
        l.remove(Integer.valueOf(12));
        printList(l);
        System.out.println("Adding element at the end");
        l.add(Integer.valueOf(1000));
        printList(l);
    }

    private static void printList(List<Integer> l) {
        for (int i = 0; i < l.getSize(); i++) {
            System.out.print(l.get(i) + " ");
        }
        System.out.println();
    }
}