package vlad.mester.syncroflowbe.Actions;

import lombok.Getter;
import org.json.simple.JSONObject;
import vlad.mester.syncroflowbe.base.Actions;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Getter
public class PasteFile extends Actions {
    private final File fileToPaste;
    private final File destinationPath;
    public static final String type = "Paste File";

    public PasteFile(String name, File fileToPaste, File destinationPath) {
        super(name, type, "Lipește fișierul " + fileToPaste.getAbsolutePath() + " în folderul " + destinationPath.getName());
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

    @Override
    public JSONObject getJSONObject() {
        JSONObject action = super.getJSONObject();
        action.put("fileToPaste", this.fileToPaste.getAbsolutePath());
        action.put("destinationPath", this.destinationPath.getAbsolutePath());
        return action;
    }
}
