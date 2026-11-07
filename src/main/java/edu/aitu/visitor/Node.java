package edu.aitu.visitor;
import java.util.Objects;
abstract class Node implements Element {
    private final String name;
    private DirectoryNode parent;
    Node(String name) {
        Objects.requireNonNull(name, "name");
        if (name.isBlank() || name.contains("/") || name.equals(".") || name.equals(".."))
            throw new IllegalArgumentException("Invalid node name");
        this.name = name;
    }
    public final String name() { return name; }
    final DirectoryNode parent() { return parent; }
    final void assignParent(DirectoryNode parent) { this.parent = parent; }
}
