/*
@author Roman Molochkov group 4
tg: @roman_molochkov
 */

import java.util.Scanner;

public class Main {
    public static void main(String[] arg) {
        System.out.println("Plz, enter size of the queue: ");
        Scanner scanner = new Scanner(System.in);
        int sizeQueue = scanner.nextInt();

        // create queue
        LinkedCircularBoundedQueue<Object> queue = new LinkedCircularBoundedQueue<>(sizeQueue);

        // add 3 elements to queue
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);

        //check if it full or not
        System.out.println("Full? " + queue.isFull());

        // add three elements and check if it is circular or not
        System.out.println("Peek = " + queue.peek());
        queue.offer(4);
        System.out.println("Peek = " + queue.peek());
        queue.offer(5);
        System.out.println("Peek = " + queue.peek());
        queue.offer(6);
        System.out.println("Peek = " + queue.peek());

        // check method flush
        queue.flush();

        // and check it
        System.out.println("Empty? = " + queue.isEmpty());

        System.out.println("Plz, enter size of the stack: ");
        int sizeStack = scanner.nextInt();

        //create stack
        QueuedBoundedStack<Object> stack = new QueuedBoundedStack<>(sizeStack);

        // add three elements
        stack.push(1);
        stack.push(2);
        stack.push(3);

        // add three elements and check if it is bounded or not
        System.out.println("Top = " + stack.top());
        stack.push(4);
        System.out.println("Top = " + stack.top());
        stack.push(5);
        System.out.println("Top = " + stack.top());
        stack.push(6);
        System.out.println("Full? " + stack.isFull());
        System.out.println("Top = " + stack.top());
        System.out.println("Full? " + stack.isFull());

        // check correctness of work method pop()
        System.out.println("Pop = " + stack.pop());
        System.out.println("Top = " + stack.top());
        System.out.println("Pop = " + stack.pop());
        stack.flush();
        System.out.println("Empty? " + stack.isEmpty());

        System.out.println("Plz, enter size of the stack: ");
        int sizeSet = scanner.nextInt();

        //create set
        DoubleHashSet<Object> set = new DoubleHashSet<>(sizeSet);

        //add three elements
        set.add(1);
        set.add(2);
        set.add(3);

        //remove element 3
        set.remove(3);

        //check if it contains or not
        System.out.println(set.contains(3));

        //check of containing element 2
        System.out.println(set.contains(2));

        //check the size of set
        System.out.println(set.size());

        //remove all elements
        set.remove(2);
        set.remove(1);

        //check the method isEmpty()
        System.out.println(set.isEmpty());
    }
}

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

class QueuedBoundedStack<T> implements IBoundedStack<T> {
    /*
    Declaration of variables:
    1. maxSizeStack
    2. size
    3. queue1
    4. queue2
     */
    private final int maxSizeStack;
    private int size;
    LinkedCircularBoundedQueue<T> queue1;
    LinkedCircularBoundedQueue<T> queue2;

    /*
    In Constructor initialize:
    1. maxSizeStack by user input
    2. size by 0
    3. queue1 by new LinkedCircularBoundedQueue with maxSizeStack
    4. queue2 by new LinkedCircularBoundedQueue with maxSizeStack
     */
    public QueuedBoundedStack(int maxSizeStack) {
        this.maxSizeStack = maxSizeStack;
        this.size = 0;
        queue1 = new LinkedCircularBoundedQueue<>(maxSizeStack);
        queue2 = new LinkedCircularBoundedQueue<>(maxSizeStack);
    }

