package main;

import cmd.ArgsHandler;
import cmd.Arguments;
import picocli.CommandLine;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {

        Arguments arguments = new Arguments();
        ArgsHandler argsHandler = new ArgsHandler(arguments.getFilename());

        CommandLine.populateCommand(arguments, args);

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
            Set<String> artistUnuqWords = argsHandler.getArtistUniqWords(arguments.getArtistName());
            artistUnuqWords.stream().forEach(System.out::println);
        }

        if(arguments.getSongName() != null) {
            Set<String> songUnuqWords = argsHandler.getSongUniqWords(arguments.getSongName());
            songUnuqWords.stream().forEach(System.out::println);
        }

        if(arguments.isArtistWordRating()) {
            Set<String> artistWordRating = argsHandler.getArtistWordRating();
            artistWordRating.stream().forEach(System.out::println);
        }

        if(arguments.getSimilarArtistName() != null) {
            Map<String, Integer> similarArtists = argsHandler.getSimilarArtists(arguments.getSimilarArtistName());
            similarArtists.entrySet().stream().forEach(System.out::println);
        }

        if(arguments.getSimilarSongName() != null) {
            Map<String, Integer> similarSongs = argsHandler.getSimilarSongs(arguments.getSimilarSongName());
            similarSongs.entrySet().stream().forEach(System.out::println);
        }

        if(arguments.isTrulyUniqWords()) {
            Set<String> trulyUniqWords = argsHandler.getTrulyUniqWords();
            trulyUniqWords.stream().forEach(System.out::println);
        }
    }
}
