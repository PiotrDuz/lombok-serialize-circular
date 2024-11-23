package org.pidu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.pidu.graph.MdGraph2;
import org.pidu.graph.MdNode2;
import org.pidu.graph.MdType2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;

public class SerializationTest {

    @Test
    public void runTest(@TempDir Path tempDir) throws IOException, ClassNotFoundException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            MdGraph2 graph2 = new MdGraph2();
            fillAll(graph2);
            validateGraph2(graph2, "a1", "a2", "et");
        }
    }

    private void fillAll(MdGraph2 graph2) {
        graph2.addNode(MdType2.ENTITY_TYPE, "entityRoot");
        graph2.addNode(MdType2.ACCOUNT_CURRENCY, "accountCurrency");
        fill(graph2, "", "entityRoot", "accountCurrency");
        fill(graph2, "x", "entityRoot", "accountCurrency");
    }

    private void fill(MdGraph2 graph2, String prefix, String entityTypeParent, String currency) {
        String ac1 = "a1" + prefix;
        String ac2 = "a2" + prefix;
        String e1 = "e1" + prefix;
        String e2 = "e2" + prefix;
        String et = "et" + prefix;
        graph2.addNode(MdType2.ACCOUNT, ac1);
        graph2.addNode(MdType2.ACCOUNT, ac2);
        graph2.addNode(MdType2.ENTITY, e1);
        graph2.addNode(MdType2.ENTITY, e2);
        graph2.addNode(MdType2.ENTITY_TYPE, et);
        graph2.setParentRelation(MdType2.ACCOUNT, ac1, MdType2.ENTITY, e1);
        graph2.setParentRelation(MdType2.ACCOUNT, ac2, MdType2.ENTITY, e2);
        graph2.setParentRelation(MdType2.ENTITY, e1, MdType2.ENTITY_TYPE, et);
        graph2.setParentRelation(MdType2.ENTITY, e2, MdType2.ENTITY_TYPE, et);
        graph2.setParentRelation(MdType2.ENTITY_TYPE, et, MdType2.ENTITY_TYPE, entityTypeParent);
        graph2.setParentRelation(MdType2.ACCOUNT, ac1, MdType2.ACCOUNT_CURRENCY, currency);
        graph2.setParentRelation(MdType2.ACCOUNT, ac2, MdType2.ACCOUNT_CURRENCY, currency);
    }

    private static void validateGraph2(MdGraph2 graph, String account1, String account2, String entityType) throws IOException, ClassNotFoundException {
        Assertions.assertFalse(isNoEntity(graph, account1));
        Assertions.assertFalse(isNoEntity(graph, account2));

        MdGraph2 graphDeser = serializeAndDeserialize(graph);

        Assertions.assertFalse(isNoEntity(graphDeser, account1));
        Assertions.assertFalse(isNoEntity(graphDeser, account2));
        MdNode2 et = graphDeser.getNode(MdType2.ENTITY_TYPE, entityType).get();
        boolean b = et.getChildren().stream().allMatch(c -> c.getParents().stream().anyMatch(p -> p.getType() == MdType2.ENTITY_TYPE));
        Assertions.assertTrue(b);
    }

    private static MdGraph2 serializeAndDeserialize(MdGraph2 graph) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream javaSerialized = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(javaSerialized);
        objectOutputStream.writeObject(graph);
        objectOutputStream.flush();
        objectOutputStream.close();
        javaSerialized.close();
        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(javaSerialized.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        MdGraph2 graphDeser = (MdGraph2) objectInputStream.readObject();
        objectInputStream.close();
        return graphDeser;
    }

    private static boolean isNoEntity(MdGraph2 graphDeser, String id) {
        return graphDeser.getNode(MdType2.ACCOUNT, id).get().getParents().stream().allMatch(p -> p.getType() != MdType2.ENTITY);
    }
}
