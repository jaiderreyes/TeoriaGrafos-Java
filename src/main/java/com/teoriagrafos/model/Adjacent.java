package com.teoriagrafos.model;

/** Elemento utilizado dentro de una lista de adyacencia. */
public record Adjacent(String destination, String edgeLabel,
        double weight, EdgeType type) {
}
