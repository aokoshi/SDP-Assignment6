package edu.aitu.visitor;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class VisitorTest {
    private DirectoryNode tree() {
        DirectoryNode root = new DirectoryNode("root"), b = new DirectoryNode("b"), nested = new DirectoryNode("nested");
        root.add(new FileNode("z.txt", 20)); root.add(b); root.add(new DirectoryNode("empty"));
        b.add(new FileNode("small.txt", 9)); b.add(new FileNode("large.bin", 30)); b.add(nested);
        nested.add(new FileNode("a.txt", 10)); return root;
    }
    @Test void doubleDispatchCallsCorrectOverloads() {
        List<String> calls = new ArrayList<>();
        Visitor visitor = new Visitor() {
            public void visit(FileNode f) { calls.add("file:" + f.name()); }
            public void visit(DirectoryNode d) { calls.add("directory:" + d.name()); }
        };
        new FileNode("a", 1).accept(visitor); new DirectoryNode("b").accept(visitor);
        assertEquals(List.of("file:a", "directory:b"), calls);
    }
    @Test void sizeIncludesEveryNestedFile() { assertEquals(69, new TotalSizeVisitor().calculate(tree())); }
    @Test void searchCombinesNameAndInclusiveThreshold() {
        assertEquals(List.of("b/nested/a.txt", "z.txt"), new SearchVisitor(n -> n.endsWith(".txt"), 10).search(tree()));
    }
    @Test void searchSortsRelativePathsIndependentlyOfInsertionOrder() {
        DirectoryNode root = new DirectoryNode("root"), z = new DirectoryNode("z"), a = new DirectoryNode("a");
        z.add(new FileNode("x", 1)); a.add(new FileNode("x", 1)); root.add(z); root.add(a);
        assertEquals(List.of("a/x", "z/x"), new SearchVisitor(n -> true, 0).search(root));
        assertEquals(List.of("x"), new SearchVisitor(n -> true, 0).search(new FileNode("x", 1)));
    }
    @Test void statisticsIncludeRootAndDeepestFile() {
        assertEquals(new StatisticsVisitor.Statistics(4, 4, 3), new StatisticsVisitor().collect(tree()));
    }
    @Test void emptyDirectoryAndNoMatchesAreHandled() {
        DirectoryNode empty = new DirectoryNode("empty");
        assertEquals(0, new TotalSizeVisitor().calculate(empty));
        assertEquals(new StatisticsVisitor.Statistics(0, 1, 0), new StatisticsVisitor().collect(empty));
        assertEquals(List.of(), new SearchVisitor(n -> n.endsWith(".pdf"), 0).search(tree()));
    }
    @Test void repeatedRunsResetAllVisitors() {
        DirectoryNode root = tree(); FileNode single = new FileNode("one.txt", 1);
        TotalSizeVisitor size = new TotalSizeVisitor(); SearchVisitor search = new SearchVisitor(n -> true, 0); StatisticsVisitor stats = new StatisticsVisitor();
        assertEquals(69, size.calculate(root)); assertEquals(1, size.calculate(single)); assertEquals(69, size.calculate(root));
        assertEquals(4, search.search(root).size()); assertEquals(List.of("one.txt"), search.search(single)); assertEquals(4, search.search(root).size());
        assertEquals(4, stats.collect(root).files()); assertEquals(new StatisticsVisitor.Statistics(1, 0, 0), stats.collect(single)); assertEquals(4, stats.collect(root).files());
    }
    @Test void invalidTreeChangesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("bad", -1));
        DirectoryNode root = new DirectoryNode("root"), child = new DirectoryNode("child"), other = new DirectoryNode("other");
        root.add(child); child.add(new DirectoryNode("nested"));
        assertThrows(IllegalArgumentException.class, () -> root.add(new FileNode("child", 0)));
        assertThrows(IllegalArgumentException.class, () -> other.add(child));
        assertThrows(IllegalArgumentException.class, () -> child.add(root));
        assertThrows(IllegalArgumentException.class, () -> root.add(root));
        assertEquals(1, root.children().size());
        assertThrows(UnsupportedOperationException.class, () -> root.children().clear());
    }
    @Test void sizeOverflowIsExplicit() {
        DirectoryNode root = new DirectoryNode("root"); root.add(new FileNode("a", Long.MAX_VALUE)); root.add(new FileNode("b", 1));
        assertThrows(ArithmeticException.class, () -> new TotalSizeVisitor().calculate(root));
    }
    @Test void namesAndThresholdAreValidated() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("a/b", 0));
        assertThrows(IllegalArgumentException.class, () -> new SearchVisitor(n -> true, -1));
    }
}
