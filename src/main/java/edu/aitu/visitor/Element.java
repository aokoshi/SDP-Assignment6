package edu.aitu.visitor;
public interface Element {
    String name();
    void accept(Visitor visitor);
}
