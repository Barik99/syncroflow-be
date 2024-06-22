package vlad.mester.syncroflowbe.services;

import vlad.mester.syncroflowbe.RuleController;

import java.sql.*;

public class LoginService {

    public static String login(String email, String password) {
        try {
            Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
            PreparedStatement preparedStatement = prepareStatementUserAccount(email, password, connection);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                connection.close();
                return "Autentificare reușită!";
            }else {
                connection.close();
                return "Email sau parolă incorectă!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "A apărut o eroare la autentificare.";
        }
    }

    public static String register(String email, String password) {
        try {
            Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
            PreparedStatement preparedStatement = prepareStatementUserAccount(email, password, connection);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                connection.close();
                return "Există deja un cont cu acest email!";
            } else {
                preparedStatement = connection.prepareStatement("INSERT INTO person (email, password_hash) VALUES (?, ?)");
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                preparedStatement.executeUpdate();

                RuleController ruleController = RuleController.createInstance(email);

                RuleControllerManager.ruleControllers.put(email, ruleController);

                connection.close();
                return "Contul a fost creat cu succes!";
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                return "Există deja un cont creat pe acest email!";
            }
            e.printStackTrace();
            return "A apărut o eroare la înregistrare.";
        }
    }

    private static PreparedStatement prepareStatementUserAccount(String email, String password, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = null;
        preparedStatement = connection.prepareStatement("SELECT * FROM person WHERE email = ? AND password_hash = ?");
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        return preparedStatement;
    }
}
