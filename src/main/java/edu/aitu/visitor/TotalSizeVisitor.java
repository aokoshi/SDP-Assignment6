package edu.aitu.visitor;
import java.util.Objects;
public final class TotalSizeVisitor implements Visitor {
    private long total;
    public long calculate(Element root) {
        total = 0;
        Objects.requireNonNull(root, "root").accept(this);
        return total;
    }
    @Override public void visit(FileNode file) { total = Math.addExact(total, file.size()); }
    @Override public void visit(DirectoryNode directory) {
        for (Element child : directory.children()) child.accept(this);
    }
}
