package cmd;

import dbService.DBService;
import picocli.CommandLine;

import java.util.*;
import java.util.stream.Collectors;

@CommandLine.Command(description = "Lyrics analysis",
        name = "LyricsAnalyzer",
        mixinStandardHelpOptions = true,
        version = "Lyrics Analyzer 1.0")

public class ArgsHandler implements Runnable {
    @CommandLine.Option(names = "-about") private boolean about = false;                   // -about

    @CommandLine.Option(names = "--file",                                                  // --file
            hidden = true,
            paramLabel = "FILENAME",
            description = "File of database. Default: \"${DEFAULT-VALUE}\".")
    private String filename = "songlyrics\\songdata.csv";

    @CommandLine.Option(names = "--list_bands",                                            // --list_bands
            arity = "0..1",
            paramLabel = "NAME",
            description = "List of all artists or artists with \"NAME\" in the title.")
    private String artistSubstr;

    @CommandLine.Option(names = "--list_songs",                                            // --list_songs
            arity = "0..1",
            paramLabel = "NAME",
            description = "List of all songs or songs with \"NAME\" in the title.")
    private String songSubstr;

    @CommandLine.Option(names = "--artist_uniq_words",                                     // --artist_uniq_words
            paramLabel = "NAME",
            description = "The unique words list of the artist with the name \"NAME\".")
    private String artistName = null;

    @CommandLine.Option(names = "--song_uniq_words",                                       // --song_uniq_words
            paramLabel = "NAME",
            description = "The unique words list of the song with the name \"NAME\".")
    private String songName = null;

    @CommandLine.Option(names = "--artist_word_rating",                                    // --artist_word_rating
    description = "List of all artists in descending order of the number unique words in their songs.")
    private boolean artistWordRating = false;

    @CommandLine.Option(names = "--similar_artists",                                       // --similar_artists
    paramLabel = "NAME",
    description = "List of artists than the vocabulary intersects with the vocabulary of the artist" +
                  "with the name \"NAME\".")
    private String similarArtistName = null;

    @CommandLine.Option(names = "--similar_songs",                                         // --similar_songs
            paramLabel = "NAME",
            description = "List of songs than the vocabulary intersects with the vocabulary of the song" +
                          "with the name \"NAME\".")
    private String similarSongName = null;

    @CommandLine.Option(names = "--truly_uniq_words",                                      // --truly_uniq_words
            description = "List of words that are found only in one song.")
    private boolean truly_uniq_words = false;

    public static final int ARTIST = 0;
    public static final int SONG = 1;
    public static final int LYRICS = 3;

    public void run() {
        if(about) System.out.println("Lyrics analysis.\n" +                                // -about
                "Author: Tsema G.S. 2018. In the course of \"Software Engineering\".\n" +
                "Saint-Petersburg Electrotechnical University ETU \"LETI\".");

        if(artistSubstr != null) {                                                         // --list_bands
            DBService dbService = new DBService(filename);

            Map<String,Integer> artists = dbService.getNames(new TypedStr(ARTIST, artistSubstr));

            artists.keySet().stream()
                            .sorted(Comparator.comparing(String::toLowerCase))
                            .forEach(System.out::println);
        }

        if(songSubstr != null) {                                                           // --list_songs
            DBService dbService = new DBService(filename);
            Map<String, Integer> songs = dbService.getNames(new TypedStr(SONG, songSubstr));

            songs.keySet().stream()
                          .sorted(Comparator.comparing(String::toLowerCase))
                          .forEach(System.out::println);

        }

        if(artistName != null) {                                                           // --artist_uniq_words
            DBService dbService = new DBService(filename);
            Map<String, Integer> words = dbService.getUniqWords(new TypedStr(ARTIST, artistName));

            words.entrySet().stream()
                            .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                            .forEach(x -> System.out.println(x.getKey()));
        }

        if(songName != null) {                                                             // --song_uniq_words
            DBService dbService = new DBService(filename);
            Map<String, Integer> words = dbService.getUniqWords(new TypedStr(SONG, songName));

            words.entrySet().stream()
                            .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                            .forEach(x -> System.out.println(x.getKey()));
        }

        if(artistWordRating) {                                                             // --artist_word_rating
            DBService dbService = new DBService(filename);
            Map<String, Set<String>> artistsUWords = dbService.getUniqueWords(ARTIST);

            Map<String, Integer> artistUWordsNum = artistsUWords.entrySet().stream()
                                                                .collect(Collectors.toMap(Map.Entry::getKey,
                                                                         entry -> entry.getValue().size()));

            artistUWordsNum.entrySet().stream()
                                      .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                                      .forEach(entry -> System.out.println(entry.getKey()));
        }

        if(similarArtistName != null) {                                                    // --similar_artists
            DBService dbService = new DBService(filename);
            Map<String, Set<String>> artistsUWords = dbService.getUniqueWords(ARTIST);

            if(!artistsUWords.containsKey(similarArtistName)) {
                System.out.println("Artist \"" + similarArtistName + "\" not found.");
                System.exit(1);
            }

            artistsUWords.entrySet().stream()
                                    .peek(entry -> entry.getValue().retainAll(artistsUWords.get(similarArtistName)))
                                    .map(entry -> new AbstractMap.SimpleEntry<String, Integer>(entry.getKey(),
                                                                                               entry.getValue().size()))
                                    .filter(entry -> entry.getValue() != 0)
                                    .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                                    .skip(1)
                                    .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
        }

        if(similarSongName != null) {                                                      // --similar_artists
            DBService dbService = new DBService(filename);
            Map<String, Set<String>> songsUWords = dbService.getUniqueWords(SONG);

            if(!songsUWords.containsKey(similarSongName)) {
                System.out.println("Song \"" + similarSongName + "\" not found.");
                System.exit(1);
            }

            songsUWords.entrySet().stream()
                                  .peek(entry -> entry.getValue().retainAll(songsUWords.get(similarSongName)))
                                  .map(entry -> new AbstractMap.SimpleEntry<String, Integer>(entry.getKey(),
                                                                                             entry.getValue().size()))
                                  .filter(entry -> entry.getValue() != 0)
                                  .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                                  .skip(1)
                                  .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
        }

        if(truly_uniq_words) {                                                             // --truly_uniq_words
            DBService dbService = new DBService(filename);
            Map<String, Set<String>> songsUWords = dbService.getUniqueWords(SONG);

            Map<String, Integer> result = new HashMap<>();

            for(Map.Entry<String, Set<String>> entry : songsUWords.entrySet()) {
                for(String value : entry.getValue()) {
                    if(result.containsKey(value)) result.put(value, result.get(value) + 1);
                    else result.put(value, 0);
                }
            }

            result.entrySet().stream()
                             .filter(entry -> entry.getValue() == 0)
                             .forEach(entry -> System.out.println(entry.getKey()));

        }
    }
}
