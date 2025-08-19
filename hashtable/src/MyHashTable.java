import java.lang.reflect.Array;
import java.util.LinkedList;

public class MyHashTable<K, V> {

    private static final int DEFAULT_CAPACITY = 8;

    private static final double CAPACITY_PERCENTAGE = 0.75;

    private LinkedList<Pair<K, V>>[] buckets;

    private int capacity;

    private int size;

    public MyHashTable() {
        capacity = DEFAULT_CAPACITY;
        buckets = (LinkedList<Pair<K, V>>[])Array.newInstance(LinkedList.class, DEFAULT_CAPACITY);
    }

    public synchronized V get(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        int hashcode = getHashcode(key);

        for (Pair<K, V> pair: buckets[hashcode]) {
            if (pair.getKey().equals(key)) {
                return pair.getValue();
            }
        }

        return null;
    }


    public synchronized void put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        int hashcode = getHashcode(key);

        if (buckets[hashcode] == null) {
            buckets[hashcode] = new LinkedList<>();
        }

        if (capacity * CAPACITY_PERCENTAGE >= size + 1) {
            grow();
        }

        buckets[hashcode].add(new Pair<>(key, value));
        size++;
    }

    private int getHashcode(K key) {
        return key.hashCode() % capacity;
    }

    private void grow() {
        capacity *= 2;
        LinkedList<Pair<K, V>>[] newBuckets = (LinkedList<Pair<K, V>>[])Array.newInstance(LinkedList.class, capacity);

        for (int i = 0; i < buckets.length; i++) {
            newBuckets[i] = new LinkedList<>();
            // if the bucket is not initalized then addAll will throw an exception
            if (buckets[i] != null) {
                newBuckets[i].addAll(buckets[i]);
            }
        }

        buckets = newBuckets;
    }

    class Pair<K, V> {

        private K key;

        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
