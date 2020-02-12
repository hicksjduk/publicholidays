package uk.org.thehickses.publicholidays;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A utility class that calculates public holidays in England and Wales for any given year. It follows the rules that
 * have been in force since the last change to the law, in 1978, and also reflects the exceptional holidays that have
 * been granted or moved for various events.
 * 
 * @author Jeremy Hicks
 */
public class EnglandAndWalesPublicHolidayCalculator
{
    /**
     * Returns a sorted stream of the public holidays in England and Wales for the specified year.
     * 
     * @param year
     *            the year.
     * @return the public holidays.
     */
    public Stream<LocalDate> getPublicHolidays(int year)
    {
        LocalDate easterDay = dateOfEaster(year);
        Stream<LocalDate> standardHolidays = Stream
                .of(weekdayOnOrAfter(1, 1, Month.JANUARY, year), easterDay.minusDays(2),
                        easterDay.plusDays(1),
                        instanceOfDayInMonth(1, DayOfWeek.MONDAY, Month.MAY, year),
                        instanceOfDayInMonth(-1, DayOfWeek.MONDAY, Month.MAY, year),
                        instanceOfDayInMonth(-1, DayOfWeek.MONDAY, Month.AUGUST, year),
                        weekdayOnOrAfter(1, 25, Month.DECEMBER, year),
                        weekdayOnOrAfter(2, 25, Month.DECEMBER, year));
        Stream<LocalDate> additionalHolidays = Stream.of(exceptions(year, true));
        return Stream
                .concat(standardHolidays.filter(isNotExcluded(year)), additionalHolidays)
                .sorted();
    }

    private Predicate<LocalDate> isNotExcluded(int year)
    {
        List<LocalDate> exclusions = Arrays.asList(exceptions(year, false));
        return d -> !exclusions.contains(d);
    }

    private static final List<Exceptional> exceptions = Arrays
            .asList(new Exceptional(1981, Month.JULY, 29, true),
                    new Exceptional(instanceOfDayInMonth(1, DayOfWeek.MONDAY, Month.MAY, 1995),
                            false),
                    new Exceptional(1995, Month.MAY, 8, true),
                    new Exceptional(1999, Month.DECEMBER, 31, true),
                    new Exceptional(instanceOfDayInMonth(-1, DayOfWeek.MONDAY, Month.MAY, 2002),
                            false),
                    new Exceptional(2002, Month.JUNE, 3, true),
                    new Exceptional(2002, Month.JUNE, 4, true),
                    new Exceptional(2011, Month.APRIL, 29, true),
                    new Exceptional(instanceOfDayInMonth(1, DayOfWeek.MONDAY, Month.MAY, 2020),
                            false),
                    new Exceptional(2020, Month.MAY, 8, true));

    static LocalDate[] exceptions(int year, boolean isHoliday)
    {
        return exceptions
                .stream()
                .filter(x -> x.isHoliday == isHoliday)
                .map(x -> x.date)
                .filter(d -> d.getYear() == year)
                .toArray(LocalDate[]::new);
    }

    static LocalDate weekdayOnOrAfter(int no, int day, Month month, int year)
    {
        LocalDate answer = LocalDate.of(year, month, day);
        for (int count = 1;; count++)
        {
            while (answer.query(EnglandAndWalesPublicHolidayCalculator::isWeekend))
                answer = answer.plusDays(1);
            if (count == no)
                break;
            answer = answer.plusDays(1);
        }
        return answer;
    }

    static LocalDate instanceOfDayInMonth(int no, DayOfWeek day, Month month, int year)
    {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate answer = no > 0 ? yearMonth.atDay(1) : yearMonth.atEndOfMonth();
        long increment = no > 0 ? 1 : -1;
        for (int count = 1;; count++)
        {
            while (answer.getDayOfWeek() != day)
                answer = answer.plusDays(increment);
            if (count == Math.abs(no))
                break;
            answer = answer.plusDays(increment);
        }
        return answer;
    }

    private static boolean isWeekend(TemporalAccessor date)
    {
        DayOfWeek day = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
        switch (day)
        {
        case SATURDAY:
        case SUNDAY:
            return true;
        default:
            return false;
        }
    }

    static LocalDate dateOfEaster(int year)
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

    private static class Exceptional
    {
        public final LocalDate date;
        public final boolean isHoliday;

        public Exceptional(LocalDate date, boolean isHoliday)
        {
            this.date = date;
            this.isHoliday = isHoliday;
        }

        public Exceptional(int year, Month month, int day, boolean isHoliday)
        {
            this(LocalDate.of(year, month, day), isHoliday);
        }
    }
}
