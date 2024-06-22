package vlad.mester.syncroflowbe.Triggers;

import lombok.Getter;
import org.json.simple.JSONObject;
import vlad.mester.syncroflowbe.base.Triggers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Getter
public class FileExistence extends Triggers {
    private final File file;
    public static final String type = "File Existence";

    public FileExistence(String name, File file) {
        super(name, type, "Fișierul " + file.getName() + " există în folderul " + file.getAbsoluteFile());
        this.file = file;
    }

    @Override
    public boolean evaluate() {
        return Files.exists(Paths.get(file.getAbsolutePath()));
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject trigger = super.getJSONObject();
        trigger.put("file", this.file.getAbsolutePath());
        return trigger;
    }
}
