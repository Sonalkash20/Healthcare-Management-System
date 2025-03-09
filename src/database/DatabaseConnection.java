package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Method to establish a connection to the MySQL database
    public static Connection connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/mediCarePlus";  // Your database URL (replace mediCarePlus with your DB name)
        String username = "root";  // Default MySQL username for XAMPP
        String password = "";      // Default MySQL password for XAMPP is empty

        // Establish and return the connection
        return DriverManager.getConnection(url, username, password);
    }
}

