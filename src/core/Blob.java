package core;

public record Blob(ObjectId id, byte[] content) implements GitObject {
    @Override
    public String type() {
        return "blob";
    }

    @Override
    public int size() {
        return content.length;
    }

    @Override
    public String toString() {
        return new String(content);
    }
}
