import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

public class PerformanceTest {

    public static void main(String[] args) {
        //arrayVsListReadPerformanceTest(50_000); // 13 ms for both
        //arrayVsListReadPerformanceTest(500_000); // 11 ms for array, 17 for list
        //arrayVsListReadPerformanceTest(1500_000); // 11 ms for array, 15 for list
        ///arrayVsListReadPerformanceTest(10_000_000); // 127 ms for array, 124 for list, Second run 171 array, 119 list
        //arrayVsListReadPerformanceTest(25_000_000); // 166 ms for array, 173 for list
        //arrayVsListReadPerformanceTest(50_000_000); // 335 ms for array, 294 for list, Second run 367 array 371 list
    }

    private static void arrayVsListReadPerformanceTest(int elements) {
        Integer[] arr = new Integer[elements];
        java.util.List<Integer> list = new ArrayList<>();
        Random rand = new SecureRandom();
        rand.nextInt();
        for (int i = 0; i < arr.length; i++) {
            Integer num = rand.nextInt();
            arr[i] = num;
            list.add(num);
        }
        System.out.println("Start of test, iterate over " + elements + " elements and compare array and list performance");

        // read array
        Long sumArr = 0L;
        long start = System.currentTimeMillis();
        for (int i = 0; i < elements; i++) {
            sumArr += arr[i];
        }

        long duration = System.currentTimeMillis() - start;

        System.out.println("Reading from Array duration in ms: " + duration);
        // read list
        Long sumList = 0L;
        start = System.currentTimeMillis();
        for (int i = 0; i < elements; i++) {
            sumArr += list.get(i);
        }

        duration = System.currentTimeMillis() - start;
        System.out.println("Reading from List duration in ms: " + duration);
    }
}
