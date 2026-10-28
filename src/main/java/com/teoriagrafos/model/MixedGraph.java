package com.teoriagrafos.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Grafo etiquetado, ponderado y capaz de combinar aristas y arcos. */
public final class MixedGraph {

    private final Map<String, Vertex> vertices = new LinkedHashMap<>();
    private final List<Edge> edges = new ArrayList<>();

    public void addVertex(String id, String label) {
        if (vertices.containsKey(id)) {
            throw new IllegalArgumentException("Vértice duplicado: " + id);
        }
        vertices.put(id, new Vertex(id, label));
    }

    public void addDirectedEdge(String id, String from, String to,
            String label, double weight) {
        addEdge(new Edge(id, from, to, label, weight, EdgeType.DIRECTED));
    }

    public void addUndirectedEdge(String id, String first, String second,
            String label, double weight) {
        addEdge(new Edge(id, first, second, label, weight, EdgeType.UNDIRECTED));
    }

    public List<Vertex> vertices() {
        return List.copyOf(vertices.values());
    }

    public List<Edge> edges() {
        return List.copyOf(edges);
    }

    public Vertex vertex(String id) {
        Vertex vertex = vertices.get(id);
        if (vertex == null) {
            throw new IllegalArgumentException("Vértice inexistente: " + id);
        }
        return vertex;
    }

    public Map<String, List<Adjacent>> adjacencyList() {
        Map<String, List<Adjacent>> result = new LinkedHashMap<>();
        vertices.keySet().forEach(id -> result.put(id, new ArrayList<>()));

        for (Edge edge : edges) {
            result.get(edge.from()).add(new Adjacent(
                    edge.to(), edge.label(), edge.weight(), edge.type()));
            if (edge.type() == EdgeType.UNDIRECTED) {
                result.get(edge.to()).add(new Adjacent(
                        edge.from(), edge.label(), edge.weight(), edge.type()));
            }
        }

        Map<String, List<Adjacent>> immutable = new LinkedHashMap<>();
        result.forEach((id, adjacent) -> immutable.put(id, List.copyOf(adjacent)));
        return Collections.unmodifiableMap(immutable);
    }

    public WeightedMatrix weightedAdjacencyMatrix() {
        List<String> ids = new ArrayList<>(vertices.keySet());
        Map<String, Integer> indexes = indexes(ids);
        double[][] matrix = new double[ids.size()][ids.size()];

        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix.length; column++) {
                matrix[row][column] = row == column
                        ? 0 : WeightedMatrix.NO_CONNECTION;
            }
        }

        for (Edge edge : edges) {
            int from = indexes.get(edge.from());
            int to = indexes.get(edge.to());
            matrix[from][to] = Math.min(matrix[from][to], edge.weight());
            if (edge.type() == EdgeType.UNDIRECTED) {
                matrix[to][from] = Math.min(matrix[to][from], edge.weight());
            }
        }
        return new WeightedMatrix(ids, matrix);
    }

    public IncidenceMatrix incidenceMatrix() {
        List<String> vertexIds = new ArrayList<>(vertices.keySet());
        List<String> edgeIds = edges.stream().map(Edge::id).toList();
        Map<String, Integer> indexes = indexes(vertexIds);
        int[][] matrix = new int[vertexIds.size()][edges.size()];

        for (int column = 0; column < edges.size(); column++) {
            Edge edge = edges.get(column);
            int from = indexes.get(edge.from());
            int to = indexes.get(edge.to());
            if (edge.type() == EdgeType.DIRECTED) {
                matrix[from][column] = -1;
                matrix[to][column] = 1;
            } else {
                matrix[from][column] = 1;
                matrix[to][column] = 1;
            }
        }
        return new IncidenceMatrix(vertexIds, edgeIds, matrix);
    }

    private void addEdge(Edge edge) {
        requireVertex(edge.from());
        requireVertex(edge.to());
        if (edges.stream().anyMatch(current -> current.id().equals(edge.id()))) {
            throw new IllegalArgumentException("Arista duplicada: " + edge.id());
        }
        edges.add(edge);
    }

    private void requireVertex(String id) {
        if (!vertices.containsKey(id)) {
            throw new IllegalArgumentException("Vértice inexistente: " + id);
        }
    }

    private Map<String, Integer> indexes(List<String> ids) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (int index = 0; index < ids.size(); index++) {
            result.put(ids.get(index), index);
        }
        return result;
    }
}
