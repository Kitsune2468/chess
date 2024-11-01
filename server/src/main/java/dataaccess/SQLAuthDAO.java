package dataaccess;

import model.AuthData;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
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
                    return new AuthData(username, token);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Auth token does not exist");
        }
    }

    @Override
    public AuthData getAuthByUsername(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT username, authToken FROM auth WHERE hashedUsername=?")) {
                statement.setString(1, username);
                try (var results = statement.executeQuery()) {
                    results.next();
                    var authToken = results.getString("authToken");
                    return new AuthData(username, authToken);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Username does not exist");
        }
    }

    @Override
    public boolean deleteAuthByToken(String token) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("DELETE username, authToken FROM auth WHERE authToken=?")) {
                statement.setString(1, token);
                statement.executeUpdate();
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
    public boolean isEmpty() throws DataAccessException {
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

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
              `token` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`token`),
              INDEX(token),
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
