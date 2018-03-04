package Homework;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FileReaderWriter {

    private FileReaderWriter() {
    }

    public static List<String> writeFileToList(File fileToRead) {
        List<String> fileToList = new LinkedList<>();
        Pattern pattern = Pattern.compile("(?<time>\\d{2}:\\d{2})\\s(?<activity>.+)");
        try {
            Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileToRead)));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    fileToList.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileToList;
    }

    public static void writeResultListToFile(File fileToWrite, List<String> resultList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToWrite))) {
            for (String s : resultList) {
                writer.write(s + "\n");
                if (s.contains("Конец")){
                    writer.write("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
