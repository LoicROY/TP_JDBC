package fr.diginamic.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionService {
    private static ConnectionService instance;

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    private Connection connection;

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("database");
        URL = bundle.getString("local_url");
        USER = bundle.getString("local_user");
        PASSWORD = bundle.getString("local_password");
    }

    private ConnectionService() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static ConnectionService getInstance() throws DaoException {
        if (instance == null) {
            try {
                instance = new ConnectionService();
            } catch (SQLException e) {
                throw new DaoException("Database connection failed");
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
