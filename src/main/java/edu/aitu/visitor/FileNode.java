package edu.aitu.visitor;
public final class FileNode extends Node {
    private final long size;
    public FileNode(String name, long size) {
        super(name);
        if (size < 0) throw new IllegalArgumentException("Size must be non-negative");
        this.size = size;
    }
    public long size() { return size; }
    @Override public void accept(Visitor visitor) { visitor.visit(this); }
}
