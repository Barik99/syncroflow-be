package vlad.mester.syncroflowbe.base;

import lombok.Data;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import vlad.mester.syncroflowbe.Actions.*;

import java.io.File;
import java.io.IOException;


@Data
public abstract class Actions {
    private String name; // This is a string that contains the name of the action. It can be "Start Notepad", "Delete File", etc.
    private String type; // This is a string that contains the type of the action. It can be "StartExternalProgram", "DeleteFile", etc.
    private String value; // This is a string that contains the value of the action. It can be a file path, a command line argument, etc.

    public Actions(String name, String type, String value){
        this.name = name;
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Actions actions){
            return this.name.equals(actions.name);
        }
        return false;
    }

    public abstract boolean execute();

    public boolean isActionUsedIn(){
        boolean result = false;
        return result;
    }

    public JSONObject getJSONObject() {
        JSONObject action = new JSONObject();
        action.put("name", this.name);
        action.put("type", this.type);
        action.put("value", this.value);
        return action;
    }

    public static Actions fromJSONObject(String jsonAction, String id) throws IOException {
        JSONObject action = (JSONObject) JSONValue.parse(jsonAction);
        String name = (String) action.get("name");
        String type = (String) action.get("type");
        switch (type){
            case StartExternalProgram.type:
                return new StartExternalProgram(name, new File((String) action.get("externalProgram")), (String) action.get("commandLineArguments"));
            case DeleteFile.type:
                return new DeleteFile(name, new File((String) action.get("fileToDelete")));
            case MoveFile.type:
                return new MoveFile(name, new File((String) action.get("fileToMove")), new File((String) action.get("destinationPath")));
            case PasteFile.type:
                return new PasteFile(name, new File((String) action.get("fileToPaste")), new File((String) action.get("destinationPath")));
            case AppendStringToFile.type:
                return new AppendStringToFile(name, (String) action.get("stringToAppend"), new File((String) action.get("file")));
            case CombinedActions.type:
                return new CombinedActions(name, (String) action.get("firstAction"), (String) action.get("secondAction"), id);
            default:
                throw new IOException("Invalid action type");
        }
    }
}
