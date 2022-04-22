import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class EXGraph {
    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        String line;
        int countOperations = 0;

        if((line = bi.readLine()) != null) {
            countOperations = Integer.parseInt(line);
        }

        Graph graph = new Graph();
        String tempString = null;
        String[] splitString;
        String command;
        String branchName1;
        String branchName2;
        int penalty;
        int distance;

        while (countOperations != 0) {
            if((line = bi.readLine()) != null) {
                tempString = line;
            }

            assert tempString != null;
            splitString = tempString.split(" ");
            command = splitString[0];

            switch (command) {
                case "ADD":
                    branchName1 = splitString[1];
                    penalty = Integer.parseInt(splitString[2]);
                    graph.insertVertex(new V(branchName1, penalty));
                    break;
                case "CONNECT":
                    branchName1 = splitString[1];
                    branchName2 = splitString[2];
                    distance = Integer.parseInt(splitString[3]);
                    graph.insertEdge(graph.findVertexByName(branchName1), graph.findVertexByName(branchName2), new E(distance / (graph.findVertexByName(branchName1).v.getPenalty() + graph.findVertexByName(branchName2).v.getPenalty())));
                    break;
                case "PRINT_MIN":
                    graph.primMST();
                    break;
                default:
                    break;
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
    private String name;
    private double penalty;

    public V(String name, int penalty) {
        this.name = name;
        this.penalty = penalty;
    }

    public String getName() {
        return name;
    }

    public double getPenalty() {
        return penalty;
    }
}

class E {
    private double weight;

    public E(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

class Vertex {
    V v;
    int index;
    DoublyLinkedList.Node refVertex;

    public Vertex(V v) {
        this.v = v;
    }
}

class Edge {
    Vertex from;
    Vertex to;
    E w;

    DoublyLinkedList.Node refEdge;

    public Edge(Vertex from, Vertex to, E w) {
        this.from = from;
        this.to = to;
        this.w = w;
    }
}

class Graph implements IGraph {
    DoublyLinkedList<Vertex> vertices;
    DoublyLinkedList<Edge> edges;
    Vector<Vector<Edge>> adjacentMatrix = new Vector<>();

    public Graph() {
        this.vertices = new DoublyLinkedList<>();
        this.edges = new DoublyLinkedList<>();
    }

    @Override
    public Vertex insertVertex(V v) {
        Vertex vertex = new Vertex(v);
        vertex.refVertex = this.vertices.add(vertex);

        if (adjacentMatrix.isEmpty()) {
            adjacentMatrix.add(new Vector<>());
            adjacentMatrix.get(0).setSize(1);
        } else if (nullObject()) {
            for (int i = 0; i < adjacentMatrix.size(); ++i) {
                if (adjacentMatrix.get(i) == null) {
                    vertex.index = i;
                }
            }
        } else {
            adjacentMatrix.add(new Vector<>());
            for (int i = 0; i < adjacentMatrix.size(); ++i) {
                adjacentMatrix.get(i).setSize(adjacentMatrix.size());
            }
            vertex.index = adjacentMatrix.size() - 1;
        }

        return vertex;
    }

    @Override
    public Edge insertEdge(Vertex from, Vertex to, E w) {
        Edge edge = new Edge(from, to, w);
        edge.refEdge = this.edges.add(edge);

        adjacentMatrix.get(from.index).add(to.index, edge);
        adjacentMatrix.get(from.index).remove(adjacentMatrix.get(from.index).size() - 1);
        adjacentMatrix.get(to.index).add(from.index, edge);
        adjacentMatrix.get(to.index).remove(adjacentMatrix.get(to.index).size() - 1);

        return edge;
    }

    public Vertex opposite(Vertex vertex, Edge edge) {
        if (edge.from == vertex) {
            return edge.to;
        } else {
            return edge.from;
        }
    }

    public Vertex findVertexByName(String branchName) {
        for (Vertex vertex : vertices) {
            if (vertex.v.getName().equals(branchName)) {
                return vertex;
            }
        }
        return null;
    }

    @Override
    public boolean areAdjacent(Vertex v, Vertex u) {
        return adjacentMatrix.get(v.index).get(u.index) != null;
    }

    @Override
    public void removeEdge(Edge edge) {
        adjacentMatrix.get(edge.from.index).add(edge.to.index, null);
        adjacentMatrix.get(edge.from.index).remove(edge.to.index + 1);//check
        adjacentMatrix.get(edge.to.index).add(edge.from.index, null);
        adjacentMatrix.get(edge.to.index).remove(edge.from.index + 1);//check
        this.edges.remove(edge.refEdge);
    }

    @Override
    public void removeVertex(Vertex v) {
        for (int i = 0; i < adjacentMatrix.get(v.index).size(); i++) {
            if (adjacentMatrix.get(v.index).get(i) != null) {
                this.removeEdge(adjacentMatrix.get(v.index).get(i));
            }
        }
        this.vertices.remove(v.refVertex);
    }

    boolean nullObject() {
        for (Vector<Edge> matrix : adjacentMatrix) {
            if (matrix == null) {
                return true;
            }
        }
        return false;
    }

    public void primMST() {
        List<Boolean> excess = new ArrayList<>(Collections.nCopies(vertices.size(),false));

        for (Vertex vertex : this.vertices) {
            if (!excess.get(vertex.index)) {
                PriorityQueue<Double, Pair<Vertex, Edge>> queue = new PriorityQueue<>();

                List<Edge> listOfMst = new ArrayList<>();

                for (Edge edge : adjacentMatrix.get(vertex.index)) {
                    if (edge == null) {
                        continue;
                    }
                    Vertex vertex2 = opposite(vertex, edge);
                    FibonacciHeapNode<Double, Pair<Vertex, Edge>> item = new FibonacciHeapNode<>(edge.w.getWeight(), new Pair<>(vertex2, edge));
                    queue.insert(item);
                }

                excess.set(vertex.index, true);

                while (!queue.isEmpty()) {
                    FibonacciHeapNode<Double, Pair<Vertex, Edge>> min_str = queue.extractMin();
                    Vertex vertex1 = min_str.data.getKey();
                    Edge tempEdge = min_str.data.getValue();

                    if (!excess.get(vertex1.index)) {
                        listOfMst.add(tempEdge);
                        for (Edge edge : adjacentMatrix.get(vertex1.index)) {
                            if (edge == null) {
                                continue;
                            }
                            Vertex vertex2 = opposite(vertex1, edge);

                            if (!excess.get(vertex2.index)) {
                                FibonacciHeapNode<Double, Pair<Vertex, Edge>> item = new FibonacciHeapNode<>(edge.w.getWeight(), new Pair<>(vertex2, edge));
                                queue.insert(item);
                            }
                        }
                        excess.set(vertex1.index, true);
                    }
                }

                for (Edge edge : listOfMst) {
                    System.out.print(edge.from.v.getName() + ":" + edge.to.v.getName() + " ");
                }
            }
        }
        System.out.println();
    }

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

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Edge edge : this.edges) {
            stringBuilder.append(edge.from.v).append(" -- ").append(edge.w).append(" -- ").append(edge.to.v).append("\n");
        }
        return stringBuilder.toString();
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

class PriorityQueue<K extends Comparable<K>, V> implements IPriorityQueue {
    private FibonacciHeapNode<K, V> min = null;
    private int n = 0;

    public PriorityQueue() {
    }

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

            if (x.key.compareTo(min.key) < 0) {
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

            if (x.key.compareTo(min.key) < 0) {
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

    // Consolidate the heap
    public void consolidate() {
        int arraySize = ((int) Math.floor(Math.log(n) / Math.log((1.0 + Math.sqrt(5.0)) / 2.0))) + 2;
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
    boolean mark;
    K key;
    int degree;

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

class DoublyLinkedList<E> implements Iterable<E> {

    public class DoublyLinkedListIterator implements Iterator<E> {
        Node current;

        public DoublyLinkedListIterator(Node current) {
            this.current = current;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public E next() {
            E value = current.value;
            current = current.next;
            return value;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new DoublyLinkedListIterator(this.head);
    }

    public class Node {
        E value;
        Node prev = null;
        Node next = null;

        public Node(E value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public Node add(E value) {
        Node node = new Node(value);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size += 1;

        return node;
    }

    public int size() {
        return this.size;
    }

    public void remove(Node n) {
        if (n.prev == null) {
            head = n.next;
            if (n.next == null) {
                tail = null;
            } else {
                n.next.prev = null;
            }
        } else if (n.next == null) {
            tail = n.prev;
            n.prev.next = null;
        } else {
            n.next.prev = n.prev;
            n.prev.next = n.next;
        }
        size -= 1;
    }
}