package dbService;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CSVExecutor {

    private String filePath;

    public CSVExecutor(String filePath) {
        this.filePath = filePath;
    }

    public File checkFile(String filePath) throws ExecutorExeption {

        File dBFile = new File(filePath);

        if(!dBFile.exists()) {
            throw new ExecutorExeption("Database file " + dBFile.getName() + " does not exist.");
        }
        if(!dBFile.getName().endsWith(".csv")) {
            throw new ExecutorExeption("Database file must have *.csv-extension.");
        }
        if(!dBFile.isFile() && !dBFile.canRead()) {
            throw new ExecutorExeption("Unable to read database file.");
        }

        return dBFile;
    }

    public CSVReader getReader() {
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

        } catch (FileNotFoundException | ExecutorExeption e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        return reader;
    }
}

