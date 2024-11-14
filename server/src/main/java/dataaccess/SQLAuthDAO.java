package dataaccess;

import model.AuthData;

import java.sql.*;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        configureAuthDatabase();
    }

    @Override
    public AuthData addAuth(String username) throws DataAccessException {
        String token = generateToken();
        AuthData newAuth = new AuthData(token, username);

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("INSERT INTO auth (username, authToken) VALUES(?, ?)")) {
                statement.setString(1, newAuth.username());
                statement.setString(2, newAuth.authToken());
                statement.executeUpdate();
                return newAuth;
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthData getAuthByToken(String token) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT username, authToken FROM auth WHERE authToken=?")) {
                statement.setString(1, token);
                try (var results = statement.executeQuery()) {
                    results.next();
                    var username = results.getString("username");
                    return new AuthData(token, username);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unauthorized");
        }
    }

    @Override
    public AuthData getAuthByUsername(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT username, authToken FROM auth WHERE username=?")) {
                statement.setString(1, username);
                try (var results = statement.executeQuery()) {
                    results.next();
                    var authToken = results.getString("authToken");
                    return new AuthData(authToken, username);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unauthorized");
        }
    }

    @Override
    public boolean deleteAuthByToken(String token) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("DELETE FROM auth WHERE authToken=?")) {
                statement.setString(1, token);
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted == 0) {
                    throw new DataAccessException("Unauthorized");
                }
                return true;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Token does not exist");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE auth")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Clear auth failed");
        }
    }

    @Override
    public boolean isAuthEmpty() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT count(*) AS authCount FROM auth")) {
                try (var results = statement.executeQuery()) {
                    results.next();
                    int authCount = results.getInt("authCount");
                    if (authCount == 0) {
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

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    private final String[] createAuthStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(authToken),
              INDEX(username)
            )
            """
    };

    private void configureAuthDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createAuthStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure authdatabase: %s", e.getMessage()));
        }
    }
}
