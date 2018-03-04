package Homework;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EveryDayDetails {

    private static final int MINUTS_IN_HOUR = 60;
    private static final int HUNDREAD_PERCENT = 100;

    private EveryDayDetails() {
    }

    public static void makeDetailsForDaysAndWriteToNewFile(File fileToRead, File fileToWrite) {
        List<String> fileContentInList = FileReaderWriter.writeFileToList(fileToRead);
        List<String> listWithIntervals = IntervalMaker.makeInterval(fileContentInList);
        List<String> resultList = countMinutsForEachActivity(listWithIntervals);
        FileReaderWriter.writeResultListToFile(fileToWrite, resultList);
    }

    private static void addDayDetails(Map<String, Integer> minutsByActivity, List<String> resultList, int day) {
        List<String> lectionsDetails = new ArrayList<>();
        int generalMinsForDag = 0;
        int minLessonsForDag = countLectionsMinsForDay(minutsByActivity);
        for (Map.Entry<String, Integer> entry : minutsByActivity.entrySet()) {
            if (!(entry.getKey().equalsIgnoreCase("Упражнения") | entry.getKey().equalsIgnoreCase("Решения")
                    | entry.getKey().contains("Перерыв") | entry.getKey().contains("Обеденный перерыв"))) {
                lectionsDetails = addToLectionsList(lectionsDetails, entry, minLessonsForDag);
            }
            generalMinsForDag += entry.getValue();
        }
        resultList.add("День " + day + ":");
        resultList.add("Лекции: " + minLessonsForDag + " минут " + minLessonsForDag * HUNDREAD_PERCENT / generalMinsForDag + "%");
        for (Map.Entry<String, Integer> entry : minutsByActivity.entrySet()) {
            if (entry.getKey().contains("Упражнения") | entry.getKey().contains("Решения")
                    | entry.getKey().contains("Перерыв") | entry.getKey().contains("Обеденный перерыв")) {
                resultList.add(entry.getKey() + ": " + entry.getValue() + " минут " + entry.getValue() * HUNDREAD_PERCENT / generalMinsForDag + "%");
            }
        }
        resultList.add("Лекции:");
        resultList = insertListOfLections(lectionsDetails, resultList);
        resultList.add("");
    }

    private static int countLectionsMinsForDay(Map<String, Integer> minutsByActivity) {
        int minLessonsForDag = 0;
        for (Map.Entry<String, Integer> entry : minutsByActivity.entrySet()) {
            if (!(entry.getKey().equalsIgnoreCase("Упражнения") | entry.getKey().equalsIgnoreCase("Решения")
                    | entry.getKey().contains("Перерыв") | entry.getKey().contains("Обеденный перерыв"))) {
                minLessonsForDag += entry.getValue();
            }
        }

        return minLessonsForDag;
    }

    private static List<String> countMinutsForEachActivity(List<String> list) {
        List<String> minuts = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\d{2}):(\\d{2})-(\\d{2}):(\\d{2})");
        int generalMinuts;
        for (String s : list) {
            Matcher matcher = pattern.matcher(s);
            StringBuffer buffer = new StringBuffer();
            if (matcher.find()) {
                int firstHour = Integer.parseInt(matcher.group(1));
                int firstMin = Integer.parseInt(matcher.group(2));
                int secHour = Integer.parseInt(matcher.group(3));
                int secMin = Integer.parseInt(matcher.group(4));
                if (secMin < firstMin) {
                    secMin += MINUTS_IN_HOUR;
                    secHour -= 1;
                }
                generalMinuts = (secMin - firstMin) + (secHour - firstHour) * MINUTS_IN_HOUR;
                String countedMins = Integer.toString(generalMinuts);
                matcher.appendReplacement(buffer, countedMins);
                matcher.appendTail(buffer);
                minuts.add(buffer.toString());
            } else {
                generalMinuts = 0;
                minuts.add(Integer.toString(generalMinuts) + " " + "Конец");
            }
        }

        return countActivityMinutsForDagenWithDetailsOfLections(minuts);
    }

    private static List<String> insertListOfLections(List<String> lectionsDetails, List<String> resultList) {
        for (String lectionsDetail : lectionsDetails) {
            resultList.add(lectionsDetail);
        }
        return resultList;
    }

    private static List<String> countActivityMinutsForDagenWithDetailsOfLections(List<String> minuts) {
        Map<String, Integer> minutsByActivity = new HashMap<>();
        List<String> resultList = new ArrayList<>();
        int day = 1;
        Pattern pattern = Pattern.compile("(\\d+)\\s(.+)");
        for (String line : minuts) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                if (!(matcher.group(2).contains("Конец"))) {
                    int firstGroup = Integer.parseInt(matcher.group(1));
                    minutsByActivity.merge(matcher.group(2), firstGroup, Integer::sum);
                } else {
                    addDayDetails(minutsByActivity, resultList, day);
                    day++;
                    minutsByActivity.clear();
                }
            }
        }

        return resultList;
    }

    private static List<String> addToLectionsList(List<String> lectionList, Map.Entry<String, Integer> entry, int minsOfLections) {
        lectionList.add(entry.getKey() + ": " + entry.getValue() + " минут " + entry.getValue() * HUNDREAD_PERCENT / minsOfLections + "%");
        return lectionList;
    }
}
