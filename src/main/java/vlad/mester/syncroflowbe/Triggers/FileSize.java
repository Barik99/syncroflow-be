package vlad.mester.syncroflowbe.Triggers;

import lombok.Getter;
import vlad.mester.syncroflowbe.Enums.TypesOfTriggers;
import vlad.mester.syncroflowbe.base.Triggers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
public class FileSize extends Triggers {
    private final File file;
    private final long sizeThreshold;

    public FileSize(String name, File file, long sizeThreshold) {
        super(name, TypesOfTriggers.FILE_SIZE.name(), file.getName() + "/Size Threshold " + sizeThreshold);
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
}
