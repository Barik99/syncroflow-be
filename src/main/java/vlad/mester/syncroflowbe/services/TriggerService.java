package vlad.mester.syncroflowbe.services;

import org.springframework.stereotype.Service;
import vlad.mester.syncroflowbe.Triggers.*;
import vlad.mester.syncroflowbe.base.Triggers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TriggerService {
    private static final String url = "jdbc:postgresql://localhost:5432/syncroflowdb";
    // Database credentials
    private static final String username = "postgres";
    private static final String password = "admin";

    public boolean addTrigger(Triggers trigger, String email) {
        if (checkIfTriggerExists(trigger.getName())) {
            return true;
        }
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            List<PreparedStatement> preparedStatement = prepareStatementTrigger(trigger, connection, email);
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

    private List<PreparedStatement> prepareStatementTrigger(Triggers trigger, Connection connection, String email) throws SQLException {
        List<PreparedStatement> preparedStatement = new ArrayList<>();
        PreparedStatement actionPreparedStatement = connection.prepareStatement("INSERT INTO triggers (name, type, value, creator_email) VALUES (?, ?, ?, ?)");
        actionPreparedStatement.setString(1, trigger.getName());
        actionPreparedStatement.setString(2, trigger.getType());
        actionPreparedStatement.setString(3, trigger.getValue());
        actionPreparedStatement.setString(4, email);
        preparedStatement.add(actionPreparedStatement);
        PreparedStatement typeActionPreparedStatement = null;
        switch (trigger.getType()) {
            case FileSize.type:
                FileSize fileSize = (FileSize) trigger;
                typeActionPreparedStatement = connection.prepareStatement("INSERT INTO filesize_trigger (name, file_path, sizethreshold) VALUES (?, ?, ?)");
                typeActionPreparedStatement.setString(1, fileSize.getName());
                typeActionPreparedStatement.setString(2, fileSize.getFile().getAbsolutePath());
                typeActionPreparedStatement.setLong(3, fileSize.getSizeThreshold());
                break;
            case FileExistence.type:
                FileExistence fileExistence = (FileExistence) trigger;
                typeActionPreparedStatement = connection.prepareStatement("INSERT INTO fileexistence_trigger (name, file_path) VALUES (?, ?)");
                typeActionPreparedStatement.setString(1, fileExistence.getName());
                typeActionPreparedStatement.setString(2, fileExistence.getFile().getAbsolutePath());
                break;
            case ExternalProgram.type:
                ExternalProgram externalProgram = (ExternalProgram) trigger;
                typeActionPreparedStatement = connection.prepareStatement("INSERT INTO externalprogram_trigger (name, externalprogram, commandlinearguments, exitstatus) VALUES (?, ?, ?, ?)");
                typeActionPreparedStatement.setString(1, externalProgram.getName());
                typeActionPreparedStatement.setString(2, externalProgram.getExternalProgram().getAbsolutePath());
                typeActionPreparedStatement.setString(3, externalProgram.getCommandLineArguments());
                typeActionPreparedStatement.setInt(4, externalProgram.getExitStatus());
                break;
            case AND.type:
                AND and = (AND) trigger;
                typeActionPreparedStatement = connection.prepareStatement("INSERT INTO and_trigger (name, first_trigger_name, second_trigger_name) VALUES (?, ?, ?)");
                typeActionPreparedStatement.setString(1, and.getName());
                typeActionPreparedStatement.setString(2, and.getFirstTrigger());
                typeActionPreparedStatement.setString(3, and.getSecondTrigger());
                break;
            case OR.type:
                OR or = (OR) trigger;
                typeActionPreparedStatement = connection.prepareStatement("INSERT INTO or_trigger (name, first_trigger_name, second_trigger_name) VALUES (?, ?, ?)");
                typeActionPreparedStatement.setString(1, or.getName());
                typeActionPreparedStatement.setString(2, or.getFirstTrigger());
                typeActionPreparedStatement.setString(3, or.getSecondTrigger());
                break;
            case NOT.type:
                NOT not = (NOT) trigger;
                typeActionPreparedStatement = connection.prepareStatement("INSERT INTO not_trigger (name, trigger_name) VALUES (?, ?)");
                typeActionPreparedStatement.setString(1, not.getName());
                typeActionPreparedStatement.setString(2, not.getTrigger());
                break;
            case DayOfMonth.type:
                DayOfMonth dayOfMonth = (DayOfMonth) trigger;
                typeActionPreparedStatement = connection.prepareStatement("INSERT INTO dayofmonth_trigger (name, day) VALUES (?, ?)");
                typeActionPreparedStatement.setString(1, dayOfMonth.getName());
                typeActionPreparedStatement.setInt(2, dayOfMonth.getDay());
                break;
            case DayOfWeek.type:
                DayOfWeek dayOfWeek = (DayOfWeek) trigger;
                typeActionPreparedStatement = connection.prepareStatement("INSERT INTO dayofweek_trigger (name, day) VALUES (?, ?)");
                typeActionPreparedStatement.setString(1, dayOfWeek.getName());
                typeActionPreparedStatement.setString(2, dayOfWeek.getDay());
                break;
            case TimeOfDay.type:
                TimeOfDay timeOfDay = (TimeOfDay) trigger;
                typeActionPreparedStatement = connection.prepareStatement("INSERT INTO timeofday_trigger (name, hours, minutes) VALUES (?, ?, ?)");
                typeActionPreparedStatement.setString(1, timeOfDay.getName());
                typeActionPreparedStatement.setInt(2, timeOfDay.getHours());
                typeActionPreparedStatement.setInt(3, timeOfDay.getMinutes());
                break;
        }
        preparedStatement.add(typeActionPreparedStatement);
        return preparedStatement;
    }

    public boolean deleteTrigger(String triggerName) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM triggers WHERE name = ?");
            preparedStatement.setString(1, triggerName);
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkIfTriggerExists(String triggerName) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM triggers WHERE name = ?");
            preparedStatement.setString(1, triggerName);
            preparedStatement.executeQuery();
            connection.close();
            return preparedStatement.getResultSet().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}