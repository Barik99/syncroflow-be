package vlad.mester.syncroflowbe.Actions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteFileTest {

    @TempDir
    public File tempDir;

    @Test
    public void testDeleteFile_Success() throws IOException {
        // Create a temporary file
        File testFile = new File(tempDir, "test_file.txt");
        testFile.createNewFile();

        // Create a DeleteFile action
        DeleteFile deleteFile = new DeleteFile("Test Delete", testFile);

        // Execute the action
        assertTrue(deleteFile.execute());

        // Assert that the file is deleted
        assertFalse(testFile.exists());
    }

    @Test
    public void testDeleteFile_Exception() {
        // Mock a situation where file deletion throws an exception
        File testFile = new File(tempDir, "test_file.txt");
        DeleteFile deleteFile = new DeleteFile("Test Delete", testFile) {
            @Override
            public boolean execute() {
                throw new RuntimeException("Simulated exception");
            }
        };

        // Execute the action. Expect a failure
        assertThrows(RuntimeException.class, deleteFile::execute);
    }
}