/**
 * program for solving task C. Car rental company (min-heap test)
 * print name of a minimum branch, using priority queue
 * @author Kdrina Denisova
 * Group №7
 * TG: @karinadenisova
 * Email: k.denisova@innopolis.university
 */

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class EXGraph {

    /**
     * main function of a program
     * @param args not used
     */
    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        Map<String, Vertex> map = new HashMap<>();
        Graph graph = new Graph();
        String tmpstr = null;
        String[] splitArray;
        String branchName1;
        String branchName2;
        String command;
        String str;
        int count = 0;
        int distance;
        int penalty;

        if ((str = bi.readLine()) != null) {
            count = Integer.parseInt(str);
        }

        while (count != 0) {
            if ((str = bi.readLine()) != null) {
                tmpstr = str;
            }

            assert tmpstr != null;
            splitArray = tmpstr.split(" ");
            command = splitArray[0];

            switch (command) {
                case "ADD" :
                    branchName1 = splitArray[1];
                    penalty = Integer.parseInt(splitArray[2]);
                    map.put(branchName1, graph.insertVertex(new V(branchName1, penalty)));
                    break;
                case "CONNECT":
                    branchName1 = splitArray[1];
                    branchName2 = splitArray[2];
                    distance = Integer.parseInt(splitArray[3]);
                    Vertex vertex1 = map.get(branchName1);
                    Vertex vertex2 = map.get(branchName2);
                    graph.insertEdge(vertex1, vertex2, new E(distance / (vertex1.v.getPenalty() + vertex2.v.getPenalty())));
                    break;
                case "PRINT_MIN" : System.out.print(graph.PimAlgo() + "\n"); break;
            }

            count--;
        }
    }
}

interface IGraph {
    Vertex insertVertex(V v);

    Edge insertEdge(Vertex from, Vertex to, E w);

    void removeVertex(Vertex v);

    void removeEdge(Edge e);

    boolean areAdjacent(Vertex v, Vertex u);

    int degree(Vertex v);

}

class V {
    private final String name;
    private final double penalty;

    /**
     * Constructor class V.
     *
     * @param name    branch name of vertex.
     * @param penalty penalty of vertex.
     */
    public V(String name, int penalty) {
        this.name = name;
        this.penalty = penalty;
    }

    /**
     * Getter of Name.
     *
     * @return the branch name of vertex.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter of Penalty.
     *
     * @return the penalty of vertex.
     */
    public double getPenalty() {
        return penalty;
    }
}

class E {
    private final double weight;

    /**
     * Constructor of the class E.
     *
     * @param weight weight of the Edge.
     */
    public E(double weight) {
        this.weight = weight;
    }

    /**
     * Getter of Weight.
     *
     * @return the weight of edge.
     */
    public double getWeight() {
        return weight;
    }
}

class Vertex {
    V v;
    int index;
    boolean Null = false;
    int degree = 0;

    /**
     * Constructor of class Vertex.
     *
     * @param v the pair of branch name and penalty.
     */
    public Vertex(V v) {
        this.v = v;
    }
}

class Edge {
    Vertex from;
    Vertex to;
    E w;

    /**
     * Constructor of class Edge.
     *
     * @param from first vertex of the edge.
     * @param to   second vertex of the edge.
     * @param w    the weight of edge.
     */
    public Edge(Vertex from, Vertex to, E w) {
        this.from = from;
        this.to = to;
        this.w = w;
    }
}

class Graph implements IGraph {
    List<Vertex> vertices = new ArrayList<>();
    List<Edge> edges = new ArrayList<>();
    List<List<Edge>> adjacencyMatrix = new ArrayList<>();

    public Graph() {
    }

    /**
     * inserting new vertex into the graph
     *
     * @param v pair of class V uses to create a new vertex
     * @return new vertex
     * @see V
     */
    @Override
    public Vertex insertVertex(V v) {
        Vertex vertex = new Vertex(v);
        this.vertices.add(vertex);

        adjacencyMatrix.add(new ArrayList<>());

        vertex.index = vertices.size()-1;

        return vertex;
    }

