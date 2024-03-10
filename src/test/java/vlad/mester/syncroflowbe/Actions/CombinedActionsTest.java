package vlad.mester.syncroflowbe.Actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class CombinedActionsTest {
    private CombinedActions combinedActions;
    private AppendStringToFile appendStringToFileAction;
    private MoveFile moveFileAction;
    private File sourceFile;
    private File destinationFile;

    @BeforeEach
    void setUp() {
        sourceFile = new File("C:\\source.txt");
        destinationFile = new File("C:\\destination.txt");
        appendStringToFileAction = new AppendStringToFile("Test","Hello, World!", sourceFile);
        moveFileAction = new MoveFile("Test", sourceFile, destinationFile);
        combinedActions = new CombinedActions("Test", appendStringToFileAction, moveFileAction);
    }

    @Test
    void shouldExecuteSuccessfullyWhenBothActionsExecuteSuccessfully() {
        assertTrue(combinedActions.execute());
    }
}