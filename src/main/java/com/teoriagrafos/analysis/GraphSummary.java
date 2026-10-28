package com.teoriagrafos.analysis;

/** Resumen cuantitativo y estructural de un grafo. */
public record GraphSummary(GraphKind kind, int vertexCount, int edgeCount,
        double totalWeight, boolean weaklyConnected) {
}
