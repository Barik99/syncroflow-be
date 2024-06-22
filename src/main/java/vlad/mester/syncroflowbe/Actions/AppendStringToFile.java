package vlad.mester.syncroflowbe.Actions;

import lombok.Getter;
import org.json.simple.JSONObject;
import vlad.mester.syncroflowbe.base.Actions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Getter
public class AppendStringToFile extends Actions {
    public static final String type = "Append String To File";
    private final String stringToAppend;
    private final File file;

    public AppendStringToFile(String name, String stringToAppend, File file) {
        super(name, type, "Adaugă șirul de caractere " + stringToAppend + " în fișierul " + file.getAbsolutePath());
        this.stringToAppend = stringToAppend;
        this.file = file;
    }

    @Override
    public boolean execute() {
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(stringToAppend + "\n");
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject action = super.getJSONObject();
        action.put("stringToAppend", this.stringToAppend);
        action.put("file", this.file.getAbsolutePath());
        return action;
    }
}
