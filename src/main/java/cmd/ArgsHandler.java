package cmd;

import dbService.DBService;
import picocli.CommandLine;

import java.util.*;

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

    @CommandLine.Option(names = "--artist_word_rating",
    description = "List of all artists in descending order of the number unique words in their songs.")
    private boolean artistWordRating = false;                                              // --artist_word_rating

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

            new ArrayList(artists.keySet())
                    .stream()
                    .sorted(Comparator.comparing(o -> o.toString().toLowerCase()))
                    .forEach(System.out::println);
        }

        if(songSubstr != null) {                                                           // --list_songs
            DBService dbService = new DBService(filename);
            Map<String, Integer> songs = dbService.getNames(new TypedStr(SONG, songSubstr));

            new ArrayList(songs.keySet())
                    .stream()
                    .sorted(Comparator.comparing(o -> o.toString().toLowerCase()))
                    .forEach(System.out::println);

        }

        if(artistName != null) {                                                           // --artist_uniq_words
            DBService dbService = new DBService(filename);
            Map<String, Integer> words = dbService.getWords(new TypedStr(ARTIST, artistName));

            words.entrySet().stream()
                            .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                            .forEach(x -> System.out.println(x.getKey()));
        }

        if(songName != null) {                                                             // --song_uniq_words
            DBService dbService = new DBService(filename);
            Map<String, Integer> words = dbService.getWords(new TypedStr(SONG, songName));

            words.entrySet().stream()
                            .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                            .forEach(x -> System.out.println(x.getKey()));
        }

        if(artistWordRating) {
            System.out.println("arrrr");
        }
    }
}
