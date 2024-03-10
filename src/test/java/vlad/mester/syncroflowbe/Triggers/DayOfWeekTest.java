package vlad.mester.syncroflowbe.Triggers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vlad.mester.syncroflowbe.Enums.DaysOfWeekEnum;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DayOfWeekTest {
    private DayOfWeek dayOfWeek;

    @BeforeEach
    void setUp() {
        dayOfWeek = new DayOfWeek("Test", DaysOfWeekEnum.valueOf(LocalDate.now().getDayOfWeek().toString().toUpperCase()));
    }

    @Test
    void shouldEvaluateTrueWhenDayMatches() {
        assertTrue(dayOfWeek.evaluate());
    }

    @Test
    void shouldEvaluateFalseWhenDayDoesNotMatch() {
        DayOfWeek differentDayOfWeek = new DayOfWeek("Test", DaysOfWeekEnum.MONDAY);
        if (LocalDate.now().getDayOfWeek().toString().equalsIgnoreCase("SATURDAY")) {
            assertFalse(differentDayOfWeek.evaluate());
        } else {
            assertTrue(differentDayOfWeek.evaluate());
        }
    }
}