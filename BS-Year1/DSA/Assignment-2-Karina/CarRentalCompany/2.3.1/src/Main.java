/**
 * program for solving task C. Car rental company (min-heap test)
 * print name of a minimum branch, using priority queue
 * @author Kdrina Denisova
 * Group №7
 * TG: @karinadenisova
 * Email: k.denisova@innopolis.university
 */

import java.util.*;

public class Main {

    /**
     * main function of a program
     * @param args not used
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int count = Integer.parseInt(input.nextLine());
        PriorityQueue<Double, String> priorityQueue = new PriorityQueue<>();
        node<Double, String> tempNode;

        String tmpstr;
        String[] splitArray;
        String command;
        String name;
        double penalty;

        while (count != 0) {
            tmpstr = input.nextLine();
            splitArray = tmpstr.split(" ");
            command = splitArray[0];


            // check for identify which command is in line. Then do operation depending on command
            switch (command) {
                case "ADD":
                    name = splitArray[1];
                    penalty = Double.parseDouble(splitArray[2]);
                    tempNode = new node<>(name, penalty);
                    priorityQueue.insert(tempNode);
                    break;
                case "PRINT_MIN":
                    tempNode = priorityQueue.extractMin();
                    System.out.println(tempNode.data);
                    break;
                default:
                    break;
            }

            count--;
        }
    }
}

/**
 * just given interface
 * @param <K> key
 * @param <V> value
 */
interface IPriorityQueue<K extends Comparable<K>, V extends Comparable<V>> {
    void insert(node<K, V> x);

    node<K, V> findMin();

    node<K, V> extractMin();

    void decreaseKey(node<K, V> x, K key);

    void delete(node<K, V> x);

    void union(PriorityQueue<K, V> anotherQueue);
}

/**
 * Priority Queue(Fibonacci Heap), implementation taken from book
 * @param <K> key
 * @param <V> value
 */
class PriorityQueue<K extends Comparable<K>, V extends Comparable<V>> implements IPriorityQueue<K, V> {
    private node<K, V> minRoot = null;
    private int n = 0;

    public PriorityQueue() {}

    /**
     * methid for insertion( inserted to the right side min node) nodes in a heap
     * @param x node to insert
     */
    @Override
    public void insert(node<K ,V> x) {
        if (minRoot == null) {
            minRoot = x;
        } else {
            x.right = minRoot;
            x.left = minRoot.left;
            minRoot.left = x;
            x.left.right = x;

            if (x.key.compareTo(minRoot.key) < 0 || (x.key == minRoot.key && x.data.compareTo(minRoot.data) < 0)) {
                minRoot = x;
            }
        }
        n++;
    }

    /**
     * method for finding min node
     * @return min node
     */
    @Override
    public node<K, V> findMin() {
        return minRoot;
    }

    /**
     * method for extracting min node from fib. heap
     * @return min element from fib. heap
     */
    @Override
    public node<K, V> extractMin() {
        node<K, V> x = minRoot;

        if (x != null) {
            int kids = x.degree;
            node<K, V> y = x.child;
            node<K, V> tmpleft;

            while (kids > 0) {
                tmpleft = y.left;

                y.left.right = y.right;
                y.right.left = y.left;

                y.right = minRoot;
                y.left = minRoot.left;
                minRoot.left = y;
                y.left.right = y;

                y.parent = null;
                y = tmpleft;
                kids--;
            }

            x.left.right = x.right;
            x.right.left = x.left;

            if (x == x.left) {
                minRoot = null;
            } else {
                minRoot = x.left;
                consolidate();
            }
            n--;
        }
        return x;
    }

    /**
     * method for decreasing key in case if new key <= key of given node
     * @param x node in a heap
     * @param key new key
     */
    @Override
    public void decreaseKey(node x, Comparable key) {
        if (key.compareTo(x.key) < 0 || key.compareTo(x.key) == 0) {
            x.key = key;
            node<K, V> y = x.parent;

            if ((y != null) && (x.key.compareTo(y.key) <= 0)) {
                cut(x, y);
                cascadingCut(y);
            }

            if (x.key.compareTo(minRoot.key) < 0 || (x.key == minRoot.key && x.data.compareTo(minRoot.data) < 0)) {
                minRoot = x;
            }
        }
    }

    /**
     * method for deleting node from heap
     * @param x node in a heap
     */
    @Override
    public void delete(node<K, V> x) {
        decreaseKey(x, Double.NEGATIVE_INFINITY);
        extractMin();
    }

