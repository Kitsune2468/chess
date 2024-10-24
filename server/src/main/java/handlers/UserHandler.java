package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.NotAuthorizedException;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import service.DataBaseService;
import service.UserService;
import spark.Request;
import spark.Response;

public class UserHandler {
    UserService userService;
    public UserHandler(UserService inputUserService) {
        userService = inputUserService;
    }

    public Object addUser(Request request, Response result) throws NotAuthorizedException {
        String resultBody;
        UserData userData = new Gson().fromJson(request.body(), UserData.class);

        if (userData.username() == null || userData.password() == null || userData.email() == null) {
            result.status(400);
            resultBody = "bad request";
            return resultBody;
        }

        try {
            AuthData newAuth = userService.addUser(userData);
            result.status(200);
            resultBody = new Gson().toJson(newAuth);
        } catch (DataAccessException e) {
            if (e.getMessage().toString().equals("already taken")) {
                result.status(403);
            }
            result.status(500);
            resultBody = "{ \"message\": \"Error: "+e.getMessage()+"\" }";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return resultBody;
    }

    public Object login(Request request, Response result) throws NotAuthorizedException {
        String resultBody;
        UserData userData = new Gson().fromJson(request.body(), UserData.class);

        if (userData.username() == null || userData.password() == null) {
            throw new NotAuthorizedException("bad request");
        }

        try {
            AuthData newAuth = userService.login(userData);
            result.status(200);
            resultBody = new Gson().toJson(newAuth);
        } catch (DataAccessException e) {
            if (e.getMessage().toString().equals("unauthorized")) {
                result.status(401);
            } else {
                result.status(500);
            }

            resultBody = "{ \"message\": \"Error: "+e.getMessage()+"\" }";
        }
        return resultBody;
    }

    public Object logout(Request request, Response result) throws NotAuthorizedException {
        String resultBody;
        String authToken = request.headers("authorization");

        try {
            userService.logout(authToken);
            result.status(200);
            resultBody = "{}";
        } catch (DataAccessException e) {
            if (e.getMessage().toString().equals("unauthorized")) {
                result.status(401);
            } else {
                result.status(500);
            }

            resultBody = "{ \"message\": \"Error: "+e.getMessage()+"\" }";
        }
        return resultBody;
    }

}
