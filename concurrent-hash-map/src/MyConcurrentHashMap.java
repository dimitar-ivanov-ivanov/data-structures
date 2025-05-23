import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Implementation of Concurrent hash map.
 * Map consists of an array, where each element can be one or chain of nodes, I call one element of the array a bucket.
 * When a thread wants to write data we find the index of the data in the array using hashcode
 * Then the thread locks the bucket adds the data and unlocks.
 * In some cases the array will have to be resized when the data becomes too much
 * When we have to resize there is a separate lock for that, we need a separate lock and not just lock every bucket lock because that's not atomic
 * @param <K> key
 * @param <V> value
 */
public class MyConcurrentHashMap<K, V> {

    // Explicitly making the load factor very high so that we get collisions
    // getting collisions is good for testing the locking
    private static final double LOAD_FACTOR = 0.99;

    private static final int INITIAL_CAPACITY = 8;

    // volatile make changes in the map visible between threads
    // when a new node is added or we resize the change is immediately visible to any other threads
    // otherwise we'll have race conditions between get and push
    private volatile Node<K,V>[] map;

    // instead of using synchronized lock we acquire locks from this array
    // otherwise synchronize(map[index]) can lead to NPE and I don't want to initialize the nodes with default values
    // The read lock is aware of the write lock, you can't acquire the read lock if you are currently writing
    // Many threads(up to 2^16) can acquire the read lock so reading is not blocked
    private volatile ReentrantReadWriteLock[] locks;

    private final AtomicInteger capacity = new AtomicInteger(INITIAL_CAPACITY);

    private final AtomicInteger countElements = new AtomicInteger(0);

    // Explicit lock that stops multiple threads from resizing the array at the same time
    // Before I was locking the locks one by one BUT that's not atomic and there were race conditions and deadlocks
    private final ReentrantReadWriteLock resizeLock = new ReentrantReadWriteLock();

    public MyConcurrentHashMap() {
        map = new Node[capacity.get()];
        locks = new ReentrantReadWriteLock[capacity.get()];

        for (int i = 0; i < capacity.get(); i++) {
            locks[i] = new ReentrantReadWriteLock();
        }
    }

    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        if (isResizeNeeded()) {
            tryResize();
        }

        int index = getHash(key, capacity.get());

