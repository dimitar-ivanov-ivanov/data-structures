import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        MyConcurrentHashMap<Integer, Integer> concurrentHashMap = new MyConcurrentHashMap<>();
        ExecutorService executorService = null;

        try {
            int threads = 8;
            executorService = Executors.newFixedThreadPool(threads);

            SecureRandom random = new SecureRandom();
            Runnable runnable = () -> {
                for (int i = 0; i < 80; i++) {
                    try {
                        int key = random.nextInt(i + 10);
                        concurrentHashMap.put(key, i);
                    } catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            };

            for (int i = 0; i < threads; i++) {
                executorService.submit(runnable);
            }
            // we need a buffer of time from when we start the threads to when we stop them
            // otherwise the executor can shutdown a thread that just acquired a lock but didn't release it
            // this will cause other threads to get block and the app won't be able to exit
            // even if you do shutdown or shutdownNow blocked threads won't allow the app to exit

        } finally {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(15, TimeUnit.SECONDS)) {
                    System.out.println("Executor did not terminate in the given time");
                    executorService.shutdownNow();
                }
            } catch (InterruptedException ex) {
                executorService.shutdownNow();
            }
            System.out.println(concurrentHashMap);
        }
    }
}