package cmd;

import dbService.DBService;

import picocli.CommandLine;
import java.util.Set;

@CommandLine.Command(description = "Lyrics analysis",
        name = "LyricsAnalyzer",
        mixinStandardHelpOptions = true,
        version = "Lyrics Analyzer 1.0")

public class ArgsHandler implements Runnable {

    @CommandLine.Option(names = "-about") private boolean about = false;

    @CommandLine.Option(names = "--file",
            hidden = true,
            paramLabel = "FILENAME",
            description = "File of database. Default: \"${DEFAULT-VALUE}\".")
    private String filename = "songlyrics\\songdata.csv";

    @CommandLine.Option(names = "--list_bands",
            arity = "0..1",
            paramLabel = "NAME",
            description = "List of all artists or artists with \"NAME\" in the title.")
    private String artistSubstr;

    @CommandLine.Option(names = "--list_songs",
            arity = "0..1",
            paramLabel = "NAME",
            description = "List of all songs or songs with \"NAME\" in the title.")
    private String songSubstr;

    public void run() {
        if(about) System.out.println("Lyrics analysis.\n" +
                "Author: Tsema G.S. 2018. In the course of \"Software Engineering\".\n" +
                "Saint-Petersburg Electrotechnical University ETU \"LETI\".");

        if(artistSubstr != null) {
            DBService dbService = new DBService(filename);
            Set<String> artists = dbService.getArtists(artistSubstr);

            for(String artist : artists) {
                System.out.println(artist);
            }
        }

        if(songSubstr != null) {
            DBService dbService = new DBService(filename);
            Set<String> songs = dbService.getSongs(songSubstr);

            for(String song : songs) {
                System.out.println(song);
            }
        }
    }
}
