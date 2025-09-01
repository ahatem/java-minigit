import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import commands.catfile.CatFileArgs;
import commands.catfile.CatFileCommand;
import commands.hashobject.HashObjectArgs;
import commands.hashobject.HashObjectCommand;
import commands.init.InitArgs;
import commands.init.InitCommand;
import commands.lstree.LsTreeArgs;
import commands.lstree.LsTreeCommand;
import repo.Repository;

public class Main {
    private static final Path projectDir = Paths.get("testing-git");

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            printUsage();
            return;
        }

        String command = args[0].toLowerCase();

        if (command.equals("init")) {
            String output = new InitCommand().run(new InitArgs(projectDir));
            System.out.print(output);
            return;
        }

        Repository repo = Repository.open(projectDir);
        switch (command) {
            case "cat-file" -> {
                char flag = args[1].charAt(1);
                String objectHash = args[2];
                String output = new CatFileCommand(repo).run(new CatFileArgs(flag, objectHash));
                System.out.print(output);
            }
            case "hash-object" -> {
                String flag = args[1];
                String filePath = args[1];

                boolean writeToDisk = false;
                if (flag.equals("-w")) {
                    writeToDisk = true;
                    filePath = args[2];
                }

                HashObjectArgs hoArgs = new HashObjectArgs(writeToDisk, filePath);
                String output = new HashObjectCommand(repo).run(hoArgs);
                System.out.print(output);
            }
            case "ls-tree" -> {
                String flag = args[1];
                String objectHash = args[1];

                boolean onlyFileNames = false;
                if (flag.equals("--name-only")) {
                    onlyFileNames = true;
                    objectHash = args[2];
                }

                String output = new LsTreeCommand(repo).run(new LsTreeArgs(onlyFileNames, objectHash));
                System.out.print(output);
            }
            default -> {
                System.out.println("Args: " + Arrays.toString(args));
            }
        }
    }

    private static void printUsage() {
        System.out.println("Usage: minigit <command> [options]");
        System.out.println("Commands:");
        System.out.println("  init");
        System.out.println("  cat-file (-p | -t | -s) <object>");
    }
}