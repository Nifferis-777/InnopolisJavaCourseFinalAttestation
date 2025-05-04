package example.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnect {
    private String connectionString;
    private String login;
    private String password;

    public DbConnect() {
        loadProperties();
    }

    private void loadProperties() {
        Properties properties = new Properties();

        try (InputStream input = DbConnect.class.getClassLoader().getResourceAsStream("dbconfig.properties")) {
            if (input == null) {
                System.err.println("Ошибка при загрузке конфигурации: dbconfig.properties не найден");
                return;
            }
            properties.load(input);
            connectionString = properties.getProperty("db.url");
            login = properties.getProperty("db.user");
            password = properties.getProperty("db.password");
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке конфигурации: " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString, login, password);
    }
}