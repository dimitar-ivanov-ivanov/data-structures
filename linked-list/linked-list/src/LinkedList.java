import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<T> implements Iterable<T> {

    // Head and Tail should NOT be exposed
    // Otherwise outside clients can change their next nodes and rearrange the list.
    // So to iterate the list we need a LinkedListIterator and the list has to implement Iterable<T>
    private Node<T> head;

    // Kepp a reference to tail
    // Otherwise we'll have to traverse the list every time we want to add a new value
    private Node<T> tail;

    private int size;

    public LinkedList(T val) {
        this.head = new Node(val);
        this.tail = new Node<>(val);
        size = 1;
    }

    public void add(T value) {
        // we removed all of the elements from the list
        if (head == null || tail == null) {
            this.head = new Node(value);
            this.tail = new Node<>(value);
            size = 1;
            return;
        }
        if (size == 1) {
            Node newNode = new Node(value);
            tail = newNode;
            head.setNext(tail);
            size++;
            return;
        }

        Node newNode = new Node(value);
        tail.setNext(newNode);
        tail = newNode;
        size++;
    }

    public void add(T value, int index) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        Node node = head;
        int i = 0;
        while (i < index) {
            node = node.getNext();
            i++;
        }

        Node newNode = new Node(value);
        newNode.setNext(node.getNext());
        node.setNext(newNode);
        size++;
    }

    public void remove(int index) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        if (index == 0 && size == 1) {
            head = null;
            tail = null;
            size = 0;
            return;
        }
        // Keep the previous node before the node you want to remove
        // The next for the previous node will become the next node for the node we want to remove
        // this will cuase the node we want to remove to be garbage collected and removed!
        Node previousNode = head;
        Node node = previousNode.getNext();
        int i = 0;
        while (i < index) {
            previousNode = node;
            node = node.getNext();
            i++;
        }

        previousNode.setNext(node.getNext());
        size--;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {

        private Node<T> current;

        public LinkedListIterator() {
            this.current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("End reached");
            }
            T value = current.getValue();
            current = current.getNext();
            return value;
        }
    }
}


