package com.teoriagrafos.model;

/** Vértice identificado por un código y una etiqueta legible. */
public record Vertex(String id, String label) {

    public Vertex {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El identificador es obligatorio");
        }
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("La etiqueta es obligatoria");
        }
    }
}
