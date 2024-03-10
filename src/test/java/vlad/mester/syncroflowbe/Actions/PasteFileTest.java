package vlad.mester.syncroflowbe.Actions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasteFileTest {

    @TempDir
    public File tempDir;

    @Test
    void shouldPasteFileSuccessfully() throws IOException {
        File sourceFile = createFileInTempDir("source.txt");
        File destinationDir = createDirInTempDir("destination");

        PasteFile pasteFile = new PasteFile("Test Paste", sourceFile, destinationDir);

        assertTrue(pasteFile.execute());
        assertTrue(new File(destinationDir, sourceFile.getName()).exists());
    }

    @Test
    void shouldNotPasteFileWhenSourceDoesNotExist() {
        File sourceFile = new File(tempDir, "nonExistentSource.txt");
        File destinationDir = createDirInTempDir("destination");

        PasteFile pasteFile = new PasteFile("Test Paste", sourceFile, destinationDir);

        assertFalse(pasteFile.execute());
    }

    private File createFileInTempDir(String fileName) throws IOException {
        File file = new File(tempDir, fileName);
        file.createNewFile();
        return file;
    }

    private File createDirInTempDir(String dirName) {
        File dir = new File(tempDir, dirName);
        dir.mkdir();
        return dir;
    }
}