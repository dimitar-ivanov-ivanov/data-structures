public class Main {
    public static void main(String[] args) {
        MyConcurrentLinkedQueue q = new MyConcurrentLinkedQueue();
        //singleThreaded(q);

        Thread t1 = new Thread(() -> {
           q.offer(1);
           q.offer(2);
        });

        Thread t2 = new Thread(() -> {
            q.offer(3);
            q.offer(4);
        });

        Thread t3 = new Thread(() -> {
            q.offer(5);
            q.offer(6);
            q.offer(7);
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }

        Long size = q.size();
        for(int i = 0; i < size; i++) {
            System.out.println(q.poll());
        }
    }

    private static void singleThreaded(MyConcurrentLinkedQueue q) {
        q.offer(1);
        System.out.println(q.peek());

        q.offer(2);
        System.out.println(q.peek());

        q.poll();
        System.out.println(q.peek());
        q.offer(3);
        q.offer(4);
        q.offer(5);

        System.out.println(q.poll());
        System.out.println(q.poll());
        System.out.println(q.poll());
        System.out.println(q.poll());
        System.out.println(q.poll());
    }
}