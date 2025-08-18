import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An unbounded thread-safe queue based on linked nodes. This queue orders elements FIFO (first-in-first-out).
 * The head of the queue is that element that has been on the queue the longest time.
 * The tail of the queue is that element that has been on the queue the shortest time.
 * New elements are inserted at the tail of the queue, and the queue retrieval operations obtain elements at the head of the queue.
 */
public class MyConcurrentLinkedQueue {

    private AtomicReference<Node> head;
    private AtomicReference<Node> tail;

    private AtomicLong count = new AtomicLong(0);

    public MyConcurrentLinkedQueue() {
        Node dummy = new Node(-1);
        this.head = new AtomicReference<>(dummy);
        this.tail = new AtomicReference<>(dummy);
    }

    public boolean offer(int value) {
        Node newNode = new Node(value);
        while (true) {
            Node currentTail = tail.get();
            AtomicReference<Node> nextRef = currentTail.getNext();
            Node next = nextRef.get();

            if (next == null) {
                // We're at the actual tail - try to add our new node
                if (nextRef.compareAndSet(null, newNode)) {
                    // Successfully added the node, now move the tail
                    tail.compareAndSet(currentTail, newNode);
                    count.getAndIncrement();
                    return true;
                }
            } else {
                // Another thread added a node but hasn't moved tail yet
                // Help move the tail pointer forward
                //tail.compareAndSet(currentTail, next);  // Use 'next' directly, not 'next.get()'
            }
        }
    }

    public Integer peek() {
        if (head == null) {
            return null;
        }
        return head.get().getValue();
    }

    public Integer poll() {
        if (head == null) {
            return null;
        }

        int val = head.get().getValue();
        head = head.get().getNext();
        count.getAndDecrement();
        return val;
    }

    public Long size() {
        return count.get();
    }
}
