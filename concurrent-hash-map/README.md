## Implementation of Concurrent Hash Map.
 - We use Reentrant lock for locking.
 - We have an array of ReentrantLocks, each lock is the lock for each bucket in the map.
 - The Map holds Node, which is a custom POJO holding key, value and next node.
 - I'm using Node because I can make it volatile, volatile didn't work on LinkedList, which was my initial implementation.
 - I needed volatile to ensure visibilty between threads for the different nodes, otherwise get method was either returning dirty data or not returning anything at all.
 - This happened when get was used to fetch data that was being written by another thread.

## Biggest problems
 - The biggest problem has that during "resize()" of the map the entire map had to be locked
 - This is done, because if it isn't we get "IndexOutOfBounds" and "IllegalMonitorStateException" and generally bad race conditions where T1 can override data during resize that T2 just put in the map
 - Second problem has that during the "locking" of the map I was locking the locks one by one, BUT that is not an atomic action you need a separate lock for that.
 - Third was not using capacity and counter AtomicInteger
 - Fourth has not double checking the "should resize" condition when we also enter the "resize" method because there is a gap in time between hitting condition for resizing and acquiring "resize" lock in that time another thread could have done the resizing

## Findings
 - Force shutdown on executor doesn't do anything if there is a deadlock 

## Unsolve
  - Why can't we terminate the executor sometimes?
    -  There is a deadlock I think when we resize we probably shouldn't be able to 