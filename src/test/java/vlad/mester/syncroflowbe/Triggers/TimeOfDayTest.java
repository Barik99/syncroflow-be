package vlad.mester.syncroflowbe.Triggers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeOfDayTest {
    private TimeOfDay timeOfDay;

    @BeforeEach
    void setUp() {
        timeOfDay = new TimeOfDay("Test", LocalTime.now().getHour(), LocalTime.now().getMinute());
    }

    @Test
    void shouldEvaluateTrueWhenTimeMatches() {
        assertTrue(timeOfDay.evaluate());
    }

    @Test
    void shouldEvaluateFalseWhenTimeDoesNotMatch() {
        TimeOfDay differentTimeOfDay = new TimeOfDay("Test", LocalTime.now().getHour() + 1, LocalTime.now().getMinute());
        assertFalse(differentTimeOfDay.evaluate());
    }
}