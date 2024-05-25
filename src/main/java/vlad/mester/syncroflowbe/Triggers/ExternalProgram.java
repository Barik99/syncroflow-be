package vlad.mester.syncroflowbe.Triggers;

import lombok.Getter;
import org.json.simple.JSONObject;
import vlad.mester.syncroflowbe.base.Triggers;

import java.io.File;
import java.io.IOException;

@Getter
public class ExternalProgram extends Triggers {
    private final File externalProgram;
    private final String commandLineArguments;
    private final int exitStatus;
    public static final String type = "External Program";

    public ExternalProgram(String name, File externalProgram, String commandLineArguments, int exitStatus) {
        super(name, type, "Programul extern " + externalProgram.getAbsoluteFile() + " este executat cu argumentele "
                + commandLineArguments + " și are exit status " + exitStatus);
        this.externalProgram = externalProgram;
        this.commandLineArguments = commandLineArguments;
        this.exitStatus = exitStatus;
    }

    @Override
    public boolean evaluate() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(externalProgram.getAbsolutePath());
        processBuilder.command().addAll(java.util.Arrays.asList(commandLineArguments.split(" ")));
        try {
            Process process = processBuilder.start();
            return process.waitFor() == exitStatus;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject trigger = super.getJSONObject();
        trigger.put("externalProgram", this.externalProgram.getAbsolutePath());
        trigger.put("commandLineArguments", this.commandLineArguments);
        trigger.put("exitStatus", this.exitStatus);
        return trigger;
    }
}
