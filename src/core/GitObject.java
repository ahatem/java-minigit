package core;

public sealed interface GitObject permits Commit, Tree, Blob {

    ObjectId id();

    String type();

    int size();

    String asString();
}
