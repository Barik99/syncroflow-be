package vlad.mester.syncroflowbe.base;

import lombok.Data;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import vlad.mester.syncroflowbe.Actions.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Data
public abstract class Actions {
    private String name;
    private String type;
    private String value;

    public Actions(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public static Actions fromJSONObject(String jsonAction, String id) throws IOException {
        JSONObject action = (JSONObject) JSONValue.parse(jsonAction);
        String name = (String) action.get("name");
        String type = (String) action.get("type");
        switch (type) {
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
            case SendEmail.type:
                return new SendEmail(name, (String) action.get("receiver"), (String) action.get("subject"), (String) action.get("body"));
            default:
                throw new IOException("Invalid action type");
        }
    }

    public static String getAllActionTypes() {
        Map<String, Map<String, String>> actionsTypes = new HashMap<>();
        actionsTypes.put(StartExternalProgram.type, Map.of("externalProgram", "String", "commandLineArguments", "String"));
        actionsTypes.put(DeleteFile.type, Map.of("fileToDelete", "String"));
        actionsTypes.put(MoveFile.type, Map.of("fileToMove", "String", "destinationPath", "String"));
        actionsTypes.put(PasteFile.type, Map.of("fileToPaste", "String", "destinationPath", "String"));
        actionsTypes.put(AppendStringToFile.type, Map.of("stringToAppend", "String", "file", "String"));
        actionsTypes.put(CombinedActions.type, Map.of("firstAction", "String", "secondAction", "String"));
        actionsTypes.put(SendEmail.type, Map.of("receiver", "String", "subject", "String", "body", "String"));
        return new JSONObject(actionsTypes).toJSONString();
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Actions actions) {
            return this.name.equals(actions.name);
        }
        return false;
    }

    public abstract boolean execute();

    public JSONObject getJSONObject() {
        JSONObject action = new JSONObject();
        action.put("name", this.name);
        action.put("type", this.type);
        action.put("value", this.value);
        return action;
    }
}
