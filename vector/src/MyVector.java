import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MyVector<T> implements Collection<T> {

    private static final int DEFAULT_CAPACITY = 8;

    T[] arr;

    int index;

    int capacity;

    Class<T> clazz;

    public MyVector(Class<T> clazz) {
        this.clazz = clazz;
        capacity = DEFAULT_CAPACITY;
        arr = (T[])Array.newInstance(clazz, DEFAULT_CAPACITY);
    }

    public synchronized int size() {
        return index;
    }

    @Override
    public synchronized boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public synchronized boolean contains(Object o) {
        for (int i = 0; i < index; i++) {
            if (arr[i].equals(o)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Collection.super.forEach(action);
    }

    @Override
    public Object[] toArray() {
        T[] cleanArr = (T[])Array.newInstance(clazz, index);
        for (int i = 0; i < index; i++) {
            cleanArr[i] = arr[i];
        }
        return cleanArr;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public <T1> T1[] toArray(IntFunction<T1[]> generator) {
        return Collection.super.toArray(generator);
    }

    public synchronized T get(int indexOfElementToFind) {
        if (indexOfElementToFind >= this.index) {
            throw new IllegalArgumentException("Index cannot be bigger than capacity of array: " + index);
        }

        return arr[indexOfElementToFind];
    }

    public synchronized boolean addAll(Collection<? extends T> collection) {
        for (T el : collection) {
            add(el);
        }

        return true;
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public synchronized boolean removeIf(Predicate<? super T> filter) {
        return Collection.super.removeIf(filter);
    }

    @Override
    public synchronized boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public synchronized void clear() {
        arr = (T[])Array.newInstance(clazz, 1);
        index = 0;
        capacity = 1;

    }

    @Override
    public synchronized Spliterator<T> spliterator() {
        return Collection.super.spliterator();
    }

    @Override
    public Stream<T> stream() {
        return Collection.super.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return Collection.super.parallelStream();
    }

    public synchronized boolean add(T el) {
        if (index + 1 == capacity) {
            grow();
        }

        arr[index] = el;
        index++;
        return true;
    }

    @Override
    public synchronized boolean remove(Object o) {
        return false;
    }

    @Override
    public synchronized boolean containsAll(Collection<?> c) {
        for (Object el : c) {
            if (!contains(el)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Double the length of the array.
     * Move the elements from the old to the new array.
     * It's assumed that whoever enters this method has gotten a lock before.
     */
    private void grow() {
        T[] newArr = (T[])Array.newInstance(clazz, capacity * 2);

        for (int i = 0; i < arr.length; i++) {
            newArr[i] = arr[i];
        }

        capacity *= 2;
        arr = newArr;
    }
}
