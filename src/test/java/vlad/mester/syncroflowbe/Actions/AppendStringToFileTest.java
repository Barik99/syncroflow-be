package vlad.mester.syncroflowbe.Actions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AppendStringToFileTest {

    @TempDir
    public File tempDir;

    @Test
    public void testAppendStringToFile_Success() throws IOException {
        // Create a temporary file
        File testFile = new File(tempDir, "test_file.txt");
        testFile.createNewFile();

        String messageToAppend = "This is a test message.";

        // Create an AppendStringToFile action
        AppendStringToFile appendAction = new AppendStringToFile("Test Append", messageToAppend, testFile);

        // Execute the action
        assertTrue(appendAction.execute());

        // Read the file contents and assert that the message was appended
        String fileContents = new String(java.nio.file.Files.readAllBytes(testFile.toPath()));
        assertEquals(messageToAppend, fileContents.trim());
    }

    @Test
    public void testAppendStringToFile_Failure() throws IOException {
        // Create a temporary file that cannot be written to (e.g., set read-only)
        File testFile = new File(tempDir, "test_file.txt");
        testFile.createNewFile();
        testFile.setReadOnly();

        String messageToAppend = "This message should not be appended.";

        // Create an AppendStringToFile action
        AppendStringToFile appendAction = new AppendStringToFile("Test Append", messageToAppend, testFile);

        // Execute the action. Expect a failure
        assertFalse(appendAction.execute());

        // Assert that the file contents remain unchanged
        assertEquals(0, testFile.length());
    }
}