package com.teoriagrafos.model;

import java.util.List;

/** Matriz de incidencia: filas de vértices y columnas de aristas. */
public record IncidenceMatrix(List<String> vertexIds, List<String> edgeIds,
        int[][] values) {

    public IncidenceMatrix {
        vertexIds = List.copyOf(vertexIds);
        edgeIds = List.copyOf(edgeIds);
    }
}
