package dbService;

import cmd.TypedStr;
import com.opencsv.CSVReader;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static cmd.ArgsHandler.ARTIST;
import static cmd.ArgsHandler.SONG;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DBServiceTest {

    @Test
    public void getNames_ARTISTAndEmptyString_NormalInputAndOk() throws IOException, DBException {

        String[] ret0 = {"A", "", "", ""};
        String[] ret1 = {"B", "", "", ""};
        String[] ret2 = {"B", "", "", ""};
        String[] ret3 = {"C", "", "", ""};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        Set<String> resultSet = dbService.getNames(new TypedStr(ARTIST, ""));

        Set<String> testSet = new HashSet<>();
        testSet.add("A");
        testSet.add("B");
        testSet.add("C");

        assertEquals(testSet, resultSet);
    }

    @Test(expected = DBException.class)
    public void getNames_ARTISTAndEmptyString_EmptyInputAndDBException() throws IOException, DBException {

        String[] ret0 = {"", "", "", ""};
        String[] ret1 = {"", "", "", ""};
        String[] ret2 = {"", "", "", ""};
        String[] ret3 = {"", "", "", ""};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        dbService.getNames(new TypedStr(ARTIST, ""));
    }

    @Test(expected = DBException.class)
    public void getNames_ARTISTAndEmptyString_NoInputAndDBException() throws IOException, DBException {

        String[] ret0 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0);

        DBService dbService = new DBService(reader);

        dbService.getNames(new TypedStr(ARTIST, ""));
    }

    @Test(expected = DBException.class)
    public void getNames_ARTISTAndEmptyString_CrashDBInputAndDBException() throws IOException, DBException {

        String[] ret0 = {};
        String[] ret1 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1);

        DBService dbService = new DBService(reader);

        dbService.getNames(new TypedStr(ARTIST, ""));
    }

    @Test(expected = DBException.class)
    public void getNames_ARTISTAndNoEmptyString_NoMatchInputAndDBException() throws IOException, DBException {

        String[] ret0 = {"A", "", "", ""};
        String[] ret1 = {"B", "", "", ""};
        String[] ret2 = {"B", "", "", ""};
        String[] ret3 = {"C", "", "", ""};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        Set<String> resultSet = dbService.getNames(new TypedStr(ARTIST, "X"));

        System.out.println(resultSet);
    }

    @Test
    public void getNames_ARTISTAndNoEmptyString_MatchInputAndDBException() throws IOException, DBException {

        String[] ret0 = {"A", "", "", ""};
        String[] ret1 = {"B", "", "", ""};
        String[] ret2 = {"B", "", "", ""};
        String[] ret3 = {"C", "", "", ""};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        Set<String> resultSet = dbService.getNames(new TypedStr(ARTIST, "A"));

        Set<String> testSet = new HashSet<>();
        testSet.add("A");

        assertEquals(testSet, resultSet);
    }

    @Test
    public void getNames_SONGAndEmptyString_NormalInputAndOk() throws IOException, DBException {

        String[] ret0 = {"", "A", "", ""};
        String[] ret1 = {"", "B", "", ""};
        String[] ret2 = {"", "B", "", ""};
        String[] ret3 = {"", "C", "", ""};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        Set<String> resultSet = dbService.getNames(new TypedStr(SONG, ""));

        Set<String> testSet = new HashSet<>();
        testSet.add("A");
        testSet.add("B");
        testSet.add("C");

        assertEquals(testSet, resultSet);
    }

    @Test(expected = DBException.class)
    public void getNames_SONGAndEmptyString_EmptyInputAndDBException() throws IOException, DBException {

        String[] ret0 = {"", "", "", ""};
        String[] ret1 = {"", "", "", ""};
        String[] ret2 = {"", "", "", ""};
        String[] ret3 = {"", "", "", ""};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        dbService.getNames(new TypedStr(SONG, ""));
    }

    @Test(expected = DBException.class)
    public void getNames_SONGAndEmptyString_NoInputAndDBException() throws IOException, DBException {

        String[] ret0 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0);

        DBService dbService = new DBService(reader);

        dbService.getNames(new TypedStr(SONG, ""));
    }

    @Test(expected = DBException.class)
    public void getNames_SONGAndEmptyString_CrashDBInputAndDBException() throws IOException, DBException {

        String[] ret0 = {};
        String[] ret1 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                .thenReturn(ret1);

        DBService dbService = new DBService(reader);

        dbService.getNames(new TypedStr(SONG, ""));
    }

    @Test(expected = DBException.class)
    public void getNames_SONGAndNoEmptyString_NoMatchInputAndDBException() throws IOException, DBException {

        String[] ret0 = {"", "A", "", ""};
        String[] ret1 = {"", "B", "", ""};
        String[] ret2 = {"", "B", "", ""};
        String[] ret3 = {"", "C", "", ""};;
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        dbService.getNames(new TypedStr(SONG, "X"));
    }

    @Test
    public void getNames_SONGAndNoEmptyString_MatchInputAndOk() throws IOException, DBException {

        String[] ret0 = {"", "A", "", ""};
        String[] ret1 = {"", "B", "", ""};
        String[] ret2 = {"", "B", "", ""};
        String[] ret3 = {"", "C", "", ""};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        Set<String> resultSet = dbService.getNames(new TypedStr(SONG, "A"));

        Set<String> testSet = new HashSet<>();
        testSet.add("A");

        assertEquals(testSet, resultSet);
    }

    @Test(expected = DBException.class)
    public void getUniqueWordsToFreq_ARTISTAndEmptyString_NormalInputAndDEException() throws IOException, DBException {
        String[] ret0 = {"A", "", "", "pa pa"};
        String[] ret1 = {"B", "", "", "ta ta"};
        String[] ret2 = {"B", "", "", "ku ku"};
        String[] ret3 = {"C", "", "", "da da"};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        dbService.getUniqueWordsToFreq(new TypedStr(ARTIST, ""));
    }

    @Test(expected = DBException.class)
    public void getUniqueWordsToFreq_ARTISTAndEmptyString_EmptyInputAndDEException() throws IOException, DBException {
        String[] ret0 = {"", "", "", ""};
        String[] ret1 = {"", "", "", ""};
        String[] ret2 = {"", "", "", ""};
        String[] ret3 = {"", "", "", ""};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        dbService.getUniqueWordsToFreq(new TypedStr(ARTIST, ""));
    }

    @Test(expected = DBException.class)
    public void getUniqueWordsToFreq_ARTISTAndEmptyString_NoInputAndDEException() throws IOException, DBException {
        String[] ret0 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0);

        DBService dbService = new DBService(reader);

        dbService.getUniqueWordsToFreq(new TypedStr(ARTIST, ""));
    }

    @Test(expected = DBException.class)
    public void getUniqueWordsToFreq_ARTISTAndNoEmptyString_CrashDBInputInputAndDEException() throws IOException, DBException {
        String[] ret0 = {};
        String[] ret1 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1);

        DBService dbService = new DBService(reader);

        dbService.getUniqueWordsToFreq(new TypedStr(ARTIST, "D"));
    }

    @Test(expected = DBException.class)
    public void getUniqueWordsToFreq_ARTISTAndNoEmptyString_NoMatchInputAndDEException() throws IOException, DBException {
        String[] ret0 = {"A", "", "", "pa ta"};
        String[] ret1 = {"B", "", "", "ta ku"};
        String[] ret2 = {"B", "", "", "ku da"};
        String[] ret3 = {"C", "", "", "da pa"};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        dbService.getUniqueWordsToFreq(new TypedStr(ARTIST, "D"));
    }

    @Test
    public void getUniqueWordsToFreq_ARTISTAndNoEmptyString_MatchInputAndOk() throws IOException, DBException {
        String[] ret0 = {"A", "", "", "pa ta"};
        String[] ret1 = {"B", "", "", "ta ku"};
        String[] ret2 = {"B", "", "", "ku da"};
        String[] ret3 = {"C", "", "", "da pa"};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                .thenReturn(ret1)
                .thenReturn(ret2)
                .thenReturn(ret3)
                .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        Map<String, Integer> resultMap = dbService.getUniqueWordsToFreq(new TypedStr(ARTIST, "B"));

        Map<String, Integer> testMap = new HashMap<>();
        testMap.put("ku", 2);
        testMap.put("ta", 1);
        testMap.put("da", 1);

        assertEquals(testMap, resultMap);
    }

    @Test(expected = DBException.class)
    public void getUniqueWordsToFreq_SONGAndEmptyString_NormalInputAndDEException() throws IOException, DBException {
        String[] ret0 = {"", "A", "", "pa pa"};
        String[] ret1 = {"", "B", "", "ta ta"};
        String[] ret2 = {"", "B", "", "ku ku"};
        String[] ret3 = {"", "C", "", "da da"};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        dbService.getUniqueWordsToFreq(new TypedStr(SONG, ""));
    }

    @Test(expected = DBException.class)
    public void getUniqueWordsToFreq_SONGTAndEmptyString_EmptyInputAndDEException() throws IOException, DBException {
        String[] ret0 = {"", "", "", ""};
        String[] ret1 = {"", "", "", ""};
        String[] ret2 = {"", "", "", ""};
        String[] ret3 = {"", "", "", ""};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        dbService.getUniqueWordsToFreq(new TypedStr(SONG, ""));
    }

    @Test(expected = DBException.class)
    public void getUniqueWordsToFreq_SONGAndEmptyString_NoInputAndDEException() throws IOException, DBException {
        String[] ret0 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0);

        DBService dbService = new DBService(reader);

        dbService.getUniqueWordsToFreq(new TypedStr(SONG, ""));
    }

    @Test(expected = DBException.class)
    public void getUniqueWordsToFreq_SONGAndNoEmptyString_CrashDBInputInputAndDEException() throws IOException, DBException {
        String[] ret0 = {};
        String[] ret1 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1);

        DBService dbService = new DBService(reader);

        dbService.getUniqueWordsToFreq(new TypedStr(SONG, "D"));
    }

    @Test(expected = DBException.class)
    public void getUniqueWordsToFreq_SONGAndNoEmptyString_NoMatchInputAndDEException() throws IOException, DBException {
        String[] ret0 = {"", "A", "", "pa ta"};
        String[] ret1 = {"", "B", "", "ta ku"};
        String[] ret2 = {"", "B", "", "ku da"};
        String[] ret3 = {"", "C", "", "da pa"};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        dbService.getUniqueWordsToFreq(new TypedStr(SONG, "D"));
    }

    @Test
    public void getUniqueWordsToFreq_SONGAndNoEmptyString_MatchInputAndOk() throws IOException, DBException {
        String[] ret0 = {"", "A", "", "pa ta"};
        String[] ret1 = {"", "B", "", "ta ku"};
        String[] ret2 = {"", "B", "", "ku da"};
        String[] ret3 = {"", "C", "", "da pa"};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        Map<String, Integer> resultMap = dbService.getUniqueWordsToFreq(new TypedStr(SONG, "B"));

        Map<String, Integer> testMap = new HashMap<>();
        testMap.put("ku", 2);
        testMap.put("ta", 1);
        testMap.put("da", 1);

        assertEquals(testMap, resultMap);
    }

    @Test
    public void getNameToUniqueWords_ARTIST_NormalInputAndOk() throws IOException, DBException {
        String[] ret0 = {"A", "", "", "pa ta pa"};
        String[] ret1 = {"B", "", "", "ta ku ta"};
        String[] ret2 = {"B", "", "", "ku da ku"};
        String[] ret3 = {"C", "", "", "da pa da"};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        Map<String, Set<String>> resultMap = dbService.getNameToUniqueWords(ARTIST);

        Map<String, Set<String>> testMap = new HashMap<>();
        testMap.put("A", new HashSet<>(Arrays.asList("pa", "ta")));
        testMap.put("B", new HashSet<>(Arrays.asList("ku", "ta", "da")));
        testMap.put("C", new HashSet<>(Arrays.asList("pa", "da")));

        assertEquals(testMap, resultMap);
    }

    @Test(expected = DBException.class)
    public void getNameToUniqueWords_ARTIST_EmptyInputAndDBException() throws IOException, DBException {
        String[] ret0 = {"", "", "", ""};
        String[] ret1 = {"", "", "", ""};
        String[] ret2 = {"", "", "", ""};
        String[] ret3 = {"", "", "", ""};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        dbService.getNameToUniqueWords(ARTIST);
    }

    @Test(expected = DBException.class)
    public void getNameToUniqueWords_ARTIST_EmptyLyricsInputAndDBException() throws IOException, DBException {
        String[] ret0 = {"A", "", "", ""};
        String[] ret1 = {"B", "", "", ""};
        String[] ret2 = {"B", "", "", ""};
        String[] ret3 = {"D", "", "", ""};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        dbService.getNameToUniqueWords(ARTIST);
    }

    @Test(expected = DBException.class)
    public void getNameToUniqueWords_ARTIST_NoInputAndDBException() throws IOException, DBException {

        String[] ret0 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0);

        DBService dbService = new DBService(reader);

        dbService.getNameToUniqueWords(ARTIST);
    }

    @Test(expected = DBException.class)
    public void getNameToUniqueWords_ARTIST_CrashDBInputAndDBException() throws IOException, DBException {

        String[] ret0 = {};
        String[] ret1 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1);

        DBService dbService = new DBService(reader);

        dbService.getNameToUniqueWords(ARTIST);
    }

    @Test
    public void getNameToUniqueWords_SONG_NormalInputAndOk() throws IOException, DBException {
        String[] ret0 = {"", "A", "", "pa ta pa"};
        String[] ret1 = {"", "B", "", "ta ku ta"};
        String[] ret2 = {"", "B", "", "ku da ku"};
        String[] ret3 = {"", "C", "", "da pa da"};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        Map<String, Set<String>> resultMap = dbService.getNameToUniqueWords(SONG);

        Map<String, Set<String>> testMap = new HashMap<>();
        testMap.put("A", new HashSet<>(Arrays.asList("pa", "ta")));
        testMap.put("B", new HashSet<>(Arrays.asList("ku", "ta", "da")));
        testMap.put("C", new HashSet<>(Arrays.asList("pa", "da")));

        assertEquals(testMap, resultMap);
    }

    @Test(expected = DBException.class)
    public void getNameToUniqueWords_SONG_EmptyInputAndDBException() throws IOException, DBException {
        String[] ret0 = {"", "", "", ""};
        String[] ret1 = {"", "", "", ""};
        String[] ret2 = {"", "", "", ""};
        String[] ret3 = {"", "", "", ""};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        dbService.getNameToUniqueWords(SONG);
    }

    @Test(expected = DBException.class)
    public void getNameToUniqueWords_SONG_EmptyLyricsInputAndDBException() throws IOException, DBException {
        String[] ret0 = {"", "A", "", ""};
        String[] ret1 = {"", "B", "", ""};
        String[] ret2 = {"", "B", "", ""};
        String[] ret3 = {"", "C", "", ""};
        String[] ret4 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1)
                               .thenReturn(ret2)
                               .thenReturn(ret3)
                               .thenReturn(ret4);

        DBService dbService = new DBService(reader);

        dbService.getNameToUniqueWords(SONG);
    }

    @Test(expected = DBException.class)
    public void getNameToUniqueWords_SONG_NoInputAndDBException() throws IOException, DBException {

        String[] ret0 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0);

        DBService dbService = new DBService(reader);

        dbService.getNameToUniqueWords(SONG);
    }

    @Test(expected = DBException.class)
    public void getNameToUniqueWords_SONG_CrashDBInputAndDBException() throws IOException, DBException {

        String[] ret0 = {};
        String[] ret1 = null;

        CSVReader reader = mock(CSVReader.class);
        when(reader.readNext()).thenReturn(ret0)
                               .thenReturn(ret1);

        DBService dbService = new DBService(reader);

        dbService.getNameToUniqueWords(SONG);
    }
}