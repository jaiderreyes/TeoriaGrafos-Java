package com.teoriagrafos.model;

/** Arista o arco con etiqueta, peso y tipo de orientación. */
public record Edge(String id, String from, String to, String label,
        double weight, EdgeType type) {

    public Edge {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("La arista necesita un identificador");
        }
        if (from == null || from.isBlank() || to == null || to.isBlank()) {
            throw new IllegalArgumentException("Los extremos son obligatorios");
        }
        if (from.equals(to)) {
            throw new IllegalArgumentException("Este ejemplo no admite lazos");
        }
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("La etiqueta de la arista es obligatoria");
        }
        if (!Double.isFinite(weight) || weight <= 0) {
            throw new IllegalArgumentException("El peso debe ser positivo y finito");
        }
        if (type == null) {
            throw new IllegalArgumentException("El tipo de arista es obligatorio");
        }
    }
}
