package dbService;

import java.io.File;
import java.util.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;

import cmd.TypedStr;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;
import stemmer.PorterStemmer;

import static cmd.ArgsHandler.ARTIST;
import static cmd.ArgsHandler.LYRICS;
import static cmd.ArgsHandler.SONG;

public class DBService {

    private final File dbFile;
    private Executor executor;

    public DBService(String dbFilePath) {
        dbFile = new File(dbFilePath);
        checkFile();
        executor = new Executor(dbFile);
    }

    private void checkFile() {
        if(!dbFile.exists()) {                                                      // Файл существует
            System.out.println("Database file " + dbFile.getName() + " does not exist.");
            System.exit(1);
        }
        if(!dbFile.getName().endsWith(".csv")) {                                    // Расширение *.csv
            System.out.println("Database file must have *.csv-extension.");
            System.exit(1);
        }
        if(!dbFile.isFile() && !dbFile.canRead()) {                                 // Можно прочесть
            System.out.println("Unable to read database file.");
            System.exit(1);
        }
    }

    public Map<String, Integer> getNames(TypedStr typedStr) {
        CSVReader reader = null;

        try {
            reader = executor.getReader();
        } catch (FileNotFoundException e) {
            System.out.println("File " + dbFile.getName() + " does not exist.");
            System.exit(1);
        }

        Map<String, Integer> data = new HashMap<>();
        String[] nextLine;

        try(ProgressBar pb = new ProgressBar("Progress", 57650, ProgressBarStyle.ASCII)) {
            int count = 0;
            while ((nextLine = reader.readNext()) != null) {

                pb.step();

                if(nextLine[typedStr.getType()].toLowerCase().contains(typedStr.getSubstr().toLowerCase())) {
                    if(!data.containsKey(nextLine[typedStr.getType()])) {
                        data.put(nextLine[typedStr.getType()], 1);
                    } else {
                        int i = data.get(nextLine[typedStr.getType()]);
                        data.put(nextLine[typedStr.getType()], ++i);
                    }
                }
            }

            if(data.isEmpty()) {
                switch (typedStr.getType()) {
                    case ARTIST: {
                        System.out.println("Artists not found.");
                        System.exit(1);
                    }
                    case SONG: {
                        System.out.println("Songs not found.");
                        System.exit(1);
                    }
                }
            }

        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Unable to read database file.");
            System.exit(1);
        }

        return data;
    }

    public Map<String, Integer> getUniqWords(TypedStr typedStr) {
        CSVReader reader = null;

        try {
            reader = executor.getReader();
        } catch (FileNotFoundException e) {
            System.out.println("File " + dbFile.getName() + " does not exist.");
            System.exit(1);
        }

        Map<String, Integer> data = new HashMap<>();
        PorterStemmer stemmer = new PorterStemmer();
        String[] nextLine;

        try(ProgressBar pb = new ProgressBar("Progress", 57650, ProgressBarStyle.ASCII)) {
            int count = 0;
            while ((nextLine = reader.readNext()) != null) {

                pb.step();

                if(nextLine[typedStr.getType()].equals(typedStr.getSubstr())) {
                    String[] words = nextLine[LYRICS].toLowerCase()
                                                     .trim()
                                                     .replaceAll("[^a-zA-Z ]", "")
                                                     .split(" {1,}");
                    for(String word : words) {
                        String stem = stemmer.getStem(word);
                        if(!data.containsKey(stem)) data.put(stem, 1);
                        else data.put(stem, data.get(stem) + 1);
                    }
                }

            }

            System.out.print('\r');

            if(data.isEmpty()) {
                switch (typedStr.getType()) {
                    case ARTIST: {
                        System.out.println("Artist not found.");
                        System.exit(1);
                    }
                    case SONG: {
                        System.out.println("Song not found.");
                        System.exit(1);
                    }
                }
            }

        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Unable to read database file.");
            System.exit(1);
        }

        return data;
    }

    public Map<String, Set<String>> getArtistsUniqueWords() {
        CSVReader reader = null;

        try {
            reader = executor.getReader();
        } catch (FileNotFoundException e) {
            System.out.println("File " + dbFile.getName() + " does not exist.");
            System.exit(1);
        }

        Map<String, Set<String>> data = new HashMap<>();
        PorterStemmer stemmer = new PorterStemmer();
        String[] nextLine;

        try(ProgressBar pb = new ProgressBar("Progress", 57650, ProgressBarStyle.ASCII)) {

            int count = 0;
            while ((nextLine = reader.readNext()) != null) {

                pb.step();

                if(data.containsKey(nextLine[ARTIST])) {
                    Set<String> uniqueWords = data.get(nextLine[ARTIST]);
                    String[] words = nextLine[LYRICS].toLowerCase()
                            .trim()
                            .replaceAll("[^a-zA-Z ]", "")
                            .split(" {1,}");

                    for(String word : words) {
                        uniqueWords.add(stemmer.getStem(word));
                    }

                    data.put(nextLine[ARTIST], uniqueWords);

                } else {

                    Set<String> uniqueWords = new HashSet<>();
                    String[] words = nextLine[LYRICS].toLowerCase()
                                                     .trim()
                                                     .replaceAll("[^a-zA-Z ]", "")
                                                     .split(" {1,}");

                    for(String word : words) {
                        uniqueWords.add(stemmer.getStem(word));
                    }

                    data.put(nextLine[ARTIST], uniqueWords);
                }
            }

            if(data.isEmpty()) {
                System.out.println("Artists and songs not found.");
                System.exit(1);
            }

        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Unable to read database file.");
            System.exit(1);
        }

        return data;
    }
}
