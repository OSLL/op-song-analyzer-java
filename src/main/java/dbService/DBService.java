package dbService;

import java.util.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opencsv.CSVReader;
import cmd.TypedStr;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;
import stemmer.PorterStemmer;

import static cmd.ArgsHandler.ARTIST;
import static cmd.ArgsHandler.LYRICS;
import static cmd.ArgsHandler.SONG;

public class DBService {

    private static Logger log = Logger.getLogger(DBService.class.getName());

    private CSVReader reader;

    public DBService(CSVReader reader) {
        this.reader = reader;
    }

    public Set<String> getNames(TypedStr typedStr) throws DBException {

        HashSet<String> data = new HashSet<>();
        String[] nextLine;

        try(ProgressBar pb = new ProgressBar("Progress", 57650, ProgressBarStyle.ASCII)) {
            while ((nextLine = reader.readNext()) != null) {
                pb.step();

                if(nextLine.length != 4) {
                    log.info("Database Integrity corruption detected! Bad entry will be skipped.");
                    continue;
                }

                String sample = typedStr.getSubstr().toLowerCase();
                String entry = nextLine[typedStr.getType()].toLowerCase();

                if(!entry.isEmpty() && entry.contains(sample)) {
                    data.add(nextLine[typedStr.getType()]);
                }
            }

        } catch (IOException e) {
            throw new DBException("Database file is fatal damaged!");
        }

        if(data.isEmpty()) {
            switch (typedStr.getType()) {
                case ARTIST: throw new DBException("Artists not found.");
                case SONG:   throw new DBException("Songs not found.");
            }
        }

        return data;
    }

    public Map<String, Integer> getUniqueWordsToFreq(TypedStr typedStr) throws DBException {

        Map<String, Integer> data = new HashMap<>();
        PorterStemmer stemmer = new PorterStemmer();
        String[] nextLine;

        try(ProgressBar pb = new ProgressBar("Progress", 57650, ProgressBarStyle.ASCII)) {
            while ((nextLine = reader.readNext()) != null) {
                pb.step();

                if(nextLine.length != 4) {
                    log.info("Database Integrity corruption detected! Bad entry will be skipped.");
                    continue;
                }

                String sample = typedStr.getSubstr();
                String entry = nextLine[typedStr.getType()];

                if(!entry.isEmpty() && entry.equals(sample)) {

                    String[] words = spliterator(nextLine[LYRICS]);
                    if (words == null) {
                        log.info("The entry contains an empty field \"Lyrics\" " +
                                      "and will be skipped.");
                        continue;
                    }

                    for(String word : words) {
                        String stem = stemmer.getStem(word);
                        if(!data.containsKey(stem)) data.put(stem, 1);
                        else data.put(stem, data.get(stem) + 1);
                    }
                }
            }

        } catch (IOException e) {
            log.log(Level.SEVERE, "Database file is fatal damaged! Exception: ", e);
            throw new DBException("Database file is fatal damaged!");
        }

        if(data.isEmpty()) {
            switch (typedStr.getType()) {
                case ARTIST: {
                    log.log(Level.SEVERE, "Artist is not found or his songs have no words.");
                    throw new DBException("Artist is not found or his songs have no words.");
                }
                case SONG: {
                    log.log(Level.SEVERE, "Song is not found or has no words.");
                    throw new DBException("Song is not found or has no words.");
                }
            }
        }

        return data;
    }

    public Map<String, Set<String>> getNameToUniqueWords(int SOURCE) throws DBException {

        HashMap<String, Set<String>> data = new HashMap<>();
        PorterStemmer stemmer = new PorterStemmer();
        String[] nextLine;

        try(ProgressBar pb = new ProgressBar("Progress", 57650, ProgressBarStyle.ASCII)) {
            while ((nextLine = reader.readNext()) != null) {
                pb.step();

                if(nextLine.length != 4) {
                    log.info("Database Integrity corruption detected! Bad entry will be skipped.");
                    continue;
                } else if(nextLine[SOURCE].isEmpty()) {
                    log.info("The entry contains an empty field \"ArtistName\" " +
                                  "or \"SongName\" and will be skipped.");
                    continue;
                }

                if(data.containsKey(nextLine[SOURCE])) {
                    Set<String> uniqueWords = data.get(nextLine[SOURCE]);
                    String[] words = spliterator(nextLine[LYRICS]);
                    if (words == null) {
                        log.info("The entry contains an empty field \"Lyrics\" " +
                                "and will be skipped.");
                        continue;
                    }

                    for(String word : words) {
                        uniqueWords.add(stemmer.getStem(word));
                    }

                    data.put(nextLine[SOURCE], uniqueWords);

                } else {

                    Set<String> uniqueWords = new HashSet<>();
                    String[] words = spliterator(nextLine[LYRICS]);
                    if (words == null) {
                        log.info("The entry contains an empty field \"Lyrics\" " +
                                "and will be skipped.");
                        continue;
                    }

                    for(String word : words) {
                        uniqueWords.add(stemmer.getStem(word));
                    }

                    data.put(nextLine[SOURCE], uniqueWords);
                }
            }

        } catch (IOException e) {
            log.log(Level.SEVERE, "Database file is fatal damaged! Exception: ", e);
            throw new DBException("Database file is fatal damaged!");
        }

        if(data.isEmpty()) {
            log.log(Level.SEVERE, "Database file does not contain songs or artists.");
            throw new DBException("Database file does not contain songs or artists.");
        }

        return data;
    }

    private String[] spliterator(String targetString) {
        String[] splitResult = targetString.toLowerCase()
                                           .trim()
                                           .replaceAll("[^a-zA-Z ]", "")
                                           .split(" {1,}");
        return (splitResult.length == 1 && splitResult[0].isEmpty()) ? null : splitResult;

    }
}
