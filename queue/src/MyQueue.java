import java.lang.reflect.Array;

/**
 * This isn't a REAL WORLD Implementation.
 * In the real world if the head is > 0 and we empty space at the start of the array.
 * We would use that empty space and tail will become 0,1,2 etc
 * We would resize when the tail meets the head not when the teail meets end of the array.
 * This is more optimal in terms of storage as we are not wasting space.
 * @param <T>
 */
public class MyQueue<T> {

    private int countElements;

    private int capacity = 8;

    private T[] data;

    private int headIndex;

    private int tailIndex;

    private Class<T> type;

    public MyQueue(Class<T> type) {
        this.type = type;
        data = (T[]) Array.newInstance(type, capacity);
    }

    public void add(T element) {
        if (element == null) {
            return;
        }

        if (tailIndex + 1 == capacity) {
            resize();
        }

        data[tailIndex++] = element;
        countElements++;


    }

    private void resize() {
        capacity *= 2;
        T[] newArray = (T[]) Array.newInstance(type, capacity);

        int i = 0;
        for (i = headIndex; i < tailIndex; i++) {
            newArray[i] = data[i];
        }

        // We want the head to start from 0 again after resizing
        // if we don't we're just wasting space
        headIndex = 0;
        tailIndex = i;
        data = newArray;
    }

    /**
     * Retrieve and remove the head of the queue.
     * If queue is empty, return null.
     * @return the head
     */
    public T poll() {
        if (countElements == 0) {
            return null;
        }

        T head = data[headIndex];
        headIndex = headIndex + 1;

        countElements--;
        return head;
    }

    /**
     * Retrieve the head of the queue.
     * @return the head
     */
    public T peak() {
        if (countElements == 0) {
            return null;
        }
        return data[headIndex];
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = headIndex; i < tailIndex; i++) {
            builder.append(data[i] + " ");
        }
        return builder.toString();
    }
}
