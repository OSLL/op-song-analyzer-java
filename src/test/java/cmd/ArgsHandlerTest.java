package cmd;

import dbService.DBException;
import dbService.DBService;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static cmd.ArgsHandler.ARTIST;
import static cmd.ArgsHandler.SONG;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ArgsHandlerTest {

    DBService dbService;
    ArgsHandler argsHandler;

    @Before
    public void setUp() {
        dbService = mock(DBService.class);
        argsHandler = new ArgsHandler(dbService);
    }

    @Test
    public void getListBands() throws DBException {
        Set<String> returnSet = new LinkedHashSet<>(Arrays.asList("B", "A", "C", "E", "D"));
        when(dbService.getNames(any())).thenReturn(returnSet);

        LinkedHashSet<String> resultSet = argsHandler.getListBands("");

        assertEquals("[A, B, C, D, E]", resultSet.toString());
    }


    @Test
    public void getListSongs() throws DBException {
        Set<String> returnSet = new LinkedHashSet<>(Arrays.asList("B", "A", "C", "E", "D"));
        when(dbService.getNames(any())).thenReturn(returnSet);

        LinkedHashSet<String> resultSet = argsHandler.getListSongs("");

        assertEquals("[A, B, C, D, E]", resultSet.toString());
    }

    @Test
    public void getArtistUniqWords() throws DBException {
        Map<String, Integer> returnMap = new LinkedHashMap<>();
        returnMap.put("C", 2);
        returnMap.put("A", 8);
        returnMap.put("B", 4);

        when(dbService.getUniqueWordsToFreq(any())).thenReturn(returnMap);

        LinkedHashSet<String> resultSet = argsHandler.getArtistUniqWords("");

        assertEquals("[A, B, C]", resultSet.toString());
    }

    @Test
    public void getSongUniqWords() throws DBException {
        Map<String, Integer> returnMap = new LinkedHashMap<>();
        returnMap.put("C", 2);
        returnMap.put("A", 8);
        returnMap.put("B", 4);

        when(dbService.getUniqueWordsToFreq(any())).thenReturn(returnMap);

        LinkedHashSet<String> resultSet = argsHandler.getSongUniqWords("");

        assertEquals("[A, B, C]", resultSet.toString());
    }

    @Test
    public void getArtistWordRating() throws DBException {
        Map<String, Set<String>> returnMap = new LinkedHashMap<>();
        Set<String> setForRetMap_1 = new LinkedHashSet<>(Arrays.asList("H"));
        Set<String> setForRetMap_2 = new LinkedHashSet<>(Arrays.asList("C", "E", "D"));
        Set<String> setForRetMap_3 = new LinkedHashSet<>(Arrays.asList("A", "G"));
        returnMap.put("C", setForRetMap_1);
        returnMap.put("A", setForRetMap_2);
        returnMap.put("B", setForRetMap_3);

        when(dbService.getNameToUniqueWords(ARTIST)).thenReturn(returnMap);

        LinkedHashSet<String> resultSet = argsHandler.getArtistWordRating();

        assertEquals("[A, B, C]", resultSet.toString());
    }

    @Test
    public void getSimilarArtists() throws DBException, ArgsHandlerException {
        Map<String, Set<String>> returnMap = new LinkedHashMap<>();
        Set<String> setForRetMap_1 = new LinkedHashSet<>(Arrays.asList("H"));
        Set<String> setForRetMap_2 = new LinkedHashSet<>(Arrays.asList("C", "E", "D"));
        Set<String> setForRetMap_3 = new LinkedHashSet<>(Arrays.asList("A", "H"));
        returnMap.put("C", setForRetMap_1);
        returnMap.put("A", setForRetMap_2);
        returnMap.put("B", setForRetMap_3);

        when(dbService.getNameToUniqueWords(ARTIST)).thenReturn(returnMap);

        LinkedHashMap<String, Integer> resultMap = argsHandler.getSimilarArtists("C");

        assertEquals("{B=1}", resultMap.toString());
    }

    @Test(expected = ArgsHandlerException.class)
    public void getSimilarArtists_NoMathInput() throws DBException, ArgsHandlerException {
        Map<String, Set<String>> returnMap = new LinkedHashMap<>();
        Set<String> setForRetMap_1 = new LinkedHashSet<>(Arrays.asList("H"));
        Set<String> setForRetMap_2 = new LinkedHashSet<>(Arrays.asList("C", "E", "D"));
        Set<String> setForRetMap_3 = new LinkedHashSet<>(Arrays.asList("A", "H"));
        returnMap.put("C", setForRetMap_1);
        returnMap.put("A", setForRetMap_2);
        returnMap.put("B", setForRetMap_3);

        when(dbService.getNameToUniqueWords(ARTIST)).thenReturn(returnMap);

        argsHandler.getSimilarArtists("D");
    }

    @Test
    public void getSimilarSongs() throws DBException, ArgsHandlerException {
        Map<String, Set<String>> returnMap = new LinkedHashMap<>();
        Set<String> setForRetMap_1 = new LinkedHashSet<>(Arrays.asList("H"));
        Set<String> setForRetMap_2 = new LinkedHashSet<>(Arrays.asList("C", "E", "D"));
        Set<String> setForRetMap_3 = new LinkedHashSet<>(Arrays.asList("A", "H"));
        returnMap.put("C", setForRetMap_1);
        returnMap.put("A", setForRetMap_2);
        returnMap.put("B", setForRetMap_3);

        when(dbService.getNameToUniqueWords(SONG)).thenReturn(returnMap);

        LinkedHashMap<String, Integer> resultMap = argsHandler.getSimilarSongs("C");

        assertEquals("{B=1}", resultMap.toString());
    }

    @Test(expected = ArgsHandlerException.class)
    public void getSimilarSongs_NoMathInput() throws DBException, ArgsHandlerException {
        Map<String, Set<String>> returnMap = new LinkedHashMap<>();
        Set<String> setForRetMap_1 = new LinkedHashSet<>(Arrays.asList("H"));
        Set<String> setForRetMap_2 = new LinkedHashSet<>(Arrays.asList("C", "E", "D"));
        Set<String> setForRetMap_3 = new LinkedHashSet<>(Arrays.asList("A", "H"));
        returnMap.put("C", setForRetMap_1);
        returnMap.put("A", setForRetMap_2);
        returnMap.put("B", setForRetMap_3);

        when(dbService.getNameToUniqueWords(SONG)).thenReturn(returnMap);

        argsHandler.getSimilarSongs("D");
    }

    @Test
    public void getTrulyUniqWords() throws DBException {
        Map<String, Set<String>> returnMap = new LinkedHashMap<>();
        Set<String> setForRetMap_1 = new LinkedHashSet<>(Arrays.asList("H"));
        Set<String> setForRetMap_2 = new LinkedHashSet<>(Arrays.asList("C", "E", "D"));
        Set<String> setForRetMap_3 = new LinkedHashSet<>(Arrays.asList("D", "H"));
        returnMap.put("C", setForRetMap_1);
        returnMap.put("A", setForRetMap_2);
        returnMap.put("B", setForRetMap_3);

        when(dbService.getNameToUniqueWords(SONG)).thenReturn(returnMap);

        Set<String> resultSet = argsHandler.getTrulyUniqWords();

        assertEquals("[C, E]", resultSet.toString());
    }
}