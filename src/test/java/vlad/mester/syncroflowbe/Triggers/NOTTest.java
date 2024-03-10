package vlad.mester.syncroflowbe.Triggers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class NOTTest {
    private NOT notTrigger;
    private FileSize fileSizeTrigger;
    private File file;

    @BeforeEach
    void setUp() throws IOException {
        file = Files.createTempFile("temp", ".txt").toFile();
        fileSizeTrigger = new FileSize("Test", file, 100);
        notTrigger = new NOT("Test", fileSizeTrigger);
    }

    @Test
    void shouldEvaluateTrueWhenFileSizeDoesNotExceedThreshold() throws IOException {
        Files.write(file.toPath(), new byte[99]);
        assertTrue(notTrigger.evaluate());
    }

    @Test
    void shouldEvaluateFalseWhenFileSizeExceedsThreshold() throws IOException {
        Files.write(file.toPath(), new byte[101]);
        assertFalse(notTrigger.evaluate());
    }

    @Test
    void shouldEvaluateTrueWhenFileDoesNotExist() {
        file.delete();
        assertTrue(notTrigger.evaluate());
    }
}