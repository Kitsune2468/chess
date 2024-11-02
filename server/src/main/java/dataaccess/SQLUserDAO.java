package dataaccess;

import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void addUser(UserData userData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("INSERT INTO users (username, hashedPassword, email) VALUES(?, ?, ?)")) {
                statement.setString(1, userData.username());
                statement.setString(2, userData.password());
                statement.setString(3, userData.email());
                statement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData getUserByUsername(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT username, hashedPassword, email FROM users WHERE username=?")) {
                statement.setString(1, username);
                try (var results = statement.executeQuery()) {
                    results.next();
                    String hashedPassword = results.getString("hashedPassword");
                    String email = results.getString("email");
                    return new UserData(username, hashedPassword, email);
                }
            }
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE users")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Clear auth failed");
        }
    }

    @Override
    public boolean isEmpty() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT count(*) AS usersCount FROM users")) {
                try (var results = statement.executeQuery()) {
                    results.next();
                    int usersCount = results.getInt("usersCount");
                    if (usersCount == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to check if auth was empty");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
              `username` varchar(256) NOT NULL,
              `hashedPassword` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(username)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

}
