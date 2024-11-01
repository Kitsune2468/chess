package dataaccess;

import model.AuthData;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthDAO {
    private ArrayList<AuthData> memoryAuths;

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public AuthData addAuth(String hashedUsername) throws DataAccessException {
        String token = generateToken();
        AuthData newAuth = new AuthData(token, hashedUsername);

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
    public AuthData getAuthByToken(String token) {
        AuthData foundAuth = null;
        for(AuthData searchAuth : memoryAuths) {
            String searchToken = searchAuth.authToken();
            if (searchToken.equals(token)) {
                foundAuth = searchAuth;
            }
        }
        return foundAuth;
    }

    @Override
    public AuthData getAuthByUsername(String hashedUsername) {
        AuthData foundAuth = null;
        /*for(AuthData searchAuth : memoryAuths) {
            String searchUsername = searchAuth.username();
            if (searchUsername.equals(username)) {
                foundAuth = searchAuth;
            }
        }*/
        return foundAuth;
    }

    @Override
    public boolean deleteAuthByToken(String token) {
        /*for(AuthData searchAuth : memoryAuths) {
            String searchToken = searchAuth.authToken();
            if (searchToken.equals(token)) {
                memoryAuths.remove(searchAuth);
                return true;
            }
        }*/
        return false;
    }

    @Override
    public void clear() throws DataAccessException{
        var statement = "TRUNCATE auth";
        executeUpdate(statement);
    }

    @Override
    public boolean isEmpty() {
        /*if (memoryAuths.isEmpty()) {
            return true;
        } else {
            return false;
        }*/
        return false;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    //if (param instanceof String p) ps.setString(i + 1, p);
                    //else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    //else if (param instanceof PetType p) ps.setString(i + 1, p.toString());
                    //else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
              `token` varchar(256) NOT NULL,
              `hashedUsername` varchar(256) NOT NULL,
              PRIMARY KEY (`token`),
              INDEX(token),
              INDEX(hashedUsername)
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
