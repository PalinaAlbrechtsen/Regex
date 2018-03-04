package Homework;

import java.io.File;
import static java.lang.String.join;

public class Main {

    public static void main(String[] args) {
        File file = new File(join(File.separator, "src", "Homework", "courseIT.log"));
        File fileToWriteScheduleWithIntervals = new File(join(File.separator, "src", "Homework", "schedule_with_intervals.log"));
        File fileToWriteDetailsOfDays = new File(join(File.separator, "src", "Homework", "details_of_course.log"));

        IntervalMaker.makeIntervalsAndWriteToNewFile(file, fileToWriteScheduleWithIntervals);
        EveryDayDetails.makeDetailsForDaysAndWriteToNewFile(file, fileToWriteDetailsOfDays);
    }
}
