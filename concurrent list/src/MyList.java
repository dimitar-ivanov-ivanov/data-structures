import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyList<T> {

    private static final int INITIAL_CAPACITY = 8;

    private volatile T[] data;

    private AtomicInteger capacity = new AtomicInteger(INITIAL_CAPACITY);

    private AtomicInteger count = new AtomicInteger(0);

    // Old implementation
    //private ReentrantLock lock;

    // Use ReentrantReadWriteLock becauase it's better instead of locking for writing
    // the read lock is aware of the write lock, while we are writing we cannot read
    // otherwise multiple readers can read at the same time and hold the read lock
    // There is a theorical limit to how many there can be which is 65k threads
    // But that shouldn't be a problem

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

    //If you decide to have 100+ threads probably use StampedLock for better performance

    private Class clazz;

    public MyList(Class clazz) {
        this.clazz = clazz;
        data = (T[]) Array.newInstance(clazz, capacity.get());
    }

    public void add(T element) {
        try {
            // make sure the thread doesn't hang when it's waiting for a resource
            // and the executor is shutting down
            // this would stop the app from exiting entirely
            writeLock.lockInterruptibly();

            if (shouldBeResized()) {
                resize();
            }

            data[count.get()] = element;
            count.incrementAndGet();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                writeLock.unlock();
            } catch (IllegalMonitorStateException ex) {
                System.out.println(ex);
            }
        }
    }

    public T get(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Not valid index");
        }

        try {
            readLock.lockInterruptibly();
            if (index > capacity.get()) {
                throw new IllegalArgumentException("Not valid index");
            }
            return data[index];
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                readLock.unlock();
            } catch (IllegalMonitorStateException ex) {
                System.out.println("Illegal monitor for " + Thread.currentThread().getName() + " trying to unlock readLock while reading " + index);
            }
        }
    }

    /**
     * Get the current size.
     * Possibly a little out of sync
     * Alternative was to use read lock.
     * @return size of structure
     */
    public int size() {
        return count.get();
    }

    private void resize() {
        capacity.set(capacity.get() * 2);
        T[] newArray = (T[]) Array.newInstance(clazz, capacity.get());

        for (int i = 0; i < data.length; i++) {
            newArray[i] = data[i];
        }
        data = newArray;
        System.out.println("Resized array using " + Thread.currentThread().getName() + " to capacity = " + capacity.get());
    }

    private boolean shouldBeResized() {
        return count.get() + 1 > capacity.get();
    }

    /**
     * toString() reads only the data that exists the moment the method is invoked.
     * It doesn't place locks on top of the data
     * @return
     */
    @Override
    public String toString() {
        try {
            readLock.lockInterruptibly();
            StringBuilder builder = new StringBuilder();
            // Take the len here because the array can change because of the resize
            int len = count.get();

            for (int i = 0; i < len; i++) {
                builder.append(data[i] + "\n");
            }

            return builder.toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                readLock.unlock();
            } catch (IllegalMonitorStateException ex) {
                System.out.println("Illegal monitor for " + Thread.currentThread().getName() + " trying toString()");
            }
        }
    }

}
