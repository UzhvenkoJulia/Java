import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class B08_03<V> { 
    private final Map<V, Set<V>> adjacencyList;

    public B08_03() {
        this.adjacencyList = new HashMap<>();
    }

    public void addVertex(V vertex) {
        adjacencyList.putIfAbsent(vertex, new HashSet<>());
    }

    public void removeVertex(V vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            return;
        }

        for (V neighbor : adjacencyList.get(vertex)) {
            if (adjacencyList.containsKey(neighbor)) {
                adjacencyList.get(neighbor).remove(vertex);
            }
        }

        adjacencyList.remove(vertex);
    }

    /**
     * додає ребро між двома вершинами (неорієнтоване)
     * якщо вершини не існують, вони будуть додані
     */
     
    public void addEdge(V v1, V v2) {
        addVertex(v1);
        addVertex(v2);

        adjacencyList.get(v1).add(v2);
        adjacencyList.get(v2).add(v1);
    }

    public void removeEdge(V v1, V v2) {
        if (adjacencyList.containsKey(v1) && adjacencyList.containsKey(v2)) {
            adjacencyList.get(v1).remove(v2);
            adjacencyList.get(v2).remove(v1);
        }
    }
    
    @Override
    
    public String toString() {
        StringBuilder sb = new StringBuilder("earl:\n");
        for (Map.Entry<V, Set<V>> entry : adjacencyList.entrySet()) {
            sb.append("top ").append(entry.getKey()).append(" -> neighborhood: ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        B08_03<String> graph = new B08_03<>();

        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");

        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "A");
        graph.addEdge("A", "D"); // автоматично

        System.out.println("--- initial graph ---");
        System.out.println(graph);

        graph.removeEdge("A", "D");
        System.out.println("--- after - A-D ---");
        System.out.println(graph);
        
        graph.removeVertex("B");
        System.out.println("--- after - B ---");
        System.out.println(graph); 
        // B зникла, а A і C більше не містять посилання на B
    }
}