/*
@author Roman Molochkov
Group #4
Telegram: @roman_molochkov
Email: r.molochkov@innopolis.university
 */

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int countOperations = Integer.parseInt(scanner.nextLine());
        PriorityQueue<Double, String> queue = new PriorityQueue<>();
        FibonacciHeapNode<Double, String> tempNode = null;

        String tempString;
        String[] splitString;
        String command;
        String branchName;
        double penalty;

        while (countOperations != 0) {
            tempString = scanner.nextLine();
            splitString = tempString.split(" ");
            command = splitString[0];

            switch (command) {
                case "ADD":
                    branchName = splitString[1];
                    penalty = Double.parseDouble(splitString[2]);
                    tempNode = new FibonacciHeapNode<>(branchName, penalty);
                    queue.insert(tempNode);
                    break;
                case "PRINT_MIN":
                    tempNode = queue.extractMin();
                    System.out.println(tempNode.data);
                    break;
                default:
                    break;
            }

            countOperations--;
        }
    }
}

interface IPriorityQueue<K extends Comparable<K>, V extends Comparable<V>> {
    void insert(FibonacciHeapNode<K, V> x);

    FibonacciHeapNode<K, V> findMin();

    FibonacciHeapNode<K, V> extractMin();

    void decreaseKey(FibonacciHeapNode<K, V> x, K key);

    void delete(FibonacciHeapNode<K, V> x);

    void union(PriorityQueue<K, V> anotherQueue);
}

class PriorityQueue<K extends Comparable<K>, V extends Comparable<V>> implements IPriorityQueue {
    private FibonacciHeapNode<K, V> min = null;
    private int n = 0;

    public PriorityQueue() {}

    // Insert a new Node to the right side of min Node
    @Override
    public void insert(FibonacciHeapNode x) {
        if (min == null) {
            min = x;
        } else {
            x.left = min;
            x.right = min.right;
            min.right = x;
            x.right.left = x;

            if (x.key.compareTo(min.key) < 0 || (x.key == min.key && x.data.compareTo(min.data) < 0)) {
                min = x;
            }
        }
        n++;
    }

    @Override
    // Find min of the heap by returning min Node
    public FibonacciHeapNode<K, V> findMin() {
        return min;
    }

    @Override
    // Extract min from the heap
    public FibonacciHeapNode<K, V> extractMin() {
        FibonacciHeapNode<K, V> x = min;

        if (x != null) {
            int numKids = x.degree;
            FibonacciHeapNode<K, V> y = x.child;
            FibonacciHeapNode<K, V> tempRight;

            while (numKids > 0) {
                tempRight = y.right;

                y.left.right = y.right;
                y.right.left = y.left;

                y.left = min;
                y.right = min.right;
                min.right = y;
                y.right.left = y;

                y.parent = null;
                y = tempRight;
                numKids--;
            }

            x.left.right = x.right;
            x.right.left = x.left;

            if (x == x.right) {
                min = null;
            } else {
                min = x.right;
                consolidate();
            }
            n--;
        }
        return x;
    }

    @Override
    // If the new key is less or equal than key of the given node
    public void decreaseKey(FibonacciHeapNode x, Comparable key) {
        if (key.compareTo(x.key) < 0 || key.compareTo(x.key) == 0) {
            x.key = key;
            FibonacciHeapNode y = x.parent;

            if ((y != null) && (x.key.compareTo(y.key) < 0 || x.key.compareTo(y.key) == 0)) {
                cut(x, y);
                cascadingCut(y);
            }

            if (x.key.compareTo(min.key) < 0 || (x.key == min.key && x.data.compareTo(min.data) < 0)) {
                min = x;
            }
        }
    }

    @Override
    // Delete the node from the heap
    public void delete(FibonacciHeapNode x) {
        decreaseKey(x, Double.NEGATIVE_INFINITY);
        extractMin();
    }

