package com.teoriagrafos.analysis;

import com.teoriagrafos.model.Edge;
import com.teoriagrafos.model.EdgeType;
import com.teoriagrafos.model.MixedGraph;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/** Operaciones de clasificación, conectividad y cálculo de grados. */
public final class GraphAnalyzer {

    private GraphAnalyzer() {
    }

    public static GraphSummary summarize(MixedGraph graph) {
        double totalWeight = graph.edges().stream()
                .mapToDouble(Edge::weight)
                .sum();
        return new GraphSummary(kindOf(graph), graph.vertices().size(),
                graph.edges().size(), totalWeight, isWeaklyConnected(graph));
    }

    public static GraphKind kindOf(MixedGraph graph) {
        boolean directed = graph.edges().stream()
                .anyMatch(edge -> edge.type() == EdgeType.DIRECTED);
        boolean undirected = graph.edges().stream()
                .anyMatch(edge -> edge.type() == EdgeType.UNDIRECTED);

        if (!directed && !undirected) {
            return GraphKind.EMPTY;
        }
        if (directed && undirected) {
            return GraphKind.MIXED;
        }
        return directed ? GraphKind.DIRECTED : GraphKind.UNDIRECTED;
    }

    public static Degree degreeOf(MixedGraph graph, String vertexId) {
        graph.vertex(vertexId);
        int incoming = 0;
        int outgoing = 0;
        int undirected = 0;

        for (Edge edge : graph.edges()) {
            if (edge.type() == EdgeType.DIRECTED) {
                if (edge.from().equals(vertexId)) {
                    outgoing++;
                }
                if (edge.to().equals(vertexId)) {
                    incoming++;
                }
            } else if (edge.from().equals(vertexId)
                    || edge.to().equals(vertexId)) {
                undirected++;
            }
        }
        return new Degree(incoming, outgoing, undirected);
    }

    public static boolean isWeaklyConnected(MixedGraph graph) {
        if (graph.vertices().isEmpty()) {
            return true;
        }

        Map<String, Set<String>> neighbors = new HashMap<>();
        graph.vertices().forEach(vertex ->
                neighbors.put(vertex.id(), new HashSet<>()));
        for (Edge edge : graph.edges()) {
            neighbors.get(edge.from()).add(edge.to());
            neighbors.get(edge.to()).add(edge.from());
        }

        String start = graph.vertices().get(0).id();
        Set<String> visited = new HashSet<>();
        Queue<String> pending = new ArrayDeque<>(List.of(start));
        visited.add(start);

        while (!pending.isEmpty()) {
            String current = pending.remove();
            for (String neighbor : neighbors.get(current)) {
                if (visited.add(neighbor)) {
                    pending.add(neighbor);
                }
            }
        }
        return visited.size() == graph.vertices().size();
    }
}
