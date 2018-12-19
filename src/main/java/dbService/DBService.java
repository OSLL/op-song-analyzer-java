package dbService;

import cmd.TypedStr;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

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
            System.out.println("Database file " + dbFile.getName() + " does not exist.");
            System.exit(1);
        }
        if(!dbFile.getName().endsWith(".csv")) {
            System.out.println("Database file must have *.csv-extension.");
            System.exit(1);
        }
        if(!dbFile.isFile() && !dbFile.canRead()) {
            System.out.println("Unable to read database file.");
            System.exit(1);
        }
    }

    public List<String> get(TypedStr typedStr) {
        CSVReader reader = null;

        try {
            reader = executor.getReader();
        } catch (FileNotFoundException e) {
            System.out.println("File " + dbFile.getName() + " does not exist.");
            System.exit(1);
        }

        Set<String> data = new HashSet<>();
        String[] nextLine;

        try {
            int count = 0;
            while ((nextLine = reader.readNext()) != null) {

                System.out.print('\r');
                System.out.print((char)27 + "[4m" +
                                 (char)27 + "[92m" +
                                  count++ + " lines are read!");

                if(nextLine[typedStr.getType()].toLowerCase().contains(typedStr.getSubstr().toLowerCase())) {
                    data.add(nextLine[typedStr.getType()]);
                }
            }
            System.out.print((char)27 + "[89m" + "\n");
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Unable to read database file.");
            System.exit(1);
        }

        List sortedData = new ArrayList(data);
        Collections.sort(sortedData);

        return sortedData;
    }
}
