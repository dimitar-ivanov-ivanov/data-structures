import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Random;

public class PerformanceTest {
    public static void main(String[] args) {
        //compareArrayAndLinkedList(50_000); // Arr 5 ms List 8ms
        //compareArrayAndLinkedList(250_000); // Arr 6 ms List 4ms
        //compareArrayAndLinkedList(500_000); // Arr 8 ms List 25ms
        //compareArrayAndLinkedList(1_000_000); // Arr 8 ms List 28ms
        //compareArrayAndLinkedList(10_000_000); // Arr 32 ms List 79ms
        //compareArrayAndLinkedList(25_000_000); // Arr 72 ms List 240ms
        //compareArrayAndLinkedList(50_000_000); // Arr 154 ms List 437ms
    }

    private static void compareArrayAndLinkedList(int size) {
        Integer[] arr = new Integer[size];
        java.util.LinkedList<Integer> linkedList = new LinkedList();

        Random rand = new SecureRandom();

        for (int i = 0; i < size; i++) {
            Integer num = rand.nextInt();
            arr[i] = num;
            linkedList.add(num);
        }

        System.out.println("Begin performance test of reading: Array VS Linked List");

        long start = System.currentTimeMillis();
        long sumArr = 0;
        for (int i = 0; i < size; i++) {
            sumArr += arr[i];
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("Reading in array took " + duration);

        start = System.currentTimeMillis();
        long sumL = 0;
        for (Integer val : linkedList) {
            sumL += val;
        }

        duration = System.currentTimeMillis() - start;
        System.out.println("Reading in linked list took " + duration);
        
    }
}
