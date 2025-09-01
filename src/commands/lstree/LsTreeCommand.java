package commands.lstree;

import java.io.IOException;
import java.util.stream.Collectors;

import commands.Command;
import core.Commit;
import core.GitObject;
import core.Tree;
import repo.Repository;

public class LsTreeCommand implements Command<LsTreeArgs, String> {
    private final Repository repo;

    public LsTreeCommand(Repository repo) {
        this.repo = repo;
    }

    @Override
    public String run(LsTreeArgs input) {
        try {
            GitObject obj = repo.objectStore().readObject(input.treeHash());
            switch (obj) {
                case Tree tree -> {
                    return getResult(tree, input);
                }
                case Commit commit -> {
                    Tree tree = (Tree) repo.objectStore().readObject(commit.tree().hex());
                    return getResult(tree, input);
                }
                default -> throw new IOException("not a tree object");
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String getResult(Tree tree, LsTreeArgs args) {
        if (!args.onlyFileNames()) {
            return tree.toString();
        }
        return tree.entries().stream().map(entry -> entry.name()).collect(Collectors.joining("\n"));
    }
}
