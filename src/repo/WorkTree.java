package repo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WorkTree {
    private final Path workTreeDir;

    public WorkTree(Path workTreeDir) {
        this.workTreeDir = workTreeDir;
    }

    public byte[] read(Path filePath) throws IOException {
        return Files.readAllBytes(filePath);
    }

    public Path getPath(String filePath) {
        return workTreeDir.resolve(filePath);
    }
    
}
