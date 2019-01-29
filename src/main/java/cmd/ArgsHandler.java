package cmd;

import dbService.DBException;
import dbService.DBService;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ArgsHandler {
    private static Logger log = Logger.getLogger(DBService.class.getName());
    private DBService dbService;

    public static final int ARTIST = 0;
    public static final int SONG = 1;
    public static final int LYRICS = 3;

    public ArgsHandler(DBService dbService) {
        this.dbService = dbService;
    }

    public void printAbout() {
        System.out.println("Lyrics analysis.\n" +
                "Author: Tsema G.S. 2019. In the course of \"Software Engineering\".\n" +
                "Saint-Petersburg Electrotechnical University ETU \"LETI\".");
    }

    public LinkedHashSet<String> getListBands(String artistSubstr) throws DBException {
        LinkedHashSet<String> listBands = new LinkedHashSet<>();
        dbService.getNames(new TypedStr(ARTIST, artistSubstr)).stream()
                                                              .sorted(Comparator.comparing(String::toLowerCase))
                                                              .forEach(listBands::add);
        return listBands;
    }

    public LinkedHashSet<String> getListSongs(String songSubstr) throws DBException {
        LinkedHashSet<String> listBands = new LinkedHashSet<>();
        dbService.getNames(new TypedStr(SONG, songSubstr)).stream()
                                                          .sorted(Comparator.comparing(String::toLowerCase))
                                                          .forEach(listBands::add);
        return listBands;
    }

    public LinkedHashSet<String> getArtistUniqWords(String artistName) throws DBException {
        LinkedHashSet<String> artistUWords = new LinkedHashSet<>();

        dbService.getUniqueWordsToFreq(new TypedStr(ARTIST, artistName))
                 .entrySet()
                 .stream()
                 .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                 .forEach(entry -> artistUWords.add(entry.getKey()));

        return artistUWords;
    }

    public LinkedHashSet<String> getSongUniqWords(String songName) throws DBException {
        LinkedHashSet<String> songUWords = new LinkedHashSet<>();

        dbService.getUniqueWordsToFreq(new TypedStr(SONG, songName))
                 .entrySet()
                 .stream()
                 .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                 .forEach(entry -> songUWords.add(entry.getKey()));

        return songUWords;
    }

    public LinkedHashSet<String> getArtistWordRating() throws DBException {
        LinkedHashSet<String> artistWordRating = new LinkedHashSet<>();

        dbService.getNameToUniqueWords(ARTIST)
                 .entrySet()
                 .stream()
                 .map(entry -> new AbstractMap.SimpleEntry<String, Integer>(entry.getKey(),
                                                                            entry.getValue().size()))
                 .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                 .forEach(entry -> artistWordRating.add(entry.getKey()));

        return artistWordRating;
    }

    public LinkedHashMap<String, Integer> getSimilarArtists(String similarArtistName) throws DBException,
                                                                                             ArgsHandlerException {
        LinkedHashMap<String, Integer> artistsToCommonWordsNum = new LinkedHashMap<>();
        Map<String, Set<String>> artistToUWords = dbService.getNameToUniqueWords(ARTIST);

        if (!artistToUWords.containsKey(similarArtistName)) {
            log.info("Artist \"" + similarArtistName + "\" not found.");
            throw new ArgsHandlerException("Artist \"" + similarArtistName + "\" not found.");
        }

        artistToUWords.entrySet().stream()
                                 .peek(entry -> entry.getValue().retainAll(artistToUWords.get(similarArtistName)))
                                 .map(entry -> new AbstractMap.SimpleEntry<String, Integer>(entry.getKey(),
                                      entry.getValue().size()))
                                 .filter(entry -> entry.getValue() != 0)
                                 .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                                 .skip(1)
                                 .forEach(entry -> artistsToCommonWordsNum.put(entry.getKey(), entry.getValue()));

        return artistsToCommonWordsNum;
    }

    public LinkedHashMap<String, Integer> getSimilarSongs(String similarSongName) throws DBException,
                                                                                         ArgsHandlerException {
        LinkedHashMap<String, Integer> songToCommonWordsNum = new LinkedHashMap<>();
        Map<String, Set<String>> songToUWords = dbService.getNameToUniqueWords(SONG);

        if (!songToUWords.containsKey(similarSongName)) {
            log.info("Song \"" + similarSongName + "\" not found.");
            throw new ArgsHandlerException("Song \"" + similarSongName + "\" not found.");
        }

        songToUWords.entrySet().stream()
                    .peek(entry -> entry.getValue().retainAll(songToUWords.get(similarSongName)))
                    .map(entry -> new AbstractMap.SimpleEntry<String, Integer>(entry.getKey(),
                                                                               entry.getValue().size()))
                    .filter(entry -> entry.getValue() != 0)
                    .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                    .skip(1)
                    .forEach(entry -> songToCommonWordsNum.put(entry.getKey(), entry.getValue()));

        return songToCommonWordsNum;
    }

    public Set<String> getTrulyUniqWords() throws DBException {
        Map<String, Integer> songUWordsToNum = new HashMap<>();

        dbService.getNameToUniqueWords(SONG).entrySet().forEach(entry -> {
                                                     for(String word : entry.getValue()) {
                                                         if(songUWordsToNum.containsKey(word))
                                                             songUWordsToNum.put(word, songUWordsToNum.get(word) + 1);
                                                         else songUWordsToNum.put(word, 0);
                                                     }
                                                 });

        return songUWordsToNum.entrySet().stream()
                                         .filter(entry -> entry.getValue() == 0)
                                         .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).keySet();
    }
}
