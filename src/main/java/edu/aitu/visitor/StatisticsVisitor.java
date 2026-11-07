package edu.aitu.visitor;
import java.util.Objects;
public final class StatisticsVisitor implements Visitor {
    public record Statistics(int files, int directories, int maxDepth) { }
    private int files, directories, maxDepth, depth;
    public Statistics collect(Element root) {
        files = directories = maxDepth = depth = 0;
        Objects.requireNonNull(root, "root").accept(this);
        return new Statistics(files, directories, maxDepth);
    }
    private void recordDepth() { maxDepth = Math.max(maxDepth, depth); }
    @Override public void visit(FileNode file) { files++; recordDepth(); }
    @Override public void visit(DirectoryNode directory) {
        directories++; recordDepth(); depth++;
        for (Element child : directory.children()) child.accept(this);
        depth--;
    }
}
