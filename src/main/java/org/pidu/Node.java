package org.pidu;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Node implements Serializable {
    private final String id;
    private final Set<Node> children = new HashSet<>();
    private final Set<Node> parents = new HashSet<>();

    public Node(String id) {
        this.id = id;
    }

    public void addChild(Node child){
        children.add(child);
    }
    public void addParent(Node parent){
        parents.add(parent);
    }

    public String getId() {
        return id;
    }

    public Set<Node> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public Set<Node> getParents() {
        return Collections.unmodifiableSet(parents);
    }
}
