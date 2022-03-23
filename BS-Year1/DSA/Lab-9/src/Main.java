import java.util.ArrayList;
import java.util.List;

//undected
class Graph<V, E> {

    class Vertex {
        V value;
        List<Edge> adjacent;

        public Vertex(V value) {
            this.value = value;
            this.adjacent = new ArrayList<Edge>();
        }
    }

    class Edge {
        Vertex from;
        Vertex to;
        E label;

        public Edge(Vertex from, Vertex to, E label) {
            this.from = from;
            this.to = to;
            this.label = label;
        }
    }

    List<Vertex> vertices;
    List<Edge> edges;


    public Graph() {
        this.vertices = new ArrayList<Vertex>();
        this.edges = new ArrayList<Edge>();
    }

    Vertex addVertex(V value) {
        Vertex v = new Vertex(value);
        this.vertices.add(v);
        return v;
    }

    Edge addEdge(Vertex from, Vertex to, E label) {
        Edge edge = new Edge(from, to, label);
        this.edges.add(edge);
        from.adjacent.add(edge);
        to.adjacent.add(edge);
        return edge;
    }

    boolean adjacent(Vertex u, Vertex v) {
        for (Edge edge : u.adjacent) {
            if (edge.from.equals(v) || edge.to.equals(v)) {
                return true;
            }
        }
        return false;
    }

    void removeEdge(Edge edge){
        edge.from.adjacent.remove(edge);
        edge.to.adjacent.remove(edge);
        this.edges.remove(edge);
    }

    void removeVertex(Vertex v) {
        for(Edge edge : v.adjacent) {
            if(!edge.from.equals(v)) edge.from.adjacent.remove(edge);
            if(!edge.to.equals(v)) edge.to.adjacent.remove(edge);
            this.edges.remove(edge);
        }
        this.vertices.remove(v);
    }
}

public class Main {

    public static void main(String[] args) {
        Graph<String, Integer> graph = new Graph<String, Integer>();
        Graph<String, Integer>.Vertex Innopolis, kazan, moscow, nowosyb;
        kazan = graph.addVertex("Kazan");
        Innopolis = graph.addVertex("Innopolis");
        moscow = graph.addVertex("Moscow");
        nowosyb = graph.addVertex("Novosyb");
        Graph<String, Integer>.Edge e1 = graph.addEdge(Innopolis, kazan, 40);
        Graph<String, Integer>.Edge e2 = graph.addEdge(moscow, kazan, 1000);
        Graph<String, Integer>.Edge e3 = graph.addEdge(kazan, nowosyb, 2000);

        graph.removeVertex(kazan);

        graph.removeEdge(e2);
        graph.removeEdge(e2);


        System.out.println(graph.adjacent(Innopolis,moscow));
        System.out.println(graph.adjacent(moscow,kazan));

        for (Graph<String, Integer>.Edge edge : graph.edges) {
            System.out.println(edge.to.value + " " + edge.from.value + " " + edge.label);
        }
    }
}