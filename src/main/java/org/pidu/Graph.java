package org.pidu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Graph implements Serializable {
    private final Map<String, Node> nodes = new HashMap<>();

    public void createNewNode(String id, String parentId){
        Node node = nodes.computeIfAbsent(id, Node::new);
        Optional.ofNullable(nodes.get(parentId)).ifPresent(parentNode -> {
            parentNode.addChild(node);
            node.addParent(parentNode);
        });
    }

    public Node get(String id){
        return nodes.get(id);
    }
}
