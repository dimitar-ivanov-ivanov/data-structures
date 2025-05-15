import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MyList<Integer> list = new MyList<>(Integer.class);
        int threads = 8;

        ExecutorService executorService = Executors.newFixedThreadPool(threads);

        Runnable r = () -> {
            for (int i = 0; i < 50; i++) {
                list.add(i);
            }
        };

        try {
            for (int i = 0; i < threads; i++) {
                executorService.submit(r);
            }
        } finally {
            executorService.shutdown();
            try {
                executorService.awaitTermination(10, TimeUnit.SECONDS);
                if (!executorService.isTerminated()) {
                    System.out.println("Forcing Executor shutdown");
                    executorService.shutdownNow();
                }

            } finally {
                if (!executorService.isTerminated()) {
                    executorService.shutdownNow();
                }
            }
        }

        System.out.println(list);
    }
}