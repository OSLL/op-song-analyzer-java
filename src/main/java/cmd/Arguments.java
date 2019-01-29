package cmd;

import picocli.CommandLine;
import java.io.File;

@CommandLine.Command(description = "Lyrics analysis",
        name = "LyricsAnalyzer",
        sortOptions = false,
        usageHelpWidth = 150,
        mixinStandardHelpOptions = true,
        version = "Lyrics Analyzer 1.0")
public class Arguments {
    @CommandLine.Option(names = "--file",                                                  // --file
            hidden = true,
            paramLabel = "FILENAME",
            description = "File of database. Default: \"${DEFAULT-VALUE}\".")
    private String filePath = "resources" + File.separatorChar + "songdata.csv";
    public String getFilePath() { return filePath; }

    @CommandLine.Option(names = "--list_bands",                                            // --list_bands
            arity = "0..1",
            paramLabel = "NAME",
            description = "List of all artists or artists with \"NAME\" in the title.")
    private String artistSubstr;
    public String getArtistSubstr() { return artistSubstr; }

    @CommandLine.Option(names = "--list_songs",                                            // --list_songs
            arity = "0..1",
            paramLabel = "NAME",
            description = "List of all songs or songs with \"NAME\" in the title.")
    private String songSubstr;
    public String getSongSubstr() { return songSubstr; }

    @CommandLine.Option(names = "--artist_uniq_words",                                     // --artist_uniq_words
            paramLabel = "NAME",
            description = "The unique words list of the artist with the name \"NAME\".")
    private String artistName = null;
    public String getArtistName() { return artistName; }

    @CommandLine.Option(names = "--song_uniq_words",                                       // --song_uniq_words
            paramLabel = "NAME",
            description = "The unique words list of the song with the name \"NAME\".")
    private String songName = null;
    public String getSongName() { return songName; }

    @CommandLine.Option(names = "--artist_word_rating",                                    // --artist_word_rating
            description = "List of all artists in descending order of the number unique words in their songs.")
    private boolean artistWordRating = false;
    public boolean isArtistWordRating() { return artistWordRating; }

    @CommandLine.Option(names = "--similar_artists",                                       // --similar_artists
            paramLabel = "NAME",
            description = "List of artists than the vocabulary intersects with the vocabulary of the artist" +
                    "with the name \"NAME\".")
    private String similarArtistName = null;
    public String getSimilarArtistName() { return similarArtistName; }

    @CommandLine.Option(names = "--similar_songs",                                         // --similar_songs
            paramLabel = "NAME",
            description = "List of songs than the vocabulary intersects with the vocabulary of the song" +
                    "with the name \"NAME\".")
    private String similarSongName = null;
    public String getSimilarSongName() { return similarSongName; }

    @CommandLine.Option(names = "--truly_uniq_words",                                      // --truly_uniq_words
            description = "List of words that are found only in one song.")
    private boolean truly_uniq_words = false;
    public boolean isTrulyUniqWords() { return truly_uniq_words; }

    @CommandLine.Option(names = "--about") private boolean about = false;                   // -about
    public boolean isAbout() { return about; }
}
