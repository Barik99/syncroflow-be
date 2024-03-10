package vlad.mester.syncroflowbe.Triggers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ORTest {
    private OR orTrigger;
    private DayOfMonth dayOfMonthTrigger;
    private FileExistence fileExistenceTrigger;
    private File file;

    @BeforeEach
    void setUp() {
        file = new File("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
        dayOfMonthTrigger = new DayOfMonth("Test", LocalDate.now().getDayOfMonth());
        fileExistenceTrigger = new FileExistence("Test", file);
        orTrigger = new OR("Test", dayOfMonthTrigger, fileExistenceTrigger);
    }

    @Test
    void shouldEvaluateTrueWhenDayMatchesAndFileExists() {
        assertTrue(orTrigger.evaluate());
    }

    @Test
    void shouldEvaluateTrueWhenDayDoesNotMatchButFileExists() {
        dayOfMonthTrigger = new DayOfMonth("Test", LocalDate.now().getDayOfMonth() + 1);
        orTrigger = new OR("Test", dayOfMonthTrigger, fileExistenceTrigger);
        assertTrue(orTrigger.evaluate());
    }

    @Test
    void shouldEvaluateTrueWhenDayMatchesButFileDoesNotExist() {
        fileExistenceTrigger = new FileExistence("Test", new File("nonExistentFile.txt"));
        orTrigger = new OR("Test", dayOfMonthTrigger, fileExistenceTrigger);
        assertTrue(orTrigger.evaluate());
    }

    @Test
    void shouldEvaluateFalseWhenDayDoesNotMatchAndFileDoesNotExist() {
        dayOfMonthTrigger = new DayOfMonth("Test", LocalDate.now().getDayOfMonth() + 1);
        fileExistenceTrigger = new FileExistence("Test", new File("nonExistentFile.txt"));
        orTrigger = new OR("Test", dayOfMonthTrigger, fileExistenceTrigger);
        assertFalse(orTrigger.evaluate());
    }
}