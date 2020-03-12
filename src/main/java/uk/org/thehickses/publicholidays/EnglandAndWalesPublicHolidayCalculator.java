package uk.org.thehickses.publicholidays;

import static uk.org.thehickses.publicholidays.PublicHolidayExceptions.*;
import static uk.org.thehickses.publicholidays.Utils.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
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
}
