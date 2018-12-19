package dbService;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Executor  {

    private File dbFile;
    private CSVReader reader;

    public Executor(File dbFile) {
        this.dbFile = dbFile;
    }

    CSVReader getReader() throws FileNotFoundException {

        final CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(false)
                .withQuoteChar('"')
                .build();

        return reader = new CSVReaderBuilder(new FileReader(dbFile.getAbsolutePath()))
                .withCSVParser(parser)
                .withSkipLines(1)
                .build();
    }
}
