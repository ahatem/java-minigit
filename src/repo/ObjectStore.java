package repo;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.InflaterInputStream;

import core.Blob;
import core.Commit;
import core.Commit.Author;
import core.GitObject;
import core.ObjectId;
import core.Tree;
import core.TreeEntry;

public class ObjectStore {
    private final Path objectsDir;

    public ObjectStore(Path gitDir) {
        this.objectsDir = gitDir.resolve("objects");
    }

    public GitObject readObject(String hash) throws IOException {
        Path objectPath = objectsDir.resolve(hash.substring(0, 2)).resolve(hash.substring(2));
        byte[] decompressedData = decompressObject(objectPath);
        return parseObject(hash, decompressedData);
    }

    // Git format: <type> <size>\0<content>
    private GitObject parseObject(String hash, byte[] decompressedData) throws IOException {
        ByteBuffer dataBuffer = ByteBuffer.wrap(decompressedData);

        StringBuilder header = new StringBuilder();
        while (dataBuffer.hasRemaining()) {
            // Don't use getChar() because it reads 2 bytes (16 bits, UTF-16)
            char b = (char) dataBuffer.get();
            if (b == 0) { // '\0' null terminator character
                break;
            }
            header.append(b);
        }

        String headerStr = header.toString();
        String[] headerParts = headerStr.split(" ");

        String type = headerParts[0];
        int size = Integer.parseInt(headerParts[1]);
        byte[] content = new byte[dataBuffer.remaining()];
        dataBuffer.get(content);

        if (content.length != size) {
            throw new IOException("Size mismatch");
        }

        return switch (type) {
            case "commit" -> parseCommit(hash, content);
            case "tree" -> new Tree(new ObjectId(hash), parseTree(content));
            case "blob" -> new Blob(new ObjectId(hash), content);
            default -> throw new IOException("Unknown type " + type);
        };
    }

    /*
     * 
     */
    private Commit parseCommit(String hash, byte[] content) throws IOException {
        String contentStr = new String(content);
        String[] lines = contentStr.split("\n");

        ObjectId treeId = null;
        List<ObjectId> parents = new ArrayList<>();
        Author author = null;
        Author committer = null;
        StringBuilder message = new StringBuilder();

        boolean inMessage = false;
        for (String line : lines) {
            if (line.startsWith("tree ")) {  // tree <hash>
                treeId = new ObjectId(line.substring(5));
            } else if (line.startsWith("parent ")) { // parent <hash>
                parents.add(new ObjectId(line.substring(7)));
            } else if (line.startsWith("author ")) { // author <info>
                author = parseAuthor(line.substring(7));
            } else if (line.startsWith("committer ")) { // committer <info>
                committer = parseAuthor(line.substring(10));
            } else if (line.isEmpty() && !inMessage) { // blank line starts message
                inMessage = true;
            } else if (inMessage) { // commit message lines
                message.append(line).append("\n");
            }
        }

        String messageStr = message.length() > 0 ? message.substring(0, message.length() - 1) : "";
        if (treeId == null || author == null || committer == null) {
            throw new IOException("Invalid commit format");
        }

        return new Commit(new ObjectId(hash), treeId, parents, author, committer, messageStr);
    }

    // Git author/committer format: name <email> timestamp timezone
    private Author parseAuthor(String authorStr) throws IOException {
        Pattern authorPattern = Pattern.compile("^(.+?) <([^>]+)> (\\d+) ([+-]\\d{4})$");
        Matcher matcher = authorPattern.matcher(authorStr.trim());
        if (matcher.matches()) {
            return new Author(
                    matcher.group(1),
                    matcher.group(2),
                    Long.parseLong(matcher.group(3)),
                    matcher.group(4));
        }

        throw new IOException("Invalid email format in author/committer");
    }

    // Git tree entry format: <mode> <name>\0<20-byte SHA-1>
    private List<TreeEntry> parseTree(byte[] content) {
        List<TreeEntry> entries = new ArrayList<>();

        ByteBuffer buffer = ByteBuffer.wrap(content);
        while (buffer.hasRemaining()) {
            StringBuilder mode = new StringBuilder();
            while (buffer.hasRemaining()) {
                char c = (char) buffer.get();
                if (c == ' ') {
                    break;
                }
                mode.append(c);
            }

            // pad mode to 6 chars (Git uses zero-padded octal)
            while (mode.length() < 6) {
                mode.insert(0, '0');
            }

            StringBuilder name = new StringBuilder();
            while (buffer.hasRemaining()) {
                char c = (char) buffer.get();
                if (c == 0) {
                    break;
                }
                name.append(c);
            }

            byte[] hashBytes = new byte[20];
            buffer.get(hashBytes);
            String hash = bytesToHex(hashBytes);

            entries.add(new TreeEntry(name.toString(), mode.toString(), new ObjectId(hash)));
        }

        return entries;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xFF));
        }
        return sb.toString();
    }

    private static byte[] decompressObject(Path objectPath) throws IOException {
        try (InputStream rawInput = Files.newInputStream(objectPath);
                BufferedInputStream bufferedInput = new BufferedInputStream(rawInput);
                InflaterInputStream decompressedInput = new InflaterInputStream(bufferedInput);
                ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = decompressedInput.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }

            return output.toByteArray();
        }
    }
}
