package vlad.mester.syncroflowbe.Triggers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class FileSizeTest {
    private FileSize fileSize;
    private File file;

    @BeforeEach
    void setUp() throws IOException {
        file = Files.createTempFile("temp", ".txt").toFile();
        fileSize = new FileSize("Test", file, 100);
    }

    @Test
    void shouldEvaluateTrueWhenFileSizeExceedsThreshold() throws IOException {
        Files.write(file.toPath(), new byte[101]);
        assertTrue(fileSize.evaluate());
    }

    @Test
    void shouldEvaluateFalseWhenFileSizeDoesNotExceedThreshold() throws IOException {
        Files.write(file.toPath(), new byte[99]);
        assertFalse(fileSize.evaluate());
    }

    @Test
    void shouldEvaluateFalseWhenFileDoesNotExist() {
        file.delete();
        assertFalse(fileSize.evaluate());
    }
}