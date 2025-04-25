import java.util.LinkedList;

public class MyHashMap<K,V> {

    private static final double LOAD_FACTOR = 0.75;

    private int countElements;

    private int capacity = 8;

    private LinkedList<Pair<K,V>>[] buckets;

    public MyHashMap() {
        buckets = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    public void put(K key, V value) {
        //System.out.println("Key=" + key);
        Pair<K,V> existingPair = get(key);
        if (existingPair != null) {
            throw new IllegalArgumentException("Key " + key + " already exists in map");
        }

        // check for resize
        if (countElements + 1 > capacity * LOAD_FACTOR) {
            resize();
        }

        int hash = getHash(key);
        buckets[hash].add(new Pair<>(key, value));
        countElements++;
    }

    // Average case O(1)
    // Worst case O(n), but that shouldn't happen because we'll resize before it does happen
    public Pair<K,V> get(K key) {
        int hash = getHash(key);

        for(Pair<K,V> pair: buckets[hash]) {
            if (pair.getKey().equals(key)) {
                return pair;
            }
        }

        return null;
    }

    private int getHash(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        // make new array with double the size
        capacity *= 2;
        LinkedList<Pair<K,V>>[] newArr = new LinkedList[capacity];

        // Initialize all buckets
        for (int i = 0; i < capacity; i++) {
            newArr[i] = new LinkedList<>();
        }

        for (int i = 0; i < buckets.length; i++) {
            for(Pair<K,V> pair : buckets[i]) {
                // rehash all entries
                // rehash because if we don't we'll have a imbalance in the buckets
                // eight buckets may have 5 elements and the rest have 0
                int hash = getHash(pair.getKey());
                // put all entries in new array
                newArr[hash].add(pair);
            }
        }

        buckets = newArr;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < capacity; i++) {
            builder.append("Bucket " + i +  ": ");
            for(Pair<K,V> pair: buckets[i]) {
                builder.append("K:" +pair.getKey() + " V:" +  pair.getValue() + " ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    private class Pair<K,V> {
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
