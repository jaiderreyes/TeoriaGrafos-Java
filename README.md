# Teoría de grafos con Java

Proyecto educativo en Java 17 y Maven que explica los conceptos fundamentales
de la teoría de grafos mediante un ejemplo real: una **red de movilidad urbana**.
Es compatible con Apache NetBeans.

## 1. ¿Qué es un grafo?

Un grafo se representa matemáticamente como `G = (V, E)`:

- `V` es el conjunto de **vértices** o nodos.
- `E` es el conjunto de **aristas** o conexiones.

En el ejemplo, los vértices son lugares de una ciudad y las conexiones son
calles, senderos, ciclovías o rutas. Esta abstracción también sirve para redes
sociales, Internet, mapas, circuitos, dependencias y sistemas de transporte.

## 2. Vértices, etiquetas y pesos

Una **etiqueta** da significado a un elemento. El vértice `HOSP`, por ejemplo,
tiene la etiqueta `Hospital`. Una conexión también puede estar etiquetada como
`Avenida Central` o `Sendero peatonal`.

Un **peso** es un valor numérico asociado a una arista. Puede representar:

- Distancia en kilómetros.
- Tiempo de viaje.
- Precio o consumo de combustible.
- Capacidad de una red.
- Riesgo o prioridad.

Este proyecto usa kilómetros y exige pesos positivos.

## 3. Grafos dirigidos, no dirigidos y mixtos

### Grafo no dirigido

Una arista `{u, v}` no tiene orientación. Si existe conexión entre `u` y `v`, se
puede recorrer en ambos sentidos. Se representa como `u -- v`.

Ejemplo: el sendero entre la casa y el parque.

### Grafo dirigido o dígrafo

Un arco `(u, v)` tiene origen y destino. Poder ir de `u` hacia `v` no implica
que se pueda regresar. Se representa como `u -> v`.

Ejemplo: una avenida de sentido único desde la casa hacia la universidad.

### Grafo mixto

Combina aristas no dirigidas y arcos dirigidos. La red urbana del proyecto es
mixta porque incluye calles de un solo sentido y rutas de doble sentido.

## 4. Otros tipos de grafos

| Tipo | Descripción |
|---|---|
| Simple | No contiene lazos ni aristas paralelas. |
| Multigrafo | Puede tener varias aristas entre el mismo par de vértices. |
| Pseudografo | Permite lazos y, normalmente, aristas paralelas. |
| Nulo | Tiene vértices, pero no tiene aristas. |
| Trivial | Tiene un solo vértice. |
| Completo | Cada par de vértices diferentes está conectado. |
| Regular | Todos los vértices tienen el mismo grado. |
| Bipartito | Los vértices se dividen en dos conjuntos y las aristas los cruzan. |
| Conexo | Existe un camino entre cualquier par de vértices. |
| Desconexo | Tiene dos o más componentes sin conexión entre ellas. |
| Cíclico | Contiene al menos un ciclo. |
| Acíclico | No contiene ciclos. |
| Árbol | Grafo no dirigido, conexo y acíclico. |
| DAG | Grafo dirigido acíclico, útil para dependencias y planificación. |
| Ponderado | Sus conexiones tienen pesos. |
| Mixto | Combina conexiones dirigidas y no dirigidas. |

La implementación es un grafo simple, ponderado, etiquetado y mixto. Para
mantener clara la matriz de incidencia no admite lazos.

## 5. Grado de un vértice

En un grafo no dirigido, el **grado** es la cantidad de aristas incidentes. En
un grafo dirigido se distinguen:

- **Grado de entrada:** arcos que llegan al vértice.
- **Grado de salida:** arcos que salen del vértice.

En el grafo mixto el programa informa entrada, salida y conexiones no dirigidas
por separado.

## 6. Lista de adyacencia

Guarda, para cada vértice, sus vecinos alcanzables. Por ejemplo:

```text
CASA -> UNIV(8.5), PARQ(2.5)
UNIV -> HOSP(4.0)
PARQ -> CASA(2.5), CENTRO(3.0), HOSP(6.0)
```

Una conexión no dirigida aparece en las listas de ambos extremos. Un arco
dirigido aparece solamente en la lista de su origen.

Ventajas:

