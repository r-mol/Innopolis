import java.util.Scanner;

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

class ArrayCircularBoundedQueue<T> implements ICircularBoundedQueue<T>{
    private final int front;
    private int rear;
    private final int capacity;
    private final T [] queue;

    ArrayCircularBoundedQueue(int capacity)
    {
        front = 0;
        rear = 0;
        this.capacity = capacity;
        queue = (T[])new Object[this.capacity];
    }


    @Override
    public void offer(T value) {
        if (capacity == rear) {
            this.poll();
        }
        queue[rear] = value;
        rear++;
    }

    @Override
    public T poll() {
        T value;
        if (this.isEmpty()) {
            throw new IllegalStateException("Queue is Empty");
        }else {
            value = queue[front];
            for (int i = 0; i < rear - 1; i++) {
                queue[i] = queue[i + 1];
            }

            if (rear < capacity) {
                queue[rear] = null;
            }

            rear--;
        }
        return value;
    }

    @Override
    public T peek() {
        if (this.isEmpty()) {
            throw new IllegalStateException("Queue is Empty");
        }
        return queue[front];
    }

    @Override
    public void flush() {
        while (!this.isEmpty()) {
            this.poll();
        }
    }

    @Override
    public boolean isEmpty() {
        return front == 0 && rear == 0;
    }

    @Override
    public boolean isFull() {
        return rear == capacity;
    }

    @Override
    public int size() {
        return rear;
    }

    @Override
    public int capacity() {
        return capacity;
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Plz, enter capacity: ");
        Scanner scanner = new Scanner(System.in);
        int capacity = scanner.nextInt();
        ArrayCircularBoundedQueue<Object> queue = new ArrayCircularBoundedQueue<>(capacity);

        queue.offer(1);
        queue.offer(2);
        queue.offer(3);

        System.out.println("Full? " + queue.isFull());
        System.out.println("Peek = " + queue.peek());
        queue.offer(10);
        System.out.println("Peek = " + queue.peek());
        queue.offer(12);
        System.out.println("Peek = " + queue.peek());
        queue.offer(14);
        System.out.println("Peek = " + queue.peek());
        queue.poll();
        System.out.println("Empty? = " + queue.isEmpty());
        queue.flush();
        System.out.println("Empty? = " + queue.isEmpty());
    }
}
