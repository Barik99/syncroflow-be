package vlad.mester.syncroflowbe.Triggers;

import lombok.Getter;
import vlad.mester.syncroflowbe.Enums.TypesOfTriggers;
import vlad.mester.syncroflowbe.base.Triggers;

import java.io.File;
import java.io.IOException;

@Getter
public class ExternalProgram extends Triggers {
    private final File externalProgram;
    private final String commandLineArguments;
    private final int exitStatus;

    public ExternalProgram(String name, File externalProgram, String commandLineArguments, int exitStatus) {
        super(name, TypesOfTriggers.EXTERNAL_PROGRAM.name(), "Program: " + externalProgram.getName() +
                "/Arguments: " + commandLineArguments + "/Exit Status: " + exitStatus);
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
}
