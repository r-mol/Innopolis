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
        FibonacciHeap<Double, String> queue = new FibonacciHeap<>();
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

    void union(FibonacciHeap<K, V> anotherQueue);
}

class FibonacciHeap<K extends Comparable<K>, V extends Comparable<V>> implements IPriorityQueue {
    private FibonacciHeapNode min = null;
    private int n = 0;

    public FibonacciHeap() {}

    @Override
    public void insert(FibonacciHeapNode x) {
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


    @Override
    public FibonacciHeapNode<K, V> findMin() {
        return min;
    }

    @Override
    public FibonacciHeapNode<K, V> extractMin() {
        FibonacciHeapNode<K, V> z = min;

        if (z != null) {
            int numKids = z.degree;
            FibonacciHeapNode<K, V> x = z.child;
            FibonacciHeapNode<K, V> tempRight;

            while (numKids > 0) {
                tempRight = x.right;

                x.left.right = x.right;
                x.right.left = x.left;

                x.left = min;
                x.right = min.right;
                min.right = x;
                x.right.left = x;

                x.parent = null;
                x = tempRight;
                numKids--;
            }

            z.left.right = z.right;
            z.right.left = z.left;

            if (z == z.right) {
                min = null;
            } else {
                min = z.right;
                consolidate();
            }
            n--;
        }
        return z;
    }

    @Override
    public void decreaseKey(FibonacciHeapNode x, Comparable key) {
        if (key.compareTo(x.key) < 0 || key.compareTo(x.key) == 0) {
            x.key = key;
            FibonacciHeapNode y = x.parent;

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

    @Override
    public void delete(FibonacciHeapNode x) {
        decreaseKey(x, Double.NEGATIVE_INFINITY);
        extractMin();
    }

    @Override
    public void union(FibonacciHeap anotherQueue) {
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

    public void cascadingCut(FibonacciHeapNode<K, V> y) {
        FibonacciHeapNode<K, V> z = y.parent;

        if (z != null) {
            if (!y.mark) {
                y.mark = true;
            } else {
                cut(y, z);

                cascadingCut(z);
            }
        }
    }

    public int size() {
        return n;
    }
}

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
