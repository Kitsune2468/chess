package service;

import java.util.UUID;

public class UserService {

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
