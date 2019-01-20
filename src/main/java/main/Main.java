package main;

import cmd.ArgsHandler;
import cmd.Arguments;
import picocli.CommandLine;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        Arguments arguments = new Arguments();
        ArgsHandler argsHandler = new ArgsHandler(arguments.getFilename());
        try {
            CommandLine commandLine = new CommandLine(arguments);
            commandLine.parse(args);

            if (commandLine.isUsageHelpRequested()) {
                commandLine.usage(System.out);
                return;
            } else if (commandLine.isVersionHelpRequested()) {
                commandLine.printVersionHelp(System.out);
                return;
            }
        } catch (CommandLine.ParameterException e) {
            System.out.println(e.getMessage());
            CommandLine.usage(arguments, System.out);
            return;
        }


        if(arguments.isAbout()) { argsHandler.printAbout(); }

        if(arguments.getArtistSubstr() != null) {
            LinkedHashSet<String> listBands = argsHandler.getListBands(arguments.getArtistSubstr());
            listBands.stream().forEach(System.out::println);
        }

        if(arguments.getSongSubstr() != null) {
            LinkedHashSet<String> listSongs = argsHandler.getListSongs(arguments.getSongSubstr());
            listSongs.stream().forEach(System.out::println);
        }

        if(arguments.getArtistName() != null) {
            LinkedHashSet<String> artistUnuqWords = argsHandler.getArtistUniqWords(arguments.getArtistName());
            artistUnuqWords.stream().forEach(System.out::println);
        }

        if(arguments.getSongName() != null) {
            LinkedHashSet<String> songUnuqWords = argsHandler.getSongUniqWords(arguments.getSongName());
            songUnuqWords.stream().forEach(System.out::println);
        }

        if(arguments.isArtistWordRating()) {
            LinkedHashSet<String> artistWordRating = argsHandler.getArtistWordRating();
            artistWordRating.stream().forEach(System.out::println);
        }

        if(arguments.getSimilarArtistName() != null) {
            LinkedHashMap<String, Integer> similarArtists = argsHandler.getSimilarArtists(arguments.getSimilarArtistName());
            similarArtists.entrySet().stream().forEach(System.out::println);
        }

        if(arguments.getSimilarSongName() != null) {
            LinkedHashMap<String, Integer> similarSongs = argsHandler.getSimilarSongs(arguments.getSimilarSongName());
            similarSongs.entrySet().stream().forEach(System.out::println);
        }

        if(arguments.isTrulyUniqWords()) {
            Set<String> trulyUniqWords = argsHandler.getTrulyUniqWords();
            trulyUniqWords.stream().forEach(System.out::println);
        }
    }
}
