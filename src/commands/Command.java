package commands;

@FunctionalInterface
public interface Command<Input, Output> {
    Output run(Input input);
}