        try {
            // Old implementation -> locks[index].lock();
            // Don't just use lock() because a thread will get a lock then be interrupted by the executor shutdown
            // it will be interrupted and never get to finally method to unlock
            // This blocks other threads that will wait forever for that lock
            // When those threads are blocked, the app will never exit regardless of executor shutdown or shutdownNow

            // lock bucket for index
            locks[index].writeLock().lockInterruptibly();
            System.out.println("Thread " + Thread.currentThread().getName() + " acquired lock for " + index + " at " + System.currentTimeMillis());
            if (get(key) != null) {
                throw new IllegalArgumentException("Key " + key + " already present in map");
            }
            if (isResizeNeeded()) {
                // unlock because after we resize we want to rehash and the index might be different
                locks[index].writeLock().unlock();
                tryResize();
                // rehash again because the old hash may be pointing to a bucket that is filled after resizing and if we add to it
                // it will cause a disbalance, with a new hash we have a higher chance to hit an empty bucket
                index = getHash(key, capacity.get());
                locks[index].writeLock().lockInterruptibly();
            }
            addNode(map, key, value, index);
            countElements.incrementAndGet();
        } catch (InterruptedException e) {
            System.out.println("Thread " + Thread.currentThread().getName() +  " was interrupted");
        } finally {
            // unlock bucket
            try {
                locks[index].writeLock().unlock();
            } catch (IllegalMonitorStateException ex) {
                // Prone to throwing IllegalMonitorException because during resize we unlock the lock before the resizing begins on line 55
                System.out.println(ex);
                throw ex;
            }
            System.out.println("Thread " + Thread.currentThread().getName() + " released lock for " + index + " at " + System.currentTimeMillis());
        }
    }

    // Only return Value not the entire node
    // Otherwise we'll expose the chain of nodes
    public V get(K key) {
        int index = getHash(key, capacity.get());

        Node<K,V> node = map[index];
        if (node == null) {
            return null;
        }

        try {
            locks[index].readLock().lock();
            resizeLock.readLock().lock();
            while (true) {
                if (node == null) {
                    return null;
                }
                if (node.getKey().equals(key)) {
                    return node.getValue();
                }
                node = node.getNext();
            }
        } finally {
            locks[index].readLock().unlock();
            resizeLock.readLock().unlock();
        }
    }

    private int getHash(K key, int length) {
        return Math.abs(key.hashCode() % length);
    }

    private void addNode(Node[] map, K key, V val, int index) {
        Node<K, V> node = map[index];

        if (node == null) {
            map[index] = new Node<>(key, val, null);
            return;
        }
        while(true) {
            if (node.getKey().equals(key)) {
                throw new IllegalArgumentException("Key " + key + " already present in map");
            }
            if (node.getNext() == null) {
                break;
            }
            node = node.getNext();
        }
        node.setNext(new Node(key, val, null));
    }

    private void tryResize() {
        try {
            resizeLock.writeLock().lockInterruptibly();
            System.out.println("Resize lock acquired by " + Thread.currentThread().getName());
            // Another thread could have already resized, during the time between the moment
            // we hit the condition to the moment we acquire the lock
            if (isResizeNeeded()) {
                resize();
            } else {
                System.out.println("Resize was done by another thread.");
            }
        } catch (InterruptedException e) {
            System.out.println("Thread " + Thread.currentThread().getName() +  " was interrupted");
        } finally {
            resizeLock.writeLock().unlock();
            System.out.println("Resize lock release by " + Thread.currentThread().getName());
        }
    }

    private boolean isResizeNeeded() {
        return countElements.get() > LOAD_FACTOR * capacity.get();
    }

    private void resize() {
        // make a variable for double capacity, because we can't set it yet
        int doubleCapacity = capacity.get() * 2;
        Node<K,V>[] newMap = new Node[doubleCapacity];
        ReentrantReadWriteLock[] newLocks = new ReentrantReadWriteLock[doubleCapacity];

        for (int i = 0; i < newLocks.length; i++) {
            newLocks[i] = new ReentrantReadWriteLock();
        }

        // Old implementation
        // DON'T DO THIS, I've only left it so that you don't redo my mistakes
        // Idea was to acquire all locks, because otherwise we have a race condition if we put a new element during resizing
        // However locking the entire map isn't atomic which led to race conditions and deadlocks
        // Lock[] oldLocks = locks;
        // for (int i = 0; i < oldLocks.length; i++) {
        //     oldLocks[i].lock();
        // }

        for (int i = 0; i < map.length; i++) {
            Node<K,V> node = map[i];

            while(node != null) {
                // We have to use new capacity for rehashing otherwise we are rehashing data using old capacity, but the map has length equal to new capacity
                // Essentially instead of putting N elements in X*2 buckets we are putting them in X buckets increasing chance of
                // 2+ elements falling in the same bucket and decreasing performance
                int index = getHash(node.getKey(), doubleCapacity);
                addNode(newMap, node.getKey(), node.getValue(), index);
                node = node.getNext();
            }
        }

        map = newMap;
        locks = newLocks;
        // Capacity has to be set at the end because if it is before the map being assigned to the new map and the thread gets interrupted
        // the capacity will be double length but the map will be its old length
        // and during toString() we will iterate over double length and get ArrayIndexOutOfBounds exception
        capacity.set(doubleCapacity);
        System.out.println("Resized array using " + Thread.currentThread().getName());
    }

    /**
     * Contract of toString() is that it will read the data available at the start of the method.
     * That's why we take a snapshot of the capacity and map
     * Because the original map can be resized during toString() and that means race conditions and 90% of the time -> ArrayOutOfBounds exception
     * @return string representation of the map
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();

        try {
            resizeLock.readLock().lock();
            int snapshotCapacity = capacity.get();

            for (int i = 0; i < snapshotCapacity; i++) {
                builder.append("Bucket " + i + ": ");
                Node node = map[i];
                while(true) {
                    if (node == null) {
                        break;
                    }
                    builder.append("K:" + node.getKey()  + ", V:" + node.getValue() + ",");
                    node = node.getNext();
                }
                builder.append("\n");
            }
            return builder.toString();
        } finally {
            resizeLock.readLock().unlock();
        }
    }
}
