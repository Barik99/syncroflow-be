package vlad.mester.syncroflowbe.Triggers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class FileExistenceTest {
    private FileExistence fileExistence;
    private File file;

    @BeforeEach
    void setUp() throws IOException {
        file = Files.createTempFile("temp", ".txt").toFile();
        fileExistence = new FileExistence("Test", file);
    }

    @Test
    void shouldEvaluateTrueWhenFileExists() {
        assertTrue(fileExistence.evaluate());
    }

    @Test
    void shouldEvaluateFalseWhenFileDoesNotExist() {
        file.delete();
        assertFalse(fileExistence.evaluate());
    }
}