package dbService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.opencsv.CSVReader;

import cmd.TypedStr;
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

    private int printStatus(int i) {                                                // Вывод строки состояния
        int count = i;

        System.out.print('\r');
        System.out.print((char)27 + "[4m" +
                (char)27 + "[92m" +
                ++count + " entries are read!");

        return count;
    }

    public Map<String, Integer> getNames(TypedStr typedStr) {
        CSVReader reader = null;

        try {
            reader = executor.getReader();
        } catch (FileNotFoundException e) {
            System.out.println("File " + dbFile.getName() + " does not exist.");
            System.exit(1);
        }

        Map<String, Integer> data = new HashMap<>();                                // Результат
        String[] nextLine;

        try {
            int count = 0;
            while ((nextLine = reader.readNext()) != null) {

                count = printStatus(count);                                         // Желтая строка

                if(nextLine[typedStr.getType()].toLowerCase().contains(typedStr.getSubstr().toLowerCase())) {
                    if(!data.containsKey(nextLine[typedStr.getType()])) {
                        data.put(nextLine[typedStr.getType()], 1);
                    } else {
                        int i = data.get(nextLine[typedStr.getType()]);
                        data.put(nextLine[typedStr.getType()], ++i);
                    }
                }
            }
            System.out.print((char)27 + "[89m" + "\n");

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
            System.out.print((char)27 + "[89m" + "\n");
            System.out.println("Unable to read database file.");
            System.exit(1);
        }

        return data;
    }

    public Map<String, Integer> getWords(TypedStr typedStr) {
        CSVReader reader = null;

        try {
            reader = executor.getReader();
        } catch (FileNotFoundException e) {
            System.out.println("File " + dbFile.getName() + " does not exist.");
            System.exit(1);
        }

        Map<String, Integer> data = new HashMap<>();                                // Результат
        PorterStemmer stemmer = new PorterStemmer();
        String[] nextLine;                                                          // Для перебора строк бд

        try {
            int count = 0;
            while ((nextLine = reader.readNext()) != null) {

                count = printStatus(count);                                         // Желтая строка

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
            System.out.print((char)27 + "[89m" + "\n");

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
            System.out.print((char)27 + "[89m" + "\n");
            System.out.println("Unable to read database file.");
            System.exit(1);
        }

        return data;
    }
}