    /**
     * method for merging two heaps
     * @param anotherQueue another heap
     */
    @Override
    public void union(PriorityQueue<K, V> anotherQueue) {
        if (anotherQueue != null) {
            if (minRoot != null) {
                if (anotherQueue.minRoot != null) {
                    minRoot.left.right = anotherQueue.minRoot.left;
                    anotherQueue.minRoot.right.left = minRoot.left;
                    minRoot.left = anotherQueue.minRoot;
                    anotherQueue.minRoot.right = minRoot;

                    if (anotherQueue.minRoot.key.compareTo(minRoot.key) < 0 || (anotherQueue.minRoot.key == minRoot.key &&anotherQueue.minRoot.data.compareTo(minRoot.data) < 0)) {
                        minRoot = anotherQueue.minRoot;
                    }
                }
            } else {
                minRoot = anotherQueue.minRoot;
            }
            n = n + anotherQueue.n;
        }
    }

    /**
     * consolidate nodes in a heap
     */
    public void consolidate() {
        int arraySize = ((int) Math.ceil(Math.log(n) / Math.log(2))) + 1;
        List<node<K, V>> arr = new ArrayList<>(arraySize);

        for (int i = 0; i < arraySize; i++) {
            arr.add(null);
        }

        int numRoots = 0;
        node<K, V> x = minRoot;

        if (x != null) {
            numRoots++;
            x = x.left;

            while (x != minRoot) {
                numRoots++;
                x = x.left;
            }
        }

        while (numRoots > 0) {
            int d = x.degree;
            node<K, V> next = x.left;

            for (; ; ) {
                node<K, V> y = arr.get(d);
                if (y == null) {
                    break;
                }

                if (x.key.compareTo(y.key) > 0) {
                    node<K, V> temp = y;
                    y = x;
                    x = temp;
                } else {
                    if (x.key.compareTo(y.key)  == 0) {
                        if (x.data.compareTo(y.data) > 0) {
                            node<K, V> temp = y;
                            y = x;
                            x = temp;
                        }
                    }
                }

                fibHeapLink(y, x);

                arr.set(d, null);
                d++;
            }

            arr.set(d, x);

            x = next;
            numRoots--;
        }

        minRoot = null;

        for (int i = 0; i < arraySize; i++) {
            node<K, V> y = arr.get(i);
            if (y == null) {
                continue;
            }

            if (minRoot != null) {
                y.left.right = y.right;
                y.right.left = y.left;

                y.right = minRoot;
                y.left = minRoot.left;
                minRoot.left = y;
                y.left.right = y;

                if (y.key.compareTo((K) minRoot.key) < 0 || (y.key.compareTo((K) minRoot.key) == 0 && y.data.compareTo((V) minRoot.data) < 0)) {
                    minRoot = y;
                }
            } else {
                minRoot = y;
            }
        }
    }

    /**
     * remove the node x from the child of y node and putting x node to the left side of root
     * @param x node, child of y
     * @param y node
     */
    public void cut(node<K, V> x, node<K, V> y) {
        x.left.right = x.right;
        x.right.left = x.left;
        y.degree--;

        if (y.child == x) {
            y.child = x.left;
        }

        if (y.degree == 0) {
            y.child = null;
        }

        x.right = minRoot;
        x.left = minRoot.left;
        minRoot.left = x;
        x.left.right = x;

        x.parent = null;
        x.mark = false;
    }

    /**
     * cut until the node is not marked
     * @param x node
     */
    public void cascadingCut(node<K, V> x) {

        if (x.parent != null) {
            if (!x.mark) {
                x.mark = true;
            } else {
                cut(x, x.parent);

                cascadingCut(x.parent);
            }
        }
    }

    /**
     * makes the node x a parent of node y,
     * @param y node
     * @param x node
     */
    public void fibHeapLink(node<K, V> y, node<K, V> x) {
        y.left.right = y.right;
        y.right.left = y.left;

        y.parent = x;

        if (x.child == null) {
            x.child = y;
            y.right = y;
            y.left = y;
        } else {
            y.right = x.child;
            y.left = x.child.left;
            x.child.left = y;
            y.left.right = y;
        }

        x.degree++;

        y.mark = false;
    }

    /**
     * @return size of neap
     */
    public int size() {
        return n;
    }

    /**
     * @return empty or not
     */
    boolean isEmpty() {
        return size() == 0;
    }
}

class node<K extends Comparable<K>, V extends Comparable<V>> {
    V data;
    node<K, V> child;
    node<K, V> left;
    node<K, V> parent;
    node<K, V> right;
    boolean mark;
    K key;
    int degree;

    public node(V data, K key) {
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
