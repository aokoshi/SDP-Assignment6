package edu.aitu.visitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
public final class SearchVisitor implements Visitor {
    private final Predicate<String> criterion;
    private final long minimumSize;
    private final List<String> matches = new ArrayList<>();
    private String path = "";
    private int depth;
    public SearchVisitor(Predicate<String> criterion, long minimumSize) {
        this.criterion = Objects.requireNonNull(criterion, "criterion");
        if (minimumSize < 0) throw new IllegalArgumentException("Minimum size must be non-negative");
        this.minimumSize = minimumSize;
    }
    public List<String> search(Element root) {
        matches.clear(); path = ""; depth = 0;
        Objects.requireNonNull(root, "root").accept(this);
        Collections.sort(matches);
        return List.copyOf(matches);
    }
    @Override public void visit(FileNode file) {
        if (file.size() >= minimumSize && criterion.test(file.name())) matches.add(path + file.name());
    }
    @Override public void visit(DirectoryNode directory) {
        String previous = path;
        if (depth > 0) path += directory.name() + "/";
        depth++;
        for (Element child : directory.children()) child.accept(this);
        depth--;
        path = previous;
    }
}
