package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Simple DB connection helper. Edit the configuration values to match your MySQL setup.
 */
public class DBConnection {
    // Update these values for your environment
    public static String HOST = "localhost";
    public static String PORT = "3306";
    public static String DATABASE = "quanlysinhvien";
    public static String USER = "root";
    public static String PASSWORD = "1234";

    private static String getJdbcUrl() {
        return "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false&serverTimezone=UTC";
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getJdbcUrl(), USER, PASSWORD);
    }
}
