package vlad.mester.syncroflowbe.Triggers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ExternalProgramTest {
    private ExternalProgram externalProgram;
    private File file;

    @BeforeEach
    void setUp() {
        file = new File("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
        externalProgram = new ExternalProgram("Test", file, "", 0);
    }

    @Test
    void shouldEvaluateTrueWhenExternalProgramExitsWithExpectedStatus() {
        assertTrue(externalProgram.evaluate());
    }

    @Test
    void shouldEvaluateFalseWhenExternalProgramDoesNotExist() {
        externalProgram = new ExternalProgram("Test", new File("nonExistentProgram.exe"), "", 0);
        assertFalse(externalProgram.evaluate());
    }

    @Test
    void shouldEvaluateFalseWhenExternalProgramExitsWithUnexpectedStatus() {
        externalProgram = new ExternalProgram("Test", file, "", 1);
        assertFalse(externalProgram.evaluate());
    }
}