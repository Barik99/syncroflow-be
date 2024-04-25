package vlad.mester.syncroflowbe.Actions;

import lombok.Getter;
import org.json.simple.JSONObject;
import vlad.mester.syncroflowbe.base.Actions;

import java.io.File;
import java.io.IOException;

@Getter
public class StartExternalProgram extends Actions {
    private final File externalProgram;
    private final String commandLineArguments;
    public static final String type = "Start External Program";

    public StartExternalProgram(String name, File externalProgram, String commandLineArguments) {
        super(name, type, "File: " + externalProgram.getName() + "/CommandLineArguments: " + commandLineArguments);
        this.externalProgram = externalProgram;
        this.commandLineArguments = commandLineArguments;
    }

    @Override
    public boolean execute() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command().add(externalProgram.getAbsolutePath());
            processBuilder.command().addAll(java.util.Arrays.asList(commandLineArguments.split(" ")));
            processBuilder.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject action = super.getJSONObject();
        action.put("externalProgram", this.externalProgram.getAbsolutePath());
        action.put("commandLineArguments", this.commandLineArguments);
        return action;
    }
}
