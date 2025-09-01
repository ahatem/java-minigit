package repo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Repository {
    private final Path workTree;
    private final Path gitDir;
    private final ObjectStore objectStore;

    private Repository(Path workTree, Path gitDir) {
        this.workTree = workTree;
        this.gitDir = gitDir;
        this.objectStore = new ObjectStore(gitDir);
    }

    public static Repository open(Path dir) throws IOException {
        Path gitDir = dir.resolve(".git");
        if (!Files.isDirectory(gitDir)) {
            throw new IOException("Not a git repository: " + gitDir);
        }
        return new Repository(dir, gitDir);
    }

    public static Repository init(Path dir) throws IOException {
        Path gitDir = dir.resolve(".git");
        if (Files.exists(gitDir)) {
            throw new IOException("Already a git repository");
        }

        Files.createDirectories(gitDir.resolve("objects"));
        Files.createDirectories(gitDir.resolve("refs/heads"));

        Files.writeString(gitDir.resolve("HEAD"), "ref: refs/heads/main\n");

        return new Repository(dir, gitDir);
    }

    public Path workTree() {
        return workTree;
    }

    public Path gitDir() {
        return gitDir;
    }

    public ObjectStore objectStore() {
        return objectStore;
    }

}
