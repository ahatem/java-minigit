package core;

import java.util.List;

public record Commit(
    ObjectId id,
    ObjectId tree,
    List<ObjectId> parents,
    Author author,
    Author committer,
    String message
) implements GitObject {

    public record Author(String name, String email, long timestamp, String timezone) {
        @Override
        public String toString() {
            return String.format("%s <%s> %d %s", name, email, timestamp, timezone);
        }
    }

    @Override
    public String type() {
        return "commit";
    }

    @Override
    public int size() {
        return toString().getBytes().length;
    }

    @Override
    public String toString() {
        StringBuilder content = new StringBuilder();
        content.append("tree ").append(tree.hex()).append("\n");
        for (ObjectId parent : parents) {
            content.append("parent ").append(parent.hex()).append("\n");
        }
        content.append("author ").append(author.toString()).append("\n");
        content.append("committer ").append(committer.toString()).append("\n");
        content.append("\n");
        content.append(message);
        return content.toString();
    }
}
