package uk.org.thehickses.publicholidays;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.stream.Stream;

public class Utils
{
    public static LocalDate dateOfEaster(int year)
    {
        int positionInLunarCycle = year % 19;
        int century = year / 100;
        int positionInCentury = year % 100;
        int centuryleapYearCycle = century / 4;
        int positionInCenturyLeapYearCycle = century % 4;
        int g = (8 * century + 13) / 25;
        int h = (19 * positionInLunarCycle + century - centuryleapYearCycle - g + 15) % 30;
        int m = (positionInLunarCycle + 11 * h) / 319;
        int leapYearCycle = positionInCentury / 4;
        int positionInLeapYearCycle = positionInCentury % 4;
        int l = (2 * positionInCenturyLeapYearCycle + 2 * leapYearCycle - positionInLeapYearCycle
                - h + m + 32) % 7;
        int month = (h - m + l + 90) / 25;
        int day = (h - m + l + month + 19) % 32;
        return LocalDate.of(year, month, day);
    }

    static LocalDate instanceOfDayInMonth(int no, DayOfWeek day, Month month, int year)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstOrLastDayOfMonth = no > 0 ? yearMonth.atDay(1) : yearMonth.atEndOfMonth();
        int weekIncrement = no > 0 ? 7 : -7;
        int differenceFromDay = day.getValue() - firstOrLastDayOfMonth.getDayOfWeek().getValue();
        int adjustment = (differenceFromDay == 0 ? 0
                : differenceFromDay + ((differenceFromDay >= 0) == (no >= 0) ? 0 : weekIncrement))
                + (Math.abs(no) - 1) * weekIncrement;
        return firstOrLastDayOfMonth.plusDays(adjustment);
    }

    private static boolean isWeekend(TemporalAccessor date)
    {
        DayOfWeek day = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
        return Stream.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).anyMatch(day::equals);
    }

    public static LocalDate weekdayOnOrAfter(int no, int day, Month month, int year)
    {
        LocalDate answer = LocalDate.of(year, month, day);
        for (int count = 1;; count++)
        {
            while (answer.query(Utils::isWeekend))
                answer = answer.plusDays(1);
            if (count == no)
                break;
            answer = answer.plusDays(1);
        }
        return answer;
    }
}
