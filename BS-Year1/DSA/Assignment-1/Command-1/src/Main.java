import java.util.Scanner;

public class Main {
    public static void main(String[] arg) {
        Scanner scanString = new Scanner(System.in);
        String tempNum = scanString.nextLine();
        String[]t = tempNum.split(" ");
        int countOfQueue = Integer.parseInt(t[0]);
        int maxSizeQueue = Integer.parseInt(t[1]);
        LinkedCircularBoundedQueue<Object> queue1 = new LinkedCircularBoundedQueue<>(maxSizeQueue);
        if(scanString.hasNextLine()) {
            while (countOfQueue != 0) {
                queue1.offer(scanString.nextLine());
                countOfQueue--;
            }
        }

        while (!queue1.isEmpty()) {
            System.out.println(queue1.poll());

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

    public void setSize(int size) {
        this.size = size;
    } // O(1)

}


class Node<T> {
    T value;
    Node<T> next;

    public Node(T value) {
        this.value = value;
        this.next = null;
    }
}