- Requiere `O(V + E)` espacio.
- Es eficiente para grafos dispersos.
- Permite recorrer rápidamente los vecinos de un nodo.

## 7. Matriz de adyacencia

Es una matriz cuadrada `V × V`. La celda `[i][j]` indica si existe conexión
desde el vértice `i` hasta `j`. En este proyecto guarda el peso:

- `0` en la diagonal.
- El peso cuando existe una conexión.
- `∞` cuando no existe conexión directa.

Una arista no dirigida produce dos valores simétricos. Un arco dirigido solo
produce el valor `origen → destino`, por lo que la matriz puede no ser simétrica.

Ventajas y costos:

- Consultar una conexión cuesta `O(1)`.
- Ocupa `O(V²)` memoria.
- Es apropiada para grafos densos.

## 8. Matriz de incidencia

Relaciona vértices con conexiones. Las filas representan vértices y las
columnas representan aristas o arcos.

Convención utilizada:

- Arco dirigido: `-1` en el origen y `+1` en el destino.
- Arista no dirigida: `+1` en cada extremo.
- `0` cuando el vértice no participa en la conexión.

Esta representación permite estudiar flujo, conservación, ciclos y redes
eléctricas. Su tamaño es `V × E`.

## 9. Ejemplo real: movilidad urbana

Los vértices son:

| Código | Etiqueta |
|---|---|
| CASA | Conjunto residencial |
| UNIV | Universidad |
| HOSP | Hospital |
| PARQ | Parque central |
| CENTRO | Centro comercial |

Las conexiones son:

| ID | Conexión | Tipo | Etiqueta | Peso |
|---|---|---|---|---:|
| E1 | CASA → UNIV | Dirigida | Avenida Central | 8.5 km |
| E2 | UNIV → HOSP | Dirigida | Carrera 10 | 4.0 km |
| E3 | CASA — PARQ | No dirigida | Sendero peatonal | 2.5 km |
| E4 | PARQ — CENTRO | No dirigida | Ciclovía | 3.0 km |
| E5 | CENTRO → HOSP | Dirigida | Autopista Norte | 5.5 km |
| E6 | HOSP — PARQ | No dirigida | Ruta urbana | 6.0 km |

Es un grafo mixto, ponderado, etiquetado y débilmente conexo. La conectividad
débil ignora temporalmente la orientación para verificar que todos los lugares
pertenezcan a una sola componente.

## 10. Organización del código

```text
src/main/java/com/teoriagrafos/
├── app/TeoriaGrafosApplication.java
├── analysis/
│   ├── Degree.java
│   ├── GraphAnalyzer.java
│   ├── GraphKind.java
│   └── GraphSummary.java
└── model/
    ├── Vertex.java
    ├── Edge.java
    ├── EdgeType.java
    ├── Adjacent.java
    ├── MixedGraph.java
    ├── WeightedMatrix.java
    └── IncidenceMatrix.java
```

### `MixedGraph`

Mantiene los vértices en un `LinkedHashMap` y las conexiones en una lista. El
orden estable facilita comparar las matrices y la salida del programa.

```java
graph.addVertex("CASA", "Conjunto residencial");
graph.addDirectedEdge("E1", "CASA", "UNIV",
        "Avenida Central, sentido único", 8.5);
graph.addUndirectedEdge("E3", "CASA", "PARQ",
        "Sendero peatonal", 2.5);
```

Sus métodos `adjacencyList`, `weightedAdjacencyMatrix` e `incidenceMatrix`
construyen las tres representaciones estudiadas.

### `GraphAnalyzer`

Clasifica el grafo como vacío, dirigido, no dirigido o mixto; calcula grados,
peso total y conectividad débil mediante una búsqueda por anchura.

### `TeoriaGrafosApplication`

Crea el caso urbano y presenta vértices, aristas, grados, lista de adyacencia,
matriz de adyacencia ponderada y matriz de incidencia.

## 11. Ejecutar en NetBeans

1. Seleccionar **File > Open Project**.
2. Abrir la carpeta `TeoriaGrafos-Java`.
3. Ejecutar `TeoriaGrafosApplication.java`.

## 12. Ejecutar con Maven

```bash
mvn test
mvn exec:java
```

La prueba `MixedGraphTest` verifica la clasificación, conectividad, dirección,
pesos y signos de la matriz de incidencia.
