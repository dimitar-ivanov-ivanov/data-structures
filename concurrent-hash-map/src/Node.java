public class Node<K,V> {

    // immutable node
    // once created it never changes
    private final K key;
    private final V value;
    private Node<K, V> next;

    public Node(K k, V v, Node<K,V> next) {
        this.key = k;
        this.value = v;
        this.next = next;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public Node<K, V> getNext() {
        return next;
    }

    public void setNext(Node<K,V> node) {
        this.next = node;
    }
}
