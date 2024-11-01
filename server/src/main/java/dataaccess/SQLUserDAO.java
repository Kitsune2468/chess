package dataaccess;

import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLUserDAO implements UserDAO {
    private ArrayList<UserData> memoryUsers;

    public SQLUserDAO() throws DataAccessException {
        memoryUsers = new ArrayList<UserData>();
        configureDatabase();
    }

    @Override
    public void addUser(UserData userData) throws DataAccessException {
        memoryUsers.add(userData);
    }

    @Override
    public UserData getUserByUsername(String name) throws DataAccessException {
        UserData foundUser = null;
        for(UserData searchUser : memoryUsers) {
            String searchName =searchUser.username().toString();
            if (searchName.equals(name)) {
                return searchUser;
            }
        }
        return foundUser;
    }

    @Override
    public void clear() throws DataAccessException {
        memoryUsers.clear();
    }

    @Override
    public boolean isEmpty() throws DataAccessException {
        if (memoryUsers.isEmpty()) {
            return true;
        } else {
            return false;
        }
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
            CREATE TABLE IF NOT EXISTS  pet (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(type),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
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

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        SQLUserDAO that = (SQLUserDAO) o;
        return Objects.equals(memoryUsers, that.memoryUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(memoryUsers);
    }

    @Override
    public String toString() {
        return "MemoryUserDAO{" +
                "memoryUsers=" + memoryUsers +
                '}';
    }
}
