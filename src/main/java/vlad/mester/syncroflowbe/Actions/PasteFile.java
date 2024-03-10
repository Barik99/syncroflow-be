package vlad.mester.syncroflowbe.Actions;

import lombok.Getter;
import vlad.mester.syncroflowbe.Enums.TypesOfAction;
import vlad.mester.syncroflowbe.base.Actions;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Getter
public class PasteFile extends Actions {
    private final File fileToPaste;
    private final File destinationPath;

    public PasteFile(String name, File fileToPaste, File destinationPath) {
        super(name, TypesOfAction.PASTE_FILE.name(), "File: " + fileToPaste.getName() + "/DestinationPath: " + destinationPath.getAbsolutePath());
        this.fileToPaste = fileToPaste;
        this.destinationPath = destinationPath;
    }

    @Override
    public boolean execute() {
        try {
            Path source = fileToPaste.toPath();
            Path destination = destinationPath.toPath();
            Files.copy(source, destination.resolve(fileToPaste.getName()), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
