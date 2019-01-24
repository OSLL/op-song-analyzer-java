package main;

import cmd.ArgsHandler;
import cmd.ArgsHandlerException;
import cmd.Arguments;
import dbService.CSVExecutor;
import dbService.DBException;
import dbService.DBService;
import dbService.ExecutorExeption;
import picocli.CommandLine;

import java.io.IOException;
import java.util.*;
import java.util.logging.LogManager;

public class Main {
    public static void main(String[] args) {
        Arguments arguments = new Arguments();

        try {
            LogManager.getLogManager().readConfiguration(
                    Main.class.getResourceAsStream("/logging.properties"));

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
                listBands.stream().forEach(System.out::println);
            }

            else if(arguments.getSongSubstr() != null) {
                LinkedHashSet<String> listSongs = argsHandler.getListSongs(arguments.getSongSubstr());
                listSongs.stream().forEach(System.out::println);
            }

            else if(arguments.getArtistName() != null) {
                LinkedHashSet<String> artistUnuqWords = argsHandler.getArtistUniqWords(arguments.getArtistName());
                artistUnuqWords.stream().forEach(System.out::println);
            }

            else if(arguments.getSongName() != null) {
                LinkedHashSet<String> songUnuqWords = argsHandler.getSongUniqWords(arguments.getSongName());
                songUnuqWords.stream().forEach(System.out::println);
            }

            else if(arguments.isArtistWordRating()) {
                LinkedHashSet<String> artistWordRating = argsHandler.getArtistWordRating();
                artistWordRating.stream().forEach(System.out::println);
            }

            else if(arguments.getSimilarArtistName() != null) {
                LinkedHashMap<String, Integer> similarArtists = argsHandler.getSimilarArtists(arguments.getSimilarArtistName());
                similarArtists.entrySet().stream().forEach(System.out::println);
            }

            else if(arguments.getSimilarSongName() != null) {
                LinkedHashMap<String, Integer> similarSongs = argsHandler.getSimilarSongs(arguments.getSimilarSongName());
                similarSongs.entrySet().stream().forEach(System.out::println);
            }

            else if(arguments.isTrulyUniqWords()) {
                Set<String> trulyUniqWords = argsHandler.getTrulyUniqWords();
                trulyUniqWords.stream().forEach(System.out::println);
            }

            else {
                commandLine.usage(System.out);
            }

        } catch (CommandLine.ParameterException e) {
            System.out.println(e.getMessage());
            CommandLine.usage(arguments, System.out);
            return;
        } catch (ExecutorExeption | DBException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        } catch (ArgsHandlerException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
