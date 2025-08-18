import java.util.concurrent.atomic.AtomicReference;

public class Node {

    private int value;

    private AtomicReference<Node> next;

    public Node(int value) {
        this.value = value;
        next = new AtomicReference<>(null);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public AtomicReference<Node> getNext() {
        return next;
    }

    /**
     * Returns false if not atomic.
     * If next is null it's fine
     * Means another thread changed the value of next
     * @param expected expected node
     * @param newNode new node to be set
     * @return false if we can't set the next, another thread set next.
     */
    public boolean atomicSetNext(Node expected, Node newNode) {
        // not thread safe, multiple threads can enter if (next ..
        // fix has to make it AtomicReference(null)
        //        if (next == null) {
       //            next = new AtomicReference<>(newNode);
      //            return true;
      //        }

        return next.compareAndSet(expected, newNode);
    }
}
