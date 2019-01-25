package main;

import cmd.ArgsHandler;
import cmd.ArgsHandlerException;
import cmd.Arguments;

import dbService.CSVExecutor;
import dbService.DBException;
import dbService.DBService;
import dbService.ExecutorExeption;

import picocli.CommandLine;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        Arguments arguments = new Arguments();

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

            CSVExecutor csvExecutor = new CSVExecutor(arguments.getFilePath());
            DBService dbService = new DBService(csvExecutor.getReader());
            ArgsHandler argsHandler = new ArgsHandler(dbService);

            if(arguments.isAbout()) { argsHandler.printAbout(); }

            else if(arguments.getArtistSubstr() != null) {
                LinkedHashSet<String> listBands = argsHandler.getListBands(arguments.getArtistSubstr());
                listBands.forEach(System.out::println);
            }

            else if(arguments.getSongSubstr() != null) {
                LinkedHashSet<String> listSongs = argsHandler.getListSongs(arguments.getSongSubstr());
                listSongs.forEach(System.out::println);
            }

            else if(arguments.getArtistName() != null) {
                LinkedHashSet<String> artistUnuqWords = argsHandler.getArtistUniqWords(arguments.getArtistName());
                artistUnuqWords.forEach(System.out::println);
            }

            else if(arguments.getSongName() != null) {
                LinkedHashSet<String> songUnuqWords = argsHandler.getSongUniqWords(arguments.getSongName());
                songUnuqWords.forEach(System.out::println);
            }

            else if(arguments.isArtistWordRating()) {
                LinkedHashSet<String> artistWordRating = argsHandler.getArtistWordRating();
                artistWordRating.forEach(System.out::println);
            }

            else if(arguments.getSimilarArtistName() != null) {
                LinkedHashMap<String, Integer> similarArtists = argsHandler.getSimilarArtists(arguments.getSimilarArtistName());
                similarArtists.entrySet().forEach(System.out::println);
            }

            else if(arguments.getSimilarSongName() != null) {
                LinkedHashMap<String, Integer> similarSongs = argsHandler.getSimilarSongs(arguments.getSimilarSongName());
                similarSongs.entrySet().forEach(System.out::println);
            }

            else if(arguments.isTrulyUniqWords()) {
                Set<String> trulyUniqWords = argsHandler.getTrulyUniqWords();
                trulyUniqWords.forEach(System.out::println);
            }

            else {
                commandLine.usage(System.out);
            }

        } catch (CommandLine.ParameterException e) {
            System.out.println(e.getMessage());
            CommandLine.usage(arguments, System.out);
        } catch (ExecutorExeption | DBException | ArgsHandlerException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