    /*
    The method push() adds value to the empty queue (queue2),
    after this step it takes from the front of the queue1 elements and put them to the queue2
    while queue1 do not become empty or while queue2 is not full.
     */
    @Override
    public void push(T value) { // O(n)
        queue2.offer(value);
        if (this.size == maxSizeStack) {
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

        // Swap the queues' elements, after it queue2 becomes empty.
        LinkedCircularBoundedQueue<T> q = queue1;
        queue1 = queue2;
        queue2 = q;

        this.size++;
    }

    /*
    The method pop() checks if the queue is empty, else it decreases size and remove element from the front.
     */
    @Override
    public T pop() { // O(1)
        if (this.isEmpty()) {
            throw new IllegalStateException("Queue is Empty");
        }
        this.size--;
        return queue1.poll();
    }

    /*
    The method top() is different from the method pop(), because it does not remove element from the stack.
    It only returns value from the top.
     */
    @Override
    public T top() { // O(1)
        return queue1.peek();
    }

    /*
    The method flush() in the stack uses the same method from the queue.
     */
    @Override
    public void flush() { // O(n)
        queue1.flush();
    }

    /*
    The method isEmpty() in the stack uses the same method from the queue.
     */
    @Override
    public boolean isEmpty() { // O(1)
        return queue1.isEmpty();
    }

    /*
    The method isFull() compares size with maxSizeQueue and if they are equal than stack is full.
     */
    @Override
    public boolean isFull() { // O(1)
        return this.size == maxSizeStack;
    }

    /*
    The method size() returns value of the variable size.
     */
    @Override
    public int size() { // O(1)
        return this.size;
    }

    /*
    The method capacity() returns value of the variable maxSizeQueue.
     */
    @Override
    public int capacity() { // O(1)
        return maxSizeStack;
    }
}

class DoubleHashSet<T> implements ISet<T> {
    /*
    Declaration of variables:
    1. maxSizeSet
    2. size
    3. arr
     */
    private final int maxSizeSet;
    private int size;
    private final Object[] arr ;

    /*
   In Constructor initialize:
   1. maxSizeStack by user input
   2. size by 0
   3. arr by new array of Objects with capacity equal to maxSizeStack
    */
    DoubleHashSet(int maxSizeSet) {
        this.maxSizeSet = maxSizeSet;
        size = 0;
        arr = new Object[maxSizeSet];
    }

    /*
   In Copy-Constructor initialize:
   Copy all elements from the other object.
    */
    DoubleHashSet(DoubleHashSet<Object> temp) {
        this.maxSizeSet = temp.maxSizeSet;
        this.size = temp.size;
        int i = 0;
        arr = new Object[temp.maxSizeSet];
        for(Object a: temp.arr){
            arr[i++]= a;
        }
    }

    /*
    The method getHash() uses default function hashCode() of the element and the function hashCode2() which are
    dependent on repetitions.
     */
    int getHash(T item, int j) {
        return Math.abs(item.hashCode() + j * hashCode2(item)) % maxSizeSet;
    }

    /*
    The method add() gets hash for the item by the function getHash().
    After it gets free index by hash in which we can add our item.
    If this index is not free it uses the loop
     */
    @Override
    public void add(T item) {
        if (getItem(getHash(item, 0)) == null) {
            setItem(getHash(item, 0), item);
        } else {
            for (int i = 0; i < maxSizeSet; i++) {
                if (getItem(getHash(item, i)) == null) {
                    setItem(getHash(item, i), item);
                    break;
                }else if (getItem((getHash(item, i))).equals(item)) {
                    break;
                }
            }
        }
        size++;
    }

    /*
   The method remove() gets hash for the item by the function getHash().
   After it gets index by hash where can locate our item.
   If this standard index does not contain our item it uses the loop to find index after collision.
    */
    @Override
    public void remove(T item) {
        if (getItem(getHash(item, 0)).equals(item)) {
            setItem(getHash(item, 0), null);
        } else {
            for (int i = 0; i < maxSizeSet; i++) {
                if (getItem(getHash(item, i)).equals(item)) {
                    setItem(getHash(item, i), null);
                    break;
                }
            }
        }

        size--;
    }

    /*
   The method contains() gets hash for the item by the function getHash().
   After it looks for index by hash where can locate our item.
   And return true - contain, false - do not contain.
    */
    @Override
    public boolean contains(T item) {

        if (getItem(getHash(item, 0)) == null) {
            return false;
        }
        if (getItem(getHash(item, 0)).equals(item)) {
            return true;
        } else {
            for (int i = 0; i < maxSizeSet; i++) {
                try{
                    if (getItem(getHash(item, i)).equals(item)) {
                        return true;

                    }
                } catch (NullPointerException e) {
                    return false;
                }

            }
        }

        return false;
    }

    /*
    The method size() returns variable size.
     */
    @Override
    public int size() {
        return size;
    }

    /*
    The method isEmpty() returns comparing of variable size with const 0.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /*
    The method hashCode2() returns hash by formula: primeNum * hash % primeNum.
     */
    public int hashCode2(T item) {
        return 7919 * item.hashCode() % 7919;
    }

    /*
    The method getItem() returns the item from the array by index.
     */
    public T getItem(int index) {
        try {
            return (T) this.arr[index];
        } catch (NullPointerException e) {
            return null;
        }
    }

    /*
    The method setItem() sets the item to the array by index.
     */
    public void setItem(int index, T item) {
        this.arr[index] = item;
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

