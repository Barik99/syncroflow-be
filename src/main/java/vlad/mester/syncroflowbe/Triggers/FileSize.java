package vlad.mester.syncroflowbe.Triggers;

import lombok.Getter;
import org.json.simple.JSONObject;
import vlad.mester.syncroflowbe.base.Triggers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
public class FileSize extends Triggers {
    private final File file;
    private final long sizeThreshold;
    public static final String type = "File Size";

    public FileSize(String name, File file, long sizeThreshold) {
        super(name, type, "Dimesiunea fișierului " + file.getAbsolutePath() + " este mai mare de " + sizeThreshold + " bytes");
        this.file = file;
        this.sizeThreshold = sizeThreshold;
    }

    @Override
    public boolean evaluate() {
        Path path = file.getAbsoluteFile().toPath();
        try {
            long fileSize = Files.size(path);
            return fileSize > sizeThreshold;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject trigger = super.getJSONObject();
        trigger.put("file", this.file.getAbsolutePath());
        trigger.put("sizeThreshold", this.sizeThreshold);
        return trigger;
    }
}
