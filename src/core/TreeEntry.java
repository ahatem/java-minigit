package core;

public record TreeEntry(String name, String mode, ObjectId id) {
    String type() {
        String fileType = mode.substring(0, Math.min(3, mode.length()));
        return switch (fileType) {
            case "100", "120" -> "blob";
            case "040" -> "tree";
            case "160" -> "commit";
            default -> "unknown";
        };
    }

    int size() {
        return mode().getBytes().length +
                1 + // Space
                name().getBytes().length +
                1 + // Null byte
                20; // 20-byte SHA-1 hash

    }

    String asString() {
        return String.format("%s %s %s\t%s", mode(), type(), id().hex(), name());
    }
}
