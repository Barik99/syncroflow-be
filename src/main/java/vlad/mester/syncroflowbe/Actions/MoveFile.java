package vlad.mester.syncroflowbe.Actions;

import lombok.Getter;
import org.json.simple.JSONObject;
import vlad.mester.syncroflowbe.base.Actions;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Getter
public class MoveFile extends Actions {
    private final File fileToMove;
    private final File destinationPath;
    public static final String type = "Move File";

    public MoveFile(String name, File fileToMove, File destinationPath) {
        super(name, type, "File: " + fileToMove.getName() + "/DestinationPath: " + destinationPath.getAbsolutePath());
        this.fileToMove = fileToMove;
        this.destinationPath = destinationPath;
    }

    @Override
    public boolean execute() {
        try {
            Path source = fileToMove.toPath();
            Path destination = destinationPath.toPath();
            Files.move(source, destination.resolve(fileToMove.getName()), StandardCopyOption.REPLACE_EXISTING);
            Files.deleteIfExists(source);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject action = super.getJSONObject();
        action.put("fileToMove", this.fileToMove.getAbsolutePath());
        action.put("destinationPath", this.destinationPath.getAbsolutePath());
        return action;
    }
}
