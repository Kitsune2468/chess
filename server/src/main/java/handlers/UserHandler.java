package handlers;

import service.DataBaseService;
import service.UserService;

public class UserHandler {
    UserService userService;
    public UserHandler(UserService inputUserService) {
        userService = inputUserService;
    }
}
