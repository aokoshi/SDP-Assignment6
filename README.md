# Visitor Pattern: in-memory file system

Java 17 Maven project with an in-memory tree and three independent Visitor operations.

Author: Yelnur Pashanov. Course: Software Design Patterns, Assignment 6.

## Run from the project root

```sh
mvn test
mvn compile exec:java
```

Requires JDK 17 and Maven on `PATH`; no IDE or manual input is required.

Expected output:

```text
Total bytes: 500
Text files >= 100 bytes: [docs/guide.txt, notes.txt]
Files: 3, directories: 4, max depth: 3
```

## Verification

Verified on Java 17.0.20.1 with `mvn clean test exec:java`: **BUILD SUCCESS**.
`edu.aitu.visitor.VisitorTest`: **10 tests, 0 failures, 0 errors, 0 skipped**.
The tests cover both dispatch overloads, nested totals, combined search filters,
inclusive thresholds, sorted relative paths, statistics, empty trees, no matches,
visitor reuse, invalid mutations, overflow, and immutable child access.

## Report

- [Word report](report/SDP_Assignment6_Pashanov_Yelnur.docx)
- [PDF report](report/SDP_Assignment6_Pashanov_Yelnur.pdf)
- [UML diagram](report/uml.png)

## Design

Each `accept` calls the matching overloaded `visit` method (double dispatch). Each visitor owns traversal: its directory overload visits every child once. No element traverses children. Start a complete operation with `calculate`, `search`, or `collect`; these reset state before walking. Visitor instances can be reused sequentially, but are not thread-safe. The returned search list is an immutable snapshot. Nesting has no fixed application limit, but recursive traversal is limited by the JVM call stack.

For a directory root, paths omit the root name, use `/`, and are lexicographically sorted; a file root is represented by its name. The root has depth 0 and each child adds 1. Directories count as nodes but add no bytes. Size overflow throws `ArithmeticException`.

Directory additions reject negative sizes at file construction, duplicate names, already-owned nodes and cycles. Names cannot be blank, `.`, `..`, or contain `/`. Children are returned as an immutable snapshot. A node cannot be detached or moved with this API.

`StatisticsVisitor` was added after the two original visitors without changes to elements or the Visitor interface. Adding a new operation only requires a new visitor implementation; adding a new element type requires a new overload and updates to all existing visitors.
