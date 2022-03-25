import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
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

class Graph2<V,E>{

    class Vertex{
        V value;
        int index;
        DoublyLinkedList.Node refVertex;

        public Vertex(V value) {
            this.value = value;
        }
    }

    class Edge{
        Vertex from;
        Vertex to;
        E label;

        DoublyLinkedList.Node refEdge;
        DoublyLinkedList.Node refFrom;
        DoublyLinkedList.Node refTo;

        public Edge(Vertex from, Vertex to, E weight) {
            this.from = from;
            this.to = to;
            this.label = weight;
        }
    }

    DoublyLinkedList<Vertex> vertices;
    DoublyLinkedList<Edge> edges;
    Vector<Vector<Edge>> adjacentMatrix = new Vector<Vector<Edge>>();

    public Graph2() {
        this.vertices = new DoublyLinkedList<>();
        this.edges = new DoublyLinkedList<>();
    }

    Vertex addVertex(V value) {
        Vertex vertex = new Vertex(value);
        vertex.refVertex = this.vertices.add(vertex);

        if(adjacentMatrix.isEmpty()){
            adjacentMatrix.add(new Vector<Edge>());
            adjacentMatrix.get(0).setSize(1);
        }
        else if(nullObject()){
            for(int i  = 0; i<adjacentMatrix.size();++i){
                if(adjacentMatrix.get(i)==null){
                    vertex.index = i;
                }
            }
        }
        else{
            adjacentMatrix.add(new Vector<Edge>());
            for(int i =0; i<adjacentMatrix.size();++i){
                adjacentMatrix.get(i).setSize(adjacentMatrix.size());
            }
            vertex.index = adjacentMatrix.size()-1;
        }


        return vertex;
    }

    Edge addEdge(Vertex from, Vertex to, E weight) {
        Edge edge = new Edge(from, to, weight);
        edge.refEdge = this.edges.add(edge);

        adjacentMatrix.get(from.index).add(to.index,edge);
        adjacentMatrix.get(from.index).remove(adjacentMatrix.get(from.index).size()-1);
        adjacentMatrix.get(to.index).add(from.index,edge);
        adjacentMatrix.get(to.index).remove(adjacentMatrix.get(to.index).size()-1);

        return edge;
    }

    Vector<Vertex> endVertices(Edge edge){
        Vector<Vertex> verticex_pair = new Vector<>();
        verticex_pair.add(edge.from);
        verticex_pair.add(edge.to);
        return verticex_pair;
    }

    Vector<Edge> incidentEdges(Vertex vertex){
        Vector<Edge> incident_list = new Vector<>();

        for(int i = 0; i < adjacentMatrix.get(vertex.index).size();i++){
            if(adjacentMatrix.get(vertex.index).get(i) != null){
                incident_list.add(adjacentMatrix.get(vertex.index).get(i));
            }
        }
        return incident_list;
    }

    Vertex opposite(Vertex vertex1, Edge edge){
        if(edge.from == vertex1){
            return edge.to;
        }
        else{
            return edge.from;
        }
    }

    boolean areAdjacent(Vertex vertex1, Vertex vertex2){
        return adjacentMatrix.get(vertex1.index).get(vertex2.index) != null;
    }

    void removeEdge(Edge edge){
        adjacentMatrix.get(edge.from.index).add(edge.to.index,null);
        adjacentMatrix.get(edge.from.index).remove(adjacentMatrix.get(edge.to.index).size()-1);//check
        adjacentMatrix.get(edge.to.index).add(edge.from.index,null);
        adjacentMatrix.get(edge.to.index).remove(adjacentMatrix.get(edge.from.index).size()-1);//check
        this.edges.remove(edge.refEdge);
    }

    void removeVertex(Vertex vertex){
        Vector<Edge> incidents = this.incidentEdges(vertex);
        for(int i = 0; i < incidents.size();++i){
            this.removeEdge(incidents.get(i));
        }
        for(int i = 0; i < adjacentMatrix.size();++i){
            Vector<Edge> vec = adjacentMatrix.get(i);
            //vec.remove(vec)
        }
    }

    boolean nullObject(){
        for (Vector<Edge> matrix : adjacentMatrix) {
            if (matrix == null) {
                return true;
            }
        }
        return false;
    }
}

public class Main2 {
    public static void main(String[] args) {
        Graph2<String, Integer> graph = new Graph2<String, Integer>();
        Graph2<String, Integer>.Vertex Innopolis, kazan, moscow, nowosyb;
        kazan = graph.addVertex("Kazan");
        Innopolis = graph.addVertex("Innopolis");
        moscow = graph.addVertex("Moscow");
        nowosyb = graph.addVertex("Novosyb");
        Graph2<String, Integer>.Edge e1 = graph.addEdge(kazan, Innopolis, 40);
        Graph2<String, Integer>.Edge e2 = graph.addEdge(moscow, kazan, 1000);
        Graph2<String, Integer>.Edge e3 = graph.addEdge(kazan, nowosyb, 2000);

        System.out.println("huy");
    }
}