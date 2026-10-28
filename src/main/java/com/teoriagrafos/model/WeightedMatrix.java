package com.teoriagrafos.model;

import java.util.List;

/** Matriz de adyacencia ponderada y el orden de sus vértices. */
public record WeightedMatrix(List<String> vertexIds, double[][] values) {

    public static final double NO_CONNECTION = Double.POSITIVE_INFINITY;

    public WeightedMatrix {
        vertexIds = List.copyOf(vertexIds);
    }
}
