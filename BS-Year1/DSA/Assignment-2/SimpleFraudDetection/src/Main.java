/*
@author Roman Molochkov
Group #4
Telegram: @roman_molochkov
Email: r.molochkov@innopolis.university
 */

import javafx.util.Pair;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    // Milliseconds in a one day
    private static final long MS_IN_A_DAY = 1000 * 60 * 60 * 24;

    public static Double getMax(List<Double> list) {
        // initialize `min` to some maximum value
        Double max = Double.MIN_VALUE;

        // loop through every element in the list and
        // compare the minimum found so far with the current value
        for (Double i: list)
        {
            // update min if found to be more than the current element
            if (max < i) {
                max = i;
            }
        }

        return max;
    }

    public static Double getMin(List<Double> list) {
        // initialize `min` to some maximum value
        Double min = Double.MAX_VALUE;

        // loop through every element in the list and
        // compare the minimum found so far with the current value
        for (Double i: list)
        {
            // update min if found to be more than the current element
            if (min > i) {
                min = i;
            }
        }

        return min;
    }

    /* Function finds next date */
    private static Date findNextDate(Date date) {
        return new Date(date.getTime() + MS_IN_A_DAY);
    }

    /* Function finds previous date */
    private static Date findPrevDate(Date date) {
        return new Date(date.getTime() - MS_IN_A_DAY);
    }

    /* Merge function that merges two parts in the right order */
    public static void Merge(Vector<Pair<Date, Double>> pairs, final int begin, final int mid, final int end) {
        int len1 = mid - begin + 1; // Length of the left part
        int len2 = end - mid; // Length of the right part
        Vector<Pair<Date, Double>> L = new Vector<>(len1 + 1);
        Vector<Pair<Date, Double>> R = new Vector<>(len2 + 1);

        for (int i = 0; i < len1; ++i) {
            L.add(i, pairs.get(begin + i));
        }
        for (int j = 0; j < len2; ++j) {
            R.add(j, pairs.get(mid + j + 1));
        }

        L.add(len1, new Pair<>(null, null));
        R.add(len2, new Pair<>(null, null));

        // Merging
        int i = 0, j = 0;
        for (int k = begin; k <= end; k++) {
            if (R.get(j).getKey() != null && L.get(i).getKey() != null && (L.get(i).getKey().compareTo(R.get(j).getKey()) < 0 || L.get(i).getKey().compareTo(R.get(j).getKey()) == 0)) {
                pairs.set(k, L.get(i));
                i++;
            } else if (R.get(j).getKey() == null) {
                pairs.set(k, L.get(i));
                i++;
            } else {
                pairs.set(k, R.get(j));
                j++;
            }
        }
    }

    /* Merge sort */
    public static void MergeSort(Vector<Pair<Date, Double>> pairs, final int begin, final int end) {
        if (begin < end) {
            int mid = (begin + end) / 2; // Finding middle index

            MergeSort(pairs, begin, mid); // Sorting left part
            MergeSort(pairs, mid + 1, end); // Sorting right part
            Merge(pairs, begin, mid, end); // Merging parts together
        }
    }

    public static void InsertionSort(Vector<Double> arr){

        int n = arr.size();
        for (int i = 1; i < n; ++i) {
            double key = arr.get(i);
            int j = i - 1;

            while (j >= 0 && arr.get(j) > key) {
                arr.set(j+1,arr.get(j));
                j = j - 1;
            }
            arr.set(j + 1, key);
        }

    }

    public static void BucketSort(List<Double> tempList){
        int n  = tempList.size();
        double max = getMax(tempList);
        double min = getMin(tempList);
        double range = (max - min)/n;

        Vector<Double>[] buckets = new Vector[n];

        for (int i = 0; i < n; i++) {
            buckets[i] = new Vector<Double>();
        }

        for (Double aDouble : tempList) {
            double dif = (aDouble - min)/range - (int)((aDouble - min)/range);
            if(dif == 0.0 && aDouble != min){
                buckets[(int)((aDouble - min)/range) - 1].add(aDouble);
            }
            else{
                buckets[(int)((aDouble - min)/range)].add(aDouble);
            }
        }

        for (int i = 0; i < n; i++) {
            InsertionSort(buckets[i]);
        }


        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < buckets[i].size(); j++) {
                tempList.set(index++,buckets[i].get(j));
            }
        }
    }

    /* Function finds median */
    public static double findMedian(LinkedCircularBoundedQueue<Double> queue) {
        LinkedCircularBoundedQueue<Double> tempQueue = new LinkedCircularBoundedQueue<>(queue);
        List<Double> tempList = new ArrayList<>();

        while (!tempQueue.isEmpty()) {
            tempList.add(tempQueue.peek());
            tempQueue.poll();
        }

       BucketSort(tempList);

        if (tempList.size() % 2 == 0) {
            return (tempList.get(tempList.size() / 2 - 1) + tempList.get(tempList.size() / 2)) / 2;
        } else {
            return tempList.get((tempList.size() - 1) / 2);
        }
    }

    /* Function counts number of notifications */
    public static int countNotifications(Vector<Pair<Date, Double>> pairs, int trailingDays) {
        LinkedCircularBoundedQueue<Double> queueOfTrailingDays = new LinkedCircularBoundedQueue<>(trailingDays); // Queue for trailing days
        int notifications = 0; // Number of notifications
        double spent = 0; // Spent amount
        int count = 0;
        Date prevDate = null; // Previous date

        // Add to vector dates with zero spent
        for (int i = 0; i < pairs.size(); i++) {
            Date date = pairs.get(i).getKey();
            if (!(prevDate == null || prevDate.compareTo(findPrevDate(date)) == 0 || prevDate.compareTo(date) == 0)) {
                while (prevDate.compareTo(findPrevDate(date)) != 0) {
                    prevDate = findNextDate(prevDate);
                    pairs.add(i, new Pair<>(prevDate, (double) 0));
                    i++;
                }
            }
            prevDate = date;
        }

        int i;
        for (i = 0; count < trailingDays && i < pairs.size() - 1; i++) {
            if (pairs.get(i).getKey().equals(pairs.get(i + 1).getKey())) {
                spent += pairs.get(i).getValue();
            } else {
                spent += pairs.get(i).getValue();
                queueOfTrailingDays.offer(spent);
                spent = 0;
                count++;
            }
        }

        spent = 0;
        prevDate = pairs.get(trailingDays - 1).getKey();
        for (; i < pairs.size(); i++) {
            double high = 2 * findMedian(queueOfTrailingDays);
            if (!prevDate.equals(pairs.get(i).getKey()) && pairs.get(i).getValue() >= high) {
                prevDate = pairs.get(i).getKey();
                spent = pairs.get(i).getValue();
                if(pairs.get(i).getValue() != 0 ) {
                    notifications += 1;
                }
                if (i == pairs.size() - 1 || !pairs.get(i + 1).getKey().equals(pairs.get(i).getKey())) {
                    queueOfTrailingDays.offer(spent);
                    spent = 0;
                }
            } else if (prevDate.equals(pairs.get(i).getKey())) {
                spent += pairs.get(i).getValue();
                if (spent >= high) {
                    notifications += 1;
                }
                if (i == pairs.size() - 1 || !pairs.get(i + 1).getKey().equals(pairs.get(i).getKey())) {
                    queueOfTrailingDays.offer(spent);
                    spent = 0;
                }
            } else {
                prevDate = pairs.get(i).getKey();
                spent = pairs.get(i).getValue();
                if (i == pairs.size() - 1 || !pairs.get(i + 1).getKey().equals(pairs.get(i).getKey())) {
                    queueOfTrailingDays.offer(spent);
                    spent = 0;
                }
            }
        }
        return notifications;
    }

    /* Function reads input */
    public static int input(Vector<Pair<Date, Double>> pairs) throws ParseException {
        Scanner scanner = new Scanner(System.in);

        // Scan first line of input
        String[] tempStr = scanner.nextLine().split(" ");
        int lines = Integer.parseInt(tempStr[0]);
        int trailingDays = Integer.parseInt(tempStr[1]);

        // Filling vector
        while (lines > 0) {
            String temp = scanner.nextLine();
            tempStr = temp.split(" \\$");

            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tempStr[0]);
            double spentAmount = Double.parseDouble(tempStr[1]);

            pairs.add(new Pair<>(date, spentAmount));

            lines--;
        }
        return trailingDays;
    }

    /* Main function */
    public static void main(String[] args) throws ParseException {
        Vector<Pair<Date, Double>> pairs = new Vector<>();
        int trailingDays = input(pairs); // Input

        MergeSort(pairs, 0, pairs.size() - 1); // Sorting the vector by date

        int result = countNotifications(pairs, trailingDays); // Counting notifications

        System.out.println(result); // Output the result
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
