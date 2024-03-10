package vlad.mester.syncroflowbe.Actions;

import lombok.Getter;
import vlad.mester.syncroflowbe.Enums.TypesOfAction;
import vlad.mester.syncroflowbe.base.Actions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Getter
public class AppendStringToFile extends Actions {
    private final String message;
    private final File file;

    public AppendStringToFile(String name, String message, File file) {
        super(name, TypesOfAction.APPEND_STRING_TO_FILE.name(), "File: " + file.getName() + "/message to append: " + message);
        this.message = message;
        this.file = file;
    }

    @Override
    public boolean execute() {
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(message + "\n");
            fileWriter.close();
            return true;
        } catch (IOException e) {
            System.out.println("An error occured.");
            e.printStackTrace();
            return false;
        }
    }
}
