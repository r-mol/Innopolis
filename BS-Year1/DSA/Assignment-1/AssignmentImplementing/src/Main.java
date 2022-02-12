import java.util.Scanner;

interface ISet<T> {
    void add(T item); // add item in the set

    void remove(T item); // remove an item from a set

    boolean contains(T item); // check if a item belongs to a set

    int size(); // number of elements in a set

    boolean isEmpty(); // check if the set is empty
}

interface IBoundedStack<T> {
    void push(T value); // push an element onto the stack
    // remove the oldest element
    // when if stack is full

    T pop(); // remove an element from the top of the stack

    T top(); // look at the element at the top of the stack
    // (without removing it)

    void flush(); // remove all elements from the stack

    boolean isEmpty(); // is the stack empty?

    boolean isFull(); // is the stack full?

    int size(); // number of elements

    int capacity(); // maximum capacity
}

interface ICircularBoundedQueue<T> {
    void offer(T value); // insert an element to the rear of the queue
    // overwrite the oldest elements
    // when the queue is full

    T poll(); // remove an element from the front of the queue

    T peek(); // look at the element at the front of the queue
    // (without removing it)

    void flush(); // remove all elements from the queue

    boolean isEmpty(); // is the queue empty?

    boolean isFull(); // is the queue full?

    int size(); // number of elements

    int capacity(); // maximum capacity
}

class LinkedCircularBoundedQueue<T> implements ICircularBoundedQueue<T> {

    private int maxSizeQueue;
    private Node<T> front;
    private Node<T> rear;
    private int size;

    public LinkedCircularBoundedQueue(int maxSizeQueue) {
        this.maxSizeQueue = maxSizeQueue;
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    public void offer(T value) { // O(1)
        Node<T> node = new Node(value);
        if (this.isEmpty()) {
            this.rear = node;
            this.front = node;
        } else if (this.size == this.maxSizeQueue) {
            this.poll();
            this.rear.next = node;
            this.rear = node;   //
        } else {
            this.rear.next = node;
            this.rear = node;
        }
        this.size++;
    }

    public T poll() { // O(1)
        if (this.isEmpty()) {
            throw new IllegalStateException("Queue is Empty");
        }
        T toRet = this.front.value;
        this.front = front.next;
        this.size--;
        if (this.front == null) this.rear = null;
        return toRet;
    }

    public T peek() { // O(1)
        if (this.isEmpty()) {
            throw new IllegalStateException("Queue is Empty");
        }
        return this.front.value;
    }

    public void flush() { // O(n)
        while (!this.isEmpty()) {
            this.poll();
        }
    }

    public int size() { // O(1)
        return this.size;
    } // O(1)

    public int capacity() { // O(1)
        return maxSizeQueue;
    } // O(1)

    public boolean isEmpty() { // O(1)
        return this.front == null && this.rear == null;
    } // O(1)

    public boolean isFull() { // O(1)
        return this.size == maxSizeQueue;
    } // O(1)

}

class QueuedBoundedStack<T> implements IBoundedStack<T> {
    private final int maxSizeStack;
    private int size;
    LinkedCircularBoundedQueue<T> q1;
    LinkedCircularBoundedQueue<T> q2;

    public QueuedBoundedStack(int maxSizeStack) {
        this.maxSizeStack = maxSizeStack;
        this.size = 0;
        q1 = new LinkedCircularBoundedQueue<>(maxSizeStack);
        q2 = new LinkedCircularBoundedQueue<>(maxSizeStack);
    }

    @Override
    public void push(T value) { // O(n)
        // Push x first in empty q2
        q2.offer(value);
        if (this.size == maxSizeStack) {
            while (!q1.isEmpty() && !q2.isFull()) {
                q2.offer(q1.peek());
                q1.poll();
            }
            q1.flush();
            this.size--;
        } else {
            while (!q1.isEmpty()) {
                q2.offer(q1.peek());
                q1.poll();
            }
        }

        // swap the names of two queues
        LinkedCircularBoundedQueue<T> q = q1;
        q1 = q2;
        q2 = q;

        this.size++;
    }

