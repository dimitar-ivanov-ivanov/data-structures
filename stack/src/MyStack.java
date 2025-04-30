import java.lang.reflect.Array;

public class MyStack<T> {

    private int capacity = 8;

    private int countElements;

    private Class<T> type;

    private T[] array;

    private int tailIndex;

    public MyStack(Class<T> type) {
        this.type = type;
        array = (T[])Array.newInstance(type, capacity);
    }

    public void push(T element) {
        if (countElements + 1 == capacity) {
            resize();
        }

        array[tailIndex] = element;
        tailIndex++;
        countElements++;
    }

    public T pop() {
        if (countElements == 0) {
            throw new IllegalStateException("Stack is empty, cannot pop from it.");
        }

        T elementToPop = array[tailIndex - 1];
        tailIndex--;
        countElements--;
        return elementToPop;
    }

    public T peek() {
        if (countElements == 0) {
            throw new IllegalStateException("Stack is empty, cannot pop from it.");
        }
        return array[tailIndex - 1];
    }

    private void resize() {
        capacity *= 2;
        T[] newArray = (T[])Array.newInstance(type, capacity);

        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }

        array = newArray;
    }

    public boolean isEmpty() {
        return countElements == 0;
    }

    public int size() {
        return countElements;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = tailIndex - 1 ; i >= 0; i--) {
            builder.append(array[i] + " ");
        }
        return builder.toString();
    }
}