    @Override
    // Union of two Fibonacci Heaps
    public void union(PriorityQueue anotherQueue) {
        if (anotherQueue != null) {
            if (min != null) {
                if (anotherQueue.min != null) {
                    min.right.left = anotherQueue.min.left;
                    anotherQueue.min.left.right = min.right;
                    min.right = anotherQueue.min;
                    anotherQueue.min.left = min;

                    if (anotherQueue.min.key.compareTo(min.key) < 0 || (anotherQueue.min.key == min.key && anotherQueue.min.data.compareTo(min.data) < 0)) {
                        min = anotherQueue.min;
                    }
                }
            } else {
                min = anotherQueue.min;
            }
            n = n + anotherQueue.n;
        }
    }

    // Consolidate the heap
    public void consolidate() {
        int arraySize = ((int) Math.floor(Math.log(n) / Math.log((1.0 + Math.sqrt(5.0)) / 2.0))) + 1;
        List<FibonacciHeapNode<K, V>> arr = new ArrayList<>(arraySize);

        for (int i = 0; i < arraySize; i++) {
            arr.add(null);
        }

        int numRoots = 0;
        FibonacciHeapNode<K, V> x = min;

        if (x != null) {
            numRoots++;
            x = x.right;

            while (x != min) {
                numRoots++;
                x = x.right;
            }
        }

        while (numRoots > 0) {
            int d = x.degree;
            FibonacciHeapNode<K, V> next = x.right;

            for (; ; ) {
                FibonacciHeapNode<K, V> y = arr.get(d);
                if (y == null) {
                    break;
                }

                if (x.key.compareTo(y.key) > 0 || (x.key.compareTo(y.key)  == 0 && x.data.compareTo(y.data) > 0)) {
                    FibonacciHeapNode<K, V> temp = y;
                    y = x;
                    x = temp;
                }

                fibHeapLink(y, x);

                arr.set(d, null);
                d++;
            }

            arr.set(d, x);

            x = next;
            numRoots--;
        }

        min = null;

        for (int i = 0; i < arraySize; i++) {
            FibonacciHeapNode<K, V> y = arr.get(i);
            if (y == null) {
                continue;
            }

            if (min != null) {
                y.left.right = y.right;
                y.right.left = y.left;

                y.left = min;
                y.right = min.right;
                min.right = y;
                y.right.left = y;

                if (y.key.compareTo(min.key) < 0 || (y.key == min.key && y.data.compareTo(min.data) < 0)) {
                    min = y;
                }
            } else {
                min = y;
            }
        }
    }

    // Removing the Node x from the child of y Node and putting X Node to the right side of root
    public void cut(FibonacciHeapNode<K, V> x, FibonacciHeapNode<K, V> y) {
        x.left.right = x.right;
        x.right.left = x.left;
        y.degree--;

        if (y.child == x) {
            y.child = x.right;
        }

        if (y.degree == 0) {
            y.child = null;
        }

        x.left = min;
        x.right = min.right;
        min.right = x;
        x.right.left = x;

        x.parent = null;
        x.mark = false;
    }

    /*
     - First case, If from the Node x are not cut the child Node, than mark it
     - Second case, If Node is marked, than cut it and make cascading cut to it's parent
     */
    public void cascadingCut(FibonacciHeapNode<K, V> x) {

        if (x.parent != null) {
            if (!x.mark) {
                x.mark = true;
            } else {
                cut(x, x.parent);

                cascadingCut(x.parent);
            }
        }
    }

    // Making the Node x a parent of Node y
    public void fibHeapLink(FibonacciHeapNode<K, V> y, FibonacciHeapNode<K, V> x) {
        y.left.right = y.right;
        y.right.left = y.left;

        y.parent = x;

        if (x.child == null) {
            x.child = y;
            y.right = y;
            y.left = y;
        } else {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right = y;
            y.right.left = y;
        }

        x.degree++;

        y.mark = false;
    }

    // Return size of the heap
    public int size() {
        return n;
    }
}

// Fibonacci Heap Node
class FibonacciHeapNode<K extends Comparable<K>, V extends Comparable<V>> {
    final V data;
    FibonacciHeapNode<K, V> child;
    FibonacciHeapNode<K, V> left;
    FibonacciHeapNode<K, V> parent;
    FibonacciHeapNode<K, V> right;
    boolean mark;
    K key;
    int degree;

    public FibonacciHeapNode(V data, K key) {
        right = this;
        left = this;
        this.data = data;
        this.key = key;
        this.degree = 0;
        this.parent = null;
        this.child = null;
        this.mark = false;
    }
}
