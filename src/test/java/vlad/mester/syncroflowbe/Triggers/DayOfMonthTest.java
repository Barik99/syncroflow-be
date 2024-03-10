package vlad.mester.syncroflowbe.Triggers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DayOfMonthTest {
    private DayOfMonth dayOfMonth;

    @BeforeEach
    void setUp() {
        dayOfMonth = new DayOfMonth("Test", LocalDate.now().getDayOfMonth());
    }

    @Test
    void shouldEvaluateTrueWhenDayMatches() {
        assertTrue(dayOfMonth.evaluate());
    }

    @Test
    void shouldEvaluateFalseWhenDayDoesNotMatch() {
        DayOfMonth differentDayOfMonth = new DayOfMonth("Test", LocalDate.now().getDayOfMonth() + 1);
        assertFalse(differentDayOfMonth.evaluate());
    }
}