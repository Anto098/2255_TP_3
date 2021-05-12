package ca.umontreal.iro.ift2255.equipe13;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Set;


public class DateOperations {
    private static final String dayPattern = "dd-MM-yyyy";
    private static final String timePattern = "HH:mm";


    public static String getDayPattern() {
        return dayPattern;
    }

    public static String getTimePattern() {
        return timePattern;
    }

    public static String dayFormat(LocalDate date) {
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern(dayPattern);
        return date.format(dayFormatter);
    }

    public static LocalDate dayParse(String time) {
        DateTimeFormatter daytimeFormatter = DateTimeFormatter.ofPattern(dayPattern);
        return LocalDate.parse(time, daytimeFormatter);
    }

    public static String timeFormat(LocalTime date) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(timePattern);
        return date.format(timeFormatter);
    }

    public static LocalTime timeParse(String time) {
        DateTimeFormatter daytimeFormatter = DateTimeFormatter.ofPattern(timePattern);
        return LocalTime.parse(time, daytimeFormatter);
    }

    public static boolean isCorrectFormat(String date, String pattern) {
        try {
            if (pattern.equals(timePattern)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                LocalTime formatted = LocalTime.parse(date, formatter);
            } else if (pattern.equals(dayPattern)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                LocalDate formatted = LocalDate.parse(date, formatter);
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static String formatDaysOfWeek(Set<DayOfWeek> days) {
        LinkedList<String> orderedDays = new LinkedList<>();

        if (days.contains(DayOfWeek.SUNDAY))
            orderedDays.add("dimanches");
        if (days.contains(DayOfWeek.MONDAY))
            orderedDays.add("lundis");
        if (days.contains(DayOfWeek.TUESDAY))
            orderedDays.add("mardis");
        if (days.contains(DayOfWeek.WEDNESDAY))
            orderedDays.add("mercredis");
        if (days.contains(DayOfWeek.THURSDAY))
            orderedDays.add("jeudis");
        if (days.contains(DayOfWeek.FRIDAY))
            orderedDays.add("vendredis");
        if (days.contains(DayOfWeek.SATURDAY))
            orderedDays.add("samedis");

        int nbDays = orderedDays.size();
        StringBuilder outString = new StringBuilder();

        for (int i = 1; !orderedDays.isEmpty(); i++) {
            if (i == 1)
                outString.append(orderedDays.pollFirst());
            else if (i != nbDays)
                outString.append(", ").append(orderedDays.pollFirst());
            else
                outString.append(" et ").append(orderedDays.pollFirst());
        }

        return outString.toString();
    }

    public static String formatDaysOfWeekRawNames(Set<DayOfWeek> days) {
        DayOfWeek[] dayArray = days.toArray(new DayOfWeek[days.size()]);
        StringBuilder outString = new StringBuilder();
        Arrays.sort(dayArray);

        for (DayOfWeek day : dayArray) {
            if (outString.length() > 0)
                outString.append(",");
            outString.append(day.toString());
        }

        return outString.toString();
    }

    public static LocalDate getLastFridayDate() {
        LocalDate today = LocalDate.now();
        int daysSinceLastFriday = (today.getDayOfWeek().getValue() + 2) % 7;

        if (daysSinceLastFriday == 0)
            daysSinceLastFriday = 7;

        return today.minusDays(daysSinceLastFriday);
    }

}
