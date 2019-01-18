package dbService;

import java.io.File;

import java.util.*;

import java.io.FileNotFoundException;
import java.io.IOException;

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

    public TreeSet<String> getNames(TypedStr typedStr) {
        CSVReader reader = null;

        try {
            reader = executor.getReader();
        } catch (FileNotFoundException e) {
            System.out.println("File \"" + dbFile.getName() + "\" does not exist.");
            System.exit(1);
        }

        TreeSet<String> data = new TreeSet<>();
        String[] nextLine;

        try(ProgressBar pb = new ProgressBar("Progress", 57650, ProgressBarStyle.ASCII)) {
            int count = 0;
            while ((nextLine = reader.readNext()) != null) {

                pb.step();

                if(nextLine[typedStr.getType()].toLowerCase().contains(typedStr.getSubstr().toLowerCase())) {
                    data.add(nextLine[typedStr.getType()]);
                }
            }

            if(data.isEmpty()) {
                switch (typedStr.getType()) {
                    case ARTIST: throw new DBException("Artists not found.");
                    case SONG:   throw new DBException("Songs not found.");
                }
            }

        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            System.out.println("\nUnable to read database file.");
            System.exit(1);
        } catch (DBException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        return data;
    }

    public Map<String, Integer> getUniqueWords(TypedStr typedStr) {
        CSVReader reader = null;

        try {
            reader = executor.getReader();
        } catch (FileNotFoundException e) {
            System.out.println("File \"" + dbFile.getName() + "\" does not exist.");
            System.exit(1);
        }

        Map<String, Integer> data = new TreeMap<>();
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

            if(data.isEmpty()) {
                switch (typedStr.getType()) {
                    case ARTIST: throw new DBException("Artist not found.");
                    case SONG:   throw new DBException("Song not found.");
                }
            }

        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            System.out.println("\nUnable to read database file.");
            System.exit(1);
        } catch (DBException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        return data;
    }

    public HashMap<String, Set<String>> getUniqueWords(int SOURCE) {
        CSVReader reader = null;

        try {
            reader = executor.getReader();
        } catch (FileNotFoundException e) {
            System.out.println("File \"" + dbFile.getName() + "\" does not exist.");
            System.exit(1);
        }

        HashMap<String, Set<String>> data = new HashMap<>();
        PorterStemmer stemmer = new PorterStemmer();
        String[] nextLine;

        try(ProgressBar pb = new ProgressBar("Progress", 57650, ProgressBarStyle.ASCII)) {

            int count = 0;
            while ((nextLine = reader.readNext()) != null) {

                pb.step();                                                             // progressBar step++

                if(data.containsKey(nextLine[SOURCE])) {
                    Set<String> uniqueWords = data.get(nextLine[SOURCE]);
                    String[] words = nextLine[LYRICS].toLowerCase()
                            .trim()
                            .replaceAll("[^a-zA-Z ]", "")
                            .split(" {1,}");

                    for(String word : words) {
                        uniqueWords.add(stemmer.getStem(word));
                    }

                    data.put(nextLine[SOURCE], uniqueWords);

                } else {

                    Set<String> uniqueWords = new HashSet<>();
                    String[] words = nextLine[LYRICS].toLowerCase()
                                                     .trim()
                                                     .replaceAll("[^a-zA-Z ]", "")
                                                     .split(" {1,}");

                    for(String word : words) {
                        uniqueWords.add(stemmer.getStem(word));
                    }

                    data.put(nextLine[SOURCE], uniqueWords);
                }
            }

            if(data.isEmpty()) {
                throw new DBException("Unknown error. Maybe something is wrong with the database file...");
            }

        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            System.out.println("\nUnable to read database file.");
            System.exit(1);
        } catch (DBException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        return data;
    }
}
