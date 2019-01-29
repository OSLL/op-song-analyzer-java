package dbService;

import com.opencsv.CSVReader;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class CSVExecutorTest {

    @Test
    public void getReader_NormalInputAndOk() throws IOException {
        String testFilePath = "src/test/resources/songdata.csv";

        CSVExecutor executor = new CSVExecutor(testFilePath);
        CSVReader reader = executor.getReader();

        String[] testEntry = reader.readNext();
        assertEquals("testArtistName", testEntry[0]);
        assertEquals("testSongName", testEntry[1]);
        assertEquals("testLyrics", testEntry[3]);
    }

    @Test(expected = ExecutorExeption.class)
    public void getReader_NotFoundInputAndExecutorExeption() throws IOException {
        String testFilePath = "src/test/resources/s.csv";

        CSVExecutor executor = new CSVExecutor(testFilePath);
        executor.getReader();
    }

    @Test(expected = ExecutorExeption.class)
    public void getReader_NoFileInputAndExecutorExeption() throws IOException {
        String testFilePath = "src/test/resources";

        CSVExecutor executor = new CSVExecutor(testFilePath);
        executor.getReader();
    }

    @Test(expected = ExecutorExeption.class)
    public void getReader_NoCSVInputAndExecutorExeption() throws IOException {
        String testFilePath = "pom.xml";

        CSVExecutor executor = new CSVExecutor(testFilePath);
        executor.getReader();
    }
}