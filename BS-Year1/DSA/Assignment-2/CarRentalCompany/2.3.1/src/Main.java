/**
 * @author Roman Molochkov
 * @Group #4
 * @Telegram: @roman_molochkov
 * @Email: r.molochkov@innopolis.university
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        String line;
        int countOperations = 0;

        if((line = bi.readLine()) != null) {
            countOperations = Integer.parseInt(line);
        }
        PriorityQueue<Double, String> queue = new PriorityQueue<>();
        FibonacciHeapNode<Double, String> tempNode = null;

        String tempString = null;
        String[] splitString;
        String command;
        String branchName;
        double penalty;

        while (countOperations != 0) {
            if((line = bi.readLine()) != null) {
                tempString = line;
            }

            assert tempString != null;
            splitString = tempString.split(" ");
            command = splitString[0];

            if (command.equals("ADD")) {
                branchName = splitString[1];
                penalty = Double.parseDouble(splitString[2]);
                tempNode = new FibonacciHeapNode<>(branchName, penalty);
                queue.insert(tempNode);
            } else if (command.equals("PRINT_MIN")) {
                tempNode = queue.extractMin();
                System.out.println(tempNode.data);
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

class PriorityQueue<K extends Comparable<K>, V extends Comparable<V>> implements IPriorityQueue<K, V> {
    private FibonacciHeapNode<K, V> min = null;
    private int n = 0;

    /**
     * Default constructor.
     */
    public PriorityQueue() {}

    /**
     * Method inserts a new Node to the right side of min Node.
     * @param x new Fibonacci Heap Node.
     */
    @Override
    public void insert(FibonacciHeapNode<K ,V> x) {
        if (min == null) {
            min = x;
        } else {
            x.left = min;
            x.right = min.right;
            min.right = x;
            x.right.left = x;

            if (x.key.compareTo(min.key) < 0) {
                min = x;
            } else if (x.key == min.key) {
                if (x.data.compareTo(min.data) < 0) {
                    min = x;
                }
            }
        }
        n++;
    }

    /**
     * Method finds min of the Priority Queue by returning min Node.
     * @return min Node of the Priority Queue.
     */
    @Override
    public FibonacciHeapNode<K, V> findMin() {
        return min;
    }

    /**
     * Extract min Node from the Priority Queue and finds new one min Node.
     * @return min node of Priority Queue.
     */
    @Override
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

    /**
     * Method decrease key of the given Node x in a Priority Queue. If the new key is less or equal than key of the given node.
     * @param x Fibonacci Node.
     * @param key Some number.
     */
    @Override
    public void decreaseKey(FibonacciHeapNode x, Comparable key) {
        if (key.compareTo(x.key) < 0 || key.compareTo(x.key) == 0) {
            x.key = key;
            FibonacciHeapNode<K, V> y = x.parent;

            if ((y != null) && (x.key.compareTo(y.key) < 0 || x.key.compareTo(y.key) == 0)) {
                cut(x, y);
                cascadingCut(y);
            }

            if (x.key.compareTo(min.key) < 0) {
                min = x;
            } else {
                if (x.key == min.key) {
                    if (x.data.compareTo(min.data) < 0) {
                        min = x;
                    }
                }
            }
        }
    }

    /**
     * Delete the node from the Priority Queue.
     * @param x Fibonacci Node
     */
    @Override
    public void delete(FibonacciHeapNode<K, V> x) {
        decreaseKey(x, Double.NEGATIVE_INFINITY);
        extractMin();
    }

    /**
     * Union of two Priority Queues.
     * @param anotherQueue Priority Queue.
     */
    @Override
    public void union(PriorityQueue<K, V> anotherQueue) {
        if (anotherQueue != null) {
            if (min != null) {
                if (anotherQueue.min != null) {
                    min.right.left = anotherQueue.min.left;
                    anotherQueue.min.left.right = min.right;
                    min.right = anotherQueue.min;
                    anotherQueue.min.left = min;

                    if (anotherQueue.min.key.compareTo(min.key) < 0) {
                        min = anotherQueue.min;
                    } else {
                        if (anotherQueue.min.key == min.key) {
                            if (anotherQueue.min.data.compareTo(min.data) < 0) {
                                min = anotherQueue.min;
                            }
                        }
                    }
                }
            } else {
                min = anotherQueue.min;
            }
            n = n + anotherQueue.n;
        }
    }

    /**
     * Function consolidates the Nodes in a Priority Queue.
     */
    public void consolidate() {
        int arraySize = ((int) Math.ceil(Math.log(n) / Math.log(2))) + 1;
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

                if (x.key.compareTo(y.key) > 0) {
                    FibonacciHeapNode<K, V> temp = y;
                    y = x;
                    x = temp;
                } else {
                    if (x.key.compareTo(y.key)  == 0) {
                        if (x.data.compareTo(y.data) > 0) {
                            FibonacciHeapNode<K, V> temp = y;
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

                if (y.key.compareTo((K) min.key) < 0) {
                    min = y;
                } else {
                    if (y.key.compareTo((K) min.key) == 0) {
                        if (y.data.compareTo((V) min.data) < 0) {
                            min = y;
                        }
                    }
                }
            } else {
                min = y;
            }
        }
    }

    /**
     * Function removing the Node x from the child of y Node and putting x Node to the right side of root.
     * @param x Fibonacci node child of y.
     * @param y Fibonacci node.
     */
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

    /**
     * First case, if from the Node x are not cut the child Node, than mark it.
     * <br>Second case, If Node is marked, than cut it and make cascading cut to its parent.
     * @param x Fibonacci Node
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

    /**
     * Function making the Node x a parent of Node y,
     * @param y Fibonacci Node
     * @param x Fibonacci Node
     */
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

    /**
     * @return size of the Priority Queue.
     */
    public int size() {
        return n;
    }

    /**
     * @return if the Priority Queue is empty or not.
     */
    boolean isEmpty() {
        return size() == 0;
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

    /**
     * Constructor of the class FibonacciHeapNode.
     * @param key is some number.
     * @param data some data of the task.
     */
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
