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

    public byte[] toRaw() {
        String header = "blob " + content.length + "\0";
        byte[] headerBytes = header.getBytes();

        byte[] raw = new byte[headerBytes.length + content.length];
        System.arraycopy(headerBytes, 0, raw, 0, headerBytes.length);
        System.arraycopy(content, 0, raw, headerBytes.length, content.length);

        return raw;
    }
}
