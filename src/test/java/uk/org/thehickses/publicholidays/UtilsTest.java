package uk.org.thehickses.publicholidays;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UtilsTest
{
    @ParameterizedTest
    @MethodSource
    void testDateOfEaster(int year, String expectedDate)
    {
        LocalDate result = Utils.dateOfEaster(year);
        assertThat(String.format("%s/%d", expectedDate, year))
                .isEqualTo(result.format(DateTimeFormatter.ofPattern("d/M/yyyy")));
    }

    static Stream<Arguments> testDateOfEaster()
    {
        return Stream
                .of(arguments(2000, "23/4"), arguments(2001, "15/4"), arguments(2002, "31/3"),
                        arguments(2003, "20/4"), arguments(2004, "11/4"), arguments(2005, "27/3"),
                        arguments(2006, "16/4"), arguments(2007, "8/4"), arguments(2008, "23/3"),
                        arguments(2009, "12/4"), arguments(2010, "4/4"), arguments(2011, "24/4"),
                        arguments(2012, "8/4"), arguments(2013, "31/3"), arguments(2014, "20/4"),
                        arguments(2015, "5/4"), arguments(2016, "27/3"), arguments(2017, "16/4"),
                        arguments(2018, "1/4"), arguments(2019, "21/4"), arguments(2020, "12/4"),
                        arguments(2021, "4/4"), arguments(2022, "17/4"), arguments(2023, "9/4"),
                        arguments(2024, "31/3"), arguments(2025, "20/4"), arguments(2026, "5/4"),
                        arguments(2027, "28/3"), arguments(2028, "16/4"), arguments(2029, "1/4"),
                        arguments(2030, "21/4"), arguments(2031, "13/4"), arguments(2032, "28/3"),
                        arguments(2033, "17/4"), arguments(2034, "9/4"), arguments(2035, "25/3"),
                        arguments(2036, "13/4"), arguments(2037, "5/4"), arguments(2038, "25/4"),
                        arguments(2039, "10/4"), arguments(2040, "1/4"));
    }
    
    @ParameterizedTest
    @MethodSource
    void testInstanceOfDayInMonth(int year, Month month, DayOfWeek day, int no,
            LocalDate expectedResult)
    {
        LocalDate result = Utils.instanceOfDayInMonth(no, day, month, year);
        assertThat(result).isEqualTo(expectedResult);
    }

    static Stream<Arguments> testInstanceOfDayInMonth()
    {
        return Stream
                .of(arguments(2020, Month.JANUARY, DayOfWeek.WEDNESDAY, 1,
                        LocalDate.of(2020, Month.JANUARY, 1)),
                        arguments(2020, Month.JANUARY, DayOfWeek.WEDNESDAY, 3,
                                LocalDate.of(2020, Month.JANUARY, 15)),
                        arguments(2020, Month.JANUARY, DayOfWeek.FRIDAY, 1,
                                LocalDate.of(2020, Month.JANUARY, 3)),
                        arguments(2020, Month.JANUARY, DayOfWeek.MONDAY, 1,
                                LocalDate.of(2020, Month.JANUARY, 6)),
                        arguments(2020, Month.JANUARY, DayOfWeek.FRIDAY, -1,
                                LocalDate.of(2020, Month.JANUARY, 31)),
                        arguments(2020, Month.JANUARY, DayOfWeek.FRIDAY, -2,
                                LocalDate.of(2020, Month.JANUARY, 24)),
                        arguments(2020, Month.JANUARY, DayOfWeek.TUESDAY, -1,
                                LocalDate.of(2020, Month.JANUARY, 28)),
                        arguments(2020, Month.JANUARY, DayOfWeek.SUNDAY, -1,
                                LocalDate.of(2020, Month.JANUARY, 26)),
                        arguments(2020, Month.FEBRUARY, DayOfWeek.SUNDAY, -4,
                                LocalDate.of(2020, Month.FEBRUARY, 2)));
    }
}
