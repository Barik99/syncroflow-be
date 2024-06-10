package vlad.mester.syncroflowbe.services;

import org.springframework.stereotype.Service;
import vlad.mester.syncroflowbe.Actions.*;
import vlad.mester.syncroflowbe.base.Actions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActionService {
    // Database URL

    public boolean addAction(Actions action, String email) {
        if (checkIfActionExists(action.getName())) {
            return true;
        }
        try {
            Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
            List<PreparedStatement> preparedStatement = prepareStatementAction(action, connection, email);
            for (PreparedStatement statement : preparedStatement) {
                statement.executeUpdate();
            }
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // create a function that gets as input an action and returns the SQL string used to insert the action in the database based on the type of the action
    private List<PreparedStatement> prepareStatementAction(Actions action, Connection connection, String email) throws SQLException {
        List<PreparedStatement> preparedStatement = new ArrayList<>();
        PreparedStatement actionPreparedStatement = connection.prepareStatement("INSERT INTO actions (name, type, value, creator_email) VALUES (?, ?, ?, ?)");
        actionPreparedStatement.setString(1, action.getName());
        actionPreparedStatement.setString(2, action.getType());
        actionPreparedStatement.setString(3, action.getValue());
        actionPreparedStatement.setString(4, email);
        preparedStatement.add(actionPreparedStatement);
        PreparedStatement typeActionPreparedStatement = null;
        switch (action.getType()) {
            case DeleteFile.type:
                DeleteFile deleteFile = (DeleteFile) action;
                typeActionPreparedStatement = connection.prepareStatement("INSERT INTO deletefile_action (name, filetodelete_path) VALUES (?, ?)");
                typeActionPreparedStatement.setString(1, deleteFile.getName());
                typeActionPreparedStatement.setString(2, deleteFile.getFileToDelete().getAbsolutePath());
                break;
            case MoveFile.type:
                MoveFile moveFile = (MoveFile) action;
                typeActionPreparedStatement = connection.prepareStatement("INSERT INTO movefile_action (name, filetomove_path, destinationpath_path) VALUES (?, ?, ?)");
                typeActionPreparedStatement.setString(1, moveFile.getName());
                typeActionPreparedStatement.setString(2, moveFile.getFileToMove().getAbsolutePath());
                typeActionPreparedStatement.setString(3, moveFile.getDestinationPath().getAbsolutePath());
                break;
            case PasteFile.type:
                PasteFile pasteFile = (PasteFile) action;
                typeActionPreparedStatement = connection.prepareStatement("INSERT INTO pastefile_action (name, filetopaste_path, destinationpath_path) VALUES (?, ?, ?)");
                typeActionPreparedStatement.setString(1, pasteFile.getName());
                typeActionPreparedStatement.setString(2, pasteFile.getFileToPaste().getAbsolutePath());
                typeActionPreparedStatement.setString(3, pasteFile.getDestinationPath().getAbsolutePath());
                break;
            case StartExternalProgram.type:
                StartExternalProgram startExternalProgram = (StartExternalProgram) action;
                typeActionPreparedStatement = connection.prepareStatement("INSERT INTO startexternalprogram_action (name, externalprogram, commandlinearguments) VALUES (?, ?, ?)");
                typeActionPreparedStatement.setString(1, startExternalProgram.getName());
                typeActionPreparedStatement.setString(2, startExternalProgram.getExternalProgram().getAbsolutePath());
                typeActionPreparedStatement.setString(3, startExternalProgram.getCommandLineArguments());
                break;
            case AppendStringToFile.type:
                AppendStringToFile appendStringToFile = (AppendStringToFile) action;
                typeActionPreparedStatement = connection.prepareStatement("INSERT INTO appendstringtofile_action (name, message, file_path) VALUES (?, ?, ?)");
                typeActionPreparedStatement.setString(1, appendStringToFile.getName());
                typeActionPreparedStatement.setString(2, appendStringToFile.getStringToAppend());
                typeActionPreparedStatement.setString(3, appendStringToFile.getFile().getAbsolutePath());
                break;
            case CombinedActions.type:
                CombinedActions combinedAction = (CombinedActions) action;
                typeActionPreparedStatement = connection.prepareStatement("INSERT INTO combinedactions_action (name, first_action_name, second_action_name) VALUES (?, ?, ?)");
                typeActionPreparedStatement.setString(1, combinedAction.getName());
                typeActionPreparedStatement.setString(2, combinedAction.getFirstAction());
                typeActionPreparedStatement.setString(3, combinedAction.getSecondAction());
                break;
        }
        preparedStatement.add(typeActionPreparedStatement);
        return preparedStatement;
    }

    public boolean deleteAction(String actionName) {
        try {
            Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM actions WHERE name = ?");
            preparedStatement.setString(1, actionName);
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkIfActionExists(String actionName) {
        try {
            Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM actions WHERE name = ?");
            preparedStatement.setString(1, actionName);
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
