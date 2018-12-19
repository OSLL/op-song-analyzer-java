package main;

import cmd.ArgsHandler;
import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        CommandLine.run(new ArgsHandler(), args);
    }
}
