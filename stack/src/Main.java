public class Main {
    public static void main(String[] args) {
        MyStack<Integer> s = new MyStack<>(Integer.class);

        for (int i = 0; i < 10; i++) {
            s.push(i);
        }

        System.out.println(s.toString());

        System.out.println(s.pop());

        System.out.println(s.pop());

        System.out.println(s.peek());

        System.out.println(s.toString());
    }
}