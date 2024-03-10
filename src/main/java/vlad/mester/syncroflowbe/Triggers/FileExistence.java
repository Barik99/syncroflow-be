package vlad.mester.syncroflowbe.Triggers;

import lombok.Getter;
import vlad.mester.syncroflowbe.Enums.TypesOfTriggers;
import vlad.mester.syncroflowbe.base.Triggers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Getter
public class FileExistence extends Triggers {
    private final File file;

    public FileExistence(String name, File file) {
        super(name, TypesOfTriggers.FILE_EXISTENCE.name(), "File: " + file.getName() + "/Directory: " + file.getAbsoluteFile());
        this.file = file;
    }

    @Override
    public boolean evaluate() {
        return Files.exists(Paths.get(file.getAbsolutePath()));
    }
}
