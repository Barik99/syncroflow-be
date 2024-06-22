package vlad.mester.syncroflowbe.services;

import org.springframework.stereotype.Service;
import vlad.mester.syncroflowbe.base.Rule;

import java.sql.*;

@Service
public class RuleService {

    public boolean addRule(Rule rule, String email) {
        if (checkIfRuleExists(rule.getName())) {
            return true;
        }
        try {
            Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
            PreparedStatement preparedStatement = prepareStatementRule(rule, connection, email);
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteRule(String ruleName) {
        try {
            Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM rule WHERE name = ?");
            preparedStatement.setString(1, ruleName);
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkIfRuleExists(String ruleName) {
        try {
            Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM rule WHERE name = ?");
            preparedStatement.setString(1, ruleName);
            ResultSet resultSet = preparedStatement.executeQuery();
            connection.close();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private PreparedStatement prepareStatementRule(Rule rule, Connection connection, String email) throws SQLException {
        PreparedStatement preparedStatement = null;
        preparedStatement = connection.prepareStatement("INSERT INTO rule (name, trigger_name, action_name, multiuse, lastuse, sleeptime, active, creator_email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        preparedStatement.setString(1, rule.getName());
        preparedStatement.setString(2, rule.getTrigger());
        preparedStatement.setString(3, rule.getAction());
        preparedStatement.setBoolean(4, rule.isMultiUse());
        preparedStatement.setDate(5, (Date) rule.getLastUse());
        preparedStatement.setInt(6, rule.getSleepTime());
        preparedStatement.setBoolean(7, rule.isActive());
        preparedStatement.setString(8, email);
        return preparedStatement;
    }
}
