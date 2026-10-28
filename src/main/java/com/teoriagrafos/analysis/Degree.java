package com.teoriagrafos.analysis;

/** Grados de entrada, salida y conexiones no dirigidas de un vértice. */
public record Degree(int incoming, int outgoing, int undirected) {

    public int total() {
        return incoming + outgoing + undirected;
    }
}
