package org.pidu;

import org.junit.jupiter.api.Test;

import java.io.*;

public class SerializeDeserializeTest {
    @Test
    public void graphTest() throws IOException, ClassNotFoundException {
        Graph graph = new Graph();
        String et = "id1";
        String e1 = "id2";
        String e2 = "id3";
        String ac1 = "id4";
        String ac2 = "id5";
        String ac3 = "id6";
        String ac4 = "id7";
        graph.createNewNode(et, null);
        graph.createNewNode(e1, et);
        graph.createNewNode(e2, et);
        graph.createNewNode(ac1, e1);
        graph.createNewNode(ac2, e1);
        graph.createNewNode(ac3, e2);
        graph.createNewNode(ac4, e2);

        for(int i = 0; i < 10000; i++){
            byte[] byteArray = serialize(graph);
            var deserialized = deserialize(byteArray);
            assert deserialized.get(ac1).getParents().stream().anyMatch(par -> par.getId().equals(e1));
            assert deserialized.get(ac2).getParents().stream().anyMatch(par -> par.getId().equals(e1));
            assert deserialized.get(ac3).getParents().stream().anyMatch(par -> par.getId().equals(e2));
            assert deserialized.get(ac4).getParents().stream().anyMatch(par -> par.getId().equals(e2));

            assert deserialized.get(e1).getChildren().size() == 2;
            assert deserialized.get(e2).getChildren().size() == 2;
        }
    }

    private static Graph deserialize(byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
        ObjectInput in = new ObjectInputStream(bis);
        var graph1 = (Graph) in.readObject();
        return graph1;
    }

    private static byte[] serialize(Graph graph) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(graph);
        }
        byte[] byteArray = bos.toByteArray();
        return byteArray;
    }
}
