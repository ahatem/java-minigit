package commands.init;

import repo.Repository;
import java.io.IOException;

import commands.Command;

public class InitCommand implements Command<InitArgs, String> {
    @Override
    public String run(InitArgs input) {
        try {
            Repository.init(input.directory());
            return "Initialized git repository in " + input.directory().resolve(".git");
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
}
