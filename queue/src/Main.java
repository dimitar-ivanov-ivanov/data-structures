public class Main {
    public static void main(String[] args) {
        MyQueue<Integer> q = new MyQueue<>(Integer.class);

        for (Integer i = 0; i < 10; i++) {
            q.add(i);
        }

        System.out.println(q.peak());

        for (int i = 0; i < 3; i++) {
            System.out.println(q.poll());
        }

        System.out.println(q.toString());
    }
}