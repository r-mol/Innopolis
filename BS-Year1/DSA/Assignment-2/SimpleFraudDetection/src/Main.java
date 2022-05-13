/**
 * @author Roman Molochkov
 * @Group #4
 * @Telegram: @roman_molochkov
 * @Email: r.molochkov@innopolis.university
 */
import javafx.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    private static final long MS_IN_A_DAY = 1000 * 60 * 60 * 24;
    static int trailingDays = 0;
    /**
     * The main function, which use methods:
     *  <pre>        1. input()
     *  2. countingSort()
     *  3. countNotification()</pre>
     * @param args not used.
     * @throws ParseException When there is error of parsing String to class Date in method input().
     */
    public static void main(String[] args) throws ParseException {
        Vector<Pair<Date, Double>> pairs = new Vector<>();

        // Input
        input(pairs);

        // Sorting the vector by date
        countingSort(pairs);

        // Counting of a notifications
        int result = countNotifications(pairs);

        // Output the result
        System.out.println(result);
    }

    /**
     * Function read input, by using String.
     * @param pairs Vector of pairs: Key - Date and Value - Double.
     * @throws ParseException  When there is error of parsing String to class Date.
     */
    public static void input(Vector<Pair<Date, Double>> pairs) throws ParseException {
        Scanner scanner = new Scanner(System.in);

        // Scan and parse the first line of the input
        String[] tempStr = scanner.nextLine().split(" ");
        int lines = Integer.parseInt(tempStr[0]);
        trailingDays = Integer.parseInt(tempStr[1]);

        // Filling the Vector with Pairs
        while (lines > 0) {
            String temp = scanner.nextLine();
            tempStr = temp.split(" \\$");

            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tempStr[0]);
            double spentAmount = Double.parseDouble(tempStr[1]);

            pairs.add(new Pair<>(date, spentAmount));

            lines--;
        }
    }

    /**
     * Standard Counting Sort, but adapted for Vector of pairs. At first convert Date to one number
     * and add it to listOfDays. Secondly count of numbers of days in a listOfDays. After increase the
     * next number of day on number of previous day to get indexes from and to of one day.
     * @param pairs Vector of pairs: Key - Date and Value - Double.
     */
    public static void countingSort(Vector<Pair<Date, Double>> pairs) {
        List<Integer> listOfDays = new ArrayList<>();
        Vector<Pair<Date, Double>> output = new Vector<>();
        output.setSize(pairs.size());

        // Add all converted days to list
        for (int i = 0; i < pairs.size(); i++) {
            listOfDays.add(i, convertToDays(pairs.get(i).getKey()));
        }

        int minimumElement = getMin(listOfDays);
        int maximumElement = getMax(listOfDays);
        int range = maximumElement - minimumElement;
        int[] counting = new int[range + 1];

        // Counting the amount of a repeating of a date in a list
        for (int i = 0; i < pairs.size(); ++i) {
            ++counting[listOfDays.get(i) - minimumElement];
        }

        // Get the position of date in a Vector
        for (int i = 1; i <= range; ++i) {
            counting[i] += counting[i - 1];
        }

        /*
        - Find the date on position "i" in listOfDays
        - Put this date to output vector
        - Decrease the amount of repeating
         */

        for (int i = pairs.size() - 1; i >= 0; i--) {
            output.add(counting[listOfDays.get(i) - minimumElement] - 1, new Pair<>(pairs.get(i).getKey(), pairs.get(i).getValue()));
            output.remove(counting[listOfDays.get(i) - minimumElement]);
            --counting[listOfDays.get(i) - minimumElement];
        }

        // Put the sorted dates to the initial Vector
        for (int i = 0; i < pairs.size(); i++) {
            pairs.set(i, output.get(i));
        }

    }

    /**
     * function that count the number of alerts the Bank sent to the client.
     * @param pairs array list that keep pair of variable - date and count of money
     * @return contain number of notification the Bank sent to client (include "empty" days)
     */
    public static int countNotifications(List<Pair<Date, Double>> pairs) {
        LinkedCircularBoundedQueue<Double> queueOfSpending = new LinkedCircularBoundedQueue<>(trailingDays);
        int alerts;
        int index;

        fillDays(pairs);

        index = firstFillOfQueue(pairs,queueOfSpending);

        alerts = mainCountingOfAlerts(index,pairs,queueOfSpending);

        return alerts;
    }

    /**
     * fill the list with pairs by days with the spending 0 which where are not in list.
     * @param pairs array list that keep pair of variable - date and count of money
     */
    public static void fillDays(List<Pair<Date, Double>> pairs){
        Date previous = null;
        for (int i = 0; i < pairs.size(); i++) {
            Date day = pairs.get(i).getKey();
            if (!(previous == null || previous.compareTo(findPrevDate(day)) == 0 || previous.compareTo(day) == 0)) {
                while (previous.compareTo(findPrevDate(day)) != 0) {
                    previous = findNextDate(previous);
                    pairs.add(i, new Pair<>(previous, (double) 0));
                    i++;
                }
            }
            previous = day;
        }
    }

    /**
     * fill queue with the spending in a range of the number of the trailing days.
     * @param pairs array list that keep pair of variable - date and count of money
     * @param queueOfSpending contain amount of spent by client money, sorted by days(that function also sorted it by money), for trailing days that Bank tracking
     * @return last index
     */
    public static int firstFillOfQueue(List<Pair<Date, Double>> pairs, LinkedCircularBoundedQueue<Double> queueOfSpending){
        double spending = 0;
        int count = 0;
        int index;

        for (index = 0; count < trailingDays && index < pairs.size() - 1; index++) {
            if (pairs.get(index).getKey().equals(pairs.get(index + 1).getKey())) {
                spending += pairs.get(index).getValue();
            } else {
                spending += pairs.get(index).getValue();
                queueOfSpending.offer(spending);
                spending = 0;
                count++;
            }
        }
        return index;
    }

    /**
     * the main function which count amount of alerts in a queue of days
     * @param index index from which program should begin work
     * @param pairs array list that keep pair of variable - date and count of money
     * @param queueOfSpending contain amount of spent by client money, sorted by days(that function also sorted it by money), for trailing days that Bank tracking
     * @return the amount of alerts
     */
    public static int mainCountingOfAlerts(int index,List<Pair<Date, Double>> pairs, LinkedCircularBoundedQueue<Double> queueOfSpending){
        Date previous = pairs.get(trailingDays - 1).getKey();
        Date initial ;
        double spending = 0;
        int alerts = 0;
        for (; index < pairs.size(); index++) {
            double limit = 2 * findMedian(queueOfSpending);
            initial = pairs.get(index).getKey();

            if (!previous.equals(initial) && pairs.get(index).getValue() >= limit) {
                previous = initial;
                spending = pairs.get(index).getValue();

                if (pairs.get(index).getValue() != 0) {
                    alerts += 1;
                }
            } else if (previous.equals(initial)) {
                spending += pairs.get(index).getValue();

                if (spending >= limit) {
                    alerts += 1;
                }
            } else {
                previous = initial;
                spending = pairs.get(index).getValue();
            }

            if (index == pairs.size() - 1 || !pairs.get(index + 1).getKey().equals(initial)) {
                queueOfSpending.offer(spending);
                spending = 0;
            }
        }
        return alerts;
    }

    /**
     * Function finds median of a spending in a range of trailing days. For the sorting spending in a queue, at first
     * transfer data from Queue to a ArrayList and after sort in by using Merge Sort.
     * @param queueOfSpending Linked Circular Bounded Queue with max capacity number of the trailing days.
     * @return Double, the median of a Queue.
     */
    public static double findMedian(LinkedCircularBoundedQueue<Double> queueOfSpending) {
        LinkedCircularBoundedQueue<Double> tempQueue = new LinkedCircularBoundedQueue<>(queueOfSpending);
        ArrayList<Double> tempListSpending = new ArrayList<>();

        // Add spending from the queue to the temp list for sorting
        while (!tempQueue.isEmpty()) {
            tempListSpending.add(tempQueue.peek());
            tempQueue.poll();
        }

        // Sorting
        mergeSort(tempListSpending);

        if (tempListSpending.size() % 2 == 0) {
            // If even amount of spending
            return (tempListSpending.get(tempListSpending.size() / 2 - 1) + tempListSpending.get(tempListSpending.size() / 2)) / 2;
        } else {
            // If odd amount of spending
            return tempListSpending.get((tempListSpending.size() - 1) / 2);
        }
    }

    /**
     * Standard Merge Sort, but for List of Double values.
     * @param tempList List of spending in one day.
     */
    public static void mergeSort(List<Double> tempList) {
        List<Double> left = new ArrayList<>();
        List<Double> right = new ArrayList<>();

        int mid;

        if (tempList.size() > 1) {
            mid = tempList.size() / 2;

            for (int i = 0; i < mid; i++) {
                left.add(tempList.get(i));
            }

            for (int j = mid; j < tempList.size(); j++) {
                right.add(tempList.get(j));
            }

            mergeSort(left);
            mergeSort(right);
            merge(tempList, left, right);
        }
    }

    /**
     * The merging of left and right sides.
     * @param tempList List of a result.
     * @param left List of the left side of the chosen range.
     * @param right List of the right side of the chosen range.
     */
    public static void merge(List<Double> tempList, List<Double> left, List<Double> right) {
        List<Double> temp;

        int numbersIndex = 0;
        int leftIndex = 0;
        int rightIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex) < right.get(rightIndex)) {
                tempList.set(numbersIndex, left.get(leftIndex));
                leftIndex++;
            } else {
                tempList.set(numbersIndex, right.get(rightIndex));
                rightIndex++;
            }
            numbersIndex++;
        }

        int tempIndex;
        if (leftIndex >= left.size()) {
            temp = right;
            tempIndex = rightIndex;
        } else {
            temp = left;
            tempIndex = leftIndex;
        }

        for (int i = tempIndex; i < temp.size(); i++) {
            tempList.set(numbersIndex, temp.get(i));
            numbersIndex++;
        }
    }

    /**
     * Function converts the date from standard class Date to the days.
     * @param date The day of this date.
     * @return One Integer number of the date.
     */
    public static int convertToDays(Date date) {
        return (int) (date.getTime() / 1000 / 60 / 60 / 24);
    }

    /**
     * Function finds next Date by using sum of converting date to ms and MS_IN_A_DAY = {@value MS_IN_A_DAY}.
     * @param date This date to which need find next Date.
     * @return Next Date.
     */
    public static Date findNextDate(Date date) {
        return new Date(date.getTime() + MS_IN_A_DAY);
    }

    /**
     * Function finds previous Date by using sum of converting date to ms and MS_IN_A_DAY = {@value MS_IN_A_DAY}.
     * @param date This date to which need find previous Date.
     * @return Previous Date.
     */
    public static Date findPrevDate(Date date) {
        return new Date(date.getTime() - MS_IN_A_DAY);
    }

    /**
     * Function finds the maximum element in a List.
     * @param list List of a dates.
     * @return The maximum date.
     */
    public static Integer getMax(List<Integer> list) {
        // Initialize `min` to some maximum value.
        int max = Integer.MIN_VALUE;

        // Loop through every element in the list and compare the minimum found so far with the current value.
        for (Integer i : list) {
            // update min if found to be more than the current element.
            if (max < i) {
                max = i;
            }
        }

        return max;
    }

    /**
     * Function finds the minimum element in a List.
     * @param list List of a dates.
     * @return The minimum date.
     */
    public static Integer getMin(List<Integer> list) {
        // Initialize `min` to some maximum value.
        int min = Integer.MAX_VALUE;

        // Loop through every element in the list and compare the minimum found so far with the current value.
        for (Integer i : list) {
            // Update min if found to be more than the current element.
            if (min > i) {
                min = i;
            }
        }
        return min;
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
        Node<T> node = new Node<>(value);
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
