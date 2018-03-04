package Homework;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class IntervalMaker {

    private IntervalMaker() {
    }

    public static List<String> makeInterval(List<String> list) {
        Pattern pattern = Pattern.compile("(?<time>\\d{2}:\\d{2})\\s(?<activity>.+)");
        for (int i = 0; i + 1 < list.size(); i++) {
            Matcher matcher = pattern.matcher(list.get(i));
            Matcher nextMatcher = pattern.matcher(list.get(i + 1));
            if (matcher.find() && nextMatcher.find()) {
                if (!(matcher.group("activity").contains("Конец"))) {
                    String formattedTime = matcher.group("time") + "-" + nextMatcher.group("time");
                    list.set(i, formattedTime + " " + matcher.group("activity"));
                }
            }
        }
        return list;
    }

    public static void makeIntervalsAndWriteToNewFile(File fileToRead, File fileToWrite) {
        List<String> fileContentInList = FileReaderWriter.writeFileToList(fileToRead);
        List<String> list = makeInterval(fileContentInList);
        FileReaderWriter.writeResultListToFile(fileToWrite, list);
    }
}
