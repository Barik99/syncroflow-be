package vlad.mester.syncroflowbe.Actions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StartExternalProgramTest {

    @TempDir
    public File tempDir;

    @Test
    void shouldStartExternalProgramSuccessfully() {
        File externalProgram = new File("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");

        StartExternalProgram startExternalProgram = new StartExternalProgram("Test Start", externalProgram, "");

        assertTrue(startExternalProgram.execute());
    }

    @Test
    void shouldNotStartExternalProgramWhenFileDoesNotExist() {
        File externalProgram = new File(tempDir, "nonExistentProgram.exe");
        String commandLineArguments = "--arg1 --arg2";

        StartExternalProgram startExternalProgram = new StartExternalProgram("Test Start", externalProgram, commandLineArguments);

        assertFalse(startExternalProgram.execute());
    }

}