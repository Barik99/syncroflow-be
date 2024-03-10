package vlad.mester.syncroflowbe.Triggers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ANDTest {
    private AND andTrigger;
    private TimeOfDay timeOfDayTrigger;
    private ExternalProgram externalProgramTrigger;
    private File file;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        file = new File("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
        timeOfDayTrigger = new TimeOfDay("Test", now.getHour(), now.getMinute());
        externalProgramTrigger = new ExternalProgram("Test", file, "", 0);
        andTrigger = new AND("Test", timeOfDayTrigger, externalProgramTrigger);
    }

    @Test
    void shouldEvaluateTrueWhenTimeIsWithinRangeAndExternalProgramExitsWithExpectedStatus() {
        assertTrue(andTrigger.evaluate());
    }

    @Test
    void shouldEvaluateFalseWhenTimeIsNotWithinRange() {
        timeOfDayTrigger = new TimeOfDay("Test", now.getHour()+1, now.getMinute());
        andTrigger = new AND("Test", timeOfDayTrigger, externalProgramTrigger);
        assertFalse(andTrigger.evaluate());
    }

    @Test
    void shouldEvaluateFalseWhenExternalProgramDoesNotExist() {
        externalProgramTrigger = new ExternalProgram("Test", new File("nonExistentProgram.exe"), "", 0);
        andTrigger = new AND("Test", timeOfDayTrigger, externalProgramTrigger);
        assertFalse(andTrigger.evaluate());
    }

    @Test
    void shouldEvaluateFalseWhenExternalProgramExitsWithUnexpectedStatus() {
        externalProgramTrigger = new ExternalProgram("Test", file, "", 1);
        andTrigger = new AND("Test", timeOfDayTrigger, externalProgramTrigger);
        assertFalse(andTrigger.evaluate());
    }
}