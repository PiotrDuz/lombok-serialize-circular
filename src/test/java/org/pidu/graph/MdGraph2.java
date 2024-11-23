package org.pidu.graph;



import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//@Slf4j
public class MdGraph2 implements Serializable {
    private final Logger log = LoggerFactory.getLogger("graph");
    private final Map<MdType2, Map<String, MdNode2>> nodes = new EnumMap<>(MdType2.class);

    public MdGraph2() { // for deserialization
    }

    public void addNode(MdType2 type, String id) {
        MdNode2 node = new MdNode2(type, id);
        nodes.computeIfAbsent(node.getType(), t -> new HashMap<>())
                .put(node.getId(), node);
        log.info("Added node");
    }

    public void setParentRelation(MdType2 type, String id, MdType2 parentType, String parentId) {
        getNode(type, id)
                .flatMap(node -> getNode(parentType, parentId)
                        .map(parentNode -> {
                            parentNode.addChild(node);
                            node.addParent(parentNode);
                            return true;
                        }))
                .orElse(false);
    }

    public Optional<MdNode2> getNode(MdType2 type, String id) {
        return Optional.ofNullable(nodes.getOrDefault(type, Collections.emptyMap()).get(id));
    }
}
