package cmd;

import dbService.DBService;
import java.util.*;
import java.util.stream.Collectors;

public class ArgsHandler {

    DBService dbService;

    public static final int ARTIST = 0;
    public static final int SONG = 1;
    public static final int LYRICS = 3;

    public ArgsHandler(String filename) {
        dbService = new DBService(filename);
    }

    public void printAbout() {
        System.out.println("Lyrics analysis.\n" +
                "Author: Tsema G.S. 2019. In the course of \"Software Engineering\".\n" +
                "Saint-Petersburg Electrotechnical University ETU \"LETI\".");
    }

    public LinkedHashSet<String> getListBands(String artistSubstr) {                                   // --list_bands
        LinkedHashSet<String> listBands = new LinkedHashSet<>();
        dbService.getNames(new TypedStr(ARTIST, artistSubstr)).stream()
                                                              .sorted(Comparator.comparing(String::toLowerCase))
                                                              .forEach(listBands::add);
        return listBands;
    }

    public LinkedHashSet<String> getListSongs(String songSubstr) {                                     // --list_songs
        LinkedHashSet<String> listBands = new LinkedHashSet<>();
        dbService.getNames(new TypedStr(SONG, songSubstr)).stream()
                .sorted(Comparator.comparing(String::toLowerCase))
                .forEach(listBands::add);
        return listBands;
    }

    public Set<String> getArtistUniqWords(String artistName) {                                   // --artist_uniq_words
        LinkedHashSet<String> artistUWords = new LinkedHashSet<>();

        Map<String, Integer> uWordsToFreq = dbService.getUniqueWordsToFreq(new TypedStr(ARTIST, artistName));

        uWordsToFreq.entrySet().stream()
                               .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                               .forEach(entry -> artistUWords.add(entry.getKey()));

        return artistUWords;
    }

    public Set<String> getSongUniqWords(String songName) {                                       // --song_uniq_words
        LinkedHashSet<String> songUWords = new LinkedHashSet<>();

        Map<String, Integer> uWordsToFreq = dbService.getUniqueWordsToFreq(new TypedStr(SONG, songName));

        uWordsToFreq.entrySet().stream()
                               .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                               .forEach(entry -> songUWords.add(entry.getKey()));

        return songUWords;
    }

    public Set<String> getArtistWordRating() {                                                   // --artist_word_rating
        LinkedHashSet<String> artistWordRating = new LinkedHashSet<>();

        Map<String, Set<String>> artistsUWords = dbService.getUniqueWords(ARTIST);

        Map<String, Integer> artistUWordsNum = artistsUWords.entrySet()
                                                            .stream()
                                                            .collect(Collectors.toMap(Map.Entry::getKey,
                                                                                      entry -> entry.getValue().size()));

        artistUWordsNum.entrySet().stream()
                                  .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                                  .forEach(entry -> artistWordRating.add(entry.getKey()));

        return artistWordRating;
    }

    public Map<String, Integer> getSimilarArtists(String similarArtistName) {                    // --similar_artists 19.01
        HashMap<String, Set<String>> artistsUWords = dbService.getUniqueWords(ARTIST);

        if (!artistsUWords.containsKey(similarArtistName)) {
            System.out.println("Artist \"" + similarArtistName + "\" not found.");
            System.exit(1);
        }

        return artistsUWords.entrySet().stream()
                .peek(entry -> entry.getValue().retainAll(artistsUWords.get(similarArtistName)))
                .map(entry -> new AbstractMap.SimpleEntry<String, Integer>(entry.getKey(),
                        entry.getValue().size()))
                .filter(entry -> entry.getValue() != 0)
                .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                .skip(1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, Integer> getSimilarSongs(String similarSongName) {                        // --similar_songs
        HashMap<String, Set<String>> songsUWords = dbService.getUniqueWords(SONG);

        if (!songsUWords.containsKey(similarSongName)) {
            System.out.println("Song \"" + similarSongName + "\" not found.");
            System.exit(1);
        }

        return songsUWords.entrySet().stream()
                .peek(entry -> entry.getValue().retainAll(songsUWords.get(similarSongName)))
                .map(entry -> new AbstractMap.SimpleEntry<String, Integer>(entry.getKey(),
                        entry.getValue().size()))
                .filter(entry -> entry.getValue() != 0)
                .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                .skip(1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Set<String> getTrulyUniqWords() {                                                     // --truly_uniq_words
        HashMap<String, Set<String>> songsUWords = dbService.getUniqueWords(SONG);

        HashMap<String, Integer> result = new HashMap<>();

        for (Map.Entry<String, Set<String>> entry : songsUWords.entrySet()) {
            for (String value : entry.getValue()) {
                if (result.containsKey(value)) result.put(value, result.get(value) + 1);
                else result.put(value, 0);
            }
        }

        return result.entrySet().stream()
                .filter(entry -> entry.getValue() == 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).keySet();
    }
}