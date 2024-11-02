package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;


public class SQLUserDAOTest {
    UserDAO userDAO;
    UserData testUser;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        userDAO = new SQLUserDAO();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE users")) {
                statement.executeUpdate();
            }
        }

        testUser = new UserData("username", "password", "email");
    }

    @AfterEach
    void cleanUp() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE users")) {
                statement.executeUpdate();
            }
        }
    }

    @Test
    void addUser() throws DataAccessException, SQLException {
        userDAO.addUser(testUser);

        String resultUsername;
        String resultPassword;
        String resultEmail;

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT username, hashedPassword, email FROM users WHERE username=?")) {
                statement.setString(1, testUser.username());
                try (var results = statement.executeQuery()) {
                    results.next();
                    resultUsername = results.getString("username");
                    resultPassword = results.getString("hashedPassword");
                    resultEmail = results.getString("email");
                }
            }
        }

        Assertions.assertEquals(testUser.username(), resultUsername);
        boolean passwordsMatch = false;

        passwordsMatch = verifyUser(testUser.username(),testUser.password());

        Assertions.assertTrue(passwordsMatch);
        Assertions.assertEquals(testUser.email(), resultEmail);
    }

    @Test
    void getUserPositive() throws DataAccessException {
        userDAO.addUser(testUser);

        UserData resultUser = userDAO.getUserByUsername(testUser.username());
        boolean passwordsMatch= verifyUser(testUser.username(),testUser.password());

        Assertions.assertEquals(testUser.username(), resultUser.username());
        Assertions.assertTrue(passwordsMatch);
        Assertions.assertEquals(testUser.email(), resultUser.email());
    }

    @Test
    void getUserNoUsers() {
        try {
            UserData testData = userDAO.getUserByUsername(testUser.username());
            if (testData == null) {
                Assertions.assertTrue(true);
            } else {
                Assertions.fail();
            }
        } catch (DataAccessException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void clear() throws DataAccessException, SQLException {
        userDAO.addUser(testUser);
        userDAO.clear();

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT username, hashedPassword, email FROM users WHERE username=?")) {
                statement.setString(1, testUser.username());
                try (var results = statement.executeQuery()) {
                    Assertions.assertFalse(results.next()); //There should be no elements
                }
            }
        }
    }

    boolean verifyUser(String username, String providedClearTextPassword) {
        // read the previously hashed password from the database
        boolean passwordsMatch;
        try {
            UserData testUser = userDAO.getUserByUsername(username);
            String hashedPassword = testUser.password();

            if (testUser.password().equals(providedClearTextPassword)) {
                passwordsMatch = true;
            } else {
                passwordsMatch = BCrypt.checkpw(providedClearTextPassword, hashedPassword);
            }
        } catch (Exception e) {
            return false;
        }
        return passwordsMatch;
    }
}
