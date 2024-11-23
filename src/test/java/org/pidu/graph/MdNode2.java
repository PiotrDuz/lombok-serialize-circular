package org.pidu.graph;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
public final class MdNode2 implements Serializable {
    //private final Logger log = LoggerFactory.getLogger("node");

    private final String id;
    private final MdType2 type;
    private final Set<MdNode2> children = new HashSet<>();
    private final Set<MdNode2> parents = new HashSet<>();

    MdNode2() { // for serialization
        id = null;
        type = null;
    }

    public MdNode2(MdType2 type, String id) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public MdType2 getType() {
        return type;
    }

    void addChild(MdNode2 child) {
        children.add(child);
        log.info("Child added");
    }

    void addParent(MdNode2 parent) {
        parents.add(parent);
        log.info("Parent added");
    }

    public Set<MdNode2> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public Set<MdNode2> getParents() {
        return Collections.unmodifiableSet(parents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MdNode2 mdNode = (MdNode2) o;
        return Objects.equals(id, mdNode.id) && type == mdNode.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}
