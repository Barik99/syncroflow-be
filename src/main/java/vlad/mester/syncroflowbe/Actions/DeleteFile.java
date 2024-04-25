package vlad.mester.syncroflowbe.Actions;

import lombok.Getter;
import org.json.simple.JSONObject;
import vlad.mester.syncroflowbe.base.Actions;

import java.io.File;

@Getter
public class DeleteFile extends Actions {
    private final File fileToDelete;
    public static final String type = "Delete File";

    public DeleteFile(String name, File fileToDelete) {
        super(name, type, "File: " + fileToDelete.getName());
        this.fileToDelete = fileToDelete;
    }

    @Override
    public boolean execute() {
        try{
            return fileToDelete.delete();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject action = super.getJSONObject();
        action.put("fileToDelete", this.fileToDelete.getAbsolutePath());
        return action;
    }
}
