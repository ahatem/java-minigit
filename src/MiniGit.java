import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class MiniGit {
    private static final String projectName = "testing-git";

    public void initGitRepo() {
        Path miniGitPath = Paths.get(projectName, ".git");
        if (Files.exists(miniGitPath)) {
            System.out.println("Repository is already initialized");
            return;
        }

        try {
            Files.createDirectories(miniGitPath);
            Files.createDirectories(Paths.get(projectName, ".git/objects"));

            Files.createDirectories(Paths.get(projectName, ".git/refs"));
            Files.createDirectories(Paths.get(projectName, ".git/refs/heads"));

            Files.writeString(Paths.get(projectName, ".git/HEAD"), "ref: refs/heads/master\n");
            System.out.println("Initialized git Repository");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MiniGit miniGit = new MiniGit();
        if (args[0] != null && args[0].equals("init")) {
            miniGit.initGitRepo();
        } else {
            System.out.println("Unknown args: " + Arrays.toString(args));
        }
    }
}