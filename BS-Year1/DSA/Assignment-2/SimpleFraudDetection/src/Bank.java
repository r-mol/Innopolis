
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public LinkedCircularBoundedQueue(LinkedCircularBoundedQueue<T> clone) {
        this.maxSizeQueue = clone.maxSizeQueue;
        this.front = clone.front;
        this.rear = clone.rear;
        this.size = clone.size();
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

        if (this.front == null) {
            this.rear = null;
        }

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

public class Bank {
    private static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

    private static Date findNextDay(Date date)
    {
        return new Date(date.getTime() + MILLIS_IN_A_DAY);
    }

    private static Date findPrevDay(Date date)
    {
        return new Date(date.getTime() - MILLIS_IN_A_DAY);
    }

    public static Map<Date, Double> sortMap(Map<Date, Double> pairs) {
        Map<Date, Double> sort = new LinkedHashMap<>();
        List<Date> list = new ArrayList<>();
        list.addAll(pairs.keySet());

        Date temp;
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < list.size() - 1; i++) {
                if (list.get(i).compareTo(list.get(i + 1)) > 0) {
                    temp = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, temp);
                    sorted = false;
                }
            }
        }

        for (Date date : list) {
            sort.put(date, pairs.get(date));
        }

        return sort;
    }

    public static double findMedian(LinkedCircularBoundedQueue<Double> temp) {
        LinkedCircularBoundedQueue<Double> tempQueue = new LinkedCircularBoundedQueue<>(temp);
        List<Double> tempList = new ArrayList<>();

        while (!tempQueue.isEmpty()) {
            tempList.add(tempQueue.peek());
            tempQueue.poll();
        }

        Collections.sort(tempList);

        if (tempList.size() % 2 == 0) {
            return (tempList.get(tempList.size() / 2 - 1) + tempList.get(tempList.size() / 2)) / 2;
        } else {
            return tempList.get((tempList.size() - 1) / 2);
        }
    }

    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        String[] tt = scanner.nextLine().split(" ");
        int lines = Integer.parseInt(tt[0]);
        int trailingDays = Integer.parseInt(tt[1]);

        Map<Date, Double> pairs = new LinkedHashMap<>();

        while (lines > 0) {
            String temp = scanner.nextLine();
            String[] t = temp.split(" \\$");

            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(t[0]);
            double money = Double.parseDouble(t[1]);

            if (pairs.containsKey(date)) {
                pairs.put(date, pairs.get(date) + money);
            } else {
                pairs.put(date, money);
            }

            lines--;
        }

        Map<Date, Double> sortedMap = sortMap(pairs);

        List<Double> money = new ArrayList<>();

        Date prevDay = null;
        for (Date date : sortedMap.keySet()) {

            if (prevDay == null || prevDay.compareTo(findPrevDay(date))==0) {
                money.add(sortedMap.get(date));
            } else{
                while(prevDay.compareTo(findPrevDay(date))!=0){
                    money.add((double) 0);
                    prevDay = findNextDay(prevDay);
                }
            }
            prevDay = date;
        }

        LinkedCircularBoundedQueue<Double> queueOfTrailingDays = new LinkedCircularBoundedQueue<>(trailingDays);

        int result = 0;
        for (int i = 0; i < trailingDays; i++) {
            queueOfTrailingDays.offer(money.get(i));
        }

        double high = 2 * findMedian(queueOfTrailingDays);
        for (int i = trailingDays; i < money.size(); i++) {
            if (money.get(i) >= high) {
                result++;
            }
            queueOfTrailingDays.offer(money.get(i));
            high = 2 * findMedian(queueOfTrailingDays);
        }

        System.out.println(result);
    }
}
