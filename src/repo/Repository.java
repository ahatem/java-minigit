package repo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Repository {
    private final WorkTree workTree;
    private final Path gitDir;
    private final ObjectStore objectStore;

    private Repository(Path workTreeDir, Path gitDir) {
        this.workTree = new WorkTree(workTreeDir);
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
        
        Files.createFile(gitDir.resolve("index"));
        Files.writeString(gitDir.resolve("HEAD"), "ref: refs/heads/master\n");

        return new Repository(dir, gitDir);
    }

    public WorkTree workTree() {
        return workTree;
    }

    public Path gitDir() {
        return gitDir;
    }

    public ObjectStore objectStore() {
        return objectStore;
    }

}
