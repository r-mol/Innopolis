
/*
@author Karina Denisova group #7
 */

import java.util.Scanner;

public class task3 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String[] tempNumber = scanner.nextLine().split(" ");
        int countOfCommands = Integer.parseInt(tempNumber[0]);
        int capacity = Integer.parseInt(tempNumber[1]);

        QueuedBoundedStack<Object> stack = new QueuedBoundedStack<>(capacity);
        DoubleHashSet<Object> documents = new DoubleHashSet<>(7919);

        stack.push(documents);
        int notChange = 0;

        for (int i = 0; i < countOfCommands; i++) {

            documents = new DoubleHashSet<>((DoubleHashSet<Object>) stack.top());

            notChange = 0;

            String tempLine = scanner.nextLine();
            String[] lineOfCommands = new String[2];
            if(tempLine.contains(" ")){
                lineOfCommands = tempLine.split(" ");
            } else {
                lineOfCommands[0] = tempLine;
            }

            if(lineOfCommands[0].equals("NEW")){

                if (documents.contains(lineOfCommands[1])) {
                    System.out.println("ERROR: cannot execute NEW " + lineOfCommands[1]);
                    notChange = 1;
                } else if (lineOfCommands[1].endsWith("/")) {
                    StringBuilder temp = new StringBuilder(lineOfCommands[1]);
                    temp.deleteCharAt(temp.length() - 1);
                    lineOfCommands[1] = String.valueOf(temp);
                    if (!documents.contains(lineOfCommands[1])) {
                        lineOfCommands[1] += "/";
                        documents.add(lineOfCommands[1]);
                    } else {
                        lineOfCommands[1] += "/";
                        System.out.println("ERROR: cannot execute NEW " + lineOfCommands[1]);
                        notChange = 1;
                    }
                } else {
                    if (!documents.contains(lineOfCommands[1] + "/")) {
                        documents.add(lineOfCommands[1]);
                    } else if (documents.contains(lineOfCommands[1])) {
                        System.out.println("ERROR: cannot execute NEW " + lineOfCommands[1]);
                        notChange = 1;
                    } else {
                        System.out.println("ERROR: cannot execute NEW " + lineOfCommands[1]);
                        notChange = 1;
                    }
                }

            }else if(lineOfCommands[0].equals("REMOVE")) {

                if (documents.contains(lineOfCommands[1])) {
                    documents.remove(lineOfCommands[1]);
                } else {
                    System.out.println("ERROR: cannot execute REMOVE " + lineOfCommands[1]);
                    notChange = 1;
                }
            }else if(lineOfCommands[0].equals("LIST")){

                    notChange = 1;
                    for (int j = 0; j < 7919; j++) {
                        if (((DoubleHashSet<Object>) stack.top()).get(j) != null && ((DoubleHashSet<Object>) stack.top()).get(j) != "DELETED") {
                            System.out.print(((DoubleHashSet<Object>) stack.top()).get(j) + " ");
                        }
                    }
                    System.out.println();
            }else if(lineOfCommands[0].equals("UNDO")) {
                notChange = 1;

                if (lineOfCommands[1] == null) {
                    if (stack.size() <= 1) {
                        System.out.println("ERROR: cannot execute UNDO");
                    } else {
                        stack.pop();
                    }
                } else {
                    if (stack.size() <= Integer.parseInt(lineOfCommands[1])) {
                        System.out.println("ERROR: cannot execute UNDO " + lineOfCommands[1]);
                    } else {
                        for (int j = 0; j < Integer.parseInt(lineOfCommands[1]); j++) {
                            stack.pop();
                        }
                    }
                }
            }

            if(notChange == 0) {
                stack.push(documents);
            }
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

class ArrayCircularBoundedQueue<T> implements ICircularBoundedQueue<T> {
    private final int front;
    private int rear;
    private final int capacity;
    private final T [] queue;

    ArrayCircularBoundedQueue(int capacity)
    {
        front = 0;
        rear = 0;
        this.capacity = capacity;
        queue = (T[]) new Object[this.capacity];
    }


    @Override
    public void offer(T value) {
        if (capacity == rear) {
            this.poll();
        }
        queue[rear]=value;
        rear++;
    }

    @Override
    public T poll() {
        T value;
        if (this.isEmpty()) {
            throw new IllegalStateException("Queue is Empty");
        }else {
            value = (T)queue[front];
            for (int i = 0; i < rear - 1; i++) {
                queue[i] = queue[i + 1];
            }

            if (rear + 1 < capacity) {
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

interface ISet<T> {
    void add(T item); // add item in the set

    void remove(T item); // remove an item from a set

    boolean contains(T item); // check if a item belongs to a set

    int size(); // number of elements in a set

    boolean isEmpty(); // check if the set is empty
}

class DoubleHashSet<T> implements ISet<T> {
    private final int capacity;
    private int size;
    private final Object[] items;

    DoubleHashSet(int capacity) {
        this.capacity = capacity;
        size = 0;
        items = new Object[capacity];
    }

    DoubleHashSet(DoubleHashSet<Object> temp) {
        this.capacity = temp.capacity;
        this.size = temp.size;
        int i = 0;
        items = new Object[temp.capacity];
        for(Object a: temp.items){
            items[i++]= a;
        }
    }

    int hash(T item, int j) {
        return Math.abs(item.hashCode() + j * hashCode2(item)) % capacity;
    }

    @Override
    public void add(T item) {
        if (get(hash(item, 0)) == null || get(hash(item, 0)) == "DELETED") {
            set(hash(item, 0), item);
            size++;
        } else {
            for (int i = 0; i < capacity; i++) {
                if (get(hash(item, i)) == null || get(hash(item, i)) == "DELETED") {
                    set(hash(item, i), item);
                    size++;
                    break;
                }else if (get((hash(item, i))).equals(item)) {
                    break;
                }
            }
        }
    }

    @Override
    public void remove(T item) {
        if (get(hash(item, 0)).equals(item)) {
            set(hash(item, 0), (T) "DELETED");
            size--;
        } else {
            for (int i = 0; i < capacity; i++) {
                if (get(hash(item, i)).equals(item)) {
                    set(hash(item, i), (T) "DELETED");
                    size--;
                    break;
                }
            }
        }
    }

    @Override
    public boolean contains(T item) {

        if (get(hash(item, 0)) == null) {
            return false;
        }else if (get(hash(item, 0)).equals(item)) {
            return true;
        } else {
            for (int i = 0; i < capacity; i++) {
                try{
                    if (get(hash(item, i)).equals(item)) {
                        return true;
                    }
                } catch (NullPointerException e) {
                    return false;
                }
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

    public int hashCode2(T item) {
        return 2003 * item.hashCode() % 2003;
    }

    public T get(int index) {
        try {
            return (T) this.items[index];
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void set(int index, T item) {
        this.items[index] = item;
    }

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

class QueuedBoundedStack<T> implements IBoundedStack<T> {
    private final int capacity;
    private int size;
    ArrayCircularBoundedQueue<Object> queue1;
    ArrayCircularBoundedQueue<Object> queue2;

    public QueuedBoundedStack(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        queue1 = new ArrayCircularBoundedQueue<>(capacity);
        queue2 = new ArrayCircularBoundedQueue<>(capacity);
    }

    @Override
    public void push(T value) { // O(n)
        queue2.offer(value);
        if (this.size == capacity) {
            while (!queue1.isEmpty() && !queue2.isFull()) {
                queue2.offer(queue1.peek());
                queue1.poll();
            }
            queue1.flush();
            this.size--;
        } else {
            while (!queue1.isEmpty()) {
                queue2.offer(queue1.peek());
                queue1.poll();
            }
        }

        ArrayCircularBoundedQueue<Object> queueTemp = queue1;
        queue1 = queue2;
        queue2 = queueTemp;

        this.size++;
    }

    @Override
    public T pop() { // O(1)
        if (this.isEmpty()) {
            throw new IllegalStateException("is Empty");
        }
        this.size--;
        return (T)queue1.poll();
    }

    @Override
    public T top() { // O(1)
        return (T)queue1.peek();
    }

    @Override
    public void flush() { // O(n)
        queue1.flush();
    }

    @Override
    public boolean isEmpty() { // O(1)
        return queue1.isEmpty();
    }

    @Override
    public boolean isFull() { // O(1)
        return this.size == capacity;
    }

    @Override
    public int size() { // O(1)
        return this.size;
    }

    @Override
    public int capacity() { // O(1)
        return capacity;
    }
}