import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

        Map<Integer, String> okCommands = new LinkedHashMap<>();
        okCommands.put(1, "java -jar LyricsAnalyzer-1.0.jar --file resources/testsongdata.csv --list_bands");
        okCommands.put(2, "java -jar LyricsAnalyzer-1.0.jar --file resources/testsongdata.csv --list_bands 1");
        okCommands.put(3, "java -jar LyricsAnalyzer-1.0.jar --file resources/testsongdata.csv --list_songs");
        okCommands.put(4, "java -jar LyricsAnalyzer-1.0.jar --file resources/testsongdata.csv --list_songs 2");
        okCommands.put(5, "java -jar LyricsAnalyzer-1.0.jar --file resources/testsongdata.csv --artist_uniq_words A3");
        okCommands.put(6, "java -jar LyricsAnalyzer-1.0.jar --file resources/testsongdata.csv --song_uniq_words A3S1");
        okCommands.put(7, "java -jar LyricsAnalyzer-1.0.jar --file resources/testsongdata.csv --artist_word_rating");
        okCommands.put(8, "java -jar LyricsAnalyzer-1.0.jar --file resources/testsongdata.csv --similar_artists A3");
        okCommands.put(9, "java -jar LyricsAnalyzer-1.0.jar --file resources/testsongdata.csv --similar_songs A1S2");
        okCommands.put(10, "java -jar LyricsAnalyzer-1.0.jar --file resources/testsongdata.csv --truly_uniq_words");

        Map<Integer, String> errCommands = new LinkedHashMap<>();
        errCommands.put(1, "java -jar LyricsAnalyzer-1.0.jar --file resources/nofile.csv --list_bands");
        errCommands.put(2, "java -jar LyricsAnalyzer-1.0.jar --file resources/logging.properties --list_bands");
        errCommands.put(3, "java -jar LyricsAnalyzer-1.0.jar --file resources/testsongdata.csv --list_songs");
        errCommands.put(4, "java -jar LyricsAnalyzer-1.0.jar --file resources/testsongdata.csv --list_songs K");
        errCommands.put(5, "java -jar LyricsAnalyzer-1.0.jar --file resources/testsongdata.csv --list_bands K");

        String errMsg_DBFileNotExist = "Database file \"nofile.csv\" does not exist.";
        String errMsg_noCSVfile = "Database file \"logging.properties\" must have *.csv-extension.";
        String errMsg_artistsNotFound = "Artists not found.";
        String errMsg_songsNotFound = "Songs not found.";
        String infoMsg_badEntry = "INFO: Database Integrity corruption detected! Bad entry will be skipped.";

        for(Map.Entry<Integer, String> cmd : okCommands.entrySet()) {
            try {
                if (cmd.getKey() == 1) {
                    if (execCmd(cmd.getValue()).toString().equals("[A1, A2, A3]")) System.out.println("Test 1 passed.");
                } else if (cmd.getKey() == 2) {
                    if (execCmd(cmd.getValue()).toString().equals("[A1]")) System.out.println("Test 2 passed.");
                } else if (cmd.getKey() == 3) {
                    if (execCmd(cmd.getValue()).toString().equals("[A1S1, A1S2, A2S1, A3S1]"))
                        System.out.println("Test 3 passed.");
                } else if (cmd.getKey() == 4) {
                    if (execCmd(cmd.getValue()).toString().equals("[A1S2, A2S1]")) System.out.println("Test 4 passed.");
                } else if (cmd.getKey() == 5) {
                    if (execCmd(cmd.getValue()).toString().equals("[a, f, s, t, i, j]"))
                        System.out.println("Test 5 passed.");
                } else if (cmd.getKey() == 6) {
                    if (execCmd(cmd.getValue()).toString().equals("[a, f, s, t, i, j]"))
                        System.out.println("Test 6 passed.");
                } else if (cmd.getKey() == 7) {
                    if (execCmd(cmd.getValue()).toString().equals("[A1, A3, A2]")) System.out.println("Test 7 passed.");
                } else if (cmd.getKey() == 8) {
                    if (execCmd(cmd.getValue()).toString().equals("[A1=3, A2=2]")) System.out.println("Test 8 passed.");
                } else if (cmd.getKey() == 9) {
                    if (execCmd(cmd.getValue()).toString().equals("[A3S1=2, A2S1=1]"))
                        System.out.println("Test 9 passed.");
                } else if (cmd.getKey() == 10) {
                    if (execCmd(cmd.getValue()).toString().equals("[b, r, c, s, f, i, o]"))
                        System.out.println("Test 10 passed.");
                }
            } catch (IOException e) {
                System.out.println("Test " + cmd.getKey() + " execution error!");
            }
        }

        for(Map.Entry<Integer, String> cmd : errCommands.entrySet()) {
            try {
                if (cmd.getKey() == 1) {
                    if (execCmd(cmd.getValue()).get(0).equals(errMsg_DBFileNotExist) &&
                            readLogFile().get(1).equals("SEVERE: " + errMsg_DBFileNotExist)) {
                        System.out.println("Test 11 passed.");
                    } else System.out.println("Test 11 not passed!");
                } else if(cmd.getKey() == 2) {
                    if(execCmd(cmd.getValue()).get(0).equals(errMsg_noCSVfile) &&
                       readLogFile().get(1).equals("SEVERE: " + errMsg_noCSVfile)) {
                        System.out.println("Test 12 passed.");
                    } else System.out.println("Test 12 not passed!");
                } else if(cmd.getKey() == 3) {
                    execCmd(cmd.getValue());
                    if(readLogFile().get(1).equals(infoMsg_badEntry)) System.out.println("Test 13 passed.");
                    else System.out.println("Test 13 not passed!");
                } else if(cmd.getKey() == 4) {
                    if(execCmd(cmd.getValue()).get(0).equals(errMsg_songsNotFound))
                        System.out.println("Test 14 passed.");
                    else System.out.println("Test 14 not passed!");
                } else if(cmd.getKey() == 5) {
                    if(execCmd(cmd.getValue()).get(0).equals(errMsg_artistsNotFound))
                        System.out.println("Test 15 passed.");
                    else System.out.println("Test 15 not passed!");
                }
            } catch (IOException | IndexOutOfBoundsException e) {
                System.out.println("Test " + cmd.getKey() + " not passed!");
            }
        }

        System.out.println("All system tests is completed!");
    }

     private static List<String> execCmd(String command) throws IOException {
        List<String> out = new LinkedList<>();

        Process process = Runtime.getRuntime().exec(command);
        InputStream stdout = process.getInputStream();
        BufferedReader reader = new BufferedReader (new InputStreamReader (stdout));
        String line;

        while ((line = reader.readLine ()) != null) {
            out.add(line);
        }
        reader.close();

        return out;
    }

    private static List<String> readLogFile() throws FileNotFoundException, IOException {
        List<String> out = new LinkedList<>();

        FileInputStream fstream = new FileInputStream("resources/log.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(fstream));
        String line;

        while ((line = reader.readLine ()) != null) out.add(line);

        reader.close();

        return out;
    }
}
