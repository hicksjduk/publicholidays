package uk.org.thehickses.publicholidays;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class EnglandAndWalesPublicHolidayCalculatorTest
{
    @ParameterizedTest
    @MethodSource
    void testGetPublicHolidays(int year, List<String> expected)
    {
        Stream<LocalDate> result = new EnglandAndWalesPublicHolidayCalculator()
                .getPublicHolidays(year);
        assertThat(result).containsExactly(toDates(year, expected));
    }

    LocalDate[] toDates(int year, List<String> dates)
    {
        return dates
                .stream()
                .map(d -> d + "/" + year)
                .map(d -> LocalDate.parse(d, DateTimeFormatter.ofPattern("d/M/yyyy")))
                .toArray(LocalDate[]::new);
    }

    static Stream<Arguments> testGetPublicHolidays()
    {
        return Stream
                .of(expectedHolidays(1995, "2/1", "14/4", "17/4", "8/5", "29/5", "28/8", "25/12",
                        "26/12"),
                        expectedHolidays(2002, "1/1", "29/3", "1/4", "6/5", "3/6", "4/6", "26/8",
                                "25/12", "26/12"),
                        expectedHolidays(2020, "1/1", "10/4", "13/4", "8/5", "25/5", "31/8",
                                "25/12", "28/12"),
                        expectedHolidays(2021, "1/1", "2/4", "5/4", "3/5", "31/5", "30/8", "27/12",
                                "28/12"));
    }

    static Arguments expectedHolidays(int year, String... dates)
    {
        return arguments(year, Arrays.asList(dates));
    }
}
