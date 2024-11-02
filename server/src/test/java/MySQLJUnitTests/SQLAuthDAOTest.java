package MySQLJUnitTests;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.*;
import passoff.model.*;
import server.Server;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SQLAuthDAOTest {

    AuthDAO authDAO;

    AuthData testAuth;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        authDAO = new SQLAuthDAO();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE auth")) {
                statement.executeUpdate();
            }
        }

        testAuth = new AuthData("token", "username");
    }

    @AfterEach
    void tearDown() throws SQLException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE auth")) {
                statement.executeUpdate();
            }
        }
    }

    @Test
    void addAuth() throws DataAccessException, SQLException {
        authDAO.addAuth(testAuth.username());

        String resultUsername;
        String resultToken;

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT username, authToken FROM auth WHERE username=?")) {
                statement.setString(1, testAuth.username());
                try (var results = statement.executeQuery()) {
                    results.next();
                    resultUsername = results.getString("username");
                }
            }
        }

        Assertions.assertEquals(testAuth.username(), resultUsername);
    }

    @Test
    void addAuthSameUser() throws DataAccessException, SQLException {
        authDAO.addAuth(testAuth.username());
        authDAO.addAuth(testAuth.username());

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT username, authToken FROM auth WHERE username=?")) {
                statement.setString(1, testAuth.username());
                try (var results = statement.executeQuery()) {
                    results.next();
                    Assertions.assertTrue(true);
                }
            }
        }
    }

    @Test
    void deleteAuth() throws DataAccessException, SQLException {
        AuthData testData = authDAO.addAuth(testAuth.username());

        authDAO.deleteAuthByToken(testData.authToken());

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT username, authToken FROM auth WHERE username=?")) {
                statement.setString(1, testAuth.username());
                try (var results = statement.executeQuery()) {
                    Assertions.assertFalse(results.next());
                }
            }
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void deleteAuthBadToken() throws DataAccessException, SQLException {
        try {
            authDAO.deleteAuthByToken("badToken");
            Assertions.fail();
        } catch (Exception e){
            Assertions.assertTrue(true);
        }
    }

    @Test
    void getAuth() throws DataAccessException {
        AuthData testData = authDAO.addAuth(testAuth.username());
        AuthData result = authDAO.getAuthByToken(testData.authToken());
        Assertions.assertEquals(testAuth.username(), result.username());
    }

    @Test
    void getAuthBadToken() throws DataAccessException{
        authDAO.addAuth(testAuth.username());
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.getAuthByToken("badToken"));
    }

    @Test
    void clear() throws DataAccessException, SQLException {
        authDAO.addAuth(testAuth.username());
        authDAO.clear();

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT username, authToken FROM auth WHERE username=?")) {
                statement.setString(1, testAuth.username());
                try (var results = statement.executeQuery()) {
                    Assertions.assertFalse(results.next()); //There should be no elements
                }
            }
        }
    }
}
