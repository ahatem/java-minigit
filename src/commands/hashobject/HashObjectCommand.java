package commands.hashobject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import commands.Command;
import core.Blob;
import repo.ObjectStore;
import repo.Repository;
import repo.WorkTree;
import utils.HashUtils;
import utils.HexUtils;

public class HashObjectCommand implements Command<HashObjectArgs, String> {

    private final Repository repo;

    public HashObjectCommand(Repository repo) {
        this.repo = repo;
    }

    @Override
    public String run(HashObjectArgs input) {
        try {
            WorkTree workTree = repo.workTree();

            Path inputFile = workTree.getPath(input.filePath());
            if (!Files.exists(inputFile)) {
                throw new IOException("File not found");
            }
            if (!Files.isRegularFile(inputFile)) {
                throw new IOException("%s is not a regular file".formatted(inputFile.toString()));
            }

            byte[] content = repo.workTree().read(inputFile);
            Blob blob = new Blob(null, content);

            if (input.writeToDisk()) {
                ObjectStore objStore = repo.objectStore();
                return objStore.writeObject(blob);
            } else {
                byte[] sha1 = HashUtils.sha1(blob.toRaw());
                return HexUtils.bytesToHex(sha1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
