package edu.aitu.visitor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public final class DirectoryNode extends Node {
    private final List<Node> children = new ArrayList<>();
    public DirectoryNode(String name) { super(name); }
    public void add(FileNode child) { addNode(child); }
    public void add(DirectoryNode child) { addNode(child); }
    private void addNode(Node child) {
        Objects.requireNonNull(child, "child");
        if (child.parent() != null) throw new IllegalArgumentException("Node already has a parent");
        for (DirectoryNode ancestor = this; ancestor != null; ancestor = ancestor.parent())
            if (ancestor == child) throw new IllegalArgumentException("Cycle is not allowed");
        if (children.stream().anyMatch(existing -> existing.name().equals(child.name())))
            throw new IllegalArgumentException("Duplicate sibling name");
        children.add(child);
        child.assignParent(this);
    }
    public List<Element> children() { return List.copyOf(children); }
    @Override public void accept(Visitor visitor) { visitor.visit(this); }
}
