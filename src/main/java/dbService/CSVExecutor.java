package dbService;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVExecutor {

    private static Logger log = Logger.getLogger(DBService.class.getName());

    private String filePath;

    public CSVExecutor(String filePath) {
        this.filePath = filePath;
    }

    public CSVReader getReader() throws ExecutorExeption {
        CSVReader reader = null;

        try {
            File dBFile = checkFile(filePath);

            final CSVParser parser = new CSVParserBuilder().withSeparator(',')
                                                           .withIgnoreQuotations(false)
                                                           .withQuoteChar('"')
                                                           .build();

            FileReader fileReader = new FileReader(dBFile.getAbsolutePath());

            reader = new CSVReaderBuilder(fileReader).withCSVParser(parser)
                                                     .withSkipLines(1)
                                                     .build();

        } catch (FileNotFoundException e) {
            log.log(Level.SEVERE, "Exception: ", e);
            throw new ExecutorExeption(e.getMessage());
        }

        return reader;
    }

    private File checkFile(String filePath) throws ExecutorExeption {

        File dBFile = new File(filePath);

        if(!dBFile.exists()) {
            log.log(Level.SEVERE, "Database file \"" + dBFile.getName() + "\" does not exist.");
            throw new ExecutorExeption("Database file \"" + dBFile.getName() + "\" does not exist.");
        }
        if(!dBFile.isFile()) {
            log.log(Level.SEVERE, "\"" + dBFile.getName() + "\" is not a file.");
            throw new ExecutorExeption("\"" + dBFile.getName() + "\"  is not a file.");
        }
        if(!dBFile.getName().endsWith(".csv")) {
            log.log(Level.SEVERE, "Database file \"" + dBFile.getName() + "\" must have *.csv-extension.");
            throw new ExecutorExeption("Database file \"" + dBFile.getName() + "\" must have *.csv-extension.");
        }
        if(!dBFile.canRead()) {
            log.log(Level.SEVERE, "Unable to read database file \"" + dBFile.getName() + "\".");
            throw new ExecutorExeption("Unable to read database file \"" + dBFile.getName() + "\".");
        }

        return dBFile;
    }
}
