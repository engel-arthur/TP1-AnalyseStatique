package utils;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
public final class GraphUtils {
    private GraphUtils(){};

    public static String serializeGraphToDotFormat(Graph<String, DefaultEdge> graph) {
        //Source : https://jgrapht.org/guide/UserOverview#graph-serialization-and-exportimport
        DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>();
        exporter.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute(v));
            return map;
        });

        Writer writer = new StringWriter();
        exporter.exportGraph(graph, writer);
        return writer.toString();
    }

    public static void convertGraphSerializedInDotFormatToPNG(String serializedGraph, String pathToSaveTo) throws IOException {
        Graphviz.fromString(serializedGraph).render(Format.PNG).toFile(new File(pathToSaveTo));
    }
}
