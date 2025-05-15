import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MyList<T> {

    private static final int INITIAL_CAPACITY = 8;

    private volatile T[] data;

    private AtomicInteger capacity = new AtomicInteger(INITIAL_CAPACITY);

    private AtomicInteger count = new AtomicInteger(0);

    private ReentrantLock lock;

    private Class clazz;

    public MyList(Class clazz) {
        this.clazz = clazz;
        data = (T[]) Array.newInstance(clazz, capacity.get());
        lock = new ReentrantLock();
    }

    public void add(T element) {
        try {
            // make sure the thread doesn't hang when it's waiting for a resource
            // and the executor is shutting down
            // this would stop the app from exiting entirely
            lock.lockInterruptibly();

            if (shouldBeResized()) {
                resize();
            }

            data[count.get()] = element;
            count.incrementAndGet();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                lock.unlock();
            } catch (IllegalMonitorStateException ex) {
                System.out.println(ex);
            }
        }
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
        StringBuilder builder = new StringBuilder();
        // Take the len here because the array can change because of the resize
        int len = count.get();

        for (int i = 0; i < len; i++) {
            builder.append(data[i] + "\n");
        }

        return builder.toString();
    }

}
