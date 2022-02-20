import java.util.*;
import java.lang.*;

public class Main {
    static Scanner scanner;
    static StringBuilder temp;
    static String[] states;
    static String[] alphabet;
    static String initSt;
    static String[] finSt;
    static String[][] trans2;
    LinkedCircularBoundedQueue<String> Errors = new LinkedCircularBoundedQueue<>(1000);
    LinkedCircularBoundedQueue<String> Warnings = new LinkedCircularBoundedQueue<>(1000);

    public static void main(String[] args) {
        input();
        System.out.println(Arrays.toString(states));
        System.out.println(Arrays.toString(alphabet));
        System.out.println(initSt);
        System.out.println(Arrays.toString(finSt));
        System.out.println(Arrays.deepToString(trans2));
    }

    public static void input() {
        temp = new StringBuilder(scanner.nextLine());
        temp.delete(0, temp.lastIndexOf("[") + 1);
        temp.delete(temp.lastIndexOf("]"), temp.length());
        states = new String(temp).split(",");

        temp = new StringBuilder(scanner.nextLine());
        temp.delete(0, temp.lastIndexOf("[") + 1);
        temp.delete(temp.lastIndexOf("]"), temp.length());
        alphabet = new String(temp).split(",");

        temp = new StringBuilder(scanner.nextLine());
        temp.delete(0, temp.lastIndexOf("[") + 1);
        temp.delete(temp.lastIndexOf("]"), temp.length());
        initSt = new String(temp);

        temp = new StringBuilder(scanner.nextLine());
        temp.delete(0, temp.lastIndexOf("[") + 1);
        temp.delete(temp.lastIndexOf("]"), temp.length());
        finSt = new String(temp).split(",");

        temp = new StringBuilder(scanner.nextLine());
        temp.delete(0, temp.lastIndexOf("[") + 1);
        temp.delete(temp.lastIndexOf("]"), temp.length());
        String [] trans1 = new String(temp).split(",");
        trans2 = new String[trans1.length][];
        for (int i = 0; i < trans1.length; i++) {
            trans2[i] = trans1[i].split(">");
        }
    }





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
    /*
    Declaration of variables:
    1. maxSizeQueue
    2. front
    3. rear
    4. size
     */
    private final int maxSizeQueue;
    private Node<T> front;
    private Node<T> rear;
    private int size;

    /*
    In Constructor initialize:
    1. maxSizeQueue by user input
    2. front by null
    3. rear by null
    4. size by 0
     */
    public LinkedCircularBoundedQueue(int maxSizeQueue) {
        this.maxSizeQueue = maxSizeQueue;
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    /*
    The method offer() considers 3 ways of insert an element to the rear of the queue.
    1. Queue is empty and element do not have predecessor and descendant.
    2. Queue is full and it has Linked Circular Bounded Queue, that is why it should remove element from front by method poll() and after insert element to the rear.
    3. Standart insert to the rear.
     */
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

    /*
    The method poll() checks if the queue is empty or not.
    If the queue is not empty, then it copies value to the variable temp.
    After it changes the link, decrease the size and return value of the front element.
     */
    public T poll() { // O(1)
        if (this.isEmpty()) {
            throw new IllegalStateException("Queue is Empty");
        }
        T temp = this.front.value;
        this.front = front.next;
        this.size--;
        if (this.front == null){ this.rear = null;}
        return temp;
    }

    /*
    The method peek() checks if queue empty or not.
    After return value of the front element, without removing.
     */
    public T peek() { // O(1)
        if (this.isEmpty()) {
            throw new IllegalStateException("Queue is Empty");
        }
        return this.front.value;
    }

    /*
    The method flush() is removing all elements from the queue by method poll().
     */
    public void flush() { // O(n)
        while (!this.isEmpty()) {
            this.poll();
        }
    }

    /*
    The method size() returns value of the variable size.
     */
    public int size() { // O(1)
        return this.size;
    } // O(1)

    /*
    The method capacity() returns value of the variable maxSizeQueue.
     */
    public int capacity() { // O(1)
        return maxSizeQueue;
    } // O(1)

    /*
    The method isEmpty() checks if the front equals null and the rear equals null than queue is empty
     */
    public boolean isEmpty() { // O(1)
        return this.front == null && this.rear == null;
    } // O(1)

    /*
    The method isFull() compares size with maxSizeQueue and if they are equal than queue is full.
     */
    public boolean isFull() { // O(1)
        return this.size == maxSizeQueue;
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