    /**
     * inserting new edge into the graph
     *
     * @param from first vertex of the edge
     * @param to   second vertex of the edge.
     * @param w    weight of the edge
     * @return new edge.
     */
    @Override
    public Edge insertEdge(Vertex from, Vertex to, E w) {
        Edge edge = new Edge(from, to, w);
        this.edges.add(edge);

        for (int i = 0; i < adjacencyMatrix.size(); ++i) {
            while (adjacencyMatrix.get(i).size() != adjacencyMatrix.size()) {
                adjacencyMatrix.get(i).add(null);
            }
        }

        adjacencyMatrix.get(from.index).set(to.index, edge);
        from.Null = true;
        from.degree++;

        adjacencyMatrix.get(to.index).set(from.index, edge);
        to.Null = true;
        to.degree++;

        return edge;
    }

    /**
     * adjacency or not
     *
     * @param v first vertex.
     * @param u second vertex.
     * @return adjacency or not
     */
    @Override
    public boolean areAdjacent(Vertex v, Vertex u) {
        return adjacencyMatrix.get(v.index).get(u.index) != null;
    }

    /**
     * removing edge from the graph
     *
     * @param edge edge which should be removed from the graph
     */
    @Override
    public void removeEdge(Edge edge) {
        adjacencyMatrix.get(edge.from.index).set(edge.to.index, null);
        adjacencyMatrix.get(edge.to.index).set(edge.from.index, null);
        this.edges.remove(edge);
    }

    /**
     * removing vertex from the graph
     *
     * @param v vertex which should be removed from the graph
     */
    @Override
    public void removeVertex(Vertex v) {
        adjacencyMatrix.remove(v.index);
        this.vertices.remove(v);
    }

    /**
     * Prime's algorithm of finding minimum spanning tree
     *
     * @return result
     */
    public StringBuilder PimAlgo() {
        List<Boolean> excess = new ArrayList<>(Collections.nCopies(vertices.size(), false));
        StringBuilder result = new StringBuilder();

        for (Vertex vertex : this.vertices) {
            if (!excess.get(vertex.index) && vertex.Null) {
                PriorityQueue<Double, Pair<Vertex, Edge>> queue = new PriorityQueue<>();

                for (Edge edge : adjacencyMatrix.get(vertex.index)) {
                    if (edge == null) {
                        continue;
                    }

                    Vertex vertex2 = (vertex == edge.from)? edge.to: edge.from;
                    node<Double, Pair<Vertex, Edge>> item = new node<>(edge.w.getWeight(), new Pair<>(vertex2, edge));
                    queue.insert(item);
                }

                excess.set(vertex.index, true);

                while (!queue.isEmpty()) {
                    int deg = 0;
                    node<Double, Pair<Vertex, Edge>> tempNode = queue.extractMin();
                    Vertex vertex1 = tempNode.data.getKey();
                    Edge tempEdge = tempNode.data.getValue();

                    if (!excess.get(vertex1.index)) {
                        result.append(tempEdge.from.v.getName()).append(":").append(tempEdge.to.v.getName()).append(" ");
                        for (Edge edge : adjacencyMatrix.get(vertex1.index)) {
                            if (deg == vertex1.degree) {
                                break;
                            }
                            if (edge == null) {
                                continue;
                            }
                            deg++;
                            Vertex vertex2 = (vertex1 == edge.from)? edge.to: edge.from;

                            if (!excess.get(vertex2.index)) {
                                node<Double, Pair<Vertex, Edge>> item = new node<>(edge.w.getWeight(), new Pair<>(vertex2, edge));
                                queue.insert(item);
                            }
                        }
                        excess.set(vertex1.index, true);
                    }
                }
            }
        }
        return result;
    }

