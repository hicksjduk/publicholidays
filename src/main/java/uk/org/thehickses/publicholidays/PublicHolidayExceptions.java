package uk.org.thehickses.publicholidays;

import static uk.org.thehickses.publicholidays.Utils.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class PublicHolidayExceptions
{
    private static final List<PublicHolidayExceptions> instances = Arrays
            .asList(exceptionalHoliday(LocalDate.of(1981, Month.JULY, 29)),
                    PublicHolidayExceptions
                            .exceptionalNonHoliday(
                                    instanceOfDayInMonth(1, DayOfWeek.MONDAY, Month.MAY, 1995)),
                    exceptionalHoliday(LocalDate.of(1995, Month.MAY, 8)),
                    exceptionalHoliday(LocalDate.of(1999, Month.DECEMBER, 31)),
                    PublicHolidayExceptions
                            .exceptionalNonHoliday(
                                    instanceOfDayInMonth(-1, DayOfWeek.MONDAY, Month.MAY, 2002)),
                    exceptionalHoliday(LocalDate.of(2002, Month.JUNE, 3)),
                    exceptionalHoliday(LocalDate.of(2002, Month.JUNE, 4)),
                    exceptionalHoliday(LocalDate.of(2011, Month.APRIL, 29)),
                    PublicHolidayExceptions
                            .exceptionalNonHoliday(
                                    instanceOfDayInMonth(1, DayOfWeek.MONDAY, Month.MAY, 2020)),
                    exceptionalHoliday(LocalDate.of(2020, Month.MAY, 8)));

    public final LocalDate date;
    public final boolean isHoliday;

    private static PublicHolidayExceptions exceptionalNonHoliday(LocalDate date)
    {
        return new PublicHolidayExceptions(date, false);
    }

    private static PublicHolidayExceptions exceptionalHoliday(LocalDate date)
    {
        return new PublicHolidayExceptions(date, true);
    }

    private PublicHolidayExceptions(LocalDate date, boolean isHoliday)
    {
        this.date = date;
        this.isHoliday = isHoliday;
    }

    public static LocalDate[] exceptions(int year, boolean isHoliday)
    {
        return instances
                .stream()
                .filter(x -> x.isHoliday == isHoliday)
                .map(x -> x.date)
                .filter(d -> d.getYear() == year)
                .toArray(LocalDate[]::new);
    }
}