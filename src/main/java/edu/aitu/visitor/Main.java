package edu.aitu.visitor;
public final class Main {
    public static void main(String[] args) {
        DirectoryNode root = new DirectoryNode("workspace");
        DirectoryNode docs = new DirectoryNode("docs");
        DirectoryNode archive = new DirectoryNode("archive");
        root.add(new FileNode("notes.txt", 120));
        root.add(docs);
        docs.add(new FileNode("guide.txt", 300));
        docs.add(archive);
        archive.add(new FileNode("old.txt", 80));
        root.add(new DirectoryNode("empty"));
        System.out.println("Total bytes: " + new TotalSizeVisitor().calculate(root));
        System.out.println("Text files >= 100 bytes: " + new SearchVisitor(name -> name.endsWith(".txt"), 100).search(root));
        var stats = new StatisticsVisitor().collect(root);
        System.out.printf("Files: %d, directories: %d, max depth: %d%n", stats.files(), stats.directories(), stats.maxDepth());
    }
}
