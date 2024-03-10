package vlad.mester.syncroflowbe.Actions;

import lombok.Getter;
import vlad.mester.syncroflowbe.Enums.TypesOfAction;
import vlad.mester.syncroflowbe.base.Actions;

import java.io.File;

@Getter
public class DeleteFile extends Actions {
    private final File fileToDelete;

    public DeleteFile(String name, File fileToDelete) {
        super(name, TypesOfAction.DELETE_FILE.name(), "File: " + fileToDelete.getName());
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
}