    @Override
    public T pop() { // O(1)
        if (this.isEmpty()) {
            throw new IllegalStateException("Queue is Empty");
        }
        this.size--;
        return q1.poll();
    }

    @Override
    public T top() { // O(1)
        return q1.peek();
    }

    @Override
    public void flush() { // O(n)
        q1.flush();
    }

    @Override
    public boolean isEmpty() { // O(1)
        return q1.isEmpty();
    }

    @Override
    public boolean isFull() { // O(1)
        return this.size == maxSizeStack;
    }

    @Override
    public int size() { // O(1)
        return this.size;
    }

    @Override
    public int capacity() { // O(1)
        return maxSizeStack;
    }
}

interface DoubleHashEl<T> {
    int hashCode2(T item, int maxSizeSet, int primeSize);

    int getPrime(int maxSizeSet);
}

class SkinObject<T> implements DoubleHashEl<T> {

    @Override
    public int hashCode2(T item, int maxSizeSet, int primeSize) {
        int value = item.hashCode();
        value %= maxSizeSet;
        if (value < 0)
            value += maxSizeSet;
        return primeSize - value % primeSize;
    }

    @Override
    public int getPrime(int maxSizeSet) {
        for (int i = maxSizeSet - 1; i >= 1; i--) {
            int cnt = 0;
            for (int j = 2; j * j <= i; j++) {
                if (i % j == 0) {
                    cnt++;
                }
            }

            if (cnt == 0) {
                return i;
            }
        }
        return 3;
    }
}

class DoubleHashSet<T extends DoubleHashEl<T>> implements ISet<T> {

    private final int maxSizeSet;
    private int size;
    private int hash;
    private Object[] arr;
    int primeSize;

    DoubleHashSet(int maxSizeSet) {
        //arr = new int[maxSizeSet];
        this.maxSizeSet = maxSizeSet;
        primeSize = getPrime(this.maxSizeSet);
    }

    private int getPrime(int maxSizeSet) {
        return 0;
    }

    int getHash(T item) {
        return (item.hashCode() + item.hashCode2(item, maxSizeSet, primeSize)) % maxSizeSet;
    }

    @Override
    public void add(T item) {

    }

    @Override
    public void remove(T item) {

    }

    @Override
    public boolean contains(T item) {

        for (Object t : arr) {
            if (t.hashCode() == item.hashCode()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}

class Node<T> {
    T value;
    Node<T> next;

    public Node(T value) {
        this.value = value;
        this.next = null;
    }
}

class Main {
    public static void main(String[] arg) {
        System.out.println("Plz, enter size of the queue: ");
        Scanner scanner = new Scanner(System.in);
        int sizeQueue = scanner.nextInt();
        LinkedCircularBoundedQueue<Object> queue = new LinkedCircularBoundedQueue<>(sizeQueue);
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);

        System.out.println("Full? " + queue.isFull());
        System.out.println("Peek = " + queue.peek());
        queue.offer(4);
        System.out.println("Peek = " + queue.peek());
        queue.offer(5);
        System.out.println("Peek = " + queue.peek());
        queue.offer(6);
        System.out.println("Peek = " + queue.peek());
        queue.flush();
        System.out.println("Empty? = " + queue.isEmpty());

        System.out.println("Plz, enter size of the stack: ");
        int sizeStack = scanner.nextInt();
        QueuedBoundedStack<Object> stack = new QueuedBoundedStack<>(sizeStack); // check of extend
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("Top = " + stack.top());
        stack.push(4);
        System.out.println("Top = " + stack.top());
        stack.push(5);
        System.out.println("Top = " + stack.top());
        stack.push(6);
        System.out.println("Full? " + stack.isFull());
        System.out.println("Top = " + stack.top());
        System.out.println("Full? " + stack.isFull());
        System.out.println("Pop = " + stack.pop());
        System.out.println("Top = " + stack.top());
        System.out.println("Pop = " + stack.pop());
        stack.flush();
        System.out.println("Empty? " + stack.isEmpty());

    }
}