public class Main {
    public static void main(String[] args) {
        Graph<String, Integer> g = new Graph<>();
        Graph.Vertex moscow = g.insertVertex("Moscow");
        Graph.Vertex kazan = g.insertVertex("Kazan");
        Graph.Vertex innopolis = g.insertVertex("Innopolis");
        Graph.Vertex ekaterinburg = g.insertVertex("Ekaterinburg");

        g.insertEdge(moscow, innopolis, 800);
        g.insertEdge(kazan, innopolis, 38);
        Graph.Edge kazan_eka = g.insertEdge(kazan, ekaterinburg, 1000);
        g.insertEdge(moscow, ekaterinburg, 900);

        System.out.println(g);

        g.removeVertex(moscow);
        System.out.println("Degree Kazan = " + g.degree(kazan));
        System.out.println("After removing Moscow");
        System.out.println(g);

        g.removeEdge(kazan_eka);
        System.out.println("After removing kazan_eka");
    }
}
