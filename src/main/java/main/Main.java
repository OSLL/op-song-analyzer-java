package main;

import cmd.ArgsHandler;
import cmd.ArgsHandlerException;
import cmd.Arguments;

import dbService.CSVExecutor;
import dbService.DBException;
import dbService.DBService;
import dbService.ExecutorExeption;

import picocli.CommandLine;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.LogManager;

public class Main {
    public static void main(String[] args) {

        Arguments arguments = new Arguments();

        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("resources/logging.properties"));

            CommandLine commandLine = new CommandLine(arguments);
            commandLine.parse(args);

            if (commandLine.isUsageHelpRequested()) {                                                             // -h
                commandLine.usage(System.out);
                return;
            } else if (commandLine.isVersionHelpRequested()) {                                                    // -V
                commandLine.printVersionHelp(System.out);
                return;
            }

            CSVExecutor csvExecutor = new CSVExecutor(arguments.getFilePath());
            DBService dbService = new DBService(csvExecutor.getReader());
            ArgsHandler argsHandler = new ArgsHandler(dbService);

            if(arguments.isAbout()) { argsHandler.printAbout(); }                                             // -about

            else if(arguments.getArtistSubstr() != null) {                                              // --list_bands
                LinkedHashSet<String> listBands = argsHandler.getListBands(arguments.getArtistSubstr());
                listBands.forEach(System.out::println);
            }

            else if(arguments.getSongSubstr() != null) {                                                // --list_songs
                LinkedHashSet<String> listSongs = argsHandler.getListSongs(arguments.getSongSubstr());
                listSongs.forEach(System.out::println);
            }

            else if(arguments.getArtistName() != null) {                                         // --artist_uniq_words
                LinkedHashSet<String> artistUnuqWords = argsHandler.getArtistUniqWords(arguments.getArtistName());
                artistUnuqWords.forEach(System.out::println);
            }

            else if(arguments.getSongName() != null) {                                             // --song_uniq_words
                LinkedHashSet<String> songUnuqWords = argsHandler.getSongUniqWords(arguments.getSongName());
                songUnuqWords.forEach(System.out::println);
            }

            else if(arguments.isArtistWordRating()) {                                           // --artist_word_rating
                LinkedHashSet<String> artistWordRating = argsHandler.getArtistWordRating();
                artistWordRating.forEach(System.out::println);
            }

            else if(arguments.getSimilarArtistName() != null) {                                    // --similar_artists
                LinkedHashMap<String, Integer> similarArtists = argsHandler.getSimilarArtists(
                                                                                    arguments.getSimilarArtistName());
                similarArtists.entrySet().forEach(System.out::println);
            }

            else if(arguments.getSimilarSongName() != null) {                                        // --similar_songs
                LinkedHashMap<String, Integer> similarSongs = argsHandler.getSimilarSongs(
                                                                                    arguments.getSimilarSongName());
                similarSongs.entrySet().forEach(System.out::println);
            }

            else if(arguments.isTrulyUniqWords()) {                                               // --truly_uniq_words
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
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }
    }
}
