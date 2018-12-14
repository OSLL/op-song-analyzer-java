package dbService;

import cmd.TypedStr;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DBService {

    private final File dbFile;
    private Executor executor;

    public DBService(String dbFilePath) {
        dbFile = new File(dbFilePath);
        checkFile();
        executor = new Executor(dbFile);
    }

    private void checkFile() {
        if(!dbFile.exists()) {
            System.out.println("File " + dbFile.getName() + " does not exist.");
            System.exit(1);
        }
        if(!dbFile.getName().endsWith(".csv")) {
            System.out.println("This is not csv-file.");
            System.exit(1);
        }
        if(!dbFile.isFile() && !dbFile.canRead()) {
            System.out.println("Unable to read file.");
            System.exit(1);
        }
    }

    public Set<String> get(TypedStr typedStr) {
        final CSVReader reader = executor.getReader();

        Set<String> artists = new HashSet<>();
        String[] nextLine;

            try {
                while ((nextLine = reader.readNext()) != null) {
                    if(nextLine[typedStr.getType()].contains(typedStr.getSubstr())) artists.add(nextLine[typedStr.getType()]);
                }
            } catch (IOException ignore) {}

        return artists;
    }
}
