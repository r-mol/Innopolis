import java.util.*;
import java.util.stream.Collectors;

//undirected
class GraphAdjacentList<V, E> {

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
        E weight;

        public Edge(Vertex from, Vertex to, E weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public int getWeight(){
            return (int)weight;
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

    List<Vertex> endVertices(Edge edge){
        List<Vertex> vertices_pair = new ArrayList<>();
        vertices_pair.add(edge.from);
        vertices_pair.add(edge.to);
        return vertices_pair;
    }

    Vertex opposite(Vertex vertex1, Edge edge){
        if(edge.from == vertex1){
            return edge.to;
        }
        else{
            return edge.from;
        }
    }

    public int MST(){
        int minimumWeight = 0;
        List<Edge> edgesCopy = edges.stream().sorted(Comparator.comparingInt(Edge::getWeight)).collect(Collectors.toList());
        List<Vertex> vertexCopy = new ArrayList<>();

        while(vertices.size() != vertexCopy.size()){
            for(Edge edge: edgesCopy){
                if(!vertexCopy.contains(edge.from) && !vertexCopy.contains(edge.to)){
                    vertexCopy.add(edge.from);
                    vertexCopy.add(edge.to);
                    minimumWeight+=(int)edge.weight;
                    edgesCopy.remove(edge);
                    System.out.println(edge.from.value + " " + edge.to.value);
                    break;
                }
                else if(!vertexCopy.contains(edge.from)){
                    vertexCopy.add(edge.from);
                    minimumWeight+=(int)edge.weight;
                    edgesCopy.remove(edge);
                    System.out.println( edge.from.value);
                    break;
                }else if(!vertexCopy.contains(edge.to)){
                    vertexCopy.add(edge.to);
                    minimumWeight+=(int)edge.weight;
                    edgesCopy.remove(edge);
                    System.out.println(edge.to.value);

                    break;
                }else{
                    edgesCopy.remove(edge);
                    break;
                }
            }
        }
       // edgesCopy.forEach(e -> System.out.println(e.weight));
        return minimumWeight;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Edge e : this.edges) {
            stringBuilder.append(e.from.value).append(" -- ").append(e.weight).append(" -- ").append(e.to.value).append("\n");
        }
        return stringBuilder.toString();
    }

    public int degree(Vertex v) {
        return v.adjacent.size();
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
        GraphAdjacentList<String, Integer>.Edge e1 = graph.addEdge(Innopolis, kazan, 100);
        GraphAdjacentList<String, Integer>.Edge e2 = graph.addEdge(Innopolis, moscow, 1000);
        GraphAdjacentList<String, Integer>.Edge e3 = graph.addEdge(Innopolis, nowosyb, 600);
        GraphAdjacentList<String, Integer>.Edge e4 = graph.addEdge(kazan, nowosyb, 10000);
        GraphAdjacentList<String, Integer>.Edge e5 = graph.addEdge(kazan, moscow, 80);
        GraphAdjacentList<String, Integer>.Edge e6 = graph.addEdge(moscow, nowosyb, 700);


        System.out.println(graph.MST());

        graph.removeEdge(e2);
        graph.removeEdge(e2);


        System.out.println(graph.adjacent(Innopolis,moscow));
        System.out.println(graph.adjacent(moscow,kazan));

        for (GraphAdjacentList<String, Integer>.Edge edge : graph.edges) {
            System.out.println(edge.to.value + " " + edge.from.value + " " + edge.weight);
        }
    }
}