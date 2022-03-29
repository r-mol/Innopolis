import java.util.ArrayList;
import java.util.List;

//undirected
class GraphAdjacentList<V, E> {

    class Vertex {
        V weight;
        List<Edge> adjacent;

        public Vertex(V weight) {
            this.weight = weight;
            this.adjacent = new ArrayList<Edge>();
        }
    }

    class Edge {
        Vertex from;
        Vertex to;
        E weight;

        public Edge(Vertex from, Vertex to, E weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    List<Vertex> vertices;
    List<Edge> edges;


    public GraphAdjacentList() {
        this.vertices = new ArrayList<Vertex>();
        this.edges = new ArrayList<Edge>();
    }

    Vertex addVertex(V weight) {
        Vertex vertex = new Vertex(weight);
        this.vertices.add(vertex);
        return vertex;
    }

    Edge addEdge(Vertex from, Vertex to, E weight) {
        Edge edge = new Edge(from, to, weight);
        this.edges.add(edge);
        from.adjacent.add(edge);
        to.adjacent.add(edge);
        return edge;
    }

    boolean adjacent(Vertex vertex1, Vertex vertex2) {
        for (Edge edge : vertex1.adjacent) {
            if (edge.from.equals(vertex2) || edge.to.equals(vertex2)) {
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

    void removeVertex(Vertex vertex) {
        for(Edge edge : vertex.adjacent) {
            if(!edge.from.equals(vertex)) edge.from.adjacent.remove(edge);
            if(!edge.to.equals(vertex)) edge.to.adjacent.remove(edge);
            this.edges.remove(edge);
        }
        this.vertices.remove(vertex);
    }
}

public class MainOfAdjacentList {

    public static void main(String[] args) {
        GraphAdjacentList<String, Integer> graph = new GraphAdjacentList<String, Integer>();
        GraphAdjacentList<String, Integer>.Vertex Innopolis, kazan, moscow, nowosyb;
        kazan = graph.addVertex("Kazan");
        Innopolis = graph.addVertex("Innopolis");
        moscow = graph.addVertex("Moscow");
        nowosyb = graph.addVertex("Novosyb");
        GraphAdjacentList<String, Integer>.Edge e1 = graph.addEdge(Innopolis, kazan, 40);
        GraphAdjacentList<String, Integer>.Edge e2 = graph.addEdge(moscow, kazan, 1000);
        GraphAdjacentList<String, Integer>.Edge e3 = graph.addEdge(kazan, nowosyb, 2000);

        graph.removeVertex(kazan);

        graph.removeEdge(e2);
        graph.removeEdge(e2);


        System.out.println(graph.adjacent(Innopolis,moscow));
        System.out.println(graph.adjacent(moscow,kazan));

        for (GraphAdjacentList<String, Integer>.Edge edge : graph.edges) {
            System.out.println(edge.to.weight + " " + edge.from.weight + " " + edge.weight);
        }
    }
}