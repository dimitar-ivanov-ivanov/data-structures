import java.util.LinkedList;

public class MySet<K> {

    private static final double LOAD_FACTOR = 0.75;

    private LinkedList<K>[] buckets;

    private int capacity = 8;

    private int countElements;

    public MySet() {
        buckets = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    public void add(K key) {
        int hash = getHash(key);
        // check for duplicate
        if (buckets[hash].contains(key)) {
            throw new IllegalArgumentException("Key " + key + " already present in Set.");
        }

        // resize if needed
        if (countElements + 1 > capacity * LOAD_FACTOR) {
            resize();
        }

        buckets[hash].add(key);
        countElements++;
    }

    public boolean contains(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Input key is null");
        }
        
        int hash = getHash(key);
        for (K k : buckets[hash]) {
            if (k.equals(key)) {
                return true;
            }
        }
        
        return false;
    }

    private void resize() {
        capacity *= 2;
        LinkedList<K>[] newBuckets = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            newBuckets[i] = new LinkedList<>();
        }

        for (int i = 0; i < buckets.length; i++) {
            for(K key : buckets[i]) {
                int hash = getHash(key);
                newBuckets[hash].add(key);
            }
        }
        
        buckets = newBuckets;
    }

    private int getHash(K key) {
        return Math.abs(key.hashCode() % capacity);
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < capacity; i++) {
            builder.append("Bucket: " + i + " ");
            for(K key : buckets[i]) {
                builder.append(key + " ");
            }
            builder.append("\n");
        }
        
        return builder.toString();
    }
}
