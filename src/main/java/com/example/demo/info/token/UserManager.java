package com.example.demo.info.token;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserManager {
    private final Map<Integer, String> loggedInUsers = new ConcurrentHashMap<>();

    public void addUser(Integer userId, String token) {
        loggedInUsers.put(userId, token);
    }

    public void removeUser(Integer userId, String token) {
        loggedInUsers.remove(userId, token);
    }

    public String getToken(Integer userId) {
        return loggedInUsers.get(userId);
    }

    public boolean isUserLoggedIn(Integer userId) {
        return loggedInUsers.containsKey(userId);
    }


    public Map<Integer, String> getLoggedInUsers() {
        return loggedInUsers;
    }

    public void logoutUser(Integer userId, String token) {
        if (loggedInUsers.containsKey(userId)) {
            loggedInUsers.remove(userId, token);
        }
    }
}
