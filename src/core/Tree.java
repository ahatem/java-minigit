package core;

import java.util.List;
import java.util.stream.Collectors;

public record Tree(ObjectId id, List<TreeEntry> entries) implements GitObject {
    @Override
    public String type() {
        return "tree";
    }

    @Override
    public int size() {
        return entries.stream().mapToInt(TreeEntry::size).sum();
    }

    @Override
    public String asString() {
        return entries.stream().map(TreeEntry::asString).collect(Collectors.joining("\n"));
    }
}
