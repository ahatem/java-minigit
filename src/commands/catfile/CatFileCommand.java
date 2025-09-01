package commands.catfile;

import commands.Command;
import core.GitObject;
import repo.Repository;

public class CatFileCommand implements Command<CatFileArgs, String> {

    private final Repository repo;

    public CatFileCommand(Repository repo) {
        this.repo = repo;
    }

    @Override
    public String run(CatFileArgs input) {
        try {
            GitObject obj = repo.objectStore().readObject(input.objectHash());
            return switch (input.flag()) {
                case 't' -> obj.type();
                case 's' -> String.valueOf(obj.size());
                case 'p' -> obj.toString();
                default -> "Unsupported flag: " + input.flag();
            };
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
