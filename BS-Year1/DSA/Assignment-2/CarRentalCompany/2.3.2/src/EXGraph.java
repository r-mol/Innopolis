/**
 * @author Roman Molochkov
 * @Group #4
 * @Telegram: @roman_molochkov
 * @Email: r.molochkov@innopolis.university
 */

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class EXGraph {
    /**
     * The main function, which reads countOperations & after reads lines with commands:
     * <br>
     * <br>1. ADD - gets Branch name and Penalty of vertex, after add the vertex to the graph.
     * <br>2. CONNECT - gets Branch name #1, Branch name #2 and Distance between two vertices, after add the edge to the graph.
     * <br>3. PRINT_MIN - print Minimum Spanning Tree by using Prime's algorithm.
     *
     * @param args not used.
     * @throws IOException used if fails BufferReader.
     */
    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        Map<String, Vertex> map = new HashMap<>();
        Graph graph = new Graph();
        String tempString = null;
        String[] splitString;
        String branchName1;
        String branchName2;
        String command;
        String line;
        int countOperations = 0;
        int distance;
        int penalty;

        if ((line = bi.readLine()) != null) {
            countOperations = Integer.parseInt(line);
        }

        while (countOperations != 0) {
            if ((line = bi.readLine()) != null) {
                tempString = line;
            }

            assert tempString != null;
            splitString = tempString.split(" ");
            command = splitString[0];

            if (command.equals("ADD")) {
                branchName1 = splitString[1];
                penalty = Integer.parseInt(splitString[2]);
                map.put(branchName1, graph.insertVertex(new V(branchName1, penalty)));
            } else if (command.equals("CONNECT")) {
                branchName1 = splitString[1];
                branchName2 = splitString[2];
                distance = Integer.parseInt(splitString[3]);
                Vertex vertex1 = map.get(branchName1);
                Vertex vertex2 = map.get(branchName2);
                graph.insertEdge(vertex1, vertex2, new E(distance / (vertex1.v.getPenalty() + vertex2.v.getPenalty())));
            } else if (command.equals("PRINT_MIN")) {
                System.out.println(graph.primMST() + "\n");
            }

            countOperations--;
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
    List<Vertex> vertices;
    List<Edge> edges;
    List<List<Edge>> adjacentMatrix = new ArrayList<>();

    /**
     * Default Constructor of the Graph, which assignments List of vertices and edges
     */
    public Graph() {
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    /**
     * Method inserts new Vertex into this graph. There are two cases:
     * <br>1. Graph is empty.
     * <br>2. Graph contains some vertices, that is why add null object to all vectors of adjacentMatrix.
     *
     * @param v Pair of class V uses to create a new Vertex
     * @return new Vertex
     * @see V
     */
    @Override
    public Vertex insertVertex(V v) {
        Vertex vertex = new Vertex(v);
        this.vertices.add(vertex);

        if (adjacentMatrix.isEmpty()) {
            adjacentMatrix.add(new ArrayList<>());
            adjacentMatrix.get(0).add(null);
        } else {
            adjacentMatrix.add(new ArrayList<>());
            for (int i = 0; i < adjacentMatrix.size(); ++i) {
                while (adjacentMatrix.get(i).size() != adjacentMatrix.size()) {
                    adjacentMatrix.get(i).add(null);
                }
            }
            vertex.index = adjacentMatrix.size() - 1;
        }

        return vertex;
    }

    /**
     * Method inserts new Edge in this graph by using adjacent matrix.
     *
     * @param from The first Vertex of the edge.
     * @param to   The Second Vertex of the edge.
     * @param w    Weight of edge.
     * @return new Edge.
     */
    @Override
    public Edge insertEdge(Vertex from, Vertex to, E w) {
        Edge edge = new Edge(from, to, w);
        this.edges.add(edge);

        adjacentMatrix.get(from.index).set(to.index, edge);
        from.Null = true;
        from.degree++;

        adjacentMatrix.get(to.index).set(from.index, edge);
        to.Null = true;
        to.degree++;

        return edge;
    }

    /**
     * Method finds opposite vertex of edge.
     *
     * @param vertex First vertex of edge.
     * @param edge   Edge in which looking opposite vertex.
     * @return Opposite vertex.
     */
    public Vertex opposite(Vertex vertex, Edge edge) {
        if (edge.from == vertex) {
            return edge.to;
        } else {
            return edge.from;
        }
    }

    /**
     * Method checks if two vertices are adjacent by using adjacent matrix.
     *
     * @param v First vertex.
     * @param u Second vertex.
     * @return Boolean, if vertices are adjacent or not.
     */
    @Override
    public boolean areAdjacent(Vertex v, Vertex u) {
        return adjacentMatrix.get(v.index).get(u.index) != null;
    }

    /**
     * Method removes edge from the graph.
     *
     * @param edge Edge which should be removed from the adjacent matrix.
     */
    @Override
    public void removeEdge(Edge edge) {
        adjacentMatrix.get(edge.from.index).set(edge.to.index, null);
        adjacentMatrix.get(edge.to.index).set(edge.from.index, null);
        this.edges.remove(edge);
    }

    /**
     * Method removes vertex from the graph.
     *
     * @param v Vertex which should be removed from the adjacent matrix.
     */
    @Override
    public void removeVertex(Vertex v) {
        adjacentMatrix.remove(v.index);
        this.vertices.remove(v);
    }

    /**
     * Method finds Minimum Spanning Tree by using Prime's algorithm, which has been shown in lecture. Program fills
     * ArrayList of vertices with the vertex and null edge. After change the value in ArrayList by index of vertex with the minimum
     * found edge.
     * @return result
     */
    public StringBuilder primMST() {
        StringBuilder result = new StringBuilder();
        List<Double> MST = new ArrayList<>(Collections.nCopies(vertices.size(), Double.MAX_VALUE));
        List<FibonacciHeapNode<Double, Pair<Vertex, Edge>>> listNode = new ArrayList<>(Collections.nCopies(vertices.size(), null));
        PriorityQueue<Double, Pair<Vertex, Edge>> queue = new PriorityQueue<>();

        MST.set(vertices.get(0).index, 0.0);

        for (Vertex v : this.vertices) {
            FibonacciHeapNode<Double, Pair<Vertex, Edge>> newNode = new FibonacciHeapNode<>(MST.get(v.index), new Pair<>(v, null));
            queue.insert(newNode);
            listNode.set(v.index, newNode);
        }

        while (queue.size() != 0) {
            int deg = 0;
            FibonacciHeapNode<Double, Pair<Vertex, Edge>> node = queue.extractMin();

            if (node == null) {
                break;
            }

            Vertex vertex = node.data.getKey();
            Edge edge = node.data.getValue();

            if (edge != null) {
                result.append(edge.from.v.getName()).append(":").append(edge.to.v.getName()).append(" ");
            }

            if (vertex.Null) {
                for (Edge z : adjacentMatrix.get(vertex.index)) {
                    if (deg == vertex.degree) {
                        break;
                    }
                    if (z != null) {
                        deg++;
                        Vertex second_vertex = opposite(vertex, z);
                        FibonacciHeapNode<Double, Pair<Vertex, Edge>> tempNode = listNode.get(second_vertex.index);

                        if (tempNode != null) {
                            if (tempNode.in && z.w.getWeight() < MST.get(second_vertex.index)) {
                                MST.set(second_vertex.index, z.w.getWeight());
                                queue.decreaseKey(tempNode, MST.get(second_vertex.index));
                                tempNode.data = new Pair<>(second_vertex, z);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Method counts the edges which are connected to this vertex.
     *
     * @param vertex Vertex of finding degree.
     * @return count of connected vertices.
     */
    @Override
    public int degree(Vertex vertex) {
        int count = 0;
        for (Edge edge : adjacentMatrix.get(vertex.index)) {
            if (edge != null) {
                count++;
            }
        }
        return count;
    }
}

interface IPriorityQueue<K extends Comparable<K>, V> {
    void insert(FibonacciHeapNode<K, V> x);

    FibonacciHeapNode<K, V> findMin();

    FibonacciHeapNode<K, V> extractMin();

    void decreaseKey(FibonacciHeapNode<K, V> x, K key);

    void delete(FibonacciHeapNode<K, V> x);

    void union(PriorityQueue<K, V> anotherQueue);
}

class PriorityQueue<K extends Comparable<K>, V> implements IPriorityQueue<K, V> {
    private FibonacciHeapNode<K, V> min = null;
    private int n = 0;

    /**
     * Default constructor.
     */
    public PriorityQueue() {
    }

    /**
     * Method inserts a new Node to the right side of min Node.
     *
     * @param x new Fibonacci Heap Node.
     */
    @Override
    public void insert(FibonacciHeapNode<K, V> x) {
        if (min == null) {
            min = x;
        } else {
            x.left = min;
            x.right = min.right;
            min.right = x;
            x.right.left = x;

            if (x.key.compareTo(min.key) < 0) {
                min = x;
            }
        }
        n++;
    }

    /**
     * Method finds min of the Priority Queue by returning min Node.
     *
     * @return min Node of the Priority Queue.
     */
    @Override
    public FibonacciHeapNode<K, V> findMin() {
        return min;
    }

    /**
     * Extract min Node from the Priority Queue and finds new one min Node.
     *
     * @return min node of Priority Queue.
     */
    @Override
    public FibonacciHeapNode<K, V> extractMin() {
        FibonacciHeapNode<K, V> x = min;
        x.in = false;

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
     *
     * @param x   Fibonacci Node.
     * @param key Some number.
     */
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
            }
        }
    }

    /**
     * Delete the node from the Priority Queue.
     *
     * @param x Fibonacci Node
     */
    @Override
    public void delete(FibonacciHeapNode<K, V> x) {
        decreaseKey(x, Double.NEGATIVE_INFINITY);
        extractMin();
    }

    /**
     * Union of two Priority Queues.
     *
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
        List<FibonacciHeapNode<K, V>> arr = new ArrayList<>(Collections.nCopies(arraySize, null));

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
            FibonacciHeapNode<K, V> next = x;
            x = x.right;
            int d = next.degree;

            while (arr.get(d) != null) {
                FibonacciHeapNode<K, V> y = arr.get(d);

                if (next.key.compareTo(y.key) > 0) {
                    FibonacciHeapNode<K, V> temp = y;
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

        min = null;

        for (int i = 0; i < arraySize; i++) {
            FibonacciHeapNode<K, V> y = arr.get(i);
            if (y != null) {
                if (min != null) {
                    y.left.right = y.right;
                    y.right.left = y.left;

                    y.left = min;
                    y.right = min.right;
                    min.right = y;
                    y.right.left = y;

                    if (y.key.compareTo(min.key) < 0) {
                        min = y;
                    }
                } else {
                    min = y;
                }
            }
        }
    }

    /**
     * Function removing the Node x from the child of y Node and putting x Node to the right side of root.
     *
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
     *
     * @param x Fibonacci Node
     */
    public void cascadingCut(FibonacciHeapNode<K, V> x) {

        FibonacciHeapNode<K, V> z = x.parent;

        if (z != null) {
            if (!x.mark) {
                x.mark = true;
            } else {
                cut(x, z);

                cascadingCut(z);
            }
        }
    }

    /**
     * Function making the Node x a parent of Node y,
     *
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
class FibonacciHeapNode<K extends Comparable<K>, V> {
    V data;
    FibonacciHeapNode<K, V> child;
    FibonacciHeapNode<K, V> left;
    FibonacciHeapNode<K, V> parent;
    FibonacciHeapNode<K, V> right;
    boolean in = true;
    boolean mark;
    K key;
    int degree;

    /**
     * Constructor of the class FibonacciHeapNode.
     *
     * @param key  is some number.
     * @param data some data of the task.
     */
    public FibonacciHeapNode(K key, V data) {
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
