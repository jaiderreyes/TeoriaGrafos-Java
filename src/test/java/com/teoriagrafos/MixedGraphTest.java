package com.teoriagrafos;

import com.teoriagrafos.analysis.GraphAnalyzer;
import com.teoriagrafos.analysis.GraphKind;
import com.teoriagrafos.app.TeoriaGrafosApplication;
import com.teoriagrafos.model.IncidenceMatrix;
import com.teoriagrafos.model.MixedGraph;
import com.teoriagrafos.model.WeightedMatrix;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MixedGraphTest {

    @Test
    void buildsAllRepresentationsForTheUrbanExample() {
        MixedGraph graph = TeoriaGrafosApplication.createUrbanMobilityGraph();

        assertEquals(GraphKind.MIXED, GraphAnalyzer.kindOf(graph));
        assertEquals(5, graph.vertices().size());
        assertEquals(6, graph.edges().size());
        assertTrue(GraphAnalyzer.isWeaklyConnected(graph));

        WeightedMatrix adjacency = graph.weightedAdjacencyMatrix();
        int home = adjacency.vertexIds().indexOf("CASA");
        int university = adjacency.vertexIds().indexOf("UNIV");
        int park = adjacency.vertexIds().indexOf("PARQ");

        assertEquals(8.5, adjacency.values()[home][university]);
        assertEquals(WeightedMatrix.NO_CONNECTION,
                adjacency.values()[university][home]);
        assertEquals(2.5, adjacency.values()[home][park]);
        assertEquals(2.5, adjacency.values()[park][home]);

        IncidenceMatrix incidence = graph.incidenceMatrix();
        int edgeE1 = incidence.edgeIds().indexOf("E1");
        assertEquals(-1, incidence.values()[home][edgeE1]);
        assertEquals(1, incidence.values()[university][edgeE1]);
    }
}