    /**
     * counting the edges which are connected to the vertex
     *
     * @param vertex Vertex of finding degree.
     * @return count of connected vertices.
     */
    @Override
    public int degree(Vertex vertex) {
        int count = 0;
        for (Edge edge : adjacencyMatrix.get(vertex.index)) {
            if (edge != null) {
                count++;
            }
        }
        return count;
    }
}


/**
 * just given interface
 *
 * @param <K> key
 * @param <V> value
 */
interface IPriorityQueue<K extends Comparable<K>, V> {
    void insert(node<K, V> x);

    node<K, V> findMin();

    node<K, V> extractMin();

    void decreaseKey(node<K, V> x, K key);

    void delete(node<K, V> x);

    void union(PriorityQueue<K, V> anotherQueue);
}

/**
 * Priority Queue(Fibonacci Heap), implementation taken from book
 *
 * @param <K> key
 * @param <V> value
 */
class PriorityQueue<K extends Comparable<K>, V> implements IPriorityQueue<K, V> {
    private node<K, V> minRoot = null;
    private int n = 0;

    public PriorityQueue() {
    }

    /**
     * methid for insertion( inserted to the right side min node) nodes in a heap
     *
     * @param x node to insert
     */
    @Override
    public void insert(node<K, V> x) {
        if (minRoot == null) {
            minRoot = x;
        } else {
            x.right = minRoot;
            x.left = minRoot.left;
            minRoot.left = x;
            x.left.right = x;

            if (x.key.compareTo(minRoot.key) < 0) {
                minRoot = x;
            }
        }
        n++;
    }

    /**
     * method for finding min node
     *
     * @return min node
     */
    @Override
    public node<K, V> findMin() {
        return minRoot;
    }

    /**
     * method for extracting min node from fib. heap
     *
     * @return min element from fib. heap
     */
    @Override
    public node<K, V> extractMin() {
        node<K, V> x = minRoot;
        x.in = false;

        if (x != null) {
            int kids = x.degree;
            node<K, V> y = x.child;
            node<K, V> tmpleft;

            while (kids > 0) {
                tmpleft = y.left;

                y.right.left = y.left;
                y.left.right = y.right;

                y.right = minRoot;
                y.left = minRoot.left;
                minRoot.left = y;
                y.left.right = y;

                y.parent = null;
                y = tmpleft;
                kids--;
            }

            x.right.left = x.left;
            x.left.right = x.right;

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
     *
     * @param x   node in a heap
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

            if (x.key.compareTo(minRoot.key) < 0) {
                minRoot = x;
            }
        }
    }

    /**
     * method for deleting node from heap
     *
     * @param x node in a heap
     */
    @Override
    public void delete(node<K, V> x) {
        decreaseKey(x, Double.NEGATIVE_INFINITY);
        extractMin();
    }

    /**
     * method for merging two heaps
     *
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

                    if (anotherQueue.minRoot.key.compareTo(minRoot.key) < 0) {
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
        List<node<K, V>> arr = new ArrayList<>(Collections.nCopies(arraySize, null));

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
            node<K, V> next = x;
            x = x.left;
            int d = next.degree;

            while (arr.get(d) != null) {
                node<K, V> y = arr.get(d);

                if (next.key.compareTo(y.key) > 0) {
                    node<K, V> temp = y;
                    y = next;
                    next = temp;
                }

                fibHeapLink(y, next);

                arr.set(d, null);
                d++;
            }

            arr.set(d, next);

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

                if (y.key.compareTo((K) minRoot.key) < 0) {
                    minRoot = y;
                }
            } else {
                minRoot = y;
            }
        }
    }

    /**
     * remove the node x from the child of y node and putting x node to the left side of root
     *
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
     *
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
     *
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

class node<K extends Comparable<K>, V> {
    V data;
    node<K, V> child;
    node<K, V> left;
    node<K, V> parent;
    node<K, V> right;
    boolean mark;
    boolean in = true;
    boolean contains = true;
    K key;
    int degree;

    public node(K key, V data) {
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
