public class List<T> {

    private static final int INITIAL_SIZE = 8;

    private T[] elements;
    private int size;

    private int maxSize = INITIAL_SIZE;

    public List() {
        elements = (T[])new Object[INITIAL_SIZE];
    }

    public List(T[] inputElements) {
        if (inputElements.length > INITIAL_SIZE) {
            maxSize = inputElements.length + INITIAL_SIZE;
        }
        elements = (T[])new Object[maxSize];
        for (int i = 0; i < inputElements.length; i++) {
            elements[i] = inputElements[i];
        }
        size = inputElements.length;
    }

    public int getSize() {
        return size;
    }

    public T get(int index) {
        return elements[index];
    }

    /**
     * Add element to the end of the array.
     * If the new element would exceed the size make a new array and copy the elements of the existing array there.
     * The new array should be twice as bigger as the exceeded one.
     * @param element element to be added
     */
    public void add(T element) {
        if (size == maxSize) {
            maxSize = maxSize * 2;
            T[] newElements = (T[])new Object[maxSize];

            for (int i = 0; i < elements.length; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
            elements[size] = element;
            size++;
            return;
        }

        elements[size] = element;
        size++;
    }

    public void insert(T element, int index) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("Index is out of bounds");
        }

        int newElementsIndex = 0;
        T[] newElements = (T[])new Object[size + 1];

        // Get the elements before the index where we want to insert in an array
        T[] elementsBefore = (T[])new Object[index];
        for (int i = 0; i < index; i++) {
            elementsBefore[i] = elements[i];
            newElements[newElementsIndex++] = elements[i];
        }

        newElements[newElementsIndex++] = element;

        // Get the elements after in another array
        T[] elementsAfter = (T[])new Object[size - index];
        for (int i = index, j =0; i < size; i++,j++) {
            elementsAfter[j] = elements[i];
            newElements[newElementsIndex++] = elements[i];
        }

        elements = newElements;
        size++;
    }

    public void remove(T element) {
        // Find first of element in array
        // if element is not found -> not found exception
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                remove(i);
                return;
            }
        }

        throw new IllegalArgumentException("Element is not part of list.");
    }

    public void remove(int index) {
        // if element is found move the elements after the element to one index before them
        //  -> a bit better than making new arrays
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("Index is out of bounds");
        }

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        size--;
    }
}
