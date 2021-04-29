package util;

import java.sql.*;
import java.util.ResourceBundle;

public class DBUtil {
    static ResourceBundle rb;

    private DBUtil() {
    }

    static {
        rb = ResourceBundle.getBundle("JDBC");
        try {
            Class.forName(rb.getString("driver"));
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                rb.getString("url"), rb.getString("username"),
                rb.getString("password")
        );
    }


    public static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
