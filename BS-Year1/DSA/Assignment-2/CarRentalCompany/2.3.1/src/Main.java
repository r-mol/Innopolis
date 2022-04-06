import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int countOperations = Integer.parseInt(scanner.nextLine());
        FibonacciHeap<String> queue = new FibonacciHeap<>();
        Map<String,FibonacciHeapNode<String>> existing = new TreeMap();

        String tempString;
        String[] splitString;
        String command;
        String branchName;
        int penalty;
        FibonacciHeapNode<String> tempNode = null;

        while (countOperations != 0) {
            tempString = scanner.nextLine();
            splitString = tempString.split(" ");
            command = splitString[0];
            if (command.equals("ADD")) {
                branchName = splitString[1];
                penalty = Integer.parseInt(splitString[2]);

                if (!existing.containsKey(branchName)) {
                    tempNode = new FibonacciHeapNode<>(branchName, penalty);
                    existing.put(branchName,tempNode);
                    queue.insert(tempNode);
                } else {
                    queue.decreaseKey(existing.get(branchName), penalty);
                }
            } else if (command.equals("PRINT_MIN")) {

                tempNode = queue.extractMin();
                existing.remove(tempNode.data);
                System.out.println(tempNode.data);
            }

            countOperations--;
        }
    }
}

interface IPriorityQueue<V extends Comparable<V>> {
    void insert(FibonacciHeapNode<V> x);

    FibonacciHeapNode<V> findMin();

    FibonacciHeapNode<V> extractMin();

    void decreaseKey(FibonacciHeapNode<V> x, double key);

    void delete(FibonacciHeapNode<V> x);

    void union(FibonacciHeap<V> anotherQueue);
}

class FibonacciHeap<V extends Comparable<V>> implements IPriorityQueue {
    private static final double oneOverLogPhi = 1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0);
    private FibonacciHeapNode min = null;
    private int n = 0;

    public FibonacciHeap() {
    }

    @Override
    public void insert(FibonacciHeapNode x) {

        if (min == null) {
            min = x;
        } else {
            x.left = min;
            x.right = min.right;
            min.right = x;
            x.right.left = x;

            if (x.key < min.key) {
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
    public FibonacciHeapNode<V> findMin() {
        return min;
    }

    @Override
    public FibonacciHeapNode<V> extractMin() {
        FibonacciHeapNode<V> z = min;

        if (z != null) {
            int numKids = z.degree;
            FibonacciHeapNode<V> x = z.child;
            FibonacciHeapNode<V> tempRight;

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
    public void decreaseKey(FibonacciHeapNode x, double key) {
        if (key <= x.key) {
            x.key = key;
            FibonacciHeapNode y = x.parent;

            if ((y != null) && (x.key <= y.key)) {
                cut(x, y);
                cascadingCut(y);
            }

            if (x.key < min.key) {
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

                    if (anotherQueue.min.key < min.key) {
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
        int arraySize = ((int) Math.floor(Math.log(n) * oneOverLogPhi)) + 1;
        List<FibonacciHeapNode<V>> arr = new ArrayList<>(arraySize);

        for (int i = 0; i < arraySize; i++) {
            arr.add(null);
        }

        int numRoots = 0;
        FibonacciHeapNode<V> x = min;

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
            FibonacciHeapNode<V> next = x.right;

            for (; ; ) {
                FibonacciHeapNode<V> y = arr.get(d);
                if (y == null) {
                    break;
                }

                if (x.key > y.key) {
                    FibonacciHeapNode<V> temp = y;
                    y = x;
                    x = temp;
                }else {
                    if (x.key == y.key) {
                        if (x.data.compareTo(y.data) > 0) {
                            FibonacciHeapNode<V> temp = y;
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
            FibonacciHeapNode<V> y = arr.get(i);
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

                if (y.key < min.key) {
                    min = y;
                } else {
                    if (y.key == min.key) {
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

    public void fibHeapLink(FibonacciHeapNode<V> y, FibonacciHeapNode<V> x) {
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

    public void cut(FibonacciHeapNode<V> x, FibonacciHeapNode<V> y) {
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

    public void cascadingCut(FibonacciHeapNode<V> y) {
        FibonacciHeapNode<V> z = y.parent;

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

class FibonacciHeapNode<V extends Comparable<V>> {
    final V data;
    FibonacciHeapNode<V> child;
    FibonacciHeapNode<V> left;
    FibonacciHeapNode<V> parent;
    FibonacciHeapNode<V> right;
    boolean mark;
    double key;
    int degree;

    public FibonacciHeapNode(V data, double key) {
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
