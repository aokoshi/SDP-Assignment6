# Visitor Pattern: in-memory file system

Java 17 Maven project with an in-memory tree and three independent Visitor operations.

## Run from the project root

```sh
mvn test
mvn compile exec:java
```

## Design

Each `accept` calls the matching overloaded `visit` method (double dispatch). Each visitor owns traversal: its directory overload visits every child once. No element traverses children. Each public operation resets its state before walking; visitor instances can be reused sequentially, but are not thread-safe. The returned search list is an immutable snapshot.

For a directory root, paths omit the root name, use `/`, and are lexicographically sorted; a file root is represented by its name. The root has depth 0 and each child adds 1. Directories count as nodes but add no bytes. Size overflow throws `ArithmeticException`.

Directory additions reject negative sizes at file construction, duplicate names, already-owned nodes and cycles. Names cannot be blank, `.`, `..`, or contain `/`. Children are returned as an immutable snapshot. A node cannot be detached or moved with this API.

`StatisticsVisitor` was added after the two original visitors without changes to elements or the Visitor interface. Adding a new operation only requires a new visitor implementation; adding a new element type requires a new overload and updates to all existing visitors.
