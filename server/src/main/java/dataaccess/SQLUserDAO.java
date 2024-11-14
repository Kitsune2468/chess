package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        configureUserDatabase();
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
    public boolean isUsersEmpty() throws DataAccessException {
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

    private final String[] createUserStatements = {
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

    private void configureUserDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createUserStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure userdatabase: %s", e.getMessage()));
        }
    }

}
