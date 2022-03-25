import java.util.Iterator;

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

public class Graph<V, E> {
    DoublyLinkedList<Vertex> vertices;
    DoublyLinkedList<Edge> edges;

    public Graph() {
        vertices = new DoublyLinkedList<>();
        edges = new DoublyLinkedList<>();
    }

    public Vertex insertVertex(V value) {
        Vertex vertex = new Vertex(value);

        vertex.refVertex = this.vertices.add(vertex);

        return vertex;
    }

    public Edge insertEdge(Vertex from, Vertex to, E weight) {
        Edge edge = new Edge(weight, from, to);

        edge.refEdge = this.edges.add(edge);
        edge.refTo = to.incidentEdges.add(edge);
        edge.refFrom = from.incidentEdges.add(edge);

        return edge;
    }

    public void removeVertex(Vertex vertex) {
        this.vertices.remove(vertex.refVertex);

        for (Edge edge : vertex.incidentEdges) {
            this.edges.remove(edge.refEdge);
            edge.to.incidentEdges.remove(edge.refTo);
            edge.from.incidentEdges.remove(edge.refFrom);
        }
    }

    public void removeEdge(Edge edge) {
        this.edges.remove(edge.refEdge);
        edge.to.incidentEdges.remove(edge.refTo);
        edge.from.incidentEdges.remove(edge.refFrom);
    }

    public boolean areAdjacent(Vertex vertex1, Vertex vertex2) {
        if (vertex1.incidentEdges.size() > vertex2.incidentEdges.size()) {
            return areAdjacent(vertex2, vertex1);
        }

        for (Edge edge : vertex1.incidentEdges) {
            if ((edge.to == vertex1 && edge.from == vertex2) || (edge.from == vertex1 && edge.to == vertex2)) {
                return true;
            }
        }
        return false;
    }

    public int degree(Vertex v) {
        return v.incidentEdges.size();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Edge e : this.edges) {
            stringBuilder.append(e.from.value + " -- " + e.weight + " -- " + e.to.value + "\n");
        }
        return stringBuilder.toString();
    }

    class Vertex {
        DoublyLinkedList<Edge> incidentEdges;
        V value;
        DoublyLinkedList.Node refVertex;

        public Vertex(V value) {
            this.value = value;
            this.incidentEdges = new DoublyLinkedList<>();
        }
    }

    class Edge {
        Vertex from;
        Vertex to;
        E weight;

        DoublyLinkedList.Node refTo;
        DoublyLinkedList.Node refFrom;
        DoublyLinkedList.Node refEdge;

        public Edge(E weight, Vertex from, Vertex to) {
            this.weight = weight;
            this.from = from;
            this.to = to;
        }


    }
}
