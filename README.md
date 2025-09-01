# MiniGit

MiniGit is a lightweight, educational implementation of Git’s core functionality, written in Java. It’s a project to explore and understand Git’s internals by recreating its key components, like objects, commits, and the index. This is a learning tool, not a production-ready replacement for Git, but it’s a fun way to dig into how version control works under the hood.

## Features

- `init`: Initializes a new Git repository with a `.git` directory.
- `hash-object`: Computes SHA-1 hashes for files and optionally stores them as blob objects.
- `cat-file`: Displays details about Git objects (type, size, or content).
- `ls-tree`: Lists the contents of a tree object, with an option to show only file names.

More features, like `add` and `commit`, are in progress as the project evolves.

## Project Structure

- `src/core/`: Defines Git objects (`Blob`, `Tree`, `Commit`, `IndexEntry`).
- `src/commands/`: Implements commands (`InitCommand`, `CatFileCommand`, etc.).
- `src/repo/`: Manages repository logic (`Repository`, `ObjectStore`, `WorkTree`).
- `src/utils/`: Utilities for hashing, compression, and hex conversion.
- `src/Main.java`: Entry point for running commands.

## Getting Started

MiniGit is built with Java 21. Clone the repo, compile the code, and run commands via the `Main` class. Check `src/Main.java` for supported commands and their usage.

## Contributing

This is primarily a learning project, but feel free to open issues for bugs, ideas, or questions about Git’s internals. Contributions are welcome, especially if you’ve got insights into Git’s plumbing!

## Acknowledgments

- [CodeCrafters](https://app.codecrafters.io/catalog): For providing a great starting point to build and learn Git internals.
- [Git Documentation](https://git-scm.com/docs/): Official reference for Git’s commands and internals.
- [Pro Git Book](https://git-scm.com/book): An awesome resource for understanding Git’s concepts in depth.

## License

Licensed under the MIT License. See the `LICENSE` file for details.