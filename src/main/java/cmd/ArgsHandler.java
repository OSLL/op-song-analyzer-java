package cmd;

import picocli.CommandLine;

@CommandLine.Command(description = "Lyrics analysis",
                     name = "LyricsAnalyzer",
                     mixinStandardHelpOptions = true,
                     version = "Lyrics Analyzer 1.0")

public class ArgsHandler implements Runnable {

    @CommandLine.Option(names = "-about", description = "About LyricsAnalyzer.") private boolean about = false;

    public void run() {
        if(about) System.out.println("Lyrics analysis.\n" +
                                    "Author: Tsema G. 2018. In the course of \"Software Engineering\".\n" +
                                    "Saint-Petersburg Electrotechnical University ETU \"LETI\".");
    }
}
