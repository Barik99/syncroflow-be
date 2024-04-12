package vlad.mester.syncroflowbe.services;

import vlad.mester.syncroflowbe.Actions.*;
import vlad.mester.syncroflowbe.Enums.DaysOfWeekEnum;
import vlad.mester.syncroflowbe.RuleController;
import vlad.mester.syncroflowbe.Triggers.*;
import vlad.mester.syncroflowbe.base.Actions;
import vlad.mester.syncroflowbe.base.Rule;
import vlad.mester.syncroflowbe.base.Triggers;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoadRuleControllerService {
    RuleController ruleController;

    public LoadRuleControllerService(String id) {
        ruleController = RuleController.createInstance(id);
    }

    public void load() {
        List<Triggers> triggers;
        List<Actions> actions;
        List<Rule> rules;
        triggers = getAllTriggers();
        actions = getAllActions();
        rules = getAllRules();
        for (Triggers trigger : triggers) {
            ruleController.addTrigger(trigger);
        }
        for (Actions action : actions) {
            ruleController.addAction(action);
        }
        for (Rule rule : rules) {
            ruleController.addRule(rule);
        }
    }

    private List<Rule> getAllRules() {
        List<Rule> rules = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM rule where creator_email = ?");
            statement.setString(1, ruleController.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String triggerName = resultSet.getString("trigger_name");
                String actionName = resultSet.getString("action_name");
                boolean multiUse = resultSet.getBoolean("multiuse");
                Date lastUse = resultSet.getDate("lastuse");
                int sleepTime = resultSet.getInt("sleeptime");
                boolean active = resultSet.getBoolean("active");
                Rule rule = new Rule(name, triggerName, actionName, active, multiUse, lastUse, sleepTime);
                rules.add(rule);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rules;
    }

    public List<Triggers> getAllTriggers() {
        List<Triggers> triggers = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM triggers WHERE creator_email = ?");
            statement.setString(1, ruleController.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                Triggers trigger = getTriggerByTypeDB(name, type);
                triggers.add(trigger);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return triggers;
    }

    private Triggers getTriggerByTypeDB(String name, String type) {
        switch (type) {
            case FileSize.type:
                FileSize fileSize = null;
                try {
                    Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM filesize_trigger WHERE name = ?");
                    statement.setString(1, name);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        String filePath = resultSet.getString("file_path");
                        long sizeThreshold = resultSet.getLong("sizethreshold");
                        File file = new File(filePath);
                        fileSize = new FileSize(name, file, sizeThreshold);
                    }
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return fileSize;
            case FileExistence.type:
                FileExistence fileExistence = null;
                try {
                    Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM fileexistence_trigger WHERE name = ?");
                    statement.setString(1, name);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        String filePath = resultSet.getString("file_path");
                        File file = new File(filePath);
                        fileExistence = new FileExistence(name, file);
                    }
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return fileExistence;
            case DayOfMonth.type:
                DayOfMonth dayOfMonth = null;
                try {
                    Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM dayofmonth_trigger WHERE name = ?");
                    statement.setString(1, name);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        int day = resultSet.getInt("day");
                        dayOfMonth = new DayOfMonth(name, day);
                    }
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return dayOfMonth;
            case DayOfWeek.type:
                DayOfWeek dayOfWeek = null;
                try {
                    Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM dayofweek_trigger WHERE name = ?");
                    statement.setString(1, name);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        String day = resultSet.getString("day");
                        dayOfWeek = new DayOfWeek(name, DaysOfWeekEnum.valueOf(day));
                    }
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return dayOfWeek;
            case ExternalProgram.type:
                ExternalProgram externalProgram = null;
                try {
                    Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM externalprogram_trigger WHERE name = ?");
                    statement.setString(1, name);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        String externalProgramPath = resultSet.getString("externalprogram");
                        String commandLineArguments = resultSet.getString("commandlinearguments");
                        int exitStatus = resultSet.getInt("exitstatus");
                        File externalProgramFile = new File(externalProgramPath);
                        externalProgram = new ExternalProgram(name, externalProgramFile, commandLineArguments, exitStatus);
                    }
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return externalProgram;
            case TimeOfDay.type:
                TimeOfDay timeOfDay = null;
                try {
                    Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM timeofday_trigger WHERE name = ?");
                    statement.setString(1, name);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        int hour = resultSet.getInt("hours");
                        int minute = resultSet.getInt("minutes");
                        timeOfDay = new TimeOfDay(name, hour, minute);
                    }
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return timeOfDay;
            case AND.type:
                AND and = null;
                try {
                    Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM and_trigger WHERE name = ?");
                    statement.setString(1, name);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        String firstTriggerName = resultSet.getString("first_trigger_name");
                        String secondTriggerName = resultSet.getString("second_trigger_name");
                        and = new AND(name, firstTriggerName, secondTriggerName, ruleController.getId());
                    }
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return and;
            case OR.type:
                OR or = null;
                try {
                    Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM or_trigger WHERE name = ?");
                    statement.setString(1, name);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        String firstTriggerName = resultSet.getString("first_trigger_name");
                        String secondTriggerName = resultSet.getString("second_trigger_name");
                        or = new OR(name, firstTriggerName, secondTriggerName, ruleController.getId());
                    }
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return or;
            case NOT.type:
                NOT not = null;
                try {
                    Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM not_trigger WHERE name = ?");
                    statement.setString(1, name);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        String triggerName = resultSet.getString("trigger_name");
                        not = new NOT(name, triggerName, ruleController.getId());
                    }
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return not;
            default:
                return null;
        }
    }

    public List<Actions> getAllActions() {
        List<Actions> actions = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM actions where creator_email = ?");
            statement.setString(1, ruleController.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                Actions action = getActionByTypeDB(name, type);
                actions.add(action);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actions;
    }

    private Actions getActionByTypeDB(String name, String type) {
        switch (type) {
            case DeleteFile.type:
                Actions deleteFile = null;
                try {
                    Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM deletefile_action WHERE name = ?");
                    statement.setString(1, name);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        String fileToDeletePath = resultSet.getString("filetodelete_path");
                        File fileToDelete = new File(fileToDeletePath);
                        deleteFile = new DeleteFile(name, new File(fileToDelete.getAbsolutePath()));
                    }
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return deleteFile;
            case MoveFile.type:
                Actions moveFile = null;
                try {
                    Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM movefile_action WHERE name = ?");
                    statement.setString(1, name);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        String sourcePath = resultSet.getString("filetomove_path");
                        String destinationPath = resultSet.getString("destinationpath_path");
                        File source = new File(sourcePath);
                        File destination = new File(destinationPath);
                        moveFile = new MoveFile(name, new File(source.getAbsolutePath()), new File(destination.getAbsolutePath()));
                    }
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return moveFile;
            case CombinedActions.type:
                CombinedActions combinedActions = null;
                try {
                    Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM combinedactions_action WHERE name = ?");
                    statement.setString(1, name);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        String firstActionName = resultSet.getString("first_action_name");
                        String secondActionName = resultSet.getString("second_action_name");
                        combinedActions = new CombinedActions(name, firstActionName, secondActionName, ruleController.getId());
                    }
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return combinedActions;
            case AppendStringToFile.type:
                AppendStringToFile appendStringToFile = null;
                try {
                    Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM appendstringtofile_action WHERE name = ?");
                    statement.setString(1, name);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        String stringToAppend = resultSet.getString("message");
                        String fileToAppendPath = resultSet.getString("file_path");
                        File fileToAppend = new File(fileToAppendPath);
                        appendStringToFile = new AppendStringToFile(name, stringToAppend, new File(fileToAppend.getAbsolutePath()));
                    }
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return appendStringToFile;
            case PasteFile.type:
                Actions pasteFile = null;
                try {
                    Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM pastefile_action WHERE name = ?");
                    statement.setString(1, name);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        String sourcePath = resultSet.getString("filetopaste_path");
                        String destinationPath = resultSet.getString("destinationpath_path");
                        File source = new File(sourcePath);
                        File destination = new File(destinationPath);
                        pasteFile = new PasteFile(name, new File(source.getAbsolutePath()), new File(destination.getAbsolutePath()));
                    }
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return pasteFile;
            case StartExternalProgram.type:
                StartExternalProgram startExternalProgram = null;
                try {
                    Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM startexternalprogram_action WHERE name = ?");
                    statement.setString(1, name);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        String externalProgramPath = resultSet.getString("externalprogram");
                        String commandLineArguments = resultSet.getString("commandlinearguments");
                        File externalProgram = new File(externalProgramPath);
                        startExternalProgram = new StartExternalProgram(name, new File(externalProgram.getAbsolutePath()), commandLineArguments);
                    }
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return startExternalProgram;
            default:
                return null;
        }
    }

}
