package vlad.mester.syncroflowbe.Actions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MoveFileTest {

    @TempDir
    public File tempDir;

    @Test
    void shouldMoveFileSuccessfully() throws IOException {
        File sourceFile = createFileInTempDir("source.txt");
        File destinationDir = createDirInTempDir("destination");

        MoveFile moveFile = new MoveFile("Test Move", sourceFile, destinationDir);

        assertTrue(moveFile.execute());
        assertFalse(sourceFile.exists());
        assertTrue(new File(destinationDir, sourceFile.getName()).exists());
    }

    @Test
    void shouldNotMoveFileWhenSourceDoesNotExist() {
        File sourceFile = new File(tempDir, "nonExistentSource.txt");
        File destinationDir = createDirInTempDir("destination");

        MoveFile moveFile = new MoveFile("Test Move", sourceFile, destinationDir);

        assertFalse(moveFile.execute());
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