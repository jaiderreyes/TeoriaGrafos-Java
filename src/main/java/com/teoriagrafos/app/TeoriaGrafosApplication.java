package com.teoriagrafos.app;

import com.teoriagrafos.analysis.Degree;
import com.teoriagrafos.analysis.GraphAnalyzer;
import com.teoriagrafos.analysis.GraphSummary;
import com.teoriagrafos.model.Adjacent;
import com.teoriagrafos.model.Edge;
import com.teoriagrafos.model.EdgeType;
import com.teoriagrafos.model.IncidenceMatrix;
import com.teoriagrafos.model.MixedGraph;
import com.teoriagrafos.model.Vertex;
import com.teoriagrafos.model.WeightedMatrix;
import java.util.List;
import java.util.Map;

/** Ejemplo real de una red urbana representada como grafo mixto ponderado. */
public final class TeoriaGrafosApplication {

    private TeoriaGrafosApplication() {
    }

    public static void main(String[] args) {
        MixedGraph city = createUrbanMobilityGraph();
        printGraph(city);
    }

    public static MixedGraph createUrbanMobilityGraph() {
        MixedGraph graph = new MixedGraph();
        graph.addVertex("CASA", "Conjunto residencial");
        graph.addVertex("UNIV", "Universidad");
        graph.addVertex("HOSP", "Hospital");
        graph.addVertex("PARQ", "Parque central");
        graph.addVertex("CENTRO", "Centro comercial");

        graph.addDirectedEdge("E1", "CASA", "UNIV",
                "Avenida Central, sentido único", 8.5);
        graph.addDirectedEdge("E2", "UNIV", "HOSP",
                "Carrera 10, sentido único", 4.0);
        graph.addUndirectedEdge("E3", "CASA", "PARQ",
                "Sendero peatonal", 2.5);
        graph.addUndirectedEdge("E4", "PARQ", "CENTRO",
                "Ciclovía bidireccional", 3.0);
        graph.addDirectedEdge("E5", "CENTRO", "HOSP",
                "Autopista Norte, sentido único", 5.5);
        graph.addUndirectedEdge("E6", "HOSP", "PARQ",
                "Ruta urbana de doble sentido", 6.0);
        return graph;
    }

    private static void printGraph(MixedGraph graph) {
        GraphSummary summary = GraphAnalyzer.summarize(graph);
        System.out.println("=== RED DE MOVILIDAD URBANA ===");
        System.out.println("Tipo: " + summary.kind());
        System.out.println("Vértices: " + summary.vertexCount());
        System.out.println("Conexiones: " + summary.edgeCount());
        System.out.printf("Peso total: %.1f km%n", summary.totalWeight());
        System.out.println("Débilmente conexo: " + summary.weaklyConnected());

        System.out.println("\nVÉRTICES ETIQUETADOS");
        for (Vertex vertex : graph.vertices()) {
            Degree degree = GraphAnalyzer.degreeOf(graph, vertex.id());
            System.out.printf("%s = %s | entrada=%d, salida=%d, no dirigidas=%d%n",
                    vertex.id(), vertex.label(), degree.incoming(),
                    degree.outgoing(), degree.undirected());
        }

        System.out.println("\nARISTAS Y ARCOS PONDERADOS");
        for (Edge edge : graph.edges()) {
            String symbol = edge.type() == EdgeType.DIRECTED ? "->" : "--";
            System.out.printf("%s: %s %s %s | %s | %.1f km%n", edge.id(),
                    edge.from(), symbol, edge.to(), edge.label(), edge.weight());
        }

        printAdjacencyList(graph.adjacencyList());
        printWeightedMatrix(graph.weightedAdjacencyMatrix());
        printIncidenceMatrix(graph.incidenceMatrix());
    }

    private static void printAdjacencyList(
            Map<String, List<Adjacent>> adjacencyList) {
        System.out.println("\nLISTA DE ADYACENCIA");
        adjacencyList.forEach((vertex, adjacent) -> {
            String text = adjacent.stream()
                    .map(item -> String.format("%s(%.1f)",
                            item.destination(), item.weight()))
                    .reduce((first, second) -> first + ", " + second)
                    .orElse("sin vecinos de salida");
            System.out.println(vertex + " -> " + text);
        });
    }

    private static void printWeightedMatrix(WeightedMatrix data) {
        System.out.println("\nMATRIZ DE ADYACENCIA PONDERADA");
        printHeader(data.vertexIds());
        for (int row = 0; row < data.values().length; row++) {
            System.out.printf("%-8s", data.vertexIds().get(row));
            for (double value : data.values()[row]) {
                System.out.printf("%10s", Double.isInfinite(value)
                        ? "∞" : String.format("%.1f", value));
            }
            System.out.println();
        }
    }

    private static void printIncidenceMatrix(IncidenceMatrix data) {
        System.out.println("\nMATRIZ DE INCIDENCIA");
        printHeader(data.edgeIds());
        for (int row = 0; row < data.values().length; row++) {
            System.out.printf("%-8s", data.vertexIds().get(row));
            for (int value : data.values()[row]) {
                System.out.printf("%10d", value);
            }
            System.out.println();
        }
        System.out.println("Dirigida: -1 sale y +1 entra; no dirigida: +1 en ambos extremos.");
    }

    private static void printHeader(List<String> labels) {
        System.out.printf("%-8s", "");
        labels.forEach(label -> System.out.printf("%10s", label));
        System.out.println();
    }
}
