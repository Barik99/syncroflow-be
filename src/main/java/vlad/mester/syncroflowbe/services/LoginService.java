package vlad.mester.syncroflowbe.services;

import java.sql.*;

public class LoginService {

    public static boolean login(String email, String password) {
        try {
            Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
            PreparedStatement preparedStatement = prepareStatementUserAccount(email, password, connection);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                connection.close();
                return true;
            }else {
                connection.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean register(String email, String password) {
        try {
            Connection connection = DriverManager.getConnection(DataBase.URL.toString(), DataBase.USERNAME.toString(), DataBase.PASSWORD.toString());
            PreparedStatement preparedStatement = prepareStatementUserAccount(email, password, connection);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                connection.close();
                return false;
            }else {
                preparedStatement = connection.prepareStatement("INSERT INTO person (email, password_hash) VALUES (?, ?)");
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                preparedStatement.executeUpdate();
                connection.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static PreparedStatement prepareStatementUserAccount(String email, String password, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = null;
        preparedStatement = connection.prepareStatement("SELECT * FROM person WHERE email = ? AND password_hash = ?");
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        return preparedStatement;
    }
}
